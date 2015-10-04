rm -rf target/universal/
$ACTIVATOR./activator dist 
cd target/universal/
unzip brief-back-end-1.0-SNAPSHOT.zip
cd brief-back-end-1.0-SNAPSHOT/bin
./brief-back-end -Dhttp.port=8080


