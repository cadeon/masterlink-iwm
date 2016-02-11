package org.mlink.agent.model;

import java.sql.Timestamp;

/**
 *        @hibernate.class
 *         table="JOB_SCHEDULE"
 *     
 */
public class JobSchedule  implements java.io.Serializable {

    // Fields    
     private Long id;
     private Job job;
     private WorkSchedule workSchedule;
     private Timestamp    deletedTime;
     private Timestamp    createdTime;
     private String       user;

     // Constructors
    public JobSchedule() {}
    public JobSchedule(Long id) {this.id = id;}
    
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="sequence"
     *             column="ID"
     *            @hibernate.generator-param
     *             name="sequence"
     *             value="job_schedule_seq"
     *         
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**
     * 			  @hibernate.property
     *            @hibernate.column 
     *             name="CREATED_TIME"
     */
    public Timestamp getCreatedTime() {return this.createdTime;}
    public void setCreatedTime(Timestamp t) {this.createdTime=t;}
    /**
     * 			  @hibernate.property
     *            @hibernate.column 
     *             name="DELETED_TIME"
     */
    public Timestamp getDeletedTime() {return this.deletedTime;}
    public void setDeletedTime(Timestamp t) {this.deletedTime=t;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="JOB_ID"
     */
    public Job getJob() {return this.job;}
    public void setJob(Job job) {this.job = job;}
    /**       
     *            @hibernate.property
     *             lazy="false"
     *             column="USR"
     */
    public String getUser() {return this.user;}
    public void setUser(String s) {this.user = s;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="WORK_SCHEDULE_ID"
     */
    public WorkSchedule getWorkSchedule() {return this.workSchedule;}
    public void setWorkSchedule(WorkSchedule workSchedule) {this.workSchedule = workSchedule;}
    
    public String toString(){
    	return "{JobSchedule:: id:"+id+
             "job:"+job+
             ";createdTime:"+createdTime+
             ";deletedTime:"+deletedTime+"}";
    }
}


