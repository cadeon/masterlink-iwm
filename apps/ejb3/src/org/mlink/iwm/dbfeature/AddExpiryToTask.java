
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddExpiryToTask extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddExpiryToTask.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT EXPIRY_TYPE_ID FROM TASK_DEF");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("CREATE TABLE TASK_DEF_EXPIRY_TYPE_REF\n" +
                    "( ID NUMBER NOT NULL ENABLE,\n" +
                    "DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,\n" +
                    "DISP_ORD NUMBER (2,0),\n" +
                    "CODE VARCHAR2(50) NOT NULL ENABLE,\n" +
                    "CONSTRAINT PK_TASK_DEF_EXPIRY_TYPE_REF_ID PRIMARY KEY (ID) ENABLE )");

        	DBAccess.executeUpdate("INSERT INTO TASK_DEF_EXPIRY_TYPE_REF(ID, DESCRIPTION, DISP_ORD, CODE) VALUES(1, 'End of Month', 1, 'EOM')");
        	DBAccess.executeUpdate("INSERT INTO TASK_DEF_EXPIRY_TYPE_REF(ID, DESCRIPTION, DISP_ORD, CODE) VALUES(2, 'End of Year', 2, 'EOY')");
        	DBAccess.executeUpdate("INSERT INTO TASK_DEF_EXPIRY_TYPE_REF(ID, DESCRIPTION, DISP_ORD, CODE) VALUES(3, 'Relative-Number of Days', 3, 'REL')");
        	DBAccess.executeUpdate("INSERT INTO TASK_DEF_EXPIRY_TYPE_REF(ID, DESCRIPTION, DISP_ORD, CODE) VALUES(4, 'Never Expire', 4, 'NEX')");

        	DBAccess.executeUpdate("ALTER TABLE TASK_DEF ADD EXPIRY_TYPE_ID NUMBER DEFAULT '3' NOT NULL ENABLE");
        	DBAccess.executeUpdate("ALTER TABLE TASK_DEF ADD CONSTRAINT FK_TASKDEF_EXPIRYTYPEID FOREIGN KEY (EXPIRY_TYPE_ID ) " +
        			" references TASK_DEF_EXPIRY_TYPE_REF (ID) ENABLE");

			DBAccess.executeUpdate("ALTER TABLE TASK_DEF ADD EXPIRY_NUMOFDAYS NUMBER DEFAULT '180' NOT NULL ENABLE ");

			DBAccess.executeUpdate("ALTER TABLE TASK ADD EXPIRY_TYPE_ID NUMBER DEFAULT '3' NOT NULL ENABLE ");
			DBAccess.executeUpdate("ALTER TABLE TASK ADD CONSTRAINT FK_TASK_EXPIRYTYPEID FOREIGN KEY (EXPIRY_TYPE_ID ) " +
					"references TASK_DEF_EXPIRY_TYPE_REF (ID) ENABLE");

        	DBAccess.executeUpdate("ALTER TABLE TASK ADD EXPIRY_NUMOFDAYS NUMBER DEFAULT '180' NOT NULL ENABLE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
