/*-----------------------------------------------------------------------------------
	File: TaskGroup.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;

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

import org.mlink.iwm.util.Constants;

@Entity
@Table(name="TASK_GROUP")
@SequenceGenerator(name="TASK_GROUP_SEQ_GEN", sequenceName="TASK_GROUP_SEQ",allocationSize = 1)
public class TaskGroup implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TASK_GROUP_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	public TaskGroup(){}
	public TaskGroup(long id){
		this. id = id;
	}
	
	public TaskGroup(TaskGroupDefinition tgd) {
		//setTaskGroupDefId(tgd.getTaskGroupDefId());
		setSkillTypeId(tgd.getSkillTypeId());
		setDescription(tgd.getDescription());
		setCustom(Constants.CUSTOMIZED_NO);
        setTaskGroupDef(tgd);
    }
	
	private String description;
	private Integer custom;
	private Integer skillTypeId;
    private TaskGroupDefinition taskGroupDef;
	private Collection<Task> tasks; 
    private ObjectEntity object;
	
    @Column(name="DESCRIPTION")
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="CUSTOM")
    public Integer getCustom() {
		return custom;
	}
	public void setCustom(Integer custom) {
		this.custom = custom;
	}
	
	@Column(name="SKILL_TYPE_ID")
    public Integer getSkillTypeId() {
		return skillTypeId;
	}
	public void setSkillTypeId(Integer skillTypeId) {
		this.skillTypeId = skillTypeId;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TASK_GROUP_DEF_ID")
    public TaskGroupDefinition getTaskGroupDef() {
		return taskGroupDef;
	}
	public void setTaskGroupDef(TaskGroupDefinition taskGroupDef) {
		this.taskGroupDef = taskGroupDef;
	}
	
	@OneToMany(mappedBy="taskGroup")
	public Collection<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Collection<Task> tasks) {
		this.tasks = tasks;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_ID")
    public ObjectEntity getObject() {
		return object;
	}
	public void setObject(ObjectEntity object) {
		this.object = object;
	}
}