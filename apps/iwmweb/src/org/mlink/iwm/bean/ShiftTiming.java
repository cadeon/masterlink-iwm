package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class ShiftTiming { 
	private String personId;
	private String orgId;
    private String orgName;
    private String userName;
    private String startDate;
    private String stopDate;
    private String shiftDurIncBreaks;
    private String shiftDur;
    private String timeOnBreaks;
    private String timeOnJobTasks;
    private String npt;
    
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getShiftDurIncBreaks() {
		return shiftDurIncBreaks;
	}
	public void setShiftDurIncBreaks(String shiftDurIncBreaks) {
		this.shiftDurIncBreaks = shiftDurIncBreaks;
	}
	public String getShiftDur() {
		return shiftDur;
	}
	public void setShiftDur(String shiftDur) {
		this.shiftDur = shiftDur;
	}
	public String getTimeOnBreaks() {
		return timeOnBreaks;
	}
	public void setTimeOnBreaks(String timeOnBreaks) {
		this.timeOnBreaks = timeOnBreaks;
	}
	public String getTimeOnJobTasks() {
		return timeOnJobTasks;
	}
	public void setTimeOnJobTasks(String timeOnJobTasks) {
		this.timeOnJobTasks = timeOnJobTasks;
	}
	public String getNpt() {
		return npt;
	}
	public void setNpt(String npt) {
		this.npt = npt;
	}
}