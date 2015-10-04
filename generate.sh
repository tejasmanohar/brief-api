cd dbdiff
bash compare.sh > diff.sql
cd ..	

cd conf/evolutions/default
	LIST=`exec ls $MY_DIR | sed 's/\([0-9]\+\).*/\1/g' | tr '\n' ' '`
	LIST=($LIST)
	newfile="`ls -l|wc -l|xargs`.sql"
	echo "Next sql file is: $newfile"
	cd ../../..
	
	if `diff dbdiff/empty.sql dbdiff/diff.sql  >/dev/null` ; then
		echo -e "\x1B[92mNo changes to the database is detected, so no evolution script added...\x1B[0m"
		
	else
		echo -e "\x1B[92mDatabase changed, new sql script created...\x1B[0m"
		echo -e "\x1B[92m"
		cat dbdiff/diff.sql
		echo -e "\x1B[0m"
		cp -pvr dbdiff/diff.sql conf/evolutions/default/$newfile
	fi
