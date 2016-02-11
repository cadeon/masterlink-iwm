package org.mlink.agent.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.Person;
import org.mlink.agent.model.WorkSchedule;
import org.mlink.agent.util.AgentConfig;

import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.OrganizationRef;

public class ScheduleWrapper implements Comparable<ScheduleWrapper> {
	private static final Logger logger = Logger.getLogger(ScheduleWrapper.class);
	
	private static final int LEVELS = 5;
	
	private Collection<Job>  jobs;
	private int              levelTime;
	private Map<Long,String> locators;
	private int              maxSkillLevel;
	private Long 			 organizationId;
	private int              productiveTime;
	private int              priorityTime;
	private int              travelTime;
	private WorkSchedule     schedule;
	private Map<Integer,Integer>skillLevels;
	private String           reason;
	private boolean          changed = false;

	public ScheduleWrapper() {
		jobs = new HashSet<Job>();
		locators = new HashMap<Long,String>();
		skillLevels = new HashMap<Integer,Integer>();
		
		levelTime=0;
		productiveTime=0;
		priorityTime=0;
		travelTime=0;
	}
	
	public ScheduleWrapper clone() {
		ScheduleWrapper copy = new ScheduleWrapper();
		copy.jobs.addAll(this.jobs);
		copy.levelTime=this.levelTime;
		copy.locators.putAll(this.locators);
		copy.maxSkillLevel=this.maxSkillLevel;
		copy.organizationId=this.organizationId;
		copy.priorityTime=this.priorityTime;
		copy.productiveTime=this.productiveTime;
		copy.travelTime=this.travelTime;
		copy.schedule=this.schedule;
		copy.skillLevels.putAll(this.skillLevels);
		return copy;
	}
	/**
	 * Assign the specified job to this work schedule. This is the expected way to add
	 * a job to a schedule, as it calculates productivity and travel time values for
	 * the schedule.
	 * 
	 * @param job
	 */
	public void assign(Job job) {
	    jobs.add(job);
	    locators.put(job.getLocator().getId(),job.getLocator().getFullLocator());
	    travelTime = getTravelTime();
	    // Account for a long job by using schedule.time instead of job.estimatedTime
	    int estTime = (job.getEstimatedTime()>schedule.getTime()?schedule.getTime():job.getEstimatedTime());
	    productiveTime += estTime;
	    priorityTime   += estTime * Integer.parseInt(job.getPriorityRef().getCode());
	    levelTime      += estTime * job.getSkillLevelRef().getValue();
	}
    
	/**
	 * Determine if job assignment can be made, ignoring time on the schedule. Also assume
	 * this is not the initial assignment pass.
	 * 
	 * @param job The job to consider
	 * @return true if the job can be put on the schedule. Otherwise false
	 */
	public boolean canAssign(Job job){return canAssign(job,false);}
	/**
	 * Determine if job assignment can be made, ignoring time on the schedule, for
	 * the specified job and assignment indicator
	 * 
	 * @param job The job to consider
	 * @param initial Is this an initial assignment
	 * @return true if the job can be put on the schedule. Otherwise false
	 */
	public boolean canAssign(Job job, boolean initial){return canAssign(job,initial,false);}
	/**
	 * Determine if job assignment can be made for the specified job, assignment indicator,
	 * and the importance of considering available time on the schedule
	 * 
	 * @param job The job to consider
	 * @param initial Is this initial assignment
	 * @param ignoreTime Pay attention to time availability on schedule
	 * @return true if the job can be put on the schedule. Otherwise false
	 */
	public boolean canAssign(Job job,boolean initial, boolean ignoreTime) {
		if (!assignable(job,initial,ignoreTime)) return false;
		reason="";
		return true;
	}
	
	public boolean canRemove(Job job) {
		reason="sticky";
        if (job.getSticky()!=null && job.getSticky()) return false;
		reason="job state";
		if (!legalState(job.getStatusRef().getCode())) return false;
		//reason="skill type/level";
		if (!skillLevelOk(job.getSkillTypeRef().getId(),job.getSkillLevelRef().getValue())) {
			StringBuffer sb = new StringBuffer("job skill:");
			sb.append(job.getSkillTypeRef().getId()).append("; job skill level: ").append(job.getSkillLevelRef().getValue());
			Integer sl= getSkillLevel(job.getSkillTypeRef().getId());
			sb.append((sl!=null?". Schedule skill level: "+ sl:". Person does not have this skill") );
			reason=sb.toString();
			return false;
		}
		reason="";
		return true;		
	}
	
