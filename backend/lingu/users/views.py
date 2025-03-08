from django.shortcuts import render
from django.conf import settings
from rest_framework.views import APIView
from .serializers import UserSerializer
from .serializers import UsernameSerializer
from rest_framework.response import Response
from django.template.loader import render_to_string
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.utils.encoding import force_bytes, force_str
from django.core.mail import EmailMessage
from django.shortcuts import redirect
from django.contrib.sites.shortcuts import get_current_site
from rest_framework.exceptions import AuthenticationFailed
from django.contrib.auth import get_user_model
from rest_framework import status
from datetime import timedelta
from django.utils import timezone
import jwt, datetime
from .models import User
from .tokens import account_activation_token
from django.http import FileResponse
from django.shortcuts import get_object_or_404
import os
import tempfile
import subprocess
import uuid
import shutil


class DownloadView(APIView):
    def get(self, request):
        # Benutzer-ID aus den GET-Parametern holen
        user_id = request.GET.get('userid')
        if not user_id:
            return Response("Benutzer-ID fehlt", status=400)

        # Pfade definieren
        cs_source_path = os.path.join(settings.BASE_DIR, "wrapper", "Program.cs")  # C#-Quelldatei
        resource_path = os.path.join(settings.BASE_DIR, "wrapper", "linguExplorer.exe")  # Eingebettete Ressource
        output_dir = os.path.join(settings.BASE_DIR, "wrapper", "output")  # Ausgabeverzeichnis
        os.makedirs(output_dir, exist_ok=True)  # Erstelle das Ausgabeverzeichnis, falls nicht vorhanden
        output_exe = os.path.join(output_dir, f"linguExplorer_personalized_{uuid.uuid4()}.exe")  # Eindeutiger Dateiname

        # EXE-Datei erstellen
        try:
            self.build_exe(cs_source_path, user_id, output_exe, resource_path)
        except subprocess.CalledProcessError as e:
            return Response(f"Fehler beim Erstellen der .exe-Datei: {e}", status=500)

        # EXE-Datei als Download bereitstellen
        try:
            response = FileResponse(open(output_exe, "rb"), content_type="application/octet-stream")
            response["Content-Disposition"] = f'attachment; filename="linguExplorer.exe"'
            return response
        finally:
            # Temporäre Dateien löschen
            os.remove(output_exe)

    def build_exe(self, cs_source_path, user_id, output_exe, resource_path):
        """
        Erstellt eine .exe-Datei, indem die Benutzer-ID in den C#-Code eingebettet wird.
        """
        # Erstelle ein temporäres Verzeichnis für das .NET-Projekt
        project_dir = os.path.join(settings.BASE_DIR, "wrapper", "temp_project")
        os.makedirs(project_dir, exist_ok=True)

        # Kopiere die C#-Quelldatei in das temporäre Verzeichnis
        temp_cs_source_path = os.path.join(project_dir, "Program.cs")
        shutil.copy(cs_source_path, temp_cs_source_path)

        # Ersetze die Benutzer-ID im C#-Code
        with open(temp_cs_source_path, "r") as f:
            code = f.read()
        new_code = code.replace("private const int USER_ID = 123456789;", f"private const int USER_ID = {user_id};")
        with open(temp_cs_source_path, "w") as f:
            f.write(new_code)

        # Kopiere die Ressource in das temporäre Verzeichnis
        shutil.copy(resource_path, os.path.join(project_dir, "linguExplorer.exe"))

        # Erstelle eine .csproj-Datei für das Projekt
        csproj_content = """
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <OutputType>WinExe</OutputType> <!-- Windows-Anwendung (kein Konsolenfenster) -->
    <TargetFramework>net8.0</TargetFramework>
    <PublishSingleFile>true</PublishSingleFile>
    <SelfContained>true</SelfContained>
    <RuntimeIdentifier>win-x64</RuntimeIdentifier>
  </PropertyGroup>
  <ItemGroup>
    <EmbeddedResource Include="linguExplorer.exe" />
  </ItemGroup>
</Project>
        """
        with open(os.path.join(project_dir, "WrapperProject.csproj"), "w") as f:
            f.write(csproj_content)

        # Kompiliere das Projekt und packe es in ein Single-File-Format
        subprocess.run(["dotnet", "publish", "-c", "Release", "-o", os.path.dirname(output_exe)], cwd=project_dir, check=True)

        # Verschiebe die generierte .exe-Datei in das gewünschte Verzeichnis
        dist_exe = os.path.join(os.path.dirname(output_exe), "WrapperProject.exe")
        os.replace(dist_exe, output_exe)

        # Lösche das temporäre Projektverzeichnis
        shutil.rmtree(project_dir)


