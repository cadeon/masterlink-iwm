package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class ShiftRef {  
	private String id;
	private String code;
    private String description;
    private String shiftStart;
    private String shiftEnd;
    private String time;
    
	private String archivedDate;
	
	public String getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(String archivedDate) {
		this.archivedDate = archivedDate;
	}
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShiftStart() {
		return shiftStart;
	}
	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShiftEnd() {
		return shiftEnd;
	}
	public void setShiftEnd(String shiftEnd) {
		this.shiftEnd = shiftEnd;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}    
}