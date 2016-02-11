package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Dec 18, 2006
 */
public class JobTaskTimesDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("day","DAY");
        nameToColumnMap.put("totalTime","TIME");
        nameToColumnMap.put("status","DESCRIPTION");  //not yet used
    }

    /**
     * Get work schedules for given jobtask
     * @return
     * @throws java.sql.SQLException
     */
    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql);
    }

    protected String getSql(SearchCriteria cr) {
        return "SELECT WS.ID, WS.DAY, P.NAME, JTT.TIME,SR.DESCRIPTION  FROM JOB_SCHEDULE JS, WORK_SCHEDULE WS, PERSON PSN, JOB_TASK_TIME JTT, PARTY P,WORK_SCHEDULE_STATUS_REF SR  " +
                "WHERE  WS.ID=JS.WORK_SCHEDULE_ID AND WS.PERSON_ID=PSN.ID AND JTT.JOB_TASK_ID =?  AND JTT.JOB_SCHEDULE_ID=JS.ID AND SR.ID=WS.STATUS_ID " +
                "AND P.ID=PSN.PARTY_ID AND JS.DELETED_TIME IS NULL ORDER BY WS.DAY DESC";
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
