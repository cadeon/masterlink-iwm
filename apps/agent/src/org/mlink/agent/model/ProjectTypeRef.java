package org.mlink.agent.model;
// Generated May 25, 2006 7:06:40 AM by Hibernate Tools 3.1.0.beta5


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="PROJECT_TYPE_REF"
 *         mutable="false"
 *     
 */
public class ProjectTypeRef  implements java.io.Serializable {

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
 *             column="CODE"
 *             length="20"
 *             not-null="true"
 *         
     */
     private String code;
     /**
      *            @hibernate.property
 *             column="DESCRIPTION"
 *             length="50"
 *             not-null="true"
 *         
     */
     private String description;
     /**
      *            @hibernate.property
 *             column="DISP_ORD"
 *             length="22"
 *         
     */
     private BigDecimal dispOrd;
     /**
      *            @hibernate.set
 *             lazy="true"
 *             inverse="true"
 *             cascade="none"
 *            @hibernate.collection-key
 *             column="PROJECT_TYPE_ID"
 *            @hibernate.collection-one-to-many
 *             class="org.mlink.agent.model.Project"
 *         
     */
     private Set projects = new HashSet(0);

     // Constructors

    /** default constructor */
    public ProjectTypeRef() {
    }

	/** minimal constructor */
    public ProjectTypeRef(BigDecimal id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }
    /** full constructor */
    public ProjectTypeRef(BigDecimal id, String code, String description, BigDecimal dispOrd, Set projects) {
       this.id = id;
       this.code = code;
       this.description = description;
       this.dispOrd = dispOrd;
       this.projects = projects;
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
     *             column="CODE"
     *             length="20"
     *             not-null="true"
     *         
     */
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    /**       
     *      *            @hibernate.property
     *             column="DESCRIPTION"
     *             length="50"
     *             not-null="true"
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
     *             column="DISP_ORD"
     *             length="22"
     *         
     */
    public BigDecimal getDispOrd() {
        return this.dispOrd;
    }
    
    public void setDispOrd(BigDecimal dispOrd) {
        this.dispOrd = dispOrd;
    }
    /**       
     *      *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="PROJECT_TYPE_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.Project"
     *         
     */
    public Set getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set projects) {
        this.projects = projects;
    }




}


