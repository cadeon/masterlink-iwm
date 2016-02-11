--sqlplus /nolog
----
--this script need to be run only one on your local XE db 
--to create supporting structures for impdp job create_sim_schema.bat
--please edit   user name and password, 
--you can enter the user_name and passowrd for your base sim schema or
--user system and the password that you specified when XE was installed
--to run the script connect to sqlplus, sqldeveloper or  toad as a priviledged user
--  execute the script. 

 
set echo on
set serveroutput on
spool impdp_prereqs.log

CREATE   PUBLIC DATABASE LINK mylocal_db
CONNECT TO mlink_sim IDENTIFIED BY mlink_sim USING 'xe'
;

CREATE DIRECTORY dpump_import AS 'D:\oracle\xe\app\oracle\admin\XE\dpdump'
;
GRANT READ, WRITE ON DIRECTORY dpump_import TO mlink_sim
;

spool off
exit


