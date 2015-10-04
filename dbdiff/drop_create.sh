#!/bin/bash
PGPASSWORD=postgres psql -U postgres -h localhost -c "select pg_terminate_backend(pid) from pg_stat_activity where datname='brief';"

PGPASSWORD=postgres dropdb -e -U postgres -w -h localhost brief

PGPASSWORD=postgres createdb -e -U postgres -w -h localhost brief

