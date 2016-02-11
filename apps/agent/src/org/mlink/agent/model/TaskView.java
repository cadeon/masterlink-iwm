package org.mlink.agent.model;

import java.sql.Date;
import java.util.Set;


/**
 *        @hibernate.class 
 *         table="TASKVIEW"
 *         mutable="false"
 *         
 *     
 */
public class TaskView {

    // Fields    
  private String description;
  private String groupDescription;
  private Double runHours;
  private Long scheduleGroup;
  private Long sequenceGroup;
  private Integer sequenceLevel;
	private Long id;
	private Integer active;
	private String plan;
	private String meterRule;
	private Date lastServicedDate;
	private Double runHoursThreshold;
	private Integer freqMonths;
	private Integer numberWorkers;
	private Integer estimatedTime;
	private Date lastPlannedDate;
	private Integer freqDays;
	private TaskTypeRef taskTypeRef;
	private Long workObjectId;
	private Long organizationId;
	private PriorityRef priorityRef;
	private SkillLevelRef skillLevelRef;
	private SkillTypeRef skillTypeRef;
    private Locator locator;
    private Long expiryTypeId;
    private String expiryType;
    private Long expiryNumOfDays;    

	private Set<Action>   actions;
	private Set<TaskView> preceedingTasks;
	private Set<TaskView> followingTasks;
	
     // Constructors

    /** default constructor */
    public TaskView() {
    }

