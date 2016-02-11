package org.mlink.agent.model;
//Generated May 25, 2006 7:06:40 AM by Hibernate Tools 3.1.0.beta5


import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="PROJECT"
 *     
 */
public class Project  implements java.io.Serializable {

	// Fields
	private Long id;
	private Timestamp createdDate;
	private Timestamp latestStartDate;
	private Timestamp earliestStartDate;
	private Timestamp finishbyDate;
	private String createdBy;
	private String name;
	private ProjectTypeRef projectTypeRef;
	private Set jobs = new HashSet(0);

	// Constructors
	public Project() {}
	public Project(Long id) {this.id = id;}


	// Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="sequence"
     *             column="ID"
     *            @hibernate.generator-param
     *             name="sequence"
     *             value="project_seq"
     *         
     */
	public Long getId() {return this.id;}
	public void setId(Long id) {this.id = id;}
	/**       
	 *            @hibernate.property
	 *             column="CREATED_DATE"
	 *         
	 */
	public Timestamp getCreatedDate() {return this.createdDate;}
	public void setCreatedDate(Timestamp createdDate) {this.createdDate = createdDate;}
	/**       
	 *            @hibernate.property
	 *             column="LATEST_START_DATE"
	 *         
	 */
	public Timestamp getLatestStartDate() {return this.latestStartDate;}
	public void setLatestStartDate(Timestamp latestStartDate) {this.latestStartDate = latestStartDate;}
	/**       
	 *            @hibernate.property
	 *             column="EARLIEST_START_DATE"
	 */
	public Timestamp getEarliestStartDate() {return this.earliestStartDate;}
	public void setEarliestStartDate(Timestamp earliestStartDate) {this.earliestStartDate = earliestStartDate;}
	/**       
	 *            @hibernate.property
	 *             column="FINISHBY_DATE"
	 */
	public Timestamp getFinishbyDate() {return this.finishbyDate;}
	public void setFinishbyDate(Timestamp finishbyDate) {this.finishbyDate = finishbyDate;}
	/**       
	 *            @hibernate.property
	 *             column="CREATEDBY"
	 */
	public String getCreatedBy() {return this.createdBy;}
	public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
	/**       
	 *            @hibernate.property
	 *             column="NAME"
	 */
	public String getName() {return this.name;}
	public void setName(String name) {this.name = name;}
	/**       
	 *            @hibernate.many-to-one
	 *             not-null="true"
	 *            @hibernate.column name="PROJECT_TYPE_ID"         
	 *         
	 */
	public ProjectTypeRef getProjectTypeRef() {return this.projectTypeRef;}
	public void setProjectTypeRef(ProjectTypeRef projectTypeRef) {this.projectTypeRef = projectTypeRef;}
	/**       
	 *            @hibernate.set
	 *             lazy="true"
	 *             inverse="true"
	 *             cascade="none"
	 *            @hibernate.collection-key
	 *             column="PROJECT_ID"
	 *            @hibernate.collection-one-to-many
	 *             class="org.mlink.agent.model.Job"
	 *         
	 */
	public Set getJobs() {return this.jobs;}
	public void setJobs(Set jobs) {this.jobs = jobs;}

	public static Project copyProperties(ProjectSpec ps) {
		Project p = null;
		if (ps==null) return p;
		p = new Project();
		p.setCreatedBy(ps.getCreatedBy());
		p.setCreatedDate(ps.getCreatedDate());
		return p;
	}
}


