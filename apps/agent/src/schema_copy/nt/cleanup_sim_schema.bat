rem  cleanup unused sim schema for Windows NT
rem this script requires four parameters that must be provided at startup:
rem  SimSource SimSourcePass SimDest SimDestPass
  
rem  to run this script enter the command followed by four parameters separated by spaces
rem in the exact order!!
rem example:
rem  cleanup_sim_schema.bat name1 pass1 name2 pass2
 
@echo off
@if not "%OS%"=="Windows_NT" goto helpmsg

rem Input Parms; if input parms are not provided the script cannot proceed
if x%1==x goto helpmsg
if x%2==x goto helpmsg
if x%3==x goto helpmsg
if x%4==x goto helpmsg

set SimSource=%1
set SimSourcePass=%2
set SimDest=%3
set SimDestPass=%4

set ORACLE_SID=XE
set ORACLE_HOME=d:\oracle\xe\app\oracle\product\10.2.0\server
set ORACLE_BASE=d:\oracle\xe

echo SimSource=%SimSource%
echo SimDest=%SimDest%
echo %ORACLE_SID%
echo %ORACLE_HOME%
echo %ORACLE_BASE%

REM cleanup sim schema  and drop schema  objects

REM construct  alter user script using DEST variables

echo spool d:\oracle\xe\SIM_LOGS\cleanup_user.log>cleanup_user.sql 
echo set echo on >>cleanup_user.sql 
echo set serveroutput on>>cleanup_user.sql 
echo select sysdate from dual;>>cleanup_user.sql 
echo show user;>>cleanup_user.sql 
echo drop  user %SimDest% cascade ; >>cleanup_user.sql 
echo alter tablespace users coalesce;>>cleanup_user.sql 
echo alter tablespace iwm_data coalesce;>>cleanup_user.sql 
echo alter tablespace iwm_indx coalesce;>>cleanup_user.sql 
echo spool off >>cleanup_user.sql 
echo exit >>cleanup_user.sql 

 
REM  run drop user script  

%ORACLE_HOME%\bin\sqlplus %SimSource%/%SimSourcePass%@%ORACLE_SID% @cleanup_user.sql 
 

goto exitjob


:helpmsg
echo  Must be Windows_NT   or Missing Simulator Parameters
echo .
echo Simulator requires the following Parameters to create a new schema
echo   [SimSource] [SimSourcePass] [SimDest] [SimDestPass]
echo .


rem *** exit
:exitjob
echo .
echo *** CLEANUP JOB COMPLETED.

rem *** clear settings
set SimSource=
set SimDest=
set SimSourcePass=
set SimDestPass=

rem *** eof
:eof
