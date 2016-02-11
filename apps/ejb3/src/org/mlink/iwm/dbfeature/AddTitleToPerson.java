package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: May 11, 2007
 * Time: 2:20:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddTitleToPerson extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddTitleToPerson.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT TITLE FROM PERSON");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE PERSON  ADD TITLE VARCHAR(150)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}