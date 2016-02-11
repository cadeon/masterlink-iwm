package org.mlink.iwm.dao;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class ProjectStencilsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("organization","ORGANIZATION");
        nameToColumnMap.put("locatorId","LOCATOR_ID");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("autoplanning","AUTOPLANNING");
    }


    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        ProjectStencilsCriteria cr = (ProjectStencilsCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getOrganizationId()!=null) parameters.add(cr.getOrganizationId());
        if(cr.getLocatorId()!=null) parameters.add(cr.getLocatorId());
        return process(parameters,sql,request);
    }



    protected String getSql(SearchCriteria criteria) {
        ProjectStencilsCriteria cr = (ProjectStencilsCriteria)criteria;

        String table = "SEQUENCE S ";
        StringBuilder where = new StringBuilder(" S.ARCHIVED_DATE IS NULL AND S.ACTIVE="+cr.getActiveStatus());

        if(cr.getOrganizationId()==null){
            //table += ", ORGANIZATION O ";
        }else{
            table += " ,(SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) O ";
            where.append(" AND O.ID = S.ORGANIZATION_ID ");
        }

        if(cr.getLocatorId()==null){
            //table += ", LOCATOR L ";
        }else{
            table += " ,(SELECT ID,FULL_LOCATOR FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) L ";
            where.append(" AND L.ID=S.LOCATOR_ID ");
        }


        String fields = "S.ID, S.NAME,get_organization_hierarchy(S.ORGANIZATION_ID) ORGANIZATION, S.LOCATOR_ID,  S.AUTOPLANNING, nvl(TASK_COUNT,0) TASK_COUNT ";

        String counters ="SELECT SEQUENCE_ID, COUNT(*) TASK_COUNT FROM TASK_SEQUENCE TS, TASK T WHERE T.ID=TS.TASK_ID AND T.ARCHIVED_DATE IS NULL GROUP BY TS.SEQUENCE_ID ";

        if(cr.getFilterText()!=null){
            String like = " AND (upper(S.NAME) LIKE '%FTV%' OR S.ID LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }

        return "SELECT " + fields + " FROM "  + table + ", (" +counters +") CNT  WHERE CNT.SEQUENCE_ID(+)=S.ID AND" + where;

    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
