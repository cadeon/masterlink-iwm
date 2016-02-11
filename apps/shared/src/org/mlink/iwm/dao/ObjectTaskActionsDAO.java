package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectTaskActionsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("taskId","TASK_ID");
        nameToColumnMap.put("actionDefId","ACTION_DEF_ID");
        nameToColumnMap.put("sequence","SEQUENCE");
        nameToColumnMap.put("verb","VERB");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("modifier","MODIFIER");
        nameToColumnMap.put("custom","CUSTOM");
    }

    //this class is a Singleton
    /*private static ObjectTaskActionsDAO ourInstance = new ObjectTaskActionsDAO();
    public static ObjectTaskActionsDAO getInstance() {return ourInstance;}
    private ObjectTaskActionsDAO() {}*/

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
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ID,SEQUENCE,VERB,NAME,MODIFIER,CUSTOM, TASK_ID, ACTION_DEF_ID FROM ACTION WHERE ARCHIVED_DATE IS NULL AND TASK_ID=?");
        return sb.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


