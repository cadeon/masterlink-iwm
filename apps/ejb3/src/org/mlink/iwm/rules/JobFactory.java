package org.mlink.iwm.rules;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.ObjectTypeRef;
import org.mlink.iwm.lookup.ScheduleResponsibilityRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Constants;

/**
 * All job creation request should be processed by calling to JobFactory
 */
public class JobFactory {
	private static final Logger logger = Logger.getLogger(JobFactory.class);

	/**
	 * 
	 * @param creator
	 * @param tasks
	 *            Task instances
	 * @param projectId
	 *            may be null
	 * @return
	 * @throws IWMException
	 * @throws BusinessException
	 */
	public static Job createJob(Job job, String creator, List<Task> tasks,
			Long projectId) throws BusinessException {
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		ControlSF csf = ServiceLocator.getControlSFLocal( );
		logger.debug(creator + " about to create a Job ");
		try {
			// ensure that tasks exist and have full set of data
			/*for (Object task2 : tasks) {
				Task task = (Task) task2;
				tasks1.add(policy.get(Task.class, task.getId()));
			}*/

			// builds job prototype, sets job properties based on property
			// values of the supplied tasks
			if (projectId == null) {
				job = CreateJobHelper.buildJobPrototype(job, tasks);
			} else {
				job = CreateJobHelper.buildProjectJobPrototype(job, tasks,
						projectId);

			}
			job.setCreatedBy(creator);

			/**
			 * moved to Job.ejbCreate() andrei 11/26/07 java.sql.Date date = new
			 * java.sql.Date(System.currentTimeMillis()); java.sql.Timestamp
			 * time = new java.sql.Timestamp(System.currentTimeMillis());
			 * vo.setCreatedDate(time); vo.setEarliestStart(date); Calendar cal
			 * = Calendar.getInstance(); cal.setTime(date);
			 * cal.add(Calendar.MONTH,6); vo.setLatestStart(new
			 * java.sql.Date(cal.getTimeInMillis()));
			 */

			/*
			 * if(projectId==null){
			 * vo.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.INS));
			 * }else{ Project project = ProjectAccess.getProject(projectId);
			 * if(ProjectStatusRef.isFinalState(project.getStatusId())){ throw
			 * new
			 * BusinessException(" Project is in a final state. Job cannot be added"
			 * ); }else
			 * if(ProjectStatusRef.PREPARING.equals(ProjectStatusRef.getLabel
			 * (project.getStatusId()))){
			 * vo.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.NYA)); }else
			 * if
			 * (ProjectStatusRef.STARTED.equals(ProjectStatusRef.getLabel(project
			 * .getStatusId()))){
			 * vo.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.INS)); } }
			 */

			ObjectEntity object = psf.get(ObjectEntity.class, job
					.getObjectId());
			job.setLocatorId(object.getLocatorId());

			for (Task taskVo : tasks) {
				if (taskVo.getArchivedDate() == null) { // andrei: 06/03/06
					csf.addTask(job, taskVo);
				}
			}

			if (projectId != null) { // it is a project job
				Project project = psf.get(Project.class, projectId);

				job.setSequenceLevel(1);
				job.setProject(project);
			}
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IWMException(e);
		}

		psf.create(job);
		return job;
	}

	/**
	 * Create job of off TenantRequest Create area object if required
	 * 
	 * @return jobId
	 * @throws IWMException
	 */

