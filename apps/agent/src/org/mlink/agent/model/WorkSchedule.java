package org.mlink.agent.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="WORK_SCHEDULE"
 *     
 */
public class WorkSchedule  implements java.io.Serializable {

    // Fields    

     private Long     id;
     private Timestamp day;
     private Integer  notschedulable;
     private Integer  time;
     private BigDecimal utilityRating;
     private String   statusCode;
     private WorkScheduleStatusRef  statusRef;
     private Locator  locator;
     private Set      jobSchedules = new HashSet(0);
     private Person   person;
     private ShiftRef shiftRef;
     private Timestamp archivedDate;

     // Constructors
    public WorkSchedule() {}
    public WorkSchedule(Long id) {this.id = id;}
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="ARCHIVED_DATE"
     */
    public Timestamp getArchivedDate() {return this.archivedDate;}
    public void setArchivedDate(Timestamp archivedDate) {this.archivedDate = archivedDate;}
    /**       
     *            @hibernate.property
     *             column="DAY"
     */
    public Timestamp getDay() {return this.day;}
    public void setDay(Timestamp day) {this.day = day;}
    /**       
     *            @hibernate.property
     *             column="NOTSCHEDULABLE"
     */
    public Integer getNotschedulable() {return this.notschedulable;}
    public void setNotschedulable(Integer notschedulable) {this.notschedulable = notschedulable;}
    /**       
     *            @hibernate.property
     *             column="TIME"
     */
    public Integer getTime() {
    	if (time==null) return 0;
    	return this.time;
    }
    public void setTime(Integer time) {this.time = time;}
    /**       
     *            @hibernate.property
     *             column="UTILITY_RATING"
     */
    public BigDecimal getUtilityRating() {return this.utilityRating;}
    public void setUtilityRating(BigDecimal utilityRating) {this.utilityRating = utilityRating;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *             column="STATUS_ID"
     */
    public WorkScheduleStatusRef getStatusRef() {return this.statusRef;}
    public void setStatusRef(WorkScheduleStatusRef status) {this.statusRef = status;}
    /**
     * Access method for use in the ShiftManager, which believes the status is
     * a three-character string. NOTE: The ShiftManager sets this value during the
     * course of its run. The status must be propagated to setStatusRef(WorkScheduleStatusRef statRef)
     * via a lookup, or the change made by the ShiftManager will be lost
     * 
     */
    public String getStatus(){
    	if (statusCode==null) 
    		try { // try to set status from the WorkScheduleStatusRef
    			statusCode = this.getStatusRef().getCode();
    		} catch (NullPointerException npe) { // Just in case
    		}
    	return statusCode;
    }
    /** 
     * Access method for use in the ShiftManager, which believes the status is
     * a three-character string. NOTE: The ShiftManager sets this value during the
     * course of its run. The status must be propagated to setStatusRef(WorkScheduleStatusRef statRef)
     * via a lookup, or the change made by the ShiftManager will be lost
     * 
     * @param code The new code for this Job
     */
    public void setStatus(String code) {statusCode=code;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *            @hibernate.column name="LOCATOR_ID"         
     *         
     */
    public Locator getLocator() {return this.locator;}    
    public void setLocator(Locator locator) {this.locator = locator;}
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="save-update"
     *             where="DELETED_TIME IS NULL"
     *            @hibernate.collection-key
     *             column="WORK_SCHEDULE_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.JobSchedule"
     *         
     */
    public Set getJobSchedules() {return this.jobSchedules;}
    public void setJobSchedules(Set jobSchedules) {this.jobSchedules = jobSchedules;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *            @hibernate.column name="PERSON_ID"  
     */
    public Person getPerson() {return this.person;}
    public void setPerson(Person person) {this.person = person;}
    /**       
     *            @hibernate.many-to-one
     *             lazy="false"
     *            @hibernate.column name="SHIFT_ID"         
     *         
     */
    public ShiftRef getShiftRef() {return this.shiftRef;}
    public void setShiftRef(ShiftRef shift) {this.shiftRef = shift;}
    
    public String toString() {
    	return "{WorkSchedule:: id:"+this.id+";day:"+this.day+";status:;"+
    	       "person:"+person+";"+
              "shiftRef"+shiftRef+";"+
    	       "jobschedule:"+this.jobSchedules+"}";
    }

}


