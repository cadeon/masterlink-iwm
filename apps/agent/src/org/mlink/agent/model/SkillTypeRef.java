package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="SKILL_TYPE_REF"
 *         mutable="false"
 *     
 */
public class SkillTypeRef  implements java.io.Serializable {

    public static final String MUST_ASSIGN = "Assign_Skill";
    
    // Fields    
	private Integer id;
	private String  description;
    private String  code;
    
    public SkillTypeRef() {}
    
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Integer getId() {return this.id;}
    public void    setId(Integer id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="DESCRIPTION"
     *         
     */
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
    /**       
     *            @hibernate.property
     *             column="CODE"
     *         
     */
    public String getCode() {return this.code;}
    public void setCode(String code) {this.code = code;}
    
    public String toString() {
    	return "{SkillTypeRef:: id:"+this.id+";description:"+this.description+";code:"+this.code+"}";
    }
}


