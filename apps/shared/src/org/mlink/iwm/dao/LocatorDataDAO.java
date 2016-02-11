package org.mlink.iwm.dao;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class LocatorDataDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("dataTypeId","DATA_TYPE_ID");
        nameToColumnMap.put("uomId","UOM_ID");
        nameToColumnMap.put("dataValue","DATA_VALUE");
        nameToColumnMap.put("dataLabel","DATA_LABEL");
    }


    /**
     * Get data for locator
     * @param request
     * @return
     * @throws SQLException
     */
    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria cr) {
        return "select id,data_type_id,uom_id,data_value,data_label from  locator_data where locator_id=?";
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
