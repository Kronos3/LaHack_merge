from django.urls import path
from django.contrib.auth import views as auth_views
from django.conf.urls import url
from django.views.decorators.csrf import csrf_exempt


from . import views

urlpatterns = [
	url('oauth/$', csrf_exempt(OAuth.as_view()), name='oauth'),
	url('oauth2-callback/$', OAuth2CallBack.as_view(), name='oauth2_callback'),
]