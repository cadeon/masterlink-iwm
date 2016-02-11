/*-----------------------------------------------------------------------------------
	File: ObjectDefinition.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="OBJECT_DEF")
@SequenceGenerator(name="OBJECT_DEF_SEQ_GEN", sequenceName="OBJECT_DEF_SEQ",allocationSize = 1)
public class ObjectDefinition implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_DEF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String plan; 
	private Long classId;
	private Date archivedDate;
	private Long presentInventory;
	
	private Collection<TaskGroupDefinition> taskGroupDefs;
	private Collection<TaskDefinition> taskDefs;
	private Collection<ObjectEntity> objects;
	private Set<ObjectDataDefinition> dataDefs;
	
	public ObjectDefinition(){}
	public ObjectDefinition(long id){
		this.id = id;
	}
	
	@Column(name="PLAN")
    public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	@Column(name="CLASS_ID")
    public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}

	@Column(name="ARCHIVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
	public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="INVENTORY")
    public Long getPresentInventory() {
		return presentInventory;
	}
	public void setPresentInventory(Long presentInventory) {
		this.presentInventory = presentInventory;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="objectDefinition")
	public Collection<TaskGroupDefinition> getTaskGroupDefs() {
		return taskGroupDefs;
	}
	public void setTaskGroupDefs(Collection<TaskGroupDefinition> taskGroupDefs) {
		this.taskGroupDefs = taskGroupDefs;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="objectDefinition")
	public Collection<TaskDefinition> getTaskDefs() {
		return taskDefs;
	}
	public void setTaskDefs(Collection<TaskDefinition> taskDefs) {
		this.taskDefs = taskDefs;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="objectDefinition")
	public Collection<ObjectEntity> getObjects() {
		return objects;
	}
	public void setObjects(Collection<ObjectEntity> objects) {
		this.objects = objects;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="objectDefinition")
	public Set<ObjectDataDefinition> getDataDefs() {
		return dataDefs;
	}
	public void setDataDefs(Set<ObjectDataDefinition> dataDefs) {
		this.dataDefs = dataDefs;
	}
	
    /**
     * Add new TaskGroupDefinition to Object
     * @param tgd
     */
    //TODO:
	//void add(TaskGroupDefinition tgd) throws CreateException;

//KEEPMEEND
}