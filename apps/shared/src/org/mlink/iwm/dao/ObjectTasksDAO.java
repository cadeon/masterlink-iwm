package org.mlink.iwm.dao;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectTasksDAO extends ListDAOTemplate {
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
        nameToColumnMap.put("custom","CUSTOM");
        nameToColumnMap.put("active","ACTIVE");
        nameToColumnMap.put("startDate","START_DATE");
        nameToColumnMap.put("groupId","GROUP_ID");
        nameToColumnMap.put("skillTypeId","SKILL_TYPE_ID");
        nameToColumnMap.put("objectId","OBJECT_ID");
        nameToColumnMap.put("organizationId","ORGANIZATION_ID");
    }

    /**
     * Get data
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


    protected String getSql(SearchCriteria criteria) {
        TasksCriteria cr = (TasksCriteria)criteria;
        String fields = "D.ID, D.DESCRIPTION, D.ORGANIZATION_ID, D.OBJECT_ID, D.GROUP_ID, D.ESTIMATED_TIME, D.SKILL_TYPE_ID, STR.DESCRIPTION SKILL_TYPE,  SLR.DESCRIPTION SKILL_LEVEL, PR.DESCRIPTION PRIORITY, " +
                " TTR.CODE TASK_TYPE, D.CUSTOM, D.ACTIVE,D.START_DATE ";
        String tableName = "OBJECT O, TASK D ,SKILL_TYPE_REF STR, SKILL_LEVEL_REF SLR, PRIORITY_REF PR, TASK_TYPE_REF TTR";

        StringBuilder where = new StringBuilder(" WHERE O.ID = ? AND D.ARCHIVED_DATE IS NULL AND O.ID = D.OBJECT_ID AND D.SKILL_TYPE_ID=STR.ID AND SLR.ID=D.SKILL_LEVEL_ID AND PR.ID=D.PRIORITY_ID AND D.TASK_TYPE_ID=TTR.ID ");
        if(cr.getTaskTypeId() != null) where.append(" AND D.TASK_TYPE_ID=?");

        // this version of grouping is much faster then just with one SELECT/count(DISTINCT)
        String counters ="SELECT A.TASK_ID, COUNT(A.ID) ACTION_COUNT FROM  ACTION A GROUP BY A.TASK_ID";
        return "SELECT " + fields + ", CNT.ACTION_COUNT  FROM "  + tableName + ", (" +counters +") CNT  " + where + " AND CNT.TASK_ID(+)=D.ID ";
        //return "SELECT " + fields + " FROM "  + tableName + where ;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

