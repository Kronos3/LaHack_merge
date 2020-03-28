import json

import httplib2
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse, HttpResponseNotAllowed
from oauth2client.client import OAuth2WebServerFlow
from django.conf import settings
import django.views

from .models import User


def get_login(request):
    if 'access_token' not in request.session:
        return HttpResponse('', status=403)
    
    request.user = User.get_from_access_token(request.session['access_token'])
    return JsonResponse(request.user.get_json())


def search_ingredient(request):
    pass


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
        return HttpResponseRedirect("/")

    @staticmethod
    def post(request, *args, **kwargs):
        return HttpResponseNotAllowed('Only GET requests!')


def index(request):
    return HttpResponse("")
