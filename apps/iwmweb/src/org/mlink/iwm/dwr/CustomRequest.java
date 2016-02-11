package org.mlink.iwm.dwr;

import org.mlink.iwm.util.DBAccess;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 27, 2007
 */
public class CustomRequest {
    private static final Logger logger = Logger.getLogger(CustomRequest.class);

    public  List<Map> execute(String sql) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        Connection conn = null;
        List<Map> vRslt = new ArrayList<Map>();
        try {
            conn = DBAccess.getDBConnection();
            stmt = conn.prepareStatement(sql);
            logger.debug(getClass().getName() + " SQL:"+sql);
            rSet = stmt.executeQuery();
            ResultSetMetaData metaData = rSet.getMetaData();
            int numCol = metaData.getColumnCount();
            while (rSet.next()) {
                // creates map with sufficient size and optimal load factor
                Map<String, Object> hTable = new HashMap<String, Object>(numCol + 1, 1);
                for (int c = 1; c <= numCol; c++) {
                    Object value = rSet.getObject(c);
                    if (value == null) continue;
                    hTable.put(metaData.getColumnName(c), value);
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
}
