package org.mlink.iwm.session.exthandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mlink.iwm.session.exthandler.ExtHandler;
import org.mlink.iwm.session.exthandler.ExtHandlerBeanService;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ExtHandlerClient {
    private static Log log = LogFactory.getLog(ExtHandlerClient.class);

    public String echo(String text) {
        log.debug("echo client, attempting to echo String "+ text);
        String result = null;
        try {

            //*** OPTION A *****
        	//JAX-WS directly
            log.info("Using jax-ws directly...");
            Service service = Service.create(
                    new URL("http://localhost:8080/IWMWS/ExtHandlerBean?wsdl"),
                    new QName("http://exthandler.session.iwm.mlink.org/","ExtHandlerBeanService") );
            ExtHandler extHandler = service.getPort(ExtHandler.class);
            result = extHandler.requestInfo(text);
            log.info("jax-ws called directly, echo: "+ result);

        	//*** OPTIONS B ****
            //JAX-WS Use generated stubs from wsconsume
            log.info("using jbossws generated stubs...");
            ExtHandlerBeanService service2 = new ExtHandlerBeanService();
            ExtHandler echo2 = service2.getExtHandlerBeanPort();
            result = echo2.requestInfo(text);
            log.info("jax-ws echo webservice, echo: "+result);
       
            //*** OPTION C ****
            //Using Axis2 generated client stubs
            //to see this, have server running and use ant..
            //ant axis2run
        } catch (Exception e) {
             e.printStackTrace();
             log.error("Error in ExtHandlerClient client: ",e);
        }
        return result;
    }
/*
    public static void main(String[] args ) {
        String echoString = args[0];
        ExtHandlerClient client = new ExtHandlerClient();
        client.echo(echoString);
    }
*/
}
