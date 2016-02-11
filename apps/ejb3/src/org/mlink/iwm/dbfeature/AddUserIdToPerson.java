
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddUserIdToPerson extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddUserIdToPerson.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT USER_ID FROM person");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("alter table PERSON add USER_ID number REFERENCES APPUSER(ID)");
            DBAccess.executeUpdate("update person p set user_id = (select id from appuser u where u.user_name = p.user_name)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
