/*-----------------------------------------------------------------------------------
	File: WorkSchedule.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Transient;

@Entity
@Table(name="WORK_SCHEDULE")
@SequenceGenerator(name="WORK_SCHEDULE_SEQ_GEN", sequenceName="WORK_SCHEDULE_SEQ",allocationSize = 1)
public class WorkSchedule implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "WORK_SCHEDULE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

    private Double utilityRating;
    private Integer statusId;
    private Long personId;
    private Long locatorId;
    private Integer time;
    private Long notschedulable;
    private Date day;
    private Integer shiftId;
    private Date archivedDate;
    
    //from VO donot save it
    private Collection<Job> jobs;
    private String location;
    
	@Column(name="UTILITY_RATING")
	public java.lang.Double getUtilityRating(){
		return utilityRating;
	}
	public void setUtilityRating(java.lang.Double utilityRating){
		this.utilityRating = utilityRating;
	}

	@Column(name="STATUS_ID")
	public java.lang.Integer getStatusId(){
		return statusId;
	}
	public void setStatusId(java.lang.Integer statusId){
		this.statusId = statusId;
	}

	@Column(name="PERSON_ID")
	public java.lang.Long getPersonId(){
		return personId;
	}
	public void setPersonId(java.lang.Long personId){
		this.personId = personId;
	}

	@Column(name="LOCATOR_ID")
	public java.lang.Long getLocatorId(){
		return locatorId;
	}
	public void setLocatorId(java.lang.Long locatorId){
		this.locatorId = locatorId;
	}

	@Column(name="TIME")
	public java.lang.Integer getTime(){
		return time;
	}
	public void setTime(java.lang.Integer time){
		this.time = time;
	}

	@Column(name="NOTSCHEDULABLE")
	public java.lang.Long getNotschedulable(){
		return notschedulable;
	}
	public void setNotschedulable(java.lang.Long notschedulable){
		this.notschedulable = notschedulable;
	}

	@Column(name="DAY")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDay(){
		return day;
	}
	public void setDay(Date day){
		this.day = day;
	}

	@Column(name="SHIFT_ID")
	public java.lang.Integer getShiftId(){
		return shiftId;
	}
	public void setShiftId(java.lang.Integer shiftId){
		this.shiftId = shiftId;
	}

	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate(){
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate){
		this.archivedDate = archivedDate;
	}
	
	@Transient
	public Collection<Job> getJobs() {
		return jobs;
	}
	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}
	
	@Transient
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}