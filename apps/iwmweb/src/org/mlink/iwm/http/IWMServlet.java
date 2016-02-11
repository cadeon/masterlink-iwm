package org.mlink.iwm.http;


import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.mlink.agent.ProductionWorldConnection;
import org.mlink.iwm.lookup.CodeLookupValues;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.filter.LocatorTree;
import org.mlink.iwm.filter.TargetClassTree;
import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.dbfeature.NewDBFeautureSetup;
//import org.mlink.iwm.timer.AgentTimer;
import org.mlink.iwm.timer.IWMTimer;
//import org.mlink.iwm.timer.RunAgents;
import org.mlink.iwm.jms.PTPClient;
import org.mlink.sitar.taps.HelloTaps;


public class IWMServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(IWMServlet.class);
    private final String CMD_RELOAD       =   "reload";
    private final String CMD_VERSION      =   "version";
    //private final String CMD_V1DATA_CLEAN =   "v1_data_clean";
    //private final String CMD_V1DATA_CLEAN_TRACE =   "v1_data_clean_trace";
    private final String CMD_SHOW_PROPS =   "show_props";
    private final String CMD_SET_PROP   =   "set_prop";
    private final String SET_DB_SCHEMA  =   "worlds";
    private final String INSTALL_FEATURES  =   "install-features";

    public void init() throws ServletException {
        try{
            Config.loadProperties();
            ServletContext ctx = getServletContext();
            log(ctx.getServletContextName() + ". Context attributes:");
            Enumeration en = ctx.getAttributeNames();
            while (en.hasMoreElements()) {
                log((String)en.nextElement());
            }

            log("application.name=" + Config.getProperty("application.name"));

            //update database with new registered feautures
            //NewDBFeautureSetup.installFeatures();

            // init lookup values
            List lookups = LookupMgr.getCDLVs();
            //LookupMgr.readLookupTables();
            for (Object lookup : lookups) {
                CodeLookupValues clv = (CodeLookupValues) lookup;
                ctx.setAttribute(clv.getName(), clv);
            }
            // end init lookup values

            IWMTimer.getInstance().start();

            // Hibernate test class
            //HibernateInspector.run();

            // Set up Java agents data connection and timer
            if("true".equals(Config.getProperty(Config.JAVA_AGENTS_ENABLED,"false"))){
                log("Loading world connection for agents");
                BaseAccess.setWorldConnection(ProductionWorldConnection.checkoutProductionWorld());
                log("World: "+ BaseAccess.getWorldConnection().getName() +
                    " ("+ BaseAccess.getWorldConnection().getSchema() +")");

                // If the timer interval is zero in config file, the timer will not be started
                //long interval = Long.parseLong(Config.getProperty(Config.JAVA_AGENTS_INTERVAL,"0"));
                //AgentTimer.start(interval);
            }

        }catch(Throwable ex){
            ex.printStackTrace();
        }

        /** Do not init PTPClieny on server start up. It will rather init itself on when it is requested
         * We noticed, first by Chris on MAC10.5 that JMS queues are not surviving gracefully hotdeploys
         *  Server hangs on [PTPClient] Creating connection
         * try {
            PTPClient.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }*/

    }


    public Properties loadProperties(String s3)throws ServletException {

        log("Loading resources from path: " + s3);
        InputStream inputstream;
        inputstream = getClass().getClassLoader().getResourceAsStream(s3);
        if(null == inputstream)
            throw new UnavailableException("Resources not found");
        Properties properties1;
        BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
        try
        {
            properties1 = new Properties();
            properties1.load(bufferedinputstream);
            bufferedinputstream.close();
            inputstream.close();
        }
        catch(IOException ioexception)
        {
            properties1 = null;
        }
        return properties1;
    }


    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //String command = httpServletRequest.getParameter("cmd");
        //log("execute cmd="+command);
        PrintWriter out = httpServletResponse.getWriter();
        Enumeration params = httpServletRequest.getParameterNames();
        while (params.hasMoreElements()) {
            String command = (String)params.nextElement();

        if(command==null){
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/Admin.do?forward=about");
            rd.forward(httpServletRequest,httpServletResponse);
        }
        else if(command.equals("help")){
            out.println("<p>Syntax: admin?<command>");
            out.println("<br>Available commands:");
            out.println("<br>" + CMD_VERSION + ": code release version");
            out.println("<br>" + CMD_RELOAD +  ": reload lookup tables");
            //out.println("<br>" + CMD_V1DATA_CLEAN +  ": cleanup data migrated from a V1 schema to V2 schema");
            //out.println("<br>" + CMD_V1DATA_CLEAN_TRACE +  ": see cleanup log");
            out.println("<br>" + CMD_SET_PROP +  ": see system props");
            out.println("<br>" + SET_DB_SCHEMA +  ": worlds");
            out.println("<br>" + INSTALL_FEATURES +  ": run registered new db updates");
        }else if(CMD_RELOAD.equals(command)){
            Config.loadProperties();
            ServletContext ctx = getServletContext();
            LookupMgr.readLookupTables();
            List lookups = LookupMgr.getCDLVs();
            for (Object lookup : lookups) {
                CodeLookupValues clv = (CodeLookupValues) lookup;
                out.println(clv.getClass().getName() + " is reloaded<br>");
                log(clv.toString());
                ctx.setAttribute(clv.getName(), clv);
            }

            LocatorTree lTree = LocatorTree.getInstance();
            lTree.load();
            ctx.setAttribute(lTree.getClass().getName(),lTree);
            out.println(lTree.getClass().getName() + " is reloaded<br>");

            TargetClassTree tcTree = TargetClassTree.getInstance();
            tcTree.load();
            ctx.setAttribute(tcTree.getClass().getName(),tcTree);
            out.println(tcTree.getClass().getName() + " is reloaded<br>");


            out.println("<p>command " + command + " is processed");
        }else if(CMD_SHOW_PROPS.equals(command)){
            Properties props = Config.getProperties();
            for (Object o : props.keySet()) {
                String key =(String) o;
                String value = props.getProperty(key);
                out.println(key + " = " + value + "<br>");
            }
        }else if(CMD_SET_PROP.equals(command)){
            String key = httpServletRequest.getParameter("key");
            String value = httpServletRequest.getParameter("value");
            if(key==null || value==null){
                out.println("key/value pair is malformed or not supplied ");
            }else{
                Config.setProperty(key,value);
            }
        }else if(CMD_VERSION.equals(command)){
            out.println("code version = Version x.x iwm_v35");
        }else if(INSTALL_FEATURES.equals(command)){
            NewDBFeautureSetup.installFeatures();
        }else if("jms".equals(command)){
            PTPClient.sendTextAsync(httpServletRequest.getParameter("text"));
        }else if("wshello".equals(command)){
            new HelloTaps().test();
         }else if(SET_DB_SCHEMA.equals(command)){
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/WorldsMaint.do?forward=read");
            rd.forward(httpServletRequest,httpServletResponse);
        /** do not remove commented code. May be used later
           }else if(CMD_V1DATA_CLEAN_TRACE.equals(command)){
            out.println("<p><a href=\"javascript:window.location.replace('/IWM/admin?cmd=v1_data_clean_trace');\">Refresh</a>");
            File output = new File((String)httpServletRequest.getSession().getAttribute("reportFile"));
            BufferedReader in = new BufferedReader(new FileReader(output));
            String line;
            while((line=in.readLine()) != null){
                out.println("<br>" + line);
            }
            in.close();
        }else if(CMD_V1DATA_CLEAN.equals(command)){
            //PolicySF psf = ServiceLocator.getPolicySFLocal( );
            //String outputFilePath = psf.cleanV1data();
            MigratedDataCleaner mdc = new MigratedDataCleaner();
            mdc.start();
            httpServletRequest.getSession().setAttribute("reportFile",mdc.getReportFileName());
            out.println("<p>command " + command + " is being processed");
            out.println("<p><a href=\"javascript:window.location.replace('/IWM/admin?cmd=v1_data_clean_trace');\">Refresh</a>");
        }*/
        }
        }

    }
    public void log(String str){logger.info(str);}

    public void destroy() {
        super.destroy();
        logger.debug("destroy is called");
        IWMTimer.getInstance().stop();


        if("true".equals(Config.getProperty(Config.JAVA_AGENTS_ENABLED,"false"))){
            log("unloading world connection for agents");
            try {
                BaseAccess.getWorldConnection().checkin();
                //AgentTimer.stop();
            } catch (Exception e) {
                log("Error checking in production world: "+ e);
                e.printStackTrace();
            }
        }

        try {
            PTPClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}

