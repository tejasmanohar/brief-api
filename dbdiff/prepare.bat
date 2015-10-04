set PGPASSWORD=postgres
psql -U postgres -h localhost -c "select pg_terminate_backend(pid) from pg_stat_activity where datname='neweps';"
psql -U postgres -h localhost -c "drop database neweps;"
psql -U postgres -h localhost -c "select pg_terminate_backend(pid) from pg_stat_activity where datname='eps';"
psql -U postgres -h localhost -c "CREATE DATABASE neweps WITH TEMPLATE eps OWNER postgres;"
