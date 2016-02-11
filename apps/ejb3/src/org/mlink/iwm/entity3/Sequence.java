/*-----------------------------------------------------------------------------------
	File: Sequence.java
	Package: org.mlink.iwm.entity
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="SEQUENCE")
@SequenceGenerator(name="SEQUENCE_SEQ_GEN", sequenceName="SEQUENCE_SEQ", allocationSize = 1)
public class Sequence implements Serializable,BaseEntity  {
	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQUENCE_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	public Sequence(){}
	public Sequence(long id){
		this.id = id;
	}
	
	private String description;
	private String createdBy;
	private Date createdDate;
	//private Collection<Task> tasks;
    private Long locatorId;
    private Date archivedDate;
    private Integer frequencyId;
    private Double frequencyValue;
    private Long organizationId;
    private String name;
    private Integer active;
    private Integer autoplanning;
    private Date startDate;
    private Date lastPlannedDate;
    private Integer projectTypeId;
    private Long parentId;
    List<TaskSequence> taskSequences;
	
    @Column(name="DESCRIPTION")
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="CREATEDBY")
    public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="LOCATOR_ID")
	public Long getLocatorId() {
		return locatorId;
	}
	public void setLocatorId(Long locatorId) {
		this.locatorId = locatorId;
	}
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="FREQUENCY_ID")
	public Integer getFrequencyId() {
		return frequencyId;
	}
	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}
	
	@Column(name="FREQUENCY_VALUE")
	public Double getFrequencyValue() {
		return frequencyValue;
	}
	public void setFrequencyValue(Double frequencyValue) {
		this.frequencyValue = frequencyValue;
	}
	
	@Column(name="ORGANIZATION_ID")
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="ACTIVE")
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	@Column(name="AUTOPLANNING")
	public Integer getAutoplanning() {
		return autoplanning;
	}
	public void setAutoplanning(Integer autoplanning) {
		this.autoplanning = autoplanning;
	}
	
	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="LAST_PLANNED_DATE")
	@Temporal(TemporalType.DATE)
	public Date getLastPlannedDate() {
		return lastPlannedDate;
	}
	public void setLastPlannedDate(Date lastPlannedDate) {
		this.lastPlannedDate = lastPlannedDate;
	}
	
	@Column(name="PROJECT_TYPE_ID")
	public Integer getProjectTypeId() {
		return projectTypeId;
	}
	public void setProjectTypeId(Integer projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
	
	@Column(name="PARENT_ID")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="sequence")
	@OrderBy("sequenceLevel")
    public List<TaskSequence> getTaskSequences() {
		return taskSequences;
	}
	public void setTaskSequences(List<TaskSequence> taskSequences) {
		this.taskSequences = taskSequences;
	}
}