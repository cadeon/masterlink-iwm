package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: chris
 * To change this template use File | Settings | File Templates.
 */
public class AddUCSeqs extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddUCSeqs.class);
    
    
    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "   CREATE SEQUENCE UC_CODE_SEQ  MINVALUE 1 MAXVALUE 99999999999 INCREMENT BY 1 START WITH 1 CACHE 2";
        String sql2 = "CREATE SEQUENCE OBJECT_CLASSIFICATION_SEQ  MINVALUE 1 MAXVALUE 9999999999999999999999 INCREMENT BY 1 START WITH 2000 NOCYCLE";
        try {
            DBAccess.executeUpdate(sql);
            DBAccess.executeUpdate(sql2);
        } catch (SQLException e) {
            isInstalled = true;
        }
       
        return isInstalled;
    }
    public void install(){
    	 //nothing to do
         }
}
