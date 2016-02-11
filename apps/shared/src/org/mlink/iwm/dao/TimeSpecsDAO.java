package org.mlink.iwm.dao;

import org.apache.commons.lang.time.DateUtils;
import org.mlink.iwm.util.ConvertUtils;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;

/**
 * User: andrei
 * Date: Nov 15, 2006
 */
public class TimeSpecsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("fullLocator","ADDRESS");
        nameToColumnMap.put("day","DAY");
        nameToColumnMap.put("shiftId","SHIFT_ID");
        nameToColumnMap.put("shiftTime","TIME");
        nameToColumnMap.put("name","NAME");
        nameToColumnMap.put("jobCount","JOB_COUNT");
        nameToColumnMap.put("status","CODE");
        nameToColumnMap.put("statusDesc","DESCRIPTION");
        nameToColumnMap.put("hierarchy","HIERARCHY");
        nameToColumnMap.put("personId","PERSON_ID");
    }

    /**
     * Get workers
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        TimeSpecsCriteria cr = (TimeSpecsCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr.getId()!=null)parameters.add(cr.getId());
        if(cr.getOrganizationId()!=null)parameters.add(cr.getOrganizationId());
        if(cr.getLocatorId()!=null)parameters.add(cr.getLocatorId());
        if(cr.getShiftId()!=null)parameters.add(cr.getShiftId());

        if(cr.getDateRange()!=null){
            Date today  = new Date(System.currentTimeMillis());
            Date arg1=today,arg2=today;
            switch(cr.getDATERANGE()){
                case NextWeek:
                    arg1= today; arg2=DateUtils.addDays(today,7);
                    break;
                case NextMonth:
                    arg1= today; arg2=DateUtils.addMonths(today,1);
                    break;
                case Next3Months:
                    arg1= today; arg2=DateUtils.addMonths(today,3);
                    break;
                case LastWeek:
                    arg2= today; arg1=DateUtils.addWeeks(today,-1);
                    break;
                case LastMonth:
                    arg2= today; arg1=DateUtils.addMonths(today,-1);
                    break;
                case Last3Months:
                    arg2= today; arg1=DateUtils.addMonths(today,-3);
                    break;
                case ExactDate:
                    try {
                        arg1 = ConvertUtils.string2Date(cr.getDate());  arg2=arg1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            if(cr.getDATERANGE()!=TimeSpecsCriteria.DATERANGE.All){
                parameters.add(new java.sql.Date(arg1.getTime()));
                parameters.add(new java.sql.Date(arg2.getTime()));
            }

        }
        return process(parameters,sql,request);
    }


    protected String getSql(SearchCriteria criteria) {
        TimeSpecsCriteria cr = (TimeSpecsCriteria)criteria;
        StringBuilder sql = new StringBuilder();

        String fields = "W.ID, L.ADDRESS, W.DAY, W.TIME,  W.SHIFT_ID,  PRT.NAME, WSR.DESCRIPTION, WSR.CODE, get_organization_hierarchy(P.ORGANIZATION_ID) HIERARCHY, W.PERSON_ID ";
        //String select = "SELECT " + fields + ", nvl(COUNT(JS.ID),0) JOB_COUNT";
        String select = "SELECT " + fields + ", (SELECT COUNT(*) FROM JOB_SCHEDULE JS WHERE JS.WORK_SCHEDULE_ID=W.ID AND JS.DELETED_TIME IS NULL) JOB_COUNT";
        String tableName = " FROM WORK_SCHEDULE W, PERSON P, LOCATOR L, PARTY PRT, WORK_SCHEDULE_STATUS_REF WSR ";

        StringBuilder where = new StringBuilder(" WHERE W.ARCHIVED_DATE IS NULL  AND P.ID=W.PERSON_ID AND L.ID=W.LOCATOR_ID AND PRT.ID=P.PARTY_ID " +
                " AND W.STATUS_ID=WSR.ID  ");


        if(cr.getId()!=null){
            where.append(" AND W.PERSON_ID=? ");
        }
        if( cr.getOrganizationId() != null){
            //where.append(" AND P.ORGANIZATION_ID = ?");
            where.append(" AND P.ORGANIZATION_ID IN (SELECT ID FROM ORGANIZATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID)");
        }
        if(cr.getLocatorId()!=null){
            where.append(" AND W.LOCATOR_ID=? ");
        }
        if(cr.getShiftId()!=null){
            where.append(" AND W.SHIFT_ID=? ");
        }
        switch(cr.getDATERANGE()){
            case All:
                break;
            default:
                where.append(" AND W.DAY BETWEEN ? AND ? ");
        }


        if(cr.getFilterText()!=null){
            String like = " AND (upper(PRT.NAME) LIKE '%FTV%' OR upper(L.ADDRESS) LIKE '%FTV%' OR upper(get_organization_hierarchy(P.ORGANIZATION_ID)) LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }

        sql.append(select).append(tableName).append(where);//.append(" GROUP BY ").append(fields);

        return sql.toString();
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }

}

