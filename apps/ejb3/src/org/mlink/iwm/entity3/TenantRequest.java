/*-----------------------------------------------------------------------------------
	File: TenantRequest.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="TENANT_REQUEST")
@SequenceGenerator(name="TENANT_REQUEST_SEQ_GEN", sequenceName="TENANT_REQUEST_SEQ",allocationSize = 1)
public class TenantRequest implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TENANT_REQUEST_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String tenantName;
	private String tenantPhone;
	private String tenantEmail;
	private Long locatorId;
	private Long problemId;
	private String note;
	private String requestType;
	private String locationComment;
	private Integer urgent;
	private Date createdDate;
	private Long jobId;
	private String emergencyContact;
	
	//from VO dont save
	private Collection<JobAction> jobActions;
	private Collection<Person> workers;
	private Integer jobStatusId;
	private Date completedDate;
	private String jobDescription;
	
	public static final String INTERNAL_REQUEST = "INT";
    public static final String EXTERNAL_REQUEST = "EXT";
    
    @Column(name="TENANT_NAME")
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
	@Column(name="PHONE")
	public String getTenantPhone() {
		return tenantPhone;
	}
	public void setTenantPhone(String tenantPhone) {
		this.tenantPhone = tenantPhone;
	}
	
	@Column(name="EMAIL")
	public String getTenantEmail() {
		return tenantEmail;
	}
	public void setTenantEmail(String tenantEmail) {
		this.tenantEmail = tenantEmail;
	}
	
	@Column(name="LOCATOR_ID")
	public Long getLocatorId() {
		return locatorId;
	}
	public void setLocatorId(Long locatorId) {
		this.locatorId = locatorId;
	}
	
	@Column(name="PROBLEM_ID")
	public Long getProblemId() {
		return problemId;
	}
	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}
	
	@Column(name="NOTE")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name="REQUEST_TYPE")
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	@Column(name="LOCATION_COMMENT")
	public String getLocationComment() {
		return locationComment;
	}
	public void setLocationComment(String locationComment) {
		this.locationComment = locationComment;
	}
	
	@Column(name="URGENT")
	public Integer getUrgent() {
		return urgent;
	}
	public void setUrgent(Integer urgent) {
		this.urgent = urgent;
	}
	
	@Column(name="CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	

	@Column(name = "JOB_ID") 
    public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	@Transient
	public Collection<JobAction> getJobActions(){
		return jobActions;
	}
	public void setJobActions(Collection<JobAction> jobActions){
		this.jobActions = jobActions;
	}
	
	@Transient
	public Collection<Person> getWorkers(){
		return workers;
	}
	public void setWorkers(Collection<Person> workers){
		this.workers = workers;
	}
	
	@Transient
	public Integer getJobStatusId() {
		return jobStatusId;
	}
	public void setJobStatusId(Integer jobStatusId) {
		this.jobStatusId = jobStatusId;
	}
	
	@Transient
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}	
	
	@Transient
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	
	@Transient
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
}