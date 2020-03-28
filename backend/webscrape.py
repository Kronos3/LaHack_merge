#Install lxml, requests, beatifulsoup4, pandas

import requests
from bs4 import BeautifulSoup

#Returns in order, # of stars, # of review, image link (None if no image)
def webscrape(_id):
    url    = 'https://www.food.com/recipe/' + str(_id)
    user_agent  = {'User-Agent': 'Mozilla/5.0'}

    r = requests.get(url, headers=user_agent)
    soup = BeautifulSoup(r.text, 'lxml')

    stars_n = None
    reviews_n = None
    if soup.find('a', {'class':'no-reviews__link theme-color'}) == None:
        stars_string = soup.find("div", {"class":"stars-rate__filler"})['style']
        stars_n = (float(stars_string[6 : 6 + stars_string[6:].find('%')])/100)*5
        reviews_n = int(soup.find("a", {"class":"reviews-count__link theme-color"}).contents[0][1:-1])

    img = soup.find('meta', {'name':'og:image'})['content']
    if img == 'https://geniuskitchen.sndimg.com/fdc-new/img/fdc-shareGraphic.png':
        img = None

    return stars_n, reviews_n, img

def main(argv):
    print(webscrape(argv[1]))

import sys
main(sys.argv)