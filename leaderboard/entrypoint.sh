#!/bin/sh
set -e

python /app/src/db_create.py

exec python /app/src/app.py
