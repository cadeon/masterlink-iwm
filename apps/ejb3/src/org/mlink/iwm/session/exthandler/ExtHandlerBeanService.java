
package org.mlink.iwm.session.exthandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.0
 * 
 */
//@WebServiceClient(name = "ExtHandlerBeanService", targetNamespace = "http://exthandler.session.iwm.mlink.org/", wsdlLocation = "http://localhost:8080/IWMWS/ExtHandlerBean?wsdl")
public class ExtHandlerBeanService
    extends Service
{

    private final static URL EXTHANDLERBEANSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(org.mlink.iwm.session.exthandler.ExtHandlerBeanService.class.getName());

    static {
        URL url = null;
        /*try {
            URL baseUrl;
            baseUrl = org.mlink.iwm.session.exthandler.ExtHandlerBeanService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/IWMWS/ExtHandlerBean?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/IWMWS/ExtHandlerBean?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }*/
        EXTHANDLERBEANSERVICE_WSDL_LOCATION = url;
    }

    public ExtHandlerBeanService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ExtHandlerBeanService() {
        super(EXTHANDLERBEANSERVICE_WSDL_LOCATION, new QName("http://exthandler.session.iwm.mlink.org/", "ExtHandlerBeanService"));
    }

    /**
     * 
     * @return
     *     returns ExtHandler
     */
    @WebEndpoint(name = "ExtHandlerBeanPort")
    public ExtHandler getExtHandlerBeanPort() {
        return super.getPort(new QName("http://exthandler.session.iwm.mlink.org/", "ExtHandlerBeanPort"), ExtHandler.class);
    }

}
