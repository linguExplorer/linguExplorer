from django.shortcuts import render
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
        raise AuthenticationFailed('Link ist ungültig')


    return redirect('http://localhost:8080/anmelden')


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

