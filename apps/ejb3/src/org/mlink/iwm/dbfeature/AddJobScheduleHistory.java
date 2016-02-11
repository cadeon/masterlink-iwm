
package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 21, 2006
 */
public class AddJobScheduleHistory  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddJobScheduleHistory.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT count(*) from job_schedule_hist");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE JOB_SCHEDULE  drop column WLS_TEMP");
            DBAccess.executeUpdate("ALTER TABLE JOB_SCHEDULE  ADD CREATED_TIME DATE");
            DBAccess.executeUpdate("ALTER TABLE JOB_SCHEDULE  ADD USR VARCHAR(50)");
            DBAccess.executeUpdate("CREATE TABLE JOB_SCHEDULE_HIST" +
                    "(" +
                    "  WORK_SCHEDULE_ID  NUMBER," +
                    "  ID                NUMBER," +
                    "  JOB_ID            NUMBER," +
                    "  CREATED_TIME      DATE," +
                    "  ACTION_DATE          DATE," +
                    "  ACTION            VARCHAR2(1 BYTE)," +
                    "  USR               VARCHAR2(50 BYTE)" +
                    ")");
            DBAccess.executeUpdate("create or replace TRIGGER DLT_JOB_SCHEDULE\n" +
                    "  AFTER DELETE\n" +
                    "     on JOB_SCHEDULE\n" +
                    " REFERENCING NEW AS NEW OLD AS OLD\n" +
                    "  FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "insert into job_schedule_hist (WORK_SCHEDULE_ID,ID,JOB_ID, CREATED_TIME, ACTION_DATE, ACTION,  USR)\n" +
                    "values (:OLD.WORK_SCHEDULE_ID, :OLD.ID, :OLD.JOB_ID, :OLD.CREATED_TIME, SYSDATE, 'D',  :OLD.USR)\n" +
                    ";\n" +
                    " END;\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
