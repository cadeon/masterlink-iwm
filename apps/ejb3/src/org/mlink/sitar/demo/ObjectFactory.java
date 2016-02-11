
package org.mlink.sitar.demo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mlink.sitar.demo package. 
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

    private final static QName _Sitar_QNAME = new QName("http://www.mlink.org/sitar/taps", "sitar");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mlink.sitar.demo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UnitType }
     * 
     */
    public UnitType createUnitType() {
        return new UnitType();
    }

    /**
     * Create an instance of {@link MeasureType }
     * 
     */
    public MeasureType createMeasureType() {
        return new MeasureType();
    }

    /**
     * Create an instance of {@link DataAgentResponse }
     * 
     */
    public DataAgentResponse createDataAgentResponse() {
        return new DataAgentResponse();
    }

    /**
     * Create an instance of {@link SitarType }
     * 
     */
    public SitarType createSitarType() {
        return new SitarType();
    }

    /**
     * Create an instance of {@link MeasuresType }
     * 
     */
    public MeasuresType createMeasuresType() {
        return new MeasuresType();
    }

    /**
     * Create an instance of {@link ReadinessType }
     * 
     */
    public ReadinessType createReadinessType() {
        return new ReadinessType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SitarType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.mlink.org/sitar/taps", name = "sitar")
    public JAXBElement<SitarType> createSitar(SitarType value) {
        return new JAXBElement<SitarType>(_Sitar_QNAME, SitarType.class, null, value);
    }

}
