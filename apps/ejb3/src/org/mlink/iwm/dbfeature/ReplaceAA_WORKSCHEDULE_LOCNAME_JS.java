
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class ReplaceAA_WORKSCHEDULE_LOCNAME_JS extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddProjectStatus.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        	//Do, or do not. There is no try.
        	//(there's nothing to select on, so we're just going to replace this view every time
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW AA_WORKSCHEDULE_LOCNAME_JS (WS_ID, PERSON_ID, DAY, TIME, WS_LOC_ID, LOC_ID, LOC_NAME, JS_ID, JS_JOB_ID, JS_WS_ID) AS " +
            		"( select ws.id as ws_id, ws.person_id as person_id, ws.day, ws.time, ws.locator_id as ws_loc_id, l.id as loc_id, l.name as loc_name, js.id as js_id, js.job_id as js_job_id, js.work_schedule_id as js_ws_id " +
            		"from work_schedule ws, locator l, job_schedule js " +
            		"where  ws.locator_id=l.id " +
            		"and js.deleted_time is null " +
            		"and ws.id = js.work_schedule_id " +
            		")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
