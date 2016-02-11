
package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 21, 2006
 */
public class AddJobStatusHistory  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddJobScheduleHistory.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT count(*) from JOB_STATUS_HIST");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE TABLE JOB_STATUS_HIST" +
                    "(" +
                    "  ID                NUMBER," +
                    "  JOB_ID            NUMBER," +
                    "  NEW_STATUS_ID      NUMBER," +
                    "  DATE_CHANGED          DATE," +
                    "  USER_ID            NUMBER," +
                    "  ASSIGNED_WKR_ID    NUMBER" +
                    ")");
            DBAccess.executeUpdate("CREATE SEQUENCE JSH_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
