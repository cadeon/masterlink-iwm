package org.mlink.agent.model;

import java.sql.Date;


/**
 *        @hibernate.class 
 *         table="JOBVIEW"
 *         mutable="false"
 *         
 *     
 */
public class JobView {

	Long    id;
	Long    jobid;
	String  priority;
	String  type;
	String  description;
	Date    lastUpdate;
	Date    created;
	Date    completed;
	Date    dispatched;
	Date    earliestStart;
	Date    latestStart;
	Date    finishBy;
	String  status;
	Integer estTime;
	String  skillType;
	String  skillLevel;
	Integer workerActive;
	Long    workSchedule;
	String  securityLevel;
	Long    locator;
	Long    party;
	Integer preceeds;
	Integer incomplete;
	String  ready;
	String  scheduleResponsibility;
	Integer numWorkers;
	String  scheduleStatus;
	Date    scheduleDate;
	Date    scheduled;
	Date    started;
	Integer time;
	
    // Property accessors
    /**       
     *             @hibernate.id
     *             generator-class="assigned"
     *             column="OBJECT_ID"
     *         
     */
	public Long  getId(){return id;}
    /**       
     *             @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="JOBID"
     *         
     */
	public Long  getJobId(){return jobid;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="PRIORITY"
     */
	public String  getPriority(){return priority;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="TYPE"
     */
	public String  getType(){return type;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="DESCRIPTION"
     */
	public String  getDescription(){return description;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="LASTUPDATE"
     */
	public Date    getLastUpdate(){return lastUpdate;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="CREATED"
     */
	public Date    getCreated(){return created;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="COMPLETED"
     */
	public Date    getCompleted(){return completed;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="DISPATCHED"
     */
	public Date    getDispatched(){return dispatched;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="EARLIESTSTART"
     */
	public Date    getEarliestStart(){return earliestStart;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="LATESTSTART"
     */
	public Date    getLatestStart(){return latestStart;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="FINISHBY"
     */
	public Date    getFinishBy(){return finishBy;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="STATUS"
     */
	public String  getStatus(){return status;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="ESTIMATEDTIME"
     */
	public Integer getEstTime(){return estTime;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SKILLTYPE"
     */
	public String  getSkillType(){return skillType;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SKILLLEVEL"
     */
	public String  getSkillLevel(){return skillLevel;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="WORKERACTIVE"
     */
	public Integer getWorkerActive(){return workerActive;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="WORKSCHEDULE"
     */
	public Long    getWorkSchedule(){return workSchedule;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SECURITYLEVEL"
     */
	public String  getSecurityLevel(){return securityLevel;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="LOCATOR"
     */
	public Long    getLocator(){return locator;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="PARTY"
     */
	public Long    getParty(){return party;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="PRECEEDS"
     */
	public Integer getPreceeds(){return preceeds;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="INCOMPLETE"
     */
	public Integer getIncomplete(){
		if (incomplete==null) return 0;
		return incomplete;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="READY"
     */
	public String  getReady(){return ready;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SCHEDULERESPONSIBILITY"
     */
	public String  getScheduleResponsibility(){return scheduleResponsibility;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="WORKERS"
     */
	public Integer getNumWorkers(){return numWorkers;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SCHEDULESTATUS"
     */
	public String  getScheduleStatus(){return scheduleStatus;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SCHEDULEDATE"
     */
	public Date    getScheduleDate(){return scheduleDate;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="SCHEDULED"
     */
	public Date    getScheduled(){return scheduled;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="STARTED"
     */
	public Date    getStarted(){return started;}
    /**
     * 	          @hibernate.property
     * 			   update="false"
     * 			   insert="false"
     *             column="TIME"
     */
	public Integer getTime(){return time;}

	public void setId(Long lid){id=lid;}
	public void setJobId(Long lid){jobid=lid;}
	public void setPriority(String s){priority=s;}
	public void setType(String s){type=s;}
	public void setDescription(String s){description=s;}
	public void setLastUpdate(Date d){lastUpdate=d;}
	public void setCreated(Date d){created=d;}
	public void setCompleted(Date d){completed=d;}
	public void setDispatched(Date d){dispatched=d;}
	public void setEarliestStart(Date d){earliestStart=d;}
	public void setLatestStart(Date d){latestStart=d;}
	public void setFinishBy(Date d){finishBy=d;}
	public void setStatus(String s){status=s;}
	public void setEstTime(Integer i){estTime=i;}
	public void setSkillType(String s){skillType=s;}
	public void setSkillLevel(String s){skillLevel=s;}
	public void setWorkerActive(Integer i){workerActive=i;}
	public void setWorkSchedule(Long li){workSchedule=li;}
	public void setSecurityLevel(String s){securityLevel=s;}
	public void setLocator(Long li){locator=li;}
	public void setParty(Long li){party=li;}
	public void setPreceeds(Integer i){preceeds=i;}
	public void setIncomplete(Integer i){incomplete=i;}
	public void setReady(String s){ready=s;}
	public void setScheduleResponsibility(String s){scheduleResponsibility=s;}
	public void setNumWorkers(Integer i){numWorkers=i;}
	public void setScheduleStatus(String s){scheduleStatus=s;}
	public void setScheduleDate(Date d){scheduleDate=d;}
	public void setScheduled(Date d){scheduled=d;}
	public void setStarted(Date d){started=d;}
	public void setTime(Integer i){time=i;}
}
