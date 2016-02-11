package org.mlink.iwm.util;

import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;


public class EnvUtils{

    public static final String JNDI_PROVIDER_HOST_NAME = "use.context.factory";
    public static Context context;

    private static void init() throws NamingException{
        if(System.getProperty(JNDI_PROVIDER_HOST_NAME)!=null){
            // intended to use by unit tests only, should be something like localhost
            context = getInitialContext(System.getProperty(JNDI_PROVIDER_HOST_NAME));
        }else
            context = new InitialContext();
    }

    /**
     * Get Initial Naming Context.
     */
    public static Context getInitialContext() throws NamingException   {
        if(context==null)
            init();
        return context;
    }


    /**
     * Get Initial Naming Context.
     */
    public static Context getInitialContext(String strHost) throws NamingException    {
        Properties props = new Properties();
        props.put(Context.PROVIDER_URL, "jnp://"+strHost +":1099");
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        //props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming.client");
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        return new InitialContext(props);
    }

}



