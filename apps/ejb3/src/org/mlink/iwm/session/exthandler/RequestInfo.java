
package org.mlink.iwm.session.exthandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="textToExtHandler" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestInfo", propOrder = {
    "textToExtHandler"
})
public class RequestInfo {

    protected String textToExtHandler;

    /**
     * Gets the value of the textToExtHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextToExtHandler() {
        return textToExtHandler;
    }

    /**
     * Sets the value of the textToExtHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextToExtHandler(String value) {
        this.textToExtHandler = value;
    }

}
