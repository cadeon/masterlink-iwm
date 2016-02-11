/*-----------------------------------------------------------------------------------
	File: TaskSequence.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TASK_SEQUENCE")
@SequenceGenerator(name="TASK_SEQUENCE_SEQ_GEN", sequenceName="TASK_SEQUENCE_SEQ", allocationSize = 1)
public class TaskSequence implements Serializable,BaseEntity  {
	public TaskSequence(){}
	
	public TaskSequence(long id){
		this.id = id;
	}
	
	public TaskSequence(Sequence sequence, Task task, Integer level) {
		setSequenceLevel(level);
		setSequence(sequence);
		setTask(task);
	}
	
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "TASK_SEQUENCE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Integer sequenceLevel;
	public Sequence sequence;
	public Task task;
	
	@Column(name="SEQUENCE_LEVEL")
	public Integer getSequenceLevel() {
		return sequenceLevel;
	}
	public void setSequenceLevel(Integer sequenceLevel) {
		this.sequenceLevel = sequenceLevel;
	}
	
	@ManyToOne()
	@JoinColumn(name="SEQUENCE_ID") 
    public Sequence getSequence() {
		return sequence;
	}
	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TASK_ID")
    public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
}