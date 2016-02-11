
package org.mlink.sitar.demo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


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
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="units" type="{http://www.mlink.org/sitar/taps}unitType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="generatedTimeMsecs" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sitarType", propOrder = {
    "action",
    "units"
})
public class SitarType {

    @XmlElement(required = true)
    protected String action;
    @XmlElement(required = true)
    protected List<UnitType> units;
    @XmlAttribute
    protected Long generatedTimeMsecs;

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the units property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the units property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnitType }
     * 
     * 
     */
    public List<UnitType> getUnits() {
        if (units == null) {
            units = new ArrayList<UnitType>();
        }
        return this.units;
    }

    /**
     * Gets the value of the generatedTimeMsecs property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGeneratedTimeMsecs() {
        return generatedTimeMsecs;
    }

    /**
     * Sets the value of the generatedTimeMsecs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGeneratedTimeMsecs(Long value) {
        this.generatedTimeMsecs = value;
    }

}
