package org.mlink.agent.model;


/**
 *        @hibernate.class
 *         table="TASK_TYPE_REF"
 *         mutable="false"
 */
public class TaskTypeRef  implements java.io.Serializable {

    public static final String ROUTINE_MAINT    = "Routine";
    public static final String ASSESSMENT       = "Assessment";
    public static final String COMBINED         = "Combined";
    public static final String URGENT         = "Urgent";
    
    // Fields
     private Integer id;
     private String  description;
     private Integer dispOrd;
     private String  code;

     // Constructors
    public TaskTypeRef() {}

    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="assigned"
     *             column="ID"
     */
    public Integer getId() {return this.id;}
    public void setId(Integer id) {this.id = id;}
    /**       
     *            @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="DESCRIPTION"
     */
    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
    /**       
     *            @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="DISP_ORD"
     */
    public Integer getDispOrd() {return this.dispOrd;}
    public void setDispOrd(Integer dispOrd) {this.dispOrd = dispOrd;}
    /**       
     *            @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="CODE"
     */
    public String getCode() {return this.code;}
    public void setCode(String code) {this.code = code;}
}