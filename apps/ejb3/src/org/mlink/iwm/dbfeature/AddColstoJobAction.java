package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Apr 20, 2007
 */
public class AddColstoJobAction extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddColstoJobAction.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT MODIFIER from JOB_ACTION where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. This one might take several minutes.Pls be patient!");
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB_ACTION  ADD MODIFIER VARCHAR2(150 BYTE)");
            DBAccess.executeUpdate("ALTER TABLE JOB_ACTION  ADD VERB VARCHAR2(150 BYTE)");
            DBAccess.executeUpdate("ALTER TABLE JOB_ACTION  ADD NAME VARCHAR2(150 BYTE)");
            DBAccess.executeUpdate("ALTER TABLE JOB_ACTION  ADD SEQUENCE NUMBER");
            DBAccess.executeUpdate("UPDATE JOB_ACTION JA\n" +
                    "SET (NAME,VERB,MODIFIER,SEQUENCE) = (\n" +
                    "  SELECT A.NAME,A.VERB,A.MODIFIER,A.SEQUENCE\n" +
                    "  FROM ACTION A\n" +
                    "  WHERE A.ID = JA.ACTION_ID)");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
