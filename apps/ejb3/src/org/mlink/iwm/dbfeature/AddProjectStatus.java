package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Feb 3, 2007
 */
public class AddProjectStatus  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddProjectStatus.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM PROJECT_STATUS_REF");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE TABLE PROJECT_STATUS_REF( " +
                "  ID           NUMBER                           NOT NULL, " +
                "  DESCRIPTION  VARCHAR2(50 BYTE)                DEFAULT '-'    NOT NULL, " +
                "  DISP_ORD     NUMBER(2), " +
                "  CODE         VARCHAR2(20 BYTE)                NOT NULL " +
                ")");
            DBAccess.executeUpdate("ALTER TABLE PROJECT_STATUS_REF ADD (CONSTRAINT PK_PROJECTSTATUSREF_ID PRIMARY KEY (ID))");
            DBAccess.executeUpdate("INSERT INTO PROJECT_STATUS_REF ( ID, DESCRIPTION, CODE, DISP_ORD ) VALUES (1,'Preparing','Preparing',10)");
            DBAccess.executeUpdate("INSERT INTO PROJECT_STATUS_REF ( ID, DESCRIPTION, CODE, DISP_ORD ) VALUES (2,'Started','Started',20) ");
            DBAccess.executeUpdate("INSERT INTO PROJECT_STATUS_REF ( ID, DESCRIPTION, CODE, DISP_ORD ) VALUES (3,'Cancelled','Cancelled',30)");
            DBAccess.executeUpdate("INSERT INTO PROJECT_STATUS_REF ( ID, DESCRIPTION, CODE, DISP_ORD ) VALUES (4,'Completed','Completed',40)");
            DBAccess.executeUpdate("ALTER TABLE PROJECT  ADD STATUS_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE PROJECT  ADD STARTED_DATE DATE");
            DBAccess.executeUpdate("ALTER TABLE PROJECT  ADD DESCRIPTION VARCHAR(200)");
            DBAccess.executeUpdate("alter table PROJECT add constraint FK_PROJECT_STATUSID " +
                "foreign key (STATUS_ID ) references PROJECT_STATUS_REF (ID) " +
                "on delete cascade");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
