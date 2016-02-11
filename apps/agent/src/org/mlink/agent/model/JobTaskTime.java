package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="JOB_TASK_TIME"
 *     
 */
public class JobTaskTime  implements java.io.Serializable {

    // Fields    
     private Long id;
     private Integer time;
     private JobSchedule jobSchedule;
     private JobTask jobTask;

     // Constructors
    public JobTaskTime() {}
    public JobTaskTime(Long id) {this.id = id;}
    
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="TIME"
     */
    public Integer getTime() {return this.time;}
    public void setTime(Integer time) {this.time = time;}
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="JOB_SCHEDULE_ID"
     */
    public JobSchedule getJobSchedule() {return this.jobSchedule;}
    public void setJobSchedule(JobSchedule jobSchedule) {this.jobSchedule = jobSchedule;}
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="JOB_TASK_ID"
     */
    public JobTask getJobTask() {return this.jobTask;}
    public void setJobTask(JobTask jobTask) {this.jobTask = jobTask;}
}


