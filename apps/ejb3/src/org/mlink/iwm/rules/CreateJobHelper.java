package org.mlink.iwm.rules;

import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.ProjectStatusRef;
import org.mlink.iwm.lookup.ScheduleResponsibilityRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;

/**
 * Job properties depend on properties of task that make the job
 * Loop through task and derive job properties into job prototype object
 *
 */
public class CreateJobHelper {

    public static Job buildProjectJobPrototype(Job job, List tasks, Long projectId) throws BusinessException{
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Job vo  = buildJobPrototype(job, tasks);
        Project project = policy.get(Project.class, projectId);
        if(ProjectStatusRef.isFinalState(project.getStatusId())){
            throw new BusinessException(" Project is in a final state. Job cannot be added");
        }else if(ProjectStatusRef.PREPARING.equals(ProjectStatusRef.getLabel(project.getStatusId()))){
            vo.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.NYA));
        }else if(ProjectStatusRef.STARTED.equals(ProjectStatusRef.getLabel(project.getStatusId()))){
            vo.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.INS));
        }
        vo.setProject(new Project(projectId));
        return vo;
    }
    /**
     * Loop through task and derive job properties into job prototype object
     * Rules:
     * 1. objectId must be the same for all tasks
     * 2. jobPriority is the highest task priority
     * 3. estTime is the sum of tasks estTime
     * 4. jobDesc==taskDecs if 1 task, 'MultipleTasks' if more than 1 task
     * 5. all tasks in job must have same skillType
     * 6. job skill level is the highest task skill level
     * 7. jobType==taskType if 1 task, 'Combined' if more than 1 task
     * 8. job workers requred = task with max workers required
     * 9. oranization is null if more than one task, overwise inherit from the task
     * @param tasks  Job instances
     * @return
     * @throws BusinessException
     */
    public static Job buildJobPrototype(Job job, List tasks) throws BusinessException{
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        
        if(tasks==null || tasks.size()==0){
            throw new BusinessException("Job must have at least one task!");
        }
        
        if(job==null){
        	job = new Job();
	        
	        job.setSkillTypeId(TaskPropertiesValidator.extractSkillType(tasks));
	        //1. objectId must be the same for all tasks
	        job.setObjectId(TaskPropertiesValidator.validateObjectId(tasks));
	
	        //8. job workers required = task with max workers required
	        job.setNumberOfWorkers(TaskPropertiesValidator.extractMaxWorkersRequired(tasks));
	
	
	        //2. jobPriority is the highest task priority
	        job.setPriorityId(TaskPropertiesValidator.extractPriority(tasks));
	
	        //6. job skill level is the highest task skill level
	        job.setSkillLevelId(TaskPropertiesValidator.extractSkillLevel(tasks));
	
	        //3. estTime is the sum of tasks estTime
	        job.setEstTime(TaskPropertiesValidator.extractEstimatedTime(tasks));
	
	        job.setScheduleResponsibilityId(ScheduleResponsibilityRef.getIdByCode(ScheduleResponsibilityRef.SYSTEM));
	
	      //All of these tasks are being created into a job. Set last planned date for all of them, now. -Chris
	        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
	        for (Object task : tasks){
	        	//it's an Object because it's in a List. Make it the Task it is.
	        	Task taskVo =  (Task) task;
	        	//Really only need to do this for Routine tasks.
	        	if (taskVo.getTaskTypeId() == TaskTypeRef.getIdByCode("Routine")) {
	        		//It qualifies, get the real Task, instead of the , so we can make changes
	        		//Set last Planned
	        		taskVo.setLastPlannedDate(date);
	        		//Set hours threshold, too
	        		if (taskVo.getRunHoursThreshInc()!=null) {
	        	        Integer currenthrs = csf.getObject(taskVo).getRunHours();
	        	        Double hrsInc = taskVo.getRunHoursThreshInc();
	        	        Double runHoursThresh = hrsInc;
	        	        if(currenthrs!=null){
	        	        	runHoursThresh+=currenthrs.doubleValue();
	        	        }
	        	        taskVo.setRunHoursThresh(runHoursThresh);
	        		}
	        	}
	        }
	        
	        if(tasks.size()== 1){
	            //4. jobDesc==taskDecs if 1 task, 'MultipleTasks' if more than 1 task
	            job.setDescription(((Task)tasks.get(0)).getTaskDescription());
	            //7. jobType==taskType if 1 task, 'Combined' if more than 1 task
	            job.setJobTypeId(((Task)tasks.get(0)).getTaskTypeId());
	            //9. oranization is null if more than one task, overwise inherit from the task
	            job.setOrganizationId(((Task)tasks.get(0)).getOrganizationId());
	        }else if(tasks.size() > 1){
	            job.setDescription("Multiple Tasks");
	            job.setJobTypeId(TaskTypeRef.getIdByCode(TaskTypeRef.COMBINED));
	        }
	
	        job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.INS));
	        job.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
	        //New Jobs aren't sticky
	        job.setSticky("N");
	        job.setEarliestStart(date);
	        //Calendar cal = Calendar.getInstance(); cal.setTime(date); cal.add(Calendar.MONTH,6);
	        //job.setLatestStart(new java.sql.Date(cal.getTimeInMillis()));    
	        
	        //set latest start
	        job.setLatestStart(TaskPropertiesValidator.extractLatestStartDate(tasks));
	        
	        job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.INS));
        }
        return job;
    }
}
