
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddLocToMobileWorkerAccess extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddLocToMobileWorkerAccess.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT LATITUDE FROM MW_ACCESS_TRACE");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("CREATE SEQUENCE MW_ACCESS_TRACE_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999 " +
    			"INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE");

		    DBAccess.executeUpdate("CREATE TABLE MW_ACCESS_TRACE_BKUP AS " +
		    		" SELECT MW_ACCESS_TRACE_SEQ.NEXTVAL AS ID, USERNAME, ACCESS_TIME, SCHEDULE FROM MW_ACCESS_TRACE");
		    
		    DBAccess.executeUpdate("DROP TABLE MW_ACCESS_TRACE");
		
		    DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE_BKUP RENAME TO MW_ACCESS_TRACE");
    
		    DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE RENAME COLUMN USERNAME TO USER_NAME");
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD LATITUDE NUMBER(11,8)");
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD LONGITUDE NUMBER(11,8)");
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD ACCURACY NUMBER(6,0)");
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD ACCESS_TYPE_ID NUMBER DEFAULT 1");

        	DBAccess.executeUpdate("CREATE TABLE MW_ACCESS_TYPE_REF\n" +
                    "( ID NUMBER NOT NULL ENABLE,\n" +
                    "DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,\n" +
                    "DISP_ORD NUMBER (2,0),\n" +
                    "CODE VARCHAR2(50) NOT NULL ENABLE,\n" +
                    "TYPE VARCHAR2(20) NOT NULL ENABLE,\n" +
                    "CONSTRAINT PK_MW_ACCESS_TYPE_REF_ID PRIMARY KEY (ID) ENABLE )");
        	
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (1,'UI Access','UIA',1,'UI')");
       		DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (10,'MW Login','MWL',2,'MW')");
       		DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (11,'MW Download','MWD',3,'MW')");
       		DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (12,'MW Upload','MWU',4,'MW')");

       		DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD CONSTRAINT FK_ACCESSTYPE_ACCESSTYPEID FOREIGN KEY (ACCESS_TYPE_ID ) references MW_ACCESS_TYPE_REF (ID) ENABLE");
       		
        	
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW " +
            		"(USER_NAME, SCHEDULE, FIRST_PUNCH, LAST_PUNCH, HRS) AS " +
            		" SELECT user_name, schedule," +
            		" to_char(min(access_time), 'HH:MI:SS AM') AS first_punch, " +
            		" to_char(max(access_time), 'HH:MI:SS AM') AS last_punch, " +
            		" round((MAX(access_time) - MIN(access_time))*24, 2) AS hrs " +
            		" FROM MW_ACCESS_TRACE " +
            		" where to_char(access_time, 'MM/dd/yyyy')= schedule " +
            		" group by user_name, schedule");
            
            /*DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW1 (USER_NAME, SCHEDULE, FIRST_PUNCH, LAST_PUNCH, HOURS) AS" +
            		" SELECT user_name, schedule, to_char(min(access_time), 'HH:MI:SS AM') AS first_punch, to_char(max(access_time), 'HH:MI:SS AM') AS last_punch, " +
            		"   round((MAX(access_time) - MIN(access_time))*24, 2) AS HOURS " +
            		" FROM MW_ACCESS_TRACE" +
            		" where to_char(access_time, 'MM/dd/yyyy')= schedule group by user_name, schedule");
			*/
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW2 (USER_NAME, SCHEDULE, latitude, longitude, FIRST_PUNCH, LAST_PUNCH, MINS, HITS) AS" +
            		" SELECT user_name, schedule, latitude, longitude, to_char(min(access_time), 'HH:MI AM') AS first_punch, to_char(max(access_time), 'HH:MI AM') AS last_punch, " +
            		"   round((MAX(access_time) - MIN(access_time))*24*60, 0) AS MINS, count(*) as HITS" +
            		" FROM MW_ACCESS_TRACE" +
            		" where to_char(access_time, 'MM/dd/yyyy')= schedule and latitude is not null and longitude is not null group by user_name, schedule, latitude, longitude");
  
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW3 (USER_NAME, SCHEDULE, LOC_INFO) AS" +
            		" SELECT user_name, schedule," +
            		"   RTRIM( xmlagg (xmlelement (c, latitude||','|| longitude||','|| first_punch||','|| last_punch||','|| MINS||','|| HITS || ';') " +
            		"		order by latitude||','|| longitude||','|| first_punch||','|| last_punch||','|| MINS||','|| HITS).extract ('//text()'), ',' ) AS loc_info" +
            		" FROM   mw_access_view2" +
            		" GROUP BY schedule, user_name");
            
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW3 (USER_NAME, SCHEDULE, LOC_INFO) AS" +
            		" SELECT user_name, schedule," +
            		"	RTRIM( xmlagg (xmlelement (c, latitude||','|| longitude||','|| first_punch||','|| last_punch||','|| MINS||','|| HITS || ';') " +
            		"		order by schedule desc, to_date(last_punch, 'HH:MI AM') desc).extract ('//text()'), ',' ) AS loc_info" +
            		" FROM   mw_access_view2" +
            		" GROUP BY schedule, user_name");  
            
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW4 (USER_NAME, PERSON_ID, ORGANIZATION_ID, ORGANIZATION_NAME, SCHEDULE, FIRST_PUNCH, LAST_PUNCH, HOURS, LOC_INFO) AS" +
            		" select v1.USER_NAME, P.ID AS PERSON_ID, ORG.ID AS ORGANIZATION_ID, PRT.NAME AS ORGANIZATION_NAME, V1.SCHEDULE, V1.FIRST_PUNCH, V1.LAST_PUNCH, V1.HRS as HOURS, V3.LOC_INFO" +
            		" from mw_access_view V1 LEFT JOIN mw_access_view3 V3 ON V1.USER_NAME = V3.USER_NAME AND V1.SCHEDULE=V3.SCHEDULE, Person P, ORGANIZATION ORG, PARTY PRT " +
            		" where P.USER_NAME=V1.USER_NAME AND P.ORGANIZATION_ID = ORG.ID and ORG.PARTY_ID = PRT.ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
