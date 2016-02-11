
package org.mlink.sitar.demo;

import org.mlink.iwm.util.Config;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * JBossWS Generated Source
 * 
 * Generation Date: Wed Dec 26 16:46:28 EST 2007
 * 
 * This generated source code represents a derivative work of the input to
 * the generator that produced it. Consult the input for the copyright and
 * terms of use that apply to this source code.
 * 
 * JAX-WS Version: 2.0
 * 
 */
@WebServiceClient(name = "SitarAgentService", targetNamespace = "http://www.taps-vss.com/dataagents", wsdlLocation = "http://www.taps-vss.com:8080/axis/services/SitarAgent?wsdl")
public class SitarAgentService
    extends Service
{

    private final static URL SITARAGENTSERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            //url = new URL("http://www.taps-vss.com:8080/axis/services/SitarAgent?wsdl");
            url = new URL(Config.getProperty(Config.TAPS_WEBSERVICE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SITARAGENTSERVICE_WSDL_LOCATION = url;
    }

    public SitarAgentService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SitarAgentService() {
        super(SITARAGENTSERVICE_WSDL_LOCATION, new QName("http://www.taps-vss.com/dataagents", "SitarAgentService"));
    }

    /**
     * 
     * @return
     *     returns SitarAgentPort
     */
    @WebEndpoint(name = "SitarAgent")
    public SitarAgentPort getSitarAgent() {
        return (SitarAgentPort)super.getPort(new QName("http://www.taps-vss.com/dataagents", "SitarAgent"), SitarAgentPort.class);
    }

}
