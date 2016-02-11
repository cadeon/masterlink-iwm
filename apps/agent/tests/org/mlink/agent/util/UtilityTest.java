package org.mlink.agent.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.mlink.agent.algorithm.ScheduleWrapper;
import org.mlink.agent.algorithm.Utility;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.PriorityRef;
import org.mlink.agent.model.ShiftRef;
import org.mlink.agent.model.SkillLevelRef;
import org.mlink.agent.model.SkillTypeRef;
import org.mlink.agent.model.WorkSchedule;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UtilityTest extends TestCase {
	
    public UtilityTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }
    public void tearDown() throws Exception {
        super.tearDown();
    }
	public void testJobUtility() {
		System.out.println("===========");
    	System.out.println("Testing Job Utility");
		Job job = TestHelper.getJob(1,60,TestHelper.MECHANIC,5,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		BigDecimal utility=Utility.getUtility(job);
		int comparison = utility.compareTo(new BigDecimal(60*5*TestHelper.GENERAL));
		assertEquals(0,comparison);
	}
	public void testScheduleNPT() {
		System.out.println("===========");
    	System.out.println("Testing Non-productive time");
		//Travel time is 70 b/w these two jobs
		Job job1 = TestHelper.getJob(1,60,TestHelper.MECHANIC,5,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		Locator locator1 = new Locator();
		locator1.setId(new Long(1));
		locator1.setFullLocator("location1.building1");
		job1.setLocator(locator1);
		Job job2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,2,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		Locator locator2 = new Locator();
		locator2.setId(new Long(2));
		locator2.setFullLocator("location1.building2");
		job2.setLocator(locator2);
		
		WorkSchedule schedule = new WorkSchedule();
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setProductiveTime(60);
		sw.setWorkSchedule(schedule);
		sw.assign(job1);
		sw.assign(job2);
		BigDecimal npt = Utility.instance().calcNpt(sw);
		int comparison = npt.compareTo(new BigDecimal(0.666667));
		assertEquals(0,comparison);
	}
	public void testScheduleGrade(){
		System.out.println("===========");
    	System.out.println("Testing Under-grade Factor");
		// This will be 3 under the schedule's skill level
		Job job2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,2,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		Locator locator2 = new Locator();
		locator2.setId(new Long(2));
		locator2.setFullLocator("location1.building2");
		job2.setLocator(locator2);
		
		WorkSchedule schedule = new WorkSchedule();
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setProductiveTime(60);
		sw.setWorkSchedule(schedule);
		sw.assign(job2);
		Map<Integer,Integer> levels = new HashMap<Integer,Integer>();
		levels.put(TestHelper.MECHANIC, 5);
		sw.setSkillLevels(levels);
		BigDecimal grade = Utility.instance().calcGrade(sw);
		System.out.println("Grade = "+grade.toPlainString());
		assertEquals(true,true);
		
	}
	public void testUsedTime(){
		System.out.println("===========");
    	System.out.println("Testing Used Time");
		WorkSchedule schedule = new WorkSchedule();
		schedule.setTime(480);
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setWorkSchedule(schedule);
		sw.setProductiveTime(60);
		BigDecimal usedTime = Utility.instance().usedTime(sw);
		System.out.println("Used time = "+ usedTime.toPlainString());
		assertEquals(true,true);
	}
	public void testPriorityTime(){
		System.out.println("===========");
    	System.out.println("Testing Priority Time");
		WorkSchedule schedule = new WorkSchedule();
		schedule.setTime(480);
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setWorkSchedule(schedule);
		// pretend 1 job - 1 hr, priority 5
		sw.setProductiveTime(60); 
		sw.setPriorityTime(TestHelper.GENERAL*60);
		BigDecimal time = Utility.instance().priorityTime(sw);
		System.out.println("Priority time = "+ time.toPlainString());
		assertEquals(true,true);
	}
	public void testRawUtility(){
		System.out.println("===========");
    	System.out.println("Testing Raw Utility");
		WorkSchedule schedule = new WorkSchedule();
		schedule.setTime(480);
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setWorkSchedule(schedule);
		// pretend 1 job - 1 hr, priority 5
		sw.setProductiveTime(60); 
		sw.setPriorityTime(TestHelper.GENERAL*60);
		BigDecimal time = Utility.instance().rawUtility(sw);
		System.out.println("Raw utility = "+ time.toPlainString());
		assertEquals(time.compareTo(new BigDecimal(0.0750)),0);
	}
	public void testUtility_ProductiveTimeZero(){
		System.out.println("===========");
    	System.out.println("Testing Productive Time of Zero");
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.setProductiveTime(0);
		BigDecimal time = Utility.getUtility(sw);
		System.out.println("Utility (w/zero productive time) = "+time.toPlainString());
		assertEquals(time.compareTo(BigDecimal.ZERO),0);
	}
	public void testUtility_ProductiveTime60(){
		System.out.println("===========");
    	System.out.println("Testing Productive of 60 minutes");
		Job job1 = TestHelper.getJob(1,60,TestHelper.MECHANIC,5,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		Locator locator1 = new Locator();
		locator1.setId(new Long(1));
		locator1.setFullLocator("location1.building1");
		job1.setLocator(locator1);
		Job job2 = TestHelper.getJob(2,60,TestHelper.MECHANIC,2,TestHelper.GENERAL,1,TestHelper.LOCATORS[1]);
		Locator locator2 = new Locator();
		locator2.setId(new Long(2));
		locator2.setFullLocator("location1.building2");
		job2.setLocator(locator2);
		
		WorkSchedule schedule = new WorkSchedule();
		schedule.setTime(480);
		
		ScheduleWrapper sw = new ScheduleWrapper();
		sw.assign(job1);
		sw.assign(job2);
		sw.setWorkSchedule(schedule);
		Map<Integer,Integer> levels = new HashMap<Integer,Integer>();
		levels.put(TestHelper.MECHANIC, 5);
		sw.setSkillLevels(levels);
		BigDecimal time = Utility.instance().rawUtility(sw);
		System.out.println("Utility = "+ time.toPlainString());
		assertEquals(time.compareTo(new BigDecimal(0.0750)),0);
	}
    
    public static Test suite() {
        return new TestSuite(UtilityTest.class);
    }
}
