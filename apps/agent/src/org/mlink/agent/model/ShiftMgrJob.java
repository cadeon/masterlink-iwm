package org.mlink.agent.model;

import java.sql.Date;

/**
 * @hibernate.class
 * table="JOB"
 *
 */
public class ShiftMgrJob {

	Long id;
	Date dispatchedDate;
	Date scheduledDate;
	
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
    
    public static ShiftMgrJob copy(Job job) {
    	ShiftMgrJob sj = new ShiftMgrJob();
    	sj.setDispatchedDate((job.getDispatchedDate()!=null)?(Date)job.getDispatchedDate():null);
    	sj.setId(job.getId());
    	sj.setScheduledDate((job.getScheduledDate()!=null)?(Date)job.getScheduledDate():null);
    	return sj;    	
    }
}
