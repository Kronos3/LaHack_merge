import json

import httplib2
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse, HttpResponseNotAllowed
from oauth2client.client import OAuth2WebServerFlow
from django.conf import settings
import django.views

from .models import *


def get_login(request):
    if 'access_token' not in request.session:
        return HttpResponse('', status=403)
    
    return JsonResponse(request.user.get_json())


def start_search(request, search_type):
    if request.method != "GET":
        return HttpResponse(status=400)
    print(request.GET)
    search_tokens = request.GET.get('search')
    
    return JsonResponse({"search_id": "test"})

def poll_search(request, search_id):
    if search_id == 'test':
        return JsonResponse(Recipe.objects.all()[0].get_json())

class BuildFlow:
    def __init__(self):
        self.flow = OAuth2WebServerFlow(settings.CLIENT_ID,
                                        settings.CLIENT_SECRET,
                                        scope='https://www.googleapis.com/auth/userinfo.profile '
                                              'https://www.googleapis.com/auth/userinfo.email',
                                        redirect_uri=settings.REDIRECT_URI)


class OAuth(django.views.View):
    @staticmethod
    def get(request, *args, **kwargs):
        build_flow = BuildFlow()
        generated_url = build_flow.flow.step1_get_authorize_url()
        return HttpResponseRedirect(generated_url)


class OAuth2CallBack(django.views.View):
    @staticmethod
    def get(request, *args, **kwargs):
        code = request.GET.get('code', False)
        if not code:
            return JsonResponse({'status': 'error, no access key received from Google or User declined permission!'})

        oauth2 = BuildFlow()
        credentials = oauth2.flow.step2_exchange(code)

        http = httplib2.Http()
        http = credentials.authorize(http)

        credentials_js = json.loads(credentials.to_json())
        access_token = credentials_js['access_token']
        
        # Store the access token in case we need it again!
        request.session['access_token'] = access_token
        request.user = MetaUser.from_access_token(access_token)
        
        print("Successful login for %s" % request.user)
        
        return HttpResponseRedirect("/")

    @staticmethod
    def post(request, *args, **kwargs):
        return HttpResponseNotAllowed('Only GET requests!')


def index(request):
    return HttpResponse("")
