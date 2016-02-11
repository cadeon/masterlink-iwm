
package org.mlink.sitar.taps;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mlink.sitar.taps package. 
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

    private final static QName _SitarMessage_QNAME = new QName("http://www.taps-vss.com/dataagents", "SitarMessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mlink.sitar.taps
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataAgentResponse }
     * 
     */
    public DataAgentResponse createDataAgentResponse() {
        return new DataAgentResponse();
    }

    /**
     * Create an instance of {@link WorkCenterType }
     * 
     */
    public WorkCenterType createWorkCenterType() {
        return new WorkCenterType();
    }

    /**
     * Create an instance of {@link UnitType }
     * 
     */
    public UnitType createUnitType() {
        return new UnitType();
    }

    /**
     * Create an instance of {@link WorkerType }
     * 
     */
    public WorkerType createWorkerType() {
        return new WorkerType();
    }

    /**
     * Create an instance of {@link DepartmentType }
     * 
     */
    public DepartmentType createDepartmentType() {
        return new DepartmentType();
    }

    /**
     * Create an instance of {@link SitarType }
     * 
     */
    public SitarType createSitarType() {
        return new SitarType();
    }

    /**
     * Create an instance of {@link CommandRequirementsType }
     * 
     */
    public CommandRequirementsType createCommandRequirementsType() {
        return new CommandRequirementsType();
    }

    /**
     * Create an instance of {@link DivisionType }
     * 
     */
    public DivisionType createDivisionType() {
        return new DivisionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SitarType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.taps-vss.com/dataagents", name = "SitarMessage")
    public JAXBElement<SitarType> createSitarMessage(SitarType value) {
        return new JAXBElement<SitarType>(_SitarMessage_QNAME, SitarType.class, null, value);
    }

}
