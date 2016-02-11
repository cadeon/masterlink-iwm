package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class JobTaskActionsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("sequence","SEQUENCE");
        nameToColumnMap.put("verb","VERB");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("modifier","MODIFIER");
        nameToColumnMap.put("fieldCondition","FIELD_CONDITION");
    }

    //this class is a Singleton
    /*private static JobTaskActionsDAO ourInstance = new JobTaskActionsDAO();
    public static JobTaskActionsDAO getInstance() {return ourInstance;}
    private JobTaskActionsDAO() {}
    */
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
        //andrei 04/20/07 action props are now copied to jobaction sb.append("SELECT JA.ID,A.SEQUENCE,A.VERB,A.NAME,A.MODIFIER,FIELD_CONDITION FROM ACTION A, JOB_ACTION JA WHERE JA.ACTION_ID=A.ID AND JA.JOB_TASK_ID=?");
        sb.append("SELECT A.ID,A.SEQUENCE,A.VERB,A.NAME,A.MODIFIER,A.FIELD_CONDITION FROM JOB_ACTION A WHERE A.JOB_TASK_ID=?");
        return sb.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


