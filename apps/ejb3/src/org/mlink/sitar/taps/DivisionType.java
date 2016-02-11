
package org.mlink.sitar.taps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DivisionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DivisionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="readiness" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="command-requirements" type="{http://www.mlink.org/sitar/taps}CommandRequirementsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="work-center" type="{http://www.mlink.org/sitar/taps}WorkCenterType" maxOccurs="unbounded"/>
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
@XmlType(name = "DivisionType", propOrder = {
    "readiness",
    "commandRequirements",
    "workCenter"
})
public class DivisionType {

    protected float readiness;
    @XmlElement(name = "command-requirements", nillable = true)
    protected List<CommandRequirementsType> commandRequirements;
    @XmlElement(name = "work-center", required = true, nillable = true)
    protected List<WorkCenterType> workCenter;
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
     * Gets the value of the workCenter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the workCenter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkCenter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WorkCenterType }
     * 
     * 
     */
    public List<WorkCenterType> getWorkCenter() {
        if (workCenter == null) {
            workCenter = new ArrayList<WorkCenterType>();
        }
        return this.workCenter;
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
