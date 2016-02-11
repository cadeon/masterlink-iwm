package org.mlink.iwm.dao;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class JobTasksDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("estTime","ESTIMATED_TIME");
        nameToColumnMap.put("totalTime","TOTAL_TIME");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        //nameToColumnMap.put("skillLevel","SKILL_LEVEL");
        //nameToColumnMap.put("priority","PRIORITY");
        nameToColumnMap.put("taskType","TASK_TYPE");
        nameToColumnMap.put("actionCount","ACTION_COUNT");
        //nameToColumnMap.put("custom","CUSTOM");
        //nameToColumnMap.put("active","ACTIVE");
        //nameToColumnMap.put("startDate","START_DATE");
    }

    //this class is a Singleton
    /*private static JobTasksDAO ourInstance = new JobTasksDAO();
    public static JobTasksDAO getInstance() {return ourInstance;}
    private JobTasksDAO() {} */

    /**
     * Get data
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        TasksCriteria cr=(TasksCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());

        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        /*String fields = "JT.ID, JT.DESCRIPTION, JT.ESTIMATED_TIME, JT.TOTAL_TIME, STR.DESCRIPTION SKILL_TYPE, TTR.CODE TASK_TYPE, CNT.ACTION_COUNT ";
        String tableName = "JOB_TASK JT, SKILL_TYPE_REF STR, TASK_TYPE_REF TTR";
        String where = " JT.JOB_ID = ? AND  JT.SKILL_TYPE_ID=STR.ID  AND JT.TASK_TYPE_ID=TTR.ID ";
        String counters ="SELECT A.JOB_TASK_ID, COUNT(A.ID) ACTION_COUNT FROM  JOB_ACTION A GROUP BY A.JOB_TASK_ID";
        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE CNT.JOB_TASK_ID(+)=JT.ID AND" + where;
        */
        String fields = "JT.ID, JT.DESCRIPTION, JT.ESTIMATED_TIME, JT.TOTAL_TIME, STR.DESCRIPTION SKILL_TYPE, TTR.CODE TASK_TYPE, " +
                "(SELECT COUNT(A.ID) FROM  JOB_ACTION A WHERE A.JOB_TASK_ID=JT.ID) ACTION_COUNT   ";
        String tableName = "JOB_TASK JT, SKILL_TYPE_REF STR, TASK_TYPE_REF TTR";
        String where = " JT.JOB_ID = ? AND  JT.SKILL_TYPE_ID=STR.ID  AND JT.TASK_TYPE_ID=TTR.ID ";
        return "SELECT " + fields + " FROM "  + tableName +  " WHERE " + where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

