package org.mlink.iwm.dao;

import org.mlink.iwm.util.ConvertUtils;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * For given date returns all the jobs that are assigned (scheduled) to somebody
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 5, 2007
 */
public class ScheduledJobsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("jobStatus","JOB_STATUS");
        nameToColumnMap.put("priority","PRIORITY");
        nameToColumnMap.put("estTime","ESTIMATEDTIME");
        nameToColumnMap.put("skill","SKILL");
        nameToColumnMap.put("skillLevel","SKILL_LEVEL");
        nameToColumnMap.put("fullLocator","ADDRESS");
        nameToColumnMap.put("day","DAY");
        nameToColumnMap.put("shiftId","SHIFT_ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("status","STATUS");
        nameToColumnMap.put("statusDesc","DESCRIPTION");
        nameToColumnMap.put("hierarchy","HIERARCHY");
        nameToColumnMap.put("isSticky","STICKY");
    }


    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        TimeSpecsCriteria cr = (TimeSpecsCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getDate()!=null){
            try {
                parameters.add(ConvertUtils.string2Date(cr.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            parameters.add(new java.sql.Date(System.currentTimeMillis()));
        }
        if(cr.getOrganizationId()!=null)parameters.add(cr.getOrganizationId());
        if(cr.getLocatorId()!=null)parameters.add(cr.getLocatorId());
        if(cr.getShiftId()!=null)parameters.add(cr.getShiftId());


      return process(parameters,sql,request);
    }

    protected String getSql(SearchCriteria criteria) {
        TimeSpecsCriteria cr = (TimeSpecsCriteria)criteria;                                                                                    
        StringBuilder sql = new StringBuilder();

        String fields = "J.ID,J.ESTIMATEDTIME,WS.DAY,PSN.ID PERSON_ID, P.NAME,  DECODE(j.sticky,NULL,0,1) STICKY, L.ADDRESS," +
                " get_organization_hierarchy(PSN.ORGANIZATION_ID) HIERARCHY, WSR.DESCRIPTION STATUS, WS.SHIFT_ID,JSR.CODE JOB_STATUS, PR.DESCRIPTION PRIORITY,STR.CODE SKILL, SLR.VALUE SKILL_LEVEL";
        String select = "SELECT " + fields;
        String tableName = " FROM PERSON PSN, PARTY P, WORK_SCHEDULE WS, JOB_SCHEDULE JS,ORGANIZATION O, JOB J, LOCATOR L, WORK_SCHEDULE_STATUS_REF WSR, JOB_STATUS_REF JSR, SKILL_TYPE_REF STR, PRIORITY_REF PR, SKILL_LEVEL_REF SLR ";

        StringBuilder where = new StringBuilder("WHERE PSN.PARTY_ID = P.ID AND PSN.ID = WS.PERSON_ID AND WS.ID = JS.WORK_SCHEDULE_ID AND PSN.ORGANIZATION_ID=O.ID AND J.ID=JS.JOB_ID " +
                " AND WS.DAY = ? AND JS.DELETED_TIME IS  NULL " +
                " AND L.ID=WS.LOCATOR_ID AND WSR.ID=WS.STATUS_ID AND JSR.ID=J.STATUS_ID AND PR.ID=J.PRIORITY_ID AND STR.ID=J.SKILL_TYPE_ID AND SLR.ID=J.SKILL_LEVEL_ID");


        if( cr.getOrganizationId() != null){
            where.append(" AND PSN.ORGANIZATION_ID IN (SELECT ID FROM ORGANIZATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)");
        }
        if(cr.getLocatorId()!=null){
            where.append(" AND WS.LOCATOR_ID=? ");
        }
        if(cr.getShiftId()!=null){
            where.append(" AND W.SHIFT_ID=? ");
        }

        if(cr.getFilterText()!=null){
            String like = " AND (J.ID LIKE '%FTV%' OR upper(P.NAME) LIKE '%FTV%' OR upper(L.ADDRESS) LIKE '%FTV%' OR upper(get_organization_hierarchy(PSN.ORGANIZATION_ID)) LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }

        sql.append(select).append(tableName).append(where);
        return sql.toString();
    }




    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


