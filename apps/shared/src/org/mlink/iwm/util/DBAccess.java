package org.mlink.iwm.util;

import org.mlink.iwm.base.BaseListDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;



/**
 * Class used to access the Oracle database via JDBC.
 * All JDBC traffic goes through this class.
 */
public class DBAccess
{
    public static final String DS_NAME = "ds_test";

    private static final Logger logger = Logger.getLogger(DBAccess.class);

    /**
     * Obtain a connection to the database using the given datasource.
     * Note: if the datasource system property is NULL, the default datasource as specified
     * in DEFAULT_DS will be used.
     * @return the database connection.
     * @throws SQLException occured.
     */
    public static Connection getDBConnection(String strDataSource) throws SQLException  {
        Connection conn = null;
        Context ctx;
        try      {
            ctx = EnvUtils.getInitialContext();
            DataSource ds = (DataSource) ctx.lookup(strDataSource);
            conn = ds.getConnection();
        }
        catch (NamingException e)      {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getDBConnection() throws SQLException  {
        Connection conn;
        Context ctx;
        String strDataSource = System.getProperty(DBAccess.DS_NAME, Config.getProperty(Config.APPLICATION_DS));
        try      {
            ctx = EnvUtils.getInitialContext();

            DataSource ds = (DataSource) ctx.lookup(strDataSource);
            conn = ds.getConnection();
        }
        catch (Exception e)      {
            System.out.println("WARNING DBConnection to "+strDataSource+
                    " failed :"+e+"\n"+"TRying getCon();");
            //     e.printStackTrace();
            try{conn = getCon();}catch(Exception ex) {
                throw new SQLException(ex.toString());
            }
        }
        return conn;
    }

    public static Connection getCon() throws Exception {
        String url = System.getProperty("database.url",
                "jdbc:oracle:thin:@siren.masterlinkcorp.com:1521:MLINKDB");
        String user = System.getProperty("database.userid",
                "mlink_blake");
        String pass = System.getProperty("database.password",
                "blake_mlink");
        Class.forName(System.getProperty("database.driver",
                "oracle.jdbc.driver.OracleDriver"));
        return DriverManager.getConnection(url,user,pass);

    }

    public static String getDefaultSchema()throws SQLException{
        Connection con = getDBConnection();
        String str =  con.getMetaData().getUserName();
        close(con);
        return str;
    }
    /**
     * Closes resultset and statement.
     * @param rSet the result set to be closed.
     * @param stmt the statement to be closed.
     */
    protected static void close(ResultSet rSet, Statement stmt)    {
        if ( rSet !=null )        {
            try            {
                rSet.close();
            }
            catch (SQLException e)            {
                e.printStackTrace();
            }
        }
        if ( stmt !=null )        {
            try            {
                stmt.close();
            }
            catch (SQLException e)            {
                e.printStackTrace();
            }
        }
    }


    /**
     * Closes resultset, statement and connection.
     * @param rSet the result set to be closed.
     * @param stmt the statement to be closed.
     * @param conn the connection to be closed.
     */
    public static void close(ResultSet rSet, Statement stmt, Connection conn)    {
        if ( rSet !=null )      {
            try         {
                rSet.close();
            }
            catch (SQLException e)         {
                e.printStackTrace();
            }
        }
        if ( stmt !=null )      {
            try         {
                stmt.close();
            }
            catch (SQLException e)         {
                e.printStackTrace();
            }
        }
        if ( conn != null )      {
            try         {
                conn.close();
            }
            catch ( SQLException e )         {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List executeDAO(BaseListDAO dao)throws SQLException{
        String sql = dao.getSql();
        Statement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        List vRslt = new ArrayList();

        logger.debug("sql=" + sql);
        logger.debug("params=" + dao.getParameters());

        try{
            conn = getDBConnection();
            if(dao.getParameters()==null){
                stmt = conn.createStatement( );
                rSet = stmt.executeQuery( sql );
            }else{
                stmt = conn.prepareStatement(sql);
                List params = dao.getParameters();
                for (int i = 0; i < params.size(); i++) {
                    Object o = params.get(i);
                    if(o==null) continue;

                    if(o instanceof java.sql.Date)
                        ((PreparedStatement)stmt).setDate(i+1,(java.sql.Date)o);
                    else if(o instanceof java.util.Date){
                        java.sql.Date date = new java.sql.Date(((java.util.Date)o).getTime());
                        ((PreparedStatement)stmt).setDate(i+1,date);
                    }
                    else
                        ((PreparedStatement)stmt).setObject(i+1,o);
                }
                rSet = ((PreparedStatement)stmt).executeQuery();
            }
            vRslt = dao.prepareResult(rSet);
        }catch (SQLException e)      {
            e.printStackTrace();
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, stmt, conn);
        }
        return vRslt ;
    }

    public static List executePreparedDAO(BaseListDAO dao)throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        List vRslt = new ArrayList();

        try{
            conn = getDBConnection();
            stmt = conn.prepareStatement(dao.getSql());
            List params = dao.getParameters();
            for (int i = 0; i < params.size(); i++) {
                Object o =  params.get(i);
                if(o!=null) stmt.setObject(i+1,o);
            }
            rSet = stmt.executeQuery();
            vRslt = dao.prepareResult(rSet);
        }catch (SQLException e)      {
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, stmt, conn);
        }
        return vRslt ;
    }

    /**
     * executes sql update statement
     * @throws SQLException
     */
    static public int executeUpdate(String sql )   throws SQLException{
        logger.debug("sql=" + sql);
        Statement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        int rtn;
        try{
            conn = getDBConnection();
            stmt = conn.createStatement( ) ;
            rtn = stmt.executeUpdate(sql );
        }
        catch (SQLException e)      {
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, stmt, conn);
        }
        return rtn;
    }

    static public int executeParameterizedUpdate(String query, List params) throws SQLException {
    	logger.debug("sql="+query);	
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        int rtn;
        try {

            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                Object o = params.get(i);
                if (o != null) stmt.setObject(i + 1, o);
            }
            rtn = stmt.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
            throw e;
        }
        finally {
            close(rSet, stmt, conn);
        }
        return rtn;
    }
    static public List <Map> execute(String query)   throws SQLException{
        return execute(query, false);
    }



