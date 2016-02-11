
package org.mlink.iwm.bean;


public class Locator {

    protected java.lang.String locatorId;
    protected java.lang.String name;
    protected java.lang.String address;
    protected java.lang.String schemaId;
    protected java.lang.String schema;
    protected java.lang.String parentId;
    protected java.lang.String fullLocator;
    protected java.lang.String locatorDataCount;
    protected java.lang.String inServiceDate;
    private java.lang.String abbr;
    private java.lang.String emergencyContact;
    private java.lang.String securityLevel;


    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }



    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public String getId() {    /* id is default name for item ID, if bean does not have like in this case it s locatorId make sure getId is still defined. Required by genereci prcodeures in IWMDataTable*/
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public void setId(String locatorId) {
    	this.locatorId = locatorId;
    }
        
    public String getInServiceDate() {
        return inServiceDate;
    }

    public void setInServiceDate(String inServiceDate) {
        this.inServiceDate = inServiceDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getFullLocator() {
        return fullLocator;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public String getLocatorDataCount() {
        return locatorDataCount;
    }

    public void setLocatorDataCount(String locatorDataCount) {
        this.locatorDataCount = locatorDataCount;
    }

    /*public String getSchema() {
        return org.mlink.iwm.lookup.LocatorSchemaRef.getLabel(StringUtils.getInteger(schemaId));
    }*/

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}