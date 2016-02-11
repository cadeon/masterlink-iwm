package org.mlink.agent.algorithm;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;

public class RemoveNegs implements Algorithm {
	private static final Logger logger = Logger.getLogger(RemoveNegs.class);
	
	public Collection<ScheduleWrapper> schedule(Collection<Job> jobs, Collection<ScheduleWrapper> schedules) {
		log("RemoveNegs: Remove negatively useful jobs");
		for (ScheduleWrapper schedule : schedules ) {
			try {
				schedule = schedule(jobs, schedule);
			} catch (Exception e) {
				log("Error during RemoveNegs for schedule "+ schedule.getId(),e);
			}
		}
		log("RemoveNegs: Done");
		return schedules;
	}
	private ScheduleWrapper schedule(Collection<Job> jobs,ScheduleWrapper schedule) throws Exception {
		boolean found;
	    // ignore jobs sent in and examine jobs on schedule
		Collection<Job> schedJobs = schedule.getJobs();
		for (;;) {
			BigDecimal origUtil = Utility.getUtility(schedule);
			found = false;
			for (Job job : schedJobs) {
				if (!schedule.canRemove(job)) continue;
				ScheduleWrapper test = schedule.without(job);
				BigDecimal testUtil = Utility.getUtility(test);
				if (testUtil.compareTo(origUtil) <= 0) continue;
				found = true;
				schedule = test;
				break;
			}
			if (!found) break;;
		}
		return schedule;
	}
	private static void log(Object o) {logger.debug(o);}
	private static void log(Object o,Throwable t) {logger.error(o,t);}
}
