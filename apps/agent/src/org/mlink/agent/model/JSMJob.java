package org.mlink.agent.model;

/**
 * @hibernate.class
 *  table="JOB"
 */
public class JSMJob {
	Long id;
	Integer statusId;
	
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *            @hibernate.column name="STATUS_ID"         
     *         
     */
    public Integer getStatusId() {return statusId;}
    public void setStatusId(Integer statusId){this.statusId=statusId;}
}
