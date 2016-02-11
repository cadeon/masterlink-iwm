package org.mlink.iwm.dao;

import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.util.ConvertUtils;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ProjectsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("projectType","TYPE");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("createdBy","CREATEDBY");
        nameToColumnMap.put("jobCount","JOB_COUNT");
        nameToColumnMap.put("percentCompleted","PERCENT_COMPLETED");
        nameToColumnMap.put("status","STATUS");
    }

    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        ProjectsCriteria cr = (ProjectsCriteria)criteria;
        String sql = getSql(criteria);
        List parameters = new ArrayList();
        if(cr.getOrganization()!=null) parameters.add(cr.getOrganization());        
        if(cr.getProjectType()!=null) parameters.add(cr.getProjectType());
        if(cr.getRangeStartDate()!=null){
            try{
                Date date = ConvertUtils.string2Date(cr.getRangeStartDate());
                parameters.add(date);
            }catch(ParseException pe){
                cr.setRangeStartDate(null);
            }
        }
        if(cr.getRangeEndDate()!=null){
            try{
                Date date = ConvertUtils.string2Date(cr.getRangeEndDate());
                parameters.add(date);
            }catch(ParseException pe){
                cr.setRangeEndDate(null);
            }
        }

        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        ProjectsCriteria cr = (ProjectsCriteria) criteria;
        String fields = " P.ID, P.CREATED_DATE, PT.DESCRIPTION TYPE, P.NAME, P.CREATEDBY, PS.DESCRIPTION STATUS, nvl(JOB_COUNT,0) JOB_COUNT , ROUND(100*NVL(COMPLETED_JOB_COUNT,0)/NVL(JOB_COUNT,1)) PERCENT_COMPLETED  ";
        String table = " PROJECT P, PROJECT_TYPE_REF PT , PROJECT_STATUS_REF PS";
        String where = " PT.ID(+)=P.PROJECT_TYPE_ID AND P.ARCHIVED_DATE IS NULL AND PS.ID(+)=P.STATUS_ID ";
        if(criteria.getFilterText()!=null){
            String like = " AND (P.ID LIKE '%FTV%' OR TO_CHAR(P.CREATED_DATE,'MM/DD/YYYY') LIKE '%FTV%' OR upper(PT.DESCRIPTION) LIKE '%FTV%' OR upper(P.NAME) LIKE '%FTV%' OR upper(P.CREATEDBY) LIKE '%FTV%')";
            where += (like.replaceAll("FTV",criteria.getFilterText().toUpperCase()));
        }

        switch(cr.getCategory()){
            case History:
                where += " AND P.COMPLETED_DATE IS NOT NULL ";
                break;
            case Active:
                where += " AND P.COMPLETED_DATE IS NULL ";
                break;
            default: //no action
                break;
        }

        if(cr.getOrganization()!=null){
            table += " ,(SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) O ";
            where += " AND O.ID = P.ORGANIZATION_ID ";
        }

        if(cr.getProjectType()!=null){
            where += " AND P.PROJECT_TYPE_ID =? ";
        }
        if(cr.getRangeStartDate()!=null) {
            where += " AND P.CREATED_DATE >= ?";
        }
        if(cr.getRangeEndDate()!=null) {
            where += " AND P.CREATED_DATE <= ? ";
        }

        String jobCounter = "SELECT J.PROJECT_ID, COUNT(J.ID) JOB_COUNT FROM JOB J WHERE J.PROJECT_ID IS NOT NULL AND J.ARCHIVED_DATE IS NULL  GROUP BY PROJECT_ID";
        String completedJobCounter = "SELECT J.PROJECT_ID, COUNT(J.ID) COMPLETED_JOB_COUNT FROM JOB J, JOB_STATUS_REF JS WHERE JS.ID=J.STATUS_ID AND J.PROJECT_ID IS NOT NULL AND J.ARCHIVED_DATE IS NULL AND "+
        	"(JS.CODE IN "+JobStatusRef.finalStatusesSQLClauseMinusNIA+") GROUP BY PROJECT_ID";
        
        String sql = "SELECT " + fields + " FROM "  + table + ", (" +jobCounter +") CNT , (" +completedJobCounter +") CNT2  WHERE CNT.PROJECT_ID(+)=P.ID AND CNT2.PROJECT_ID(+)=P.ID AND" + where;
        return sql;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

