from django.conf.urls import url
from django.urls import path
from django.views.decorators.csrf import csrf_exempt

from . import views

urlpatterns = [
	path('', views.index, name='index'),
	path('api/login-meta', views.get_login, name='login-meta'),
	url('^api/search/start/(?P<search_type>[k|i|t])/$', csrf_exempt(views.start_search), name='start_search'),
	url('^api/search/poll/(?P<search_id>[0-9A-z]+)/$', csrf_exempt(views.poll_search), name='poll_search'),
	url('oauth/$', csrf_exempt(views.OAuth.as_view()), name='oauth'),
	url('api/process/start/$', csrf_exempt(views.image_process), name='process_start'),
	url('api/process/get/(?P<process_id>[0-9A-z]+)/$', csrf_exempt(views.image_process_get), name='process_get'),
	url('oauth2-callback/$', views.OAuth2CallBack.as_view(), name='oauth2_callback'),
	url('api/user_data/$', csrf_exempt(views.user_data), name='user_data'),
	url('api/user_data/request/$', csrf_exempt(views.request_token), name='request_token')
]