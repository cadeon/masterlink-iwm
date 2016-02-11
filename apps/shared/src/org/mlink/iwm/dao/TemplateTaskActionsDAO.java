package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTaskActionsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("sequence","SEQUENCEN");
        nameToColumnMap.put("verb","VERB");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("modifier","MODIFIER");
    }


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
        // to_number(SEQUENCE) is here to make conversion from CHAR col to number for correct ordering. SEQUNCE col has char data type which is a mistake hard to correct for now
        sb.append("SELECT ID,to_number(SEQUENCE) SEQUENCEN ,VERB,NAME,MODIFIER FROM ACTION_DEF WHERE ARCHIVED_DATE IS NULL AND TASK_DEF_ID=?");
        return sb.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


