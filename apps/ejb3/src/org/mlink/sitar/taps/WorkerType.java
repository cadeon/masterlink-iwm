
package org.mlink.sitar.taps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WorkerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="skillgroup" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="skilllevel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rate" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="rating" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PNEC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DNEC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="certification" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="qualification" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="licence" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="readiness" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkerType", propOrder = {
    "userId",
    "skillgroup",
    "skilllevel",
    "rate",
    "rating",
    "pnec",
    "dnec",
    "certification",
    "qualification",
    "licence",
    "readiness"
})
public class WorkerType {

    @XmlElement(required = true)
    protected String userId;
    @XmlElement(required = true)
    protected String skillgroup;
    @XmlElement(required = true)
    protected String skilllevel;
    protected float rate;
    @XmlElement(required = true)
    protected String rating;
    @XmlElement(name = "PNEC", required = true)
    protected String pnec;
    @XmlElement(name = "DNEC", required = true)
    protected String dnec;
    protected List<String> certification;
    protected List<String> qualification;
    protected List<String> licence;
    protected float readiness;

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the skillgroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkillgroup() {
        return skillgroup;
    }

    /**
     * Sets the value of the skillgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkillgroup(String value) {
        this.skillgroup = value;
    }

    /**
     * Gets the value of the skilllevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkilllevel() {
        return skilllevel;
    }

    /**
     * Sets the value of the skilllevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkilllevel(String value) {
        this.skilllevel = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     */
    public float getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     */
    public void setRate(float value) {
        this.rate = value;
    }

    /**
     * Gets the value of the rating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRating(String value) {
        this.rating = value;
    }

    /**
     * Gets the value of the pnec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPNEC() {
        return pnec;
    }

    /**
     * Sets the value of the pnec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPNEC(String value) {
        this.pnec = value;
    }

    /**
     * Gets the value of the dnec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDNEC() {
        return dnec;
    }

    /**
     * Sets the value of the dnec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDNEC(String value) {
        this.dnec = value;
    }

    /**
     * Gets the value of the certification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCertification() {
        if (certification == null) {
            certification = new ArrayList<String>();
        }
        return this.certification;
    }

    /**
     * Gets the value of the qualification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getQualification() {
        if (qualification == null) {
            qualification = new ArrayList<String>();
        }
        return this.qualification;
    }

    /**
     * Gets the value of the licence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the licence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLicence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLicence() {
        if (licence == null) {
            licence = new ArrayList<String>();
        }
        return this.licence;
    }

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

}