	/** minimal constructor */
    public TaskView(Long id) {
        this.id=id;
    }
    
   
    // Property accessors
    /**       
     *             @hibernate.id
     *             generator-class="assigned"
     *             column="OBJECT_ID"
     */
    public Long  getId() {return this.id;}
    public void setId(Long id) {this.id=id;}
    /**
     * 	          @hibernate.property
     *             column="ACTIVE"
     */
    public Integer getActive() {return this.active;}
    public void setActive(Integer i){this.active=i;}
    /**       
     *            @hibernate.property
     *             column="PLAN"
     */
    public String getPlan() {return this.plan;}
    public void setPlan(String plan) {this.plan=plan;}
    /**       
     *             @hibernate.property
     *             column="METERRULE"
     */
    public String getMeterRule() {return this.meterRule;}
    public void setMeterRule(String meterRule) {this.meterRule=meterRule;}
    /**       
     *           @hibernate.property
     *             column="DESCRIPTION"
     */
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
    /**       
     *           @hibernate.property
     *             column="GROUPDESCRIPTION"
     */
    public String getGroupDescription() {return this.groupDescription;}
    public void setGroupDescription(String groupDescription) {this.groupDescription = groupDescription;}
    /**       
     *            @hibernate.property
     *             column="LASTSERVICED"
     */
    public java.sql.Date getLastServiced() {return this.lastServicedDate;}
    public void setLastServiced(java.sql.Date date) {this.lastServicedDate=date;}
    /**       
     *             @hibernate.property
     *             column="RUNHOURS"
     */
    public Double getRunHours() {return this.runHours;}
    public void setRunHours(Double runHours) {this.runHours = runHours;}
    /**       
     *             @hibernate.property
     *             column="THRESHOLD"
     */
    public Double getThreshold() {return this.runHoursThreshold;} 
    public void setThreshold(Double d) {this.runHoursThreshold=d;}
    /**       
     *             @hibernate.property
     *             column="MONTHS"
     */
    public Integer getMonths() {return this.freqMonths;}
    public void setMonths(Integer i) {this.freqMonths=i;}
    /**
     *            @hibernate.property
     *            column="NUMBERWORKERS"
     */
    public Integer getNumberWorkers() {return this.numberWorkers;}
    public void    setNumberWorkers(Integer i) {this.numberWorkers=i;}
    /**       
     *             @hibernate.property
     *             column="ESTIMATEDTIME"
     */
    public Integer getEstimatedTime() {return this.estimatedTime;}
    public void setEstimatedTime(Integer i) {this.estimatedTime=i;}
    /**       
     *             @hibernate.property
     *             column="LASTPLANNED"
     */
    public java.sql.Date getLastPlanned() {return this.lastPlannedDate;}
    public void setLastPlanned(java.sql.Date date) {this.lastPlannedDate=date;}
    /**       
     *            @hibernate.property
     *             column="DAYS"
     */
    public Integer getDays() {return this.freqDays;}
    public void setDays(Integer i) {this.freqDays=i;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             not-null="true"
     *            @hibernate.column name="TASKTYPE"
     */
    public TaskTypeRef getTaskTypeRef() {return this.taskTypeRef;}
    public void setTaskTypeRef(TaskTypeRef ttr) {this.taskTypeRef=ttr;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="TARGET"         
     */
    public Long getWorkObjectId() {return this.workObjectId;}
    public void setWorkObjectId(Long object) {this.workObjectId=object;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="ORGANIZATION_ID"         
     *         
     */
    public Long getOrganizationId() {return this.organizationId;}
    public void setOrganizationId(Long lid) {this.organizationId=lid;}
    /**       
     *            @hibernate.property   
     *            @hibernate.column name="SEQUENCEGROUP"    
     *         
     */
    public Long getSequenceGroupId() {return this.sequenceGroup;}
    public void setSequenceGroupId(Long lo) {this.sequenceGroup = lo;}
    /**       
     *            @hibernate.property
     *            @hibernate.column name="SEQUENCELEVEL" 
     *         
     */
    public Integer getSequenceLevel() {return this.sequenceLevel;}
    public void setSequenceLevel(Integer i) {this.sequenceLevel = i;}
    
    /**       
     *            @hibernate.property   
     *            @hibernate.column name="EXPIRYTYPEID"    
     *         
     */
    public Long getExpiryTypeId() {return this.expiryTypeId;}
    public void setExpiryTypeId(Long lo) {this.expiryTypeId = lo;}
    
    /**       
     *            @hibernate.property   
     *            @hibernate.column name="EXPIRYTYPE"    
     *         
     */
    public String getExpiryType() {return this.expiryType;}
    public void setExpiryType(String lo) {this.expiryType = lo;}
    
    /**       
     *            @hibernate.property   
     *            @hibernate.column name="EXPIRYNUMOFDAYS"    
     *         
     */
    public Long getExpiryNumOfDays() {return this.expiryNumOfDays;}
    public void setExpiryNumOfDays(Long lo) {this.expiryNumOfDays = lo;}
    
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             not-null="true"
     *            @hibernate.column name="SKILLTYPE"         
     *         
     */
    public SkillTypeRef getSkillTypeRef() {return this.skillTypeRef;}
    public void setSkillTypeRef(SkillTypeRef str) {this.skillTypeRef=str;}
    /**       
     *           @hibernate.many-to-one
     *             lazy="false"
     *             not-null="true"
     *            @hibernate.column name="PRIORITY"         
     *         
     */
    public PriorityRef getPriorityRef() {return this.priorityRef;}
    public void setPriorityRef(PriorityRef pr) {this.priorityRef=pr;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             not-null="true"
     *            @hibernate.column name="SKILLLEVEL"         
     *         
     */
    public SkillLevelRef getSkillLevelRef() {return this.skillLevelRef;}
    public void setSkillLevelRef(SkillLevelRef skr) {this.skillLevelRef=skr;} 
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             not-null="true"
     *            @hibernate.column name="LOCATOR_ID"         
     */
    public Locator getLocator() {return this.locator;}
    public void setLocator(Locator locator) {this.locator=locator;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="SCHEDULEGROUP"         
     *         
     */
    public Long getScheduleGroup()              {return this.scheduleGroup;}
    public void setScheduleGroup(Long taskGroup){this.scheduleGroup=taskGroup;}
    /**
     * FIXME: Fix annotations to return tasks with sequenceLevel < this.sequenceLevel
     *            @hibernate.set
     *             lazy="false"
     *             inverse="false"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="SEQUENCEGROUP"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.TaskView"
     *         
     */
    public Set getPreceedingTasks(){return this.preceedingTasks;}
    public void setPreceedingTasks(Set<TaskView> preceeds){this.preceedingTasks=preceeds;}
    /**
     * FIXME: Fix annotations to return tasks with sequenceLevel >= this.sequenceLevel
     *            @hibernate.set
     *             lazy="false"
     *             inverse="false"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="SEQUENCEGROUP"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.TaskView"
     *         
     */
    public Set getFollowingTasks(){return this.followingTasks;}
    public void setFollowingTasks(Set<TaskView> follows	){this.followingTasks=follows;}
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="false"
     *            @hibernate.collection-key
     *             column="TASK_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.Action"
     *         
     */
    public Set getActions() {return this.actions;}
    public void setActions(Set jobTasks) {this.actions = jobTasks;}
    
    public String toString() {
      return  " id:"+id + "description:"+description + 
        " workObjectId:"+workObjectId + 
        " skillType:" + skillTypeRef==null?"":skillTypeRef +
          " skillLevel:" + skillLevelRef==null?"":skillLevelRef +
        " priority:"+priorityRef==null?"":priorityRef +  " runHours:"+runHours + 
        " scheduleGroup:"+scheduleGroup + " sequenceGroup:"+sequenceGroup + 
        " sequenceLevel:"+sequenceLevel + " active:"+active + 
        " expiryTypeId:"+expiryTypeId + " expiryType:"+expiryType+
        " expiryNumOfDays:"+ expiryNumOfDays+
        " plan:"+ plan+" actions:"+actions;
    }
}