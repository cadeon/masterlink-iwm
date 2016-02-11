package org.mlink.iwm.dao;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ProjectStencilTasksDAO extends ObjectTasksDAO {

    static {
        nameToColumnMap.put("objectRef","OBJECT_REF");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("sequenceLevel","SEQUENCE_LEVEL");
        nameToColumnMap.put("taskSequenceId","TASK_SEQUENCE_ID");
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
        parameters.add(cr.getId());  //projectStencilId or equally sequenceId
        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        String fields = "D.ID, D.DESCRIPTION, D.GROUP_ID, D.ESTIMATED_TIME, D.SKILL_TYPE_ID, STR.DESCRIPTION SKILL_TYPE, PR.DESCRIPTION PRIORITY, " +
                " TTR.DESCRIPTION TASK_TYPE, D.CUSTOM, D.ACTIVE,D.START_DATE,O.OBJECT_REF,L.FULL_LOCATOR,TS.SEQUENCE_LEVEL,TS.ID TASK_SEQUENCE_ID ";
        String tableName = "OBJECT O, TASK D ,SKILL_TYPE_REF STR, PRIORITY_REF PR, TASK_TYPE_REF TTR, TASK_SEQUENCE TS,LOCATOR L ";
        String where = " WHERE  D.ARCHIVED_DATE IS NULL AND O.ID = D.OBJECT_ID AND O.LOCATOR_ID=L.ID " +
                "AND D.SKILL_TYPE_ID=STR.ID AND PR.ID=D.PRIORITY_ID " +
                "AND D.TASK_TYPE_ID=TTR.ID AND TS.TASK_ID=D.ID AND TS.SEQUENCE_ID=?";
        return "SELECT " + fields + " FROM "  + tableName +  " " + where ;
    }

}


