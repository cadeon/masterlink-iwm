/*-----------------------------------------------------------------------------------
	File: ImplementationSFRemote.java
	Package: org.mlink.iwm.session
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.session;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskSequence;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.lookup.MWAccessTypeRef;

public interface ControlSF extends CommonSF{
	public JobSchedule getJobScheduleByPersonDateAndJob(Long personId, Date day, Long jobId) throws DataException;
    public JobTaskTime getJTTByJobScheduleAndJobTask(Long jobScheduleId, Long jobTaskId) throws DataException;
    /**
     * Update JobAction entity. vo.getJobActionId() will be used to identify the JobAction instance to update
     * @param vo JobAction
     */
    public void updateJobAction (JobAction bean);
    
    public Long getTotalTimeByJobId(Long jobId);
	public Long getTotalTimeByJobTaskId(Long jobTaskId);
    
    public Collection<WorkSchedule> getActiveWorkSchedulesByPersonAndDate(Long personId, Date targetDate);
	public Date getEarliestActiveScheduledDateByPersonAndDate(Long personId, Integer statusId, Date day);
    public void createJobSchedule(Long workScheduleId,Long jobId) throws BusinessException;
        
    /**
	 * Retrieves collection of earliest active workschedules for the given
	 * person.
	 * 
	 * @param person
	 *            person for whom to retrieve workschedules
	 * @
	 *             Andrei's note 12/20/06: Important this as an active Get
	 *             operation as it changes state of WS and Job. It also sets the
	 *             sticky flag to jobs. Must be used only by Mobile Worker
	 */
	public Collection<WorkSchedule> getCurrentWorkSchedules(Person p);
	public void completeProjects();
	public Job getJobByTenantRequest(Long id) throws DataException;
	public Long createJob(String creator, List tasks, Long projectId, Job jobVo) throws BusinessException, SQLException;
	
	/**
     * Retrieve the jobtasktime list for job specified by given jobschedule. Note that
     * if no task time exists for the specified date, a new task time entity.
     * @param jobScheduleId
     * @return col of JobTaskTime
     */
    public Collection<JobTaskTime> getTasksAndTimes(Long jobScheduleId);
    
    public Collection<JobTask> getJobTasksByJob(Long jobId);
    
    /**
     * Remove the job schedule for the specified workScheduleId and jobId.
     * @param jobId
     * @throws BusinessException
     */
    public void removeJobSchedule(Long workScheduleId,Long jobId) throws BusinessException;
    
    /**
     * Get all jobs for person on given day
     * @param personId
     * @param scheduledDate
     * @return Collection<Job>
     */
    public Collection<Job> getWorkerJobs(Long personId, java.util.Date scheduledDate);
    
    /**
     * Updates time for given jobTaskTimeId
     * @param jobTaskTimeId
     * @param time
     * @
     */
    public void updateJobTaskTime(Long jobTaskTimeId, int time, boolean cumilate);
    
    /////

    /**
     * Get TenantRequest of TenantRequest entity
     * @param tenantRequestId
     * @return TenantRequest
     * @
     */
    public TenantRequest getTenantRequest(Long  tenantRequestId) throws SQLException ;

    public TenantRequest getTenantRequestByJob(Long  jobId) throws SQLException;

