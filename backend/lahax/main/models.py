import django
import requests
from bs4 import BeautifulSoup
from django.contrib.auth.base_user import AbstractBaseUser
from django.contrib.auth.models import User
from django.db import models
from django.db.models import Q
from datetime import datetime

from django.db.models.signals import post_save
from django.dispatch import receiver

from .util import powerset


class Tag(models.Model):
    @staticmethod
    def create(name):
        tags = Tag.objects.filter(name__exact=name)
        if len(tags) == 0:
            t = Tag(name=name)
            print(name)
            t.save()
            return t
        
        return tags[0]
    
    name = models.CharField(max_length=256)


class Ingredient(models.Model):
    @staticmethod
    def create(name):
        ingredients = Ingredient.objects.filter(name__exact=name)
        if len(ingredients) == 0:
            ingredients = Ingredient(name=name)
            ingredients.save()
            return ingredients
        
        return ingredients[0]
    
    name = models.CharField(max_length=256)


class Recipe(models.Model):
    name = models.CharField(max_length=1024)
    recipe_id = models.CharField(max_length=128, primary_key=True)
    minutes = models.CharField(max_length=128)
    contributor_id = models.CharField(max_length=128)
    submitted = models.DateField()
    
    tags = models.ManyToManyField(Tag)
    
    ingredients = models.ManyToManyField(Ingredient)
    
    image_cache = models.URLField(null=True)
    rating = models.FloatField()
    rating_n = models.IntegerField()
    
    @staticmethod
    def parse(row):
        # name,id,minutes,contributor_id,submitted,tags,nutrition,n_steps,steps,description,ingredients,n_ingredients
        
        name, _id, _min, contrib, submit, tags, nut, n_steps, steps, desc, ing, n_ing = row
        print(name, _id)
        submit = datetime.strptime(submit, "%Y-%m-%d")
        
        r = Recipe(name=name,
                   recipe_id=_id, minutes=_min, contributor_id=contrib, submitted=submit, image_cache=None, rating=0,
                   rating_n=0)
        
        r.save()
        
        for t in eval(tags):
            r.tags.add(Tag.create(t))
        
        for i in eval(ing):
            r.ingredients.add(Ingredient.create(i))
        
        r.save()
        
        return r
    
    def fill_metadata(self):
        url = 'https://www.food.com/recipe/' + str(self.recipe_id)
        user_agent = {'User-Agent': 'Mozilla/5.0'}
        
        r = requests.get(url, headers=user_agent)
        soup = BeautifulSoup(r.text, 'lxml')
        
        stars_f = 0.0
        reviews_n = 0
        if soup.find('a', {'class': 'no-reviews__link theme-color'}) is None:
            stars_string = soup.find("div", {"class": "stars-rate__filler"})['style']
            stars_f = (float(stars_string[6: 6 + stars_string[6:].find('%')]) / 100) * 5
            reviews_n = int(soup.find("a", {"class": "reviews-count__link theme-color"}).contents[0][1:-1])
        
        img = soup.find('meta', {'name': 'og:image'})['content']
        if img == 'https://geniuskitchen.sndimg.com/fdc-new/img/fdc-shareGraphic.png':
            img = None
        
        self.image_cache = img
        self.rating = stars_f
        self.rating_n = reviews_n
    
    def get_json(self):
        if self.image_cache is None:
            self.fill_metadata()
        
        if self.image_cache is None:
            print("Failed to get image for '%s' id '%s'" % (self.name, self.id))
        
        tag_list = []
        for tag in self.tags.all():
            tag_list.append({
                "id": tag.id,
                "name": tag.name
            })
        
        ingredient_list = []
        for ingredient in self.ingredients.all():
            ingredient_list.append({
                "id": ingredient.id,
                "name": ingredient.name
            })
        
        return {
            "id": self.recipe_id,
            "name": self.name,
            "minutes": self.minutes,
            "contributor_id": self.contributor_id,
            "submitted": self.submitted.strftime("%Y-%m-%d"),
            "tags": tag_list,
            "ingredients": ingredient_list,
            "image": self.image_cache if self.image_cache is not None else "",
            "rating": self.rating,
            "rating_n": self.rating_n
        }


"""
from main.script import *
g = Search.start_search(["corn", "butter", "beef"], 'I')
"""

