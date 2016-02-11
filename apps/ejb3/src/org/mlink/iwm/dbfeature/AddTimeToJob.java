package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Sep 13, 2007
 */
public class AddTimeToJob extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddTimeToJob.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB ADD TOTAL_TIME NUMBER");
            DBAccess.executeUpdate("UPDATE JOB J SET TOTAL_TIME = (SELECT  SUM(JT.TOTAL_TIME) FROM JOB_TASK JT WHERE JT.JOB_ID=J.ID GROUP BY JT.JOB_ID)");
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
