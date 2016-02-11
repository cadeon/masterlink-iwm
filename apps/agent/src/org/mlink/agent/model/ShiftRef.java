package org.mlink.agent.model;

import java.util.Calendar;

/** 
 * @hibernate.class
 *  table="SHIFT_REF"
 *  mutable="false"
 *
 */
public class ShiftRef implements Comparable<ShiftRef> {
	private static final Integer DEFAULT_SHIFT_LENGTH = 8*60; // 8 hour day * minutes in hour

	private Integer id;
	private String  code;
	private String  description;
	private Integer shiftStart;
	private Integer shiftEnd;
	private Integer time = DEFAULT_SHIFT_LENGTH;

    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Integer getId() {return this.id;}
    public void    setId(Integer id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             update="false"
     *	           insert="false"
     *             column="CODE"   
     */
    public String getCode() {return this.code;}
    public void setCode(String s) {this.code = s;}
    /**       
     *            @hibernate.property
     *             update="false"
     *	           insert="false"
     *             column="DESCRIPTION"   
     */
    public String getDescription() {return this.description;}
    public void setDescription(String s) {this.description = s;}
    /**       
     *            @hibernate.property
     *             update="false"
     *	           insert="false"
     *             column="SHIFTSTART"   
     *             
     * Shift start -- offset from 12:00am, in seconds
     */
    public Integer getShiftStart() {return this.shiftStart;}
    public void setShiftStart(Integer i) {this.shiftStart = i;}
    /**       
     *            @hibernate.property
     *             update="false"
     *	           insert="false"
     *             column="SHIFTEND"   
     */
    public Integer getShiftEnd() {return this.shiftEnd;}
    public void setShiftEnd(Integer i) {this.shiftEnd = i;}
    /**       
     *            @hibernate.property
     *             update="false"
     *	           insert="false"
     *             column="TIME"   
     */
    public Integer getTime() {return this.time;}
    public void setTime(Integer i) {this.time = i;}
    public String toString() {
    	return "{ShiftRef:: id:"+this.id+";description:"+this.description+"}";
    }


	public int compareTo(ShiftRef ref) {
		if (ref.getShiftStart()==null) throw new NullPointerException("Tried to compare to shift ref with null shift start. Id was "+ ref.getId());
		Calendar compareToTime = Calendar.getInstance();
		Calendar shiftTime = Calendar.getInstance();
		compareToTime.set(Calendar.HOUR_OF_DAY,ref.getShiftStart()/60);
		compareToTime.set(Calendar.MINUTE,ref.getShiftStart()%60);
		shiftTime.set(Calendar.HOUR_OF_DAY,this.getShiftStart()/60);
		shiftTime.set(Calendar.MINUTE,this.getShiftStart()%60);
		return shiftTime.compareTo(compareToTime);
	}
	
}