	/**
	 * Creates a clone that replicates the job collection and recalculates time values 
	 * (level, priority, productivity, and travel) minus the job specified.
	 * 
	 * @param job
	 * @return A ScheduleWrapper "clone" minus the specified job.
	 * @throws Exception
	 */
	public ScheduleWrapper without(Job job) throws Exception {
		if(job.getSticky()!=null && job.getSticky()) throw new Exception("Cannot remove sticky job");
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setWorkSchedule(this.schedule);
		sw.setSkillLevels(this.skillLevels);
		// Must reassign jobs (except for one being removed)
		// so that levelTime, priorityTime, productiveTime,
		// and travelTime are calculated properly.
		for (Job j : jobs) {
			if (!job.getId().equals(j.getId()))
				sw.assign(j);
		}
		return sw;
	}
	
	public boolean validSkills(Job job) {
		Integer skillTypeId = job.getSkillTypeRef().getId();
		Integer skillLevel = job.getSkillLevelRef().getValue();
		return skillLevelOk(skillTypeId,skillLevel);
	}
	
	public boolean isChanged(){return this.changed;}
	
    // Getters/Setters
	public int getAvailableTime() {return getTotalTime()-productiveTime-travelTime;}
	public boolean getChanged(){return this.changed;}
	public Long getId(){return schedule.getId();}
	public Collection<Job> getJobs(){return jobs;}
	public Long getOrganizationId(){return organizationId;}
	public void setOrganizationId(Long id){organizationId=id;}
	public int getMaxSkillLevel(){return maxSkillLevel;}
	/** 
	 * Get list of jobs assigned to this schedule on the previous run of the Scheduler
	 * 
	 * @return Collection of jobs
	 */
	public Set<Job> getJobsAssignedLastRun(){
		Set<Job> prevJobs = new HashSet<Job>();
		if (schedule==null) return prevJobs;
		Set<JobSchedule> jobSchedules = schedule.getJobSchedules();
		for (JobSchedule js:jobSchedules){
			Job job = js.getJob();
			prevJobs.add(job);
		}
		return prevJobs;
	}
	public int getProductiveTime(){return productiveTime;}
	public int getPriorityTime(){return priorityTime;}
	public String getReason(){return reason;}
	public Integer getShiftId(){
		if (schedule==null) return null;
		return schedule.getShiftRef().getId();
	}
	public Integer getSkillLevel(Integer skillTypeId){return skillLevels.get(skillTypeId);}
	public Map<Integer,Integer> getSkillLevels(){return skillLevels;}
	public int getTotalTime(){ return schedule.getTime();}
	/**
	 * Calculate travel time between jobs on the schedule. We don't
	 * calculate, and perhaps can't know, how long it takes the worker,
	 * from wherever they are when they start their day, to get to the first job.
	 * We are just looking at travel time between jobs on the schedule.
	 * 
	 * @return The travel time between the locators for jobs on this schedule
	 */
	public int getTravelTime(){ return getTravelTime(null);}
	/**
	 * Calculate travel time between jobs on the schedule. We don't
	 * calculate, and perhaps can't know, how long it takes the worker,
	 * from wherever they are when they start their day, to get to the first job.
	 * We are just looking at travel time between jobs on the schedule.
	 * 
	 * @param locator A locator to include in the calculation that is NOT included in the schedule's locators
	 * @return The travel time between the locators for jobs on this schedule; also includes the specified locator
	 */
	public int getTravelTime(Locator locator){
		// handle empty schedule
		if (locator==null && locators.size()==0) return 0;
		
		Map<Long,String[]> tokenized = new HashMap<Long,String[]>();
		// Prepare data: divide the levels on the full locator into tokens;
		// the levels are delimited by "."
		if (locator!=null) tokenized.put(locator.getId(), getTokens(locator.getFullLocator()));
		
		Set<Long> locatorIds = locators.keySet();
		for(Long id : locatorIds) {
			tokenized.put(id, getTokens(locators.get(id)));
		}
		// Count # of changes at each level
		int[] count = new int[LEVELS];
		Set<Long> keys = tokenized.keySet();
		for (int i = 0; i<LEVELS; i++ ) { // Currently there are 5 levels
			count[i]=0;
			List<String> seenit = new ArrayList<String>();
			for(Long key:keys) {
				String[] list = tokenized.get(key);
				// If we have > LEVELS levels, throw an exception b/c this code should be changed
				if (list.length > LEVELS ) throw new RuntimeException("More than "+ LEVELS +" level(s) in locator "+ key);
				if (list.length<i+1) continue; // already processed all levels in this list
				if (!seenit.contains(list[i])) {
					seenit.add(list[i]); //add the locator name to those seen at this level
					count[i]++;          //increment # of new locators seen at this level
				}
			}
		}
		// Minus one to account for the location of the first job - 
		for (int i = 0; i<LEVELS; i++ ) { 
			if (count[i]>0) count[i]--;
		}
		int location = count[0];
		int building = count[1];
		int zone     = count[2];
		int floor    = count[3];
		int room     = count[4];
		return location*AgentConfig.locationConst() + 
			   building*AgentConfig.buildingConst() + 
			   zone*AgentConfig.zoneConst() + 
			   floor*AgentConfig.floorConst() + 
			   room*AgentConfig.roomConst(); 
	} 
	public String getWorkerName(){return schedule.getPerson().getUsername();}
	public WorkSchedule getWorkSchedule(){return schedule;}

