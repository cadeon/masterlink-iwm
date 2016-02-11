package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: andrei
 * Date: Dec 9, 2006
 * Time: 8:22:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateOrgHierachyFunction extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(CreateOrgHierachyFunction.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "CREATE OR REPLACE FUNCTION Get_Organization_Hierarchy \n" +
                "(orgId IN NUMBER)    \n" +
                "RETURN VARCHAR2\n" +
                "IS    \n" +
                "\t  hierarchy VARCHAR2(200);    \n" +
                "\t  separator VARCHAR(1);    \n" +
                "\t  CURSOR c1 IS SELECT P.NAME FROM ORGANIZATION O,PARTY P WHERE P.ID=O.PARTY_ID START WITH O.ID=orgId CONNECT BY PRIOR O.PARENT_ID=O.ID;\n" +
                "BEGIN \n" +
                "\t  FOR myrecord IN c1 \n" +
                "\t  LOOP     hierarchy := myrecord.NAME || separator || hierarchy; \n" +
                "\t  separator:='|';    \n" +
                "\t  END LOOP;     \n" +
                "\t  RETURN hierarchy;\n" +
                "END;";
        try {
            DBAccess.executeUpdate(sql);
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
