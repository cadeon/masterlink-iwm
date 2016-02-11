package org.mlink.iwm.entity3;

import org.mlink.iwm.util.EqualsUtils;

import javax.persistence.*;

import java.io.Serializable;

/**
 * User: andreipovodyrev
 * Date: Mar 6, 2009
 */
@Entity
@Table(name="ADDRESS")
@SequenceGenerator(name="ADDRESS_SEQ_GEN", sequenceName="ADDRESS_SEQ",allocationSize = 1)
public class Address implements Serializable,BaseEntity {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ADDRESS_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
    
	public Address(){
	}
	
	public Address(String address1, String address2, String city, String state, String zip){
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
    private String address1;
    public String getAddress1(){  return address1; }
    public void setAddress1(String value){ this.address1 = value; }

    private String address2;
    public String getAddress2(){  return address2; }
    public void setAddress2(String value){ this.address2 = value; }

    private String city;
    public String getCity(){  return city; }
    public void setCity(String value){ this.city = value; }


    private String state;
    public String getState(){  return state; }
    public void setState(String value){ this.state = value; }

    private String zip;
    public String getZip(){  return zip; }
    public void setZip(String value){ this.zip = value; }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("\n").append(this.getClass().getName());
        sb.append("\n\tid=").append(getId());
        sb.append("\n\taddress1=").append(getAddress1());
        sb.append("\n\taddress2=").append(getAddress2());
        sb.append("\n\tcity=").append(getCity());
        sb.append("\n\tstate=").append(getState());
        sb.append("\n\tzip=").append(getZip());
        return sb.toString();
    }

    public boolean equals(Object other){
        if(this == other) return true;
        if(!(other instanceof Address)) return false;
        Address that = (Address)other;
        return  EqualsUtils.areEqual(this.getId(), that.getId())
                && EqualsUtils.areEqual(this.getAddress1(), that.getAddress1())
                && EqualsUtils.areEqual(this.getAddress2(), that.getAddress2())
                && EqualsUtils.areEqual(this.getCity(), that.getCity())
                && EqualsUtils.areEqual(this.getState(), that.getState())
                && EqualsUtils.areEqual(this.getZip(), that.getZip());
    }
}