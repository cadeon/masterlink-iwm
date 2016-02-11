package org.mlink.iwm.util;

import org.apache.log4j.Logger;
import org.mlink.iwm.base.SimpleListDAO;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;


public class Config {
	public final static String APPLICATION_DS            = "application.ds";
    public final static String J2EE_HOST            = "j2ee.host";
    public final static String CONTEXT_FACTORY      = "context.factory";
    public final static String URL_PREFIX           = "url.prefix";
    public final static String DATASOURCE           = "datasource";
    public static final String TWO_DIGIT_START_YEAR = "2digit.start.year";
    public static final String YEAR4D_DATE_PATTERN = "year4d.date.pattern";
    public static final String DATE_PATTERN = "date.pattern";
    public static final String TIME_PATTERN = "time.pattern";
    public static final String DAY_PATTERN = "day.pattern";
    public static final String SQL_TIME_PATTERN = "sql.time.pattern";
    public static final String SERVER_TIMEZONE = "server.timezone";
    public static final String REMOTE_TIMEZONE = "remote.timezone";
    public static final String REPORT_OUT_DELIMITER = "report.output.delimiter";
    public static final String PRINTLABEL_QRCODE_URL = "printlabel.qrcode.url";
    
    public static final String SHIFT_TIMEKEEPING="shift.timekeeping";
    public static final String TASK_TIMEKEEPING="task.timekeeping";
    public static final String BREAK_TIMEKEEPING="break.timekeeping";
    public static final String TEMPLATE_INVENTORYKEEPING="template.inventorykeeping";
    
    public static final String APP_INVENTORY_CONTROL="application.inventorycontrol";
    public static final String DISPLAY_ADDEDIT_SKILLS="display.addedit.skills";
    public static final String DISPLAY_ADDEDIT_SHIFTREFS="display.addedit.shiftrefs";
    public static final String DISPLAY_WORKERS_LOCATION="display.workers.location";
    
    public static final String SHOW_AREA_OBJECTS = "show.area.objects";
    public static final String USE_SERVLET_OUT_COMPRESSION = "use.servlet.out.compression";
    public static final String ROW_NUMBER_LIMIT_DISPLAY = "row.number.limit.display";
    //app props
    private static final Logger logger = Logger.getLogger(Config.class);

    private final static String PROPERTY_FILE  = "config/iwm.properties";
    private static Properties iwmProperties = new Properties();

    public final static String BUILD_PROPERTY_FILE  = "build-log.properties";
    private static Properties buildProperties = new Properties();


    public static final String AREA_OBJECT_DEFINITION_ID        = "area.object.definition.id";
    public static final String LOCATOR_SCHEMA_TOP               = "locator.schema.top";
    public static final String CLASSFICATION_SCHEMA_TOP               = "classification.schema.top";
    public static final String CURRENT_SCHEMA                   = "current.schema";
    public static final String PRODUCTION_SCHEMA                = "production.schema";
    public static final String EXT_REQUEST_OBJECT_DEFINITION_ID = "ext.request.object.definition.id";
    public static final String DEFECT_REPORT_OBJECT_DEFINITION_ID = "defect.report.object.definition.id";

    public static final String COPY_COMMAND   = "copy.command";
    public static final String DELETE_COMMAND = "delete.command";
    public static final String EXPORT_COMMAND = "export.command";
    public static final String IMPORT_COMMAND = "import.command";


    public static final String JAVA_AGENTS_ENABLED = "java.agents.enabled";
    public static final String JAVA_AGENTS_INTERVAL = "java.agents.interval";
    public static final String CREATE_WORK_SCHEDULES_ENABLED = "create.work.schedules.enabled";
    
    public static final String EARLISEST_ACTIVE_SCHEDULE_DATE_IS_TODAY = "earlist.active.schedule.date.is.today";

    public static final String UPLOADED_CONTENT_DIR="uploaded.content.dir";
    public static final String TAPS_WEBSERVICE_URL="taps.wsdl.url";


    static {
        //loadProperties();
    }


