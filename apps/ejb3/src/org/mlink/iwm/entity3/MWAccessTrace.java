package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MW_ACCESS_TRACE")
@SequenceGenerator(name="MW_ACCESS_TRACE_SEQ_GEN", sequenceName="MW_ACCESS_TRACE_SEQ",allocationSize = 1)
public class MWAccessTrace implements Serializable, BaseEntity {
	//private static final Logger logger = Logger.getLogger(MWAccessTrace.class);

	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "MW_ACCESS_TRACE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long accuracy){ this.id = accuracy; }

	private String userName;
    private Integer accuracy;
	private String schedule;
	private Date accessTime;
	private Double latitude;
	private Double longitude;
	private Integer accessTypeId;	
	private Long jobTaskId;
	
	@Column(name="ACCESS_TIME")
	@OrderBy
	@Temporal(TemporalType.TIMESTAMP)
    public Date getAccessTime(){
		return accessTime;
	}
	public void setAccessTime(Date accessTime){
		this.accessTime = accessTime;
	}
	
	@Column(name="USER_NAME", nullable = false)
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="ACCURACY")
	public Integer getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}
	
	@Column(name="SCHEDULE")
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	@Column(name="LATITUDE")
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Column(name="LONGITUDE")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Column(name="ACCESS_TYPE_ID")
	public Integer getAccessTypeId() {
		return accessTypeId;
	}
	public void setAccessTypeId(Integer accessTypeId) {
		this.accessTypeId = accessTypeId;
	}
	
	@Column(name="JOB_TASK_ID")
	public Long getJobTaskId() {
		return jobTaskId;
	}
	public void setJobTaskId(Long jobTaskId) {
		this.jobTaskId = jobTaskId;
	}
}