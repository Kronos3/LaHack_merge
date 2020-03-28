#!/bin/bash

cd "$( dirname $0 )"

source ./venv/bin/activate
uwsgi --ini uwsgi.ini
