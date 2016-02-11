/*-----------------------------------------------------------------------------------
	File: TaskGroupDefinition.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TASK_GROUP_DEF")
@SequenceGenerator(name="TASK_GROUP_DEF_SEQ_GEN", sequenceName="TASK_GROUP_DEF_SEQ",allocationSize = 1)
public class TaskGroupDefinition implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TASK_GROUP_DEF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	public TaskGroupDefinition(){}
	public TaskGroupDefinition(long id){
		this.id = id;
	}
	
	private String description;
	private ObjectDefinition objectDefinition;
	private Collection<TaskDefinition> taskDefs; 
	private Collection<TaskGroup> taskGroups;
	private Integer skillTypeId;

	@Column(name="DESCRIPTION")
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_DEF_ID")
    public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}
	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}
	
   @OneToMany(cascade = {CascadeType.ALL}, mappedBy="taskGroupDef")
   public Collection<TaskDefinition> getTaskDefs() {
		return taskDefs;
	}
	public void setTaskDefs(Collection<TaskDefinition> taskDefs) {
		this.taskDefs = taskDefs;
	}

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="taskGroupDef")
	public Collection<TaskGroup> getTaskGroups() {
		return taskGroups;
	}
	public void setTaskGroups(Collection<TaskGroup> taskGroups) {
		this.taskGroups = taskGroups;
	}
	
	@Column(name="SKILL_TYPE_ID")
    public Integer getSkillTypeId() {
		return skillTypeId;
	}
	public void setSkillTypeId(Integer skillTypeId) {
		this.skillTypeId = skillTypeId;
	}
}