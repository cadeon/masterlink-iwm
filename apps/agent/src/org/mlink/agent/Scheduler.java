package org.mlink.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mlink.agent.algorithm.Algorithm;
import org.mlink.agent.algorithm.Multi;
import org.mlink.agent.algorithm.RemoveNegs;
import org.mlink.agent.algorithm.RescheduleDec;
import org.mlink.agent.algorithm.ScheduleWrapper;
import org.mlink.agent.algorithm.UtilityAlgo;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.JobStatusRef;
import org.mlink.agent.model.WorkSchedule;

public class Scheduler extends BaseAgent {
	private ArrayList<Algorithm> algos;
 
	public Scheduler() {
		super("Scheduler");
		algos = new ArrayList<Algorithm>();
		algos.add(new Multi());
		algos.add(new UtilityAlgo());
		algos.add(new RescheduleDec());
		algos.add(new RemoveNegs());
	}	
    public Scheduler(String name) {
		super(name);
		algos = new ArrayList<Algorithm>();
		algos.add(new UtilityAlgo());
		algos.add(new RescheduleDec());
		algos.add(new RemoveNegs());
	}
    
	/**
	 * Runs the Scheduler algorithm:
	 * 1. Examine all RFS jobs and schedules for today
	 * 2. Attach jobs to schedules depending on skill/skill level, etc.
	 * 3. Run additional algorithms to determine best schedule
	 * 
	 * @deprecated Use run(Map<Long,Set<ScheduleWrapper>> schedules,Map<Long,Set<Job>> jobs)
	 * @param c The collection of schedules by location
	 * @returns The job schedules created as a result of the run
	 */
	public Collection run(Collection c) {
		return null;
	}
	
	public List<Map> run(Map<Long,Set<ScheduleWrapper>> schedules,Map<Long,HashSet<Job>> jobs) {
		log("Running Scheduler...");
		Set<Long> locations = schedules.keySet();
		int size = jobs.size();
		log("Preassign: Put jobs back on same (active) schedule:");
		int lsize=0;
		for(Long l:locations) {
			log("location "+ l);
			// Sort schedules by shift and put them back in location map for use during algorithms
			TreeSet<ScheduleWrapper> sortedScheds = new TreeSet<ScheduleWrapper>(schedules.get(l));
			schedules.put(l, sortedScheds); 
			
			log("...has "+ sortedScheds.size() +" schedules.");
			if (sortedScheds.size()==0 ) {
				log("...nothing to try, skipping preassign/migration.");
				continue;
			} /********* Preassign/Migrate *********/
			for (ScheduleWrapper sw:sortedScheds) {
				Set<Job> scheduleJobs = sw.getJobsAssignedLastRun();
				int sj = scheduleJobs.size();
				// Preassign previously scheduled jobs
				Set<Job> unscheduled = null;
				if (sj>0) {
					log ("...trying to preassign "+ sj +" jobs on schedule "+ sw);
					unscheduled = preassign(sw,scheduleJobs);
					sj = sj-unscheduled.size();
					log ("...preassigned "+ sj +" jobs (unscheduled "+ unscheduled.size() +" jobs)");
				}
				// Migrate incomplete jobs 
				Set <Job> checkJobs = jobs.get(l);
				if (checkJobs.size()>0) {
					log("...attempting to migrate incomplete jobs using schedule "+ sw +" and location "+ l);
					Set<Job> migrated = migrateIncompleteJobs(sw,checkJobs);
					log("...migrated "+ migrated.size()+" incomplete jobs to active schedules");
					if (migrated.size()>0)
					jobs.get(l).removeAll(migrated);
				}
				if (unscheduled!=null && unscheduled.size()>0) {
					// Add jobs that couldn't go back on the schedule to unscheduled jobs 
					// (do this here because the unscheduled jobs don't need to be considered
					//  for migration)
					jobs.get(l).addAll(unscheduled);
				}

				log("...after preAssign: schedule "+ sw +" has "+ sw.getJobs().size() +" jobs");
				lsize=jobs.get(l).size();
				size += lsize;
			}
			log("After preAssign/migration, location "+ l +" has "+ lsize +" jobs to schedule");
		}
		log("After preAssign/migration for all locations, there are "+ size +" jobs to schedule");
		log("Preassign/migration complete.");
		log("Algorithms: run scheduling algorithms");
		for(Long l:locations) {
			log("location: "+l);
			Collection<ScheduleWrapper> locScheds = schedules.get(l);
			Collection<Job> locJobs = jobs.get(l);
			log("...has "+ locScheds.size() +" schedules.");
			log("...has "+ locJobs.size() +" jobs.");
			if (locScheds.size()==0 || locJobs.size()==0) {
				log("...nothing to try, skipping algorithms.");
			} else {
				for (Algorithm algorithm:algos) {
					log("__________________________________");
					log("Schedules (before algorithm): ");
					for (ScheduleWrapper sw:locScheds) {
						log(sw.getId());
					}
					log("__________________________________");
					algorithm.schedule(locJobs, locScheds);
				}
			}
			removeUnderAssignedMultiJobs(locScheds);
			updateSchedules(locScheds);
		}
		log("Algorithms complete");
		unassignJobs(jobs);
		log("Scheduler run complete");
		
		// Return possibly-modified schedules and unassigned jobs
		List<Map> sj = new ArrayList<Map>();
		sj.add(schedules);
		sj.add(jobs);
		return sj;
	}
	
