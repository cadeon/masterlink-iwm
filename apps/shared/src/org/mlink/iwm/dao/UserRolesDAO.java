package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Dec 4, 2006
 */
public class UserRolesDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("roleId","ID");
        nameToColumnMap.put("desc","DESCRIPTION");
        nameToColumnMap.put("isAssigned","ASSIGNED");
    }

    /**
     * Get data for locator
     * @return
     * @throws java.sql.SQLException
     */
    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql);
    }

    protected String getSql(SearchCriteria cr) {
        return "SELECT R.ID,R.DESCRIPTION,DECODE(UR.USER_ID,NULL,0,1) ASSIGNED " +
                "FROM ROLE R,USER_ROLE UR WHERE R.ID=UR.ROLE_ID(+) AND UR.USER_ID(+)=? ORDER BY R.DISP_ORD";
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}
