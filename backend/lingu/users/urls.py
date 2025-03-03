from django.urls import path
from .views import RegisterView, LoginView, UserView, LogoutView, UpdateUsernameView, ResendEmail, download_lingu_explorer
from . import views
urlpatterns = [
    path('register', RegisterView.as_view()),
    path('login', LoginView.as_view()),
    path('user', UserView.as_view()),
    path('logout', LogoutView.as_view()),
    path('update-username', UpdateUsernameView.as_view()),
    path('resend', ResendEmail.as_view()),
    path('download', views.download_lingu_explorer),


    path('activate/<uidb64>/<token>', views.activate, name='activate')
]