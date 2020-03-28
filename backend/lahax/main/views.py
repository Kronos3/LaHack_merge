import json

import httplib2
from django.http import HttpResponse, StreamingHttpResponse, HttpResponseRedirect, JsonResponse, HttpResponseNotAllowed
import google.oauth2.credentials
import google_auth_oauthlib.flow
from oauth2client.client import OAuth2WebServerFlow
from django.conf import settings
import django.views

# Use the client_secret.json file to identify the application requesting
# authorization. The client ID (from that file) and access scopes are required.
flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
    'client_secret.json',
    ['https://www.googleapis.com/auth/drive.metadata.readonly'])

# Indicate where the API server will redirect the user after the user completes
# the authorization flow. The redirect URI is required. The value must exactly
# match one of the authorized redirect URIs for the OAuth 2.0 client, which you
# configured in the API Console. If this value doesn't match an authorized URI,
# you will get a 'redirect_uri_mismatch' error.
flow.redirect_uri = 'https://www.example.com/oauth2callback'


def login(request):
    token_request_uri = "https://accounts.google.com/o/oauth2/auth"
    response_type = "code"
    redirect_uri = "http://mysite/login/google/auth"
    scope = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email"
    url = ("{token_request_uri}?response_type={response_type}&client_id={client_id}&redirect_uri={"
           "redirect_uri}&scope={scope}").format(
        token_request_uri=token_request_uri,
        response_type=response_type,
        client_id=client_id,
        redirect_uri=redirect_uri,
        scope=scope)
    return HttpResponseRedirect(url)


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
    def get(self, request, *args, **kwargs):
        build_flow = BuildFlow()
        generated_url = build_flow.flow.step1_get_authorize_url()
        return HttpResponseRedirect(generated_url)


class OAuth2CallBack(django.views.View):
    def get(self, request, *args, **kwargs):
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

    def post(self, request, *args, **kwargs):
        return HttpResponseNotAllowed('Only GET requests!')
