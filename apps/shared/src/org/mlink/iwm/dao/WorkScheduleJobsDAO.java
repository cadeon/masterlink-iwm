package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import org.mlink.iwm.util.Config;

/**
 * User: andrei
 * Date: Dec 21, 2006
 */
public class WorkScheduleJobsDAO extends ListDAOTemplate{
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("objectRef","OBJECT_REF");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("age","AGE");
        nameToColumnMap.put("priority","PRIORITY");
        nameToColumnMap.put("status","JOB_STATUS");
        nameToColumnMap.put("scheduledBy","USR");
        nameToColumnMap.put("scheduledTime","CREATED_TIME");

    }

    /**
     * Get jobs for that work schedule
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,getSql(cr),request);
    }

    protected String getSql(SearchCriteria cr) {
        return "SELECT J.ID,J.DESCRIPTION, O.OBJECT_REF,L.FULL_LOCATOR, CEIL(nvl(J.COMPLETED_DATE,SYSDATE)-J.CREATED_DATE) AGE, " +
                "PR.DESCRIPTION PRIORITY,JST.CODE JOB_STATUS, TO_CHAR(new_time(JS.CREATED_TIME, '"+Config.getProperty(Config.SERVER_TIMEZONE)+"', '"+Config.getProperty(Config.REMOTE_TIMEZONE)+"'), '"+Config.getProperty(Config.SQL_TIME_PATTERN)+"') as CREATED_TIME, JS.USR " +
                "FROM  JOB J, LOCATOR L, OBJECT O, JOB_SCHEDULE JS, PRIORITY_REF PR, JOB_STATUS_REF JST " +
                "WHERE J.STATUS_ID=JST.ID  AND JS.DELETED_TIME IS NULL AND JS.WORK_SCHEDULE_ID=? AND JS.JOB_ID=J.ID AND J.LOCATOR_ID=L.ID AND J.OBJECT_ID=O.ID AND J.PRIORITY_ID=PR.ID";
    }

   // "SR.DESCRIPTION, , JS.USR " +


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
