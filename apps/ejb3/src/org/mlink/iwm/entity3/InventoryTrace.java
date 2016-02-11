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

import org.apache.log4j.Logger;

@Entity
@Table(name="INVENTORY_TRACE")
@SequenceGenerator(name="INVENTORY_TRACE_SEQ_GEN", sequenceName="INVENTORY_TRACE_SEQ",allocationSize = 1)
public class InventoryTrace implements Serializable,BaseEntity  {
	private static final Logger logger = Logger.getLogger(InventoryTrace.class);
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "INVENTORY_TRACE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	//CREATE TABLE INVENTORY_TRACE(ID  NUMBER(38) NOT NULL, OBJECT_DEF_ID NUMBER, OBJECT_ID NUMBER,
    //USER_NAME VARCHAR2(30) NOT NULL, PERSON_ID  NUMBER, INVENTORY_DATE DATE, INVENTORY_AMT INT, JOB_TASK_TIME_ID NUMBER,
	
	private Long objectId;
	private Long objectDefId;
	private Long jobTaskTimeId;
	private String userName;
	private Long personId;
	private Integer inventory;
	private Date inventoryDate;
   	//private String schedule;
   	
   	public InventoryTrace(){};
    public InventoryTrace(long id){
    	this.id = id;
    }
    
	/*@Column(name="SCHEDULE")
    public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}*/
	
    @Column(name = "OBJECT_ID")
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

    @Column(name = "OBJECT_DEF_ID")
    public Long getObjectDefId() {
		return objectDefId;
	}
	public void setObjectDefId(Long objectDefId) {
		this.objectDefId = objectDefId;
	}
	
    @Column(name = "JOB_TASK_TIME_ID")
    public Long getJobTaskTimeId() {
		return jobTaskTimeId;
	}
	public void setJobTaskTimeId(Long jobTaskTimeId) {
		this.jobTaskTimeId = jobTaskTimeId;
	}
	
	@Column(name="INVENTORY_AMT")
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	
	@Column(name="INVENTORY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInventoryDate() {
		return inventoryDate;
	}
	public void setInventoryDate(Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}

	@Column(name = "PERSON_ID")
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
	@Column(name="USER_NAME")
    public String getUserName(){
    	return userName;
    }
    public void setUserName(String userName){
    	this.userName = userName;
    }
}