//KEEPMESTART

    /**
     * Create Project from ProjectStencil
     * @param projectStencilId
     * @param createdBy TODO
     * @return
     * @throws CreateException
     */
    public Long createProjectFromStencil(Long projectStencilId, String createdBy) throws CreateException,BusinessException;

    /**
     * Get job tasks
     * @param jobId
     * @return
     * @
     */
    public Collection<JobTask> getJobTasks(Long jobId) ;

     /**
      * Delete Job of Job entity
      * * @param jobId
      */
     public void removeJob(Long  jobId) throws BusinessException;

    /**
     * Release all jobs for the project. Release meaning change status to INS  for jobs in NYA
     * @param projectId
     */
    public void startProject(Long  projectId) throws BusinessException;

    /**
     * Cancel project and its jobs
     * @param projectId
     */
    public void cancelProject(Long  projectId) throws BusinessException;

    /**
     * Update Job entity. vo.getJobId() will be used to identify the Job instance to update
     * @param vo Job
     * @throws SQLException 
     */
    public void updateJob (Job job) throws BusinessException, SQLException;

    /**
     * Creates tenant request and job for it
     * There are 2 types of Tenant requests (Internal and External) They have different ObjectDefs associated with them
     * @param vo
     * @return
     * @throws BusinessException
     */
    public Long createTenantRequest (TenantRequest tr) throws BusinessException;

    public Long createExternalWorkRequest (TenantRequest tr) throws BusinessException;

    public Date getEarliestActiveScheduledDate(Long personId)
        ;

    /**
     * Get all jobs for given workschedule
     * @param wsId
     * @return Collection of  Job
     */
    public Collection<Job> getJobsOnWorkSchedule(Long wsId);

    /**
     * Modified getCurrentWorkSchedules to get Schedules for agiven Date. This method is used when page accessed by super not the worker himself. see MobileWorkerJobs
     * Retrieves collection of earliest active workschedules for the given person and date.
     * @param personId ID of the person for whom to retrieve workschedules
     * @
     */
    public Collection<WorkSchedule> getWorkSchedulesForDate(Long personId, java.sql.Date scheduledDate) ;

    public void resetWorkSchedule(Long wsId) ;

    public void closeJob(Long jobId) throws BusinessException, SQLException;
    
    /**
     * Get the work schedule for the specified person, locator, and date
     * @param personId
     * @param locatorId
     * @param scheduledDate
     * @
     */
    public WorkSchedule getWorkSchedule(Long personId,Long locatorId, java.sql.Date scheduledDate) ;

    /**
     * Retrieve the task list and the specified day's time for each task. Note that
     * if no task time exists for the specified date, a new task time entity.
     *
     * @param jobId
     * @param personId
     * @param d The date for which to retrieve job task times
     * @throws CreateException
     * @
     */
    public Collection<JobTaskTime> getTasksAndTimes(Long jobId,Long personId,java.sql.Date d)
        throws CreateException, FinderException;
    /**
     * Main method for job creation
     * @param creator
     * @param tasks      Task objects
     * @param projectId  may be null
     * @return jobId
     * @throws BusinessException
     * @throws SQLException 
     */
    public Long createJob(String creator, List<Task> tasks, Long projectId) throws BusinessException, SQLException;
    public  WorkSchedule getWorkScheduleByPersonLocatorAndDate(Long personId , Long locatorId , Date day) throws DataException;

    public void logMWAccess(String userName, String scheduledDate, MWAccessTypeRef.Type type, Long jobTaskId, Double latitude, Double longitude, Integer accuracy);
    public void logMWAccess(String userName, String scheduledDate, Date eventTime, MWAccessTypeRef.Type type, Long jobTaskId, Double latitude, Double longitude, Integer accuracy);
    	    
    public Project getProject(Job job);
    public Collection<JobTask> getJobTasks(Job job);
   	//public TenantRequest getTenantRequest(Job job);
   	
   	public Action getAction(JobAction jobAction);
	public JobTask getJobTask(JobAction jobAction);
	

	public Task getTask(JobTask jobTask);
	public Job getJob(JobTask jobTask);
	public Collection<JobTaskTime> getJobTaskTimes(JobTask jobTask);
	public Collection<JobAction> getActions(JobTask jobTask);
	
	public JobTask getJobTask(JobTaskTime jtt);
	
	public Collection<Job> getJobs(Project project);
	
	public Collection<JobTask> getJobTasks(Task task);
	public TaskDefinition getTaskDefinition(Task task);
	public Collection<Action> getActions(Task task);
    public List<TaskSequence> getTaskSequences(Task task);
    public TaskGroup getTaskGroup(Task task);
    public ObjectEntity getObject(Task task);
    
    //public Job getJob(TenantRequest tr);
    
    public void addTask(Job job, Task task);
    
    public void addDefaultActions(Task task);

    public Date getEarliestActiveScheduledDate(Person person);
    public long startShift(Person person, String scheduledDate, Date eventTime) throws ParseException;
    public void endShift(Person person, String scheduledDate, Date eventTime) throws Exception;
    public void startBreak(Person person, String scheduledDate, Date eventTime) throws ParseException;
    public void endBreak(Person person, String scheduledDate, Date eventTime) throws ParseException;
    public void startTask(Long jobTaskId, Long jobTaskTimeId, String scheduledDate, Date eventTime) throws ParseException;
    public void stopTask(Long jobTaskId, Long jobTaskTimeId, String scheduledDate, Date eventTime) throws ParseException;
    public void doReplace(String userName, Long objectId, Long jobId, Long jobTaskId, Long jobTaskTimeId, Date eventTime)throws BusinessException;
    public Long checkAnyTaskInProgress(Long jobTaskTimeId);
    public List getJobWorkers(Long jobId, java.sql.Date date) throws SQLException;
	
}