def activate(request, uidb64, token):
    User = get_user_model()
    try:
        uid = force_str(urlsafe_base64_decode(uidb64))
        user = User.objects.get(pk=uid)
    except:
        user = None
    
    if user is not None and account_activation_token.check_token(user, token):
        user.is_active = True
        user.save()
    else:
        return redirect('https://linguexplorer.com?error=invalid-link')


    return redirect('https://linguexplorer.com/anmelden')


class ResendEmail(APIView):
    def post(self, request):

        email = request.data.get('email')
        user = User.objects.filter(email=email).first()
        activateEmail(request, user)
        return Response({'message': 'Activation email sent successfully'}, status=status.HTTP_200_OK)


def activateEmail(request, user):
    mail_subject = "Aktiviere deinen Account!"
    message = render_to_string("template_activate_account.html", {
        'user': user.name,
        'domain': get_current_site(request).domain,
        'uid': urlsafe_base64_encode(force_bytes(user.pk)),
        'token': account_activation_token.make_token(user),
        "protocol": 'https' if request.is_secure() else 'hhtp'
    })
    email = EmailMessage(mail_subject, message, to=[user.email])
    if not email.send():
        raise AuthenticationFailed('Problem beim senden der E-mail an {user.email}')




class UpdateUsernameView(APIView):
    def patch(self, request):
        token = request.COOKIES.get('jwt')
        try:
            payload = jwt.decode(token, 'secret',  algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Unatuhifi')
    
        user = User.objects.filter(id=payload['id']).first()

        if user.username_changed_at:
            time_difference = timezone.now() - user.username_changed_at
            if time_difference < timedelta(hours=24):
                return Response({
                    "error": "Du kannst deinen Benutzernamen nur alle 24 Stunden ändern."
                }, status=status.HTTP_400_BAD_REQUEST)

        serializer = UsernameSerializer(user, data=request.data, partial=True)

        if serializer.is_valid():
            serializer.save()
            user.username_changed_at = timezone.now()
            user.save()
            return Response({"message": "Benutzername erfolgreich geändert"}, status=status.HTTP_200_OK)


        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


        

class RegisterView(APIView):
    def post(self, request):
        serializer= UserSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.save()
        activateEmail(request, user)
        return Response(serializer.data)
    
class LoginView(APIView):
    def post(self,request):
        email= request.data['email']
        password=request.data['password']

        user = User.objects.filter(email=email).first()

        if user is None:
            raise AuthenticationFailed('User not found!')
        
        if not user.check_password(password):
            raise AuthenticationFailed('Incorrect password!')
        
        if not user.is_active:
            raise AuthenticationFailed('User ist nicht aktiv!')


        
        payload = {
            'id': user.id,
            'exp': datetime.datetime.now(datetime.timezone.utc) + datetime.timedelta(minutes=60),
            'iat':datetime.datetime.now(datetime.timezone.utc)
        }

        token = jwt.encode(payload, 'secret', algorithm='HS256')


        response = Response()

        response.set_cookie(key='jwt', value=token, httponly=True)
        response.data = {
            
            'jwt': token
        }
        return response
        

class UserView(APIView):
    def get(self, request):
        token = request.COOKIES.get('jwt')

        if not token:
            raise AuthenticationFailed('Unatuhifi')
        
        try:
            payload = jwt.decode(token, 'secret',  algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Unatuhifi')
        
        user = User.objects.filter(id=payload['id']).first()
        serializer = UserSerializer(user)

        return Response(serializer.data)
    

class LogoutView(APIView):
    def post(self, request):
        response = Response()
        response.delete_cookie('jwt')
        response.data = {
            'message': 'success'
        }
        return response;

