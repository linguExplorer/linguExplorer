from django.db import models
from django.contrib.auth.models import AbstractUser
from django.dispatch import receiver
from django.urls import reverse
from django_rest_passwordreset.signals import reset_password_token_created
from django.core.mail import send_mail, EmailMessage

# Create your models here.
class User(AbstractUser):
    name = models.CharField(max_length=55)
    email = models.CharField(max_length=55, unique=True)
    password = models.CharField(max_length=255)
    username = None
    username_changed_at = models.DateTimeField(null=True, blank=True)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []


@receiver(reset_password_token_created)
def password_reset_token_created(sender, instance, reset_password_token, *args, **kwargs):

    email_plaintext_message = "Klicken Sie auf den Link um ihr Passwort zurückzusetzen" + " " + "{}{}".format(instance.request.build_absolute_uri("http://localhost:8080/newPassword/"), reset_password_token.key)
    
    send_mail(
        # title:
        "Password zurücksetzen  {title}".format(title="Crediation portal account"),
        # message:
        email_plaintext_message,
        # from:
        #"team@linguexplorer.com",
        None,
        # to:
        [reset_password_token.user.email],
        fail_silently=False,
    )