package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andreipovodyrev
 * Date: Dec 4, 2007
 */
public class AddBSCToPerson extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddBSCToPerson.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT BSCODE from PERSON where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. This one might take several minutes.Pls be patient!");
        try {
            DBAccess.executeUpdate("ALTER TABLE PERSON  ADD BSCODE VARCHAR2(50 BYTE)");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
