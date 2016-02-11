package org.mlink.agent.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;

public class UtilityAlgo implements Algorithm {
	private static final Logger logger = Logger.getLogger(UtilityAlgo.class);

	public Collection<ScheduleWrapper> schedule(Collection<Job> jobs, Collection<ScheduleWrapper> schedules) {
		log("UtilityAlgo: Schedule jobs according to utility");
		
		log("sorting jobs");
		Job[] sorted = sort(jobs);
		HashSet<Job> scheduled = new HashSet<Job>();
		// iterate in reverse order  -- high utility jobs first
		for (int i=sorted.length-1;i>=0;i--) {
			Job job = sorted[i];
			log("|{{{ Testing "+ job.getDescription() +" ("+job.getId()+")");
			if (!schedule(job,schedules)) {
				log("Job "+ job.getId()+" not scheduled");
			} 
			else {
				scheduled.add(job);
				log("scheduled Job "+job.getId());
			}
			log("|}}} Tested "+ job.getDescription() +" ("+job.getId()+" )");			
		
		}
		log("assigned "+ (scheduled.size()) +" jobs");
		jobs.removeAll(scheduled);
		log("==================================");
		log("Jobs scheduled: ");
		for (Job j:scheduled) {
			log(j.getId());
		}
		log("==================================");
		log("==================================");
		log("UJQ: ");
		for (Job j:jobs) {
			log(j.getId());
		}
		log("==================================");
		log("==================================");
		log("Schedules: ");
		for (ScheduleWrapper sw:schedules) {
			log(sw);
			log("...");
		}
		log("==================================");
		log("UtilityAlgo: Done");
		return schedules;
	}
	public Job[] sort(Collection<Job> jobs) {
		Job[] sorted = new Job[jobs.size()];
		jobs.toArray(sorted);
		Arrays.sort(sorted);
		return sorted;
	}
	private boolean schedule(Job job,Collection<ScheduleWrapper> schedules) {
		log("Attempting to schedule");
		BigDecimal best = new BigDecimal(0,MathContext.DECIMAL32);
		ScheduleWrapper bestSched = null;
		ScheduleWrapper bestReplaced = null;
		for (ScheduleWrapper schedule:schedules) {
			log("trying "+ schedule.getWorkerName() +" ("+ schedule.getId() +")");
			ScheduleWrapper test = schedule.clone();
			if (!test.canAssign(job)) {
				log("does not fit ("+ test.getReason()+")");
				continue;
			}
			test.assign(job);
			BigDecimal delta = Utility.getUtility(test).subtract(Utility.getUtility(schedule));
			log(" delta = "+ delta.toPlainString());
			if (delta.compareTo(best) <= 0) {
				log("    delta("+delta.toPlainString()+") < best("+best.toPlainString()+")");
				continue;
			} else 
				log("    best so far");
			best=delta;
			bestSched = test;
			bestReplaced = schedule;
		}
		if (bestSched==null) return false;
		log("__________________________________");
		log("Schedules (before removal of schedule replaced by scheduling job): ");
		for (ScheduleWrapper sw:schedules) {
			log(sw.getId());
		}
		log("__________________________________");
		log("removing "+ bestReplaced);
		for (Iterator<ScheduleWrapper> it = schedules.iterator();it.hasNext();) {
			ScheduleWrapper sw = it.next();
			if (sw.getId()==bestSched.getId()) {
				log(sw);
				it.remove();
				break;
			}
		}
		log("__________________________________");
		log("Schedules (after removal, before adding cloned schedule containing scheduled job): ");
		for (ScheduleWrapper sw:schedules) {
			log(sw.getId());
		}
		log("__________________________________");
		schedules.add(bestSched);
		log("__________________________________");
		log("Schedules (after add): ");
		for (ScheduleWrapper sw:schedules) {
			log(sw.getId());
		}
		log("__________________________________");
		return true;
	}
	private static void log(Object o) {logger.debug(o);}
}
