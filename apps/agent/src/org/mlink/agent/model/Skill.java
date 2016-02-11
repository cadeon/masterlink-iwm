package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="SKILL"
 *     
 */
public class Skill  implements java.io.Serializable {

    // Fields    
	private Long id;
    private SkillTypeRef skillTypeRef;
    private Person person;
    private SkillLevelRef skillLevelRef;

    public Skill() {}

    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="PERSON_ID"
     */
    public Person getPerson() {return this.person;}
    public void setPerson(Person person) {this.person = person;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="SKILL_LEVEL_ID"
     */
    public SkillLevelRef getSkillLevelRef() {return this.skillLevelRef;}
    public void setSkillLevelRef(SkillLevelRef skillLevelRef) {this.skillLevelRef = skillLevelRef;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="SKILL_TYPE_ID"
     */
    public SkillTypeRef getSkillTypeRef() {return this.skillTypeRef;}
    public void setSkillTypeRef(SkillTypeRef skillTypeRef) {this.skillTypeRef = skillTypeRef;}
    
    public String toString() {
    	return "{Skill:: id:"+this.id+";person:"+person+";skillType:"+skillTypeRef+";skillLevel:"+skillLevelRef+"}";
    }
}


