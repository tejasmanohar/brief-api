#!/bin/bash
echo "# --- !Ups"
PGPASSWORD=postgres pg_dump --schema-only --no-owner -U postgres -h localhost -s -f new.sql brief
PGPASSWORD=postgres pg_dump --schema-only --no-owner -U postgres -h localhost -s -f original.sql newbrief 2>/dev/null
unset JAVA_TOOL_OPTIONS && java -jar apgdiff-2.4.jar --ignore-start-with original.sql new.sql

echo "# --- !Downs"
unset JAVA_TOOL_OPTIONS && java -jar apgdiff-2.4.jar --ignore-start-with new.sql original.sql
