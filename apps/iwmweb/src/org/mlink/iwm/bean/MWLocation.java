package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 7, 2006
 */
public class MWLocation {
    private String workerName;
    private String personId;
    private String organizationId;
    private String organizationName;
    private String firstPunch;
    private String lastPunch;
    private String hours;
    private String locInfo;
    
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getFirstPunch() {
		return firstPunch;
	}
	public void setFirstPunch(String firstPunch) {
		this.firstPunch = firstPunch;
	}
	public String getLastPunch() {
		return lastPunch;
	}
	public void setLastPunch(String lastPunch) {
		this.lastPunch = lastPunch;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getLocInfo() {
		return locInfo;
	}
	public void setLocInfo(String locInfo) {
		this.locInfo = locInfo;
	}
}
