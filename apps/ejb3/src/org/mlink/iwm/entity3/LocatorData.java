/*-----------------------------------------------------------------------------------
	File: LocatorData.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name="LOCATOR_DATA")
@SequenceGenerator(name="LOCATOR_DATA_SEQ_GEN", sequenceName="LOCATOR_DATA_SEQ",allocationSize = 1)
public class LocatorData implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "LOCATOR_DATA_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private Integer dataTypeId;
	private Integer uomId;
	private String dataLabel;
	private String dataValue;
	private Locator locator;
	
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
	
	@ManyToOne()
	@JoinColumn(name = "LOCATOR_ID")
    public Locator getLocator() {
		return locator;
	}
	public void setLocator(Locator locator) {
		this.locator = locator;
	}
}