	public void setChanged(boolean b){this.changed=b;}

	public void setJobs(Collection<Job> jobs){this.jobs = jobs;}
	public void setProductiveTime(int i){this.productiveTime=i;}
	public void setPriorityTime(int i){this.priorityTime=i;}
	public void setSkillLevels(Map<Integer,Integer> levels){skillLevels=levels;}
	public void setWorkSchedule(WorkSchedule ws){schedule=ws;}

	public void addSkillLevel(Integer skillTypeId,int skillLevel){
		Integer level = skillLevels.get(skillTypeId);
		if (level==null) level=0;
		if (level.intValue()<skillLevel) {
			level=skillLevel;
			if (skillLevel > maxSkillLevel) maxSkillLevel = skillLevel;
			skillLevels.put(skillTypeId, skillLevel);
		}
	}
	
	// Helpers
	private boolean assignable(Job job,boolean initial,boolean ignoreTime) {
		//TODO: Not checking worker type here. Likely should? See #262
		if (initial) {
			reason = "Not preassigning - multiworker job";
			if (job.getNumberOfWorkers()>1) {
				log("Not preassigning "+ job.getId() +". Is a multiworker job");
				return false; // N>1 worker jobs scheduled by Multi, not during initial pass
			}
			Integer strid = job.getSkillTypeRef().getId();
			Integer slrid = job.getSkillLevelRef().getValue();
			reason = "Job/person Org mismatch";
			//If there is an org mismatch AND the job is not sticky, fail.
			if (!containsOrganization(job.getOrganizationId())) {
				if (!(job.getSticky()!=null && job.getSticky())){
					return false;	
				}
			}
			reason = "Job/person skill mismatch";
			if (!skillLevelOk(strid,slrid)) {
				log("Job/person skill mismatch. Cleaning up job "+ job.getId());
				job.setSticky(Boolean.FALSE); //Sticky DOES NOT override a skill mismatch.
				return false;
			}
			return true;
		}
		
		//If it is sticky, it should have been assigned in pre-assign.
		reason="sticky";
		 if (job.getSticky()!=null && job.getSticky()) return false;
		
		
		reason="job state";
		if (!legalState(job.getStatusRef().getCode())) return false;
		//reason="skill type/level";
		Integer strid = job.getSkillTypeRef().getId();
		Integer slrid = job.getSkillLevelRef().getValue();
		if (!skillLevelOk(strid,slrid)) {
			StringBuffer sb = new StringBuffer("job skill:");
			sb.append(job.getSkillTypeRef().getId()).append("; job skill level: ").append(job.getSkillLevelRef().getValue());
			Integer sl= getSkillLevel(job.getSkillTypeRef().getId());
			sb.append((sl!=null?". Schedule skill level: "+ sl:". Person does not have this skill") );
			reason=sb.toString();
			return false;
		}
		if(!containsLocator(job.getLocator())) {
			StringBuffer sb = new StringBuffer("schedule locator: "+ schedule.getLocator().getId());
			sb.append(System.getProperty("line.separator"));
			sb.append("job top-most parent locator: "+ job.getLocator().getTopParentId());
			reason=sb.toString();
			return false;
		}
		reason="organization";
		if(!containsOrganization(job.getOrganizationId())) return false;
		reason="time";
		if (ignoreTime) return true;
	    Integer travTime  = getTravelTime(job.getLocator());
	    Integer totalTime = schedule.getTime();
	    // Next, account for long jobs
	    Integer jobEstTime= (job.getEstimatedTime()>schedule.getTime()?schedule.getTime():job.getEstimatedTime());
	    if (job.getId().equals(57705)) {
	    	log("productive time: "+ productiveTime);
	    	log("travel time: "+ travTime);
	    	log("job est time: "+ jobEstTime);
	    	log("total time: "+ totalTime);
	    }
	    Integer timeUsed  = productiveTime + travTime + jobEstTime;
	    if (totalTime < timeUsed) return false;
	    return true;
	}
	
