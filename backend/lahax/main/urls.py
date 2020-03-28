from django.conf.urls import url
from django.urls import path
from django.views.decorators.csrf import csrf_exempt

from . import views

urlpatterns = [
	path('', views.index, name='index'),
	path('login-meta', views.get_login, name='login-meta'),
	url('oauth/$', csrf_exempt(views.OAuth.as_view()), name='oauth'),
	url('oauth2-callback/$', views.OAuth2CallBack.as_view(), name='oauth2_callback'),
]