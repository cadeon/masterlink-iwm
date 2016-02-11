package org.mlink.iwm.util;

/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: Mar 5, 2006
 */
public class UserTrackHelper {
    private static ThreadLocal user = new ThreadLocal();
    private static ThreadLocal selectedSchema = new ThreadLocal();
    private static String matrix = Config.getProperty(Config.PRODUCTION_SCHEMA);

    public static String getUser() {
        return(String) user.get();
    }

    public static String getSelectedSchema() {
        String schema = (String) selectedSchema.get();
        return schema==null?getProductionSchema():schema;
    }

    public static String getProductionSchema() {
        return matrix;
    }

    public static void setProductionSchema(String str) {
        matrix = str;
    } // set on application load via Connection Interceptor


    public static void setUser(String userId) {
        user.set(userId);
    }

    public static void setSelectedSchema(String schema) {
        selectedSchema.set(schema);
    }
}
