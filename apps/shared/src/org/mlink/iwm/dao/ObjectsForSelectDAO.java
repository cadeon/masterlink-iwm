package org.mlink.iwm.dao;

import org.mlink.iwm.util.Config;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Jan 11, 2007
 */
public class ObjectsForSelectDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("objectId","ID");
        nameToColumnMap.put("classId","CLASS_ID");
        nameToColumnMap.put("objectRef","OBJECT_REF");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("active","ACTIVE");
        nameToColumnMap.put("classCode","CODE");
    }

    /**
     * Get all objects that recursively traverse to the given class and locator
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        ObjectsCriteria cr = (ObjectsCriteria) criteria;
        List params = new ArrayList();
        if(cr.getClassId()!=null)  params.add(cr.getClassId());
        if(cr.getLocatorId()!=null) params.add(cr.getLocatorId());
        return process(params,getSql(cr),request);
    }


    /**
     * this version uses better optimized grouping  for retrieving counters than  getOracleSql2
     * @param criteria
     * @return String
     */
    protected String getSql(SearchCriteria criteria) {
        ObjectsCriteria cr = (ObjectsCriteria) criteria;
        String objectClassification;
        if(cr.getClassId()==null){
            objectClassification = "OBJECT_CLASSIFICATION";
        }else{
            objectClassification = "(SELECT ID, CODE, DESCRIPTION FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)";
        }

        String locator;
        if(cr.getLocatorId()==null){
            locator = "LOCATOR";
        }else{
            locator = "(SELECT * FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID)";
        }

        String fields = "D.ID, D.CLASS_ID, D.OBJECT_REF, D.TAG, D.LOCATOR_ID,  " +
                "D.ACTIVE, L.FULL_LOCATOR,OC.CODE ";

        String tableName = " OBJECT D, " + objectClassification + " OC, " + locator + " L  ";
        String where = " D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL ";
        where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID);
        where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID);
        return  "SELECT " + fields + " FROM "  + tableName + " WHERE" + where;
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
