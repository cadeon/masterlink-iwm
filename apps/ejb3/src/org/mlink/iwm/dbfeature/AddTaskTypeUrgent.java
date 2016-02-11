package org.mlink.iwm.dbfeature;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 21, 2006
 * Since ExternalWorkRequest module, it is required to have Urgent task type. Requested by Douglas on 6/21/06
 */
public class AddTaskTypeUrgent extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddTaskTypeUrgent.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "SELECT 1 FROM TASK_TYPE_REF WHERE CODE='"+ TaskTypeRef.URGENT + "'";
        try {
            if((DBAccess.execute(sql)).size()>0) 
                isInstalled=true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
        return isInstalled;
    }

    public void install() {
        String sql = null;
        try{
            logger.debug("1. Calculate id for the type");
            sql = "SELECT MAX(ID) MAX_ID FROM TASK_TYPE_REF";
            List result = DBAccess.execute(sql);
            Map map = (Map)result.get(0);
            long id = ((BigDecimal)map.get("MAX_ID")).longValue() + 1;

            logger.debug("2. add " + TaskTypeRef.URGENT + " to TASK_TYPE_REF");
            sql="INSERT INTO TASK_TYPE_REF ( ID,DESCRIPTION,DISP_ORD,CODE) VALUES ("
                    +id+",'"+TaskTypeRef.URGENT + "',20,'"+TaskTypeRef.URGENT+"')";
            logger.debug(sql);
            DBAccess.executeUpdate(sql);
        } catch (Exception e) {
            logger.debug("error executing " + sql);
            e.printStackTrace();
        }
    }
}
