from django.conf.urls import url
from django.urls import path
from django.views.decorators.csrf import csrf_exempt

from . import views

urlpatterns = [
	path('', views.index, name='index'),
	path('api/login-meta', views.get_login, name='login-meta'),
	url('^api/search/start/(?P<search_type>[k|i|t])/?search=df,as,d,d$', csrf_exempt(views.start_search), name='start_search'),
	url('^api/search/poll/(?P<search_id>[0-9A-z]+)/$', csrf_exempt(views.poll_search), name='poll_search'),
	url('oauth/$', csrf_exempt(views.OAuth.as_view()), name='oauth'),
	url('oauth2-callback/$', views.OAuth2CallBack.as_view(), name='oauth2_callback'),
]