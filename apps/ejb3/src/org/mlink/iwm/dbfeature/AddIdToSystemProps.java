package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * CREATEd by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class AddIdToSystemProps extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddCQLS.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT ID FROM SYSTEM_PROPS");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE SEQUENCE SYSTEM_PROPS_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999 " +
            		"INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE");
 
            DBAccess.executeUpdate("CREATE TABLE SYSTEM_PROPS_BKUP as " +
            		" select SYSTEM_PROPS_SEQ.NEXTVAL as ID, PROPERTY, VALUE, DESCRIPTION from SYSTEM_PROPS");
            
            DBAccess.executeUpdate("DROP TABLE SYSTEM_PROPS");

            DBAccess.executeUpdate("ALTER TABLE SYSTEM_PROPS_BKUP rename to SYSTEM_PROPS");
            
            DBAccess.executeUpdate("ALTER TABLE SYSTEM_PROPS ADD CONSTRAINT SYSTEM_PROPS_PK PRIMARY KEY (ID)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
