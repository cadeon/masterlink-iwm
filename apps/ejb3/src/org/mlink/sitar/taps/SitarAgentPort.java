
package org.mlink.sitar.taps;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * JBossWS Generated Source
 * 
 * Generation Date: Fri Sep 28 13:47:56 EDT 2007
 * 
 * This generated source code represents a derivative work of the input to
 * the generator that produced it. Consult the input for the copyright and
 * terms of use that apply to this source code.
 * 
 * JAX-WS Version: 2.0
 * 
 */
@WebService(name = "SitarAgentPort", targetNamespace = "http://www.taps-vss.com/dataagents")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SitarAgentPort {


    /**
     * 
     * @param sitarMessage
     * @return
     *     returns org.mlink.sitar.taps.DataAgentResponse
     */
    @WebMethod(action = "urn:#publishData")
    @WebResult(name = "DataAgentResponse", targetNamespace = "http://www.taps-vss.com/dataagents", partName = "DataAgentResponse")
    public DataAgentResponse publishData(
        @WebParam(name = "SitarMessage", targetNamespace = "http://www.taps-vss.com/dataagents", partName = "SitarMessage")
        SitarType sitarMessage);

}
