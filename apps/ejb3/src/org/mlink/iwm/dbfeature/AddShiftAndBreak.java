
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddShiftAndBreak extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddShiftAndBreak.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT JOB_TASK_ID FROM MW_ACCESS_TRACE");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD JOB_TASK_ID NUMBER");
        	DBAccess.executeUpdate("ALTER TABLE MW_ACCESS_TRACE ADD CONSTRAINT FK_MWACCESSTRACE_JOBTASKID FOREIGN KEY (JOB_TASK_ID ) references JOB_TASK (ID) ENABLE");
       		
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (13,'Start Shift','MWSS',5,'MW')");
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (14,'End Shift','MWES',6,'MW')");
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (15,'Start Break','MWSB',7,'MW')");
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (16,'End Break','MWEB',8,'MW')");
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (17,'Start Task','MWST',9,'MW')");
        	DBAccess.executeUpdate("INSERT INTO MW_ACCESS_TYPE_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (18,'End Task','MWET',10,'MW')");
        
        	DBAccess.executeUpdate("ALTER TABLE WORK_SCHEDULE_STATUS_REF ADD CONSTRAINT PK_WORKSTATREF_ID PRIMARY KEY (ID)");
        	 
        	DBAccess.executeUpdate("INSERT INTO WORK_SCHEDULE_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD)VALUES (1058,'Break','BRK',6)");	
        	
        	DBAccess.executeUpdate("INSERT INTO JOB_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD)VALUES (1059,'Started Job Order','SJO',12)");	
        	DBAccess.executeUpdate("INSERT INTO JOB_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD)VALUES (1060,'Job In Progress','JIP',13)");	
        
        	DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD TASK_IN_PROGRESS NUMBER DEFAULT 0");
        	
        	DBAccess.executeUpdate("ALTER TABLE JOB_TASK ADD TASK_IN_PROGRESS NUMBER DEFAULT 0");
        	
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
