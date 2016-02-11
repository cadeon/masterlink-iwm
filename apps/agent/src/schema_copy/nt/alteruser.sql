spool d:\oracle\xe\SIM_LOGS\alteruser.log
set echo on
set serveroutput on 
show user;
""
alter user mytest identified by mytest ; 
spool off 
exit 
