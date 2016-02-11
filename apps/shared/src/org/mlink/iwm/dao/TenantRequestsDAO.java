package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 31, 2006
 */
public class TenantRequestsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("jobId","JOB_ID");
        nameToColumnMap.put("status","JOB_STATUS");
        nameToColumnMap.put("jobDescription","DESCRIPTION");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("note","NOTE");
        nameToColumnMap.put("requestType","REQUEST_TYPE");
        nameToColumnMap.put("createdDate","CREATED_DATE");
    }

    //this class is a Singleton
    //private static TenantRequestsDAO ourInstance = new TenantRequestsDAO();
    //public static TenantRequestsDAO getInstance() {return ourInstance;}
    //private TenantRequestsDAO() {}

    /**
     * Get all objects that recursively traverse to the given class and locator
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        List params = new ArrayList();
        if(cr.getId()!=null) params.add(cr.getId());
        return process(params,getSql(cr),request);
    }


    protected String getSql(SearchCriteria cr) {

        String fields = "SELECT TR.ID, TR.JOB_ID,L.FULL_LOCATOR, TD.DESCRIPTION, JS.CODE JOB_STATUS, TR.TENANT_NAME,TR.EMAIL,TR.NOTE,TR.PHONE,TR.REQUEST_TYPE,TR.CREATED_DATE ";
        StringBuilder tableName = new StringBuilder(" FROM TENANT_REQUEST_UNIQUE TR, ACTIVE_JOBS_VIEW AJ, TASK_DEF TD, JOB_STATUS_REF JS");
        StringBuilder where = new StringBuilder(" WHERE TR.JOB_ID=AJ.ID AND AJ.STATUS_ID=JS.ID AND TR.LOCATOR_ID=L.ID AND TD.ID=TR.PROBLEM_ID") ;

        if(cr.getId()==null){
            tableName.append(", LOCATOR L ");
        }else{
            tableName.append(" ,(SELECT ID,FULL_LOCATOR FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) L ");
        }

        if(cr.getFilterText()!=null){
            String like = " AND (TR.JOB_ID LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' " +
                    "OR  upper(TD.DESCRIPTION) LIKE '%FTV%' OR  upper(JS.CODE) LIKE '%FTV%' " +
                    "OR  upper(TR.NOTE) LIKE '%FTV%' " +
                    "OR  upper(TR.REQUEST_TYPE) LIKE '%FTV%' " +
                    "OR  TO_CHAR(TR.CREATED_DATE,'MM/DD/YYYY') LIKE '%FTV%' " +
                    ")";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }
        return fields + tableName  + where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}
