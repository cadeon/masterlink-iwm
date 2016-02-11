package org.mlink.iwm.session;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.dao.JobSelectionsListDAO;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.InventoryTrace;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobStatusHist;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.MWAccessTrace;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Sequence;
import org.mlink.iwm.entity3.ShiftTrace;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskSequence;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.MWAccessTypeRef;
import org.mlink.iwm.lookup.ProjectStatusRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.notification.JobAssigned;
import org.mlink.iwm.notification.MailUtils;
import org.mlink.iwm.notification.WorkRequestCreated;
import org.mlink.iwm.rules.DeletionVisitor;
import org.mlink.iwm.rules.JobFactory;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.DateUtil;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andreipovodyrev Date: Mar 21, 2009
 */
@Interceptors(SessionInterceptor.class)
@Stateless(name = "ControlSFBean")
@RemoteBinding(jndiBinding = "ControlSFRemote")
@LocalBinding(jndiBinding = "ControlSFLocal")
public class ControlSFBean extends CommonSFBean implements ControlSFLocal,
		ControlSFRemote {
	private static final Logger logger = Logger.getLogger(ControlSFBean.class);

	@PersistenceContext(unitName = "iwm_dpc")
	private EntityManager manager;

	/**
	 * Log access to the mobile worker from iPhone
	 * 
	 * @param userName
	 * @param accessDate
	 * @param schedule
	 */
	public void logMWAccess(String userName, String scheduledDate,
			MWAccessTypeRef.Type type, Long jobTaskId, Double latitude,
			Double longitude, Integer accuracy) {
		Date eventTime = new Date();
		logMWAccess(userName, scheduledDate, eventTime, type, jobTaskId,
				latitude, longitude, accuracy);
	}

	/**
	 * Log access to the mobile worker from iPhone
	 * 
	 * @param userName
	 * @param accessDate
	 * @param schedule
	 */
	public void logMWAccess(String userName, String scheduledDate,
			Date eventTime, MWAccessTypeRef.Type type, Long jobTaskId,
			Double latitude, Double longitude, Integer accuracy) {
		MWAccessTrace mwAccessTrace = new MWAccessTrace();
		mwAccessTrace.setUserName(userName);
		mwAccessTrace.setAccessTime(eventTime);
		if (scheduledDate == null) {
			scheduledDate = DateUtil.displayShortDate(eventTime);
		} else {
			SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat(
					Config.getProperty(Config.YEAR4D_DATE_PATTERN));
			try {
				Date dt = LONG_DATE_FORMAT.parse(scheduledDate);
				scheduledDate = DateUtil.displayShortDate(dt);
			} catch (ParseException pe) {
				// do nothing as its of correct format of MM/DD/YY
				int x = 0;
			}
		}
		mwAccessTrace.setSchedule(scheduledDate);
		mwAccessTrace.setAccessTypeId(MWAccessTypeRef.getIdByCode(type));
		if (jobTaskId != null) {
			mwAccessTrace.setJobTaskId(jobTaskId);
		}

		if (accuracy != 0) {
			mwAccessTrace.setLatitude(latitude);
			mwAccessTrace.setLongitude(longitude);
			mwAccessTrace.setAccuracy(accuracy);
		}
		create(mwAccessTrace);
	}

	/**
	 * get workers assigned for the job for given date
	 * 
	 * @param jobId
	 * @param date
	 * @return List of Person
	 * @throws SQLException
	 */
	public List getJobWorkers(Long jobId, java.sql.Date date)
			throws SQLException {
		List criteriaSel = new ArrayList();
		criteriaSel.add(jobId);
		criteriaSel.add(date);
		JobSelectionsListDAO workersDao = new JobSelectionsListDAO();
		workersDao.setParameters(criteriaSel);
		return DBAccess.executeDAO(workersDao);
	}

	/**
	 * Delete Project of Project entity
	 * 
	 * @param projectId
	 * @throws RemoveException
	 */
	public void removeProject(Long projectId) throws RemoveException,
			BusinessException {
		try {
			Project project = get(Project.class, projectId);
			DeletionVisitor.delete(project);
		} catch (BusinessException be) {
			throw be; // do not rollback
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	public JobSchedule getJobScheduleByPersonDateAndJob(Long personId,
			Date day, Long jobId) throws DataException {
		/*
		 * select object(js) from JobSchedule as js, WorkSchedule as ws //where
		 * ws.personId = ?1 and ws.day = ?2 and ws.workScheduleId =
		 * js.workScheduleId and js.jobId = ?3 and js.deletedTime is null
		 */
		Map<String, Object> paramValueMap = new HashMap<String, Object>();
		paramValueMap.put("personId", personId);
		paramValueMap.put("day", day);
		paramValueMap.put("jobId", jobId);

		String sqlStr = "select distinct(js) from org.mlink.iwm.entity3.JobSchedule js, org.mlink.iwm.entity3.WorkSchedule ws"
				+ " where ws.id = js.workScheduleId and js.deletedTime is null "
				+ " and js.jobId = :jobId and ws.personId = :personId and ws.day = :day";

		Query query = manager.createQuery(sqlStr);
		if (!paramValueMap.isEmpty()) {
			for (Map.Entry<String, Object> e : paramValueMap.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		}

		return (JobSchedule) getUniqueData(query.getResultList());

	}

	public JobTaskTime getJTTByJobScheduleAndJobTask(Long jobScheduleId,
			Long jobTaskId) throws DataException {
		// select object(jtt) from JobTaskTime as jtt where jtt.jobScheduleId =
		// ?1 and jtt.jobTaskId=?2]]>
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobScheduleId", jobScheduleId);
		map.put("jobTask.id", jobTaskId);
		return (JobTaskTime) getUniqueData(getData(JobTaskTime.class, null, map));
	}

	static final String parseWarning1 = "WARNING: Unable to parse run hours in field condition.\n"
			+ "WARNING: Value only saved to job action field condition.\n"
			+ "WARNING: Object run hours not updated.\n"
			+ "WARNING: Text was : ";

	/**
	 * Update JobAction entity. vo.getJobActionId() will be used to identify the
	 * JobAction instance to update
	 * 
	 * @param vo
	 *            JobAction
	 */
	public void updateJobAction(JobAction bean) {
		update(bean);
		String fcStr = bean.getFieldCondition();
		if (!noe(fcStr)) {
			Action a = bean.getAction();
			if (a.isMeterReading()) {
				JobTask jt = bean.getJobTask();
				Task t = jt.getTask();
				ObjectEntity oe = t.getObject();
				try {
					oe.setRunHours(ConvertUtils.intVal(fcStr));
					update(oe);
				} catch (Exception ex) {
					logger.error(parseWarning1 + " " + fcStr);
				}
			}
		}
	}

	boolean noe(Object o) {
		if (o == null)
			return true;
		if (o.equals(""))
			return true;
		if (o instanceof String)
			return ((String) o).equalsIgnoreCase("null");
		return false;
	}

	/**
	 * Retrieves collection of earliest active workschedules for the given
	 * person.
	 * 
	 * @param person
	 *            person for whom to retrieve workschedules @ * Andrei's note
	 *            12/20/06: Important this as an active Get operation as it
	 *            changes state of WS and Job. It also sets the sticky flag to
	 *            jobs. Must be used only by Mobile Worker
	 */
	public Collection<WorkSchedule> getCurrentWorkSchedules(Person person) {
		Collection<WorkSchedule> wss = new ArrayList<WorkSchedule>();
		Collection<WorkSchedule> ce = getActiveWorkSchedulesForPerson(person);

		Collection<Job> jobs, jobCompleteds;
		Collection<JobTask> jobTasks;
		Collection<JobAction> jobActions;

		Integer statusCIA = JobStatusRef.getIdByCode(JobStatusRef.Status.CIA);
		Integer statusEJO = JobStatusRef.getIdByCode(JobStatusRef.Status.EJO);
		Integer statusDUN = JobStatusRef.getIdByCode(JobStatusRef.Status.DUN);

		for (WorkSchedule ws : ce) {
			// Update to "IP" regardless of prev. status
			// FIXME: Need to analyze what to do in cases of "TO" -- Timed Out
			ws.setStatusId(WorkScheduleStatusRef
					.getIdByCode(WorkScheduleStatusRef.Status.IP));

			Long locationId = ws.getLocatorId();
			if (locationId != null) {
				Locator locator = get(Locator.class, locationId);
				ws.setLocation(locator.getFullLocator());
			}

			jobs = getJobsOnWorkSchedule(ws.getId());
			jobCompleteds = new ArrayList<Job>();
			if (jobs != null && !jobs.isEmpty()) {
				// Set dispatch date b/c this method invocation means
				// the worker is viewing the job list
				for (Job job : jobs) {
					if (job.getCompletedDate() == null) {
						Integer statusId = job.getStatusId();
						if (!statusCIA.equals(statusId)
								&& !statusEJO.equals(statusId)
								&& !statusDUN.equals(statusId)) {
							jobCompleteds.add(job);
						} else {
							continue;
						}
					}

					job.setFullLocator(ws.getLocation());

					ObjectEntity o = get(ObjectEntity.class, job.getObjectId());
					// setting dispatched date is done in Start Shift but if
					// this is after start shift we will need to do it here
					/*
					 * if (job.getDispatchedDate() == null){
					 * job.setDispatchedDate(new java.sql.Date(System
					 * .currentTimeMillis())); }
					 */
					job.setObjectRef(o.getObjectRef());

					// andrei added jobschedule on 01/12/07 (v3.5)
					JobSchedule js = getJobSchedule(ws.getId(), job.getId());
					job.setJobScheduleId(js.getId());
					// all set above are transient so do not update job
					// update(job);

					jobTasks = job.getJobTasks();
					if (jobTasks != null) {
						jobTasks.size();
						for (JobTask jobTask : jobTasks) {
							jobActions = jobTask.getActions();
							if (jobActions != null) {
								jobActions.size();
							}
						}
					}
				}
			}
			ws.setJobs(jobCompleteds);
			update(ws);
			wss.add(ws);
		}
		return wss;
	}

	private Collection<Job> getJobListForWorkSchedule(Long workScheduleId) {
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
		fieldNameValueMap.put("id", workScheduleId);
		// return getData(
		// Job.class,
		// ", JobSchedule js where o.jobId = js.jobId and js.deletedTime is null",
		// fieldNameValueMap);
		// select object(ja) from JobAction as ja, JobTask jt where
		// jt.jobTaskId=ja.jobTaskId and jt.jobId = ?1]]>
		return (Collection<Job>) manager
				.createQuery(
						"select o from Job o, JobSchedule js where o.id = js.jobId and js.deletedTime "
								+ "is null and o.archivedDate is null and js.workScheduleId = :name")
				.setParameter("name", workScheduleId).getResultList();
	}

	public Collection<Job> getJobsOnWorkSchedule(Long workScheduleId) {
		Collection<Job> jobs = getJobListForWorkSchedule(workScheduleId);
		for (Object job1 : jobs) {
			Job job = (Job) job1;
			getJobSchedule(workScheduleId, job.getId()); // check for diplicates
		}
		return jobs;
	}

	/**
	 * Get job schedule for give workschedule and job if job was scheduled
	 * simultaneosly by user and Scheduler (race) we need to clean the
	 * duplicate. This sitiation has happened frequently when user creates a job
	 * (system schedule) and tries to schedule it right away without realizing
	 * that the job is subject for system schedule
	 */
	private JobSchedule getJobSchedule(Long workScheduleId, Long jobId) {
		Collection<JobSchedule> col = null;
		col = getJobScheduleByWorkScheduleAndJob(workScheduleId, jobId);

		JobSchedule entity;
		if (col == null || col.size() == 0)
			return null;
		Iterator it = col.iterator();
		entity = (JobSchedule) it.next();
		while (it.hasNext()) {
			JobSchedule js = (JobSchedule) it.next();
			logger.info("removing job schedule as a duplicate jsId="
					+ js.getId());
			js.setUser("sysdupscheck");
			js.setDeletedTime(new Date());
			update(js);
		}
		return entity;
	}

	private Collection<JobSchedule> getJobScheduleByWorkScheduleAndJob(
			Long workScheduleId, Long jobId) {
		// TODO: refaactor this to a generalized method
		return (Collection<JobSchedule>) manager
				.createQuery(
						"select js from JobSchedule js, WorkSchedule ws where ws.id = :name1 and ws.id = js.workScheduleId "
								+ " and js.jobId = :name2 and js.deletedTime is null")
				.setParameter("name1", workScheduleId)
				.setParameter("name2", jobId).getResultList();
		// "select o from " + clazz.getName() + " o where o." + fieldName+
		// " = :name").setParameter("name", fieldValue);
	}

	public Long getTotalTimeByJobId(Long jobId) {
		Collection<Long> total = (ArrayList<Long>) manager
				.createQuery(
						"select sum(jt.totalTime) from JobTask jt where jt.job.id= :name1")
				.setParameter("name1", jobId).getResultList();
		return total.iterator().next();
	}

	public Long getTotalTimeByJobTaskId(Long jobTaskId) {
		Collection<Long> total = (ArrayList<Long>) manager
				.createQuery(
						"select sum(jtt.time) from JobTaskTime jtt where jtt.jobTask.id= :name1")
				.setParameter("name1", jobTaskId).getResultList();
		return total.iterator().next();
	}

	public Collection<WorkSchedule> getActiveWorkSchedulesByPersonAndDate(
			Long personId, Date day) {
		// select object(ws) from WorkSchedule as ws where ws.personId=?1 and
		// ws.day=?2]]>
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("personId", personId);
		map.put("day", DateUtil.removeTime(day));
		return getData(WorkSchedule.class, " o.archivedDate is null", map);
	}

	public Date getEarliestActiveScheduledDateByPersonAndDate(Long personId,
			Integer statusId, Date day) {
		// select MIN(ws.day) from WorkSchedule as ws where ws.personId= ?1 and
		// ws.statusId <> ?2 and ws.day <= ?3
		Collection<Date> lst = manager
				.createQuery(
						"select MIN(ws.day) from WorkSchedule ws where ws.personId= :personId "
								+ " and ws.statusId <> :statusId and ws.day <= :day and archivedDate is null")
				.setParameter("personId", personId)
				.setParameter("personId", personId)
				.setParameter("personId", personId).getResultList();
		Date dt = null;
		if (lst != null || !lst.isEmpty()) {
			dt = lst.iterator().next();
		}
		return dt;
	}

	/**
	 * Finds active projects and checks if completed
	 */
	public void completeProjects() {
		logger.debug("Checking projects for completion");
		try {
			Collection<Project> projects = getProjectsInState(ProjectStatusRef
					.getIdByCode(ProjectStatusRef.STARTED));
			logger.debug("Found " + projects.size() + " projects in state "
					+ ProjectStatusRef.STARTED);
			for (Object o : projects) {
				Project project = (Project) o;
				logger.debug("Checking project " + project.getId() + " "
						+ project.getName() + " for completion");
				boolean allProjectJobsCompleted = true;
				Collection<Job> jobs = project.getJobs();
				for (Job job : jobs) {
					String tmp = "\tJob ID/Desc/State " + job.getId() + "/"
							+ job.getDescription() + "/"
							+ JobStatusRef.getCode(job.getStatusId());
					if (job.isTerminal()) {
						// logger.debug(tmp+ "  is in terminal state");
					} else {
						// logger.debug(tmp + " is still active.");
						allProjectJobsCompleted = false;
						break;
					}
				}
				if (allProjectJobsCompleted) {
					logger.debug("Setting project " + project.getId() + " "
							+ project.getName() + " complete");
					complete(project);
				} else {
					logger.debug("Project " + project.getId() + " "
							+ project.getName() + " is not yet complete");
				}
			}
		} catch (Exception e) {
			logger.debug("Did not find projects in state"
					+ ProjectStatusRef.STARTED);
		}
	}

	public Collection<Project> getProjectsInState(int statusId) {
		// select object(c) from Project as c where c.statusId=?1 and
		// c.archivedDate is null
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", statusId);
		return getData(Project.class, " o.archivedDate is null", map);
	}

	/**
	 * Create a job schedule for the specified workschedule and job
	 * 
	 * @param workScheduleId
	 * @param jobId
	 * @throws BusinessException
	 */
	public void createJobSchedule(Long workScheduleId, Long jobId)
			throws BusinessException {
		Job job = get(Job.class, jobId);
		if (JobStatusRef.getIdByCode(JobStatusRef.Status.DUN) == job
				.getStatusId()) {
			throw new BusinessException(
					"This job has been completed. No assignments can be made!");
		} else if (JobStatusRef.getIdByCode(JobStatusRef.Status.EJO) == job
				.getStatusId()) {
			throw new BusinessException(
					"This job has expired. No assignments can be made!");
		} else if (JobStatusRef.getIdByCode(JobStatusRef.Status.CIA) == job
				.getStatusId()) {
			throw new BusinessException(
					"This job has been cancelled. No assignments can be made!");
		} else if (SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN) == job
				.getSkillTypeId()) {
			throw new BusinessException("This job must be assigned a skill!");
		} 

		WorkSchedule ws = get(WorkSchedule.class, workScheduleId);
		logger.debug("Scheduling job ");
		logger.debug("jobid:" + jobId);
		logger.debug("personid:" + ws.getPersonId());
		logger.debug("locatorid:" + ws.getLocatorId());
		logger.debug("date:" + ws.getDay());

		if (ws.getDay().before(DateUtils.truncate(new Date(), Calendar.DATE))) {
			throw new BusinessException("Cannot schedule for a past date! "
					+ DateUtil.displayShortDate(ws.getDay()));
		}
		makeJobSchedule(ws, job);
		job.setSticky("Y");
		
		//Now that it's scheduled, ensure valid state.
		//If it's NYA go to INS
		if (JobStatusRef.getIdByCode(JobStatusRef.Status.NYA) == job
				.getStatusId()) {
			logger.info("Scheduling NYA job. Moving job to INS");
			job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.INS));
		}
		
		//Now that it's assigned, notify the worker that it's been assigned to.
		//We've already done all the important stuff above. If we run into issues here, we can just bail.
		
		if (ws.getDay().after(DateUtils.truncate(new Date(), Calendar.DATE))){
			//We're not assigning this job for *today*, so no need to notify
			logger.info("Not notifying due to schedule date being in the future");
			return;
		}
		
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		Person assignee = get(Person.class, ws.getPersonId());
		Person assigner = null;
		
		String username = getCallerPrincipal().getName();
		if (username !=null && !username.equals("super")){
			User user = psf.getUserByName(username);
			assigner = user.getPerson();
		}
		
		if (assignee.getParty().getEmail() == null || assigner == null){
			//Our worker doesn't have an email address in the system, or we don't have an assigner (it was super)
			logger.info("Unable to notify worker- no email address in the system, or job was assigned by super");
			return;
		} 
			
		//Hopefully at this point we're good to go.
		logger.debug("Sending email to assigned worker " + assignee.getParty().getEmail());
		logger.debug("TIME:" + System.currentTimeMillis());
		new JobAssigned(job,assignee,assigner).execute();
		}
		

	public JobSchedule makeJobSchedule(WorkSchedule ws, Job job) {
		JobSchedule entity;
		// if job was already scheduled by the Scheduler we need to avoid
		// creating a duplicate. This sitiation has happened
		// frequently when user creates a job (system schedule) and tries to
		// schedule it right away without realizing that the job is subject for
		// system schedule
		entity = getJobSchedule(ws.getId(), job.getId());
		if (entity == null) {
			entity = new JobSchedule(ws, job);
			// per Mike 08/20/07 the status of jobs with scheduled date set by
			// JSM
			// todo on-demand call to JSM is desirable
			// job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.DPD));
			job.setScheduledDate(new java.sql.Date(System.currentTimeMillis()));
			update(job);
			create(entity);
		} else {
			logger.debug("job "
					+ job.getId()
					+ " is already on ws "
					+ ws.getId()
					+ ". Likely was it done by the Scheduler. Skipping makeJobSchedule");
		}
		return entity;
	}

	public Job getJobByTenantRequest(Long tenantRequestId) throws DataException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tenantRequest.id", tenantRequestId);
		return (Job) getUniqueData(getData(Job.class, null, map));
	}

	/**
	 * Main method for job creation
	 * 
	 * @param creator
	 * @param tasks1
	 *            Task objects
	 * @param projectId
	 *            may be null
	 * @return jobId
	 * @throws BusinessException
	 * @throws SQLException
	 */
	public Long createJob(String creator, List tasks, Long projectId, Job jobVo)
			throws BusinessException, SQLException {
		List<Task> tasks1 = new ArrayList<Task>();
		List<Task> tasksO = tasks;
		if (tasks1 != null) {
			Task task;
			for (Task taskO : tasksO) {
				task = get(Task.class, taskO.getId());
				Collection<Action> actions = task.getActions();
				if (actions != null) {
					actions.size();
				}
				tasks1.add(task);
			}
		}

		Job job = JobFactory.createJob(jobVo, creator, tasks1, projectId);
		// Unless we set these, the new job we just created gets the defaults
		// not matter what the user input was -Chris
		// Though perhaps this should be in it's own method which checks for
		// nulls, etc. Item391, Item395
		job.setEarliestStart(jobVo.getEarliestStart());
		
		/* Along with setting earliest start, we must properly set last planned date
		on the tasks on this job. By this, I mean if the job gets created with a future
		earliest start, the last planned will be incorrect- next time the planner will kick
		out a new job seemingly too soon by end user view (since it's set by *created* date,
		not earliest start). As such, set last planned of every task to earliest start. */
		
		for (JobTask jt : job.getJobTasks()){
			jt.getTask().setLastPlannedDate(job.getEarliestStart());
		}
		
		job.setLatestStart(jobVo.getLatestStart());
		job.setNote(jobVo.getNote());
		job.setSkillTypeId(jobVo.getSkillTypeId());
		job.setSkillLevelId(jobVo.getSkillLevelId());
		job.setOrganizationId(jobVo.getOrganizationId());
		job.setEstTime(jobVo.getEstTime());
		job.setNumberOfWorkers(jobVo.getNumberOfWorkers());
		job.setPriorityId(jobVo.getPriorityId());
		job.setJobTypeId(jobVo.getJobTypeId());
		job.setScheduleResponsibilityId(jobVo.getScheduleResponsibilityId());
		//
		jobVo.setId(job.getId());
		validateJobDates(jobVo);

		update(job);

		return job.getId();
	}

	private void validateJobDates(Job vo) throws BusinessException {
		if (vo.getEarliestStart() != null && vo.getLatestStart() != null) {
			if (vo.getEarliestStart().after(vo.getLatestStart()))
				throwBusinessException("Earliest Start is after Latest Start!");
		}
		if (vo.getEarliestStart() != null && vo.getCreatedDate() != null) {
			if (vo.getEarliestStart().before(
					DateUtils.truncate(vo.getCreatedDate(), Calendar.DATE)))
				throwBusinessException("Earlist Start is before Created Date!");
		}
	}

	/**
	 * Get all jobs for person on given day
	 * 
	 * @param personId
	 * @param scheduledDate
	 * @return Collection <Job>
	 */
	public Collection<Job> getWorkerJobs(Long personId, Date scheduledDate) {
		Collection<Job> rtn = new ArrayList<Job>();
		try {
			Collection<WorkSchedule> schedules = getActiveWorkSchedulesByPersonAndDate(
					personId, scheduledDate);
			for (WorkSchedule ws : schedules) {
				rtn.addAll(getJobsOnWorkSchedule(ws.getId()));
			}
		} catch (Exception e) {
			throw new IWMException(e);
		}
		return rtn;
	}

	/**
	 * Retrieve the jobtasktime list for job specified by given jobschedule.
	 * Note that if no task time exists for the specified date, a new task time
	 * entity.
	 * 
	 * @param jobScheduleId
	 * @return col of JobTaskTime
	 */
	public Collection<JobTaskTime> getTasksAndTimes(Long jobScheduleId) {
		Collection<JobTaskTime> tasksNtimes = new ArrayList<JobTaskTime>();
		JobSchedule js = get(JobSchedule.class, jobScheduleId);
		
		//If the job's never been scheduled it has no time.
		if (js == null){return tasksNtimes;};

		try {
			// Get list of tasks for job
			Collection<JobTask> c = getJobTasksByJob(js.getJobId());
			JobTaskTime jtt;

			// Iterate through tasks and retrieve task times
			for (JobTask jt : c) {
				jtt = null;
				logger.debug("Job Schedule Id : " + js.getId()
						+ "\tJob Task Id     : " + jt.getId());
				try {
					jtt = getJTTByJobScheduleAndJobTask(js.getId(), jt.getId());
				} catch (DataException de) {
					// no prob
				}

				if (jtt == null) {
					// if no task time, create entity and insert values
					logger.debug("CSF.getTasksAndTimes: Creating JobTaskTime");
					jtt = new JobTaskTime();
					jtt.setJobTask(jt);
					jtt.setJobScheduleId(js.getId());
					jtt.setTime(0);
					create(jtt);
				}
				// package up JobTaskTimes and send forth.
				/*
				 * JobTaskTime jttvo = JobTaskTimeAccess.get(jtt);
				 * jttvo.setTaskDescription(jt.getTask().getTaskDescription());
				 * jttvo.setEstTime(jt.getTask().getEstTime());
				 * //jttvo.setTotalTime (jt.getTotalTime(jt.getJobTaskId()));
				 * //since v35 jttvo.setTotalTime(jt.getTotalTime());
				 */
				tasksNtimes.add(jtt);
			}
		} catch (Exception e) {
			throw new IWMException(e);
		}
		return tasksNtimes;
	}

	public Collection<JobTask> getJobTasksByJob(Long jobId) {
		// select object(jt) from JobTask as jt where jt.jobId = ?1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("job.id", jobId);
		return getData(JobTask.class, "o.job.archivedDate is null", map);
	}

	private Collection<JobAction> getJobActionsByJob(Long jobId) {
		// select object(ja) from JobAction as ja, JobTask jt where
		// jt.jobTaskId=ja.jobTaskId and jt.jobId = ?1]]>
		return (Collection<JobAction>) manager
				.createQuery(
						"select ja from JobAction ja, JobTask jt where jt.id=ja.jobTask.id and jt.job.archivedDate is null "
								+ " and jt.job.id = :name")
				.setParameter("name", jobId).getResultList();
	}

	/**
	 * Remove the job schedule for the specified workScheduleId and jobId.
	 * 
	 * @param jobId
	 * @throws BusinessException
	 */
	public void removeJobSchedule(Long workScheduleId, Long jobId)
			throws BusinessException {
		JobSchedule js = getJobSchedule(workScheduleId, jobId);
		if (js != null) {
			Job job = get(Job.class, jobId);
			job.setSticky("N");
			update(job);
			DeletionVisitor.delete(js);
		}
	}

	/**
	 * Updates time for given jobTaskTimeId
	 * 
	 * @param jobTaskTimeId
	 * @param time
	 *            @
	 */
	public void updateJobTaskTime(Long jobTaskTimeId, int time, boolean cumilate) {
		JobTaskTime bean = get(JobTaskTime.class, jobTaskTimeId);
		if (cumilate) {
			time += bean.getTime();
		}
		bean.setTime(time);
		update(bean);

		// Also need to see if the job has a started date, and
		// if not, set it
		JobTask jobTask = bean.getJobTask();
		Job job = jobTask.getJob();
		if (job.getStartedDate() == null) {
			job.setStartedDate(new Timestamp(System.currentTimeMillis()));
		}

		if (time > 0) {
			job.setSticky("Y"); // see disc at

			// https://staff.masterlinkcorp.com/twiki/bin/view/Bugs/Item303
			// andrei: 11/09/06: set total time for job task. v35
			jobTask.setTotalTime(getTotalTimeByJobTaskId(jobTask.getId()));
			job.setTotalTime(getTotalTimeByJobId(job.getId()));
		}

		update(jobTask);
		update(job);
	}

	// ///
	/**
	 * Get TenantRequest of TenantRequest entity
	 * 
	 * @param tenantRequestId
	 * @return TenantRequest @
	 */
	public TenantRequest getTenantRequest(Long tenantRequestId)
			throws SQLException {
		TenantRequest bean = get(TenantRequest.class, tenantRequestId);
		Job job = getJobByTenantRequest(tenantRequestId);

		// all values below are transient. so no need to save
		bean.setJobStatusId(job.getStatusId());
		bean.setCompletedDate(job.getCompletedDate());

		// get field confitions (ie job actions)
		bean.setJobActions(getJobActionsByJob(job.getId()));

		// get workers assigned for the job today (look for todays schedules
		// only)
		bean.setWorkers(getJobWorkers(job.getId(),
				new java.sql.Date(System.currentTimeMillis())));
		return bean;
	}

	public TenantRequest getTenantRequestByJob(Long jobId) throws SQLException {
		Job job = get(Job.class, jobId);
		TenantRequest bean = job.getTenantRequest();

		// all values below are transient. so no need to save
		bean.setJobStatusId(job.getStatusId());
		bean.setCompletedDate(job.getCompletedDate());
		bean.setJobDescription(job.getDescription());
		// get field confitions (ie job actions)
		bean.setJobActions(getJobActionsByJob(job.getId()));
		// get workers assigned for the job today (look for todays schedules
		// only)
		bean.setWorkers(getJobWorkers(job.getId(),
				new java.sql.Date(System.currentTimeMillis())));

		return bean;
	}

	// KEEPMESTART

	/**
	 * Create Project from ProjectStencil
	 * 
	 * @param projectStencilId
	 * @return
	 * @throws CreateException
	 */
	public Long createProjectFromStencil(Long projectStencilId, String createdBy)
			throws CreateException, BusinessException {
		Sequence theProjectStencil = get(Sequence.class, projectStencilId);
		Project proj = makeProject(theProjectStencil, createdBy);
		return proj.getId();
	}

	private Project makeProject(Sequence projectStencil, String createdBy)
			throws CreateException, BusinessException {
		logger.debug("Creating project from project stencil " + projectStencil);
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		Project project;
		project = new Project(projectStencil);
		project.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		project.setCreatedBy(createdBy);
		create(project);
		Collection<TaskSequence> stencilTasks = psf
				.getTaskSequences(projectStencil);
		List<Task> tasks;
		for (TaskSequence ts : stencilTasks) {
			tasks = new ArrayList<Task>();
			Task task = ts.getTask();
			if (Constants.YES.equals(task.getActive())) {
				logger.debug("Creating job from task " + task.getId());
				tasks.add(task);
				Job job = JobFactory.createJob(null, project.getCreatedBy(),
						tasks, project.getId());
				job.setSequenceLevel(ts.getSequenceLevel());
				update(job);
			} else {
				logger.debug("Ignoring inactive task " + task.getId());
			}
		}
		return project;
	}

	/**
	 * Get job tasks
	 * 
	 * @param jobId
	 * @return @
	 */
	public Collection<JobTask> getJobTasks(Long jobId) {
		Job job = get(Job.class, jobId);
		return job.getJobTasks();
	}

	/**
	 * Delete Job of Job entity * @param jobId
	 */
	public void removeJob(Long jobId) throws BusinessException {
		try {
			Job job = get(Job.class, jobId);
			DeletionVisitor.delete(job);
		} catch (BusinessException be) {
			throw be; // do not rollback
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Release all jobs for the project. Release meaning change status to INS
	 * for jobs in NYA
	 * 
	 * @param projectId
	 */
	public void startProject(Long projectId) throws BusinessException {
		Project project = get(Project.class, projectId);
		if (project.getStatusId() == null) {
			// data clean up
			project.setStatusId(ProjectStatusRef
					.getIdByCode(ProjectStatusRef.PREPARING));
		}
		if (ProjectStatusRef.getIdByCode(ProjectStatusRef.PREPARING) != project
				.getStatusId()) {
			throw new BusinessException("Cannot start project in "
					+ ProjectStatusRef.getLabel(project.getStatusId())
					+ " state");
		}
		start(project);
	}

	/**
	 * Cancel project and its jobs
	 * 
	 * @param projectId
	 */
	public void cancelProject(Long projectId) throws BusinessException {
		Project project = get(Project.class, projectId);
		if (project.getCompletedDate() != null)
			throw new BusinessException(" Project in "
					+ ProjectStatusRef.getLabel(project.getStatusId())
					+ " state cannot be cancelled");
		cancel(project);
	}

	/**
	 * Update Job entity. vo.getJobId() will be used to identify the Job
	 * instance to update
	 * 
	 * @param vo
	 *            Job
	 * @throws SQLException
	 */
	public void updateJob(Job job) throws BusinessException, SQLException {
		validateJobDates(job);
		if (job.isTerminal()) {
			job.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
			// TODO: above should be timestamp
		}
		update(job);
	}

	/**
	 * Creates tenant request and job for it There are 2 types of Tenant
	 * requests (Internal and External) They have different ObjectDefs
	 * associated with them
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public Long createTenantRequest(TenantRequest tr) throws BusinessException {
		tr.setRequestType(TenantRequest.INTERNAL_REQUEST);
		Long odId = Long.valueOf(Config
				.getProperty(Config.AREA_OBJECT_DEFINITION_ID));
		ObjectDefinition od = get(ObjectDefinition.class, odId);
		create(tr);
		Job job = JobFactory.createJob(tr, od);
		job.setDescription(ConvertUtils.substring(tr.getJobDescription(), 150));
		update(job);
		return tr.getId();
	}

	public Long createExternalWorkRequest(TenantRequest tr)
			throws BusinessException {
		Long requestId;
		tr.setRequestType(TenantRequest.EXTERNAL_REQUEST);
		Long odId = Long.valueOf(Config
				.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID));
		ObjectDefinition od = get(ObjectDefinition.class, odId);
		create(tr);
		Job job = JobFactory.createJob(tr, od);
		logger.debug("job for external work request is created " + job.getId());
		job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.NYA));
		update(job);

		requestId = tr.getId();
		if (Constants.YES.equals(tr.getUrgent())) {
			logger.debug("Urgent job is requested");
			try {
				job.setJobTypeId(TaskTypeRef.getIdByCode(TaskTypeRef.URGENT));
				logger.debug("setting job type to Urgent");
			} catch (IWMException e) {
				logger.warn("Urgent task type is not defined in system. Table TASK_TYPE_REF");
				MailUtils.informSupport("Task type URGENT is not defined!",
						"The job type will not be set to URGENT for job ="
								+ job.getId());
			}
			// this is needed for emergency request notification
			Locator locator = get(Locator.class, job.getLocatorId());
			String contact = locator.getEmergencyContact();
			while (contact == null || contact.trim().length() == 0) {
				if (locator.getParentId() != null) {
					// check parent's emergency contact
					locator = get(Locator.class, locator.getParentId());
					contact = locator.getEmergencyContact();
				} else {
					break;
				}
			}

			if (contact == null || contact.trim().length() == 0) { // todo need
				// velocity template for system warnings
				logger.warn("The emergency contact for "
						+ locator.getFullLocator()
						+ " is not defined! System is not able to notify a worker for urgent job request "
						+ job.getId());
				MailUtils.informSupport(
						"The emergency contact for " + locator.getFullLocator()
								+ " is not defined!",
						" System is not able to notify a worker for urgent job request "
								+ job.getId());
			} else {
				logger.debug("Sending email to emergency contact " + contact);
				tr.setEmergencyContact(contact);
				update(tr);
				logger.debug("TIME:" + System.currentTimeMillis());
				new WorkRequestCreated(tr).execute();
			}
			update(job);
		}

		return requestId;
	}

	public Date getEarliestActiveScheduledDate(Long personId) {
		return getEarliestActiveScheduledDate(get(Person.class, personId));
	}

	/**
	 * Modified getCurrentWorkSchedules to get Schedules for agiven Date. This
	 * method is used when page accessed by super not the worker himself. see
	 * MobileWorkerJobs Retrieves collection of earliest active workschedules
	 * for the given person and date.
	 * 
	 * @param personId
	 *            ID of the person for whom to retrieve workschedules @
	 */
	public Collection<WorkSchedule> getWorkSchedulesForDate(Long personId,
			java.sql.Date scheduledDate) {
		Collection<WorkSchedule> c = new ArrayList<WorkSchedule>();

		Person person = get(Person.class, personId);
		Collection<WorkSchedule> ce = getActiveWorkSchedulesByPersonAndDate(
				person.getId(), new Date(scheduledDate.getTime()));

		for (WorkSchedule ws : ce) {
			Collection<Job> jobs = getJobsOnWorkSchedule(ws.getId());
			for (Job job : jobs) {
				ObjectEntity o = get(ObjectEntity.class, job.getObjectId());
				// all below are transient
				job.setObjectRef(o.getObjectRef());
				Locator l = get(Locator.class, job.getLocatorId());
				job.setFullLocator(l.getFullLocator());
				JobSchedule js = getJobSchedule(ws.getId(), job.getId());
				job.setJobScheduleId(js.getId());
			}
			ws.setJobs(jobs);
			c.add(ws);
		}
		return c;
	}

	public void resetWorkSchedule(Long wsId) {
		WorkSchedule ws = get(WorkSchedule.class, wsId);
		ws.setStatusId(WorkScheduleStatusRef
				.getIdByCode(WorkScheduleStatusRef.Status.IP));
		update(ws);
	}

	public void closeJob(Long jobId) throws BusinessException, SQLException {
		Job job = get(Job.class, jobId);
		if (checkJobInProgress(jobId)) {
			throw new BusinessException(
					"Job is in progress. Please stop all tasks and try again.");
		}

		if (!job.isTerminal()) {
			// per Mike 08/20/07 the status of jobs with completed date set by
			// JSM
			// todo on-demand call to JSM is desirable
			// Raghu: Chris and I are making all status changes where req
			// instead of waiting for JSM to do it eventually.
			job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.DUN));
			job.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
			//Create the status history here. It doesn't work in the update function below (the state change is already made).
			JobStatusHist jsh = new JobStatusHist(job.getId(), job.getStatusId());
			create(jsh);
			
			update(job);
		} else {
			throw new BusinessException("Job is already closed. State: "
					+ JobStatusRef.getLabel(job.getStatusId()));
		}
	}

	private boolean checkJobInProgress(Long jobId) {
		boolean jobInProgress = false;
		Job job = get(Job.class, jobId);
		long inProgressStatus = JobStatusRef
				.getIdByCode(JobStatusRef.Status.JIP);

		if (job.getStatusId() != null
				&& inProgressStatus == job.getStatusId().longValue()) {
			jobInProgress = true;
		}
		return jobInProgress;
	}

	/**
	 * Get the work schedule for the specified person, locator, and date
	 * 
	 * @param personId
	 * @param locatorId
	 * @param scheduledDate
	 *            @
	 */
	public WorkSchedule getWorkSchedule(Long personId, Long locatorId,
			java.sql.Date scheduledDate) {
		return getWorkScheduleByPersonLocatorAndDate(personId, locatorId,
				scheduledDate);
	}

	/**
	 * Retrieve the task list and the specified day's time for each task. Note
	 * that if no task time exists for the specified date, a new task time
	 * entity.
	 * 
	 * @param jobId
	 * @param personId
	 * @param d
	 *            The date for which to retrieve job task times
	 * @throws CreateException
	 *             @
	 */
	public Collection<JobTaskTime> getTasksAndTimes(Long jobId, Long personId,
			java.sql.Date d) throws CreateException, FinderException {
		JobSchedule js = null;
		try {
			// Get job schedule for use in job task time retrieval/creation
			js = getJobScheduleByPersonDateAndJob(personId, d, jobId);
		} catch (DataException fe) {
			log("Job schedule not found for person id:" + personId + ";date:"
					+ DateUtil.displayShortDate(d) + ";job id:" + jobId);
		}
		if (js == null)
			return null;

		return getTasksAndTimes(js.getJobId());
	}

	/**
	 * Main method for job creation
	 * 
	 * @param creator
	 * @param tasks
	 *            Task objects
	 * @param projectId
	 *            may be null
	 * @return jobId
	 * @throws BusinessException
	 * @throws SQLException
	 */
	public Long createJob(String creator, List<Task> tasks, Long projectId)
			throws BusinessException, SQLException {
		return createJob(creator, tasks, projectId, null);
	}

	private void log(String s) {
		logger.debug(s);
	}

	public WorkSchedule getWorkScheduleByPersonLocatorAndDate(Long personId,
			Long locatorId, Date day) throws DataException {
		// select object(ws) from Person as p, WorkSchedule as ws where
		// p.personId = ?1 and p.personId = ws.personId and ws.locatorId = ?2
		// and ws.day = ?3]]>
		return (WorkSchedule) getUniqueData(manager
				.createQuery(
						"select ws from Person p, WorkSchedule ws where p.personId = :personId and p.personId = ws.personId and ws.locatorId = :locatorId and ws.day = :day")
				.setParameter("personId", personId).getResultList());
	}

	public Project getProject(Job job) {
		return job.getProject();
	}

	public Collection<JobTask> getJobTasks(Job job) {
		Collection<JobTask> jobTasks = job.getJobTasks();
		if (jobTasks != null) {
			jobTasks.size();
		}
		return jobTasks;
	}

	/*
	 * public TenantRequest getTenantRequest(Job job) { return
	 * job.getTenantRequest(); }
	 */

	public Action getAction(JobAction jobAction) {
		return jobAction.getAction();
	}

	public JobTask getJobTask(JobAction jobAction) {
		return jobAction.getJobTask();
	}

	public Task getTask(JobTask jobTask) {
		return jobTask.getTask();
	}

	public Job getJob(JobTask jobTask) {
		return jobTask.getJob();
	}

	public Collection<JobTaskTime> getJobTaskTimes(JobTask jobTask) {
		if (jobTask.getJobTaskTimes() != null)
			jobTask.getJobTaskTimes().size();
		return jobTask.getJobTaskTimes();
	}

	public Collection<JobAction> getActions(JobTask jobTask) {
		if (jobTask.getActions() != null)
			jobTask.getActions().size();
		return jobTask.getActions();
	}

	public JobTask getJobTask(JobTaskTime jtt) {
		return jtt.getJobTask();
	}

	public Collection<Job> getJobs(Project project) {
		if (project.getJobs() != null)
			project.getJobs().size();
		return project.getJobs();
	}

	public Collection<JobTask> getJobTasks(Task task) {
		if (task.getJobTasks() != null)
			task.getJobTasks().size();
		return task.getJobTasks();
	}

	public TaskDefinition getTaskDefinition(Task task) {
		return task.getTaskDefinition();
	}

	public Collection<Action> getActions(Task task) {
		if (task.getActions() != null)
			task.getActions().size();
		return task.getActions();
	}

	public List<TaskSequence> getTaskSequences(Task task) {
		if (task.getTaskSequences() != null)
			task.getTaskSequences().size();
		return task.getTaskSequences();
	}

	public TaskGroup getTaskGroup(Task task) {
		return task.getTaskGroup();
	}

	public ObjectEntity getObject(Task task) {
		return task.getObject();
	}

	/*
	 * public Job getJob(TenantRequest tr) { return tr.getJob(); }
	 */

	private Collection<WorkSchedule> getActiveWorkSchedulesForPerson(
			Person person) {
		Collection<WorkSchedule> cws;
		Date targetDate = getEarliestActiveScheduledDate(person);
		logger.debug("PersonBean:getActiveWorkSchedules: target date is "
				+ targetDate);
		cws = getActiveWorkSchedulesByPersonAndDate(new Long(person.getId()),
				targetDate);
		logger.debug("PersonBean:getActiveWorkSchedules: work schedules " + cws);
		return cws;
	}

	public Date getEarliestActiveScheduledDate(Person person) {
		Date d = new Date(System.currentTimeMillis());
		if ("true".equals(Config.getProperty(
				Config.EARLISEST_ACTIVE_SCHEDULE_DATE_IS_TODAY, "false"))) {
			return d;
		} else {
			int statusDone = WorkScheduleStatusRef
					.getIdByCode(WorkScheduleStatusRef.Status.DUN);
			return getEarliestActiveScheduledDateByPersonAndDate(new Long(
					person.getId()), new Integer(statusDone), d);
		}
	}

	public void addTask(Job job, Task task) {
		JobTask jobTask = new JobTask();
		jobTask.setTask(task);
		jobTask.setJob(job);
		jobTask.setNumberOfWorkers(job.getNumberOfWorkers());
		jobTask.setSkillTypeId(task.getSkillTypeId());
		jobTask.setSkillLevelId(task.getSkillLevelId());
		jobTask.setPriorityId(job.getPriorityId());
		jobTask.setEstTime(task.getEstTime());
		jobTask.setTaskDescription(task.getTaskDescription());
		jobTask.setTaskTypeId(task.getTaskTypeId());

		Collection<JobTask> jobTasks = getJobTasks(job);
		if (jobTasks == null) {
			jobTasks = new ArrayList<JobTask>();
			job.setJobTasks(jobTasks);
		}
		jobTasks.add(jobTask);

		Collection<Action> actions = getActions(task);
		for (Action action : actions) {
			if (action.getArchivedDate() == null) {
				JobAction jobAction = new JobAction(action);
				jobAction.setJobTask(jobTask);
				Collection<JobAction> jobActions = getActions(jobTask);
				if (jobActions == null) {
					jobActions = new HashSet<JobAction>();
					jobTask.setActions(jobActions);
				}
				jobActions.add(jobAction);
			}
		}
	}

	/**
	 * Start project, Release all jobs for the project. Release meaning change
	 * status to INS for jobs in NYA
	 */
	private void start(Project p) {
		logger.debug("Starting project " + p.getId());
		Collection<Job> col = getJobs(p);
		for (Job job : col) {
			if (job.getStatusId().equals(
					JobStatusRef.getIdByCode(JobStatusRef.Status.NYA))) {
				job.setStatusId(JobStatusRef
						.getIdByCode(JobStatusRef.Status.INS));
				update(job);
			}
		}
		p.setStatusId(ProjectStatusRef.getIdByCode(ProjectStatusRef.STARTED));
		p.setStartedDate(new Date(System.currentTimeMillis()));
		update(p);
	}

	/**
	 * Cancel project and its jobs
	 */
	public void cancel(Project p) {
		logger.debug("Cancelling project " + p.getId());
		Collection<Job> col = getJobs(p);
		for (Object aCol : col) {
			Job job = (Job) aCol;
			if (!job.getStatusId().equals(
					JobStatusRef.getIdByCode(JobStatusRef.Status.DUN))
					& !job.getStatusId().equals(
							JobStatusRef.getIdByCode(JobStatusRef.Status.EJO))) {
				job.setStatusId(JobStatusRef
						.getIdByCode(JobStatusRef.Status.CIA));
				update(job);
			}
		}
		p.setStatusId(ProjectStatusRef.getIdByCode(ProjectStatusRef.CANCELLED));
		p.setCompletedDate(new Date(System.currentTimeMillis()));
		update(p);
	}

	/**
	 * Complete project. Warning check for jobs state should be done elsewhere
	 */
	public void complete(Project p) {
		logger.debug("Completing project " + p.getId());
		p.setStatusId(ProjectStatusRef.getIdByCode(ProjectStatusRef.COMPLETED));
		p.setCompletedDate(new Date(System.currentTimeMillis()));
		update(p);
	}

	/**
	 * When Task created it must have default actions created also
	 */
	public void addDefaultActions(Task task) {
		Action action1 = new Action();
		action1.setVerb("Record");
		action1.setName("Worker");
		action1.setModifier("Feedback");
		action1.setSequence(1);
		action1.setCustom(Constants.CUSTOMIZED_YES);

		Action action2 = new Action();
		action2.setVerb("Does This");
		action2.setName("Job Require");
		action2.setModifier("Further Action?");
		action2.setSequence(2);
		action2.setCustom(Constants.CUSTOMIZED_YES);

		Collection actions = getActions(task);
		actions.add(action1);
		actions.add(action2);
	}

	public long startShift(Person person, String scheduledDate, Date eventTime)
			throws ParseException {
		java.sql.Date sqlDt = new java.sql.Date(System.currentTimeMillis());
		if (scheduledDate != null) {
			sqlDt = DateUtil.parseShortDate(scheduledDate);
		}
		Collection<WorkSchedule> cws;
		cws = getWorkSchedulesForDate(new Long(person.getId()), sqlDt);
		int workerStatusIP = WorkScheduleStatusRef
				.getIdByCode(WorkScheduleStatusRef.Status.IP);
		int workerStatusNYS = WorkScheduleStatusRef
				.getIdByCode(WorkScheduleStatusRef.Status.NYS);
		int workerStatus = workerStatusIP;
		boolean alreadyInShift = false;
		// int jobStatus = JobStatusRef.getIdByCode(JobStatusRef.Status.DJO);

		for (WorkSchedule ws : cws) { // implements filter by locator
			// lst.addAll(CopyUtils.copyProperties(MWJob.class,
			// wsvo.getJobs()));

			// setting job status and dispatched date in
			// action-MobileWorkerMaint
			if (workerStatusNYS == ws.getStatusId()) {
				workerStatus = workerStatusIP;
				ws.setStatusId(workerStatus);
				update(ws);
			} else {
				alreadyInShift = true;
				workerStatus = ws.getStatusId();
				// break;
			}
		}

		if (!alreadyInShift) {
			logMWAccess(person.getUsername(), scheduledDate, eventTime,
					MWAccessTypeRef.Type.MWSS, null, 0d, 0d, 0);

			// log into shiftTrace
			ShiftTrace st = new ShiftTrace();
			st.setPerson(person);
			st.setShiftStartDate(eventTime);
			st.setUserName(person.getUsername());
			st.setSchedule(scheduledDate);
			create(st);
		}
		return workerStatus;
	}

	public void endShift(Person person, String scheduledDate, Date eventTime)
			throws Exception {
		java.sql.Date sqlDt = DateUtil.parseShortDate(scheduledDate);
		Collection<WorkSchedule> cws;
		cws = getWorkSchedulesForDate(person.getId(), sqlDt);
		int dunStatus = WorkScheduleStatusRef
				.getIdByCode(WorkScheduleStatusRef.Status.DUN);
		int ipStatus = WorkScheduleStatusRef
				.getIdByCode(WorkScheduleStatusRef.Status.IP);

		Integer jobStatusCIA = JobStatusRef
				.getIdByCode(JobStatusRef.Status.CIA);
		Integer jobStatusEJO = JobStatusRef
				.getIdByCode(JobStatusRef.Status.EJO);
		Integer jobStatusDUN = JobStatusRef
				.getIdByCode(JobStatusRef.Status.DUN);

		int timeOnJobTasks = 0;

		for (WorkSchedule ws : cws) { // implements filter by locator
			if (ws.getStatusId() != null
					&& ws.getStatusId().longValue() != ipStatus) {
				throw new Exception(
						"Worker should be in 'In Progress' status inorder to end the Shift.");
			}

			// also stop tasks
			List<JobSchedule> jss = (List<JobSchedule>) getJobSchedulesByWSId(ws
					.getId());
			List<JobTask> jobTasks;
			List<JobTaskTime> jobTaskTimes;

			if (jss != null) {
				timeOnJobTasks = 0;
				long inProgressStatus = JobStatusRef
						.getIdByCode(JobStatusRef.Status.JIP);

				Job job;
				for (JobSchedule js : jss) {
					job = get(Job.class, js.getJobId());
					if (job.getStatusId() != null) {
						long statusId = job.getStatusId().longValue();
						if (jobStatusCIA.equals(statusId)
								|| jobStatusEJO.equals(statusId)
								|| jobStatusDUN.equals(statusId)) {
							if (job.getCompletedDate().after(eventTime)) {
								throw new BusinessException(
										"End Shift Event Date: " + eventTime
												+ " is before a job end time.");
							}
						}

						if (job.getStatusId() != null
								&& inProgressStatus == job.getStatusId()
										.longValue()) {
							if (job.getStartedDate().after(eventTime)) {
								throw new BusinessException(
										"End Shift Event Date: "
												+ eventTime
												+ " is before a job start time.");
							}

							jobTasks = (List<JobTask>) job.getJobTasks();
							for (JobTask jobTask : jobTasks) {
								jobTaskTimes = (List<JobTaskTime>) jobTask
										.getJobTaskTimes();
								for (JobTaskTime jobTaskTime : jobTaskTimes) {
									if (jobTaskTime.getJobScheduleId()
											.longValue() == js.getId()) {
										try {
											stopTask(jobTask.getId(),
													jobTaskTime.getId(),
													scheduledDate, eventTime);
											break;
										} catch (RuntimeException re) {
											// task already closed. no problem.
											int x = 0;
										}
									}
								}
							}
						}
					}

					// calculating time spent on jobtasks
					jobTasks = (List<JobTask>) job.getJobTasks();
					for (JobTask jobTask : jobTasks) {
						jobTaskTimes = (List<JobTaskTime>) jobTask
								.getJobTaskTimes();
						for (JobTaskTime jobTaskTime : jobTaskTimes) {
							if (jobTaskTime.getJobScheduleId().longValue() == js
									.getId()) {
								timeOnJobTasks += jobTaskTime.getTime();
							}
						}
					}
				}
			}

			ws.setStatusId(dunStatus);
			update(ws);

			ShiftTrace st = getShiftTracebyPersonAndDate(person.getId(),
					scheduledDate);
			st.setShiftStopDate(eventTime);
			st.setTimeOnBreaksInMins(getTimeOnBreaksInMins(st));
			st.setTimeOnJobTasks(timeOnJobTasks);
			update(st);
		}
		logMWAccess(person.getUsername(), scheduledDate, eventTime,
				MWAccessTypeRef.Type.MWES, null, 0d, 0d, 0);
	}

	private ShiftTrace getShiftTracebyPersonAndDate(Long personId,
			String scheduledDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("person.id", personId);
		map.put("schedule", scheduledDate);
		return (ShiftTrace) getUniqueData(getData(ShiftTrace.class, null, map));
	}

	private int getTimeOnBreaksInMins(ShiftTrace st) {
		int timeOnBreaksInMins = 0;
		int endShiftStatus = MWAccessTypeRef
				.getIdByCode(MWAccessTypeRef.Type.MWES);
		int startBreakStatus = MWAccessTypeRef
				.getIdByCode(MWAccessTypeRef.Type.MWSB);
		int endBreakStatus = MWAccessTypeRef
				.getIdByCode(MWAccessTypeRef.Type.MWEB);
		List<MWAccessTrace> traces = (List<MWAccessTrace>) getMWAccessTrace(
				st.getUserName(), st.getShiftStartDate());
		if (traces != null && !traces.isEmpty()) {
			Date start = null;
			for (MWAccessTrace trace : traces) {
				if (endShiftStatus == trace.getAccessTypeId()) {
					break;
				} else if (startBreakStatus == trace.getAccessTypeId()) {
					start = trace.getAccessTime();
				} else if (endBreakStatus == trace.getAccessTypeId()) {
					timeOnBreaksInMins += Math.round((trace.getAccessTime()
							.getTime() - start.getTime()) / 60000.0f); // as
																		// returned
																		// value
																		// is in
																		// millisecs
					start = null;
				}
			}
		}
		return timeOnBreaksInMins;
	}

	public void startBreak(Person person, String scheduledDate, Date eventTime)
			throws ParseException {
		java.sql.Date sqlDt = DateUtil.parseShortDate(scheduledDate);
		Collection<WorkSchedule> cws;
		cws = getWorkSchedulesForDate(new Long(person.getId()), sqlDt);

		String statusStr = WorkScheduleStatusRef.Status.BRK.toString();
		for (WorkSchedule ws : cws) { // implements filter by locator
			ws.setStatusId(WorkScheduleStatusRef.getIdByCode(statusStr));
			update(ws);
		}

		logMWAccess(person.getUsername(), scheduledDate, eventTime,
				MWAccessTypeRef.Type.MWSB, null, 0d, 0d, 0);
	}

	public void endBreak(Person person, String scheduledDate, Date eventTime)
			throws ParseException {
		java.sql.Date sqlDt = DateUtil.parseShortDate(scheduledDate);
		Collection<WorkSchedule> cws;
		cws = getWorkSchedulesForDate(new Long(person.getId()), sqlDt);

		String statusStr = WorkScheduleStatusRef.Status.IP.toString();
		for (WorkSchedule ws : cws) { // implements filter by locator
			ws.setStatusId(WorkScheduleStatusRef.getIdByCode(statusStr));
			update(ws);
		}

		logMWAccess(person.getUsername(), scheduledDate, eventTime,
				MWAccessTypeRef.Type.MWEB, null, 0d, 0d, 0);
	}

	public void startTask(Long jobTaskId, Long jobTaskTimeId,
			String scheduledDate, Date eventTime) throws ParseException {
		JobTaskTime jtt = get(JobTaskTime.class, jobTaskTimeId);
		JobSchedule js = get(JobSchedule.class, jtt.getJobScheduleId());
		WorkSchedule ws = get(WorkSchedule.class, js.getWorkScheduleId());
		Person person = get(Person.class, ws.getPersonId());
		logMWAccess(person.getUsername(), scheduledDate, eventTime,
				MWAccessTypeRef.Type.MWST, jobTaskId, 0d, 0d, 0);

		Job job = get(Job.class, js.getJobId());
		if (job.getStartedDate() == null) {
			job.setStartedDate(eventTime);
			job.setSticky("Y");
		}
		job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.JIP));
		update(job);

		JobTask jobTask = jtt.getJobTask();
		jobTask.setTaskInProgress(1);
		update(jobTask);
	}

	public void stopTask(Long jobTaskId, Long jobTaskTimeId,
			String scheduledDate, Date eventTime) throws ParseException {
		JobTaskTime jtt = get(JobTaskTime.class, jobTaskTimeId);
		JobSchedule js = get(JobSchedule.class, jtt.getJobScheduleId());
		WorkSchedule ws = get(WorkSchedule.class, js.getWorkScheduleId());
		Person person = get(Person.class, ws.getPersonId());
		String userName = person.getUsername();

		Timestamp startDt = getLastStartTask(userName, jobTaskId);
		if (startDt != null) {
			logMWAccess(person.getUsername(), scheduledDate, eventTime,
					MWAccessTypeRef.Type.MWET, jobTaskId, 0d, 0d, 0);
			int timeInMillSec = getTimeSpentOnTaskFromLastST(userName,
					scheduledDate, jobTaskId,
					new java.sql.Date(startDt.getTime()));
			int timeInMin = Math.round(timeInMillSec / 60000.0f);
			logger.debug("Stop Task : " + jobTaskId + " time: " + timeInMin);
			updateJobTaskTime(jobTaskTimeId, timeInMin, true);
		} else {
			// should not happen
			int x = 0;
		}
	}

	public void doReplace(String userName, Long objectId, Long jobId,
			Long jobTaskId, Long jobTaskTimeId, Date eventTime)
			throws BusinessException {
		ObjectEntity object = get(ObjectEntity.class, objectId);
		ObjectDefinition objectDef = object.getObjectDefinition();
		Long presentInventory = objectDef.getPresentInventory();
		if (presentInventory <= 0) {
			throw new BusinessException(
					"No inventory available to replace. Please order inventory.");
		}

		// do replace
		presentInventory--;
		objectDef.setPresentInventory(presentInventory);
		update(objectDef);
		InventoryTrace it = new InventoryTrace();
		it.setInventory(-1);
		it.setInventoryDate(eventTime);
		it.setJobTaskTimeId(jobTaskTimeId);
		it.setObjectId(objectId);
		it.setObjectDefId(objectDef.getId());

		PolicySF psf = ServiceLocator.getPolicySFLocal();
		User user = psf.getUserByName(userName);
		if (user.getPerson() != null) {
			it.setPersonId(user.getPerson().getId());
		}
		it.setUserName(userName);
		create(it);
	}

	public ObjectEntity getObjectByJobTaskId(Long jobTaskId)
			throws DataException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tasks[0].id", jobTaskId);
		ObjectEntity objectEntity = (ObjectEntity) getUniqueData(getData(
				ObjectEntity.class, null, map));
		objectEntity.getObjectDefinition();
		return objectEntity;
	}

	public Long checkAnyTaskInProgress(Long jobTaskTimeId) {
		Long jobId = null;
		JobTaskTime jtt = get(JobTaskTime.class, jobTaskTimeId);
		JobSchedule js1 = get(JobSchedule.class, jtt.getJobScheduleId());
		long inProgressStatus = JobStatusRef
				.getIdByCode(JobStatusRef.Status.JIP);

		List<JobSchedule> jss = (List<JobSchedule>) getJobSchedulesByWSId(js1
				.getWorkScheduleId());
		if (jss != null) {
			Job job;
			for (JobSchedule js : jss) {
				job = get(Job.class, js.getJobId());
				if (job.getStatusId() != null
						&& inProgressStatus == job.getStatusId().longValue()) {
					jobId = job.getId();
					break;
				}
			}
		}
		return jobId;
	}

	private Collection<JobSchedule> getJobSchedulesByWSId(long workScheduleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("workScheduleId", workScheduleId);
		return getData(JobSchedule.class, null, map);
	}

	private Timestamp getLastStartTask(String userName, Long jobTaskId) {
		// (select max(tr.access_time) from mw_access_trace tr where
		// access_type_id = ST and job_task_id=jobTaskId)
		Collection<Timestamp> dates = manager
				.createQuery(
						"select max(tr1.accessTime) from MWAccessTrace tr1 where tr1.userName=:userName1"
								+ " and tr1.jobTaskId=:jobTaskId2 and tr1.accessTypeId="
								+ MWAccessTypeRef
										.getIdByCode(MWAccessTypeRef.Type.MWST))
				.setParameter("userName1", userName)
				.setParameter("jobTaskId2", jobTaskId).getResultList();

		Timestamp startDt = null;
		if (dates != null && !dates.isEmpty()) {
			if (dates.size() == 1) {
				startDt = dates.iterator().next();
			}
		}
		return startDt;
	}

	private Collection<MWAccessTrace> getMWAccessTrace(String userName,
			Date startDt) {
		return manager
				.createQuery(
						"select tr1 from MWAccessTrace tr1 where tr1.userName=:userName1"
								+ " and tr1.accessTime>=:accessTime2 order by tr1.accessTime")
				.setParameter("userName1", userName)
				.setParameter("accessTime2", startDt).getResultList();
	}

	private int getTimeSpentOnTaskFromLastST(String userName,
			String scheduledDate, Long jobTaskId, java.sql.Date startDt) {
		int time = 0;
		List<MWAccessTrace> traces = (List<MWAccessTrace>) getMWAccessTrace(
				userName, startDt);
		if (traces != null && !traces.isEmpty()) {
			Date start = null, end = null;
			boolean taskStarted = false;
			int taskStartedStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWST);
			int ssStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWSS);
			int esStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWES);
			int sbStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWSB);
			int ebStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWEB);
			int etStatus = MWAccessTypeRef
					.getIdByCode(MWAccessTypeRef.Type.MWET);
			for (MWAccessTrace trace : traces) {
				if (!taskStarted
						&& trace.getJobTaskId().longValue() == jobTaskId
								.longValue()
						&& taskStartedStatus == trace.getAccessTypeId()
								.intValue()) {
					taskStarted = true;
					start = trace.getAccessTime();
				} else if (taskStarted) {
					if (etStatus == trace.getAccessTypeId()) {
						end = trace.getAccessTime();
					} else if (esStatus == trace.getAccessTypeId()
							|| sbStatus == trace.getAccessTypeId()) {
						time += (trace.getAccessTime().getTime() - start
								.getTime());
					} else if (ssStatus == trace.getAccessTypeId()
							|| ebStatus == trace.getAccessTypeId()) {
						start = trace.getAccessTime();
					}
				}
			}

			time += (end.getTime() - start.getTime());
		}
		return time;
	}

	private Collection<MWAccessTrace> getMWAccessTrace(String userName,
			String scheduledDate, Long jobTaskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("schedule", scheduledDate);
		map.put("jobTaskId", jobTaskId);
		return getData(MWAccessTrace.class, null, map);
	}

	private Collection<MWAccessTrace> getMWAccessTrace(String userName,
			String scheduledDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("schedule", scheduledDate);
		return getData(MWAccessTrace.class, null, map);
	}
}