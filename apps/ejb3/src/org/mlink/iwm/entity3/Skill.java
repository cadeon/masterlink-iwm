package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SKILL")
@SequenceGenerator(name="SKILL_SEQ_GEN", sequenceName="SKILL_SEQ",allocationSize = 1)
public class Skill implements Serializable,BaseEntity {
	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SKILL_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	private Long personId;
	private Long skillTypeId;
	private Long skillLevelId;
	
	@Column(name="PERSON_ID")
	public Long getPersonId(){
		return personId;
	}
	public void setPersonId(Long personId){
		this.personId = personId;
	}

	@Column(name="SKILL_TYPE_ID")
	public Long getSkillTypeId(){
		return skillTypeId;
	}
	public void setSkillTypeId(Long skillTypeId){
		this.skillTypeId = skillTypeId;
	}

	@Column(name="SKILL_LEVEL_ID")
	public Long getSkillLevelId(){
		return skillLevelId;
	}
	public void setSkillLevelId(Long skillLevelId){
		this.skillLevelId = skillLevelId;
	}
}