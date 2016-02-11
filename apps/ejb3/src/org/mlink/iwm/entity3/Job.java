/*-----------------------------------------------------------------------------------
	File: Job.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.sql.SQLException;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;

import uk.ltd.getahead.dwr.WebContextFactory;

@Entity
@Table(name="JOB")
@SequenceGenerator(name="JOB_SEQ_GEN", sequenceName="JOB_SEQ",allocationSize = 1)
public class Job implements Serializable,BaseEntity {
	private static final Logger logger = Logger.getLogger(Job.class);

	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JOB_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
    
	public Job(){}
	public Job(long id){
		this.id = id;
	}
	
	private Date createdDate;
	private Date completedDate;
	private Date earliestStart;
	private Date latestStart;
	private Date lastUpdated;
	private Date finishBy;
	private String createdBy;
	private Date dispatchedDate;
	private Date startedDate;
	private Long estTime;
	private Integer jobTypeId;
	private Integer priorityId;
	private Integer statusId;
	private Integer skillTypeId;
	private Integer skillLevelId;
	private Long objectId;
	private Long locatorId;
	private Integer numberOfWorkers;
	private Integer scheduleResponsibilityId;
	private Integer approved;
	private String description;
	private Long organizationId;
	private Date scheduledDate;
	private String note;
	private Integer sequenceLevel;
	private Date archivedDate;
	private String refId;
	private String sticky;
	private Long totalTime;
	private Project project; 
	private Collection<JobTask> jobTasks;
	private TenantRequest tenantRequest; 
	
	//from VO : do not store them
	private String objectRef;
    private Long jobScheduleId;
    private String fullLocator;
    
	@Column(name="CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate(){
		return createdDate;
	}
	public void setCreatedDate(Date createdDate){
		this.createdDate = createdDate;
	}
	
	@Column(name="CREATEDBY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="OBJECT_ID")
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	
	@Column(name="LOCATOR_ID")
	public Long getLocatorId() {
		return locatorId;
	}
	public void setLocatorId(Long locatorId) {
		this.locatorId = locatorId;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="ORGANIZATION_ID")
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	@Column(name="COMPLETED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getCompletedDate(){
		return completedDate;
	}
	public void setCompletedDate(Date completedDate){
		this.completedDate = completedDate;
	}

	@Column(name="EARLIEST_START")
	@Temporal(TemporalType.DATE)
    public Date getEarliestStart(){
		return earliestStart;
	}
	public void setEarliestStart(Date earliestStart){
		this.earliestStart = earliestStart;
	}

	@Column(name="LATEST_START")
	@Temporal(TemporalType.DATE)
    public Date getLatestStart(){
		return latestStart;
	}
	public void setLatestStart(Date latestStart){
		this.latestStart = latestStart;
	}
	
	@Column(name="LAST_UPDATED")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdated(){
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	@Column(name="FINISHBY")
	@Temporal(TemporalType.DATE)
    public Date getFinishBy(){
		return finishBy;
	}
	public void setFinishBy(Date finishBy){
		this.finishBy = finishBy;
	}
	
	@Column(name="DISPATCHED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getDispatchedDate(){
		return dispatchedDate;
	}
	public void setDispatchedDate(Date dispatchedDate){
		this.dispatchedDate = dispatchedDate;
	}
	
	@Column(name="STARTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getStartedDate(){
		return startedDate;
	}
	public void setStartedDate(Date startedDate){
		this.startedDate = startedDate;
	}

	@Column(name="ESTIMATEDTIME")
	public Long getEstTime(){
		return estTime;
	}
	public void setEstTime(Long estTime){
		this.estTime = estTime;
	}
	
	@Column(name="JOB_TYPE_ID")
    public Integer getJobTypeId(){
		return jobTypeId;
	}
	public void setJobTypeId(Integer jobTypeId){
		this.jobTypeId = jobTypeId;
	}

	@Column(name="PRIORITY_ID")
    public Integer getPriorityId(){
		return priorityId;
	}
	public void setPriorityId(Integer priorityId){
		this.priorityId = priorityId;
	}
	
	@Column(name="STATUS_ID")
    public Integer getStatusId(){
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	@Column(name="SKILL_TYPE_ID")
    public Integer getSkillTypeId(){
		return skillTypeId;
	}
	public void setSkillTypeId(Integer skillTypeId){
		this.skillTypeId = skillTypeId;
	}
	
	@Column(name="SKILL_LEVEL_ID")
    public Integer getSkillLevelId(){
		return skillLevelId;
	}
	public void setSkillLevelId(Integer skillLevelId){
		this.skillLevelId = skillLevelId;
	}
	
	@Column(name="NUMBER_WORKERS")
    public Integer getNumberOfWorkers(){
		return numberOfWorkers;
	}
	public void setNumberOfWorkers(Integer numberOfWorkers){
		this.numberOfWorkers = numberOfWorkers;
	}
	
	@Column(name="SCHEDULE_RESPONSIBILITY_ID")
    public Integer getScheduleResponsibilityId(){
		return scheduleResponsibilityId;
	}
	public void setScheduleResponsibilityId(Integer scheduleResponsibilityId){
		this.scheduleResponsibilityId = scheduleResponsibilityId;
	}
	
	@Column(name="APPROVED")
    public Integer getApproved(){
		return approved;
	}
	public void setApproved(Integer approved){
		this.approved = approved;
	}

	@Column(name="SCHEDULED_DATE")
	@Temporal(TemporalType.DATE)
    public Date getScheduledDate(){
		return scheduledDate;
	}
	public void setScheduledDate(Date scheduledDate){
		this.scheduledDate = scheduledDate;
	}	
	
	@Column(name="NOTE")
	public String getNote(){
		return note;
	}
	public void setNote(String note){
		this.note = note;
	}

	@Column(name="SEQUENCE_LEVEL")
	public Integer getSequenceLevel(){
		return sequenceLevel;
	}
	public void setSequenceLevel(Integer sequenceLevel){
		this.sequenceLevel = sequenceLevel;
	}


	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getArchivedDate(){
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate){
		this.archivedDate = archivedDate;
	}

	@Column(name="REF_ID")
    public String getRefId(){
		return refId;
	}
    public void setRefId(String refId){
    	this.refId = refId;
    }
	
	@Column(name="STICKY")
    public String getSticky(){
		return sticky;
	}
	public void setSticky(String sticky){
    	this.sticky = sticky;
    }
    
    @Column(name="TOTAL_TIME")
    public Long getTotalTime(){
    	return this.totalTime;
    }
    public void setTotalTime(Long totalTime){
    	this.totalTime = totalTime;
    }
    
    @ManyToOne()
	@JoinColumn(name = "PROJECT_ID")
    public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="job", fetch=FetchType.EAGER)
    public Collection<JobTask> getJobTasks() {
		return jobTasks;
	}
	public void setJobTasks(Collection<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@JoinColumn(name = "TENANT_REQUEST_ID")
    public TenantRequest getTenantRequest() {
		return tenantRequest;
	}
	public void setTenantRequest(TenantRequest tenantRequest) {
		this.tenantRequest = tenantRequest;
	}
	
//KEEPMESTART

   @Transient
   public boolean isAssessment() {
       return (getJobTypeId()== TaskTypeRef.getIdByCode(TaskTypeRef.ASSESSMENT));
   }
//KEEPMEEND

   /**
    * Job is in terminal state when status is one of these DUN EJO CIA
    * @return boolean
    */
   @Transient
   public boolean isTerminal(){
       return  JobStatusRef.getIdByCode(JobStatusRef.Status.CIA)==getStatusId()   ||
               JobStatusRef.getIdByCode(JobStatusRef.Status.DUN)==getStatusId() ||
               JobStatusRef.getIdByCode(JobStatusRef.Status.EJO) ==getStatusId();
   }

   @Transient
   public String getObjectRef() {
	   return objectRef;
   }
   public void setObjectRef(String objectRef) {
	   this.objectRef = objectRef;
   }

   @Transient
   public Long getJobScheduleId() {
	   return jobScheduleId;
   }
   public void setJobScheduleId(Long jobScheduleId) {
	   this.jobScheduleId = jobScheduleId;
   }
   
   @Transient
   public String getFullLocator() {
	   return fullLocator;
   }
   public void setFullLocator(String fullLocator) {
	   this.fullLocator = fullLocator;
   }
}