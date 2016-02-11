package org.mlink.iwm.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class SystemPropsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
    	nameToColumnMap.put("id","ID");
        nameToColumnMap.put("property","PROPERTY");
        nameToColumnMap.put("value","VALUE");
        nameToColumnMap.put("description","DESCRIPTION");
    }
    
    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        String sql = getSql(null);
        List parameters = new ArrayList();
        return process(parameters,sql,request);
    }

    protected String getSql(SearchCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID, PROPERTY, VALUE, DESCRIPTION FROM SYSTEM_PROPS");
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}