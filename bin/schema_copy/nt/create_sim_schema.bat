rem  create new sim schema ON Windows NT
rem this script requires four parameters that must be provided at startup
rem  in the exact order!!:
rem  SimSource SimSourcePass SimDest SimDestPass
  
rem  to run this script enter the command followed by four parameters separated by spaces
rem  example:
rem  create_sim_schema.bat name1 pass1 name2 pass2
 
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
set SIM_LOGS=d:\oracle\xe\app\oracle\admin\XE\sim_logs

echo SimSource=%SimSource%
echo SimDest=%SimDest%
echo %ORACLE_SID%
echo %ORACLE_HOME%
echo %ORACLE_BASE%

Rem Create LOG Folders

MD %ORACLE_BASE%\SIM_LOGS 

set SIM_LOGS=%ORACLE_BASE%\SIM_LOGS 
echo %SIM_LOGS%

REM create a new schema  and Load data into objects

%ORACLE_HOME%\bin\impdp %SimSource%/%SimSourcePass% SCHEMAS=%SimSource% REMAP_SCHEMA=%SimSource%:%SimDest% parfile=create_sim_schema.par
 
REM  move log file from base directory

copy d:\oracle\xe\app\oracle\admin\XE\dpdump\create_sim_schema_impdp.log d:\oracle\xe\SIM_LOGS\import_sim_schema.log

REM construct  alter user script using DEST variables

echo spool d:\oracle\xe\SIM_LOGS\alteruser.log>alteruser.sql
echo set echo on>>alteruser.sql
echo set serveroutput on >>alteruser.sql
echo show user;>>alteruser.sql
echo "">>alteruser.sql
echo alter user %SimDest% identified by %SimDestPass% ; >>alteruser.sql
echo spool off >>alteruser.sql
echo exit >>alteruser.sql

 
REM  run alter user script and assign password

%ORACLE_HOME%\bin\sqlplus %SimSource%/%SimSourcePass%@%ORACLE_SID% @alteruser.sql
 

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
echo *** JOB COMPLETED.

rem *** clear settings
set SimSource=
set SimDest=
set SimSourcePass=
set SimDestPass=

rem *** eof
:eof
