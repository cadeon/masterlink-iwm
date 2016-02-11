package org.mlink.iwm.dbfeature;

import org.mlink.iwm.util.DBAccess;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Sep 28, 2007
Required by Sitar (Ken's email Sept 28,07 --- On the sailor/details/edit screen there is a checkbox called Active.  They want a pulldown instead for values dave will give you - things like active, temporary duty away, in the brig, limited duty etc.
 */


public class AddWorkerStatus extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddWorkerStatus.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT STATUS_ID FROM PERSON");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE PERSON  ADD STATUS_ID NUMBER(2,0)");

            DBAccess.executeUpdate("CREATE TABLE WORKER_STATUS_REF\n" +
                    "( ID NUMBER NOT NULL ENABLE,\n" +
                    "DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,\n" +
                    "DISP_ORD NUMBER (2,0),\n" +
                    "CODE VARCHAR2(20) NOT NULL ENABLE,\n" +
                    "CONSTRAINT PK_WORKERSTATUSREF_ID PRIMARY KEY (ID) ENABLE )");
            DBAccess.executeUpdate("INSERT INTO WORKER_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD )VALUES (1,'Available','Available',10)");
            DBAccess.executeUpdate("INSERT INTO WORKER_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD )VALUES (2,'Not Available','Not_Available',20)");
            DBAccess.executeUpdate("alter table PERSON add constraint FK_PERSON_STATUSID foreign  key (STATUS_ID ) references WORKER_STATUS_REF (ID) ENABLE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
