
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddUser_NameToPerson extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddUser_NameToPerson.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT user_name FROM person");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE person RENAME COLUMN username TO user_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
