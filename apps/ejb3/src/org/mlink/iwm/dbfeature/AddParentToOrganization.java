package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Nov 2, 2006
 */
public class AddParentToOrganization extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddParentToOrganization.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        try {
            DBAccess.executeUpdate("ALTER TABLE ORGANIZATION ADD PARENT_ID NUMBER");
            DBAccess.executeUpdate("ALTER TABLE ORGANIZATION ADD HIERARCHY VARCHAR(200)");
            DBAccess.executeUpdate("ALTER TABLE ORGANIZATION ADD SCHEMA_ID NUMBER");
            DBAccess.executeUpdate("INSERT INTO SCHEMA_REF ( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT, DISP_ORD ) VALUES (20,'Department','O', 'Department',NULL, 20)");
            DBAccess.executeUpdate("INSERT INTO SCHEMA_REF ( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT, DISP_ORD ) VALUES (21,'Division','O', 'Division',20, 22)");
            DBAccess.executeUpdate("INSERT INTO SCHEMA_REF ( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT, DISP_ORD ) VALUES (22,'Group','O', 'Group',21, 24)");
            DBAccess.executeUpdate("INSERT INTO SCHEMA_REF ( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT, DISP_ORD ) VALUES (23,'Team','O', 'Team',22, 26)");
            DBAccess.executeUpdate("UPDATE ORGANIZATION SET SCHEMA_ID=20");
            //DBAccess.executeUpdate("UPDATE ORGANIZATION O SET HIERARCHY= (SELECT P.NAME FROM PARTY P WHERE O.PARTY_ID=P.ID) ");
            logger.debug("the feature is installed installed");
        } catch (SQLException e) {
            //e.printStackTrace();
            isInstalled = true;
            logger.debug("the feature has been previously installed");
        }
        return isInstalled;
    }

    public void install() {
        // nothing to do since the whole work is done in isInstalled
    }
}
