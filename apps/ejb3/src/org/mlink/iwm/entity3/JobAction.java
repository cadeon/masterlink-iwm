/*-----------------------------------------------------------------------------------
	File: JobAction.java
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
@Table(name="JOB_ACTION")
@SequenceGenerator(name="JOB_ACTION_SEQ_GEN", sequenceName="JOB_ACTION_SEQ",allocationSize = 1)
public class JobAction implements Serializable,BaseEntity  {
	public JobAction(){}
	public JobAction(Action action) {
        setName(action.getName());
        setVerb(action.getVerb());
        setModifier(action.getModifier());
        setSequence(action.getSequence());
        setAction(action);  
    }

	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JOB_ACTION_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String fieldCondition;
	private String name;
	private String verb;
    private String modifier;
    private Integer sequence;
	private Action action;
	private JobTask jobTask;
	
	@Column(name="FIELD_CONDITION")
	public String getFieldCondition() {
		return fieldCondition;
	}
	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = fieldCondition;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="VERB")
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
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
	
	@ManyToOne()
	@JoinColumn(name = "ACTION_ID")
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	@ManyToOne()
	@JoinColumn(name = "JOB_TASK_ID")
    public JobTask getJobTask() {
		return jobTask;
	}
	public void setJobTask(JobTask jobTask) {
		this.jobTask = jobTask;
	}
}