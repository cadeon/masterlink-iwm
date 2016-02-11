package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="ACTION"
 *         mutable="false"
 *     
 */
public class Action {
    
	private Long    actionDefId;
	private Long    id;
	private String  modifier;
	private String  name;
	private Integer sequence;
	private Long    taskId;
	private String  verb;
	
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id=id;}
    
    /**
     *            @hibernate.property
     *             column="TASK_ID"
     *             
     */
    public Long getTaskId() {return this.taskId;}
    public void setTaskId(Long id) {this.taskId=id;}
    
    /**
     *            @hibernate.property
     *             column="MODIFIER"
     *             
     */
    public String getModifier() {return this.modifier;}
    public void setModifier(String s) {this.modifier=s;}
    
    /**
     *            @hibernate.property
     *             column="VERB"
     *             
     */
    public String getVerb() {return this.verb;}
    public void setVerb(String s) {this.verb=s;}
    
    /**
     *            @hibernate.property
     *             column="SEQUENCE"
     *             
     */
    public Integer getSequence() {return this.sequence;}
    public void setSequence(Integer i) {this.sequence=i;}
    
    /**
     *            @hibernate.property
     *             column="NAME"
     *             
     */
    public String getName() {return this.name;}
    public void setName(String s) {this.name=s;}
    
    /**
     *            @hibernate.property
     *             column="ACTION_DEF_ID"
     *             
     */
    public Long getActionDefId() {return this.actionDefId;}
    public void setActionDefId(Long lid) {this.actionDefId=lid;}
}
