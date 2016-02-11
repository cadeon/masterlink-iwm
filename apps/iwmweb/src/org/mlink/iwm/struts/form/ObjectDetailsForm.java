package org.mlink.iwm.struts.form;


/**
 * User: andrei
 * Date: Dec 15, 2006
 */
public class ObjectDetailsForm extends BaseForm{
	private String objectRef;
	private String fullLocator;
	private String locatorId;
	private String createdDate;
	private String className;
	private String classId;
	private String active;
	private String organization;
	private String organizationId;
	
	
	public String getObjectRef() {
		return objectRef;
	}
	public void setObjectRef(String objectRef) {
		this.objectRef = objectRef;
	}
	public String getFullLocator() {
		return fullLocator;
	}
	public void setFullLocator(String fullLocator) {
		this.fullLocator = fullLocator;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getLocatorId() {
		return locatorId;
	}
	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
}
