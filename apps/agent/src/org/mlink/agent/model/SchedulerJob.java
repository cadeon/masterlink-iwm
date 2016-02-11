package org.mlink.agent.model;

import java.sql.Date;

/**
 * @hibernate.class
 *  table="JOB"
 */
public class SchedulerJob {
	Long id;
	Date dispatchedDate;
	Date scheduledDate;
	Boolean sticky;
	
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
     *             column="DISPATCHED_DATE"
     */
    public Date getDispatchedDate() {return this.dispatchedDate;}
    public void setDispatchedDate(Date d) {this.dispatchedDate=d;}
    /**       
     *            @hibernate.property
     *             column="SCHEDULED_DATE"
     */
    public Date getScheduledDate() {return this.scheduledDate;}
    public void setScheduledDate(Date d) {this.scheduledDate=d;}
  /**       
     *            @hibernate.property
     *             type="yes_no"
     *             column="STICKY"
     */
    public Boolean getSticky() {return this.sticky;}
    public void setSticky(Boolean sticky) {this.sticky=sticky;}
    
    public static SchedulerJob copy(Job job) {
    	SchedulerJob sj = new SchedulerJob();
    	if(job.getDispatchedDate()!=null){
    		sj.setDispatchedDate((Date)(job.getDispatchedDate())); 
    	}
    	
    	sj.setId(job.getId());
    	if(job.getScheduledDate()!=null){
        	sj.setScheduledDate((Date)job.getScheduledDate());
    	}
    	sj.setSticky(job.getSticky());
    	return sj;    	
    }
}
