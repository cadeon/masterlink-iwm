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
@Table(name="SHIFT_REF")
@SequenceGenerator(name="SHIFT_REF_SEQ_GEN", sequenceName="SHIFT_REF_SEQ",allocationSize = 1)
public class ShiftRef implements Serializable, BaseEntity {
	private static final Logger logger = Logger.getLogger(ShiftRef.class);

	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SHIFT_REF_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String code;
	private String description;
	private Integer shiftStart;
	private Integer shiftEnd;
	private Integer dispOrd;
	private Integer time;

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
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="SHIFTSTART")
	public Integer getShiftStart() {
		return shiftStart;
	}
	public void setShiftStart(Integer shiftStart) {
		this.shiftStart = shiftStart;
	}
	
	@Column(name="SHIFTEND")
	public Integer getShiftEnd() {
		return shiftEnd;
	}
	public void setShiftEnd(Integer shiftEnd) {
		this.shiftEnd = shiftEnd;
	}
	
	@Column(name="DISP_ORD")
	public Integer getDispOrd() {
		return dispOrd;
	}
	public void setDispOrd(Integer dispOrd) {
		this.dispOrd = dispOrd;
	}
	
	@Column(name="TIME")
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
}