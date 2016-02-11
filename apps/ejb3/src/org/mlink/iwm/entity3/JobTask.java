package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;

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

import org.apache.log4j.Logger;

@Entity
@Table(name="JOB_TASK")
@SequenceGenerator(name="JOB_TASK_SEQ_GEN", sequenceName="JOB_TASK_SEQ",allocationSize = 1)
public class JobTask implements Serializable,BaseEntity  {
	private static final Logger logger = Logger.getLogger(JobTask.class);
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JOB_TASK_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	private Task task;
	private Long totalTime;
	private Integer skillTypeId;
	private Integer priorityId;
	private Integer taskTypeId;
   	private Integer numberOfWorkers;
   	private String taskDescription;
   	private Integer estTime;
   	private Integer skillLevelId;
   	private Integer taskInProgress;
   	
   	private Job job;
    private Collection<JobTaskTime> jobTaskTimes;
    private Collection<JobAction> actions;
    
    public JobTask(){};
    public JobTask(long id){
    	this.id = id;
    }
	
	@ManyToOne()
	@JoinColumn(name = "TASK_ID")
	public Task getTask()
	{
		return task;
	}
	public void setTask(Task task)
	{
		this.task = task;
	}

	@ManyToOne()
	@JoinColumn(name = "JOB_ID")
	public Job getJob()
	{
		return job;
	}
	public void setJob(Job job)
	{
		this.job = job;
	}

	@Column(name="TASK_IN_PROGRESS")
	public Integer getTaskInProgress() {
		return taskInProgress;
	}
	public void setTaskInProgress(Integer taskInProgress) {
		this.taskInProgress = taskInProgress;
	}
	
	@Column(name="TOTAL_TIME")
	public Long getTotalTime(){
		return totalTime;
	}
	public void setTotalTime(Long totalTime){
    	this.totalTime = totalTime;
    }

    @Column(name="SKILL_TYPE_ID")
    public Integer getSkillTypeId(){
    	return skillTypeId;
    }
    public void setSkillTypeId(Integer skillTypeId){
    	this.skillTypeId = skillTypeId;
    }

    @Column(name="PRIORITY_ID")
    public Integer getPriorityId(){
    	return priorityId;
    }
    public void setPriorityId(Integer priorityId){
    	this.priorityId = priorityId;
    }

    @Column(name="TASK_TYPE_ID")
    public Integer getTaskTypeId(){
    	return taskTypeId;
    }
    public void setTaskTypeId(Integer taskTypeId){
    	this.taskTypeId = taskTypeId;
    }

    @Column(name="NUMBER_WORKERS")
    public Integer getNumberOfWorkers(){
    	return numberOfWorkers;
    }
    public void setNumberOfWorkers(Integer numberOfWorkers){
    	this.numberOfWorkers = numberOfWorkers;
    }

    @Column(name="DESCRIPTION")
    public String getTaskDescription(){
    	return taskDescription;
    }
    public void setTaskDescription(String taskDescription){
    	this.taskDescription = taskDescription;
    }

    @Column(name="ESTIMATED_TIME")
    public Integer getEstTime(){
    	return estTime;
    }
    public void setEstTime(Integer estTime){
    	this.estTime = estTime;
    }

    @Column(name="SKILL_LEVEL_ID")
    public Integer getSkillLevelId(){
    	return skillLevelId;
    }
    public void setSkillLevelId(Integer skillLevelId){
    	this.skillLevelId = skillLevelId;
    }

	 @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="jobTask")
	 public Collection<JobTaskTime> getJobTaskTimes() {
		 return jobTaskTimes;
	 }
	 public void setJobTaskTimes(Collection<JobTaskTime> jobTaskTimes) {
		 this.jobTaskTimes = jobTaskTimes;
	 }
	
	 @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="jobTask")
	 @OrderBy("sequence")
	 public Collection<JobAction> getActions() {
		 return actions;
	 }
	 public void setActions(Collection<JobAction> actions) {
		 this.actions = actions;
	 }
}