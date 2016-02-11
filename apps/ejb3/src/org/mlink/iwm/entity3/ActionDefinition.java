/*-----------------------------------------------------------------------------------
	File: ActionDefinition.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ACTION_DEF")
@SequenceGenerator(name="ACTION_DEF_SEQ_GEN", sequenceName="ACTION_DEF_SEQ",allocationSize = 1)
public class ActionDefinition implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ACTION_DEF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String verb;
	private String name;
	private String modifier;
	private Integer sequence;
	private Date archivedDate;
	private TaskDefinition taskDefinition;
	private Collection<Action> actions;
	
	public ActionDefinition(){}
	public ActionDefinition(long id){
		this.id = id;
	}
	
	@Column(name="VERB")
    public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	@Column(name="NAME")
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="MODIFIER")
    public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	@Column(name="SEQUENCE")
    public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Column(name="ARCHIVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TASK_DEF_ID")
    public TaskDefinition getTaskDefinition() {
		return taskDefinition;
	}
	public void setTaskDefinition(TaskDefinition taskDefinition) {
		this.taskDefinition = taskDefinition;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="actionDefinition")
    public Collection<Action> getActions() {
		return actions;
	}
	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}

//KEEPMESTART

//KEEPMEEND
}