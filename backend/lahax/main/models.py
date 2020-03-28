import requests
from bs4 import BeautifulSoup
from django.contrib.auth.base_user import AbstractBaseUser
from django.db import models
from datetime import datetime


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
parse_csv("../RAW_recipes.csv")
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
    
    found_recipes = models.ManyToManyField(Recipe)
    sent = models.IntegerField(default=0)
    keyword = models.CharField(max_length=256)
    
    @staticmethod
    def start_search(argument: str, search_type='K'):
        
        
        return Search(search_type=search_type, found_recipes=[], sent=0, keyword=argument)


class User(AbstractBaseUser):
    email = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,
    )
    
    name = models.CharField(max_length=1024)
    picture = models.URLField()
    access_token = models.CharField(max_length=64)
    
    recipes = models.ManyToManyField(Recipe)
    
    def get_json(self):
        recipe_jsons = []
        for r in self.recipes.all():
            recipe_jsons.append(r.get_json())
        
        return {
            'email': self.email,
            'name': self.name,
            'picture': self.picture,
            'recipes': recipe_jsons
        }
    
    @staticmethod
    def get_from_access_token(access_token: str):
        r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo?access_token=%s' % access_token)
        
        info = r.json()
        
        found_user = User.objects.filter(email__exact=info['email'])
        if len(found_user) != 0:
            found_user = found_user[0]
            found_user.access_token = access_token
            return found_user
        else:
            new_user = User(email=info['email'], name=info['name'], picture=info['picture'], access_token=access_token)
            new_user.save()
            
            return new_user
