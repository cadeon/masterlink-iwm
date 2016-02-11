package org.mlink.agent.model;

/**
 * @hibernate.class
 * table = "SCHEMA_REF"
 * mutable="false"
 * 
 * @author michaelpeter
 *
 */
public class SchemaRef {
	
	private Long    id;
	private Integer changeTime;
	private String  code;
	private String  description;
	private Long    parent;
	private String  type;

    // Constructors
   public SchemaRef() {}
   public SchemaRef(Long id) {this.id = id;}    
  
   // Property accessors
   /**       
    *            @hibernate.id
    *             generator-class="assigned"
    *             column="ID"
    */
   public Long getId(){return this.id;}
   public void setId(Long id){this.id = id;}
   /**
    * 			@hibernate.property
    * 			column="CHANGETIME"
    */
   public Integer getChangeTime(){return this.changeTime;}
   public void setChangeTime(Integer i){this.changeTime=i;}
   /**
    * 			@hibernate.property
    * 			column="CODE"
    */
   public String getCode(){return this.code;}
   public void setCode(String s){this.code=s;}
   
   /**
    * 			@hibernate.property
    * 			column="DESCRIPTION"
    */
   public String getDescription(){return this.description;}
   public void setDescription(String s){this.description=s;}
   /**
    * 			@hibernate.property
    * 			column="PARENT"
    */
   public Long getParent(){return this.parent;}
   public void setParent(Long li){this.parent=li;}
   /**
    * 			@hibernate.property
    * 			column="SCHEMA_TYPE"	
    */
   public String getSchemaType(){return this.type;}
   public void setSchemaType(String s){this.type=s;}
}
