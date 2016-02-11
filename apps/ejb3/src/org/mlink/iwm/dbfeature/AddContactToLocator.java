package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 21, 2006
 */
public class AddContactToLocator  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddContactToLocator.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "ALTER TABLE LOCATOR ADD EMERGENCY_CONTACT VARCHAR2(150)";
        try {
            DBAccess.executeUpdate(sql);
        } catch (SQLException e) {
            isInstalled = true;
        }
        return isInstalled;
    }

    public void install() {
    // nothing to do since the whole work is done in isInstalled
        logger.debug("the feature has been installed");
    }
}
