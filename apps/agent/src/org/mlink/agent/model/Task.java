package org.mlink.agent.model;
// Generated May 27, 2006 12:59:46 PM by Hibernate Tools 3.1.0.beta5

import java.sql.Date;
import java.sql.Timestamp;


/**
 *        @hibernate.class
 *         table="TASK"
 *     
 */
public class Task {

	private String description;
	private Long id; 
	private Date lastServicedDate;
	private String meterRule;
	private String plan;
	private Date startDate;
	private Integer numberOfWorkers;
	private Double runHoursThresh;
	private Integer active;
	private Integer freqMonths;
	private Integer estTime;
	private Date lastPlannedDate;
	private Date lastScheduledDate;
	private Integer freqDays;
	private Double runHoursThreshInc;
	private Integer custom;
	private Timestamp archivedDate;
	private Integer taskTypeId;
	private Long taskDefId;
	private Long objectId;
	private Integer frequencyId;
	private Long organizationId;
	private Integer skillTypeId;
	private Integer priorityId;
	private Integer workerTypeId;
	private Integer skillLevelId;
	private Integer scheduleResponsibilityId;
	private Long groupId;
	
	
	
     // Constructors

    /** default constructor */
    public Task() {}

	/** minimal constructor */
    public Task(Long id) {id=this.id;}    
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id=id;}
    /**       
     *            @hibernate.property
     *             column="PLAN"
     *             length="150"
     *         
     */
    public String getPlan() {return this.plan;}
    public void setPlan(String plan) {this.plan=plan;}
    /**       
     *            @hibernate.property
     *             column="METER_RULE"
     *             length="150"
     *         
     */
    public String getMeterRule() {return this.meterRule;}
    public void setMeterRule(String meterRule) {this.meterRule=meterRule;}
    /**       
     *            @hibernate.property
     *             column="START_DATE"
     *             length="7"
     *         
     */
    public java.sql.Date getStartDate() {return this.startDate;}
    public void setStartDate(java.sql.Date startDate) {this.startDate=startDate;}
    /**       
     *            @hibernate.property
     *             column="DESCRIPTION"
     *             length="150"
     *         
     */
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description=description;}
    /**       
     *            @hibernate.property
     *             column="LAST_SERVICED_DATE"
     *         
     */
    public java.sql.Date getLastServicedDate() {return this.lastServicedDate;}
    public void setLastServicedDate(java.sql.Date date) {this.lastServicedDate=date;}
    /**       
     *            @hibernate.property
     *             column="NUMBER_WORKERS"
     *         
     */
    public Integer getNumberWorkers() {return this.numberOfWorkers;}
    public void setNumberWorkers(Integer i) {this.numberOfWorkers=i;}
    /**       
     *            @hibernate.property
     *             column="RUN_HOURS_THRESHOLD"
     *         
     */
    public Double getRunHoursThreshold() {return this.runHoursThresh;}
    public void setRunHoursThreshold(Double d) {this.runHoursThresh=d;}
    /**       
     *            @hibernate.property
     *             column="ACTIVE"
     *         
     */
    public Integer getActive() {return this.active;}
    public void setActive(Integer i) {this.active=i;}
    /**       
     *            @hibernate.property
     *             column="FREQ_MONTHS"
     *         
     */
    public Integer getFreqMonths() {return this.freqMonths;}
    public void setFreqMonths(Integer i) {this.freqMonths=i;}
    /**       
     *            @hibernate.property
     *             column="ESTIMATED_TIME"
     *         
     */
    public Integer getEstimatedTime() {return this.estTime;}
    public void setEstimatedTime(Integer i) {this.estTime=i;}
    /**       
     *            @hibernate.property
     *             column="LAST_PLANNED_DATE"
     *         
     */
    public java.sql.Date getLastPlannedDate() {return this.lastPlannedDate;}
    public void setLastPlannedDate(java.sql.Date date) {this.lastPlannedDate=date;}
    /**       
     *            @hibernate.property
     *             column="LAST_SCHEDULED_DATE"
     *         
     */
    public java.sql.Date getLastScheduledDate() {return this.lastScheduledDate;}
    public void setLastScheduledDate(java.sql.Date date) {this.lastScheduledDate=date;}
    /**       
     *            @hibernate.property
     *             column="FREQ_DAYS"
     *         
     */
    public Integer getFreqDays() {return this.freqDays;}
    public void setFreqDays(Integer i) {this.freqDays=i;}
    /**       
     *            @hibernate.property
     *             column="RUN_HOURS_THRESHOLD_INCREMENT"
     *         
     */
    public Double getRunHoursThresholdIncrement() {return this.runHoursThreshInc;}
    public void setRunHoursThresholdIncrement(Double d) {this.runHoursThreshInc=d;}
    /**       
     *            @hibernate.property
     *             column="CUSTOM"
     *         
     */
    public Integer getCustom() {return this.custom;}
    public void setCustom(Integer i) {this.custom=i;}
    /**       
     *            @hibernate.property
     *             column="ARCHIVED_DATE"
     *         
     */
    public Timestamp getArchivedDate() {return this.archivedDate;}
    public void setArchivedDate(Timestamp archivedDate) {this.archivedDate=archivedDate;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="TASK_TYPE_ID"         
     *         
     */
    public Integer getTaskTypeId() {return this.taskTypeId;}
    public void setTaskTypeId(Integer i) {this.taskTypeId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="TASK_DEF_ID"         
     *         
     */
    public Long getTaskDef() {return this.taskDefId;}
    public void setTaskDef(Long taskDef) {this.taskDefId=taskDef;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="FREQUENCY_ID"         
     *         
     */
    public Integer getTaskFrequencyId() {return this.frequencyId;}
    public void setTaskFrequencyId(Integer i) {this.frequencyId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="OBJECT_ID"         
     *         
     */
    public Long getObjectId() {return this.objectId;}
    public void setObjectId(Long oid) {this.objectId=oid;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="ORGANIZATION_ID"         
     *         
     */
    public Long getOrganizationId() {return this.organizationId;}
    public void setOrganizationId(Long lid) {this.organizationId=lid; }
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="SKILL_TYPE_ID"         
     *         
     */
    public Integer getSkillTypeId() {return this.skillTypeId;}
    public void setSkillTypeId(Integer i){this.skillTypeId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="PRIORITY_ID"         
     *         
     */
    public Integer getPriorityId() {return this.priorityId;}
    public void setPriorityId(Integer i) {this.priorityId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="WORKER_TYPE_ID"         
     *         
     */
    public Integer getWorkerTypeId() {return this.workerTypeId;}
    public void setWorkerTypeId(Integer i) {this.workerTypeId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="SKILL_LEVEL_ID"         
     *         
     */
    public Integer getSkillLevelId() {return this.skillLevelId;}
    public void setSkillLevelId(Integer i) {this.skillLevelId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="SCHEDULE_RESPONSIBILITY_ID"         
     *         
     */
    public Integer getScheduleResponsibilityId() {return this.scheduleResponsibilityId;}
    public void setScheduleResponsibilityId(Integer i) {this.scheduleResponsibilityId=i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="GROUP_ID"         
     *         
     */
    public Long getGroupId() {return this.groupId;}
    public void setGroupId(Long lid) {this.groupId=lid;}
    
    
    public String toString() {return this.description;}

}


