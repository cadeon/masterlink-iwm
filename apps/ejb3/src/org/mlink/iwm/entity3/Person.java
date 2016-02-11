/*-----------------------------------------------------------------------------------
	File: Person.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name="PERSON")
@SequenceGenerator(name="PERSON_SEQ_GEN", sequenceName="PERSON_SEQ",allocationSize = 1)
public class Person implements Serializable,BaseEntity  {
	private static final Logger logger = Logger.getLogger(Person.class);

	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "PERSON_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Party party;
	private String username;
	private Long userId;
	private Long organizationId;
	private String fia;
	private Date archivedDate;
	private Integer	active;
	private Integer workerTypeId;
	private Integer securityLevelId;
	private Float billingRate;
	private String title;
	private Integer statusId;
	private String refId;
	private String billetSequenceCode;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "PARTY_ID")
	public Party getParty()
	{
		return party;
	}
	public void setParty(Party party)
	{
		this.party = party;
	}
	
	@Column(name="USER_NAME")
    public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	@Column(name="USER_ID")
    public Long getUserId(){
		return userId;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	@Column(name="ORGANIZATION_ID")
    public Long getOrganizationId(){
		return organizationId;
	}
	public void setOrganizationId(Long organizationId){
		this.organizationId = organizationId;
	}

	@Column(name="FIA")
    public String getFia(){
		return fia;
	}
	public void setFia(String fia){
		this.fia = fia;
	}

	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate(){
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate){
		this.archivedDate = archivedDate;
	}

	public Integer getActive(){
		return active;
	}
	public void setActive(Integer active){
		this.active = active;
	}

	@Column(name="WORKER_TYPE_ID")
	public Integer getWorkerTypeId(){
		return workerTypeId;
	}
	public void setWorkerTypeId(Integer workerTypeId){
		this.workerTypeId = workerTypeId;
	}

	@Column(name="SECURITY_LEVEL_ID")
	public Integer getSecurityLevelId(){
		return securityLevelId;
	}
	public void setSecurityLevelId(Integer securityLevelId){
		this.securityLevelId = securityLevelId;
	}

	@Column(name="BILLING_RATE")
	public Float getBillingRate(){
		return billingRate;
	}
	public void setBillingRate(Float billingRate){
		this.billingRate = billingRate;
	}
	
	public String getTitle(){
    	return title;
    }
	public void setTitle(String title){
    	this.title = title;
    }

    @Column(name="STATUS_ID")
	public Integer getStatusId(){
    	return statusId;
    }
    public void setStatusId(Integer statusId){
    	this.statusId = statusId;
    }

    @Column(name="REF_ID")
	public String getRefId(){
    	return refId;
    }
    public void setRefId(String refId){
    	this.refId = refId;
    }

    @Column(name="BSCODE")
	public String getBilletSequenceCode(){
    	return billetSequenceCode;
    }
    public void setBilletSequenceCode(String billetSequenceCode){
    	this.billetSequenceCode = billetSequenceCode;
    }
}