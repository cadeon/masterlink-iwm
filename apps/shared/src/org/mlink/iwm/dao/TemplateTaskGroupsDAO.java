package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTaskGroupsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        nameToColumnMap.put("skillTypeId","SKILL_TYPE_ID");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("instanceCount","INSTANCE_COUNT");
    }

    //this class is a Singleton
    /*private static TemplateTaskGroupsDAO ourInstance = new TemplateTaskGroupsDAO();
    public static TemplateTaskGroupsDAO getInstance() {return ourInstance;}
    private TemplateTaskGroupsDAO() {}*/

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
        String fields = " G.ID, G.DESCRIPTION, NVL(STR.DESCRIPTION,'Skil Type is invalid. Edit&Save group to reset!') SKILL_TYPE, STR.ID SKILL_TYPE_ID, TASK_COUNT, INSTANCE_COUNT";
        String tableName = "TASK_GROUP_DEF G,  SKILL_TYPE_REF STR";
        String where = "STR.ID(+)=G.SKILL_TYPE_ID AND G.OBJECT_DEF_ID=?";
        String counters ="SELECT T1.ID,  TASK_COUNT, INSTANCE_COUNT FROM "
                        + " (SELECT G.ID, COUNT(T.ID) TASK_COUNT FROM TASK_GROUP_DEF G, TASK_DEF T WHERE G.ID=T.GROUP_ID(+) GROUP BY G.ID) T1, "
                        + " (SELECT G.ID, COUNT(TG.ID) INSTANCE_COUNT FROM TASK_GROUP_DEF G, TASK_GROUP TG WHERE G.ID=TG.TASK_GROUP_DEF_ID(+) GROUP BY G.ID) T2 "+
                          " WHERE T1.ID=T2.ID "        ;
        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE G.ID=CNT.ID AND " + where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


