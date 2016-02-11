package org.mlink.iwm.dao;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class OrganizationsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("hierarchy","HIERARCHY");
        nameToColumnMap.put("type","DESCRIPTION");
        nameToColumnMap.put("phone","PHONE");
        nameToColumnMap.put("fax","FAX");
        nameToColumnMap.put("workerCount","PERSON_COUNT");
        nameToColumnMap.put("schemaId","SCHEMA_ID");
    }

    //this class is a Singleton
    /*private static OrganizationsDAO ourInstance = new OrganizationsDAO();
    public static OrganizationsDAO getInstance() {return ourInstance;}
    private OrganizationsDAO() {}*/

    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getId()!=null) parameters.add(cr.getId());
        return process(parameters,sql,request);
    }



    protected String getSql(SearchCriteria cr) {
        String table;
        switch(cr.getResultCategory()){
            //case ALL:
            //    table = "SELECT * FROM ORGANIZATION ";
            //   break;
            case CHILDREN:
                if(cr.getId()==null){
                    table = "SELECT * FROM ORGANIZATION ";
                }else{
                    table = "SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID ";  //all children of the organization
                }
                break;
            case FIRST_CHILDREN:
                if(cr.getId()==null){
                    table = "SELECT * FROM ORGANIZATION WHERE PARENT_ID IS NULL";
                }else{
                    table = "SELECT * FROM ORGANIZATION WHERE PARENT_ID=? "; //immediate (ie first) children of the organization
                }
                break;
            case PARENTS:
                if(cr.getId()==null){
                    table = "SELECT * FROM ORGANIZATION  WHERE ID=-1";
                }else{
                    table = "SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR PARENT_ID=ID";  //all parents of the organization including itself
                }
                break;
            default:
                table = "SELECT * FROM ORGANIZATION WHERE PARENT_ID IS NULL ";   //top level organizations only
        }

        String fields = "L.ID, P.NAME,get_organization_hierarchy(L.ID) HIERARCHY, OTR.DESCRIPTION, P.PHONE, P.FAX, nvl(PERSON_COUNT,0) PERSON_COUNT,L.SCHEMA_ID ";
        String tableName = "("+table+") L, PARTY P, ADDRESS A, ORGANIZATION_TYPE_REF OTR, SCHEMA_REF SR";

        String where = " L.ORGANIZATION_TYPE_ID=OTR.ID AND L.PARTY_ID = P.ID AND P.ADDRESS_ID=A.ID AND L.ARCHIVED_DATE IS NULL AND SR.ID=L.SCHEMA_ID ";
        String counters ="SELECT PERSON.ORGANIZATION_ID, COUNT(*) PERSON_COUNT FROM PERSON WHERE ACTIVE=1 AND PERSON.ARCHIVED_DATE IS NULL GROUP BY PERSON.ORGANIZATION_ID";

        if(cr.getFilterText()!=null){
            String like = " AND (upper(P.NAME) LIKE '%FTV%' OR upper(get_organization_hierarchy(L.ID)) LIKE '%FTV%')";
            where += like.replaceAll("FTV",cr.getFilterText().toUpperCase());
        }
        return "SELECT " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE CNT.ORGANIZATION_ID(+)=L.ID AND" + where;

    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}
