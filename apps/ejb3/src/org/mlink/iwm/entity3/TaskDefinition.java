/*-----------------------------------------------------------------------------------
	File: TaskDefinition.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="TASK_DEF")
@SequenceGenerator(name="TASK_DEF_SEQ_GEN", sequenceName="TASK_DEF_SEQ",allocationSize = 1)
public class TaskDefinition implements Serializable,BaseEntity  {
	public TaskDefinition(){}
	public TaskDefinition(Long id){
		this.id = id;
	}
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TASK_DEF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Integer estTime;
	private Double runHoursThreshInc;
	private Integer taskTypeId;
	private Integer priorityId;
	private Integer workerTypeId;
	private Integer skillTypeId;
	private Integer skillLevelId;
	private Integer numberOfWorkers;
	private Integer freqMonths;
	private Integer freqDays;
	private Integer frequencyId;
	private String taskDescription;
	public Date archivedDate;
	private Integer expiryTypeId;
	private Integer expiryNumOfDays;
	
	public Collection<ActionDefinition> actionDefs;
	public Collection<Task> tasks;
	public TaskGroupDefinition taskGroupDef;
	public ObjectDefinition objectDefinition;
	
	@Column(name="ESTIMATED_TIME")
    public Integer getEstTime() {
		return estTime;
	}
	public void setEstTime(Integer estTime) {
		this.estTime = estTime;
	}
	
	@Column(name="RUN_HOURS_THRESHOLD_INCREMENT")
    public Double getRunHoursThreshInc() {
		return runHoursThreshInc;
	}
	public void setRunHoursThreshInc(Double runHoursThreshInc) {
		this.runHoursThreshInc = runHoursThreshInc;
	}
	
	@Column(name="TASK_TYPE_ID")
    public Integer getTaskTypeId() {
		return taskTypeId;
	}
	public void setTaskTypeId(Integer taskTypeId) {
		this.taskTypeId = taskTypeId;
	}
	
	@Column(name="PRIORITY_ID")
    public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	
	@Column(name="WORKER_TYPE_ID")
    public Integer getWorkerTypeId() {
		return workerTypeId;
	}
	public void setWorkerTypeId(Integer workerTypeId) {
		this.workerTypeId = workerTypeId;
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
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="taskDefinition")
	public Collection<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Collection<Task> tasks) {
		this.tasks = tasks;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="taskDefinition")
	public Collection<ActionDefinition> getActionDefs() {
		return actionDefs;
	}
	public void setActionDefs(Collection<ActionDefinition> actionDefs) {
		this.actionDefs = actionDefs;
	}
	
	@ManyToOne()
	@JoinColumn(name = "GROUP_ID")
    public TaskGroupDefinition getTaskGroupDef() {
		return taskGroupDef;
	}
	public void setTaskGroupDef(TaskGroupDefinition taskGroupDef) {
		this.taskGroupDef = taskGroupDef;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_DEF_ID")
    public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}
	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}
}