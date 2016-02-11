/*-----------------------------------------------------------------------------------
	File: Role.java
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
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
@SequenceGenerator(name = "ROLE_SEQ_GEN", sequenceName = "ROLE_SEQ",allocationSize = 1)
public class Role implements Serializable,BaseEntity  {

	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ROLE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	String name;
	String description;
	Set<User> users;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy="roles")
	public Set<User> getUsers(){
		return users;
	}
	public void setUsers(Set<User> users){
		this.users = users;
	}
}