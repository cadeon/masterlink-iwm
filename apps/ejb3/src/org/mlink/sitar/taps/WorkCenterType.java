
package org.mlink.sitar.taps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WorkCenterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkCenterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="readiness" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="command-requirements" type="{http://www.mlink.org/sitar/taps}CommandRequirementsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="worker" type="{http://www.mlink.org/sitar/taps}WorkerType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkCenterType", propOrder = {
    "readiness",
    "commandRequirements",
    "worker"
})
public class WorkCenterType {

    protected float readiness;
    @XmlElement(name = "command-requirements", nillable = true)
    protected List<CommandRequirementsType> commandRequirements;
    @XmlElement(required = true)
    protected List<WorkerType> worker;
    @XmlAttribute
    protected String name;

    /**
     * Gets the value of the readiness property.
     * 
     */
    public float getReadiness() {
        return readiness;
    }

    /**
     * Sets the value of the readiness property.
     * 
     */
    public void setReadiness(float value) {
        this.readiness = value;
    }

    /**
     * Gets the value of the commandRequirements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the commandRequirements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommandRequirements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommandRequirementsType }
     * 
     * 
     */
    public List<CommandRequirementsType> getCommandRequirements() {
        if (commandRequirements == null) {
            commandRequirements = new ArrayList<CommandRequirementsType>();
        }
        return this.commandRequirements;
    }

    /**
     * Gets the value of the worker property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the worker property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorker().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WorkerType }
     * 
     * 
     */
    public List<WorkerType> getWorker() {
        if (worker == null) {
            worker = new ArrayList<WorkerType>();
        }
        return this.worker;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
