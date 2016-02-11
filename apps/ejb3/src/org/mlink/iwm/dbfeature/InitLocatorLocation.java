package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 18, 2007
 */
public class InitLocatorLocation extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(InitLocatorLocation.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            List tmp = DBAccess.execute(" select object_name, status, last_ddl_time from user_objects where object_name='INIT_LOCATOR_LOCATION_PROC'");
            if(tmp.size()>0) {
                logger.debug("the feature has been installed");
            }else{
                isInstalled = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed.");
        try {
            DBAccess.executeUpdate("create or replace procedure INIT_LOCATOR_LOCATION_PROC \n" +
                    "as\n" +
                    "--\n" +
                    "--\n" +
                    "   CURSOR c1 IS\n" +
                    "select id, get_locator_location(ID) location from locator;\n" +
                    "\n" +
                    "BEGIN\n" +
                    "      FOR myrecord IN c1\n" +
                    "      LOOP   \n" +
                    "      update locator\n" +
                    "      set top_parent_id = myrecord.location where id=myrecord.id;\n" +
                    "      END LOOP;\n" +
                    "END INIT_LOCATOR_LOCATION_PROC;");
            logger.debug("Done");

            DBAccess.executeUpdate("alter trigger locator_fixup disable");
            DBAccess.executeUpdate("alter trigger s_locator_fixup disable");
            DBAccess.executeUpdate("alter trigger upd_loc_wksched_archdate disable");

            DBAccess.executeUpdate(" call INIT_LOCATOR_LOCATION_PROC()");

            DBAccess.executeUpdate("alter trigger locator_fixup enable");
            DBAccess.executeUpdate("alter trigger s_locator_fixup enable");
            DBAccess.executeUpdate("alter trigger upd_loc_wksched_archdate enable");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
