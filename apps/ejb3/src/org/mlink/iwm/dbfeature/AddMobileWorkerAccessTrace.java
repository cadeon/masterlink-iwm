package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddMobileWorkerAccessTrace extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddProjectStatus.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM MW_ACCESS_TRACE WHERE ROWNUM < 6");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE TABLE MW_ACCESS_TRACE ( " +
                "  USERNAME		VARCHAR2(50)	NOT NULL, " +
                "  ACCESS_TIME		DATE 		NOT NULL," +
                "  SCHEDULE	VARCHAR2(10) 	NOT NULL )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
