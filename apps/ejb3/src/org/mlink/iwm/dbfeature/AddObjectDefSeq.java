package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: raghu
 * Date: Aug 17, 2009
 * Time: 2:31:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddObjectDefSeq extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddObjectDefSeq.class);
    
    
    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "CREATE SEQUENCE OBJECT_DEF_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 0 CACHE 20 NOORDER  NOCYCLE";
        try {
            DBAccess.executeUpdate(sql);
        } catch (SQLException e) {
            isInstalled = true;
        }
        return isInstalled;
    }
    public void install(){
    	//do nothing, isInstalled did it
    }
}
