
package org.mlink.sitar.demo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for unitType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="unitType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="readiness" type="{http://www.mlink.org/sitar/taps}readinessType"/>
 *         &lt;element name="subUnit" type="{http://www.mlink.org/sitar/taps}unitType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unitType", propOrder = {
    "readiness",
    "subUnit"
})
public class UnitType {

    @XmlElement(required = true)
    protected ReadinessType readiness;
    @XmlElement(required = true)
    protected List<UnitType> subUnit;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String type;

    /**
     * Gets the value of the readiness property.
     * 
     * @return
     *     possible object is
     *     {@link ReadinessType }
     *     
     */
    public ReadinessType getReadiness() {
        return readiness;
    }

    /**
     * Sets the value of the readiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReadinessType }
     *     
     */
    public void setReadiness(ReadinessType value) {
        this.readiness = value;
    }

    /**
     * Gets the value of the subUnit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subUnit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnitType }
     * 
     * 
     */
    public List<UnitType> getSubUnit() {
        if (subUnit == null) {
            subUnit = new ArrayList<UnitType>();
        }
        return this.subUnit;
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

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
