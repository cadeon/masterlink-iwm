package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name="SHIFT_TRACE")
@SequenceGenerator(name="SHIFT_TRACE_SEQ_GEN", sequenceName="SHIFT_TRACE_SEQ",allocationSize = 1)
public class ShiftTrace implements Serializable,BaseEntity  {
	private static final Logger logger = Logger.getLogger(ShiftTrace.class);
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SHIFT_TRACE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	//ID  NUMBER(38) NOT NULL, SHIFT_ID NUMBER, USER_NAME VARCHAR2(30), " +
	//	" PERSON_ID  NUMBER, SHIFT_START_DATE DATE, SHIFT_STOP_DATE DATE, TIME_ON_BREAKS INT" +
	// CONSTRAINT PK_SHIFTTRACE_ID PRIMARY KEY (ID) ENABLE )
	
	private ShiftRef shiftRef;
	private String userName;
	private Person person;
	private Integer timeOnBreaksInMins;
	private Integer timeOnJobTasks;
	private Date shiftStartDate;
   	private Date shiftStopDate;
   	private String schedule;
   	
   	public ShiftTrace(){};
    public ShiftTrace(long id){
    	this.id = id;
    }
    
	@Column(name="SCHEDULE")
    public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	@Column(name="TIME_ON_JOBTASKS")
	public Integer getTimeOnJobTasks() {
		return timeOnJobTasks;
	}
	public void setTimeOnJobTasks(Integer timeOnJobTasks) {
		this.timeOnJobTasks = timeOnJobTasks;
	}
	@Column(name="TIME_ON_BREAKS")
    public Integer getTimeOnBreaksInMins() {
		return timeOnBreaksInMins;
	}
	public void setTimeOnBreaksInMins(Integer timeOnBreaksInMins) {
		this.timeOnBreaksInMins = timeOnBreaksInMins;
	}
	
	@Column(name="SHIFT_START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
	public Date getShiftStartDate() {
		return shiftStartDate;
	}
	public void setShiftStartDate(Date shiftStartDate) {
		this.shiftStartDate = shiftStartDate;
	}
	
	@Column(name="SHIFT_STOP_DATE")
    @Temporal(TemporalType.TIMESTAMP)
	public Date getShiftStopDate() {
		return shiftStopDate;
	}
	public void setShiftStopDate(Date shiftStopDate) {
		this.shiftStopDate = shiftStopDate;
	}
	
	@ManyToOne()
	@JoinColumn(name = "SHIFT_ID")
	public ShiftRef getShiftRef()
	{
		return shiftRef;
	}
	public void setShiftRef(ShiftRef shiftRef)
	{
		this.shiftRef = shiftRef;
	}

	@ManyToOne()
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson()
	{
		return person;
	}
	public void setPerson(Person person)
	{
		this.person = person;
	}

	@Column(name="USER_NAME")
    public String getUserName(){
    	return userName;
    }
    public void setUserName(String userName){
    	this.userName = userName;
    }
}