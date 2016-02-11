package org.mlink.iwm.entity3;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name="SYSTEM_PROPS")
@SequenceGenerator(name="SYSTEM_PROPS_SEQ_GEN", sequenceName="SYSTEM_PROPS_SEQ",allocationSize = 1)
public class SystemProp implements Serializable, BaseEntity {
	private static final Logger logger = Logger.getLogger(SystemProp.class);

	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SYSTEM_PROPS_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String property;
    private String value;
	private String description;
	
	@Column(name="PROPERTY", nullable = false)
    public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
	@Column(name="VALUE", nullable = false)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}