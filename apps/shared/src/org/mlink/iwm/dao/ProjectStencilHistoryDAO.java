package org.mlink.iwm.dao;

import org.apache.log4j.Logger;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Nov 18, 2006
 */
public class ProjectStencilHistoryDAO extends ListDAOTemplate {
    private static final Logger logger = Logger.getLogger(ProjectStencilHistoryDAO.class);
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("startedDate",  "STARTED_DATE");
        nameToColumnMap.put("completedDate","COMPLETED_DATE");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("duration","DURATION");
        nameToColumnMap.put("status","STATUS");
    }

    /**
     * Get WORKS SCHEDULES THAT HAD THAT JOB
     * @return
     * @throws SQLException
     */
    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());   //projectStencilId
        return process(parameters,sql);
    }

    protected String getSql(SearchCriteria cr) {
        return "SELECT P.ID, P.CREATED_DATE, P.STARTED_DATE, P.COMPLETED_DATE, CEIL(NVL(P.COMPLETED_DATE,SYSDATE)-P.CREATED_DATE) DURATION, PS.DESCRIPTION STATUS \n" +
                "FROM PROJECT P, PROJECT_STATUS_REF PS WHERE P.SEQUENCE_ID=? AND P.STATUS_ID=PS.ID ORDER BY P.ID DESC";
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
