package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 27, 2007
 */
public class AddParentToProject extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddParentToProject.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT PARENT_ID FROM PROJECT");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE PROJECT  ADD PARENT_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD PARENT_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE PROJECT  ADD ORGANIZATION_ID NUMBER");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}