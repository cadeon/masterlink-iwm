package org.mlink.agent.model;
// Generated May 27, 2006 12:59:46 PM by Hibernate Tools 3.1.0.beta5


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="TASK_GROUP"
 *     
 */
public class TaskGroup  implements java.io.Serializable {

    // Fields    

     /**
      *            @hibernate.id
 *             generator-class="assigned"
 *             type="java.math.BigDecimal"
 *             column="ID"
 *         
     */
     private BigDecimal id;
     /**
      *            @hibernate.property
 *             column="DESCRIPTION"
 *             length="150"
 *         
     */
     private String description;
     /**
      *            @hibernate.property
 *             column="CUSTOM"
 *             length="22"
 *         
     */
     private BigDecimal custom;
     /**
      *            @hibernate.many-to-one
 *             not-null="true"
 *            @hibernate.column name="OBJECT_ID"         
 *         
     */
     private WorkObject object;
     /**
      *            @hibernate.property
 *             not-null="true"
 *            @hibernate.column name="TASK_GROUP_DEF_ID"         
 *         
     */
     private Long taskGroupDef;
     /**
      *            @hibernate.many-to-one
 *             not-null="true"
 *            @hibernate.column name="SKILL_TYPE_ID"         
 *         
     */
     private SkillTypeRef skillTypeRef;
     /**
      *            @hibernate.set
 *             lazy="true"
 *             inverse="true"
 *             cascade="none"
 *            @hibernate.collection-key
 *             column="GROUP_ID"
 *            @hibernate.collection-one-to-many
 *             class="org.mlink.agent.model.Task"
 *         
     */
     private Set tasks = new HashSet(0);

     // Constructors

    /** default constructor */
    public TaskGroup() {
    }

	/** minimal constructor */
    public TaskGroup(BigDecimal id) {
        this.id = id;
    }
    /** full constructor */
    public TaskGroup(BigDecimal id, String description, BigDecimal custom, WorkObject object, 
    		Long taskGroupDef, SkillTypeRef skillTypeRef, Set tasks) {
       this.id = id;
       this.description = description;
       this.custom = custom;
       this.object = object;
       this.taskGroupDef = taskGroupDef;
       this.skillTypeRef = skillTypeRef;
       this.tasks = tasks;
    }
    
   
    // Property accessors
    /**       
     *      *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.math.BigDecimal"
     *             column="ID"
     *         
     */
    public BigDecimal getId() {
        return this.id;
    }
    
    public void setId(BigDecimal id) {
        this.id = id;
    }
    /**       
     *      *            @hibernate.property
     *             column="DESCRIPTION"
     *             length="150"
     *         
     */
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    /**       
     *      *            @hibernate.property
     *             column="CUSTOM"
     *             length="22"
     *         
     */
    public BigDecimal getCustom() {
        return this.custom;
    }
    
    public void setCustom(BigDecimal custom) {
        this.custom = custom;
    }
    /**       
     *      *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="OBJECT_ID"         
     *         
     */
    public WorkObject getObject() {
        return this.object;
    }
    
    public void setObject(WorkObject object) {
        this.object = object;
    }
    /**       
     *      *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="TASK_GROUP_DEF_ID"         
     *         
     */
    public Long getTaskGroupDef() {
        return this.taskGroupDef;
    }
    
    public void setTaskGroupDef(Long taskGroupDef) {
        this.taskGroupDef = taskGroupDef;
    }
    /**       
     *      *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="SKILL_TYPE_ID"         
     *         
     */
    public SkillTypeRef getSkillTypeRef() {
        return this.skillTypeRef;
    }
    
    public void setSkillTypeRef(SkillTypeRef skillTypeRef) {
        this.skillTypeRef = skillTypeRef;
    }
    /**       
     *      *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="GROUP_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.Task"
     *         
     */
    public Set getTasks() {
        return this.tasks;
    }
    
    public void setTasks(Set tasks) {
        this.tasks = tasks;
    }




}


