package org.mlink.agent.model;

/**
 * @hibernate.class
 *  table="WORK_SCHEDULE_STATUS_REF"
 *  mutable="false"  
 *
 */
public class WorkScheduleStatusRef {
	
	public enum Status {A, DUN, I, IP, NYS, TO;
		public boolean equals(String str){
	        return this.toString().equals(str);
	    }
	}
	
	public static String schedulesTimeOut = "('"+WorkScheduleStatusRef.Status.NYS+"','"+WorkScheduleStatusRef.Status.DUN+"','"+WorkScheduleStatusRef.Status.TO+"' ) ";
	public static String batchUpdateSchedulesDone = "('"+WorkScheduleStatusRef.Status.DUN+"','"+WorkScheduleStatusRef.Status.NYS+"') ";
	// Fields
	private Integer id;
	private String description;
	private String code;
	
	/**
	 * @hibernate.id
	 *  generator-class="assigned"
	 * 
	 */
	public Integer getId(){return this.id;}
	public void setId(Integer id){this.id=id;}
	/**
	 * @hibernate.property
	 * 
	 */
	public String getDescription(){return this.description;}
	public void setDescription(String s){this.description=s;}
	/**
	 * @hibernate.property
	 * 
	 */
	public String getCode(){return this.code;}
	public void setCode(String s){this.code=s;}
	
    public static WorkScheduleStatusRef getRef(String code) {
    	WorkScheduleStatusRef ref = new WorkScheduleStatusRef();
    	ref.setCode(code);
    	int id = org.mlink.iwm.lookup.WorkScheduleStatusRef.getIdByCode(code);
    	ref.setDescription(org.mlink.iwm.lookup.WorkScheduleStatusRef.getLabel(id));
    	ref.setId(id);
    	return ref;
    }
}
