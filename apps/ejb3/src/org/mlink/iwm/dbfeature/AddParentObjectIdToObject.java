
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddParentObjectIdToObject extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddParentObjectIdToObject.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT PARENT_OBJECT_ID FROM OBJECT");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
        	DBAccess.executeUpdate("Alter table OBJECT add PARENT_OBJECT_ID NUMBER");
        	DBAccess.executeUpdate("Alter table OBJECT ADD CONSTRAINT FK_OBJ_PARENTOBJ FOREIGN KEY (PARENT_OBJECT_ID) REFERENCES OBJECT (ID) ENABLE ");
       
        	DBAccess.executeUpdate("Create table OBJECT_STRUCT_HIST(ID  NUMBER(38) NOT NULL, OBJECT_ID NUMBER," +
    			" CUR_PARENT_OBJECT_ID NUMBER, PREV_PARENT_OBJECT_ID NUMBER, CHANGE_DATE DATE, " +
    			" CUR_LOCATOR_ID NUMBER, PREV_LOCATOR_ID NUMBER, " +
    			" CONSTRAINT PK_OBJECTSTRUCTHIST_ID PRIMARY KEY (ID) ENABLE )");
        	
        	DBAccess.executeUpdate("CREATE SEQUENCE OBJECT_STRUCT_HIST_SEQ MINVALUE 0 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 0 CACHE 20 NOORDER  NOCYCLE");
            
            DBAccess.executeUpdate("alter table OBJECT_STRUCT_HIST add constraint FK_CURPARENTOBJECTID_OBJECTID foreign key (CUR_PARENT_OBJECT_ID) references OBJECT (ID) ENABLE");
            DBAccess.executeUpdate("alter table OBJECT_STRUCT_HIST add constraint FK_PREVPARENTOBJECTID_OBJECTID foreign key (PREV_PARENT_OBJECT_ID) references OBJECT (ID) ENABLE");
            DBAccess.executeUpdate("alter table OBJECT_STRUCT_HIST add constraint FK_CURLOCATORID_LOCATORID foreign key (CUR_LOCATOR_ID) references LOCATOR (ID) ENABLE");
            DBAccess.executeUpdate("alter table OBJECT_STRUCT_HIST add constraint FK_PREVLOCATORID_LOCATORID foreign key (PREV_LOCATOR_ID) references LOCATOR (ID) ENABLE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
