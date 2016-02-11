#! /usr/bin/ksh
#########################################################################
#   clean up unused sim schemas                                         #
#  CONNECT TO DB AND DROP UNUSED SIM SCHEMA                             #
# THIS SCRIPT REQUIRES FOUR PARAMETERS IN THE FOLLOWING ORDER!          #
#                                                                       #
# SIM_SOURCE SIM_SOURCE_PASS SIM_DEST SIM_DEST_PASS                     #
#  EXAMPLE:                                                             #
#  cleanup_sim_schema.sh name1 pass1 name2 pass2                        #
# ATTN:                                                                 #
#    if input parameters are not provided the script cannot proceed     #
#    if parameter order is wrong it may invalidate the source schema    #
#                                                                       #
#  master copy of  these scripts for Linux is located on siren          #
#  in /opt/oracle/dbtools/simload/scripts directory                     #
#  the final location may change later                                  #
#  you DO NOT  need to copy these scripts  to linux                     #
#  these files are  provided for your reference                         #
#                                                                       #
######################################################################### 

if [ "$#" -ne 4 ]
	then

	echo "\n Missing parameter \n"
	echo "\n IWM + SCHEMA Creation Requires the following Parameters \n"
	echo "\n   [SIM_SOURCE] [SIM_SOURCE_PASS] [SIM_DEST] [SIM_DEST_PASS]\n"
	exit

fi

SIM_SOURCE=$1
export SIM_SOURCE

SIM_SOURCE_PASS=$2
export SIM_SOURCE_PASS

SIM_DEST=$3
export SIM_DEST

SIM_DEST_PASS=$4
export SIM_DEST_PASS

export DB="mlinkdb"

. /opt/oracle/dbtools/simload/common/passwd_impdp.ksh

# 
OUTF=${0}.out

if [ -f "$OUTF" ]
then
        rm -f "$OUTF"
fi

{ 

echo "`date` ----------------Beginning of IMPDP JOB ------------"
echo "Script name: $0"
 
#---------------------------------------------------------------
#   LOCAL HOST NAME
#--------------------------------------------------------
LOCAL_HOST=$LOCAL_HOST
#echo " "
echo $LOCAL_HOST
echo " "

# ---------------------------------------------------------------------------
# Oracle DBA user id (account).
# ---------------------------------------------------------------------------

ORACLE_USER=$ORACLE_USER
export ORACLE_USER
ORACLE_SID=$DB

SIM_SCRIPTS=/opt/oracle/dbtools/simload/scripts
SIM_LOGS=/opt/oracle/dbtools/simload/logs
echo " "
# ---------------------------------------------------------------------------
# Print out the value of the variables set by this script.
# ---------------------------------------------------------------------------                
echo " "
echo   "LOCAL_HOST:  $LOCAL_HOST"
echo " "
echo   "ORACLE_USER:  $ORACLE_USER"
echo   "USER:  $USER"
echo   "ROLE:  $ROLE"
echo   "SIM_SOURCE:  $SIM_SOURCE" 
echo   "SIM_DEST: $SIM_DEST"
echo " " 
echo   "ORACLE_SID: $ORACLE_SID" 
echo   "ORACLE_HOME: $ORACLE_HOME"
echo   "TNS_ADMIN: $TNS_ADMIN"
echo   "SIM_SCRIPTS: $SIM_SCRIPTS"
echo   "SIM_LOGS: $SIM_LOGS"
echo   "PATH: $PATH"
echo " "

#
QOUTE="'"
ORACLE_SID=$DB
ORACLE_HOME=$ORACLE_HOME
PATH=$ORACLE_HOME/bin:$PATH
export ORACLE_SID
export ORACLE_HOME
export USER
export PASS
export DB
export ROLE
export QUOTE
export LOCAL_HOST

## cleanup sim schema  and drop schema  objects

## construct  alter user script using DEST variables

echo "spool $SIM_LOGS/cleanup_user.log">$SIM_SCRIPTS/cleanup_user.sql 
echo "set echo on" >>$SIM_SCRIPTS/cleanup_user.sql 
echo "set serveroutput on">>$SIM_SCRIPTS/cleanup_user.sql 
echo "select sysdate from dual;">>$SIM_SCRIPTS/cleanup_user.sql 
echo "show user;">>$SIM_SCRIPTS/cleanup_user.sql 
echo "drop  user $SIM_DEST cascade ; ">>$SIM_SCRIPTS/cleanup_user.sql 
echo "alter tablespace users coalesce;">>$SIM_SCRIPTS/cleanup_user.sql 
echo "alter tablespace iwm_data coalesce;">>$SIM_SCRIPTS/cleanup_user.sql 
echo "alter tablespace iwm_indx coalesce;">>$SIM_SCRIPTS/cleanup_user.sql 
echo "spool off ">>$SIM_SCRIPTS/cleanup_user.sql 
echo "exit ">>$SIM_SCRIPTS/cleanup_user.sql 

 
##  run drop user script  

$ORACLE_HOME/bin/sqlplus $SIM_SOURCE/$SIM_SOURCE_PASS@$ORACLE_SID @$SIM_SCRIPTS/cleanup_user.sql 
 

RETURN_STATUS=$?

#### if not successfull then   create a logfile #

if [ $RETURN_STATUS -ne 0 ]
 then
 
# 
##create an error log file
touch $SIM_LOGS/$DB"_drop_sim_failed"`date +%m%d%Y`
cp $OUTF $SIM_LOGS/$DB"_drop_sim_failed"`date +%m%d%Y`
else
touch $SIM_LOGS/$DB"_drop_sim_succeded_"`date +%m%d%Y`
cp $OUTF $SIM_LOGS/$DB"_drop_sim_succeded_"`date +%m%d%Y`
fi

echo 
echo "`date` ----------------End of Script------------------"
echo 
echo "exit $RETURN_STATUS"
exit $RETURN_STATUS

} >> $OUTF

