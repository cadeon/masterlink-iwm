package org.mlink.iwm.base;

import org.mlink.iwm.base.BaseListDAO;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

/**
 * User: andrei
 * Date: Feb 21, 2005
 * This class executes query and returns List of rows as string of comadelimeted column name/value pairs
 * created to support DeletionVistor
 */
public class SimpleListDAO extends  BaseListDAO{
    private String query;


    public SimpleListDAO(String query, List parameters) {
        this.query=query;
        setParameters(parameters);
    }

    public SimpleListDAO(String query, Object parameter1) {
        this.query=query;
        List params = new ArrayList();
        params.add(parameter1);
        setParameters(params);
    }

    public void setParameters(List parameters) {
        this.parameters=parameters;
    }
    public String getSql() {
        return query;
    }

    /*public List prepareResult(ResultSet rSet) throws SQLException {
        List list = new ArrayList();
        StringBuffer sb;
        ResultSetMetaData metaData = rSet.getMetaData();
        int numCol = metaData.getColumnCount();
        while(rSet.next()) {
            sb=new StringBuffer();
            for ( int c=1; c <=numCol; c++ )            {
                Object value = rSet.getObject(c);
                if ( value == null ) continue;
                sb.append(" " + metaData.getColumnName(c) + "="+ value ) ;
            }
            list.add(sb.toString()) ;
        }
        return list;
    }*/

    public List prepareResult(ResultSet rSet) throws SQLException {
        List list = new ArrayList();
        Map map;
        ResultSetMetaData metaData = rSet.getMetaData();
        int numCol = metaData.getColumnCount();
        while(rSet.next()) {
            map=new HashMap();
            for ( int c=1; c <=numCol; c++ )            {
                Object value = rSet.getObject(c);
                if ( value == null ) continue;
                map.put(metaData.getColumnName(c),value) ;
            }
            list.add(map) ;
        }
        return list;
    }


}
