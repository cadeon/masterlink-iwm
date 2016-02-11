/*-----------------------------------------------------------------------------------
	File: Task.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.mlink.iwm.lookup.ScheduleResponsibilityRef;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.EqualsUtils;

@Entity
@Table(name="TASK")
@SequenceGenerator(name="TASK_SEQ_GEN", sequenceName="TASK_SEQ", allocationSize = 1)
public class Task implements Serializable,BaseEntity  {
	private static final Logger logger = Logger.getLogger(Task.class);

	public long id;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TASK_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	public Task() {
	}
	public Task(Long id) {
		this.id = id;
	}
	
	public Task(TaskDefinition td){
		setCustom(Constants.CUSTOMIZED_NO);
        setTaskDefinition(td);
        setPlan(td.getObjectDefinition().getPlan());

        //default schedule by is System: see BugTracker bug 12. andrei 08/08/05
        setScheduleResponsibilityId(ScheduleResponsibilityRef.getIdByCode(ScheduleResponsibilityRef.SYSTEM));

        synchronize();
        setWorkerTypeId(td.getWorkerTypeId()); //this property can be overriden weithout setting task custom
        //create actions from action defs
        Collection actionDefs = td.getActionDefs(), taskActionDefs = new HashSet();
        for (Object actionDef : actionDefs) {
            ActionDefinition ad = (ActionDefinition) actionDef;
            inherit(ad);
        }
        setStartDate(new java.sql.Date(System.currentTimeMillis()));
        setActive(Constants.STATUS_ACTIVE);   
    }
	
	private Integer estTime;
	private Date startDate;
	private Long organizationId;
	private Double runHoursThresh;
	private Double runHoursThreshInc;
	private Integer taskTypeId;
	private Integer priorityId;
	private Integer workerTypeId;
	private Integer active;
	private Integer skillTypeId;
	private Integer skillLevelId;
	private Integer numberOfWorkers;
	private Integer freqMonths;
	private Integer freqDays;
	private Integer frequencyId;
	private String taskDescription;
	private Date lastServicedDate;
	private Date lastPlannedDate;
	private Date lastScheduledDate;
	private Integer scheduleResponsibilityId;
	private String meterRule;
	private String plan;
	private Integer custom;
	private Date archivedDate;
	private TaskDefinition taskDefinition;
	private Collection<Action> actions;
	private TaskGroup taskGroup;
	private ObjectEntity object;
	private List<TaskSequence> taskSequences;
	private Collection<JobTask> jobTasks;
	private Integer expiryNumOfDays;
	private Integer expiryTypeId;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="task")
    public Collection<JobTask> getJobTasks() {
		return jobTasks;
	}
	public void setJobTasks(Collection<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}	
	
	@Column(name="ESTIMATED_TIME")
	public Integer getEstTime(){
		return estTime;
	}
	public void setEstTime(Integer estTime){
		this.estTime = estTime;
	}

	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartDate(){
		return startDate;
	}
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	@Column(name="ORGANIZATION_ID")
	public Long getOrganizationId(){
		return organizationId;
	}
	public void setOrganizationId(Long organizationId){
		this.organizationId = organizationId;
	}

	@Column(name="RUN_HOURS_THRESHOLD")
	public Double getRunHoursThresh(){
		return runHoursThresh;
	}
	public void setRunHoursThresh(Double runHoursThresh){
		this.runHoursThresh = runHoursThresh;
	}

	@Column(name="RUN_HOURS_THRESHOLD_INCREMENT")
	public Double getRunHoursThreshInc(){
		return runHoursThreshInc;
	}
	public void setRunHoursThreshInc(Double runHoursThreshInc){
		this.runHoursThreshInc = runHoursThreshInc;
	}

	@Column(name="TASK_TYPE_ID")
	public Integer getTaskTypeId(){
		return taskTypeId;
	}
	public void setTaskTypeId(Integer taskTypeId){
		this.taskTypeId = taskTypeId;
	}

	@Column(name="PRIORITY_ID")
	public Integer getPriorityId(){
		return priorityId;
	}
	public void setPriorityId(Integer priorityId){
		this.priorityId = priorityId;
	}

	@Column(name="WORKER_TYPE_ID")
	public Integer getWorkerTypeId(){
		return workerTypeId;
	}
	public void setWorkerTypeId(Integer workerTypeId){
		this.workerTypeId = workerTypeId;
	}

	@Column(name="ACTIVE")
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	@Column(name="SKILL_TYPE_ID")
	public Integer getSkillTypeId() {
		return skillTypeId;
	}
	public void setSkillTypeId(Integer skillTypeId) {
		this.skillTypeId = skillTypeId;
	}
	
	@Column(name="SKILL_LEVEL_ID")
	public Integer getSkillLevelId() {
		return skillLevelId;
	}
	public void setSkillLevelId(Integer skillLevelId) {
		this.skillLevelId = skillLevelId;
	}
	
	@Column(name="NUMBER_WORKERS")
	public Integer getNumberOfWorkers() {
		return numberOfWorkers;
	}
	public void setNumberOfWorkers(Integer numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}
	
	@Column(name="FREQ_MONTHS")
	public Integer getFreqMonths() {
		return freqMonths;
	}
	public void setFreqMonths(Integer freqMonths) {
		this.freqMonths = freqMonths;
	}
	
	@Column(name="FREQ_DAYS")
	public Integer getFreqDays() {
		return freqDays;
	}
	public void setFreqDays(Integer freqDays) {
		this.freqDays = freqDays;
	}
	
	@Column(name="FREQUENCY_ID")
	public Integer getFrequencyId() {
		return frequencyId;
	}
	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}
	
	@Column(name="DESCRIPTION")
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
	@Column(name="LAST_SERVICED_DATE")
	public Date getLastServicedDate() {
		return lastServicedDate;
	}
	public void setLastServicedDate(Date lastServicedDate) {
		this.lastServicedDate = lastServicedDate;
	}
	
	@Column(name="LAST_PLANNED_DATE")
	public Date getLastPlannedDate() {
		return lastPlannedDate;
	}
	public void setLastPlannedDate(Date lastPlannedDate) {
		this.lastPlannedDate = lastPlannedDate;
	}
	
	@Column(name="LAST_SCHEDULED_DATE")
	public Date getLastScheduledDate() {
		return lastScheduledDate;
	}
	public void setLastScheduledDate(Date lastScheduledDate) {
		this.lastScheduledDate = lastScheduledDate;
	}
	
	@Column(name="SCHEDULE_RESPONSIBILITY_ID")
	public Integer getScheduleResponsibilityId() {
		return scheduleResponsibilityId;
	}
	public void setScheduleResponsibilityId(Integer scheduleResponsibilityId) {
		this.scheduleResponsibilityId = scheduleResponsibilityId;
	}
	
	@Column(name="METER_RULE")
	public String getMeterRule() {
		return meterRule;
	}
	public void setMeterRule(String meterRule) {
		this.meterRule = meterRule;
	}
	
	@Column(name="PLAN")
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	@Column(name="CUSTOM")
	public Integer getCustom() {
		return custom;
	}
	public void setCustom(Integer custom) {
		this.custom = custom;
	}
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="EXPIRY_NUMOFDAYS")
	public Integer getExpiryNumOfDays() {
		return expiryNumOfDays;
	}
	public void setExpiryNumOfDays(Integer expiryNumOfDays) {
		this.expiryNumOfDays = expiryNumOfDays;
	}
	
	@Column(name="EXPIRY_TYPE_ID")
	public Integer getExpiryTypeId() {
		return expiryTypeId;
	}
	public void setExpiryTypeId(Integer expiryTypeId) {
		this.expiryTypeId = expiryTypeId;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TASK_DEF_ID")
    public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}
	public void setTaskDefinition(TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="task")
    public Collection<Action> getActions() {
		return actions;
	}
	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="task")
	@OrderBy("sequenceLevel")
    public List<TaskSequence> getTaskSequences() {
		return taskSequences;
	}
	public void setTaskSequences(List<TaskSequence> taskSequences) {
		this.taskSequences = taskSequences;
	}
	
	@ManyToOne()
	@JoinColumn(name = "GROUP_ID")
    public TaskGroup getTaskGroup() {
		return taskGroup;
	}
	public void setTaskGroup(TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_ID")
    public ObjectEntity getObject() {
		return object;
	}
	public void setObject(ObjectEntity object) {
		this.object = object;
	}
	
	//KEEPMESTART
	/**
     * Sync up properties with the TaskDefinition
     * Should be called of the TaskDefinition change
     */
    public void synchronize(){
        if(Constants.CUSTOMIZED_YES.equals(getCustom()))
            return; // custom instances are not logically linked to their definitions

        logger.debug("synchronize Task " + getId());

        TaskDefinition td = getTaskDefinition();
        /** andrei 03/09/05 commenting as v1 to v2 migration shows massive differecnes btw objectDefPlan and Task plans
         * causing 90% of tasks become custom. Plan is no longer customizable property per Garry/Douglas input
        setPlan(td.getObjectDefinition().getPlan());
         */
        setEstTime(td.getEstTime());
        //setStartDate(td.getStartDate()); not in CustomizationVisitor
        //setRunHoursThresh(td.getRunHoursThresh()); is not TaskDefinition property
        setRunHoursThreshInc(td.getRunHoursThreshInc());

        //if task type is changing need to check if RoutineType which has special treatment
        if(!EqualsUtils.areEqual(td.getTaskTypeId(),getTaskTypeId())){
            //task type property is changed
            setTaskTypeId(td.getTaskTypeId());
        }

        setPriorityId(td.getPriorityId());
        setSkillTypeId(td.getSkillTypeId());
        setSkillLevelId(td.getSkillLevelId());
        setNumberOfWorkers(td.getNumberOfWorkers());
        setFreqMonths(td.getFreqMonths());
        setFreqDays(td.getFreqDays());
        setFrequencyId(td.getFrequencyId());
        setTaskDescription(td.getTaskDescription());
        
        setExpiryTypeId(td.getExpiryTypeId());
        setExpiryNumOfDays(td.getExpiryNumOfDays());
    }

    /**
     * Non-Custom tasks follow their definitions as they change group memebership
     * Find task group for object the current task belongs to and if available move the task into the task group
     */
    public void synchronizeGroupMembership(Collection <TaskGroup> taskGroups){
        if(Constants.CUSTOMIZED_YES.equals(getCustom()))
            return; // custom instances are not logically linked to their definitions
        logger.debug("in synchronizeGroupMembership for Task " + getTaskDescription() + " id="+ getId());
        TaskDefinition td = getTaskDefinition();
        if(td.getTaskGroupDef()==null) {
            setTaskGroup(null);
        }else{
        	Long taskGroupDefinitionId  = td.getTaskGroupDef().getId();
            for (TaskGroup taskGroup : taskGroups) {
                if (taskGroupDefinitionId.equals(taskGroup.getTaskGroupDef().getId())) {
                    setTaskGroup(taskGroup);
                    break;
                }
            }
        }
    }
    
    public void inherit(ActionDefinition actionDef){
        Action action = new Action(actionDef);
        action.setTask(this);
        Collection<Action> actions = this.getActions();
        if(actions == null){
        	actions = new HashSet<Action>();
        	this.setActions(actions);
        }
        actions.add(action);
    }

	//KEEPMEEND
}