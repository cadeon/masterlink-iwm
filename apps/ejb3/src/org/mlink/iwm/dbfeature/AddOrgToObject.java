package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 27, 2007
 */
public class AddOrgToObject extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddOrgToObject.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT ORGANIZATION_ID from OBJECT where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. This one might take several minutes.Pls be patient!");
        try {
            DBAccess.executeUpdate("ALTER TABLE OBJECT  ADD ORGANIZATION_ID NUMBER");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
