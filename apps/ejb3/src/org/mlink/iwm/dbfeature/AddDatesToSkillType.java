
package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 21, 2006
 */
public class AddDatesToSkillType  extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddDatesToSkillType.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT created_date from skill_type_ref");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("ALTER TABLE SKILL_TYPE_REF ADD CREATED_DATE DATE");
            DBAccess.executeUpdate("ALTER TABLE SKILL_TYPE_REF ADD ARCHIVED_DATE DATE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
