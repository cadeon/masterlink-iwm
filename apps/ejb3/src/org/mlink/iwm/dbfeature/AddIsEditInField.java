package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Dec 17, 2006
 */
public class AddIsEditInField  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddIsEditInField.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql1 = "ALTER TABLE OBJECT_DATA ADD IS_EDIT_IN_FIELD NUMBER(1)";
        String sql2 = "ALTER TABLE OBJECT_DATA_DEF ADD IS_EDIT_IN_FIELD NUMBER(1)";
        try {
            DBAccess.executeUpdate(sql1);
            DBAccess.executeUpdate(sql2);
            logger.debug("the feature is being installed");
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
