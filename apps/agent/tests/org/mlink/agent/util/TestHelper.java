package org.mlink.agent.util;

import java.util.*;


import org.mlink.agent.algorithm.ScheduleWrapper;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.JobStatusRef;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.Person;
import org.mlink.agent.model.PriorityRef;
import org.mlink.agent.model.ShiftRef;
import org.mlink.agent.model.SkillLevelRef;
import org.mlink.agent.model.SkillTypeRef;
import org.mlink.agent.model.WorkSchedule;


public class TestHelper {

	public static final int MECHANIC = 1;
	public static final int ELECTRICIAN = 2;
	public static final int PLUMBER = 3;
	
	//public static final int RFS = 10;
	//public static final int DJO = 20;
	public static final String DJO = "DJO";
	public static final String RFS = "RFS";
	
	public static final int LOW = 1;
	public static final int GENERAL=5;
	public static final int MEDIUM=10;
	public static final int HIGH=15;
	public static final int EMERGENCY=25;

	static final long[]   LOCATOR_IDS = {1,2,3,4,5};
	static final String[] FULL_LOCATOR = {"1","2","3","4","5"};
	
	public static Locator[] LOCATORS;
	
	static {
        LOCATORS = new Locator[LOCATOR_IDS.length];
        for (int i=0;i<LOCATOR_IDS.length;i++){
        	Locator locator = new Locator();
        	locator.setId(LOCATOR_IDS[i]);
        	locator.setFullLocator(FULL_LOCATOR[i]);
        	LOCATORS[i]=locator;
        }
    }

    public static Job getJob(long id,int estTime,int skillTypeId,int skillLevel,int priority,int numworkers,Locator locator){
    	return getJob(id,estTime,skillTypeId,skillLevel,priority,numworkers,locator,RFS);
    }
        
    public static Job getJob(long id,int estTime,int skillTypeId,int skillLevel,int priority,int numworkers,Locator locator,String status){
    	Job job = new Job();
    	job.setId(id);
    	job.setDescription(""+job.getId().longValue());
    	job.setEstimatedTime(estTime);
    	job.setNumberOfWorkers(numworkers);
    	job.setSkillTypeRef(getSkillTypeRef(skillTypeId));
    	job.setSkillLevelRef(getSkillLevelRef(skillLevel));
    	job.setPriorityRef(getPriorityRef(priority));
    	job.setStatusRef(getJobStatusRef(status));
    	job.setLocator(locator);
    	job.setSticky(false);
      //   toXml(job,""+id);
    	return job;
    }
    public static ScheduleWrapper getSchedule(long id,int shiftId,Locator locator, Map<Integer,Integer>skills,int time) {
    	WorkSchedule schedule = new WorkSchedule();
    	schedule.setId(id);
    	schedule.setLocator(locator);
    	schedule.setShiftRef(getShiftRef(shiftId));
    	schedule.setTime(time);
    	Person p = new Person();
    	p.setUsername("Bob"+id);
    	schedule.setPerson(p);
    	ScheduleWrapper sw = new ScheduleWrapper();
    	sw.setWorkSchedule(schedule);
    	sw.setSkillLevels(skills);
      //   toXml(sw,""+id);
    	return sw;
    }
    public static JobSchedule getJobSchedule(long id,Job job){
    	JobSchedule js = new JobSchedule();
    	js.setId(id);
    	js.setJob(job);
      //   toXml(js,""+id);
    	return js;
    }
    public static PriorityRef getPriorityRef(int priority){
    	PriorityRef ref = new PriorityRef();
    	ref.setCode(""+priority);
    	ref.setId(priority);
      //   toXml(ref,""+priority);
    	return ref;
    }
    public static ShiftRef getShiftRef(int id){
    	ShiftRef ref = new ShiftRef();
    	ref.setCode(""+id);
    	ref.setId(id);
    	ref.setDescription(""+id);
      //   toXml(ref,""+id);
    	return ref;
    }
    public static SkillTypeRef getSkillTypeRef(int id){
    	SkillTypeRef ref = new SkillTypeRef();
    	ref.setCode(""+id);
    	ref.setId(id);
    	ref.setDescription(""+id);
      //   toXml(ref,""+id);
    	return ref;
    }
    public static SkillLevelRef getSkillLevelRef(int level){
    	SkillLevelRef ref = new SkillLevelRef();
    	ref.setCode(""+level);
    	ref.setId(level);
    	ref.setDescription(""+level);
      //   toXml(ref,""+level);
    	return ref;
    }
    public static JobStatusRef getJobStatusRef(String status){
    	JobStatusRef ref = new JobStatusRef();
    	ref.setCode(status);
    	ref.setDescription(status);
      //   toXml(ref,""+ref.getId());
    	return ref;
    }
   

}
