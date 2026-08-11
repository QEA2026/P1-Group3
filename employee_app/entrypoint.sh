#!/bin/sh
set -e

# seed.py handles existing data itself, so it's safe to run on every start
python db/seed.py

exec gunicorn --bind 0.0.0.0:8080 app:app