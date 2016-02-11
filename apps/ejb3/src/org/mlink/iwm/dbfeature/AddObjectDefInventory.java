package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: raghu
 * Date: Aug 17, 2009
 * Time: 2:31:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddObjectDefInventory extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddObjectDefInventory.class);
    
    public boolean isInstalled(){
        boolean isInstalled = true;
        String sql = "select inventory from object_def";
        try {
            DBAccess.executeUpdate(sql);
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }
    public void install(){
    	logger.debug("the feature is being installed");
    try {
    	DBAccess.executeUpdate("ALTER TABLE OBJECT_DEF ADD INVENTORY INT DEFAULT 0");
    	
    	DBAccess.executeUpdate("CREATE TABLE INVENTORY_TRACE(ID  NUMBER(38) NOT NULL, OBJECT_DEF_ID NUMBER, OBJECT_ID NUMBER," +
    			" USER_NAME VARCHAR2(30) NOT NULL, PERSON_ID  NUMBER, INVENTORY_DATE DATE, INVENTORY_AMT INT, JOB_TASK_TIME_ID NUMBER, " +
    			" CONSTRAINT PK_INVENTORYTRACE_ID PRIMARY KEY (ID) ENABLE )");

    	DBAccess.executeUpdate("CREATE SEQUENCE INVENTORY_TRACE_SEQ START WITH 1000");

        DBAccess.executeUpdate("alter table INVENTORY_TRACE add constraint FK_INVENTORYTRACE_OBJECTDEFID foreign key (OBJECT_DEF_ID) references OBJECT_DEF (ID) ENABLE");
        DBAccess.executeUpdate("alter table INVENTORY_TRACE add constraint FK_INVENTORYTRACE_OBJECTID foreign key (OBJECT_ID) references OBJECT (ID) ENABLE");
        DBAccess.executeUpdate("alter table INVENTORY_TRACE add constraint FK_INVENTORYTRACE_JOBTASKTIMEID foreign key (JOB_TASK_TIME_ID) references JOB_TASK_TIME (ID) ENABLE");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
}
