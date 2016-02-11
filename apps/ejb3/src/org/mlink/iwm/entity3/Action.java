/*-----------------------------------------------------------------------------------
	File: Action.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.mlink.iwm.util.Constants;

@Entity
@Table(name="ACTION")
@SequenceGenerator(name="ACTION_SEQ_GEN", sequenceName="ACTION_SEQ",allocationSize = 1)
public class Action implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ACTION_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	private String verb; 
	private String name;
	private String modifier;
	private Integer sequence;
	private Integer custom;
	private Date archivedDate;
	private ActionDefinition actionDefinition; 
	private Task task;
	
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
	
	@Column(name="CUSTOM")
    public Integer getCustom() {
		return custom;
	}
	public void setCustom(Integer custom) {
		this.custom = custom;
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
	@JoinColumn(name = "ACTION_DEF_ID")
    public ActionDefinition getActionDefinition() {
		return actionDefinition;
	}
	public void setActionDefinition(ActionDefinition actionDefinition) {
		this.actionDefinition = actionDefinition;
	}
	
	@ManyToOne()
	@JoinColumn(name = "TASK_ID")
    public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}

//KEEPMESTART

    /**
     * @deprecated see Deletion Rules
     * @return
     * @
     */
    //boolean hasJobActions() ;
	
	/**
	 * isMeterReading
	 */
	@Transient
	public boolean isMeterReading() {
		return ("record".equalsIgnoreCase(getVerb()) &&
			"meter".equalsIgnoreCase(getName()) );
	}
	
	public Action(){
		
	}

	public Action(ActionDefinition ad){
		//setSequence(ad.getSequence());
		//setActionDefId(ad.getActionDefId());
        setCustom(Constants.CUSTOMIZED_NO);
        setActionDefinition(ad);
        synchronize();
    }

    /**
     * Sync up properties with the ActionDefinition
     * Should be called of the ActionDefinition change
     */
    public void synchronize(){
        if(Constants.CUSTOMIZED_YES.equals(getCustom()))
            return; // custom instances are not logically linked to their definitions

        ActionDefinition ad = getActionDefinition();
		setVerb(ad.getVerb());
		setName(ad.getName());
		setModifier(ad.getModifier());
        setSequence(ad.getSequence());

    }
//KEEPMEEND
}