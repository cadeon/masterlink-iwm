/*-----------------------------------------------------------------------------------
	File: ObjectClassification.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

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
@Table(name="OBJECT_CLASSIFICATION")
@SequenceGenerator(name="OBJECT_CLASSIFICATION_SEQ_GEN", sequenceName="OBJECT_CLASSIFICATION_SEQ", allocationSize = 1)
public class ObjectClassification implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_CLASSIFICATION_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String code;
    private String description;
    private Long parentId;
    private Integer schemaId;
    private String abbr;
	
    @Column(name="CODE")
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="PARENT_ID")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="SCHEMA_ID")
	public Integer getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(Integer schemaId) {
		this.schemaId = schemaId;
	}
	
	@Column(name="ABBR")
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
}