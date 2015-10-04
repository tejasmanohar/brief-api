#!/bin/bash
PGPASSWORD=postgres psql -U postgres -h localhost -c "select pg_terminate_backend(pid) from pg_stat_activity where datname='newbrief';"
PGPASSWORD=postgres psql -U postgres -h localhost -c "drop database brief;"
PGPASSWORD=postgres psql -U postgres -h localhost -c "select pg_terminate_backend(pid) from pg_stat_activity where datname='brief';"
PGPASSWORD=postgres psql -U postgres -h localhost -c "CREATE DATABASE newbrief OWNER postgres;"
