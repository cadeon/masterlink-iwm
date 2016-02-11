package org.mlink.agent.model;
// Generated May 27, 2006 12:59:46 PM by Hibernate Tools 3.1.0.beta5


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="JOB"
 *     
 */
public class Job implements Comparable {

    // Fields 
	private JobPrecedes jobPrecedes;
	private String      jobTypeCode;
    private JobSchedule latestJobSchedule;
    private Locator     locator;
	private String      priorityCode;
    private Project     project;
	private Integer     skillLevel=0;
	private String      statusCode;
	private Integer     totalTime;
    private BigDecimal  util=new BigDecimal(0);
    private Set         jobTasks = new HashSet(0);
    private Set         jobSchedules = new HashSet(0);
    
    private Long id;
	private Timestamp createdDate;
	private Date completedDate;
	private Date earliestStart;
	private Date latestStart;
	private Date lastUpdated;
	private Date finishBy;
	private String createdBy;
	private Date dispatchedDate;
	private Date startedDate;
	private Integer estTime;
	private Integer jobTypeId;
	private PriorityRef priorityRef;
	private JobStatusRef jobStatusRef;
	private SkillTypeRef skillTypeRef;
	private SkillLevelRef skillLevelRef;
	private Long objectId;
	private Integer numberOfWorkers;
	private ScheduleResponsibilityRef scheduleResponsibilityRef;
	private String description;
	private Long organizationId;
	private Date scheduledDate;
	private Integer sequenceLevel;
    private Boolean sticky;

    
    private boolean     isChanged = false;
     
