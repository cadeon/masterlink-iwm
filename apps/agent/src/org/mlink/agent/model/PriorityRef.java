package org.mlink.agent.model;

/**
 *        @hibernate.class
 *         table="PRIORITY_REF"
 *         mutable="false"
 *     
 */
public class PriorityRef  implements java.io.Serializable {

	public static final String ZERO = "0";
	
    // Fields
     private Integer id;
     private String  code;

     // Constructors
    public PriorityRef() {}
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     *         
     */
    public Integer getId() {return this.id;}
    public void    setId(Integer id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="CODE"
     *      
     */
    public String getCode() {return this.code;}
    public void setCode(String code) {this.code = code;}
    

    public static PriorityRef getRef(String code) {
    	PriorityRef ref = new PriorityRef();
    	ref.setCode(code);
    	int id = org.mlink.iwm.lookup.PriorityRef.getIdByCode(code);
    	ref.setId(id);
    	return ref;
    }
}


