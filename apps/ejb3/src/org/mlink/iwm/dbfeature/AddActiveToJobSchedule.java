package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Apr 19, 2007
 */
public class AddActiveToJobSchedule extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddActiveToJobSchedule.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT DELETED_TIME from job_schedule where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB_SCHEDULE  ADD DELETED_TIME DATE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}