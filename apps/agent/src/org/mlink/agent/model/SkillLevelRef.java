package org.mlink.agent.model;


/**
 *        @hibernate.class
 *         table="SKILL_LEVEL_REF"
 *         mutable="false"
 *     
 */
public class SkillLevelRef  implements java.io.Serializable {

    public static final Integer NONE  = 0;
    public static final Integer ONE   = 1;
    public static final Integer TWO   = 2;
    public static final Integer THREE = 3;
    
    // Fields    
	private Integer id;
    private Integer value;
    private String  description;
    private String  code;

    public SkillLevelRef() {}
    
   
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
    /**       
     *            @hibernate.property
     *             column="VALUE"
     *         
     */
    public Integer getValue() {return this.value;}
    public void setValue(Integer i) {this.value = i;}
    

    public static SkillLevelRef getRef(String code) {
    	SkillLevelRef ref = new SkillLevelRef();
    	ref.setCode(code);
    	int id = org.mlink.iwm.lookup.SkillLevelRef.getIdByCode(code);
    	ref.setDescription(org.mlink.iwm.lookup.SkillLevelRef.getLabel(id));
    	ref.setId(id);
    	ref.setValue(org.mlink.iwm.lookup.SkillLevelRef.getSkillLevelValue(id));
    	return ref;
    }
   
  public String toString() {
    return"{"+value+"}";
  }

}