	public static Job createJob(TenantRequest tr, ObjectDefinition od)
			throws BusinessException {
		PolicySF policy = ServiceLocator.getPolicySFLocal();
		ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
		Job job = null;
		try {
			Long locatorId = tr.getLocatorId();
			// Long areaId =
			// Long.valueOf(Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID));
			// ObjectDefinition od =
			// ObjectDefinitionAccess.getObjectDefinition(areaId);
			Collection<Task> tasks = null;

			Collection<ObjectEntity> col = policy.findByClassAndLocator(od
					.getClassId(), locatorId);
			// there should be one or none
			ObjectEntity object;
			if (col.iterator().hasNext()) {
				object = (ObjectEntity) col.iterator().next();
			} else { // then object must be created
				object = new ObjectEntity();
				object.setTag(String
						.valueOf((new Double(100000 * Math.random()))
								.intValue()));
				object.setLocatorId(locatorId);
				object.setClassId(od.getClassId());
				object.setObjectTypeId(ObjectTypeRef
						.getIdByCode(ObjectTypeRef.FACILITIES));
				object.setRunHours(null);
				object.setActive(Constants.YES);
				object.setObjectRef(org.mlink.iwm.lookup.TargetClassRef.getAbbr(object.getClassId())+"."+object.getTag());

				//ObjectDefinition od = isf.get(ObjectDefinition.class, od.getId());
				object.setObjectDefinition(od);
				createObjectEntity(object, od);
				
				tasks = isf.getTasks(object);

				// activate tasks
				if (tasks != null) {
					for (Task task : tasks) {
						task.setStartDate(new Date(System.currentTimeMillis()));
						task.setActive(Constants.YES);
						// set schedule responsibility to SYSTEM for newly
						// activated tasks
						task.setScheduleResponsibilityId(ScheduleResponsibilityRef
										.getIdByCode(ScheduleResponsibilityRef.SYSTEM));
					}
				}
			}

			// Now find which task is selected by tenant as the problem
			Long taskDefId = tr.getProblemId();
			if(tasks==null){
				tasks = isf.getTasks(object);
			}
			Task selectedProblem;
			boolean matchFound = false;
			if (tasks != null) {
				for (Object task1 : tasks) {
					selectedProblem = (Task) task1;
					if (taskDefId.equals(selectedProblem.getTaskDefinition()
							.getId())) {
						List<Task> selectedtasks = new ArrayList<Task>();
						selectedtasks.add(selectedProblem);
						job = createJob(null, "tenant", selectedtasks, null);
						// job.setNote(tr.getNote()); Note in job is a manager
						// comment
						job.setSticky("N");
						job.setScheduleResponsibilityId(selectedProblem
								.getScheduleResponsibilityId());
						job.setStatusId(JobStatusRef
								.getIdByCode(JobStatusRef.Status.INS));
						java.sql.Date date = new Date(System
								.currentTimeMillis()); // +
						// 1000L*60*60*24*60);
						// //+60days
						job.setEarliestStart(date);
						job.setTenantRequest(tr);
						tr.setJobId(job.getId());
						tr.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));

						policy.update(job);
						policy.update(tr);
						matchFound = true;
						break;
					}
				}
			}
			if (!matchFound) {
				throw new IWMException(
						"Area Object is missing a required task!");
			}
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IWMException(e);
		}

		return job;
	}
	
	public static void createObjectEntity(ObjectEntity object, ObjectDefinition od) {
		ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
		Collection<TaskGroupDefinition> defs;
        //setObjectDefinition(od);    //cmr setXXX should be called in postCreate not in create, see ejb specs

        defs = od.getTaskGroupDefs();
        for (TaskGroupDefinition tad: defs) {
            inherit(object, tad);
        }

        Collection<TaskDefinition> taskDefs = od.getTaskDefs();
        for (TaskDefinition td: taskDefs) {
             inherit(object, td);
        }

        Collection<ObjectDataDefinition> dataDefs = od.getDataDefs();
        for (ObjectDataDefinition bean: dataDefs) {
            inherit(object, bean);
        }

        object.setHasCustomData(Constants.CUSTOMIZED_NO);
        object.setHasCustomTask(Constants.CUSTOMIZED_NO);
        object.setHasCustomTaskGroup(Constants.CUSTOMIZED_NO);
        
        isf.create(object);
    }
	
	/**
     * Creates ObjectData off ObjectDataDefinition and adds to current ObjectEntity
     * @param odd          ObjectDataDefinition
     * @
     */
    public static void inherit(ObjectEntity object, ObjectDataDefinition odd) {
    	ObjectData oData = new ObjectData(odd);
    	oData.setObject(object);
        Collection<ObjectData> oDatums = object.getDatums();
        if(oDatums == null){
        	oDatums = new HashSet<ObjectData>();
        	object.setDatums(oDatums);
        }
        oDatums.add(oData);
    }

    /**
     * Creates TaskGroup off TaskGroupDefinition and adds to current ObjectEntity
     * @param tgd
     * @
     */
    public static void inherit(ObjectEntity object, TaskGroupDefinition tgd) {
        TaskGroup group = new TaskGroup(tgd);
        group.setObject(object);
        Collection<TaskGroup> taskGroups = object.getTaskGroups();
        if(taskGroups == null){
        	taskGroups = new HashSet<TaskGroup>();
        	object.setTaskGroups(taskGroups);
        }
        taskGroups.add(group);
    }

    /**
     * Creates Task off TaskDefinition and adds to current ObjectEntity
     * @param td
     * @
     */
    public static void inherit(ObjectEntity object, TaskDefinition td) {
        Task task = new Task(td);
        task.setObject(object);
        List<Task> tasks = object.getTasks();
        if(tasks == null){
        	tasks = new ArrayList<Task>();
        	object.setTasks(tasks);
        }
        tasks.add(task);
        task.synchronizeGroupMembership(object.getTaskGroups());
    }
}
