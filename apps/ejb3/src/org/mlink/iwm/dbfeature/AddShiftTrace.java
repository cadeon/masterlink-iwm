package org.mlink.iwm.dbfeature;

import org.mlink.iwm.util.DBAccess;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Sep 28, 2007
Required by IWM Shift report
*/

public class AddShiftTrace extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddShiftTrace.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM SHIFT_TRACE");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("CREATE TABLE SHIFT_TRACE(ID  NUMBER(38) NOT NULL, SCHEDULE	VARCHAR2(10) NOT NULL, SHIFT_ID NUMBER, " +
        			" USER_NAME VARCHAR2(30) NOT NULL, PERSON_ID  NUMBER, SHIFT_START_DATE DATE, SHIFT_STOP_DATE DATE, TIME_ON_BREAKS INT, TIME_ON_JOBTASKS INT," +
        			" CONSTRAINT PK_SHIFTTRACE_ID PRIMARY KEY (ID) ENABLE )");

        	DBAccess.executeUpdate("CREATE SEQUENCE SHIFT_TRACE_SEQ START WITH 1000");

            DBAccess.executeUpdate("alter table SHIFT_TRACE add constraint FK_SHIFTTRACE_SHIFTID foreign key (SHIFT_ID) references SHIFT_REF (ID) ENABLE");
            DBAccess.executeUpdate("alter table SHIFT_TRACE add constraint FK_PERSON_STATUSID foreign key (PERSON_ID) references PERSON (ID) ENABLE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