     // Constructors
    public Job() {}
 
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="sequence"
     *             column="ID"
     *            @hibernate.generator-param
     *             name="sequence"
     *             value="job_seq"
     *         
     */
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="CREATEDBY"
     */
    public String getCreatedBy() {return createdBy;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    /**       
     *            @hibernate.property
     *             column="CREATED_DATE"
     */
    public Timestamp getCreatedDate() {return createdDate;}
    public void setCreatedDate(Timestamp createdDate) {this.createdDate = createdDate;}
    /**       
     *            @hibernate.property
     *             column="DISPATCHED_DATE"
     */
    public Date getDispatchedDate() {return dispatchedDate;}
    public void setDispatchedDate(Date dispatchedDate) {this.dispatchedDate = dispatchedDate;}
    /**       
     *            @hibernate.property
     *             column="STARTED_DATE"
     *         
     */
    public Date getStartedDate() {return startedDate;}
    public void setStartedDate(Date startedDate) {this.startedDate = startedDate;}
    /**       
     *            @hibernate.property
     *             column="FINISHBY"
     *         
     */
    public Date getFinishby() {return finishBy;}
    public void setFinishby(Date finishBy) {this. finishBy = finishBy;}
    /**       
     *            @hibernate.property
     *             column="ESTIMATEDTIME"
     *         
     */
    public Integer getEstimatedTime() {return estTime;}
    public void setEstimatedTime(Integer estTime) {this.estTime = estTime;}
    
    /**       
     *            @hibernate.property
     *             column="LAST_UPDATED"
     */
    public Date getLastUpdated() {return lastUpdated;}
    public void setLastUpdated(Date lastUpdated) {this.lastUpdated = lastUpdated;}
    /**       
     *            @hibernate.property
     *             column="NUMBER_WORKERS"
     */
    public Integer getNumberOfWorkers(){return numberOfWorkers;}
    public void setNumberOfWorkers(Integer numberOfWorkers){this.numberOfWorkers = numberOfWorkers;}
    
    /**       
     *            @hibernate.property
     *             column="COMPLETED_DATE"
     */
    public Date getCompletedDate() {return completedDate;}
    public void setCompletedDate(Date completedDate) {this.completedDate = completedDate;}
    /**       
     *            @hibernate.property
     *             column="EARLIEST_START"
     */
    public Date getEarliestStart() {return earliestStart;}    
    public void setEarliestStart(Date earliestStart) {this.earliestStart = earliestStart;}
    /**       
     *            @hibernate.property
     *             column="LATEST_START"
     */
    public Date getLatestStart() {return latestStart;}
    public void setLatestStart(Date latestStart) {this.latestStart = latestStart;}
    /**       
     *            @hibernate.property
     *             column="DESCRIPTION"
     */
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    /**       
     *            @hibernate.property
     *             column="SCHEDULED_DATE"
     */
    public Date getScheduledDate() {return scheduledDate;}
    public void setScheduledDate(Date scheduledDate) {this.scheduledDate = scheduledDate;}
       /**       
     *            @hibernate.property
     *             column="SEQUENCE_LEVEL"
     *         
     */
    public Integer getSequenceLevel() {return sequenceLevel;}
    public void setSequenceLevel(Integer sequenceLevel) {this.sequenceLevel = sequenceLevel;}
    /**       
     *            @hibernate.property
     *             type="yes_no"
     *             column="STICKY"
     */
    public Boolean getSticky() {return sticky;}
    public void setSticky(Boolean sticky) {this.sticky = sticky;}
    /**       
     *            @hibernate.property
     *             column="JOB_TYPE_ID"         
     *         
     */
    public Integer getJobTypeId() {return jobTypeId;}
    public void setJobTypeId(Integer jobTypeId) {this.jobTypeId = jobTypeId;}
    /**
     * Access method for use in the Planner, which believes the job type (task type) is
     * a String. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The job type ref must be propagated to setJobTypeRef(TaskTypeRef ttrr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     */
    public String getJobType(){
    	return jobTypeCode;
    }
    /**
     * Access method for use in the Planner, which believes the job type (task type) is
     * a String. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The job type ref must be propagated to setJobTypeRef(TaskTypeRef ttrr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     * @param code The new task type for this Job
     */
    public void setJobType(String code) {jobTypeCode=code;}
    /**       
     *            @hibernate.property
     *             column="OBJECT_ID"         
     *         
     */
    public Long getObjectId() {return objectId;}
    public void setObjectId(Long objectId) {this.objectId = objectId;}
    /**       
     *            @hibernate.property
     *             column="ORGANIZATION_ID"         
     *         
     */
    public Long getOrganizationId() {return organizationId;}
    public void setOrganizationId(Long organizationId) {this.organizationId = organizationId;}
    /**       
     *            @hibernate.property
     *             column="TOTAL_TIME"         
     *         
     */
    public Integer getTotalTime() {return this.totalTime;}
    public void setTotalTime(Integer totalTime) {this.totalTime=totalTime;}
    /**       
     *            @hibernate.one-to-one
     *             lazy="false"
     *            @hibernate.column name="ID"
     *         
     */
    public JobPrecedes getJobPrecedes(){return this.jobPrecedes;}
    public void setJobPrecedes(JobPrecedes jp){this.jobPrecedes=jp;}
    
    public Integer getIncomplete(){
    	if (this.jobPrecedes==null) return 0;
    	return this.jobPrecedes.getIncomplete();
    }
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="save-update"
     *            @hibernate.collection-key
     *             column="JOB_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.JobTask"
     *         
     */
    public Set getJobTasks() {return this.jobTasks;}
    public void setJobTasks(Set jobTasks) {this.jobTasks = jobTasks;}
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *             where="DELETED_TIME IS NULL"
     *            @hibernate.collection-key
     *             column="JOB_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.JobSchedule"
     *         
     */
    public Set getJobSchedules() {return this.jobSchedules;} 
    public void setJobSchedules(Set jobSchedules) {this.jobSchedules = jobSchedules;}
    
    /** 
     * Convenience function for the latest job schedule (attached to the most current
     * work schedule).
     * 
     */
    public JobSchedule getLatestJobSchedule(){return this.latestJobSchedule;}
    public void setLatestJobSchedule(JobSchedule latest){this.latestJobSchedule = latest;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *            @hibernate.column name="LOCATOR_ID"         
     *         
     */
    public Locator getLocator() {return this.locator;}
    public void setLocator(Locator locator) {this.locator = locator;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="none"
     *             lazy="false"
     *            @hibernate.column name="SCHEDULE_RESPONSIBILITY_ID"         
     *         
     */
    public ScheduleResponsibilityRef getScheduleResponsibilityRef() {return scheduleResponsibilityRef;}
    public void setScheduleResponsibilityRef(ScheduleResponsibilityRef scheduleResponsibilityRef) {this.scheduleResponsibilityRef = scheduleResponsibilityRef;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="none"
     *             lazy="false"
     *            @hibernate.column name="SKILL_LEVEL_ID"         
     *         
     */
    public SkillLevelRef getSkillLevelRef() {return skillLevelRef;}
    public void setSkillLevelRef(SkillLevelRef skillLevelRef) {this.skillLevelRef = skillLevelRef;}
    /**
     * Access method for use in the Planner, which believes the skill level is
     * an int. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The skill level must be propagated to setSkillLevelRef(SkillLevelRef slr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     */
    public Integer getSkillLevel(){
    	if (skillLevel==0) 
    		try { // try to set level from the SkillLevelRef
    			skillLevel = skillLevelRef.getValue();
    		} catch (NullPointerException npe) { // Just in case
    			skillLevel = SkillLevelRef.NONE;
    		}
    	return skillLevel;
    }
    /**
     * Access method for use in the Planner, which believes the skill level is
     * an int. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The skill level must be propagated to setSkillLevelRef(SkillLevelRef slr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     * @param i The new skill level for this Job
     */
    public void setSkillLevel(Integer i) {skillLevel=i;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="none"
     *             lazy="false"
     *            @hibernate.column name="SKILL_TYPE_ID"         
     *         
     */
    public SkillTypeRef getSkillTypeRef() {return skillTypeRef;}
    public void setSkillTypeRef(SkillTypeRef skillTypeRef) {this.skillTypeRef = skillTypeRef;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="none"
     *             lazy="false"
     *            @hibernate.column name="PRIORITY_ID"         
     *         
     */
    public PriorityRef getPriorityRef() {return priorityRef;}
    public void setPriorityRef(PriorityRef priorityRef) {this.priorityRef = priorityRef;}
    /**
     * Access method for use in the Planner, which believes the priority is
     * an int. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The priority must be propagated to setPriorityRef(PriorityRef pr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     */
    public Integer getPriority(){
    	if (priorityCode==null) 
    		try { // try to set priority from the PriorityRef
    			priorityCode = priorityRef.getCode();
    		} catch (NullPointerException npe) { // Just in case
    			priorityCode = PriorityRef.ZERO;
    		}
    	return Integer.parseInt(priorityCode);
    }
    /**
     * Access method for use in the Planner, which believes the priority is
     * an int. 
     * 
     * NOTE: The Planner sets this value during the course of its run.
     * The priority must be propagated to setPriorityRef(PriorityRef pr)
     * via a lookup, or the change made by the Planner will be lost
     * 
     * @param code The new priority for this Job
     */
    public void setPriority(Integer code) {priorityCode=""+code;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="none"
     *             lazy="false"
     *            @hibernate.column name="STATUS_ID"         
     *         
     */
    public JobStatusRef getStatusRef(){return jobStatusRef;}
    public void setStatusRef(JobStatusRef jobStatusRef){this.jobStatusRef = jobStatusRef;}

    /**
     * Access method for use in the JSM, which believes the status is
     * a three-character string. NOTE: The JSM sets this value during the
     * course of its run. The status must be propagated to setStatusRef(JobStatusRef jsr)
     * via a lookup, or the change made by the JSM will be lost
     * 
     */
    public String getStatus(){
     	if (statusCode==null) {
        // try to set status from the JobStatusRef
        if (jobStatusRef != null) {
    			statusCode = jobStatusRef.getCode();
    		} else {
    			return JobStatusRef.Status.INS.toString();
        }
      }
    	return statusCode;
    }
    /** 
     * Access method for use in the JSM, which believes the status is
     * a three-character string. NOTE: The JSM sets this value during the
     * course of its run. The status must be propagated to setStatusRef(JobStatusRef jsr)
     * via a lookup, or the change made by the JSM will be lost
     * 
     * @param code The new code for this Job
     */
    public void setStatus(String code) {statusCode=code;}
    /**       
     *            @hibernate.many-to-one
     *             cascade="save-update"
     *             lazy="false"
     *            @hibernate.column name="PROJECT_ID"         
     *         
     */
    public Project getProject() {return project;}
    public void setProject(Project p){project=p;}
    
    public boolean getIsChanged(){return this.isChanged;}
    public void    setIsChanged(boolean b){this.isChanged=b;}
    
    public BigDecimal getUtility(){return this.util;}
    public void       setUtility(BigDecimal bd){this.util=bd;}


	public int compareTo(Object o) {
		if (o==null) throw new NullPointerException("Tried to compare Job "+ this.getId() +" to a null object");
		if (!(o instanceof Job)) throw new ClassCastException("Not an instance of a Job: "+ o);
		Job job = (Job)o;
		if (job.getUtility()==null) throw new NullPointerException("Tried to compare to null utility on Job "+ job.getId());
		return this.getUtility().compareTo(job.getUtility());
	}
    
    public String toString() {
    	  		String str =  "{id:"+ id+";description:"+description+
          ";locator:"+getLocator()+
               ";time:"+estTime+";skill:("+
          (skillTypeRef==null?"":skillTypeRef.getId());
        str += ","+(skillLevelRef==null?"":skillLevelRef.getValue())+
          ");utility:"+util+";status:"+getStatus()+ "}";

        return str;
      }
}
