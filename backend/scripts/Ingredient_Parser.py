import csv

s = set()
f = open('ingredients.csv', 'r')
for x in f.read().split(','):
    s.add(x)


def parse(input_string):
    ingredients = []
    for x in input_string.split('\n'):
        if(s in x):
            ingredients.append(x)
    
    return ingredients