    public static Properties getProperties() {
        return iwmProperties;
    }

    public static String getFileStoreLocation(){
        return System.getProperty("jboss.server.base.dir") + iwmProperties.getProperty(Config.UPLOADED_CONTENT_DIR);
    }

    public static void setProperty(String key, String value){
        iwmProperties.setProperty(key,value);

    }

    public static Properties getBuildProperties() {
        return buildProperties;
    }

    /**
     * use this method if props location is set in java env
     * @return
     * @throws IOException
     */
    public static InputStream  getPropByLocation() throws IOException  {
        String fileName = System.getProperty(PROPERTY_FILE);
        return new FileInputStream(fileName);
    }

    /**
     * load application properties. Proprties loaded using environment variable or
     * using context class loader ( cms.properties must be in the classpath)
     */
    public static void loadProperties() {
        InputStream is;
        iwmProperties.clear();
        //1.Load application properties
        try {
            is = getPropertyResourceAsStream(PROPERTY_FILE);
            DataInputStream in = new DataInputStream(is);
            iwmProperties.load(in);
            logger.info(PROPERTY_FILE + " application properties loaded");

            List result = DBAccess.executeDAO(new SimpleListDAO("SELECT PROPERTY, VALUE FROM SYSTEM_PROPS", (List)null));
            for (Object aResult : result) {
                Map map = (Map) aResult;
                iwmProperties.put(map.get("PROPERTY"), map.get("VALUE"));
                System.getProperties().put(map.get("PROPERTY"), map.get("VALUE"));
            }

            logger.info(PROPERTY_FILE + " table system_props loaded");
            for (Object o : iwmProperties.keySet()) {
                String key =(String) o;
                logger.info(key + "=" + iwmProperties.get(key));
            }

        } catch (Exception e) {
            logger.error(PROPERTY_FILE + " application properties not loaded!");
        }

        //2.Load Build properties (such as jvm of the build, date and build number)
        try {
            is = getPropertyResourceAsStream(BUILD_PROPERTY_FILE);
            DataInputStream in = new DataInputStream(is);
            buildProperties.load(in);
            logger.info(BUILD_PROPERTY_FILE + " build application properties loaded");

            logger.info(BUILD_PROPERTY_FILE + " table system_props loaded");
            for (Object o : buildProperties.keySet()) {
                String key =(String) o;
                logger.info(key + "=" + buildProperties.get(key));
            }

        } catch (Exception e) {
            logger.error(BUILD_PROPERTY_FILE + " build application properties not loaded!");
        }

        try {
            String defaultSchema = DBAccess.getDefaultSchema();
            iwmProperties.put(PRODUCTION_SCHEMA, defaultSchema);
            logger.debug("Default Schema = " +defaultSchema);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    /**
     * get application property
     * @param key
     * @return  parameter value
     */
    public static String getProperty(String key){
        String prop =  iwmProperties.getProperty(key);
        if(prop == null)
            prop = System.getProperty(key);
        return prop;
    }

    /**
     * get application property
     * @param key
     * @param defaultValue
     * @return string
     */
    public static String getProperty(String key, String defaultValue){
        return iwmProperties.getProperty(key, defaultValue);
    }

    /**
     * property file is expected in classpath
     * @return String
     */
    private static InputStream getPropertyResourceAsStream() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is =  cl.getResourceAsStream(PROPERTY_FILE);
        if(is==null) { // or try this one
            is = Config.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        }
        return is;
    }

    public static InputStream getPropertyResourceAsStream(String fileName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is =  cl.getResourceAsStream(fileName);
        if(is==null) { // or try this one
            is = Config.class.getClassLoader().getResourceAsStream(fileName);
        }
        return is;
    }

    public static URL getPropertyResource(String fileName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url =  cl.getResource(fileName);
        if(url==null) { // or try this one
            url = Config.class.getClassLoader().getResource(fileName);
        }
        return url;
    }


       /*
        public byte[] getBytes(InputStream is) throws IOException {

        // Get the size of the file
        //long length = file.length();

        is.

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read the stream ");
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    } */

}

