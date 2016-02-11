
package org.mlink.sitar.demo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


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
@WebService(name = "SitarAgentPort", targetNamespace = "http://www.taps-vss.com/dataagents")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SitarAgentPort {


    /**
     * 
     * @param sitar
     * @return
     *     returns org.mlink.sitar.demo.DataAgentResponse
     */
    @WebMethod(action = "urn:#publishData")
    @WebResult(name = "dataAgentResponse", targetNamespace = "http://www.taps-vss.com/dataagents", partName = "dataAgentResponse")
    public DataAgentResponse publishData(
        @WebParam(name = "sitar", targetNamespace = "http://www.mlink.org/sitar/taps", partName = "sitar")
        SitarType sitar);

}
