/*-----------------------------------------------------------------------------------
	File: User.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="APPUSER")
@SequenceGenerator(name="APPUSER_SEQ_GEN", sequenceName="APPUSER_SEQ",allocationSize = 1)
public class User implements Serializable,BaseEntity  {
	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APPUSER_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Person person;
	private String username;
	private String password;
	private Set<Role> roles;
	 
	@Column(name="USER_NAME")
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}

	@Column(name="PASSWORD")
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@JoinColumn(name="PERSON_ID")
    public Person getPerson(){
		return person;
	}
	public void setPerson(Person person){
		this.person = person;
	}

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	   @JoinTable(name = "user_role",
	                     joinColumns = {@JoinColumn(name = "USER_ID")},
	                     inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
	public Set<Role> getRoles(){
		return roles;
	}
	public void setRoles(Set<Role> roles){
		this.roles = roles;
	}
}