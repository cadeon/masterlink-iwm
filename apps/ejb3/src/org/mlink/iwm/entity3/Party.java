/*-----------------------------------------------------------------------------------
	File: Party.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

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

import org.mlink.iwm.util.EqualsUtils;

@Entity
@Table(name="PARTY")
@SequenceGenerator(name="PARTY_SEQ_GEN", sequenceName="PARTY_SEQ",allocationSize = 1)
public class Party implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "PARTY_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	private String email;
	private String url;
	private String name;
	private String fax;
	private String phone;
	private Long locatorId;
	private Address address;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "ADDRESS_ID")
	public Address getAddress()
	{
		return address;
	}
	
	public void setAddress(Address address){
		this.address = address;
	}
		
	public Party(){
	}
	
	public Party(String name, String email, String url, String fax, String phone, Long locatorId, String address1, String address2, String city, String state, String zip){
		this.name = name;
		this.email = email;
		this.url = url;
		this.fax = fax;
		this.phone = phone;
		this.locatorId = locatorId;
		this.address = new Address(address1, address2, city, state, zip);
	}

	@Column(name="EMAIL")
	public String getEmail(){
		return email;
	}	
	public void setEmail(String email){
		this.email = email;
	}

	@Column(name="URL")
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}

	@Column(name="NAME")
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	@Column(name="FAX")
	public String getFax(){
		return fax;
	}
	public void setFax(String fax){
		this.fax = fax;
	}

	@Column(name="PHONE")
	public String getPhone(){
		return phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	} 

	@Column(name="LOCATOR_ID")
	public Long getLocatorId(){
		return locatorId;
	}
	public void setLocatorId(Long locatorId){
		this.locatorId = locatorId;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("\n").append(this.getClass().getName());
        sb.append("\n\tid=").append(getId());
    	
        sb.append("\n\temail=").append(getEmail());
        sb.append("\n\turl=").append(getUrl());
        sb.append("\n\tname=").append(getName());
        sb.append("\n\tfax=").append(getFax());
        sb.append("\n\tzip=").append(getPhone());
        sb.append("\n\tlocatorId=").append(getLocatorId());
        return sb.toString();
    }

    public boolean equals(Object other){
    	if(this == other) return true;
        if(!(other instanceof Party)) return false;
        Party that = (Party)other;
        return  EqualsUtils.areEqual(this.getId(), that.getId())
                && EqualsUtils.areEqual(this.getEmail(), that.getEmail())
                && EqualsUtils.areEqual(this.getUrl(), that.getUrl())
                && EqualsUtils.areEqual(this.getName(), that.getName())
                && EqualsUtils.areEqual(this.getFax(), that.getFax())
                && EqualsUtils.areEqual(this.getPhone(), that.getPhone())
                && EqualsUtils.areEqual(this.getLocatorId(), that.getLocatorId())
                && this.getAddress().equals(that.getAddress());
    }
}