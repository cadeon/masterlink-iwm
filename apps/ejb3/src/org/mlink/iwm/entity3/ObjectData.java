/*-----------------------------------------------------------------------------------
	File: ObjectData.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.Constants;

@Entity
@Table(name="OBJECT_DATA")
@SequenceGenerator(name="OBJECT_DATA_SEQ_GEN", sequenceName="OBJECT_DATA_SEQ",allocationSize = 1)
public class ObjectData implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_DATA_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	public ObjectData(){
		
	}
	
	public ObjectData(ObjectDataDefinition dataDef){
		setCustom(Constants.CUSTOMIZED_NO);
        setObjectDataDef(dataDef);
        setIsEditInField(dataDef.getIsEditInField());
        setDataLabel(dataDef.getDataLabel());
        setDataTypeId(dataDef.getDataTypeId());
        setDataValue(dataDef.getDataValue());//   data value may be modified at instance level without making instance custom
        setUomId(dataDef.getUomId());
        setIsDisplay(dataDef.getIsDisplay());
    }
	
	private Integer dataTypeId;
	private Integer uomId;
	private String dataLabel;
	private String dataValue;
	private Integer custom;
	private Integer isDisplay;
	private Integer isEditInField;
	private ObjectDataDefinition objectDataDef;
	private ObjectEntity object;
	
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
	
	@Column(name="CUSTOM")
	public Integer getCustom() {
		return custom;
	}
	public void setCustom(Integer custom) {
		this.custom = custom;
	}
	
	@Column(name="IS_DISPLAY")
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	
	@Column(name="IS_EDIT_IN_FIELD")
	public Integer getIsEditInField() {
		return isEditInField;
	}
	public void setIsEditInField(Integer isEditInField) {
		this.isEditInField = isEditInField;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_DATA_DEF_ID")
    public ObjectDataDefinition getObjectDataDef() {
		return objectDataDef;
	}
	public void setObjectDataDef(ObjectDataDefinition dataDefinition) {
		this.objectDataDef = dataDefinition;
	}

	@ManyToOne()
	@JoinColumn(name = "OBJECT_ID")
    public ObjectEntity getObject() {
		return object;
	}
	public void setObject(ObjectEntity object) {
		this.object = object;
	}

//KEEPMESTART
    /**
     * Sync up properties with the ObjectDataDefinition
     * Should be called of the ObjectDataDefinition change
     */
	public void synchronize() {
		ImplementationSF implementation = ServiceLocator.getImplementationSFLocal();
        if(Constants.CUSTOMIZED_NO.equals(getCustom())){
            ObjectDataDefinition dataDef = implementation.getDataDefinition(this);
            setDataLabel(dataDef.getDataLabel());
            setDataTypeId(dataDef.getDataTypeId());
            setDataValue(dataDef.getDataValue());   //data value may be modified at instance level without making instance custom
            setUomId(dataDef.getUomId());
            setIsDisplay(dataDef.getIsDisplay());
            implementation.update(dataDef);
        }
    }
//KEEPMEEND
}