package org.mlink.agent.model;
// Generated May 25, 2006 7:06:40 AM by Hibernate Tools 3.1.0.beta5


import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="JOB_TASK"
 *     
 */
public class JobTask  implements java.io.Serializable {

    // Fields    
	private Long id;

    private String  description;
    private Integer estimatedTime;
    private Integer numberWorkers;
    private Integer priorityId;
    private Integer skillLevelId;
    private Integer skillTypeId;
    private Integer taskTypeId;
  	private Job job;
    private Long task;
    
    private Set jobTaskTimes = new HashSet(0);
    private Set jobActions = new HashSet(0);

     // Constructors
    public JobTask() {}
    public JobTask(Long id) {this.id = id;}
    

    
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="sequence"
     *             column="ID"
     *            @hibernate.generator-param
     *             name="sequence"
     *             value="job_task_seq"
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="DESCRIPTION" 
     */
    public String getDescription() {return this.description;}    
    public void setDescription(String s) {this.description = s;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="ESTIMATED_TIME" 
     */
    public Integer getEstimatedTime() {return this.estimatedTime;}    
    public void setEstimatedTime(Integer i) {this.estimatedTime = i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="NUMBER_WORKERS" 
     */
    public Integer getNumberWorkers() {return this.numberWorkers;}    
    public void setNumberWorkers(Integer i) {this.numberWorkers = i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="PRIORITY_ID" 
     */
    public Integer getPriorityId() {return this.priorityId;}    
    public void setPriorityId(Integer i) {this.priorityId = i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="SKILL_LEVEL_ID" 
     */
    public Integer getSkillLevelId() {return this.skillLevelId;}    
    public void setSkillLevelId(Integer i) {this.skillLevelId = i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="SKILL_TYPE_ID" 
     */
    public Integer getSkillTypeId() {return this.skillTypeId;}    
    public void setSkillTypeId(Integer i) {this.skillTypeId = i;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="TASK_TYPE_ID" 
     */
    public Integer getTaskTypeId() {return this.taskTypeId;}    
    public void setTaskTypeId(Integer i) {this.taskTypeId = i;}
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *             column="JOB_ID"
     */
    public Job getJob() {return this.job;}
    public void setJob(Job job) {this.job = job;}
    /**       
     *            @hibernate.property
     *             not-null="true"
     *             column="TASK_ID" 
     */
    public Long getTaskId() {return this.task;}    
    public void setTaskId(Long lid) {this.task = lid;}
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="all"
     *            @hibernate.collection-key
     *             column="JOB_TASK_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.JobTaskTime"
     */
    public Set getJobTaskTimes() {return this.jobTaskTimes;}
    public void setJobTaskTimes(Set jobTaskTimes){this.jobTaskTimes = jobTaskTimes;}
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="all"
     *            @hibernate.collection-key
     *             column="JOB_TASK_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.JobAction"
     *         
     */
    public Set getJobActions() {return this.jobActions;}
    public void setJobActions(Set jobActions) {this.jobActions = jobActions;}
    
    public static JobTask copyProperties(TaskView task) {
    	JobTask jobTask = new JobTask();
        jobTask.setDescription(task.getDescription());
        jobTask.setEstimatedTime(task.getEstimatedTime());
        jobTask.setNumberWorkers(task.getNumberWorkers());
        
        jobTask.setPriorityId(task.getPriorityRef().getId());
        jobTask.setSkillLevelId(task.getSkillLevelRef().getId());
        jobTask.setSkillTypeId(task.getSkillTypeRef().getId());
        jobTask.setTaskTypeId(task.getTaskTypeRef().getId());
        return jobTask;
    }
   
  public String toString() {
      return  "{task_id: "+task+
     " description: "+description +
     " estimatedTime: "+estimatedTime +
     " numberWorkers: "+numberWorkers +
     " priorityId: "+priorityId +
     " skillLevelId: "+skillLevelId +
     " skillTypeId: "+skillTypeId +
        " ACTIONS: "+jobActions+" }";
  }
}


