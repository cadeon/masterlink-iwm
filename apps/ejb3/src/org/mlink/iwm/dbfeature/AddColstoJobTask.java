package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Apr 23, 2007
 */
public class AddColstoJobTask extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddColstoJobTask.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT DESCRIPTION from JOB_TASK where id=1000");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed. This one might take several minutes. Please be patient!");
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  SKILL_TYPE_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  PRIORITY_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  TASK_TYPE_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  NUMBER_WORKERS NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  ESTIMATED_TIME NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  SKILL_LEVEL_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD  DESCRIPTION VARCHAR2(150 BYTE)");
            DBAccess.executeUpdate("UPDATE JOB_TASK JA\n" +
                    "SET (SKILL_TYPE_ID,PRIORITY_ID,TASK_TYPE_ID,NUMBER_WORKERS,ESTIMATED_TIME,SKILL_LEVEL_ID,DESCRIPTION) = (\n" +
                    "  SELECT A.SKILL_TYPE_ID,A.PRIORITY_ID,A.TASK_TYPE_ID,A.NUMBER_WORKERS,A.ESTIMATED_TIME,A.SKILL_LEVEL_ID,A.DESCRIPTION\n" +
                    "  FROM TASK A\n" +
                    "  WHERE A.ID = JA.TASK_ID)");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
