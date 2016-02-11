package org.mlink.agent.model;

import java.util.HashSet;
import java.util.Set;

/**
 *        @hibernate.class
 *         table="PERSON"
 *         mutable="false"
 *     
 */
public class Person  implements java.io.Serializable {

    // Fields    
    private Long id;
    private Long organizationId;
    private String password;
    private Integer active;
    private Long partyId;
    private String username;
    private Long securityTypeRef;
    private Set workSchedules = new HashSet(0);
    private Set skills = new HashSet(0);
    private Long workerTypeRef;

     // Constructors
    public Person() {}
    
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
     *             column="ORGANIZATION_ID"
     */
    public Long getOrganizationId() {return this.organizationId;}
    public void setOrganizationId(Long organizationId) {this.organizationId = organizationId;}
    /**       
     *            @hibernate.property
     *             column="PASSWORD"
     */
    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}
    /**       
     *            @hibernate.property
     *             column="ACTIVE"
     */
    public Integer getActive() {return this.active;}
    public void setActive(Integer active) {this.active = active;}
    /**       
     *            @hibernate.property
     *             column="PARTY_ID"
     */
    public Long getPartyId() {return this.partyId;}
    public void setPartyId(Long partyId) {this.partyId = partyId;}
    /**       
     *            @hibernate.property
     *             column="USER_NAME"
     */
    public String getUsername() {return this.username;}
    public void setUsername(String username) {this.username = username;}
    /**       
     *            @hibernate.property
     *            @hibernate.column name="SECURITY_LEVEL_ID"     
     */
    public Long getSecurityTypeRef() {return this.securityTypeRef;}
    public void setSecurityTypeRef(Long securityTypeRef) {this.securityTypeRef = securityTypeRef;}
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="PERSON_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.WorkSchedule"
     */
    public Set getWorkSchedules() {return this.workSchedules;}
    public void setWorkSchedules(Set workSchedules) {this.workSchedules = workSchedules;}
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="PERSON_ID"
     *            @hibernate.collection-one-to-many
     *             class="org.mlink.agent.model.Skill"
     *         
     */
    public Set getSkills() {return this.skills;}
    public void setSkills(Set skills) {this.skills = skills;}
    /**       
     *            @hibernate.property
     *            @hibernate.column name="WORKER_TYPE_ID"      
     */
    public Long getWorkerTypeRef() {return this.workerTypeRef;}    
    public void setWorkerTypeRef(Long workerTypeRef) {this.workerTypeRef = workerTypeRef;}

  public String toString() {return username;}
  
  public String toVerboseString() {
	  return "{Person:: id:"+this.id+";name:"+this.username+";skills:"+this.skills+"}";
  }

  public boolean equals(Object o){
    if (((Person) o).getId().equals(id)) return true;
    return false;
  }

  public int hashCode() {return id.shortValue();}
}


