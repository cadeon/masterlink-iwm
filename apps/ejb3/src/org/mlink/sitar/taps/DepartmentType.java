
package org.mlink.sitar.taps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DepartmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DepartmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="readiness" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="command-requirements" type="{http://www.mlink.org/sitar/taps}CommandRequirementsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="division" type="{http://www.mlink.org/sitar/taps}DivisionType" maxOccurs="unbounded"/>
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
@XmlType(name = "DepartmentType", propOrder = {
    "readiness",
    "commandRequirements",
    "division"
})
public class DepartmentType {

    protected float readiness;
    @XmlElement(name = "command-requirements", nillable = true)
    protected List<CommandRequirementsType> commandRequirements;
    @XmlElement(required = true)
    protected List<DivisionType> division;
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
     * Gets the value of the division property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the division property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDivision().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DivisionType }
     * 
     * 
     */
    public List<DivisionType> getDivision() {
        if (division == null) {
            division = new ArrayList<DivisionType>();
        }
        return this.division;
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
