
package org.mlink.iwm.session.exthandler;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mlink.iwm.session.exthandler package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RequestInfoResponse_QNAME = new QName("http://exthandler.session.iwm.mlink.org/", "requestInfoResponse");
    private final static QName _RequestInfo_QNAME = new QName("http://exthandler.session.iwm.mlink.org/", "requestInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mlink.iwm.session.exthandler
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RequestInfo }
     * 
     */
    public RequestInfo createRequestInfo() {
        return new RequestInfo();
    }

    /**
     * Create an instance of {@link RequestInfoResponse }
     * 
     */
    public RequestInfoResponse createRequestInfoResponse() {
        return new RequestInfoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://exthandler.session.iwm.mlink.org/", name = "requestInfoResponse")
    public JAXBElement<RequestInfoResponse> createRequestInfoResponse(RequestInfoResponse value) {
        return new JAXBElement<RequestInfoResponse>(_RequestInfoResponse_QNAME, RequestInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://exthandler.session.iwm.mlink.org/", name = "requestInfo")
    public JAXBElement<RequestInfo> createRequestInfo(RequestInfo value) {
        return new JAXBElement<RequestInfo>(_RequestInfo_QNAME, RequestInfo.class, null, value);
    }

}
