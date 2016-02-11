package org.mlink.iwm.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ShiftRefsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
    	nameToColumnMap.put("id","ID");
        nameToColumnMap.put("code","CODE");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("shiftStart","SHIFTSTART");
        nameToColumnMap.put("shiftEnd","SHIFTEND");
        nameToColumnMap.put("time","TIME");
        nameToColumnMap.put("dispOrd","DISP_ORD");
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
        sql.append("SELECT ID, CODE, DESCRIPTION, SHIFTSTART, SHIFTEND, TIME FROM SHIFT_REF WHERE archived_date is null");
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}