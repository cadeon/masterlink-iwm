package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Nov 9, 2006
 */
public class AddTimeToJobTask extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddTimeToJobTask.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK  ADD TOTAL_TIME NUMBER");
            DBAccess.executeUpdate("UPDATE JOB_TASK JT SET TOTAL_TIME = (SELECT  SUM(JTT.TIME) FROM JOB_TASK_TIME JTT WHERE JTT.JOB_TASK_ID=JT.ID GROUP BY JTT.JOB_TASK_ID)");
            logger.debug("the feature is being installed");
        } catch (SQLException e) {
            //e.printStackTrace();
            isInstalled = true;
            logger.debug("the feature has been previously installed");
        }
        return isInstalled;
    }

    public void install() {
        // nothing to do since the whole work is done in isInstalled
    }
}
