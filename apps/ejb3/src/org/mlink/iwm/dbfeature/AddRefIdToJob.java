package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andreipovodyrev
 * Date: Nov 30, 2007
 */
public class AddRefIdToJob extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddRefIdToJob.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT REF_ID from JOB where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. This one might take several minutes.Pls be patient!");
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB  ADD REF_ID VARCHAR2(150 BYTE)");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

