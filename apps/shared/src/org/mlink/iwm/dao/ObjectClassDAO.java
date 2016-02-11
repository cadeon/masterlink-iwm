package org.mlink.iwm.dao;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 17, 2006
 */
public class ObjectClassDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("value","ID");
        nameToColumnMap.put("label","LABEL");
        nameToColumnMap.put("code","CODE");
        nameToColumnMap.put("schemaId","SCHEMA_ID");

    }

    //this class is a Singleton
    /*private static ObjectClassDAO ourInstance = new ObjectClassDAO();
    public static ObjectClassDAO getInstance() {return ourInstance;}
    private ObjectClassDAO() {}*/

    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getId()!=null) parameters.add(cr.getId());
        return process(parameters,sql,request);
    }

    /*public DAOResponse getFirstChildrenClasses(Long classId) throws SQLException {
        //String sql = getSql(ResultCategory.FIRST_CHILDREN);
        String sql = getSql(new FilterTreeSearchCriteria(classId,FilterTreeSearchCriteria.ResultCategory.FIRST_CHILDREN));
        sql += " ORDER BY CODE ASC";
        if(classId==null)
            return getTopLevelClasses();
        else{
            List parameters = new ArrayList();
            parameters.add(classId);
            return process(parameters,sql);
        }
    }*/

    /*public DAOResponse getParentClasses(Long firstParentId) throws SQLException {
        //String sql = getSql(ResultCategory.ALL_PARENTS);
        String sql = getSql(new FilterTreeSearchCriteria(firstParentId,FilterTreeSearchCriteria.ResultCategory.ALL_PARENTS));

        sql += " ORDER BY SCHEMA_ID ASC";
        List parameters = new ArrayList();
        if(firstParentId==null){
            return new PaginationResponse();  //nothing to return
        }else{
            parameters.add(firstParentId);
            return process(parameters,sql);
        }
    }*/

    /*private DAOResponse getTopLevelClasses() throws SQLException {
        //String sql = getSql(ResultCategory.TOP_LEVEL);
        String sql = getSql(new FilterTreeSearchCriteria(null,
                FilterTreeSearchCriteria.ResultCategory.TOP_LEVEL));

        sql += " ORDER BY CODE ASC";
        return process(new ArrayList(),sql);
    }*/

    /*protected String getSql(FilterTreeSearchCriteria cr){
        String rtn;
        switch(currentDatabaseType){
            case SQLSERVER:
                rtn =  null;
                break;
            case ORACLE:
            default:
                rtn =  getOracleSql(cr);
        }
        return rtn;
    }*/


    protected String getSql(SearchCriteria cr) {
        String table;
        switch(cr.getResultCategory()){
            case FIRST_CHILDREN:
                if(cr.getId()==null){
                    table = "OBJECT_CLASSIFICATION WHERE PARENT_ID IS NULL ";   //top level classes only
                }else{
                    table = "OBJECT_CLASSIFICATION WHERE PARENT_ID=? "; //immediate (ie first) children of the class
                }
                break;
            case PARENTS:
                if(cr.getId()==null){
                    table = "OBJECT_CLASSIFICATION WHERE ID=-1";  //no parents for parentId=null
                }else{
                    table = "OBJECT_CLASSIFICATION START WITH ID=? CONNECT BY PRIOR PARENT_ID=ID";  //all parents of the class including itself
                }
                break;
            default:
                table = "OBJECT_CLASSIFICATION WHERE PARENT_ID IS NULL ";   //top level classes only
        }

        String fields = "ID, '['  || CODE || '] ' || DESCRIPTION LABEL, SCHEMA_ID, CODE ";
        return   " SELECT " + fields  + " FROM " + table;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

