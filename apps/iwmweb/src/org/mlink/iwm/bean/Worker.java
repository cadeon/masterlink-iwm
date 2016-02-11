package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class Worker extends Party{
    private String userId;
    private String active;
    private String organizationId;

    private String type;
    private String personId;
    private String billingRate;
    private String fia;
    private String title;
    private String username;
    private String locatorId;
    private String statusId;
    
    private String addressId;

    private String refId;
    private String billetSequenceCode;
    
    private String locInfo;

    public String getLocInfo() {
		return locInfo;
	}

	public void setLocInfo(String locInfo) {
		this.locInfo = locInfo;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getBillingRate() {
        return billingRate;
    }

    public void setBillingRate(String billingRate) {
        this.billingRate = billingRate;
    }

    public String getFia() {
        return fia;
    }

    public void setFia(String fia) {
        this.fia = fia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getBilletSequenceCode() {
        return billetSequenceCode;
    }

    public void setBilletSequenceCode(String billetSequenceCode) {
        this.billetSequenceCode = billetSequenceCode;
    }
}
