from django.contrib.auth.models import User
import django.utils.timezone
from django.db import models


class Tag(models.Model):
	name = models.CharField(max_length=256)


class Ingredient:
	name = models.CharField(max_length=128)


class Recipe(models.Model):
	#  name,id,minutes,contributor_id,submitted,tags,nutrition,n_steps,steps,description,ingredients,n_ingredients
	
	name = models.CharField(max_length=128)
	id = models.CharField(max_length=128)
	minutes = models.IntegerField()
	contributor_id = models.CharField(max_length=128)
	submitted = models.DateField()
	
	tags = models.ManyToManyField(Tag)
	
	ingredients = models.ManyToManyField(Ingredient)
	image_cache = models.URLField()
	
	rating = models.FloatField()
