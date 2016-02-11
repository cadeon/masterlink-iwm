package org.mlink.agent.model;
// Generated May 27, 2006 12:59:46 PM by Hibernate Tools 3.1.0.beta5


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="OBJECT"
 *     
 */
public class WorkObject  implements java.io.Serializable {

    // Fields    

     /**
      *            @hibernate.id
 *             generator-class="assigned"
 *             column="ID"
 *         
     */
     private BigDecimal id;
     /**
      *            @hibernate.property
 *             column="CREATED_DATE"
 *             length="7"
 *         
     */
     private Timestamp createdDate;
     /**
      *            @hibernate.property
 *             column="ACTIVE"
 *             length="22"
 *         
     */
     private BigDecimal active;
     /**
      *            @hibernate.property
 *             column="START_DATE"
 *             length="7"
 *         
     */
     private Timestamp startDate;
     /**
      *            @hibernate.property
 *             column="CLASS_ID"
 *             length="22"
 *         
     */
     private BigDecimal classId;
     /**
      *            @hibernate.property
 *             column="RUN_HOURS"
 *             length="126"
 *         
     */
     private Float runHours;
     /**
      *            @hibernate.property
 *             column="TAG"
 *             length="150"
 *         
     */
     private String tag;
     /**
      *            @hibernate.property
 *             column="CUSTOM"
 *             length="22"
 *         
     */
     private BigDecimal custom;
     /**
      *            @hibernate.property
 *             column="OBJECT_REF"
 *             length="50"
 *         
     */
     private String objectRef;
     /**
      *            @hibernate.property
 *             column="ARCHIVED_DATE"
 *             length="7"
 *         
     */
     private Timestamp archivedDate;
     /**
      *            @hibernate.property
 *             column="HAS_CUSTOM_DATA"
 *             length="1"
 *         
     */
     private Integer hasCustomData;
     /**
      *            @hibernate.property
 *             column="HAS_CUSTOM_TASK"
 *             length="1"
 *         
     */
     private Integer hasCustomTask;
     /**
      *            @hibernate.property
 *             column="HAS_CUSTOM_TASK_GROUP"
 *             length="1"
 *         
     */
     private Integer hasCustomTaskGroup;
     /**
      *            @hibernate.property
 *             not-null="true"
 *            @hibernate.column name="OBJECT_TYPE_ID"         
 *         
     */
     private Long objectTypeRef;
     /**
      *            @hibernate.many-to-one
 *             not-null="true"
 *            @hibernate.column name="LOCATOR_ID"         
 *         
     */
     private Locator locator;
     /**
      *            @hibernate.property
 *             not-null="true"
 *            @hibernate.column name="OBJECT_DEF_ID"         
 *         
     */
     private Long objectDef;

     // Constructors

    /** default constructor */
    public WorkObject() {
    }

	/** minimal constructor */
    public WorkObject(BigDecimal id) {
        this.id = id;
    }
    /** full constructor */
    public WorkObject(BigDecimal id, Timestamp createdDate, BigDecimal active, Timestamp startDate, 
    		BigDecimal classId, Float runHours, String tag, BigDecimal custom, String objectRef, 
    		Timestamp archivedDate, Integer hasCustomData, Integer hasCustomTask, Integer hasCustomTaskGroup, 
    		Long objectTypeRef, Locator locator, Long objectDef) {
       this.id = id;
       this.createdDate = createdDate;
       this.active = active;
       this.startDate = startDate;
       this.classId = classId;
       this.runHours = runHours;
       this.tag = tag;
       this.custom = custom;
       this.objectRef = objectRef;
       this.archivedDate = archivedDate;
       this.hasCustomData = hasCustomData;
       this.hasCustomTask = hasCustomTask;
       this.hasCustomTaskGroup = hasCustomTaskGroup;
       this.objectTypeRef = objectTypeRef;
       this.locator = locator;
       this.objectDef = objectDef;
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
     *             column="CREATED_DATE"
     *             length="7"
     *         
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    /**       
     *      *            @hibernate.property
     *             column="ACTIVE"
     *             length="22"
     *         
     */
    public BigDecimal getActive() {
        return this.active;
    }
    
    public void setActive(BigDecimal active) {
        this.active = active;
    }
    /**       
     *      *            @hibernate.property
     *             column="START_DATE"
     *             length="7"
     *         
     */
    public Timestamp getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    /**       
     *      *            @hibernate.property
     *             column="CLASS_ID"
     *             length="22"
     *         
     */
    public BigDecimal getClassId() {
        return this.classId;
    }
    
    public void setClassId(BigDecimal classId) {
        this.classId = classId;
    }
    /**       
     *      *            @hibernate.property
     *             column="RUN_HOURS"
     *             length="126"
     *         
     */
    public Float getRunHours() {
        return this.runHours;
    }
    
    public void setRunHours(Float runHours) {
        this.runHours = runHours;
    }
    /**       
     *      *            @hibernate.property
     *             column="TAG"
     *             length="150"
     *         
     */
    public String getTag() {
        return this.tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
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
     *      *            @hibernate.property
     *             column="OBJECT_REF"
     *             length="50"
     *         
     */
    public String getObjectRef() {
        return this.objectRef;
    }
    
    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }
    /**       
     *      *            @hibernate.property
     *             column="ARCHIVED_DATE"
     *             length="7"
     *         
     */
    public Timestamp getArchivedDate() {
        return this.archivedDate;
    }
    
    public void setArchivedDate(Timestamp archivedDate) {
        this.archivedDate = archivedDate;
    }
    /**       
     *      *            @hibernate.property
     *             column="HAS_CUSTOM_DATA"
     *             length="1"
     *         
     */
    public Integer getHasCustomData() {
        return this.hasCustomData;
    }
    
    public void setHasCustomData(Integer hasCustomData) {
        this.hasCustomData = hasCustomData;
    }
    /**       
     *      *            @hibernate.property
     *             column="HAS_CUSTOM_TASK"
     *             length="1"
     *         
     */
    public Integer getHasCustomTask() {
        return this.hasCustomTask;
    }
    
    public void setHasCustomTask(Integer hasCustomTask) {
        this.hasCustomTask = hasCustomTask;
    }
    /**       
     *      *            @hibernate.property
     *             column="HAS_CUSTOM_TASK_GROUP"
     *             length="1"
     *         
     */
    public Integer getHasCustomTaskGroup() {
        return this.hasCustomTaskGroup;
    }
    
    public void setHasCustomTaskGroup(Integer hasCustomTaskGroup) {
        this.hasCustomTaskGroup = hasCustomTaskGroup;
    }
    /**       
     *      *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="OBJECT_TYPE_ID"         
     *         
     */
    public Long getObjectTypeRef() {
        return this.objectTypeRef;
    }
    
    public void setObjectTypeRef(Long objectTypeRef) {
        this.objectTypeRef = objectTypeRef;
    }
    /**       
     *      *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="LOCATOR_ID"         
     *         
     */
    public Locator getLocator() {
        return this.locator;
    }
    
    public void setLocator(Locator locator) {
        this.locator = locator;
    }
    /**       
     *      *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="OBJECT_DEF_ID"         
     *         
     */
    public Long getObjectDef() {
        return this.objectDef;
    }
    
    public void setObjectDef(Long objectDef) {
        this.objectDef = objectDef;
    }




}


