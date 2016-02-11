package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectTaskGroupsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        nameToColumnMap.put("skillTypeId","SKILL_TYPE_ID");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("custom","CUSTOM");
    }

    //this class is a Singleton
    /*private static ObjectTaskGroupsDAO ourInstance = new ObjectTaskGroupsDAO();
    public static ObjectTaskGroupsDAO getInstance() {return ourInstance;}
    private ObjectTaskGroupsDAO() {} */

    /**
     * Get data for locator
     * @param cr
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria cr) {
        String fields = " G.ID, G.DESCRIPTION, NVL(STR.DESCRIPTION,'dirty.save group to clean') SKILL_TYPE, STR.ID SKILL_TYPE_ID, g.custom, TASK_COUNT";
        String tableName = "TASK_GROUP G,  SKILL_TYPE_REF STR";
        String where = "STR.ID(+)=G.SKILL_TYPE_ID AND G.OBJECT_ID=?";
        String counters ="SELECT G.ID, COUNT(T.ID) TASK_COUNT FROM TASK_GROUP G, TASK T WHERE G.ID=T.GROUP_ID(+) GROUP BY G.ID";
        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE G.ID=CNT.ID AND " + where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


