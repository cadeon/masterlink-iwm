package org.mlink.iwm.dao;


import java.util.Date;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import org.mlink.iwm.util.DateUtil;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class WorkersDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("userId","USER_ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("active","ACTIVE");
        nameToColumnMap.put("phone","PHONE");
        nameToColumnMap.put("fax","FAX");
        nameToColumnMap.put("hierarchy","HIERARCHY");
        nameToColumnMap.put("organizationId","ORGANIZATION_ID");
        nameToColumnMap.put("locInfo", "LOC_INFO");
    }
    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        WorkersCriteria cr = (WorkersCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getId()!=null)parameters.add(cr.getId());
        if(cr.getIsActive()!=null)parameters.add(cr.getIsActive());
        if(cr.getWorkerStatus()!=null)parameters.add(cr.getWorkerStatus());
        return process(parameters,sql,request);
    }

    protected String getSql(SearchCriteria criteria) {
        WorkersCriteria cr = (WorkersCriteria)criteria;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT WORKER.*, LOC_INFO FROM (");
        sql.append("SELECT L.ID, U.ID USER_ID, P.NAME, L.ACTIVE, L.STATUS_ID, O.ID AS ORG_ID, P.PHONE, P.FAX, get_organization_hierarchy(O.ID) HIERARCHY,L.ORGANIZATION_ID ");
        sql.append(" FROM PERSON L, PARTY P, ADDRESS A, ORGANIZATION O, APPUSER U ");
        sql.append(" WHERE L.PARTY_ID = P.ID AND P.ADDRESS_ID=A.ID AND L.ARCHIVED_DATE IS NULL AND O.ID=L.ORGANIZATION_ID AND U.PERSON_ID=L.ID ");
        
        if( cr.getId() != null){
            //sql.append(" AND L.ORGANIZATION_ID = ?");
            sql.append(" AND ORGANIZATION_ID IN (SELECT ID FROM ORGANIZATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)");
        }
        if(cr.getIsActive() != null){
            sql.append(" AND ACTIVE = ?");
        }

        if(cr.getWorkerStatus() != null){
            sql.append(" AND STATUS_ID = ?");
        }

        if(cr.getFilterText()!=null){
            String like = " AND (upper(NAME) LIKE '%FTV%' OR upper(get_organization_hierarchy(ORGANIZATION_ID)) LIKE '%FTV%')";
            sql.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }
        

        sql.append(") WORKER LEFT OUTER JOIN MW_ACCESS_VIEW4 MWAV ON WORKER.ID=MWAV.PERSON_ID ");
        sql.append(" AND MWAV.SCHEDULE = '"+DateUtil.displayShortDate(new Date(System.currentTimeMillis()))+"' ");
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}