	/**
	 * Preassign jobs - handles reassigning jobs on an active schedule back onto that schedule.
	 * It leverages the work done by the run of the Scheduler that originally determined that
	 * the assigned jobs should be on this schedule.
	 * 
	 * Note that since the job has been previously assigned, preassign recalculates the estimated 
	 * time to completion for the job by subtracting the time on the job so far. 
	 * If total time > estimated time, estimated time is 1 (zero would cause utility calculations
	 * to zero out)
	 * 
	 * @param schedule
	 * @param jobs
	 * @return list of jobs that have not been scheduled, to put in the unscheduled jobs pot
	 */
	private Set<Job> preassign(ScheduleWrapper schedule,Set<Job> jobs) {
		Set<Job> unscheduled = new HashSet<Job>();
		for (Job job:jobs) {
			// this check duplicates a check in WorldDAO
			if (isFinalState(job)) continue;
			
            job.setEstimatedTime(recalcEstTime(job));
            
            log("gonna assign job "+job);
			if (schedule.canAssign(job, true)) {
				schedule.assign(job);
			} else {
				unscheduled.add(job);
				log("Cannot pre-assign job "+job.getId() +
				    " to schedule "+ schedule +" : "+
				    schedule.getReason());
			}
		}
		return unscheduled;
	}
	private boolean isFinalState(Job job) {
		boolean b = (JobStatusRef.Status.CIA.equals(job.getStatusRef().getCode()) ||
	        	     JobStatusRef.Status.EJO.equals(job.getStatusRef().getCode()) );
		return b;
	}
	/**
	 * Process incomplete jobs - Migrate active jobs on closed schedule, assign each to a matching current schedule.
	 * "Incomplete" means a job that has not been closed, that is on a schedule that is closed, 
	 * and there is not a more current schedule that is active which contains that job.
	 * 
	 * Note that since the job has been previously assigned, migrateIncompleteJobs recalculates the estimated 
	 * time to completion for the job by subtracting the time on the job so far. 
	 * If total time > estimated time, estimated time is 1 (zero would cause utility calculations
	 * to zero out)
	 * 
	 * @param schedule
	 * @param incomplete List of incomplete jobs
	 */
	private Set<Job> migrateIncompleteJobs(ScheduleWrapper schedule, Set<Job> jobs) {
		Set<Job> migrated = new HashSet<Job>();
		for (Job job:jobs) {
			
			job.setEstimatedTime(recalcEstTime(job));
			
			if (job.getLatestJobSchedule()!=null) {
				// try to migrate regardless of sticky or not, b/c if not sticky, 
				// algorithms have a shot at pulling it off this schedule. 
				try {
					WorkSchedule old = job.getLatestJobSchedule().getWorkSchedule();
					WorkSchedule current = schedule.getWorkSchedule();
					if (current.getLocator().getId().equals(old.getLocator().getId()) &&
						current.getPerson().getId().equals(old.getPerson().getId()) ) {
						// schedule match, migrate job
						if (schedule.canAssign(job,true,true) ) {
							schedule.assign(job);
							migrated.add(job);
						}
					}
				} catch (Exception e) {
					error("Error trying to migrate incomplete job"+ job.getId() +
						  " to schedule "+ schedule +
						  ". Skipping migration of this job.", e);
				}
			}
		}
		return migrated;
	}
	private Integer recalcEstTime(Job job) {
        Integer time = job.getEstimatedTime() - (job.getTotalTime()!=null?job.getTotalTime():0);
       return time>0?time:1;
	}
	/**
	 * Final check of multi-worker jobs. Remove any where # schedules on which the job has been placed is
	 * less than the number of workers on the job
	 * 
	 * @param schedules
	 */
	private void removeUnderAssignedMultiJobs(Collection<ScheduleWrapper> schedules) {
		Map<Job,ArrayList<ScheduleWrapper>> multiJob = new HashMap<Job,ArrayList<ScheduleWrapper>>();
		for (ScheduleWrapper sw:schedules){
			Collection<Job> jobs4Sched = sw.getJobs();
			for (Job job:jobs4Sched){
				if (job.getNumberOfWorkers()>1) {
					if (multiJob.get(job)==null)
						multiJob.put(job,new ArrayList<ScheduleWrapper>());
					multiJob.get(job).add(sw);
				}
			}
		}
		Iterator<Job> keys = multiJob.keySet().iterator();
		while (keys.hasNext()) {
			Job j = keys.next();
			ArrayList<ScheduleWrapper> al = multiJob.get(j);
			if (al.size()!=j.getNumberOfWorkers()) { // under-scheduled, remove from schedules
				for (ScheduleWrapper sw:al) 
					sw.getJobs().remove(j);
				j.setSticky(false);
				j.setIsChanged(true);
				j.setScheduledDate(null);
			}
		}
	}
	private void updateSchedules(Collection<ScheduleWrapper> schedules) {
		int changed = 0;
		log("Updating schedules...");
		for (ScheduleWrapper sw:schedules) { 
			sw.setChanged(false);
			Set<JobSchedule> jsSet = sw.getWorkSchedule().getJobSchedules();
			for (JobSchedule js:jsSet) {
				if (sw.getJobs().contains(js.getJob())) { // job schedule already exists
					sw.getJobs().remove(js.getJob());     // so no need to create it
				}
				else  {// wasn't put back on schedule, so archive job schedule 
					js.setDeletedTime(stampTime());
					sw.setChanged(true);
				}
			}
			// create job schedules for new jobs
			java.sql.Date scheduled = stampDate();
			for (Job job:sw.getJobs()) {
				job.setScheduledDate(scheduled);
				job.setDispatchedDate(null);
				JobSchedule jobSchedule = new JobSchedule();
				jobSchedule.setJob(job);
				jobSchedule.setUser("Scheduler");
				jobSchedule.setCreatedTime(stampTime());
				jobSchedule.setWorkSchedule(sw.getWorkSchedule());
				sw.getWorkSchedule().getJobSchedules().add(jobSchedule);
				sw.setChanged(true);
			}
			if (sw.isChanged()) {
				changed++;
			}
		}
		log(changed+" schedules changed");
	}
	private void unassignJobs(Map<Long,HashSet<Job>> jobs) {
		for(Long l:jobs.keySet()) {
			int cnt = 0;
			int all = 0;
			for (Job job:jobs.get(l)) {
				all++;
				if (job.getScheduledDate()!=null ) // needs to be unscheduled to transition to RFS via JSM
				{
					job.setDispatchedDate(null);
					job.setScheduledDate(null);
					job.setIsChanged(true);
					cnt++;
				}
			}
			log("Unassigned "+ cnt +" jobs. "+ all +" total job in Unassigned Job Queue for location "+ l);
		}
	}
	private java.sql.Date stampDate() {
		return new java.sql.Date(System.currentTimeMillis());
	}
	private java.sql.Timestamp stampTime() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}
}
