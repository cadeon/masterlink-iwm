package org.mlink.iwm.dao;

import java.text.ParseException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import org.mlink.iwm.util.ConvertUtils;

/**
 * User: andrei
 * Date: Dec 21, 2006
 */
public class MWLocationsDAO extends ListDAOTemplate{
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("workerName","USER_NAME");
        nameToColumnMap.put("personId","PERSON_ID");
        nameToColumnMap.put("organizationId","ORGANIZATION_ID");
        nameToColumnMap.put("organizationName","ORGANIZATION_NAME");
        nameToColumnMap.put("firstPunch","FIRST_PUNCH");
        nameToColumnMap.put("lastPunch","LAST_PUNCH");
        nameToColumnMap.put("hours","HOURS");
        nameToColumnMap.put("locInfo","LOC_INFO");
    }

    /**
     * Get jobs for that work schedule
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
    	MWJobsCriteria cr = (MWJobsCriteria)criteria;
        String sql;
        if("org".equals(cr.getType())){
        	sql = getOrgSql(cr);
        }else{
        	sql = getSql(cr);
        }
        
        List parameters = new ArrayList();
        if(cr.getId()!=null)parameters.add(cr.getId());
        if(cr.getScheduledDate()!=null){
        	String dtStr = cr.getScheduledDate();
        	java.sql.Date dt=null;
			try {
				dt = ConvertUtils.string2Date(dtStr);
				parameters.add(ConvertUtils.formatDateShort(dt));
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        return process(parameters,sql,request);
    }

    protected String getSql(SearchCriteria cr) {
        StringBuffer sql = new StringBuffer("SELECT USER_NAME, PERSON_ID, ORGANIZATION_ID, ORGANIZATION_NAME, SCHEDULE, FIRST_PUNCH, LAST_PUNCH, HOURS, LOC_INFO " +
        		"from MW_ACCESS_VIEW4 P, ORGANIZATION O");// order by to_date(schedule, 'MM/DD/YYYY') desc, user_name ";
        sql.append(" WHERE O.ID=P.ORGANIZATION_ID");
        if( cr.getId() != null){
            //sql.append(" AND L.ORGANIZATION_ID = ?");
            sql.append(" AND P.ORGANIZATION_ID IN (SELECT ID FROM ORGANIZATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)");
        }
        sql.append(" AND SCHEDULE = ?");
        
        if(cr.getFilterText()!=null){
            String like = " AND (upper(USER_NAME) LIKE '%FTV%' OR upper(get_organization_hierarchy(O.ID)) LIKE '%FTV%')";
            sql.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }
        return sql.toString();
    }

    protected String getOrgSql(SearchCriteria cr) {
        StringBuffer sql = new StringBuffer("SELECT ORGANIZATION_ID, ORGANIZATION_NAME, LOC_INFO " +
        		"from MW_ORG_ACCESS_VIEW3 P, ORGANIZATION O");// order by to_date(schedule, 'MM/DD/YYYY') desc, user_name ";
        sql.append(" WHERE O.ID=P.ORGANIZATION_ID");
        if( cr.getId() != null){
            //sql.append(" AND L.ORGANIZATION_ID = ?");
            sql.append(" AND P.ORGANIZATION_ID IN (SELECT ID FROM ORGANIZATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)");
        }
        sql.append(" AND SCHEDULE = ?");
        
        if(cr.getFilterText()!=null){
            String like = " AND (upper(P.ORGANIZATION_NAME) LIKE '%FTV%' OR upper(get_organization_hierarchy(O.ID)) LIKE '%FTV%')";
            sql.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}