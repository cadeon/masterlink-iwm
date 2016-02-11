package org.mlink.agent.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.mlink.agent.model.Job;
import org.mlink.agent.util.TestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AlgorithmTest extends TestCase {
	
    public AlgorithmTest(String name) {
        super(name);
    }
    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testMulti() {
    	System.out.println("==========");
    	System.out.println("Testing MultiWorker scheduling");
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	System.out.println("Adding job w/ plumber skill and 2 worker slots - this one should NOT be scheduled (not enough workers for location/skill)");
    	Job j1 = TestHelper.getJob(1,120,TestHelper.PLUMBER,2,TestHelper.GENERAL,2,TestHelper.LOCATORS[2]);
    	j1.setUtility(Utility.getUtility(j1));
    	System.out.println(j1);
    	jobs.add(j1);
    	System.out.println("Adding job w/ mechanic skill and 2 worker slots - this one SHOULD be scheduled (enough workers for location/skill)");
    	Job j2 = TestHelper.getJob(2,120,TestHelper.MECHANIC,5,TestHelper.HIGH,2,TestHelper.LOCATORS[1]);
    	j2.setUtility(Utility.getUtility(j2));
    	System.out.println(j2);
    	jobs.add(j2);
    	System.out.println("Adding job w/ mechanic skill and 1 worker slots - this one should NOT be scheduled (not a multi-worker job)");
    	Job j3 = TestHelper.getJob(3,60,TestHelper.MECHANIC,3,TestHelper.MEDIUM,1,TestHelper.LOCATORS[1]);
    	j3.setUtility(Utility.getUtility(j3));
    	System.out.println(j3);
    	jobs.add(j3);
    	
    	ArrayList<ScheduleWrapper> schedules = new ArrayList<ScheduleWrapper>();
    	Map<Integer,Integer>skills1 = new HashMap<Integer,Integer>();
    	skills1.put(TestHelper.MECHANIC,5);
    	int time = 480;
    	schedules.add(TestHelper.getSchedule(1,1,TestHelper.LOCATORS[1],skills1,time));
    	Map<Integer,Integer>skills2 = new HashMap<Integer,Integer>();
    	skills2.put(TestHelper.MECHANIC,5);
    	skills2.put(TestHelper.PLUMBER, 3);
    	schedules.add(TestHelper.getSchedule(2,1,TestHelper.LOCATORS[1],skills2,time));
    	Map<Integer,Integer>skills3 = new HashMap<Integer,Integer>();
    	skills3.put(TestHelper.PLUMBER, 3);
    	schedules.add(TestHelper.getSchedule(3,2,TestHelper.LOCATORS[2],skills3,time));

    	System.out.println("Running multi with : schedules "+ schedules.toString());
    	Multi multi = new Multi();
    	Collection<ScheduleWrapper> result = multi.schedule(jobs, schedules);
    	System.out.println("Multi results : schedules "+ result.toString());
    	boolean scheduled = true;
    	for (ScheduleWrapper sw:result) {
    		if (sw.getId().equals(1) ||
    			sw.getId().equals(2)) scheduled = scheduled && sw.getJobs().contains(j2);
    	}
    	assertTrue(scheduled);
    }

    public void testRescheduleDec() {
    	System.out.println("==========");
    	System.out.println("Test RescheduleDec");
    	System.out.println("Testing RescheduleDec scheduling");
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	System.out.println("Adding job 1 w/ mechanic skill to unscheduled jobs - this one should replace the job currently on schedule 1");
    	Job j1 = TestHelper.getJob(1,120,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]);
    	j1.setUtility(Utility.getUtility(j1));
    	System.out.println(j1);
    	jobs.add(j1);
    	
    	System.out.println("Adding job 2 w/ mechanic skill to schedule 1 - this one will be replaced");
    	Job j2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,3,TestHelper.MEDIUM,1,TestHelper.LOCATORS[1]); 
    	j2.setUtility(Utility.getUtility(j2));
    	System.out.println(j2);
    	System.out.println();
    	
    	ArrayList<ScheduleWrapper> schedules = new ArrayList<ScheduleWrapper>();
    	Map<Integer,Integer>skills1 = new HashMap<Integer,Integer>();
    	skills1.put(TestHelper.MECHANIC,5);
    	int time = 120;
    	ScheduleWrapper sw1 = TestHelper.getSchedule(1,1,TestHelper.LOCATORS[1],skills1,time);
    	sw1.assign(j2);
    	schedules.add(sw1);

    	System.out.println("Running reschedule dec with : schedules "+ schedules.toString());
    	RescheduleDec rescheduleDec = new RescheduleDec();
    	Collection<ScheduleWrapper> result = rescheduleDec.schedule(jobs, schedules);
    	System.out.println("RescheduleDec results : schedules "+ result.toString());
    	boolean scheduled = true;
    	for (ScheduleWrapper sw:result) {
    		if (sw.getId().equals(1)) scheduled = sw.getJobs().contains(j1);
    	}
    	assertTrue(scheduled);
    }
    public void testRemoveNegs() {
    	System.out.println("==========");
    	System.out.println("Testing RemoveNegs scheduling");
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	System.out.println("Adding job 1 w/ mechanic skill to schedule 1- this one should remain on schedule 1");
    	Job j1 = TestHelper.getJob(1,120,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j1.setUtility(Utility.getUtility(j1));
    	System.out.println(j1);
    	jobs.add(j1);
    	
    	System.out.println("Adding job 2 w/ mechanic skill to schedule 1 - this one should be removed");
    	Job j2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,1,TestHelper.LOW,1,TestHelper.LOCATORS[2]); 
    	j2.setUtility(Utility.getUtility(j2));
    	System.out.println(j2);
    	
    	System.out.println("Adding job 3 w/ mechanic skill to schedule 1 - this one should remain on schedule 1");
    	Job j3 = TestHelper.getJob(3,120,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j3.setUtility(Utility.getUtility(j3));
    	System.out.println(j3);
    	System.out.println();
    	
    	ArrayList<ScheduleWrapper> schedules = new ArrayList<ScheduleWrapper>();
    	Map<Integer,Integer>skills1 = new HashMap<Integer,Integer>();
    	skills1.put(TestHelper.MECHANIC,5);
    	int time = 480;
    	ScheduleWrapper sw1 = TestHelper.getSchedule(1,1,TestHelper.LOCATORS[1],skills1,time);
    	sw1.assign(j1);
    	sw1.assign(j2);
    	sw1.assign(j3);
    	schedules.add(sw1);

    	System.out.println("Running remove neg with : schedules "+ schedules.toString());
    	RemoveNegs removeNeg = new RemoveNegs();
    	Collection<ScheduleWrapper> result = removeNeg.schedule(jobs, schedules);
    	System.out.println("RemoveNeg results : schedules "+ result.toString());
    	boolean scheduled = true;
    	for (ScheduleWrapper sw:result) {
    		if (sw.getId().equals(1)) scheduled = !sw.getJobs().contains(j2);
    	}
    	assertTrue(scheduled);
    }
    public void testUtilityAlgo_Sorting() {
    	System.out.println("=========="); 
    	System.out.println("Testing Sorting in UtilityAlgo");
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	System.out.println("Adding job 1 - utility=9000");
    	Job j1 = TestHelper.getJob(1,120,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j1.setUtility(Utility.getUtility(j1));
    	System.out.println(j1);
    	jobs.add(j1);
    	
    	System.out.println("Adding job 2 - utility=60");
    	Job j2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,1,TestHelper.LOW,1,TestHelper.LOCATORS[2]); 
    	j2.setUtility(Utility.getUtility(j2));
    	System.out.println(j2);
    	jobs.add(j2);
    	
    	System.out.println("Adding job 3 - utility=2700");
    	Job j3 = TestHelper.getJob(3,60,TestHelper.PLUMBER,3,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j3.setUtility(Utility.getUtility(j3));
    	System.out.println(j3);
    	jobs.add(j3);

    	System.out.println("Running utility algo sort. ");
    	UtilityAlgo utilityAlgo = new UtilityAlgo();
    	Job[] sorted = utilityAlgo.sort(jobs);
    	System.out.println("Sort results : ");
		// iterate in reverse order  -- high utility jobs first
		for (int i=sorted.length-1;i>=0;i--) {
			Job job = sorted[i];
    		System.out.println("Job "+job.toString());    		
    	}
    	assertTrue(true);
    }
    public void testUtilityAlgo() {
    	System.out.println("==========");
    	System.out.println("Testing UtilityAlgo scheduling");
    	ArrayList<Job> jobs = new ArrayList<Job>();
    	System.out.println("Adding job 1 - utility=9000");
    	Job j1 = TestHelper.getJob(1,120,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j1.setUtility(Utility.getUtility(j1));
    	System.out.println(j1);
    	jobs.add(j1);
    	
    	System.out.println("Adding job 2 - utility=60");
    	Job j2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,1,TestHelper.LOW,1,TestHelper.LOCATORS[2]); 
    	j2.setUtility(Utility.getUtility(j2));
    	System.out.println(j2);
    	jobs.add(j2);
    	
    	System.out.println("Adding job 3 - utility=2700");
    	Job j3 = TestHelper.getJob(3,60,TestHelper.PLUMBER,3,TestHelper.HIGH,1,TestHelper.LOCATORS[1]); 
    	j3.setUtility(Utility.getUtility(j3));
    	System.out.println(j3);
    	jobs.add(j3);
    	
    	ArrayList<ScheduleWrapper> schedules = new ArrayList<ScheduleWrapper>();
    	Map<Integer,Integer>skills1 = new HashMap<Integer,Integer>();
    	skills1.put(TestHelper.MECHANIC,5);
    	int time1 = 480;
    	ScheduleWrapper sw1 = TestHelper.getSchedule(1,1,TestHelper.LOCATORS[1],skills1,time1);
    	schedules.add(sw1);

    	Map<Integer,Integer>skills2 = new HashMap<Integer,Integer>();
    	skills2.put(TestHelper.PLUMBER,3);
    	int time2 = 480;
    	ScheduleWrapper sw2 = TestHelper.getSchedule(2,1,TestHelper.LOCATORS[1],skills2,time2);
    	schedules.add(sw2);
    	
    	System.out.println("Running utility algo sort. ");
    	UtilityAlgo utilityAlgo = new UtilityAlgo();
    	Collection<ScheduleWrapper> result = utilityAlgo.schedule(jobs,schedules);
    	System.out.println("UtilityAlog results : schedules "+ result.toString());
    	System.out.println("UtilityAlog results : jobs "+ jobs.toString());
    	assertTrue(!jobs.contains(j1));
    }
    
    public static Test suite() {
        return new TestSuite(AlgorithmTest.class);
    }
}
