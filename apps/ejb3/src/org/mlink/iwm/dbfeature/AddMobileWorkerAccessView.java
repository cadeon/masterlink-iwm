
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddMobileWorkerAccessView extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddMobileWorkerAccessView.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM MW_ACCESS_VIEW");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW MW_ACCESS_VIEW " +
            		"(USERNAME, SCHEDULE, FIRST_PUNCH, LAST_PUNCH, HRS) AS " +
            		" select username, schedule," +
            		" to_char(min(access_time), 'HH:MI:SS AM') as first_punch, " +
            		" to_char(max(access_time), 'HH:MI:SS AM') as last_punch, " +
            		" round((MAX(access_time) - MIN(access_time))*24, 2) as hrs " +
            		" FROM MW_ACCESS_TRACE " +
            		" where to_char(access_time, 'MM/DD/YYYY')= schedule " +
            		" group by username, schedule");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
