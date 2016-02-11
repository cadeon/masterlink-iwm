package org.mlink.agent.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;

public class Multi implements Algorithm {
	private static final Logger logger = Logger.getLogger(Multi.class);
	/**
	 * Schedule multi-worker jobs
	 */
	public Collection<ScheduleWrapper> schedule(Collection<Job> jobs, Collection<ScheduleWrapper> schedules) {
		log("Multi: Schedule multi-worker jobs");
		
		log("sorting jobs");
		Job[] sorted = sort(jobs);
		log("Got "+ sorted.length+" jobs");
		// iterate in reverse order  -- high utility jobs first
		for (int i=sorted.length-1;i>=0;i--) {
			Job job = sorted[i];
			if (job.getNumberOfWorkers() < 2) continue;
			// else multi-worker job
			log("Got multi-worker job (id="+job.getId()+")");
			schedule(job,schedules);
		}
		log("Multi: Done");
		return schedules;
	}
	private Job[] sort(Collection<Job> jobs) {
		Job[]sorted = new Job[jobs.size()];
		jobs.toArray(sorted);
		Arrays.sort(sorted);
		return sorted;
	}
	private void schedule(Job job, Collection<ScheduleWrapper> schedules) {
		log("Workers available: "+schedules.size());
		log("Number workers req'd by job: "+ job.getNumberOfWorkers());
		if (schedules.size()<job.getNumberOfWorkers()) {
			log("Not enough workers available...skip");
			return;
		}
		Map<Integer,SchedDelta> shifts = new HashMap<Integer,SchedDelta>();
		
		for (ScheduleWrapper schedule : schedules) {
			log("Check schedule for "+ schedule.getWorkerName());
			if (schedule.canAssign(job)) {
				ScheduleWrapper clone = schedule.clone();
				clone.assign(job);
				// Add to assessment list for shift
				SchedDelta delta = new SchedDelta();
				delta.deltaUtil = Utility.getUtility(clone).subtract(Utility.getUtility(schedule));
				delta.scheduleId = schedule.getId();
				SchedDelta sd = shifts.get(schedule.getShiftId());
				if (sd==null) sd = delta;
				shifts.put(schedule.getShiftId(), sd.insert(delta));
			}
			else { log("Not scheduled b/c of "+schedule.getReason());}
		}
		BigDecimal bestDelta = new BigDecimal(0,MathContext.DECIMAL32);
		List<Long> bestSet = new ArrayList<Long>();
		Set<Integer> keys = shifts.keySet();
		for (Iterator<Integer> it = keys.iterator();it.hasNext();) {
			Integer shiftId = it.next();
			log("Trying shift: "+ shiftId);
			SchedDelta currList = shifts.get(shiftId);
			if(currList.chainLength<job.getNumberOfWorkers()) {
				log("Not scheduled to enough workers");
				continue; //not enough assignments made this shift
			}
			// Sufficient schedules, check utility
			log("Found enough workers for job");
			List<Long> thisSet = new ArrayList<Long>();
			BigDecimal thisDelta = new BigDecimal(0,MathContext.DECIMAL32);
			for (int i=0;i<job.getNumberOfWorkers();i++) {
				thisDelta = thisDelta.add(currList.deltaUtil);
				thisSet.add(currList.scheduleId);
				currList = currList.after;
			}
			log("shift delta: "+ thisDelta.toPlainString());
			if (thisDelta.compareTo(bestDelta) <= 0) continue;
			log("(best so far)");
			bestDelta=thisDelta;
			bestSet=thisSet;
		}
		if (bestSet.size()==0) return;
		log("assigning job "+ job.getId() +" to: ");
		for (ScheduleWrapper schedule:schedules) {
			if (bestSet.contains(schedule.getId())) {
				log(" schedule "+ schedule.getId());
				schedule.assign(job);
			}
		}
		job.setSticky(true);
	}
	
	// Convenience class to keep track of deltas in utilities
	class SchedDelta {
		BigDecimal deltaUtil;
		Long       scheduleId;
		SchedDelta after;
		int    chainLength = 1;
		
		SchedDelta insert(SchedDelta sd) {
			if (this.equals(sd)) return this;
			if (this.deltaUtil.compareTo(sd.deltaUtil) >= 0) {
				this.chainLength+=sd.chainLength;
				if (this.after==null) {
					this.after = sd;
					return this;
				}
				if (this.after.deltaUtil.compareTo(sd.deltaUtil) <= 0) {
					sd.chainLength+=this.after.chainLength;
					sd.after = this.after;
					this.after = sd;
				}
				else // this.after.deltaUtil > sd.deltaUtil
					this.after.insert(sd);
				return this;
			} // else sd.deltaUtil > this.deltaUtil
			sd.chainLength+=this.chainLength;
			return sd.insert(this);
		}
	}
	
	private static void log(Object o) {logger.debug(o);}
}
