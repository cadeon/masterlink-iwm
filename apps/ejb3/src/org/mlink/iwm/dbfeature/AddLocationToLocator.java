package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: May 24, 2007
 * Time: 6:50:55 PM
 */
public class AddLocationToLocator extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddLocationToLocator.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT TOP_PARENT_ID from LOCATOR where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed.");
        try {
            DBAccess.executeUpdate("ALTER TABLE LOCATOR  ADD TOP_PARENT_ID NUMBER");
            DBAccess.executeUpdate("create or replace FUNCTION GET_LOCATOR_LOCATION\n" +
                    "( locatorId IN NUMBER) RETURN NUMBER \n" +
                    "IS\n" +
                    "  top_parent_id NUMBER;\n" +
                    "  CURSOR c1 IS select id  from locator start with id = locatorId connect by prior parent_id = ID;\n" +
                    "\n" +
                    "BEGIN\n" +
                    "\t  FOR myrecord IN c1 \n" +
                    "\t  LOOP     top_parent_id := myrecord.ID; \n" +
                    "\t  END LOOP;\n" +
                    "          RETURN top_parent_id;\n" +
                    "END GET_LOCATOR_LOCATION;");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}