#!/usr/bin/ksh
###############################################################################
##                                                                           ##
##  This file is called by create_sim_schema.sh and cleanup_sim_schema.sh    ##
##  to supply some of the environment variables required to run these jobs   ##
##  variables can be  added  later to provide scalability and portability    ##                                                                      ##
##  If the "ROLE" variable is modified ensure that correct spacing exists.   ##
##                                                                           ##
##  To protect the file, this file will be set to 0700 for permissions.      ##
##                                                                           ##
##  master copy of  this script for Linux is located on siren                ##
##  in /opt/oracle/dbtools/simload/common  directory                         ##
##  the final location may change later                                      ##
##  you DO NOT  need to copy these scripts  to linux                         ##
##  these files are  provided for your reference                             ##
##                                                                           ##
############################################################################### 
case ${DB} in
     mlinkdb  )
                export ORACLE_USER=oracle
                export USER=sys
                export QOUTE="'"
                export ROLE=" as sysdba"
                export MLINK_DB=mlinkdb
                export ORACLE_SID=$MLINKDB
                export LOCAL_HOST=siren
		export ORACLE_HOME=/opt/oracle/product/10gR1
		export INIT_LOCATION=/opt/oracle/product/10gR1/dbs/initmlinkdb.ora
                export TNS_ADMIN=/opt/oracle/product/10gR1/network/admin
	;;      
        *       )
                ## Do nothing.
        ;;
esac
###############################################################################

