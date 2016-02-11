package org.mlink.iwm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ShiftTimingReportDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
    	nameToColumnMap.put("personId","PERSON_ID");
        nameToColumnMap.put("orgId","ORGANIZATION_ID");
        nameToColumnMap.put("orgName","ORGANIZATION_NAME");
        nameToColumnMap.put("userName","USER_NAME");
        nameToColumnMap.put("startDate","SHIFT_START_DATE");
        nameToColumnMap.put("stopDate","SHIFT_STOP_DATE");
        nameToColumnMap.put("shiftDurIncBreaks","SHIFT_DURATION_INC_BREAKS");
        nameToColumnMap.put("shiftDur","SHIFT_DURATION");
        nameToColumnMap.put("timeOnBreaks","TIME_ON_BREAKS");
        nameToColumnMap.put("timeOnJobTasks","TIME_ON_JOBTASKS");
        nameToColumnMap.put("npt","NPT");
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
        parameters.add(criteria.getFilterText());
        parameters.add(criteria.getId());
        return process(parameters,sql, request);
    }

    protected String getSql(SearchCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        sql.append("select PRTU.NAME as USER_NAME, ORG.ID AS ORGANIZATION_ID, PRT.NAME AS ORGANIZATION_NAME, ST.PERSON_ID," +
        		" TO_CHAR(SHIFT_START_DATE,'HH24:MI') as SHIFT_START_DATE, TO_CHAR(SHIFT_STOP_DATE,'HH24:MI') AS SHIFT_STOP_DATE," +
        		" NVL(ROUND((SHIFT_STOP_DATE-SHIFT_START_DATE)*24*60),0) SHIFT_DURATION_INC_BREAKS, TIME_ON_BREAKS," +
        		" (ROUND((SHIFT_STOP_DATE-SHIFT_START_DATE)*24*60)-TIME_ON_BREAKS) SHIFT_DURATION,TIME_ON_JOBTASKS," +
        		" (ROUND((SHIFT_STOP_DATE-SHIFT_START_DATE)*24*60)-TIME_ON_BREAKS-TIME_ON_JOBTASKS) NPT" +
        		" from shift_trace st " +
        		" INNER JOIN PERSON P1 ON st.person_ID = P1.ID INNER JOIN ORGANIZATION ORG ON ORG.ID=P1.ORGANIZATION_ID " +
        		" INNER JOIN PARTY PRT ON ORG.PARTY_ID=PRT.ID " +
        		" INNER JOIN PARTY PRTU ON P1.PARTY_ID=PRTU.ID" +
        		" where ST.SCHEDULE=? and org.id=?");
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}