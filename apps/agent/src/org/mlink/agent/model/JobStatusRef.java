package org.mlink.agent.model;


/**
 *        @hibernate.class
 *         table="JOB_STATUS_REF"
 *         mutable="false"
 */
public class JobStatusRef  implements java.io.Serializable {

	public enum Status {CIA, DJO, DPD, DUN, EJO, INS, NYA, PJO, RFS, SPD, VSJ, WFP, WFS, WSR;
		public boolean equals(String str){
	        return this.toString().equals(str);
	    }
	}
	   
    // Fields    
	private Integer id;
	private String  code;
	private String  description;    
   
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
     * 			   update="false"
     * 			   insert="false"
     *             column="CODE"
     */
    public String getCode() {return this.code;}
    public void setCode(String code) {this.code = code;}
    /**       
     *            @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="DESCRIPTION"
     */
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
    
    public static JobStatusRef getRef(String code) {
    	JobStatusRef ref = new JobStatusRef();
    	ref.setCode(code);
    	int id = org.mlink.iwm.lookup.JobStatusRef.getIdByCode(code);
    	ref.setDescription(org.mlink.iwm.lookup.JobStatusRef.getLabel(id));
    	ref.setId(id);
    	return ref;
    }
}