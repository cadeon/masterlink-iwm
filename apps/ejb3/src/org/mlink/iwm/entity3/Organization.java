/*-----------------------------------------------------------------------------------
	File: Organization.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.mlink.iwm.util.EqualsUtils;

@Entity
@Table(name="ORGANIZATION")
@SecondaryTable(name = "ORG_TREE", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "ORG_ID")})
@SequenceGenerator(name="ORGANIZATION_SEQ_GEN", sequenceName="ORGANIZATION_SEQ",allocationSize = 1)
public class Organization implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ORGANIZATION_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Integer organizationTypeId;
	private Date archivedDate;
	private Long parentId;
	private long schemaId;
	private Party party;
	private String fullOrganization;

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

	
	public Organization(){
	}
	
	public Organization(Integer organizationTypeId, Date archivedDate, Long parentId, Integer schemaId,  
			String name, String email, String url, String fax, String phone, Long locatorId, 
			String address1, String address2, String city, String state, String zip){
		this.organizationTypeId = organizationTypeId;
		this.archivedDate = archivedDate;
		this.parentId = parentId;
		this.schemaId = schemaId;
		this.party = new Party(name, email, url, fax, phone, locatorId, 
				address1, address2, city, state, zip);
	}
	
    @Column(name="ORGANIZATION_TYPE_ID")
    public Integer getOrganizationTypeId(){
		return organizationTypeId;
	}
	public void setOrganizationTypeId(Integer organizationTypeId){
		this.organizationTypeId = organizationTypeId;
	}
	
	@Column(name="FULL_ORG_NAME", table="ORG_TREE", insertable=false, updatable=false)
    public String getFullOrganization(){
		return fullOrganization;
	}
	public void setFullOrganization(String fullOrganization){
		this.fullOrganization = fullOrganization;
	}
	
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate(){
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate){
		this.archivedDate = archivedDate;
	}
	
	@Column(name="PARENT_ID")
    public Long getParentId(){
    	return parentId;
    }
	public void setParentId(Long parentId){
    	this.parentId = parentId;
    }

    @Column(name="SCHEMA_ID")
    public Long getSchemaId(){
    	return schemaId;
    }
    public void setSchemaId(long schemaIdTop){
    	this.schemaId = schemaIdTop;
    }
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("\n").append(this.getClass().getName());
        sb.append("\n\tid=").append(getId());
        sb.append("\n\torganizationTypeId=").append(getOrganizationTypeId());
        sb.append("\n\tarchivedDate=").append(getArchivedDate());
        sb.append("\n\tparentId=").append(getParentId());
        sb.append("\n\tschemaId=").append(getSchemaId());
        return sb.toString();
    }

    public boolean equals(Object other){
    	if(this == other) return true;
        if(!(other instanceof Organization)) return false;
        Organization that = (Organization)other;
        return  EqualsUtils.areEqual(this.getId(), that.getId())
                && EqualsUtils.areEqual(this.getOrganizationTypeId(), that.getOrganizationTypeId())
                && EqualsUtils.areEqual(this.getArchivedDate(), that.getArchivedDate())
                && EqualsUtils.areEqual(this.getParentId(), that.getParentId())
                && EqualsUtils.areEqual(this.getSchemaId(), that.getSchemaId())
                && (this.getParty().equals(that.getParty()));
    }
}