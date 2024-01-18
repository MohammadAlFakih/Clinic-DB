set LAB=.
set SERV=-u root -p76716011samra321$
set DBApp=clinic_db
echo Beginning...

mysql %SERV% -e "CREATE DATABASE IF NOT EXISTS clinic_db;"
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createTables.sql
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createViews.sql
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createTriggers.sql
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createIndexes.sql
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createProcedures.sql
mysql %SERV% %DBApp% < %LAB%/SQL/%DBApp%_createFunctions.sql



echo End of batch file...