package org.mlink.iwm.dao;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTasksDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("estTime","ESTIMATED_TIME");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        nameToColumnMap.put("skillLevel","SKILL_LEVEL");
        nameToColumnMap.put("priority","PRIORITY");
        nameToColumnMap.put("taskType","TASK_TYPE");
        nameToColumnMap.put("actionCount","ACTION_COUNT");
        nameToColumnMap.put("instanceCount","INSTANCE_COUNT");
        nameToColumnMap.put("groupId","GROUP_ID");
        nameToColumnMap.put("skillTypeId","SKILL_TYPE_ID");
        nameToColumnMap.put("expiryNumOfDays","EXPIRY_NUMOFDAYS");
        nameToColumnMap.put("expiryTypeId","EXPIRY_TYPE_ID");        
    }


    /**
     * Get data for locator
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        TasksCriteria cr = (TasksCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        if(cr.getTaskTypeId()!=null) {
            parameters.add(cr.getTaskTypeId());   //optional
        }
        return process(parameters,sql,request);
    }


    protected String getSql2(TasksCriteria cr) {
        String fields = "D.ID, D.DESCRIPTION, D.ESTIMATED_TIME, D.SKILL_TYPE_ID, D.SKILL_LEVEL_ID, D.PRIORITY_ID, D.TASK_TYPE_ID, D.EXPIRY_TYPE_ID, D.EXPIRY_NUMOFDAYS ";
        String tableName = " OBJECT_DEF O, TASK_DEF D, ACTION_DEF A, TASK T";
        StringBuilder sql = new StringBuilder("select " + fields + ", count(DISTINCT A.ID) ACTION_COUNT, count(DISTINCT T.ID) INSTANCE_COUNT FROM "  + tableName);
        sql.append(" where O.ID = ? AND D.ARCHIVED_DATE IS NULL AND O.ID = D.OBJECT_DEF_ID AND D.ID=A.TASK_DEF_ID(+) AND D.ID=T.TASK_DEF_ID(+) ");
        if(cr.getTaskTypeId() != null) sql.append(" AND D.TASK_TYPE_ID=?");
        sql.append(" GROUP BY " + fields);

        /*String where = " D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL ";
        if("false".equals(Config.getProperty(Config.SHOW_AREA_OBJECTS,"true"))){             //do we need to show area objects?
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID);
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID);
        }*/

        String counters = " count(DISTINCT A.ID) ACTION_COUNT, count(DISTINCT T.ID) INSTANCE_COUNT " +
                " FROM OBJECT_DEF O, TASK_DEF D, ACTION_DEF A, TASK T " +
                " O.ID = D.OBJECT_DEF_ID AND D.ID=A.TASK_DEF_ID(+) AND D.ID=T.TASK_DEF_ID(+)  GROUP BY D.ID ";


        //String sql = "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE D.ID=CNT.ID AND" + where;


        return sql.toString();
    }

    protected String getSql3(SearchCriteria criteria) {
        TasksCriteria cr = (TasksCriteria) criteria;
        String fields = "D.ID, D.DESCRIPTION, D.ESTIMATED_TIME, D.SKILL_TYPE_ID, STR.DESCRIPTION SKILL_TYPE,  SLR.DESCRIPTION SKILL_LEVEL, PR.DESCRIPTION PRIORITY, " +
                " TTR.CODE TASK_TYPE, D.GROUP_ID, CNT.ACTION_COUNT, CNT.INSTANCE_COUNT, D.EXPIRY_TYPE_ID, D.EXPIRY_NUMOFDAYS ";
        String tableName = "OBJECT_DEF O, TASK_DEF D ,SKILL_TYPE_REF STR, SKILL_LEVEL_REF SLR, PRIORITY_REF PR, TASK_TYPE_REF TTR";

        StringBuilder where = new StringBuilder(" O.ID = ? AND D.ARCHIVED_DATE IS NULL AND O.ID = D.OBJECT_DEF_ID AND D.SKILL_TYPE_ID=STR.ID AND SLR.ID=D.SKILL_LEVEL_ID AND PR.ID=D.PRIORITY_ID AND D.TASK_TYPE_ID=TTR.ID ");
        if(cr.getTaskTypeId() != null) where.append(" AND D.TASK_TYPE_ID=?");

        // this version of grouping is much faster then just with one SELECT/count(DISTINCT)
        String counters ="SELECT T1.ID, ACTION_COUNT, INSTANCE_COUNT FROM " +
                "(SELECT D.ID, COUNT(A.ID) ACTION_COUNT FROM TASK_DEF D, ACTION_DEF A WHERE A.ARCHIVED_DATE IS NULL AND D.ID=A.TASK_DEF_ID(+) GROUP BY D.ID) T1, " +
                "(SELECT D.ID, COUNT(T.ID) INSTANCE_COUNT  FROM TASK_DEF D,  TASK T WHERE T.CUSTOM(+)=0 AND D.ID=T.TASK_DEF_ID(+) GROUP BY D.ID) T2 " +
                "WHERE T1.ID=T2.ID";

        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE D.ID=CNT.ID(+) AND" + where;
    }

    protected String getSql(SearchCriteria criteria) {
        TasksCriteria cr = (TasksCriteria) criteria;
        String fields = "D.ID, D.DESCRIPTION, D.ESTIMATED_TIME, D.SKILL_TYPE_ID, STR.DESCRIPTION SKILL_TYPE,  SLR.DESCRIPTION SKILL_LEVEL, PR.DESCRIPTION PRIORITY, " +
                " TTR.CODE TASK_TYPE, D.GROUP_ID, D.EXPIRY_TYPE_ID, D.EXPIRY_NUMOFDAYS, " +
                " (SELECT COUNT(A.ID) FROM ACTION_DEF A WHERE A.ARCHIVED_DATE IS NULL AND D.ID=A.TASK_DEF_ID) ACTION_COUNT, " +
                " (SELECT COUNT(T.ID) FROM TASK T WHERE T.CUSTOM=0 AND D.ID=T.TASK_DEF_ID) INSTANCE_COUNT ";
        String tableName = "OBJECT_DEF O, TASK_DEF D ,SKILL_TYPE_REF STR, SKILL_LEVEL_REF SLR, PRIORITY_REF PR, TASK_TYPE_REF TTR";

        StringBuilder where = new StringBuilder(" O.ID = ? AND D.ARCHIVED_DATE IS NULL AND O.ID = D.OBJECT_DEF_ID AND D.SKILL_TYPE_ID=STR.ID AND SLR.ID=D.SKILL_LEVEL_ID AND PR.ID=D.PRIORITY_ID AND D.TASK_TYPE_ID=TTR.ID ");
        if(cr.getTaskTypeId() != null) where.append(" AND D.TASK_TYPE_ID=?");

        // this version of grouping is much faster then just with one SELECT/count(DISTINCT)
        /*String counters ="SELECT T1.ID, ACTION_COUNT, INSTANCE_COUNT FROM " +
                "(SELECT D.ID, COUNT(A.ID) ACTION_COUNT FROM TASK_DEF D, ACTION_DEF A WHERE A.ARCHIVED_DATE IS NULL AND D.ID=A.TASK_DEF_ID(+) GROUP BY D.ID) T1, " +
                "(SELECT D.ID, COUNT(T.ID) INSTANCE_COUNT  FROM TASK_DEF D,  TASK T WHERE T.CUSTOM(+)=0 AND D.ID=T.TASK_DEF_ID(+) GROUP BY D.ID) T2 " +
                "WHERE T1.ID=T2.ID";*/

        return "SELECT " + fields + " FROM "  + tableName + " WHERE " + where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

