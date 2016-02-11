package org.mlink.agent;



import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mlink.agent.algorithm.ScheduleWrapper;
import org.mlink.agent.algorithm.Utility;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.*;
import org.mlink.agent.util.TestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



public class SchedulerTest extends MlinkTestCase {

 

	public void testScheduler(){
    	System.out.println("==========");
    	System.out.println("Testing Scheduler");
    	Map<Long,HashSet<Job>> jobs = new HashMap<Long,HashSet<Job>>();
    	System.out.println("Adding job 1 w/ mechanic skill to unscheduled jobs");

    	Job j1 = (Job) getBean("Job","1");
    	j1.setUtility(Utility.getUtility(j1));
    	p(j1.toString());
    	HashSet<Job> loc1Jobs = new HashSet<Job>();
    	loc1Jobs.add(j1);
    	jobs.put(TestHelper.LOCATORS[1].getId(), loc1Jobs);
    	
    	System.out.println("Adding job 2 w/ mechanic skill to schedule 1");
    	Job j2 = (Job) getBean("Job","2");
    	j2.setUtility(Utility.getUtility(j2));
    	p(j2.toString());
     	
    	Map<Long, Set<ScheduleWrapper>> schedules = new HashMap<Long,Set<ScheduleWrapper>>();
    	Map<Integer,Integer>skills1 = new HashMap<Integer,Integer>();
    	skills1.put(TestHelper.MECHANIC,5);
    	int time = 120;
    	ScheduleWrapper sw1 =  (ScheduleWrapper) getBean("ScheduleWrapper","1");
    	WorkSchedule schedule = sw1.getWorkSchedule();
    	Set jobSchedules = new HashSet();
    	jobSchedules.add((JobSchedule) getBean("JobSchedule","1"));
    	schedule.setJobSchedules(jobSchedules);
    	Set<ScheduleWrapper> loc1 = new HashSet<ScheduleWrapper>();
    	loc1.add(sw1);
    	schedules.put(TestHelper.LOCATORS[1].getId(),loc1); 	
    	p("Running Scheduler with : schedules "+ schedules.toString());
    	Scheduler scheduler = new Scheduler("Test Scheduler");
    	List<Map> result = scheduler.run(schedules,jobs);
    	Map<Long,Set<ScheduleWrapper>> changedSchedules = result.get(0);
    	Map<Long,Set<Job>> unassignedJobs = result.get(1);
    	
    	System.out.println("Scheduler results : schedules "+ changedSchedules.toString());
    	System.out.println("Scheduler results : jobs "+ unassignedJobs.toString());
    	assertTrue(true);
	}
    public static Test suite() {
        return new TestSuite(SchedulerTest.class);
    }
}
