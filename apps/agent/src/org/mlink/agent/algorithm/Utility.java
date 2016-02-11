package org.mlink.agent.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.mlink.agent.model.Job;
import org.mlink.agent.util.AgentConfig;

public class Utility {
	private static final Logger logger = Logger.getLogger(Utility.class);
	private static final BigDecimal ONEHUNDRED = new BigDecimal(100,MathContext.DECIMAL32);
	private static Utility utility;
	
	private BigDecimal kValue;
	private int    maxGrade;
	private int    maxPriority;
	private BigDecimal nptFactor;
	private BigDecimal gradeFactor;
	
	private Utility() {
		setKValue(AgentConfig.kValueConst());
		setNptFactor(AgentConfig.userNptConst());
		setGradeFactor(AgentConfig.userGradeConst());
		setMaxGrade(AgentConfig.maxGradeConst());
		setMaxPriority(AgentConfig.maxPriorityConst());
	}
	/**
	 * Calculate utility on the specified job
	 * 
	 * @param job
	 * @return BigDecimal representing the calculated utility
	 * @throws RuntimeException if utility factors not loaded successfully
	 */
	public static BigDecimal getUtility(Job job) {return instance().utility(job);}
	/**
	 * Calculate utility on the specified schedule
	 * 
	 * @param schedule
	 * @return BigDecimal representing the calculated utility
	 * @throws RuntimeException if utility factors not loaded successfully
	 */
	public static BigDecimal getUtility(ScheduleWrapper schedule) {return instance().utility(schedule);}
	/**
	 * Calculate utility on the specified list of schedules
	 * 
	 * @param schedules
	 * @return BigDecimal representing the calculated utility
	 * @throws RuntimeException if utility factors not loaded successfully
	 */
	public static BigDecimal getUtility(List<ScheduleWrapper> schedules) {return instance().utility(schedules);}
	
	public BigDecimal getKValue(){return kValue;}
	public BigDecimal getNptFactor(){return nptFactor;}
	public BigDecimal getGradeFactor(){return gradeFactor;}
  
	public void setKValue(String s){kValue=new BigDecimal(s,MathContext.DECIMAL32);}
	public void setNptFactor(String s){nptFactor=new BigDecimal(s,MathContext.DECIMAL32);}
	public void setGradeFactor(String s){gradeFactor=new BigDecimal(s,MathContext.DECIMAL32).divide(ONEHUNDRED);}
	public void setMaxGrade(String s){maxGrade=Integer.parseInt(s);}
	public void setMaxPriority(String s){maxPriority=Integer.parseInt(s);}
	
	private BigDecimal utility(Job job) {
		return new BigDecimal(//job.getEstimatedTime()* -- Ken sez time not considered in Job utility
				              job.getSkillLevelRef().getValue() *
				              Integer.parseInt(job.getPriorityRef().getCode()),
				              MathContext.DECIMAL32); 
	}
	
