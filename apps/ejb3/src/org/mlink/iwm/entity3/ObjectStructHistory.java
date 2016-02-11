/*-----------------------------------------------------------------------------------
	File: ObjectEntity.java
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

@Entity
@Table(name="OBJECT_STRUCT_HIST")
@SequenceGenerator(name="OBJECT_STRUCT_HIST_SEQ_GEN", sequenceName="OBJECT_STRUCT_HIST_SEQ",allocationSize = 1)
public class ObjectStructHistory implements Serializable,BaseEntity  {
	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_STRUCT_HIST_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
    public void setId(long value){ this.id = value; }

    public ObjectStructHistory(){}
    public ObjectStructHistory(long id){this.id = id;}
    
    private Long objectId;
	private Long curLocatorId;
	private Long prevLocatorId;
	private Long prevParentObjectId;
    private Long curParentObjectId;
    private Date changeDate;

	@Column(name="OBJECT_ID")
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	@Column(name="CHANGE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	@Column(name="CUR_LOCATOR_ID")
    public Long getCurLocatorId() {
		return curLocatorId;
	}
	public void setCurLocatorId(Long curLocatorId) {
		this.curLocatorId = curLocatorId;
	}
	
	@Column(name="PREV_LOCATOR_ID")
    public Long getPrevLocatorId() {
		return prevLocatorId;
	}
	public void setPrevLocatorId(Long prevLocatorId) {
		this.prevLocatorId = prevLocatorId;
	}
	
	@Column(name="PREV_PARENT_OBJECT_ID")
	public Long getPrevParentObjectId() {
		return prevParentObjectId;
	}
	public void setPrevParentObjectId(Long prevParentObjectId) {
		this.prevParentObjectId = prevParentObjectId;
	}
	
	@Column(name="CUR_PARENT_OBJECT_ID")
	public Long getCurParentObjectId() {
		return curParentObjectId;
	}
	public void setCurParentObjectId(Long curParentObjectId) {
		this.curParentObjectId = curParentObjectId;
	}
}