class Search(models.Model):
    SEARCH_TYPES = (
        ('T', 'Tag search'),
        ('I', 'Ingredient search'),
        ('K', 'Keyword search'),
    )
    
    search_type = models.CharField(
        max_length=1,
        choices=SEARCH_TYPES,
        default='K',
    )
    
    sent = models.IntegerField(default=0)
    keywords_cat = models.TextField()
    powerset_index = models.IntegerField(default=0)

    @staticmethod
    def start_search(arguments, search_type='K'):
        s = Search(search_type=search_type, sent=0, keywords_cat=",".join(arguments))
        s.save()
        
        return s
    
    @staticmethod
    def generate_search_query() -> Q:
        pass
    
    def poll(self, n):
        recipes = []
        
        keyword_powerset = list(powerset(self.keywords_cat.split(",")))
        keyword_powerset.sort(key=len, reverse=True)
        keyword_powerset = keyword_powerset[:-1]  # Dont do the empty set
        
        print(keyword_powerset)
        
        while len(recipes) < n:
            for i, keys in enumerate(keyword_powerset, self.powerset_index):
                query = Q()
        
        """
        if self.search_type == 'T':
            tags = []
            for x in arguments:
                tags.extend(Tag.objects.filter(name__iexact=x))
            for t in tags:
                recipes.append(t.recipe_set.all())           
        elif search_type == 'I':
            ingredients = []
            for x in arguments:
                ingredients.extend(Ingredient.objects.filter(name__iexact=x))
            for i in ingredients:
                recipes.append(i.recipe_set.all())
        else:
            for x in arguments:
                recipes.append(Recipe.objects.filter(name__iexact=x))
        print(len(recipes))

        rs_list = []
        r_list = []
        
        for sets in recipes:
            print(len(sets))
            for r in sets:
                if r not in r_list:
                    r_list.append(r)
                    rs_list.append(RecipeSearch(parent_search=s, parent_recipe=r, matches=1))
                else:
                    rs_list[r_list.index(r)].matches += 1

        for rs in rs_list:
            rs.save()

        s.save()
        
        return s
        """
    

class RecipeSearch(models.Model):
    parent_search = models.ForeignKey(Search, on_delete=models.CASCADE)
    parent_recipe = models.ForeignKey(Recipe, on_delete=models.CASCADE)
    matches = models.IntegerField(default=0)

    def __lt__(self, other):
        if self.matches != other.matches:
            return self.matches < other.matches
        if self.parent_recipe.rating != other.parent_recipe.rating:
            return self.parent_recipe.rating < other.parent_recipe.rating
        if self.parent_recipe.rating_n != other.parent_recipe.rating_n:
            return self.parent_recipe.rating_n < other.parent_recipe.rating_n
        return self.parent_recipe.name < other.parent_recipe.name


class MetaUser(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    picture = models.URLField()
    access_token = models.CharField(max_length=64)
    
    recipes = models.ManyToManyField(Recipe)
    
    def get_json(self):
        recipe_jsons = []
        for r in self.recipes.all():
            recipe_jsons.append(r.get_json())
        
        return {
            'email': self.user.email,
            'name': "%s %s" % (self.user.first_name, self.user.last_name),
            'picture': self.picture,
            'recipes': recipe_jsons
        }
    
    @staticmethod
    def from_access_token(access_token: str):
        r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo?access_token=%s' % access_token)
        info = r.json()
        
        try:
            found_user = User.objects.get(email__exact=info['email'])
            return found_user
        except django.contrib.auth.models.User.DoesNotExist:  # Not found
            new_user = User(username=info['email'], email=info['email'], first_name=info['given_name'], last_name=info['family_name'])

            new_user.save()
            
            new_user.metauser.picture = info['picture']
            new_user.metauser.access_token = access_token
            
            new_user.save()
    
            return new_user
    
    def update(self, access_token: str):
        r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo?access_token=%s' % access_token)
        
        info = r.json()
        
        self.user.email = info['email']
        self.user.first_name = info['given_name']
        self.user.last_name = info['family_name']
        self.picture = info['picture']
        self.access_token = access_token


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        MetaUser(user=instance, picture="", access_token="")


@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.metauser.save()
