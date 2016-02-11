package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="SCHEDULE_RESPONSIBILITY_REF"
 *         mutable="false"
 *     
 */
public class ScheduleResponsibilityRef {
	
  public static final String SYSTEM = "System";
    public static final String MANUAL = "Manual";

	
    // Fields
     private Integer id;
     private String  code;

     // Constructors
    public ScheduleResponsibilityRef() {}
   
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
     *             column="CODE"
     */
    public String getCode() {return this.code;}
    public void setCode(String code) {this.code = code;}
    

    public static ScheduleResponsibilityRef getRef(String code) {
    	ScheduleResponsibilityRef ref = new ScheduleResponsibilityRef();
    	ref.setCode(code);
    	int id = org.mlink.iwm.lookup.ScheduleResponsibilityRef.getIdByCode(code);
    	ref.setId(id);
    	return ref;
    }
}
