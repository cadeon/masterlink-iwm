package org.mlink.iwm.dao;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class LocatorsDAO extends ListDAOTemplate {
    public enum ResultCategory{TOP_LEVEL,ALL,ALL_CHILDREN,FIRST_CHILDREN,ALL_PARENTS}
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("schemaId","SCHEMA_ID");
        nameToColumnMap.put("schema","DESCRIPTION");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("locatorId","ID");
        nameToColumnMap.put("securityLevel","SECURITY_LEVEL");
        nameToColumnMap.put("parentId","PARENT_ID");
        nameToColumnMap.put("address","ADDRESS");
        nameToColumnMap.put("abbr","ABBR");
        nameToColumnMap.put("inServiceDate","INSERVICE_DATE");
        nameToColumnMap.put("locatorDataCount","LOCATOR_DATA_COUNT");
    }

    //this class is a Singleton
    /*private static LocatorsDAO ourInstance = new LocatorsDAO();
    public static LocatorsDAO getInstance() {return ourInstance;}
    private LocatorsDAO() {}*/


    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getId()!=null) parameters.add(cr.getId());
        return process(parameters,sql,request);
    }



    protected String getSql(SearchCriteria cr) {
        String table;
        switch(cr.getResultCategory()){
            case CHILDREN:
                if(cr.getId()==null){
                    table = "SELECT * FROM LOCATOR ";
                }else{
                    table = "SELECT * FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID ";  //all children of the locator
                }
                break;
            case FIRST_CHILDREN:
                if(cr.getId()==null){
                    table = "SELECT * FROM LOCATOR WHERE PARENT_ID IS NULL ";   //top level locators only
                }else{
                    table = "SELECT * FROM LOCATOR WHERE PARENT_ID=? "; //immediate (ie first) children of the locator
                }
                break;
            case PARENTS:
                if(cr.getId()==null){
                    table = "SELECT * FROM LOCATOR WHERE ID = -1";
                }else{
                    table = "SELECT * FROM LOCATOR START WITH ID=? CONNECT BY PRIOR PARENT_ID=ID";  //all parents of the locator including itself
                }
                break;
            default:
                table = "SELECT * FROM LOCATOR WHERE PARENT_ID IS NULL ";   //top level locators only
        }

        String fields = "L.NAME, L.SCHEMA_ID, SR.DESCRIPTION, L.FULL_LOCATOR,L.ID, L.SECURITY_LEVEL, L.PARENT_ID, " +
                "L.ADDRESS, L.ABBR,  L.INSERVICE_DATE " +
                ",(SELECT COUNT(LD.ID) FROM LOCATOR_DATA LD WHERE L.ID=LD.LOCATOR_ID) LOCATOR_DATA_COUNT";

        //String where = " WHERE L.ID=LD.LOCATOR_ID(+) AND SR.ID=L.SCHEMA_ID ";
        String where = " WHERE SR.ID=L.SCHEMA_ID ";
        if(cr.getFilterText()!=null){
            String like = " AND (upper(L.NAME) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' OR  upper(SR.DESCRIPTION) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }

        /*String sql =   " SELECT " + fields + ", count(nvl(LD.ID,0)) LOCATOR_DATA_COUNT " +
                " FROM (" + table + ") L , LOCATOR_DATA LD, SCHEMA_REF SR  " + where +
                " GROUP BY " + fields ; */

        String sql =   " SELECT " + fields + " FROM (" + table + ") L , SCHEMA_REF SR  " + where;
        return sql;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
