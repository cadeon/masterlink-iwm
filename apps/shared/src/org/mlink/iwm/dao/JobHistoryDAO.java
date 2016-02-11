package org.mlink.iwm.dao;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.Config;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Nov 18, 2006
 */
public class JobHistoryDAO extends ListDAOTemplate {
    private static final Logger logger = Logger.getLogger(ListDAOTemplate.class);
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("phone","PHONE");
        nameToColumnMap.put("email","EMAIL");
        nameToColumnMap.put("day","DAY");
        nameToColumnMap.put("totalTime","TOTAL_TIME");
        nameToColumnMap.put("status","DESCRIPTION");
        nameToColumnMap.put("hierarchy","HIERARCHY");
        nameToColumnMap.put("scheduledBy","USR");
        nameToColumnMap.put("scheduledTime","CREATED_TIME");
        nameToColumnMap.put("personId", "PERSON_ID");
    }

    /**
     * Get WORKS SCHEDULES THAT HAD THAT JOB
     * @return
     * @throws SQLException
     */
    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql);
    }

    protected String getSql(SearchCriteria cr) {
        return "SELECT WS.ID, WS.DAY, WS.PERSON_ID, P.NAME, P.PHONE, P.EMAIL, get_organization_hierarchy(PSN.ORGANIZATION_ID) HIERARCHY,SUM(JTT.TIME) TOTAL_TIME, " +
                " SR.DESCRIPTION, TO_CHAR(new_time(JS.CREATED_TIME, '"+Config.getProperty(Config.SERVER_TIMEZONE)+"', '"+Config.getProperty(Config.REMOTE_TIMEZONE)+"'), '"+Config.getProperty(Config.SQL_TIME_PATTERN)+"') as CREATED_TIME, JS.USR" +
                " FROM JOB_SCHEDULE JS, WORK_SCHEDULE WS, PERSON PSN, JOB_TASK_TIME JTT, PARTY P,WORK_SCHEDULE_STATUS_REF SR " +
                " WHERE  WS.ID=JS.WORK_SCHEDULE_ID AND WS.PERSON_ID=PSN.ID AND JS.JOB_ID=? AND SR.ID=WS.STATUS_ID" +
                " AND JTT.JOB_SCHEDULE_ID(+)=JS.ID AND P.ID=PSN.PARTY_ID AND JS.DELETED_TIME IS NULL AND WS.DAY < SYSDATE" +
                " GROUP BY WS.ID, WS.DAY, WS.PERSON_ID, P.NAME, P.PHONE, P.EMAIL, get_organization_hierarchy(PSN.ORGANIZATION_ID), SR.DESCRIPTION, CREATED_TIME, JS.USR ORDER BY WS.DAY DESC";
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
