package org.mlink.agent.algorithm;

import java.util.Collection;

import org.mlink.agent.model.Job;

public interface Algorithm {
	public Collection<ScheduleWrapper> schedule(Collection<Job> jobs, Collection<ScheduleWrapper> schedules);

}