	private boolean containsLocator(Locator locator) {
		Locator schedLocator = schedule.getLocator();
		if (locator.getId().equals(schedLocator.getId())) return true;
		if (locator.getTopParentId().equals(schedLocator.getId())) return true;
		return false;
	}
	private boolean containsOrganization(Long jobOrgId) {
		Person p = this.getWorkSchedule().getPerson();
		Long orgId = p.getOrganizationId();
		
		if (jobOrgId==null) 
			return true; // Job with null org can be assigned 
		
		// Check for equal
		if (orgId.equals(jobOrgId)) return true;
		
		Long parentId = OrganizationRef.getParentId(orgId);
		while (parentId != null){
			if (parentId.equals(jobOrgId)) {
				//Found it
			return true;
			} else {
				parentId=OrganizationRef.getParentId(parentId); //didn't find it, try the next one up.
			}
			
		}
		// If we made it here we found nothing
		log("Doesn't fit, Org");
		log("Job requires " + OrganizationRef.getLabel(jobOrgId) + jobOrgId);
		log("Worker has " + OrganizationRef.getLabel(organizationId) + organizationId);
		
		return false;
	}
	
	private boolean legalState(String statusCode) {
		boolean rfs = JobStatusRef.Status.RFS.equals(statusCode);
		boolean dpd = JobStatusRef.Status.DPD.equals(statusCode);
		return rfs || dpd; //JobStatusRef.RFS.equals(statusCode) || JobStatusRef.DPD.equals(statusCode);
	}
	private boolean skillLevelOk(Integer skillTypeId,Integer skillLevel) {
		if (skillLevels.get(skillTypeId)==null) return false;
		if (skillLevels.get(skillTypeId).compareTo(skillLevel) < 0) return false;
		log("job skill type id: "+ skillTypeId +"; skill level: "+ skillLevel);
		log("Person skills/levels:");
		log(skillLevels);
		return true;
	}
	private String[] getTokens(String s) {
		StringTokenizer st = new StringTokenizer(s,".");
		String[] tokens = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			tokens[i++]=st.nextToken();
		}
		return tokens;
	}

	public int compareTo(ScheduleWrapper wrapper) {
		if (wrapper.getWorkSchedule()==null) throw new NullPointerException("Tried to compare to null work schedule. Id was "+ wrapper.getId());
		if (wrapper.getWorkSchedule().getShiftRef()==null) throw new NullPointerException("Tried to compare to null shift ref on work schedule "+ wrapper.getId());
		// Note on the following check, we don't check to see where we are in the day; 
		// we just want to order by the start time
		int retval = this.getWorkSchedule().getShiftRef().compareTo(wrapper.getWorkSchedule().getShiftRef());
		// if shifts are equal, put the specified wrapper after this one
		return (retval==0?1:retval);
	}
	
	public String toString() {
    StringBuffer sb = new StringBuffer( schedule==null ? "" : schedule.toString());
    sb.append(" skills:[");
    sb.append(skillLevels.toString());
		sb.append("];levelTime:"+levelTime);
		sb.append(";productiveTime:"+productiveTime).append(";priorityTime:"+priorityTime);
		sb.append(";travelTime:"+travelTime);
		sb.append(";jobs:[");
		sb.append(jobs.toString());
		sb.append("]}");
		return sb.toString();
	}
	private void log(Object o) {logger.debug(o);}
	private void error(Object o,Throwable t){logger.error(o,t);}
}
