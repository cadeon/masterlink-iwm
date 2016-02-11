/*-----------------------------------------------------------------------------------
	File: Locator.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LOCATOR")
@SequenceGenerator(name="LOCATOR_SEQ_GEN", sequenceName="LOCATOR_SEQ",allocationSize = 1)
public class Locator implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "LOCATOR_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

    private String address;
    private Date inServiceDate;
    private String name;
    private Long parentId;
    private Long schemaId;
    private Long securityLevel;
    private String abbr;
    private String fullLocator;
    private Date archivedDate;
    private String emergencyContact;
    private Long topParentId;
    private Collection<LocatorData> locatorData;
    
    @Column(name="ADDRESS")
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="INSERVICE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getInServiceDate() {
		return inServiceDate;
	}
	public void setInServiceDate(Date inServiceDate) {
		this.inServiceDate = inServiceDate;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="PARENT_ID")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="SCHEMA_ID")
	public Long getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(Long schemaId) {
		this.schemaId = schemaId;
	}
	
	@Column(name="SECURITY_LEVEL")
	public Long getSecurityLevel() {
		return securityLevel;
	}
	public void setSecurityLevel(Long securityLevel) {
		this.securityLevel = securityLevel;
	}
	
	@Column(name="ABBR")
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	@Column(name="FULL_LOCATOR")
	public String getFullLocator() {
		return fullLocator;
	}
	public void setFullLocator(String fullLocator) {
		this.fullLocator = fullLocator;
	}
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="EMERGENCY_CONTACT")
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	
	@Column(name="TOP_PARENT_ID")
	public Long getTopParentId() {
		return topParentId;
	}
	public void setTopParentId(Long topParentId) {
		this.topParentId = topParentId;
	}
    
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="locator")
    public Collection<LocatorData> getLocatorData() {
		return locatorData;
	}
	public void setLocatorData(Collection<LocatorData> locatorData) {
		this.locatorData = locatorData;
	}
	
//KEEPMESTART
    //TODO:
    //public void updateDependents() throws BusinessException;
//KEEPMEEND

    
}
