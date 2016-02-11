package org.mlink.agent.model;
// Generated May 27, 2006 12:59:46 PM by Hibernate Tools 3.1.0.beta5

import java.util.List;

/**
 *        @hibernate.class
 *         table="LOCATOR"
 *         mutable="false"
 *     
 */
public class Locator  implements java.io.Serializable {

    // Fields    
     private Long   id;
     private Long   securityLevel;
     private String fullLocator;
     private String abbr;
     private String name;
     private Long   topParentId;
     private Locator parent;
     private List<WorkSchedule> schedules;
     private SchemaRef schemaRef;

     // Constructors
    public Locator() {}
    public Locator(Long id) {this.id = id;}    
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Long getId(){return this.id;}
    public void setId(Long id){this.id = id;}
    /**       
     *            @hibernate.property
     *             column="SECURITY_LEVEL"
     */
    public Long getSecurityLevel(){return this.securityLevel;}
    public void setSecurityLevel(Long securityLevel) {this.securityLevel = securityLevel;}
    /**       
     *            @hibernate.property
     *             column="FULL_LOCATOR"
     */
    public String getFullLocator() {return this.fullLocator;}
    public void setFullLocator(String fullLocator) {this.fullLocator = fullLocator;}
    /**       
     *            @hibernate.property
     *             column="ABBR"
     */
    public String getAbbr() {return this.abbr;}
    public void setAbbr(String abbr) {this.abbr = abbr;}
    /**       
     *            @hibernate.property
     *             column="NAME"
     */
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    /**       
     *            @hibernate.many-to-one
     *            @hibernate.column name="PARENT_ID"         
     *         
     */
    public Locator getParent() {return this.parent;}
    public void setParent(Locator locator) {this.parent = locator;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *            @hibernate.column name="SCHEMA_ID"
     */
    public SchemaRef getSchemaRef() {return this.schemaRef;}
    public void setSchemaRef(SchemaRef schemaRef) {this.schemaRef = schemaRef;}
    /**
     *            @hibernate.property
     *             column="TOP_PARENT_ID"
     */
    public Long getTopParentId(){return this.topParentId;}
    public void setTopParentId(Long top){this.topParentId=top;}

    public List<WorkSchedule> getSchedules(){return this.schedules;}
    public void setSchedules(List<WorkSchedule> list){this.schedules=list;}
    
    public String toString() {
    	return "{id:"+id+";name:"+name+";full locator"+fullLocator+";abbr:"+abbr+";top parent id:"+topParentId+"}";
    }
}


