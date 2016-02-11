package org.mlink.iwm.rules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.lookup.TaskExpiryTypeRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * Provides a series of methods for either validation of properties in a collection of tasks
 * or calculating aggregate values of tasks properties
 */
public class TaskPropertiesValidator {
    /**
     //business rule check for the same skillTypeId
     * @param tasks collection of Task|TaskDefinition|Task|TaskDefinition objects
     * @return  skillTypeId that is the same for all tasks
     * @throws BusinessException
     * @since v35 MustAsssignSkill type can be mixed with other types in group of tasks without throwing exception
     *            if other skill types are present in group use their skill type. If only one task with skill MustAssignSkill use it.
     */
    public static Integer extractSkillType(Collection items) throws BusinessException{
        List <Integer> skillTypes = new ArrayList<Integer>();
        if(items!=null &&  !items.isEmpty()){
            for (Object o : items) {
                Integer currentSkillTypeId;
                if (o instanceof Task)
                {         //todo implement Task taskDef inheritance (business interface) when ready and update this line
                    currentSkillTypeId = ((Task) o).getSkillTypeId();
                } else if (o instanceof TaskDefinition) {
                    currentSkillTypeId = ((TaskDefinition) o).getSkillTypeId();
                } else if (o instanceof TaskDefinition) {
                    currentSkillTypeId = ((TaskDefinition) o).getSkillTypeId();
                } else if (o instanceof Task) {
                    currentSkillTypeId = ((Task) o).getSkillTypeId();
                } else {
                    // nonsence, should never happen
                    throw new IWMException("wrong object type in collection");
                }
                if (currentSkillTypeId == null) {
                    throw new IWMException("Skill type is null! " + o.getClass().getName());
                }
                if(!skillTypes.contains(currentSkillTypeId)) skillTypes.add(currentSkillTypeId);
            }
            if(skillTypes.size()==1){
                return skillTypes.get(0); //just one item in items, return it.  SkillTypeRef.MUST_ASSIGN is also allowed
            }
            // if more than one, remove SkillTypeRef.MUST_ASSIGN. The goal is to return anything but SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN) if available
            skillTypes.remove(Integer.valueOf(SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN)));

            // there only one unique skill type should remain
            if(skillTypes.size()>1){
               //if more than one memeber of array then we have disparate skill types
                throw new BusinessException("Tasks must have the same skill!");
            }
            return skillTypes.get(0);
        }

