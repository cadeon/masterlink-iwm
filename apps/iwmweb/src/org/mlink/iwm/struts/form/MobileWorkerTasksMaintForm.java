package org.mlink.iwm.struts.form;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionMapping;

/**
 * User: andrei
 * Date: Dec 16, 2006
 * Time: 1:18:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class MobileWorkerTasksMaintForm extends BaseForm{
    protected String tenantNote;
    protected String name;
    protected String createdDate;
    protected String closedDate;
    protected String note;
    protected String description;
    protected String objectId;
    protected String phone;
    protected String email;
    protected String scheduledDate;
    protected String workerStatus;
    protected String jobId;
    protected Long jobTypeId;
    protected String taskTimeKeeping;
    protected String jobStatus;
    protected String age;
    protected String objectRef;
    protected String fullLocator;
    protected String jobTaskId;
    protected String estTime;
    
    
    public void setClosedDate(String closedDate){
    	this.closedDate = closedDate;
    }
    
    public String getClosedDate(){
    	return closedDate;
    }
    
    public void setJobTaskId(String jtid){
    	this.jobTaskId = jtid;
    }
    
    public String getJobTaskId(){
    	return jobTaskId;
    }
    
    public void setObjectRef(String or){
    	this.objectRef = or;
    }
    
    public String getObjectRef(){
    	return objectRef;
    }
    
    public void setCreatedDate(String cd){
    	this.createdDate = cd;
    }
    
    public String getCreatedDate(){
    	return createdDate;
    }
    
    public void setFullLocator(String fl){
    	this.fullLocator = fl;
    }
    
    public String getFullLocator(){
    	return fullLocator;
    }
    
    public String getAge() {
		return age;
	}
    
	public void setAge(String age) {
		this.age = age;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getTaskTimeKeeping() {
		return taskTimeKeeping;
	}

	public void setTaskTimeKeeping(String taskTimeKeeping) {
		this.taskTimeKeeping = taskTimeKeeping;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}


	public long getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(long jobTypeId) {
		this.jobTypeId = jobTypeId;
	}
	
	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(String workerStatus) {
		this.workerStatus = workerStatus;
	}

	public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTenantNote() {
        return tenantNote;
    }

    public void setTenantNote(String tenantNote) {
        this.tenantNote = tenantNote;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void reset(ActionMapping actionMapping, ServletRequest servletRequest) {
        super.reset(actionMapping, servletRequest);
        tenantNote=null;
        note=null;
        description=null;
    }

	
}
