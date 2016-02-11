package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectDataDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("dataTypeId","DATA_TYPE_ID");
        nameToColumnMap.put("uomId","UOM_ID");
        nameToColumnMap.put("dataValue","DATA_VALUE");
        nameToColumnMap.put("dataLabel","DATA_LABEL");
        nameToColumnMap.put("isDisplay","IS_DISPLAY");
        nameToColumnMap.put("isEditInField","IS_EDIT_IN_FIELD");
        nameToColumnMap.put("custom","CUSTOM");
    }


    /**
     * Get data for locator
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria , PaginationRequest request) throws SQLException {
        DataSearchCriteria cr = (DataSearchCriteria) criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        if(cr.getDataTypeId()!=null) parameters.add(cr.getDataTypeId());
        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        DataSearchCriteria cr = (DataSearchCriteria)criteria;
        String whereDataType = "";
        if(cr.getDataTypeId()!=null){
            whereDataType=" AND DATA_TYPE_ID=?";
        }
        if(cr.getIsDisplayOnly()){
            whereDataType=" AND IS_DISPLAY=1";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ID,DATA_TYPE_ID,UOM_ID,DATA_VALUE,DATA_LABEL,IS_DISPLAY,CUSTOM,IS_EDIT_IN_FIELD FROM  OBJECT_DATA where OBJECT_ID =?");
        sb.append(whereDataType);
        return sb.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}