        return null;
    }

    /**
     * Find task with max of worker required and return the number
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static int extractMaxWorkersRequired(List tasks) throws BusinessException{
        int max = 0;
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            if (task.getNumberOfWorkers() == null) {
                throw new BusinessException("Number of workers is not set for task " + task.getTaskDescription());
            } else if (max < task.getNumberOfWorkers()) {
                max = task.getNumberOfWorkers();
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
    public static int extractPriority(List tasks) throws BusinessException{
        int priorityId = 0;
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            if (ConvertUtils.intVal(task.getPriorityId()) > priorityId) {
                priorityId = ConvertUtils.intVal(task.getPriorityId());
            }
        }
        if(priorityId==0) throw new BusinessException("Failed to determine job priority. All supplied tasks have no priority set!");

        return priorityId;
    }

    /**
     * Finf highest skill level from the given task list
     * @param tasks
     * @throws BusinessException
     */
    public static int extractSkillLevel(List tasks) throws BusinessException{
        int skillLevel = 0;
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            if (ConvertUtils.intVal(task.getSkillLevelId()) > skillLevel) {
                skillLevel = ConvertUtils.intVal(task.getSkillLevelId());
            }
        }
        //not sure if its a violation if(skillLevel==0) throw new BusinessException("Failed to determine job skill level. All supplied tasks have no skill level set!");

        return skillLevel;
    }

    /**
     * Calculate sum of estimated times for tasks in the given list
     * @param tasks
     */
    public static long extractEstimatedTime(List tasks) {
        long rtn = 0L;
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            rtn += ConvertUtils.intVal(task.getEstTime());
        }
        return rtn;
    }


    /**
     * Validate that objectId is the same for all tasks
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static Long validateObjectId(List tasks) throws BusinessException{
        Long objectId = null;
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            if (task.getObject() == null) {
                throw new BusinessException("objectId is null for task " + task.getTaskDescription());
            } else if (objectId != null && !objectId.equals(task.getObject().getId())) {
                throw new BusinessException("objectId must be identical for all tasks in the job " + task.getTaskDescription());
            } else {
                objectId = task.getObject().getId();
            }
        }
        return objectId;
    }

    /**
     * Provide validation methods for TaskType related properties
     *
     Rules:  (IWM_2.3.2.1_BR 8/3/2004)
     1.	Task Creation
     *	Routine Tasks must have Frequency or, Months/Days Run, or Threshold Increment set
     *	Non-Routine Tasks Cannot have ANY Routine values set
     *	Frequency-Based Tasks can be 'N' Months/Days OR Frequency Drop-down values * But Not Both
     *	Tasks with Frequency set MAY have Threshold or Increment Set * Planner Runs EITHER Frequency OR Threshold (see planner rules)

     2.	Task Def Update
     *	If the selected Task Def is a member of a Task Group Def, the Skill Type cannot be updated to be of Skill Type <> to that of the remaining members of the Task Group Def
     *
     */

    /*public static void validateTaskType(TaskDefinition vo) throws BusinessException{
        try{
           //TaskDefinition td = TaskDefinitionAccess.getTaskDefinition(vo.getId());
        }catch(Exception e){
            throw new IWMException(e);
        }

    }*/

    /*public static void validateTaskType(Task vo) throws BusinessException{
        try{
           //Task task = TaskAccess.getTask(vo.getTaskId());
        }catch(Exception e){
            throw new IWMException(e);
        }

    }*/


    /**
     * Provide validation methods for TaskType related properties
     *
     Rules:  (IWM_2.3.2.1_BR 8/3/2004)
     1.	Task Creation
     *	Routine Tasks must have Frequency or, Months/Days Run, or Threshold Increment set
     *	Non-Routine Tasks Cannot have ANY Routine values set
     *	Frequency-Based Tasks can be 'N' Months/Days OR Frequency Drop-down values * But Not Both
     *	Tasks with Frequency set MAY have Threshold or Increment Set * Planner Runs EITHER Frequency OR Threshold (see planner rules)

     2.	Task Def Update
     *	If the selected Task Def is a member of a Task Group Def, the Skill Type cannot be updated to be of Skill Type <> to that of the remaining members of the Task Group Def
     *
     */
    /**
     * Same as above in another words:
     * Routine tasks must have a frequence defined. Frequency can be Time based or Meter based
     * Routine task is meter based if  one (only one) of getFreqDays or getFreqMonths  are set
     * Routine task is Time based if  getRunHoursThreshInc is defined.
     * All conditions are eclusive. No mixing is allowed
     * @param vo
     * @throws BusinessException
     */
    public static void validateTaskType(Task vo) throws BusinessException{
        if(EqualsUtils.areEqual(Integer.valueOf(TaskTypeRef.getIdByCode(TaskTypeRef.ROUTINE_MAINT)),vo.getTaskTypeId())){
            //first check if Meter based
            if(vo.getRunHoursThreshInc()!=null){  //Meter based
                if(vo.getFrequencyId()!=null || vo.getFreqMonths()!=null || vo.getFreqDays()!=null) throw new BusinessException("Meter based frequency is not compatible with Time based settings!");
            }else {//Frequency based
                if(vo.getFrequencyId()==null && vo.getFreqMonths()==null && vo.getFreqDays()==null) throw new BusinessException("Routine Tasks must have frequency!");
            }
        }else{
            if(vo.getFrequencyId()!=null || vo.getRunHoursThreshInc()!=null || vo.getFreqDays()!=null || vo.getFreqDays()!=null) throw new BusinessException("Non Routine Tasks may not have frequency!");
        }
    }

    public static void validateStartDate(Task vo) throws BusinessException{
    }

    public static void validateEstimatedTime(Task vo) throws BusinessException{
        if(vo.getEstTime()==null || vo.getEstTime() <=0){
            throw new BusinessException("Estimated time must be defined and greater than zero");
        }

    }
    
    public static void validateTaskType(TaskDefinition vo) throws BusinessException{
        if(EqualsUtils.areEqual(Integer.valueOf(TaskTypeRef.getIdByCode(TaskTypeRef.ROUTINE_MAINT)),vo.getTaskTypeId())){
            //first check if Meter based
            if(vo.getRunHoursThreshInc()!=null){  //Meter based
                if(vo.getFrequencyId()!=null || vo.getFreqMonths()!=null || vo.getFreqDays()!=null) throw new BusinessException("Meter based frequency is not compatible with Time based settings!");
            }else {//Frequency based
                if(vo.getFrequencyId()==null && vo.getFreqMonths()==null && vo.getFreqDays()==null) throw new BusinessException("Routine Tasks must have frequency!");
            }
        }else{
            if(vo.getFrequencyId()!=null || vo.getRunHoursThreshInc()!=null || vo.getFreqDays()!=null || vo.getFreqDays()!=null) throw new BusinessException("Non Routine Tasks may not have frequency!");
        }
    }

    public static void validateEstimatedTime(TaskDefinition vo) throws BusinessException{
        if(vo.getEstTime()==null || vo.getEstTime() <=0){
            throw new BusinessException("Estimated time must be defined and greater than zero");
        }

    }
    
    /**
     * Validate that latestStartDate is the same for all tasks
     * @param tasks
     * @return
     * @throws BusinessException
     */
    public static Date extractLatestStartDate(List<Task> tasks) throws BusinessException{
        Date latestStartDate = null, latestStartDateOfTask;
        boolean firstTask = true;
        for (Task task : tasks) {
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
    
    private static Date calculateLastestStartDateOfTask(Task task){
    	Date latestStartDate=null;
    	Integer expiryTypeId = task.getExpiryTypeId();
    	String expiryType = TaskExpiryTypeRef.getCode(expiryTypeId);
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
    	}else if(TaskExpiryTypeRef.Status.EOW.toString().equals(expiryType)){
    		//Assumes Sunday is beginning of week
    		Calendar cal = Calendar.getInstance(); 
    		cal.setTime(new Date());
    		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    		if (dayOfWeek == Calendar.SATURDAY){
    			//Today is Saturday, no need to expire it today. It should expire next Saturday
    			cal.add(Calendar.DATE, 7);
    			latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    			return latestStartDate;
    		}
    		while (dayOfWeek != Calendar.SATURDAY){
    			cal.add(Calendar.DATE, 1);
    			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    		}
    		latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    	}else if(TaskExpiryTypeRef.Status.REL.toString().equals(expiryType)){
    		Calendar cal = Calendar.getInstance(); 
    		cal.setTime(new Date()); 
    		cal.add(Calendar.DAY_OF_MONTH, task.getExpiryNumOfDays().intValue());
    		latestStartDate = new java.sql.Date(cal.getTimeInMillis());
    	}
    	return latestStartDate;
    }
}
