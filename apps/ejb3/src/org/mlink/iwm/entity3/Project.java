/*-----------------------------------------------------------------------------------
	File: Project.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name="PROJECT")
@SequenceGenerator(name="PROJECT_SEQ_GEN", sequenceName="PROJECT_SEQ",allocationSize = 1)
public class Project implements Serializable,BaseEntity {
	private static final Logger logger = Logger.getLogger(Project.class);

	public long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "PROJECT_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }

	private String name; 
	private Date createdDate;
	private Date completedDate;
	private Date earliestStartDate;
	private Date latestStartDate;
	private Date finishbyDate;
	private String createdBy;
	private Integer projectTypeId;
	private Long parentId;
	private Long organizationId;
	private Date archivedDate;
	private Integer statusId;
	private Date startedDate;
	private String description;
	private Long sequenceId;
    private Collection<Job> jobs;
	
    public Project(Sequence projectStencil)  {
        setSequenceId(projectStencil.getId());
        setName(projectStencil.getName());
        setDescription(projectStencil.getDescription());
        setProjectTypeId(projectStencil.getProjectTypeId());
        setOrganizationId(projectStencil.getOrganizationId());
    }
    
    public Project() {	
	}
    public Project(long id){
    	this.id = id;
    }
    
	@Column(name="NAME")
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    @Column(name="CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="COMPLETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	@Column(name="EARLIEST_START_DATE")
    @Temporal(TemporalType.DATE)
    public Date getEarliestStartDate() {
		return earliestStartDate;
	}
	public void setEarliestStartDate(Date earliestStartDate) {
		this.earliestStartDate = earliestStartDate;
	}
	
	@Column(name="LATEST_START_DATE")
    @Temporal(TemporalType.DATE)
    public Date getLatestStartDate() {
		return latestStartDate;
	}
	public void setLatestStartDate(Date latestStartDate) {
		this.latestStartDate = latestStartDate;
	}
	
	@Column(name="FINISHBY_DATE")
    @Temporal(TemporalType.DATE)
    public Date getFinishbyDate() {
		return finishbyDate;
	}
	public void setFinishbyDate(Date finishbyDate) {
		this.finishbyDate = finishbyDate;
	}
	
	@Column(name="CREATEDBY")
    public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	
	@Column(name="ORGANIZATION_ID")
    public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Column(name="ARCHIVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="STATUS_ID")
    public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	@Column(name="STARTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartedDate() {
		return startedDate;
	}
	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}
	
	@Column(name="DESCRIPTION")
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="SEQUENCE_ID")
    public Long getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="project")
	public Collection<Job> getJobs() {
		return jobs;
	}
	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}
}