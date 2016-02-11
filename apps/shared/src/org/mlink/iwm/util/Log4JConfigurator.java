package org.mlink.iwm.util;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

/**
 * This class was supposed to take care of IWM logging independent of log4j setup used by Jboss.
 * Well, I had a number of issues with this pertaining to that Log4j uses singleton's for its appenders.
 * I am chosing log4j override option for now (see ant build.xml)
 * User: Andrei
 * Date: Apr 8, 2006
 * Time: 9:29:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Log4JConfigurator {
    private Log4JConfigurator() {}

    public static void setup(String log4jFileName){
        URL url = Config.getPropertyResource(log4jFileName);
        System.out.println("Loading " + url);
        if(url!=null){
            if(log4jFileName.indexOf(".properties")>0)
                PropertyConfigurator.configure(url);
            else if(log4jFileName.indexOf(".xml")>0)
                DOMConfigurator.configure(url);
            else
                System.out.println(log4jFileName + " must have extension .properties or .xml ");
        }
        else
            System.out.println(" Log4j property file " + log4jFileName + " could not be loaded");
    }


}
