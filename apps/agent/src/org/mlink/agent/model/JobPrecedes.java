package org.mlink.agent.model;

/**
 * 
 * @hibernate.class
 *  table="AGT_PRECEDES_SUM"
 * 
 */
public class JobPrecedes {

	private Long id;
	private Long projectId;
	private Integer incomplete;
	private Integer complete;
	
    // Constructors
    public JobPrecedes() {}
    
    // Property accessors
    /**       
     *            @hibernate.id 
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**
     * @hibernate.property
     *  column="PROJECT_ID"
     */
    public Long getProjectId(){return this.projectId;}
    public void setProjectId(Long pid){this.projectId=pid;}
    /**
     * @hibernate.property
     *  column="INCOMPLETE"
     */
    public Integer getIncomplete(){return this.incomplete;}
    public void    setIncomplete(Integer i){this.incomplete=i;}
    /**
     * @hibernate.property
     *  column="complete"
     */
    public Integer getComplete(){return this.complete;}
    public void    setComplete(Integer i){this.complete=i;}
}
