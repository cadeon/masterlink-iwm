package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.util.List;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 20, 2007
 */
public class CreateClassHierarchyFunc extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(CreateClassHierarchyFunc.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            List tmp = DBAccess.execute(" select object_name, status, last_ddl_time from user_objects where object_name='GET_CLASS_HIERARCHY'");
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
            DBAccess.executeUpdate("create or replace\n" +
                    "FUNCTION GET_CLASS_HIERARCHY \n" +
                    "(classId IN NUMBER)    \n" +
                    "RETURN VARCHAR2\n" +
                    "IS    \n" +
                    "\t  hierarchy VARCHAR2(200);\n" +
                    "          separator VARCHAR(1);\n" +
                    "\t  CURSOR c1 IS SELECT OC.CODE FROM OBJECT_CLASSIFICATION OC START WITH OC.ID=classId CONNECT BY PRIOR OC.PARENT_ID=OC.ID;\n" +
                    "BEGIN \n" +
                    "          separator:='';\n" +
                    "\t  FOR myrecord IN c1 \n" +
                    "\t  LOOP     hierarchy := myrecord.CODE || separator || hierarchy;\n" +
                    "          separator:='.';\n" +
                    "\t  END LOOP;     \n" +
                    "\t  RETURN hierarchy;\n" +
                    "END GET_CLASS_HIERARCHY;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
