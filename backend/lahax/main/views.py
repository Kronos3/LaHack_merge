import json

import httplib2
from django.core.files.storage import FileSystemStorage
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse, HttpResponseNotAllowed
from django.shortcuts import render
from oauth2client.client import OAuth2WebServerFlow
from django.conf import settings
import django.views

from .models import *


def get_login(request):
    if 'access_token' not in request.session:
        return HttpResponse('', status=403)
    
    return JsonResponse(request.user.get_json())


def start_search(request, search_type: str):
    if request.method != "GET":
        return HttpResponse(status=400)
    
    search_tokens = request.GET.get('search').split(",")
    search = Search.start_search(search_tokens, search_type.upper())
    
    search.save()
    
    return JsonResponse({"search_id": search.id})


def poll_search(request, search_id):
    if search_id == 'test':
        return JsonResponse(Recipe.objects.all()[0].get_json())
    
    try:
        search = Search.objects.get(id=search_id)
    except Search.DoesNotExist:
        return JsonResponse({})
    
    recipe_found = search.poll()
    
    if recipe_found is None:
        return JsonResponse({})
    else:
        return JsonResponse(recipe_found.get_json())


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
        credentials.authorize(http)

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


def image_process_get(request, process_id):
    try:
        process = ImageProcess.objects.get(id=process_id)
        search = process.start()
        
        return JsonResponse({'search_id': search.id})
    except ImageProcess.DoesNotExist:
        return HttpResponse(status=404)


def image_process(request, detection):
    if request.method == 'POST':
        uploaded_files = []
        
        for file in request.FILES:
            fs = FileSystemStorage()
            filename = fs.save("temp/%s" % request.FILES[file].name, request.FILES[file])
            uploaded_files.append(fs.url(filename))
        
        img_p = ImageProcess.new_from_files(detection, uploaded_files)
        img_p.save()
        
        return HttpResponse(content=img_p.id)
    
    return render(request, 'upload.html')


def image_process_text(request):
    return image_process(request, 'T')


def image_process_obj(request):
    return image_process(request, 'O')


def user_data(request):
    if request.method != 'GET':
        return HttpResponse(status=400)
    
    user_token = request.GET.get('user_token')
    action = request.GET.get('action')
    
    try:
        user = RecipeUser.objects.get(user_token=user_token)
    except RecipeUser.DoesNotExist:
        user = RecipeUser.new_from_token(user_token)

    if action == "get":  # No editing is needed
        pass
    elif action == "add":
        recipe_id = request.GET.get('recipe_id')
        user.add_recipe(recipe_id)
    elif action == "remove":
        recipe_id = request.GET.get('recipe_id')
        user.remove_recipe(recipe_id)
    
    return JsonResponse(user.get_json(), safe=False)


def request_token(request):
    if request.method != 'GET':
        return HttpResponse(status=400)
    
    return HttpResponse(content=RecipeUser.new_token())
