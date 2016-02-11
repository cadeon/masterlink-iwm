package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 17, 2007
 */
public class AddIsProductionToWorld extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddIsProductionToWorld.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT is_production from world where id=1");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE world  ADD is_production number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
