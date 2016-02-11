package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 25, 2006
 * Time: 2:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SchemaTopPropsSetup extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(SchemaTopPropsSetup.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql1 = "INSERT INTO SYSTEM_PROPS (ID, PROPERTY,VALUE,DESCRIPTION) VALUES (SKILL_TYPE_REF_SEQ.NEXTVAL, 'classification.schema.top', '6', 'highest level one may attach objects to')";
        try {
            DBAccess.executeUpdate(sql1);
        } catch (SQLException e) {
            isInstalled = true;
        }
        return isInstalled;
    }

    public void install() {
    // nothing to do since the whole work is done in isInstalled
        logger.debug("the feature has been installed");
    }
}
