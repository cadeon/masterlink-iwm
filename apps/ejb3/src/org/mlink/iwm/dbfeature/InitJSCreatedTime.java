package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 3, 2007
 */
public class InitJSCreatedTime extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(InitJSCreatedTime.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            List<Map> tmp = DBAccess.execute(" select count(*) CNT from job_schedule where created_time is null ");
            if("0".equals(tmp.get(0).get("CNT").toString())){
                logger.debug("the feature has been installed");
            }else{
                isInstalled = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. Please be patient may take a couple of minutes");
        try {
            DBAccess.executeUpdate("update job_schedule js set js.created_time = (select day from work_schedule ws where js.work_schedule_id=ws.id ) where js.created_time is null ");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

