/*-----------------------------------------------------------------------------------
	File: JobSchedule.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.PolicySF;

@Entity
@Table(name="JOB_SCHEDULE")
@SequenceGenerator(name="JOB_SCHEDULE_SEQ_GEN", sequenceName="JOB_SCHEDULE_SEQ",allocationSize = 1)
public class JobSchedule implements Serializable,BaseEntity  {
	public JobSchedule(){
	}
	
	public JobSchedule(WorkSchedule ws, Job job)   {
		PolicySF policy = ServiceLocator.getPolicySFLocal();
        
        setJobId(job.getId());
        setWorkScheduleId(ws.getId());
        policy.signUser(this);
        setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
    }
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JOB_SCHEDULE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Long jobId;
	private Long workScheduleId;
	private String user;
	private Date createdTime; 
	private Date deletedTime;
	
	@Column(name="JOB_ID")
    public Long getJobId(){
		return jobId;
	}
	public void setJobId(Long jobId){
		this.jobId = jobId;
	}

	@Column(name="WORK_SCHEDULE_ID")
    public Long getWorkScheduleId(){
		return workScheduleId;
	}
	public void setWorkScheduleId(Long workScheduleId){
		this.workScheduleId = workScheduleId;
	}

	@Column(name="USR")
    public String getUser(){
		return user;
	}
	public void setUser(String user){
		this.user = user;
	}

	@Column(name="CREATED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime(){
		return this.createdTime;
	}
	public void setCreatedTime(Date createdTime){
    	this.createdTime = createdTime;
    }

    //void signUser() ;

    @Column(name="DELETED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getDeletedTime(){
    	return deletedTime;
    }
    public void setDeletedTime(Date deletedTime){
    	this.deletedTime = deletedTime;
    }
}