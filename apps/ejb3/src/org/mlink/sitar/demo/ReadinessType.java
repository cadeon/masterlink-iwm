
package org.mlink.sitar.demo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for readinessType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readinessType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="availableForTasking" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *         &lt;element name="certifications" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *         &lt;element name="qualifications" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *         &lt;element name="licences" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *         &lt;element name="qualifiedForTasking" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *         &lt;element name="readyForTasking" type="{http://www.mlink.org/sitar/taps}measuresType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readinessType", propOrder = {
    "availableForTasking",
    "certifications",
    "qualifications",
    "licences",
    "qualifiedForTasking",
    "readyForTasking"
})
public class ReadinessType {

    @XmlElement(required = true)
    protected MeasuresType availableForTasking;
    @XmlElement(required = true)
    protected MeasuresType certifications;
    @XmlElement(required = true)
    protected MeasuresType qualifications;
    @XmlElement(required = true)
    protected MeasuresType licences;
    @XmlElement(required = true)
    protected MeasuresType qualifiedForTasking;
    @XmlElement(required = true)
    protected MeasuresType readyForTasking;

    /**
     * Gets the value of the availableForTasking property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getAvailableForTasking() {
        return availableForTasking;
    }

    /**
     * Sets the value of the availableForTasking property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setAvailableForTasking(MeasuresType value) {
        this.availableForTasking = value;
    }

    /**
     * Gets the value of the certifications property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getCertifications() {
        return certifications;
    }

    /**
     * Sets the value of the certifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setCertifications(MeasuresType value) {
        this.certifications = value;
    }

    /**
     * Gets the value of the qualifications property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getQualifications() {
        return qualifications;
    }

    /**
     * Sets the value of the qualifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setQualifications(MeasuresType value) {
        this.qualifications = value;
    }

    /**
     * Gets the value of the licences property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getLicences() {
        return licences;
    }

    /**
     * Sets the value of the licences property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setLicences(MeasuresType value) {
        this.licences = value;
    }

    /**
     * Gets the value of the qualifiedForTasking property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getQualifiedForTasking() {
        return qualifiedForTasking;
    }

    /**
     * Sets the value of the qualifiedForTasking property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setQualifiedForTasking(MeasuresType value) {
        this.qualifiedForTasking = value;
    }

    /**
     * Gets the value of the readyForTasking property.
     * 
     * @return
     *     possible object is
     *     {@link MeasuresType }
     *     
     */
    public MeasuresType getReadyForTasking() {
        return readyForTasking;
    }

    /**
     * Sets the value of the readyForTasking property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasuresType }
     *     
     */
    public void setReadyForTasking(MeasuresType value) {
        this.readyForTasking = value;
    }

}
