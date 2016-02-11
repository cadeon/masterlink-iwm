package org.mlink.iwm.util;


import java.io.Serializable;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;
import org.apache.log4j.Logger;

/**
 * used OracleConnectionChecker.java as example (C:\jboss-4.0.2-src\connector\src\main\org\jboss\resource\adapter\jdbc\vendor)
 * This class is called every time connection is requested from the pool.
 * Has to be declared by <valid-connection-checker-class-name> (oracle_ds.xml)
 * Example:
 <local-tx-datasource>
 <jndi-name>iwm_x</jndi-name>
 <connection-url>jdbc:oracle:thin:@localhost:1521:xe</connection-url>
 <driver-class>oracle.jdbc.driver.OracleDriver</driver-class>
 <user-name>test_u</user-name><password>test_u</password>
 <valid-connection-checker-class-name>org.mlink.iwm.util.ConnectionInterceptor</valid-connection-checker-class-name>
 <metadata>
 <type-mapping>Oracle9i</type-mapping>
 </metadata>
 </local-tx-datasource>*
 * The sole purpose is to support on-the-fly change of schema to whic IWM app is attached to
 * by executing  alter session set current_schema=
 */
public class ConnectionInterceptor implements ValidConnectionChecker, Serializable{
    private static final long serialVersionUID = -2227528634302168877L;
    private static final Logger log = Logger.getLogger(ConnectionInterceptor.class);
    private static final String alterSchemaSql="ALTER SESSION SET CURRENT_SCHEMA=?";
    private Map history = new HashMap();  /* keeps track of connections and their schemas */

    public static void logWarnings(SQLWarning warning) {
        for(; warning != null; warning = warning.getNextWarning()) {
            StringBuffer buf = (new StringBuffer(30)).append("SQL Warning: ").append(warning.getErrorCode()).append(", SQLState: ").append(warning.getSQLState());
            log.debug(buf.toString());
            log.debug(warning.getMessage());
        }

    }

    public synchronized SQLException isValidConnection(Connection c)    {
        try {
            if(c.getWarnings()!=null) {
                log.debug("Got Connection with Warnings! Connection id" + System.identityHashCode(c));
                logWarnings(c.getWarnings());
                c.clearWarnings();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if(UserTrackHelper.getProductionSchema()==null){
                UserTrackHelper.setProductionSchema(c.getMetaData().getUserName());
            }

            String schema = UserTrackHelper.getSelectedSchema();

            //log.debug("checking connection " + System.identityHashCode(c) + " requested schema " + schema);
            String lastSchemaForThisConnection =(String)history.get(String.valueOf(System.identityHashCode(c)));
            if(schema.equals(lastSchemaForThisConnection)){
                // no need to execute ALTER SESSION sinnce schema is not changed
            }else{
                // at this point ALTER SESSION for this connection is required
                alterSchema(c,schema);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void alterSchema(Connection c, String schema){
        PreparedStatement st=null;
        try{
            history.put(String.valueOf(System.identityHashCode(c)), schema);
            String sql = "ALTER SESSION SET CURRENT_SCHEMA="+schema;
            log.debug("executing " + sql + " for connection " + System.identityHashCode(c));
            st = c.prepareStatement(alterSchemaSql);
            st.setString(1,schema);
            st.execute(sql);
        } catch (SQLException e) {
            //its pretty bad here, no recovery strategy for now
            e.printStackTrace();
        } finally{
            try {
                if(st!=null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

