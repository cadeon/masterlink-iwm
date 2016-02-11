package org.mlink.agent;


import java.util.ArrayList; 
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.WorkSchedule;
import org.mlink.agent.util.AgentConfig;

public class ShiftManager extends BaseAgent {
	
    public ShiftManager() {
		super("ShiftManager");
	}
    public ShiftManager(String name) {
		super(name);
	}

	/**
	 * Runs the ShiftManager algorithm:
	 * 1. Examine schedule
	 * 2. Expire schedules past their shift time
	 * 
	 * Schedules should be in IP or NYS, though any non-DUN state will be transitioned
	 * to DUN if the schedule is approximately half a day past the shift (depending on
	 * what the shift constant is set to in the agent config file)
	 * 
	 * @param c The collection of schedules to examine
	 * @returns The schedules modified as a result of the run
	 */
	public Collection<WorkSchedule> run(Collection c) {
		log("Running ShiftManager...");
		Collection<WorkSchedule> schedules = new ArrayList<WorkSchedule>();
		Collection<WorkSchedule> modified  = new ArrayList<WorkSchedule>();
		try {
			schedules = c;
		} catch (Exception e) {
			log("Could not make cast to Collection<WorkSchedule> with collection passed to ShiftManager: "+ c);
			return c;
		}
		for (WorkSchedule schedule:schedules) {
			if (schedule.getStatus()!=null &&
				!"DUN".equals(schedule.getStatus())) {
				// Check for schedules which have been archived but are not DUN
				if (schedule.getArchivedDate()!=null){
					//Got one, kill it, the modified check below will strip the jobs
					schedule.setStatus("DUN");
				}
				
				// check how long past shift end
				int shiftEnd = schedule.getShiftRef().getShiftEnd();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(schedule.getDay().getTime());
				calendar.add(Calendar.MINUTE, shiftEnd); // takes us to the end of the shift
				calendar.add(Calendar.HOUR, AgentConfig.shiftConst()); // plus const
				if (calendar.before(Calendar.getInstance())) {
					schedule.setStatus("DUN");
					if (!modified.contains(schedule)) modified.add(schedule);
					
					//update jobs - reset scheduledDate/dispatchedDate
					Set<JobSchedule> jsSet = schedule.getJobSchedules();
					for(JobSchedule js:jsSet) {
						Job job = js.getJob();
						job.setDispatchedDate(null);
						job.setScheduledDate(null);
					}
				}
			}
		}
		log("...ShiftManager run complete");
		return modified;
	}

}
