package org.mlink.iwm.dao;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * This class is not used. 
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ProjectJobsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("projectType","TYPE");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("createdBy","CREATEDBY");
        nameToColumnMap.put("jobCount","JOB_COUNT");
    }

    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        String sql = getSql(criteria);
        List parameters = new ArrayList();
        return process(parameters,sql,request);
    }

    /**
     * Not yet used
     * @param criteria
     */
    protected String getSql(SearchCriteria criteria) {
        String fields = "J.ID, D.ID, D.OBJECT_REF, J.DESCRIPTION, J.LOCATOR_ID, J.SKILL_TYPE_ID, J.JOB_TYPE_ID, J.STATUS_ID, J.PROJECT_ID, J.SEQUENCE_LEVEL, J.CREATED_DATE ";
        String table = " OBJECT D, JOB J, JOB_TASK T  ";
        String sql = "SELECT " + fields + ",COUNT(DISTINCT T.ID) from "  + table + " WHERE ";

        return  sql + " J.PROJECT_ID = ? AND J.ARCHIVED_DATE is NULL AND J.OBJECT_ID=D.ID AND T.JOB_ID(+)=J.ID GROUP BY " + fields;

    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

