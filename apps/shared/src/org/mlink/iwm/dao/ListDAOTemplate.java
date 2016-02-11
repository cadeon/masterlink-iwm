package org.mlink.iwm.dao;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.exception.DAORequestException;
import org.mlink.iwm.exception.IWMException;
import org.apache.log4j.Logger;

import java.util.*;
import java.sql.*;

/**
 * User: andrei
 * Date: Nov 13, 2006
 */
public abstract class ListDAOTemplate {
    private static final Logger logger = Logger.getLogger(ListDAOTemplate.class);
    protected List<Map> getData(List params, String sql) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        List<Map> vRslt = new ArrayList<Map>();
        try {
            conn = DBAccess.getDBConnection();
            stmt = conn.prepareStatement(sql);
            logger.debug(getClass().getName() + " SQL:"+sql);
            logger.debug("Params:"+params);

            for (int i = 0; i < params.size(); i++) {
                Object o = params.get(i);
                if (o != null) stmt.setObject(i + 1, o);
            }
            rSet = stmt.executeQuery();
            ResultSetMetaData metaData = rSet.getMetaData();
            int numCol = metaData.getColumnCount();
            while (rSet.next()) {
                // creates map with sufficient size and optimal load factor
                Map<String, Object> hTable = new HashMap<String, Object>(numCol + 1, 1);

                for (int c = 1; c <= numCol; c++) {
                    Object value = rSet.getObject(c);
                    if (value == null) continue;
                    String propName = convertColumnToProperty(metaData.getColumnName(c));
                    hTable.put(propName, value);
                }
                vRslt.add(hTable);
            }

        } catch (SQLException e) {
            logger.error("SQL Failed " + sql);
            e.fillInStackTrace();
            throw e;
        }
        finally {
            DBAccess.close(rSet, stmt, conn);
        }

        return vRslt;
    }


    /**
     * Child class must provide Map between java property names and db column names
     * java names will be used for conversion to java objects
     * @return java to databse name mappings  key/value = javaPropName/DBColName
     */
    protected  Properties getPropertyToColumnMap(){
        throw new DAORequestException(getClass().getName() + " property map is missing!");
    }

    private String convertColumnToProperty(String columnName) {
        Properties props = getPropertyToColumnMap();
        String propName = null;
        for (Object o : props.keySet()) {
            String key =(String) o;
            String value = props.getProperty(key);
            if (value.equals(columnName)) {
                propName = key;
                break;
            }
        }
        if (propName == null) propName=columnName;
        return propName;
    }


    protected String convertPropertyToColumnName(String propertyName) {
        String colName = getPropertyToColumnMap().getProperty(propertyName);
        if (colName == null) colName = propertyName;
        return colName;
    }

    public DAOResponse getData(SearchCriteria cr) throws SQLException {
        throw new IWMException("getData(SearchCriteria) method is not implemented by " +this.getClass());
    }

    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException{
        throw new IWMException("getData(SearchCriteria,PaginationRequest) method is not implemented by " +this.getClass());
    }


    protected abstract String getSql(SearchCriteria cr);

    protected String prepareDataSql(String sql, PaginationRequest request) {
        StringBuilder wrappedSql = new StringBuilder();
        StringBuilder orderByClause = new StringBuilder();
        if(request.getOrderBy()!=null){
            String orderColumn = convertPropertyToColumnName(request.getOrderBy());
            switch(request.getOrderDirection()){
                case DESC:
                case NODIR:
                    orderByClause.append(" ORDER BY ");
                    orderByClause.append(orderColumn);
                    orderByClause.append(" ");
                    orderByClause.append(PaginationRequest.DirectionValues.get(PaginationRequest.Direction.DESC));
                    break;
                case ASC:
                    orderByClause.append(" ORDER BY ");
                    orderByClause.append(orderColumn);
                    orderByClause.append(" ");
                    orderByClause.append(PaginationRequest.DirectionValues.get(PaginationRequest.Direction.ASC));
                    break;
            }
        }

        if(PaginationRequest.PAGE_SIZE_UNLIMITED==request.getPageSize()){
            wrappedSql.append(sql + orderByClause);
        } else{
            int startRow = request.getOffset() + 1;
            int endRow = request.getOffset() + request.getPageSize();
            //wrap wrappedSql in pagination wrapper
            wrappedSql.append(" SELECT  outertable.*  FROM(");
            wrappedSql.append(     "SELECT ROWNUM outer, innertable.* FROM ( ");
            wrappedSql.append(         sql + orderByClause);
            wrappedSql.append(     ") innertable");
            wrappedSql.append(" ) outertable WHERE outertable.outer BETWEEN " + startRow + " AND " + endRow);
        }
        return wrappedSql.toString();
    }

    protected PaginationResponse process(List params, String sql, PaginationRequest request) throws SQLException {
        //int count2 = getCount(params, sql);
        String countSql = "SELECT COUNT(*) CNT FROM ("+ sql+ ")";
        Object cnt = getData(params,countSql).get(0).get("CNT");
        int count = Integer.parseInt(cnt.toString());
        List <Map> data = getData(params,prepareDataSql(sql,request));
        return new PaginationResponse(data,count,request.getOffset(),request.getPageSize());
    }

    protected DAOResponse process(List params, String sql) throws SQLException {
        List <Map> data = getData(params,sql);
        return new DAOResponse(data);
    }
}
