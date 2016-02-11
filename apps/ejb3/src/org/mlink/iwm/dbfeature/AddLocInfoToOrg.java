
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddLocInfoToOrg extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddLocInfoToOrg.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM MW_ORG_ACCESS_VIEW0");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ORG_ACCESS_VIEW0 " +
        			" (USER_NAME, SCHEDULE, latitude, longitude, FIRST_PUNCH, LAST_PUNCH, MINS, HITS) AS" +
        			" SELECT user_name, schedule, latitude, longitude, min(access_time) AS first_punch, max(access_time) AS last_punch, " +
        			"   round((MAX(access_time) - MIN(access_time))*24*60, 0) AS MINS, count(*) as HITS" +
        			" FROM MW_ACCESS_TRACE" +
        			" where to_char(access_time, 'MM/DD/YYYY')= schedule and latitude is not null and longitude is not null " +
        			" group by user_name, schedule, latitude, longitude");

        	DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ORG_ACCESS_VIEW1 " +
        			" (USER_NAME, SCHEDULE, latitude, longitude, FIRST_PUNCH, LAST_PUNCH, MINS, HITS) AS" +
        			" select V1.USER_NAME, V1.SCHEDULE, V1.latitude, V1.longitude, V1.FIRST_PUNCH, V1.LAST_PUNCH, V1.MINS, V1.HITS " +
        			" from MW_ACCESS_VIEW2 V1 " +
        			" join (select V2.user_name, V2.schedule,  max(V2.last_punch) as last_punch from MW_ORG_ACCESS_VIEW0 V2 " +
        			" group by V2.user_name, V2.schedule) V3 ON" +
        			"	V1.user_name = V3.user_name and V1.schedule = V3.schedule and V1.last_punch = to_char(V3.last_punch, 'HH:MI AM') " +
        			" order by to_date(V1.schedule, 'MM/DD/YYYY') desc, V1.user_name");

        	DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ORG_ACCESS_VIEW2 " +
        			" (USER_NAME, ORGANIZATION_ID, ORGANIZATION_NAME, SCHEDULE, latitude, longitude, FIRST_PUNCH, LAST_PUNCH, MINS, HITS) AS" +
        			" select v1.USER_NAME, ORG.ID AS ORGANIZATION_ID, PRT.NAME AS ORGANIZATION_NAME, V1.SCHEDULE,  V1.latitude, V1.longitude, V1.FIRST_PUNCH, V1.LAST_PUNCH, V1.MINS, V1.HITS " +
        			" from mw_org_access_view1 V1, Person P, ORGANIZATION ORG, PARTY PRT " +
        			" where P.USER_NAME=V1.USER_NAME AND P.ORGANIZATION_ID = ORG.ID and ORG.PARTY_ID = PRT.ID");

        	DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ORG_ACCESS_VIEW3 " +
        			" (ORGANIZATION_ID, ORGANIZATION_NAME, SCHEDULE, LOC_INFO) AS" +
        			" SELECT ORGANIZATION_ID, ORGANIZATION_NAME, schedule , " +
        			"	RTRIM( xmlagg (xmlelement (c, user_name||','||latitude||','|| longitude||','|| first_punch||','|| last_punch||','|| MINS||','|| HITS || ';') " +
        			"		order by schedule desc, to_date(last_punch, 'HH:MI AM') desc).extract ('//text()'), ',' ) AS loc_info" +
        			" FROM   mw_ORG_ACCESS_view2" +
        			" GROUP BY SCHEDULE, ORGANIZATION_ID, ORGANIZATION_NAME");

        	//select * from MW_ORG_ACCESS_VIEW0  order by to_date(schedule, 'MM/DD/YYYY') desc, user_name
        	//select * from MW_ORG_ACCESS_VIEW1  order by to_date(schedule, 'MM/DD/YYYY') desc, user_name
        	//select * from MW_ORG_ACCESS_VIEW2  order by organization_id, to_date(schedule, 'MM/DD/YYYY') desc
        	//select * from MW_ORG_ACCESS_VIEW3 order by to_date(schedule, 'MM/DD/YYYY') desc, ORGANIZATION_ID     
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