	private BigDecimal utility(List<ScheduleWrapper> schedules) {
		BigDecimal result = new BigDecimal(0,MathContext.DECIMAL32);
		for (ScheduleWrapper schedule:schedules) {
			result.add(utility(schedule));
		}
		return result;
	}
	/**
	 * Calculate Utility. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Utility for the schedule
	 */
	private BigDecimal utility(ScheduleWrapper schedule) {
		// raw * gradefactor * nptFactor
		if (schedule.getProductiveTime() == 0) return BigDecimal.ZERO;
		BigDecimal npt = calcNpt(schedule);
		BigDecimal grade = calcGrade(schedule);
		BigDecimal raw = rawUtility(schedule);
		BigDecimal result = raw.multiply(grade).multiply(npt);
		return result;		
	}
	/**
	 * Calculate Non-Productive Time. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Non-productive time for the schedule
	 */
	public BigDecimal calcNpt(ScheduleWrapper schedule) {
		// (pt-npt*usernptFactor)/pt
		BigDecimal pt = new BigDecimal(schedule.getProductiveTime(),MathContext.DECIMAL32).multiply(ONEHUNDRED);
		BigDecimal npt = new BigDecimal(schedule.getTravelTime(),MathContext.DECIMAL32);
		BigDecimal intermed = pt.subtract(npt.multiply(nptFactor));
		BigDecimal answer = intermed.divide(pt,MathContext.DECIMAL32);
		return answer;
	}
	/**
	 * Calculate Grade Factor. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Grade factor for the schedule
	 */
	public BigDecimal calcGrade(ScheduleWrapper schedule) {
		// 1 - gradeFactor*(1 - rawGradeFactor)
		BigDecimal sum =new BigDecimal(0,MathContext.DECIMAL32);
		for (Iterator<Job> it = schedule.getJobs().iterator();it.hasNext();) {
			Job job = it.next();
			Integer iLevel = schedule.getSkillLevel(job.getSkillTypeRef().getId());
			if (iLevel==null) {
				// FIXME: If a job is put on the work schedule, and then the skill for that job is deleted from the worker,
				// schedule.getSkillLevel will return null. The job is no longer valid for the work schedule, and should
				// be removed. There are two places that this removal should occur:
				// 1. When the user deletes a skill from a worker, a check should be made to see if any jobs on the worker's
				//    current schedule have that skill. The user can then decide to abort, or delete the skill and remove
				//    the jobs from the schedule.
				// 2. When data is being prepared for the Scheduler, the data should be examined to see if the person's skills
				//    match the jobs on the schedules for that person, and remove any jobs which require skills the person
				//    does not have.
				//
				//  i.e. the fix for invalid data should not be done here. This code just catches the cases where the data clean
				//  was not performed prior to calling this code.
				//
				log("Worker does not have skill '"+ job.getSkillTypeRef().getDescription() +"', though job "+ job.getId() +" requires it.");
				continue;
			}
			int schedSkillLevel = iLevel;
			// # grades worker skill level is over job skill level
			BigDecimal over = new BigDecimal(schedSkillLevel - job.getSkillLevelRef().getValue(),MathContext.DECIMAL32);  
			// 1 - (over/maxGrade)
			BigDecimal penalty = new BigDecimal(1).subtract(over.divide(new BigDecimal(maxGrade),MathContext.DECIMAL32)); 
			BigDecimal ptime = new BigDecimal(job.getEstimatedTime().intValue(),MathContext.DECIMAL32);
			BigDecimal ptimepenalty= ptime.multiply(penalty);
			sum = sum.add(ptimepenalty);
		}
		BigDecimal prodTime = new BigDecimal(schedule.getProductiveTime(),MathContext.DECIMAL32).multiply(ONEHUNDRED);
		// sum/(scheduleProdTime Hundredths)
		BigDecimal rawGradeFactor = sum.divide(prodTime,MathContext.DECIMAL32); 
		BigDecimal oneMinusRawGrade = new BigDecimal(1).subtract(rawGradeFactor);
		BigDecimal result = new BigDecimal(1).subtract(gradeFactor.multiply(oneMinusRawGrade)); 
		return result;
	}
	/**
	 * Calculate Raw Utility. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Raw Utility
	 */
	public BigDecimal rawUtility(ScheduleWrapper schedule) {
		// A is % time on schedule, or used time
		// B is % time * priority, or priority time
		// kValue*A + (1 - kValue)*B
		BigDecimal A = usedTime(schedule);
		if (A.compareTo(BigDecimal.ONE) > 0) A = BigDecimal.ONE;
		BigDecimal B = priorityTime(schedule);
		if (B.compareTo(BigDecimal.ONE) > 0) {
			return new BigDecimal(-1);
		}
		BigDecimal oneMinusKValue = new BigDecimal(1,MathContext.DECIMAL32).subtract(kValue);
		BigDecimal result = kValue.multiply(A).add(oneMinusKValue.multiply(B));
		return result;
	}
	/**
	 * Calculate Priority Time. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Priority time
	 */
	public BigDecimal priorityTime(ScheduleWrapper schedule) {
		// priorityTime/(totalTime*maxPriority)
		BigDecimal priority = new BigDecimal(schedule.getPriorityTime(),MathContext.DECIMAL32);
		BigDecimal divisor  = new BigDecimal(schedule.getTotalTime()*maxPriority,MathContext.DECIMAL32);
		BigDecimal result = priority.divide(divisor,MathContext.DECIMAL32);
		return result;
	}
	/**
	 * Calculate Used Time. Checked against "Introduction to the Scheduler Log" white paper
	 * 
	 * @param schedule
	 * @return Used time
	 */
	public BigDecimal usedTime(ScheduleWrapper schedule) {
		// productiveTime / totalTime
		BigDecimal productive = new BigDecimal(schedule.getProductiveTime(),MathContext.DECIMAL32);
		BigDecimal divisor    = new BigDecimal(schedule.getTotalTime(),MathContext.DECIMAL32);
		BigDecimal result = productive.divide(divisor,MathContext.DECIMAL32);
		return result;
	}
	
	public static synchronized Utility instance(){
		if (utility==null) {
			try {
				utility = new Utility();
			} catch (Exception e) {
				throw new RuntimeException("Error getting utility instance",e);
			}
		}
		return utility;
	}

    private static void log(Object o){logger.debug(o);}
    private static void log(Object o,Throwable t){logger.error(o,t);}
}
