package org.mlink.agent.model;

/** 
 * @hibernate.class
 * 	table="TASK"
 *
 */
public class PlannerTask {

	private Long id; 
	private Double runHoursThresh;
	
     // Constructors

    /** default constructor */
    public PlannerTask() {}

	/** minimal constructor */
    public PlannerTask(Long id) {id=this.id;} 
    
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id=id;}
    /**       
     *            @hibernate.property
     *             column="RUN_HOURS_THRESHOLD"
     */
    public Double getRunHoursThreshold() {return this.runHoursThresh;}
    public void setRunHoursThreshold(Double d) {this.runHoursThresh=d;}

}
