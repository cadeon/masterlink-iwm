package org.mlink.agent.util;

import java.util.Calendar;
import java.util.List;

import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobStatusRef;
import org.mlink.agent.model.Project;
import org.mlink.agent.model.TaskView;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.TaskTypeRef;

/**
 * Job properties depend on properties of task that make the job
 * Loop through task and derive job properties into job prototype object
 *
 */
public class CreateJobHelper {

    public static Job buildProjectJobPrototype(List<TaskView> tasks, Project project) throws BusinessException{
        Job job  = buildJobPrototype(tasks);
        job.setStatus(JobStatusRef.Status.NYA.toString());
        job.setProject(project);
        return job;
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
     * 10. locatorId must be the same for all tasks
     * @param tasks  Job instances
     * @return
     * @throws BusinessException
     */
    public static Job buildJobPrototype(List<TaskView> tasks) throws BusinessException{

        if(tasks==null || tasks.size()==0){
            throw new BusinessException("Job must have at least one task!");
        }
        Job job = new Job();
        job.setSkillTypeRef(TaskPropertiesValidator.extractSkillType(tasks));
        //1. objectId must be the same for all tasks
        job.setObjectId(TaskPropertiesValidator.validateObjectId(tasks));

        //10. locatorId must be the same for all tasks
        job.setLocator(TaskPropertiesValidator.validateLocatorId(tasks));

        //8. job workers required = task with max workers required
        job.setNumberOfWorkers(TaskPropertiesValidator.extractMaxWorkersRequired(tasks));


        //2. jobPriority is the highest task priority
        job.setPriority(TaskPropertiesValidator.extractPriority(tasks));

        //6. job skill level is the highest task skill level
        job.setSkillLevel(TaskPropertiesValidator.extractSkillLevel(tasks));

        //3. estTime is the sum of tasks estTime
        job.setEstimatedTime(TaskPropertiesValidator.extractEstimatedTime(tasks));

        if(tasks.size()== 1){
            //4. jobDesc==taskDecs if 1 task, 'MultipleTasks' if more than 1 task
            job.setDescription(tasks.get(0).getDescription());
            //7. jobType==taskType if 1 task, 'Combined' if more than 1 task
            job.setJobTypeId(tasks.get(0).getTaskTypeRef().getId());
            //9. oranization is null if more than one task, otherwise inherit from the task
            job.setOrganizationId(tasks.get(0).getOrganizationId());
        }else if(tasks.size() > 1){
            job.setDescription(tasks.get(0).getGroupDescription());
            job.setJobType(TaskTypeRef.COMBINED);
        }

        job.setStatus(JobStatusRef.Status.INS.toString());
        job.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        job.setEarliestStart(date);
        //Calendar cal = Calendar.getInstance(); cal.setTime(date); cal.add(Calendar.MONTH,6);
        //job.setLatestStart(new java.sql.Date(cal.getTimeInMillis()));
        
        //set latest start
        job.setLatestStart(TaskPropertiesValidator.extractLatestStartDate(tasks));
        return job;
    }
}
