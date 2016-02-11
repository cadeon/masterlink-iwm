package org.mlink.agent.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;

public class RescheduleDec implements Algorithm{
	private static final Logger logger = Logger.getLogger(RescheduleDec.class);

	public Collection<ScheduleWrapper> schedule(Collection<Job> jobs, Collection<ScheduleWrapper> schedules) {
		int loopCount = 0;
		for (boolean changedOne=true;changedOne;) {
			log("Doing a rescheduling pass");
			if (loopCount > 99) {
				log("RescheduleDec loop repeated 100 times, which seems like a lot."); 
				log("Stopping so this can be investigated.");
				break;
			}
			changedOne = false;
			Collection<Job> removed   = new HashSet<Job>();
			Collection<Job> scheduled = new HashSet<Job>();
			for (Job job : jobs) {
				Job result = schedule(job,schedules);
				if (job.getId().equals(result.getId())) continue;  // could not schedule job, so returned same job
				// Otherwise job was scheduled, check and see if we had to remove one
				if (result.getId()!=null) removed.add(result); // put removed job in unscheduled jobs pot
				// else we added without removing anything
				scheduled.add(job);
				changedOne = true;
				log("Made a change -- will need to run RescheduleDec again.");
			}
			jobs.removeAll(scheduled); // remove those scheduled
			jobs.addAll(removed);      // add those removed from schedules
			loopCount++;
		}
		return schedules;
	}
	private Job schedule(Job job, Collection<ScheduleWrapper> schedules) {
		BigDecimal oldUtil   = new BigDecimal(0,MathContext.DECIMAL32);
		BigDecimal bestDelta = new BigDecimal(0,MathContext.DECIMAL32);;
		ScheduleWrapper bestSchedule = null;
		Job bestReplacedJob = null;
		log("  Trying job "+ job.getId());
		for (ScheduleWrapper schedule : schedules) {
			log("    Trying schedule "+ schedule.getId());
			if (!schedule.canAssign(job,false,true)) {
				log("  Cannot assign job, even ignoring time");
				continue;
			}
			if (schedule.canAssign(job,false,false)) {
				// Got one -- schedule it
				schedule.assign(job);
				log("      Job "+ job.getId() +" fits into schedule "+ schedule.getId()+" without removing anything");
				return new Job();
			}
			
			Collection<Job> schedJobs = schedule.getJobs();
			log("      Trying replacements");
			try {
			for (Job sj : schedJobs) {
				oldUtil = Utility.getUtility(schedule);
				//System.out.println("old "+ oldUtil.toPlainString());
				log("        Trying without job "+ sj.getId());
				if(!schedule.canRemove(sj)) {
					log("        Cannot remove job ("+ schedule.getReason()+")");
					continue;
				}
				try {
					ScheduleWrapper test = schedule.without(sj);
					if(!test.canAssign(job,false,false)) {
						log("        Cannot assign");
						continue;
					}
					test.assign(job);
					BigDecimal newUtil = Utility.getUtility(test);
					//System.out.println("new "+ newUtil.toPlainString());
					if (newUtil.compareTo(oldUtil) <= 0) {
						log("        Utility was decreased from "+ oldUtil +" to "+ newUtil);
						continue;
					}
					BigDecimal newMinusOld = newUtil.subtract(oldUtil);
					//System.out.println("new - old "+ newMinusOld.toPlainString());
					//System.out.println("best delta "+ bestDelta.toPlainString());
					if (newMinusOld.compareTo(bestDelta) <= 0) {
						log("        Not the best tried so far");
						continue;
					}
					// Otherwise, this one IS the best so far; store info
					bestDelta = newMinusOld;
					bestSchedule = test;
					bestReplacedJob = sj;
					log("        This is the best so far: ");
					log("          Existing job to replace="+sj.getId());
					log("          Best delta="+bestDelta.toPlainString());
					log("          Utility with new job replacing existing ="+newUtil.toPlainString());
					log("          Utility with existing job ="+oldUtil.toPlainString());
				} catch (Exception e) {
					log("Error during scheduler run. Most likely trying to remove a sticky job. Continuing run");
				}
			}
			} catch (Exception e) {
				log("Error during scheduler run for job "+ job.getId() +", schedule "+ schedule.getId());
			}
		}
		if (bestDelta.compareTo(BigDecimal.ZERO) == 0) {
			log("  None of the schedules would benefit");
			return job;
		}
		// Got one -- schedule it
		for (Iterator<ScheduleWrapper> it = schedules.iterator();it.hasNext();) {
			ScheduleWrapper sw = it.next();
			if (sw.getId()==bestSchedule.getId()) {
				it.remove();
				break;
			}
		}
		schedules.add(bestSchedule);
		log("  On schedule "+bestSchedule.getId()+" : ");
		log("    Replaced job "+ bestReplacedJob.getId());
		log("    With job     "+ job.getId());
		log("    And went from utility "+ oldUtil.toPlainString());
		log("    to utility "+ Utility.getUtility(bestSchedule).toPlainString());
		// Send back the one we removed
		return bestReplacedJob;
	}
	
	private void log(Object o) {logger.debug(o);}
	private void log(Object o,Throwable t){logger.error(o,t);}
}
