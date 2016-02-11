package org.mlink.iwm.dao;

import org.apache.log4j.Logger;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Apr 17, 2007
 */
public class GlobalSearchDAO extends ListDAOTemplate {
    private static final Logger logger = Logger.getLogger(GlobalSearchDAO.class);
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("status","JOB_STATUS");
        nameToColumnMap.put("objectRef","OBJECT_REF");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        nameToColumnMap.put("jobType","JOB_TYPE");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("estTime","ESTIMATEDTIME");
        nameToColumnMap.put("sequenceLevel","SEQUENCE_LEVEL");
    }


    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        return process(parameters,sql,request);
    }

    public String getSql(SearchCriteria cr) {

        String fields = "J.ID, D.OBJECT_REF, J.DESCRIPTION, L.FULL_LOCATOR, STR.DESCRIPTION SKILL_TYPE, " +
                "TTR.DESCRIPTION JOB_TYPE,JS.CODE JOB_STATUS, J.CREATED_DATE, J.ESTIMATEDTIME, J.SEQUENCE_LEVEL  ";
        String tableName = " FROM OBJECT D, JOB J, JOB_STATUS_REF JS, TASK_TYPE_REF TTR, SKILL_TYPE_REF STR ";
        StringBuilder where = new StringBuilder("WHERE TTR.ID=J.JOB_TYPE_ID AND STR.ID=J.SKILL_TYPE_ID AND J.LOCATOR_ID=L.ID " +
                "AND J.STATUS_ID=JS.ID  AND J.ARCHIVED_DATE IS NULL  AND J.OBJECT_ID=D.ID  ") ;

            tableName += ", LOCATOR L ";

        if(cr.getFilterText()!=null){
            String like = " AND (J.ID LIKE '%FTV%' OR upper(D.OBJECT_REF) LIKE '%FTV%' " +
                    "OR upper(J.DESCRIPTION) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' " +
                    "OR upper(STR.DESCRIPTION) LIKE '%FTV%' OR upper(TTR.DESCRIPTION) LIKE '%FTV%' " +
                    "OR upper(JS.CODE) LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }


        String sql = "SELECT " + fields + tableName  + where;

        return sql;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}
