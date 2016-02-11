/*-----------------------------------------------------------------------------------
	File: JobTaskTime.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="JOB_TASK_TIME")
@SequenceGenerator(name="JOB_TASK_TIME_SEQ_GEN", sequenceName="JOB_TASK_TIME_SEQ",allocationSize = 1)
public class JobTaskTime implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JOB_TASK_TIME_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Long jobScheduleId;
	private Integer time;
	private JobTask jobTask;
	
	@Column(name="JOB_SCHEDULE_ID")
	public Long getJobScheduleId() {
		return jobScheduleId;
	}
	public void setJobScheduleId(Long jobScheduleId) {
		this.jobScheduleId = jobScheduleId;
	}
	
	@Column(name="TIME")
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	
	@ManyToOne()
	@JoinColumn(name = "JOB_TASK_ID")
    public JobTask getJobTask() {
		return jobTask;
	}
	public void setJobTask(JobTask jobTask) {
		this.jobTask = jobTask;
	}
}