    static public List <Map> executeQuery(String query, List params) throws SQLException {
        return executeQuery(query,params,false);
    }

    /**
     * executes sql statement
     * @param query
     * @return  List of Maps with keys as column names and values as column values
     * @throws SQLException
     */
    static public List <Map> execute(String query , boolean dataTypeConversion)   throws SQLException{
        if ( query == null ) return null ;
        List<Map> vRslt = new ArrayList<Map>();
        Statement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        try{
            conn = getDBConnection();
            stmt = conn.createStatement( ) ;
            rSet = stmt.executeQuery( query );
            ResultSetMetaData metaData = rSet.getMetaData();
            int numCol = metaData.getColumnCount();
            while ( rSet.next() ){
                // creates Hashtable with sufficient size and optimal load factor
                Map hTable = new HashMap( numCol+1, 1 ) ;
                for ( int c=1; c <=numCol; c++ )            {
                    Object value;
                    if(dataTypeConversion)
                        value = getDbObject(rSet, c);
                    else
                        value = rSet.getObject(c);

                    if ( value == null ) continue;
                    hTable.put( metaData.getColumnName(c), value ) ;
                }

                vRslt.add(hTable) ;
            }
        }
        catch (SQLException e)      {
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, stmt, conn);
        }
        return vRslt ;
    }

    static public List <Map> executeQuery(String query, List params, boolean dataTypeConversion) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        List<Map> vRslt = new ArrayList<Map>();

        try {

            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
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
                    Object value;
                    if (dataTypeConversion)
                        value = DBAccess.getDbObject(rSet, c);
                    else
                        value = rSet.getObject(c);

                    if (value == null) continue;
                    hTable.put(metaData.getColumnName(c), value);
                }
                vRslt.add(hTable);
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
            throw e;
        }
        finally {
            close(rSet, stmt, conn);
        }

        return vRslt;
    }

    static public void executeProcedure(String strStatement,List params)   throws SQLException{
        if ( strStatement == null ) return ;

        CallableStatement cstmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        try{
            conn = getDBConnection();
            cstmt = conn.prepareCall(strStatement);
            // Bind the values.
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    Object objValue = params.get(i);
                    if (objValue instanceof Date) {
                        Date dTmp = (Date) objValue;
                        objValue = new Timestamp(dTmp.getTime());
                    }
                    cstmt.setObject(i + 1, objValue);
                }
            }
            // Execute.
            cstmt.executeUpdate();
        }
        catch (SQLException e)      {
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, cstmt, conn);
        }
    }

    static public Object executeFunction(String funcName,Object param)   throws SQLException{
        List params = new ArrayList();
        if(param!=null) params.add(param);
        return executeFunction(funcName,params);
    }
    static public Object executeFunction(String funcName,List params)   throws SQLException{
        String tmp="",separator="";
        for (Object param : params) {
            tmp = tmp + separator + param;
            separator = ",";
        }
        List <Map> rtn = execute("select " + funcName + "(" + tmp +  ") RTNVALUE from dual");
        return rtn.get(0).get("RTNVALUE");
    }

    /**
     * Smart database result set retrival cell method. Uses the metadata to figure
     * out the type of column and creates a Java object from the given result set.
     * @param rSet the actual result set.
     * @param nCol the current column index.
     * @return the Java object created for the database cell.
     * @throws SQLException if the information could not be obtained.
     */
    public static Object getDbObject(ResultSet rSet, int nCol)  throws SQLException{
        ResultSetMetaData metaData = rSet.getMetaData();
        try{
            switch (metaData.getColumnType(nCol)) {
                case Types.BIGINT:
                case Types.BINARY:
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.REAL:
                case Types.NUMERIC:
                case Types.INTEGER:
                case Types.SMALLINT:
                case Types.TINYINT:
                    // we need to make a call to get a value here so the wasNull call will
                    // work on the current column
                    double d = rSet.getDouble(nCol);
                    if (rSet.wasNull())               {
                        return null;
                    }
                    long   l = rSet.getLong(nCol);
                    if (l != d  || metaData.getScale(nCol) > 0)
                    {
                        return d;
                    }
                    else if (metaData.getPrecision(nCol) == 1) {
                        return(l == 1 ? Boolean.TRUE : Boolean.FALSE);
                    }
                    return l;


                case Types.DATE:
                case Types.TIMESTAMP:
                    return rSet.getTimestamp(nCol);

                case Types.LONGVARCHAR:
                    return rSet.getLong(nCol);

                case Types.BIT:
                case Types.CHAR:
                case Types.VARCHAR:
                    return rSet.getString(nCol);

                case Types.LONGVARBINARY:
                case Types.NULL:
                case Types.OTHER:
                case Types.TIME:
                case Types.VARBINARY:
                default:
                    return rSet.getObject(nCol);
            }
        }
        catch (SQLException se)
        {
            String strMessage = "SQL Exception getting data: " + se.getMessage() + "\r\n" +
                    "\tColumn Name" + metaData.getColumnName(nCol) + "\r\n" +
                    "\tColumn Number: "   + nCol + "\r\n" +
                    "\tColumn Label: "    + metaData.getColumnLabel(nCol) + "\r\n" +
                    "\tColumn TypeName: " + metaData.getColumnTypeName(nCol) + "\r\n" +
                    "\tColumn Type: "     + metaData.getColumnType(nCol);
            // Update the stack trace.
            System.out.print(strMessage);
            se.fillInStackTrace();
            throw se;
        }
    }



    /**
     * get schema available for current schema owner
     * @return List
     */
    public static List getSchemas(){
        List vRslt = new ArrayList();
        ResultSet rSet = null;
        Connection conn = null;
        try{
            conn = getDBConnection();
            rSet = conn.getMetaData().getSchemas();
            while ( rSet.next() ){
                vRslt.add(rSet.getString(1)) ;
            }
        }
        catch (SQLException e)      {
            e.printStackTrace();
        }
        finally      {
            close(rSet, null, conn);
        }
        return vRslt ;
    }

    public static String getSchemaOwner(){
        String user=null;
        Connection conn = null;
        try{
            conn = getDBConnection();
            user = conn.getMetaData().getUserName();
        }
        catch (SQLException e)      {
            e.printStackTrace();
        }
        finally      {
            close(null, null, conn);
        }
        return user ;
    }

    public static Long generateID(String pSequenceName, Connection conn ) throws SQLException   {
        String strSQL = "SELECT " + pSequenceName + ".NEXTVAL FROM ALL_SEQUENCES WHERE SEQUENCE_NAME='" + pSequenceName + "'";
        //Connection conn = getDBConnection();
        Statement stmt = null;
        ResultSet rSet = null;
        Long id;
        try {
            stmt = conn.createStatement( ) ;
            rSet = stmt.executeQuery( strSQL );
            rSet.next();
            id =  rSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            e.fillInStackTrace();
            throw e;
        }
        finally      {
            close(rSet, stmt, null);
        }
        return id;
    }
}
