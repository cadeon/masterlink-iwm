package org.mlink.iwm.dao;



import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * For a given job and date range Returns workschedules assigned to it as well as possible candidate workschedules
 * User: andrei
 * Date: Nov 15, 2006
 */
public class JobSchedulesDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("personId","PERSON_ID");
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("organizationName","ORG_NAME");
        nameToColumnMap.put("isAssigned","IS_ASSIGNED");
        nameToColumnMap.put("isSticky","STICKY");
        nameToColumnMap.put("day","DAY");
    }


    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        JobSchedulesCriteria cr = (JobSchedulesCriteria)criteria;
        if(cr.getEndDate()==null) cr.setEndDate(cr.getStartDate());
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());          // need it twice
        parameters.add(cr.getStartDate());
        parameters.add(cr.getEndDate());
        parameters.add(cr.getId());          // need it twice
        parameters.add(cr.getStartDate());
        parameters.add(cr.getEndDate());

        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT WS.ID,WS.DAY,PSN.ID PERSON_ID, P.NAME, P2.NAME ORG_NAME, 0 IS_ASSIGNED,  0 STICKY" +
                "   FROM  PERSON PSN, PARTY P, WORK_SCHEDULE WS, SKILL S, SKILL_LEVEL_REF RP, SKILL_LEVEL_REF RJ, JOB J,ORGANIZATION O,PARTY P2 " +
                "   WHERE PSN.ID = WS.PERSON_ID AND PSN.PARTY_ID=P.ID  AND S.PERSON_ID=PSN.ID AND S.SKILL_TYPE_ID=J.SKILL_TYPE_ID AND PSN.ACTIVE = 1 " +
                "    AND S.SKILL_LEVEL_ID=RP.ID AND RJ.ID = J.SKILL_LEVEL_ID AND RP.VALUE >= RJ.VALUE " +
                "    AND WS.ID NOT IN (SELECT WORK_SCHEDULE_ID FROM JOB_SCHEDULE JS WHERE JS.JOB_ID = J.ID AND JS.DELETED_TIME IS NULL) " +
                "    AND WS.LOCATOR_ID IN (SELECT ID FROM LOCATOR START WITH ID=J.LOCATOR_ID CONNECT BY PRIOR PARENT_ID=ID) AND PSN.ORGANIZATION_ID=O.ID AND O.PARTY_ID=P2.ID " +
                "    AND J.ID = ? AND WS.DAY >= ? AND  WS.DAY <= ? AND WS.ARCHIVED_DATE IS NULL" +
                "   UNION " +
                "   SELECT WS.ID,WS.DAY,PSN.ID PERSON_ID, P.NAME, P2.NAME ORG_NAME, 1 IS_ASSIGNED,  DECODE(j.sticky,NULL,0,1) STICKY  " +
                "   FROM PERSON PSN, PARTY P, WORK_SCHEDULE WS, JOB_SCHEDULE JS,ORGANIZATION O,PARTY P2, JOB J " +
                "   WHERE PSN.PARTY_ID = P.ID AND PSN.ID = WS.PERSON_ID AND WS.ID = JS.WORK_SCHEDULE_ID AND PSN.ORGANIZATION_ID=O.ID AND O.PARTY_ID=P2.ID AND J.ID=JS.JOB_ID " +
                "    AND JS.JOB_ID = ? AND WS.DAY >= ? AND  WS.DAY <= ? AND JS.DELETED_TIME IS  NULL");

        return sql.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

