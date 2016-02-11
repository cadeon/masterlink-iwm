package org.mlink.agent.model;
// Generated May 25, 2006 7:06:40 AM by Hibernate Tools 3.1.0.beta5


import java.math.BigDecimal;

/**
 *        @hibernate.class
 *         table="TASK_SEQUENCE"
 *     
 */
public class TaskSequence  implements java.io.Serializable {

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
 *             column="SEQUENCE_LEVEL"
 *             length="22"
 *         
     */
     private BigDecimal sequenceLevel;
     /**
      *            @hibernate.property
 *             not-null="true"
 *            @hibernate.column name="SEQUENCE_ID"         
 *         
     */
     private Long sequence;
     /**
      *            @hibernate.many-to-one
 *             not-null="true"
 *            @hibernate.column name="TASK_ID"         
 *         
     */
     private Task task;

     // Constructors

    /** default constructor */
    public TaskSequence() {
    }

	/** minimal constructor */
    public TaskSequence(BigDecimal id) {
        this.id = id;
    }
    /** full constructor */
    public TaskSequence(BigDecimal id, BigDecimal sequenceLevel, Long sequence, Task task) {
       this.id = id;
       this.sequenceLevel = sequenceLevel;
       this.sequence = sequence;
       this.task = task;
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
     *             column="SEQUENCE_LEVEL"
     *             length="22"
     *         
     */
    public BigDecimal getSequenceLevel() {
        return this.sequenceLevel;
    }
    
    public void setSequenceLevel(BigDecimal sequenceLevel) {
        this.sequenceLevel = sequenceLevel;
    }
    /**       
     *      *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="SEQUENCE_ID"         
     *         
     */
    public Long getSequence() {
        return this.sequence;
    }
    
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
    /**       
     *      *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="TASK_ID"         
     *         
     */
    public Task getTask() {
        return this.task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }




}


