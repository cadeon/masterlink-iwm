package org.mlink.iwm.dao;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.Config;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Jan 9, 2007
 */
public class TemplatesForObjectCreateDAO extends ListDAOTemplate {
    private static final Logger logger = Logger.getLogger(ListDAOTemplate.class);
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("classId","CLASS_ID");
        nameToColumnMap.put("classCode","CODE");
    }

    /**
     * Get WORKS SCHEDULES THAT HAD THAT JOB
     * @return
     * @throws java.sql.SQLException
     */
    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        List parameters = new ArrayList();
        if(cr.getId()!=null)  parameters.add(cr.getId());
        return process(parameters,getSql(cr));
    }

    protected String getSql(SearchCriteria cr) {
        String classTable;
        if(cr.getId()==null){
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION  ";
        }else{
            classTable = "SELECT * FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID ";
        }


        String fields = "D.ID, D.CLASS_ID, C.CODE";
        String tableName =  " OBJECT_DEF D,  ("+classTable + ") C  ";
        String where = " EXISTS (SELECT 1 FROM TASK_DEF T WHERE D.ID=T.OBJECT_DEF_ID) AND D.CLASS_ID=C.ID ";             // EXIST CLAUSE IS TO INSURE THAT TEMPLATE HAS AT LEAST ONE TASK, THEN IKT IS ELIGIBLE
        where += " AND D.ID <> " + Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID);
        where += " AND D.ID <> " + Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID);

        return "SELECT " + fields + " FROM "  + tableName + " WHERE "+ where;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
