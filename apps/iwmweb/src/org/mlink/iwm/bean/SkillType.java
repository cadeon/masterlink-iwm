package org.mlink.iwm.bean;

/**
 * User: raghu
 * Date: Aug 25, 2009
 */
public class SkillType {  
	private String id;
	private String code;
    private int displayOrder;
	private String description;
	private String createdDate;
	private String archivedDate;

	private String workerCount;
	private String jobCount;
	private String templateCount;
	
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
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(String archivedDate) {
		this.archivedDate = archivedDate;
	}
	public String getWorkerCount() {
		return workerCount;
	}
	public void setWorkerCount(String workerCount) {
		this.workerCount = workerCount;
	}
	public String getJobCount() {
		return jobCount;
	}
	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	}
	public String getTemplateCount() {
		return templateCount;
	}
	public void setTemplateCount(String templateCount) {
		this.templateCount = templateCount;
	} 
}
