
package org.mlink.sitar.taps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sitarType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sitarType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="generated-date" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="unit" type="{http://www.mlink.org/sitar/taps}UnitType" maxOccurs="unbounded"/>
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
@XmlType(name = "sitarType", propOrder = {
    "generatedDate",
    "unit"
})
public class SitarType {

    @XmlElement(name = "generated-date")
    protected long generatedDate;
    @XmlElement(required = true)
    protected List<UnitType> unit;
    @XmlAttribute
    protected String name;

    /**
     * Gets the value of the generatedDate property.
     * 
     */
    public long getGeneratedDate() {
        return generatedDate;
    }

    /**
     * Sets the value of the generatedDate property.
     * 
     */
    public void setGeneratedDate(long value) {
        this.generatedDate = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnitType }
     * 
     * 
     */
    public List<UnitType> getUnit() {
        if (unit == null) {
            unit = new ArrayList<UnitType>();
        }
        return this.unit;
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
