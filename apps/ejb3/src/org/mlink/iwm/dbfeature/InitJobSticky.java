package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 29, 2007
 */
public class InitJobSticky extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(InitLocatorLocation.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            List <Map> tmp = DBAccess.execute(" select count(*) as CNT from job where sticky is null");
            if("0".equals(tmp.get(0).get("CNT").toString())){
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
            DBAccess.executeUpdate("update job set sticky=\'N\' where sticky is null");
            logger.debug("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

