package org.mlink.agent.util;

import org.apache.log4j.Logger;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.SkillTypeRef;
import org.mlink.agent.model.TaskView;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.TaskExpiryTypeRef;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * Provides a series of methods for either validation of properties in a collection of tasks
 * or calculating aggregate values of tasks properties
 */
public class TaskPropertiesValidator {
	private static Logger logger = Logger.getLogger(TaskPropertiesValidator.class);

    /**
     * Find task with max of worker required and return the number
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static int extractMaxWorkersRequired(List<TaskView> tasks) throws BusinessException{
        int max = 0;
        for (TaskView task : tasks) {
            if (task.getNumberWorkers() == null) {
                throw new BusinessException("Number of workers is not set for task " + task.getDescription());
            } else if (max < task.getNumberWorkers()) {
                max = task.getNumberWorkers();
            }
        }
        return max;
    }

    /**
     * Finf highest priority from task list
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static int extractPriority(List<TaskView> tasks) throws BusinessException{
      int priority = 0;
      int pc = 0;
        for (TaskView task : tasks) {
        	pc = (task.getPriorityRef()!=null?Integer.parseInt(task.getPriorityRef().getCode()):0);;
            if (pc > priority) {
              priority = pc;
            }
        }
        if(priority==0) throw new BusinessException("Failed to determine job priority. All supplied tasks have no priority set!");

        return priority;
    }
    /**
     //business rule check for the same skillTypeRef
     * @param tasks collection of TaskView objects
     * @return  skillTypeRef that is the same for all tasks
     * @throws BusinessException
     * @since v35 MustAssignSkill type can be mixed withn other types in group of tasks without throwing exception
     *            if other skill types are present in group use their skill type. If only one task with skill MustAssignSkill use it.
     */
    public static SkillTypeRef extractSkillType(Collection<TaskView> tasks) throws BusinessException{
        Map<String,SkillTypeRef> skillTypes = new HashMap<String,SkillTypeRef>();
        if(tasks!=null){
            for (TaskView tv : tasks) {
                SkillTypeRef currentSkillType = tv.getSkillTypeRef();
                if (currentSkillType == null) {
                    throw new IWMException("Skill type is null! " + tv.getClass().getName());
                }
                if(!skillTypes.containsKey(currentSkillType.getCode())) {
                	skillTypes.put(currentSkillType.getCode(),currentSkillType);
                }
            }
            if(skillTypes.size()==1){
                return skillTypes.values().iterator().next(); //just one item in tasks, return it.  SkillTypeRef.MUST_ASSIGN is also allowed
            }
            // if more than one, remove SkillTypeRef.MUST_ASSIGN. The goal is to return anything but SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN) if available
            skillTypes.remove(SkillTypeRef.MUST_ASSIGN);

            // there only one unique skill type should remain
            if(skillTypes.size()>1){
               //if more than one member of array then we have disparate skill types
                throw new BusinessException("Tasks must have the same skill type.");
            } 
            if (skillTypes.size()==0) throw new BusinessException("Job must have skill type.");
            SkillTypeRef skillType = skillTypes.values().iterator().next();
            return skillType;
        }
        throw new IWMException("Returning null skill type!");
    }
    /**
     * Find highest skill level from the given task list
     * @param tasks
     * @throws BusinessException
     */
    public static int extractSkillLevel(List<TaskView> tasks) throws BusinessException{
      Integer skillLevel = org.mlink.iwm.lookup.SkillLevelRef.ZERO;
      Integer  sk = null;
        for (TaskView task : tasks) {
        	sk = task.getSkillLevelRef().getValue();
            if (sk!=null && sk > skillLevel) {
                skillLevel = sk;
            }
        }
        //not sure if its a violation if(skillLevel==0) throw new BusinessException("Failed to determine job skill level. All supplied tasks have no skill level set!");

        return skillLevel;
    }

    /**
     * Calculate sum of estimated times for tasks in the given list
     * @param tasks
     */
    public static int extractEstimatedTime(List<TaskView> tasks) {
        int rtn = 0;
        Integer time = null;
        for (TaskView task : tasks) {
          time = task.getEstimatedTime();   
          rtn += time==null? 0 : time;
        }
        return rtn;
    }

    /**
     * Validate that objectId is the same for all tasks
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static Long validateObjectId(List<TaskView> tasks) throws BusinessException{
        Long objectId = null;
        for (TaskView task : tasks) {
            if (task.getWorkObjectId() == null) {
                throw new BusinessException("workObjectId is null for task " + task.getDescription());
            } else if (objectId != null && !objectId.equals(task.getWorkObjectId())) {
                throw new BusinessException("workObjectId must be identical for all tasks in the job " + task.getDescription());
            } else {
                objectId = task.getWorkObjectId();
            }
        }
        return objectId;
    }
    /**
     * Validate that locatorId is the same for all tasks
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static Locator validateLocatorId(List<TaskView> tasks) throws BusinessException{
        Locator locator = null;
        for (TaskView task : tasks) {
            if (task.getLocator()==null || task.getLocator().getId()==null) {
                throw new BusinessException("locator is null for task " + task.getDescription());
            } else if (locator != null && !locator.getId().equals(task.getLocator().getId())) {
                throw new BusinessException("locators must be identical for all tasks in the job " + task.getDescription());
            } else {
            	locator = task.getLocator();
            }
        }
        return locator;
    }
    
    /**
     * Validate that latestStartDate is the same for all tasks
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static Date extractLatestStartDate(List<TaskView> tasks) throws BusinessException{
        Date latestStartDate = null, latestStartDateOfTask;
        boolean firstTask = true;
        for (TaskView task : tasks) {
        	latestStartDateOfTask = calculateLastestStartDateOfTask(task);
        	if(latestStartDateOfTask == null){//NEX
        		latestStartDate = null;
        		break;
        	}else{
        		if(firstTask){
        			latestStartDate = latestStartDateOfTask;
        		}else{
        			if(latestStartDate.before(latestStartDateOfTask)){
        				latestStartDate = latestStartDateOfTask;
        			}
        		}
        	}
        }
        return latestStartDate;
    }
    
    private static Date calculateLastestStartDateOfTask(TaskView task){
    	Date latestStartDate=null;
    	String expiryType = task.getExpiryType();
    	if(TaskExpiryTypeRef.Status.NEX.toString().equals(expiryType)){
    		latestStartDate = null;
    	}else if(TaskExpiryTypeRef.Status.EOY.toString().equals(expiryType)){
    		Calendar cal = Calendar.getInstance(); 
    		cal.setTime(new Date()); 
    		cal.set(Calendar.MONTH, Calendar.DECEMBER);
    		cal.set(Calendar.DAY_OF_MONTH, 31);	
    		latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    	}else if(TaskExpiryTypeRef.Status.EOM.toString().equals(expiryType)){
    		Calendar cal = Calendar.getInstance(); 
    		cal.setTime(new Date()); 
    		int max = cal.getMaximum(Calendar.DAY_OF_MONTH);
    		int actualMax = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    		cal.set(Calendar.DAY_OF_MONTH, actualMax);
    		latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    	}else if(TaskExpiryTypeRef.Status.REL.toString().equals(expiryType)){
    		Calendar cal = Calendar.getInstance(); 
    		cal.setTime(new Date()); 
    		cal.add(Calendar.DAY_OF_MONTH, task.getExpiryNumOfDays().intValue());
    		latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    	}
    	return latestStartDate;
    }
    
    private static void log(Object o){logger.debug(o);}
    private static void log(Object o,Throwable t){logger.error(o,t);}
}
