package org.mlink.agent.model;
// Generated May 25, 2006 7:06:40 AM by Hibernate Tools 3.1.0.beta5


/**
 *        @hibernate.class
 *         table="JOB_ACTION"
 *         mutable="false"
 *     
 */
public class JobAction  implements java.io.Serializable {

    // Fields    
     private Long    id;
     private String  fieldCondition;
     private String  modifier;
     private String  verb;
     private String  name;
     private Integer sequence;
     
     private JobTask jobTask;
     private Action  action;

     // Constructors

    /** default constructor */
    public JobAction() {}
    /** full constructor */
    public JobAction(Long id, String fieldCondition, JobTask jobTask, Action action) {
       this.id = id;
       this.fieldCondition = fieldCondition;
       this.jobTask = jobTask;
       this.action = action;
    }
    
   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="sequence"
     *             column="ID"
     *            @hibernate.generator-param
     *             name="sequence"
     *             value="job_action_seq"
     *         
     */
    public Long getId() {return this.id;}
    public void setId(Long id) {this.id = id;}
    /**       
     *            @hibernate.property
     *             column="FIELD_CONDITION"
     */
    public String getFieldCondition() {return this.fieldCondition;}
    public void setFieldCondition(String fieldCondition) {this.fieldCondition = fieldCondition;}
    /**       
     *            @hibernate.property
     *             column="MODIFIER"
     */
    public String getModifier() {return this.modifier;}
    public void setModifier(String s) {this.modifier = s;}
    /**       
     *            @hibernate.property
     *             column="VERB"
     */
    public String getVerb() {return this.verb;}
    public void setVerb(String s) {this.verb = s;}
    /**       
     *            @hibernate.property
     *             column="NAME"
     */
    public String getName() {return this.name;}
    public void setName(String s) {this.name = s;}
    /**       
     *            @hibernate.property
     *             column="SEQUENCE"
     */
    public Integer getSequence() {return this.sequence;}
    public void setSequence(Integer i) {this.sequence = i;}
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="JOB_TASK_ID"    
     */
    public JobTask getJobTask() {return this.jobTask;}
    public void setJobTask(JobTask jobTask) {this.jobTask = jobTask;}
    /**       
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ACTION_ID"   
     */
    public Action getAction() {return this.action;}
    public void setAction(Action action) {this.action = action;}
    
    public static JobAction copyProperties(Action action){
    	JobAction jobAction = new JobAction();
    	jobAction.setModifier(action.getModifier());
    	jobAction.setVerb(action.getVerb());
    	jobAction.setName(action.getName());
    	jobAction.setSequence(action.getSequence());
    	return jobAction;
    }
   
  public String toString() {
       return "{ "+verb +" "+name + " "+modifier+" }" ;
  }
}


