package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Feb 3, 2007
 */
public class AddProjectStencils  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddProjectStencils.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT LOCATOR_ID FROM SEQUENCE");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {                                             
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  drop column SEQUENCE_TYPE_ID");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD ARCHIVED_DATE DATE");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD LOCATOR_ID NUMBER");      //todo FK
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD FREQUENCY_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD FREQUENCY_VALUE NUMBER");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD ORGANIZATION_ID NUMBER");    //todo FK
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD ACTIVE NUMBER(1)");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD AUTOPLANNING NUMBER(1)");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD NAME VARCHAR(50)");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  MODIFY DESCRIPTION VARCHAR(250)");
            DBAccess.executeUpdate("UPDATE SEQUENCE  SET ACTIVE=1");               //BACKWARD COMPATIBILITY
            DBAccess.executeUpdate("UPDATE SEQUENCE  SET AUTOPLANNING=1");         //BACKWARD COMPATIBILITY
            DBAccess.executeUpdate("DROP TABLE  SEQUENCE_TYPE");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD START_DATE DATE");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD LAST_PLANNED_DATE DATE");
            DBAccess.executeUpdate("ALTER TABLE SEQUENCE  ADD PROJECT_TYPE_ID NUMBER");       //TODO FK
            DBAccess.executeUpdate("ALTER TABLE PROJECT   ADD SEQUENCE_ID NUMBER");           //todo FK
            DBAccess.executeUpdate("alter table PROJECT add constraint FK_PROJECT_SEQUENCEID foreign key (SEQUENCE_ID ) references SEQUENCE (ID) on delete cascade");
            //DBAccess.executeUpdate("alter table SEQUENCE add constraint FK_SEQUENCE_FREQUENCYID " +  todo FK
            //    "foreign key (FREQUENCY_ID ) references TASK_FREQUENCY_REF (ID)");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

