package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name="SKILL_TYPE_REF")
@SequenceGenerator(name="SKILL_TYPE_REF_SEQ_GEN", sequenceName="SKILL_TYPE_REF_SEQ",allocationSize = 1)
public class SkillType implements Serializable, BaseEntity {
	private static final Logger logger = Logger.getLogger(SkillType.class);

	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SKILL_TYPE_REF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long displayOrder){ this.id = displayOrder; }

	private String code;
    private Integer displayOrder;
	private String description;
	private Date createdDate;
	private Date archivedDate;
	
	@Column(name="CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate(){
		return createdDate;
	}
	public void setCreatedDate(Date createdDate){
		this.createdDate = createdDate;
	}
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate(){
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate){
		this.archivedDate = archivedDate;
	}
	
	@Column(name="CODE", nullable = false)
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="DISP_ORD")
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}