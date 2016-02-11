package org.mlink.iwm.dao;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class TemplatesDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("classId","CLASS_ID");
        nameToColumnMap.put("classCode","CODE");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("taskGroupCount","TASK_GROUP_COUNT");
        nameToColumnMap.put("dataCount","OBJECT_DATA_COUNT");
        nameToColumnMap.put("instanceCount","INSTANCE_COUNT");
        nameToColumnMap.put("presentInventory","INVENTORY");
    }

    /**
     * Get all templates (TreeOfTemplates)that recursively traverse to the given class
     * @param cr
     * @param request
     * @return
     * @throws SQLException
     */
    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        List parameters = new ArrayList();
        if(cr.getId()!=null)  parameters.add(cr.getId());
        return process(parameters,getSql(cr),request);
    }


    protected String getSql(SearchCriteria cr) {
        String classTable;
        if(cr.getId()==null){
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION  ";
        }else{
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID ";
        }

        String fields = "D.ID, D.CLASS_ID, C.CODE, D.INVENTORY, " +
                "(SELECT COUNT(T.ID) FROM TASK_DEF T WHERE D.ID=T.OBJECT_DEF_ID AND T.ARCHIVED_DATE IS NULL) TASK_COUNT, " +
                "(SELECT COUNT(T.ID) FROM TASK_GROUP_DEF T WHERE D.ID=T.OBJECT_DEF_ID) TASK_GROUP_COUNT, " +
                "(SELECT COUNT(T.ID) FROM OBJECT_DATA_DEF T WHERE D.ID=T.OBJECT_DEF_ID) OBJECT_DATA_COUNT, " +
                "(SELECT COUNT(T.ID) FROM OBJECT T WHERE D.ID=T.OBJECT_DEF_ID AND T.ARCHIVED_DATE IS NULL) INSTANCE_COUNT ";
        String tableName =  " OBJECT_DEF D,  ("+classTable + ") C  ";
        String where = "D.CLASS_ID=C.ID  AND D.ARCHIVED_DATE IS NULL";

        if(cr.getFilterText()!=null){
            String like = " AND (upper(C.CODE) LIKE '%FTV%' OR upper(C.DESCRIPTION) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }
        return "SELECT " + fields + " FROM "  + tableName + "  WHERE " + where;

    }
    protected String getSql2(SearchCriteria cr) {
        String classTable;
        if(cr.getId()==null){
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION  ";
        }else{
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID ";
        }


        String fields = "D.ID, D.CLASS_ID, D.INVENTORY, C.CODE, TASK_COUNT, TASK_GROUP_COUNT, OBJECT_DATA_COUNT, INSTANCE_COUNT";
        String tableName =  " OBJECT_DEF D,  ("+classTable + ") C  ";
        String where = "D.CLASS_ID=C.ID";
        String counters ="SELECT T1.ID, TASK_COUNT, TASK_GROUP_COUNT, OBJECT_DATA_COUNT, INSTANCE_COUNT FROM " +
                "(SELECT D.ID, COUNT(T.ID) TASK_COUNT FROM OBJECT_DEF D, TASK_DEF T WHERE D.ID=T.OBJECT_DEF_ID(+) GROUP BY D.ID) T1, " +
                "(SELECT D.ID, COUNT(T.ID) TASK_GROUP_COUNT FROM OBJECT_DEF D, TASK_GROUP_DEF T WHERE D.ID=T.OBJECT_DEF_ID(+) GROUP BY D.ID) T2, " +
                "(SELECT D.ID, COUNT(T.ID) OBJECT_DATA_COUNT FROM OBJECT_DEF D, OBJECT_DATA_DEF T WHERE D.ID=T.OBJECT_DEF_ID(+) GROUP BY D.ID) T3, " +
                "(SELECT D.ID, COUNT(T.ID) INSTANCE_COUNT FROM OBJECT_DEF D, OBJECT T WHERE D.ID=T.OBJECT_DEF_ID(+) GROUP BY D.ID) T4 " +
                "WHERE T1.ID=T2.ID AND T1.ID=T3.ID AND T1.ID=T4.ID";

        if(cr.getFilterText()!=null){
            String like = " AND (upper(C.CODE) LIKE '%FTV%' OR upper(C.DESCRIPTION) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }
        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE D.ID=CNT.ID AND " + where;

    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
