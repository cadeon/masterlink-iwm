/*-----------------------------------------------------------------------------------
	File: ObjectDataDefinition.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="OBJECT_DATA_DEF")
@SequenceGenerator(name="OBJECT_DATA_DEF_SEQ_GEN", sequenceName="OBJECT_DATA_DEF_SEQ",allocationSize = 1)
public 
class ObjectDataDefinition implements Serializable,BaseEntity  {
	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_DATA_DEF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
    public void setId(long value){ this.id = value; }

	private Integer dataTypeId;
	private Integer uomId;
	private Integer isDisplay;
	private String dataLabel;
	private String dataValue;
	private ObjectDefinition objectDefinition;
	private Integer isEditInField;
	
	public ObjectDataDefinition(){}
	public ObjectDataDefinition(long id){
		this.id = id;
	}
	
	@Column(name="DATA_TYPE_ID")
	public Integer getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(Integer dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	@Column(name="UOM_ID")
	public Integer getUomId() {
		return uomId;
	}
	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}
	
	@Column(name="IS_DISPLAY")
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	
	@Column(name="DATA_LABEL")
	public String getDataLabel() {
		return dataLabel;
	}
	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}
	
	@Column(name="DATA_VALUE")
	 public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	
	@Column(name="IS_EDIT_IN_FIELD")
	public Integer getIsEditInField() {
		return isEditInField;
	}
	public void setIsEditInField(Integer isEditInField) {
		this.isEditInField = isEditInField;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_DEF_ID")
    public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}
	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}
}