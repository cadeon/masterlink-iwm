package org.mlink.iwm.bean;

public class Organization extends Party{

    protected String workerCount;
    protected String type;
    protected String locatorId;
    protected String parentId;
    protected String partyId;
    protected String addressId;
    protected String schemaId;


    public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(String workerCount) {
        this.workerCount = workerCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
