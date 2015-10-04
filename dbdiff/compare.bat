@echo off
echo # --- !Ups
pg_dump --schema-only --no-owner -U postgres -h localhost -s -f new.sql eps
pg_dump --schema-only --no-owner -U postgres -h localhost -s -f original.sql neweps
java -jar apgdiff-2.4.jar --ignore-start-with original.sql new.sql

echo # --- !Downs
java -jar apgdiff-2.4.jar --ignore-start-with new.sql original.sql

