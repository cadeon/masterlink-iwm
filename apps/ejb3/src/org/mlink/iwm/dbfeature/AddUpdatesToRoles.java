package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Feb 3, 2007
 */
public class AddUpdatesToRoles  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddUpdatesToRoles.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            if(DBAccess.execute("SELECT 1 FROM role where DESCRIPTION='Project Stencils' ").size()>0){
                logger.debug("the feature has been installed");
            } else {
                isInstalled = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("UPDATE ROLE SET DESCRIPTION='Project Stencils' WHERE DESCRIPTION='Task Sequences'");
            DBAccess.executeUpdate("UPDATE ROLE SET DESCRIPTION='Organizations and Workers' WHERE DESCRIPTION='Organization'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

