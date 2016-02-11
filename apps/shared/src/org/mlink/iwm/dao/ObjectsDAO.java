package org.mlink.iwm.dao;

import org.mlink.iwm.util.Config;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class ObjectsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("objectId","ID");
        nameToColumnMap.put("classId","CLASS_ID");
        nameToColumnMap.put("objectRef","OBJECT_REF");
        //nameToColumnMap.put("tag","TAG");
        nameToColumnMap.put("locatorId","LOCATOR_ID");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("active","ACTIVE");
        //nameToColumnMap.put("custom","CUSTOM");
        nameToColumnMap.put("hasCustomData","HAS_CUSTOM_DATA");
        nameToColumnMap.put("hasCustomTask","HAS_CUSTOM_TASK");
        nameToColumnMap.put("hasCustomTaskGroup","HAS_CUSTOM_TASK_GROUP");
        nameToColumnMap.put("childrenCount","CHILDREN_COUNT");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("taskGroupCount","TASK_GROUP_COUNT");
        nameToColumnMap.put("dataCount","OBJECT_DATA_COUNT");
        nameToColumnMap.put("classCode","CODE");
        nameToColumnMap.put("organizationId","ORGANIZATION_ID");
        //nameToColumnMap.put("classDesc","DESCRIPTION");
    }

    //this class is a Singleton
    //private static ObjectsDAO ourInstance = new ObjectsDAO();
    //public static ObjectsDAO getInstance() {return ourInstance;}
    //private ObjectsDAO() {}

    /**
     * Get all objects that recursively traverse to the given class and locator
     * @param request
     * @return
     * @throws SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        ObjectsCriteria cr = (ObjectsCriteria) criteria;
        List params = new ArrayList();
        if(cr.getClassId()!=null)  params.add(cr.getClassId());
        if(cr.getLocatorId()!=null) params.add(cr.getLocatorId());
        if(cr.getOrganizationId()!=null) params.add(cr.getOrganizationId());
        return process(params,getSql(cr),request);
    }
    
    /**
     * Get WORKS SCHEDULES THAT HAD THAT JOB
     * @return
     * @throws SQLException
     */
    public DAOResponse getData(SearchCriteria criteria) throws SQLException {
    	ObjectsCriteria cr = (ObjectsCriteria) criteria;
    	String sql = getSql4(cr);
        List params = new ArrayList();
        if(cr.getClassId()!=null)  params.add(cr.getClassId());
        if(cr.getLocatorId()!=null) params.add(cr.getLocatorId());
        return process(params,sql);
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
        
        String organization="";
        // add org IFF we need it
        if(cr.getOrganizationId()!=null){
        	organization = ", (SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) ORG";
        }

        String fields = "D.ID, D.CLASS_ID, D.OBJECT_REF, D.TAG, D.LOCATOR_ID,  D.ORGANIZATION_ID, " +
                "D.ACTIVE, D.CUSTOM, D.HAS_CUSTOM_DATA, D.HAS_CUSTOM_TASK, D.HAS_CUSTOM_TASK_GROUP, " +
                "L.FULL_LOCATOR,OC.CODE, " +
                "(SELECT COUNT(CO.ID) FROM OBJECT CO WHERE CO.PARENT_OBJECT_ID=D.ID AND CO.ARCHIVED_DATE IS NULL) CHILDREN_COUNT, "+
                "(SELECT COUNT(T.ID) FROM TASK T WHERE T.OBJECT_ID=D.ID AND T.ARCHIVED_DATE IS NULL) TASK_COUNT, " +
                "(SELECT COUNT(TG.ID) FROM TASK_GROUP TG WHERE TG.OBJECT_ID=D.ID) TASK_GROUP_COUNT, " +
                "(SELECT COUNT(OD.ID) FROM OBJECT_DATA OD  WHERE OD.OBJECT_ID=D.ID) OBJECT_DATA_COUNT ";

        String tableName = " OBJECT D, " + objectClassification + " OC, " + locator + " L  "+ organization ;

        String where = " D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL ";
        
        if("false".equals(Config.getProperty(Config.SHOW_AREA_OBJECTS,"true"))){             //do we need to show area objects?
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID);
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID);
        }

        if(cr.getFilterText()!=null){
            String like = " AND (upper(D.OBJECT_REF) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' OR upper(OC.CODE) LIKE '%FTV%' OR upper(OC.DESCRIPTION) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }

        if(cr.getId()!=null){
            where += " AND PARENT_OBJECT_ID = "+cr.getId();
        }else{
        	where += " AND D.PARENT_OBJECT_ID is null ";
        }
        
        if(cr.getOrganizationId()!=null){
            where += " AND D.ORGANIZATION_ID(+)=ORG.ID ";
        }
        
        String sql = "SELECT " + fields + " FROM "  + tableName + " WHERE " + where;
        return sql;
    }

    protected String getSql3(SearchCriteria criteria) {
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
                "D.ACTIVE, D.CUSTOM, D.HAS_CUSTOM_DATA, D.HAS_CUSTOM_TASK, D.HAS_CUSTOM_TASK_GROUP, " +
                "L.FULL_LOCATOR,OC.CODE, TASK_COUNT,TASK_GROUP_COUNT,OBJECT_DATA_COUNT ";

        String tableName = " OBJECT D, " + objectClassification + " OC, " + locator + " L  ";

        String where = " D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL ";
        if("false".equals(Config.getProperty(Config.SHOW_AREA_OBJECTS,"true"))){             //do we need to show area objects?
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID);
            where += " AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID);
        }

        /**String counters = " SELECT D.ID, COUNT(DISTINCT T.ID) TASK_COUNT, COUNT(DISTINCT TG.ID) TASK_GROUP_COUNT, COUNT(DISTINCT OD.ID) OBJECT_DATA_COUNT " +
                " FROM OBJECT D, TASK T, TASK_GROUP TG, OBJECT_DATA OD " +
                "WHERE T.OBJECT_ID(+)=D.ID AND TG.OBJECT_ID(+)=D.ID AND OD.OBJECT_ID(+)=D.ID  GROUP BY D.ID ";
         */
        // this version of grouping is much faster then just with one SELECT
        String counters = "SELECT T1.ID, TASK_COUNT,TASK_GROUP_COUNT, OBJECT_DATA_COUNT FROM " +
                "(SELECT D.ID, COUNT(T.ID) TASK_COUNT FROM OBJECT D, TASK T WHERE T.OBJECT_ID(+)=D.ID  GROUP BY D.ID) T1, " +
                "(SELECT D.ID, COUNT(TG.ID) TASK_GROUP_COUNT FROM OBJECT D, TASK_GROUP TG WHERE TG.OBJECT_ID(+)=D.ID  GROUP BY D.ID) T2, " +
                "(SELECT D.ID, COUNT(OD.ID) OBJECT_DATA_COUNT FROM OBJECT D, OBJECT_DATA OD  WHERE OD.OBJECT_ID(+)=D.ID  GROUP BY D.ID) T3 " +
                "WHERE T1.ID=T2.ID AND T1.ID=T3.ID ORDER BY T1.ID ";

        if(cr.getFilterText()!=null){
            String like = " AND (upper(D.OBJECT_REF) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' OR upper(OC.CODE) LIKE '%FTV%' OR upper(OC.DESCRIPTION) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }

        String sql = "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE D.ID=CNT.ID AND" + where;


        return sql;
    }
    /*protected String getOracleSql2(ObjectsCriteria cr) {

        String objectClassification;
        if(cr.getClassId()==null){
            objectClassification = "SELECT ID, CODE, DESCRIPTION FROM OBJECT_CLASSIFICATION START WITH PARENT_ID IS NULL CONNECT BY PRIOR ID=PARENT_ID ";
        }else{
            objectClassification = "SELECT ID, CODE, DESCRIPTION FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID ";
        }

        String locator;
        if(cr.getLocatorId()==null){
            locator = "SELECT * FROM LOCATOR START WITH PARENT_ID IS NULL CONNECT BY PRIOR ID=PARENT_ID ";
        }else{
            locator = "SELECT * FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID ";
        }
        String fields = "D.ID, D.CLASS_ID, D.OBJECT_REF, D.TAG, D.LOCATOR_ID,  D.ACTIVE, D.CUSTOM, " +
                "D.HAS_CUSTOM_DATA, D.HAS_CUSTOM_TASK, D.HAS_CUSTOM_TASK_GROUP, L.FULL_LOCATOR,OC.CODE,OC.DESCRIPTION ";
        String tableName = " OBJECT D, TASK T, TASK_GROUP TG, OBJECT_DATA OD, " +
            "(" + objectClassification + ") OC, (" + locator + ") L  ";
        String sql = "select " + fields + ", COUNT(DISTINCT T.ID) TASK_COUNT, COUNT(DISTINCT TG.ID) TASK_GROUP_COUNT, COUNT(DISTINCT OD.ID) OBJECT_DATA_COUNT from "  + tableName;
        StringBuilder where = new StringBuilder("WHERE D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL ");

        //do we need to show area objects?
        if("false".equals(Config.getProperty(Config.SHOW_AREA_OBJECTS,"true"))){
            where.append(" AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID));
            where.append(" AND D.OBJECT_DEF_ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID));
        }



        sql = sql + where + " AND T.OBJECT_ID(+)=D.ID AND TG.OBJECT_ID(+)=D.ID AND OD.OBJECT_ID(+)=D.ID  GROUP BY " + fields ;
        return sql;
    }*/

    protected String getSql4(ObjectsCriteria cr) {
        String locator, objectClassification;
        if(cr.getClassId()==null){
            objectClassification = "OBJECT_CLASSIFICATION";
        }else{
            objectClassification = "(SELECT ID, CODE, DESCRIPTION FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)";
        }
        
        //Acc to Chris we should not show data from child-locators
        //if(cr.getLocatorId()==null){
            locator = "LOCATOR";
        /*}else{
            locator = "(SELECT * FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID)";
        }*/

        String fields = "D.ID, D.OBJECT_REF, D.LOCATOR_ID ";
        String tableName = " OBJECT D, " + objectClassification + " OC, " + locator + " L  ";

        String where = " D.LOCATOR_ID=L.ID AND  D.CLASS_ID = OC.ID AND D.ARCHIVED_DATE IS NULL AND D.PARENT_OBJECT_ID is null ";
        where +=" AND LOCATOR_ID=?";
        
        
        String order = " order by D.OBJECT_REF ";
        String sql = "SELECT " + fields + " FROM "  + tableName + " WHERE " + where+ order;
        return sql;
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}
