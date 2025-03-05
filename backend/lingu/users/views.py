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
import pefile



class DownloadView(APIView):
    def get(self, request):
        user_id = request.GET.get('userid')
        if not user_id:
            return Response("Benutzer-ID fehlt", status=400)

        original_exe_path = os.path.join(settings.BASE_DIR, "wrapper", "linguExplorer_original.exe")
        personalized_exe_path = os.path.join(settings.BASE_DIR, "wrapper", "linguExplorer_personalized.exe")

        # Erstelle temporäre Konfigurationsdatei
        with tempfile.NamedTemporaryFile(mode='w+', delete=False, suffix='.txt') as config_file:
            config_file.write(f"userid={user_id}\n")
            config_file_path = config_file.name

        # Einbetten der Konfiguration in die .exe-Datei
        self.embed_config(original_exe_path, config_file_path, personalized_exe_path)

        response = FileResponse(open(personalized_exe_path, 'rb'), content_type='application/octet-stream')
        response['Content-Disposition'] = f'attachment; filename="linguExplorer.exe"'
        return response

    def embed_config(self, original_exe, config_file, output_exe):
        # Lese Konfigurationsdaten
        with open(config_file, "rb") as f:
            config_data = f.read()

        # Öffne die .exe-Datei und füge die Konfiguration als Ressource hinzu
        pe = pefile.PE(original_exe)
        pe.add_resource(config_data, pefile.RESOURCE_TYPE["RT_RCDATA"], "CONFIG")
        pe.write(output_exe)
        pe.close()





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

