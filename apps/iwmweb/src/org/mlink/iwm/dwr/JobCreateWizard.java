package org.mlink.iwm.dwr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ObjectTask;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.JobsCriteria;
import org.mlink.iwm.dao.ObjectsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.rules.CreateJobHelper;
import org.mlink.iwm.rules.TaskPropertiesValidator;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andrei
 * Date: Jan 9, 2007
 */
public class JobCreateWizard implements ReturnCodes{
    /**
     * Get objects as candidates for job creation
     * @param criteria
     * @param offset
     * @param pageSize
     * @param orderBy
     * @param orderDirection
     * @return
     * @throws Exception
     */
    public ResponsePage getObjects(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        ObjectsCriteria cr = new ObjectsCriteria(criteria);
        //PaginationRequest request = new PaginationRequest("objectRef");
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectsForSelectDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectInstance.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * get tasks of the seleted object for selection
     * @return
     * @throws Exception
     */
    public ResponsePage getTasks() throws Exception {
        TasksCriteria cr = new TasksCriteria();
        ObjectEntity object = (ObjectEntity) SessionUtil.getAttribute("objectSelectedForJob");
        cr.setId(object.getId());
        PaginationRequest request = new PaginationRequest("description");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTasksDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectTask.class);
        SessionUtil.setAttribute("taskSelectedForJob",new ArrayList<ObjectTask>());
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * Get currently active jobs on the selected object
     * @return
     * @throws Exception
     */
    public ResponsePage getActiveJobs() throws Exception {
        ObjectEntity object = (ObjectEntity) SessionUtil.getAttribute("objectSelectedForJob");
        JobsCriteria cr = new JobsCriteria();
        cr.setObjectId(object.getId());
        cr.setJobCategory(Constants.ACTIVE_JOBS_CATEGORY);
        PaginationRequest request = new PaginationRequest(0,1000,"createdDate","DESC");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Job.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * Make object selection for job creation
     * @param objectId
     * @return
     * @throws Exception
     */
    public String selectObject(Long objectId) throws Exception{
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectEntity vo = isf.get(ObjectEntity.class, objectId);
        SessionUtil.setAttribute("objectSelectedForJob",vo);
        return ITEM_SAVED_OK_MSG;
    }

    /**
     * Select task to be assocoated with the job
     * @param taskId
     * @return
     * @throws Exception
     */
    public String selectTask(Long taskId) throws Exception{
        String rtn = ITEM_SAVED_OK_MSG;
        ObjectTasks dwrAction = new ObjectTasks();
        ObjectTask task = dwrAction.getItem(taskId);
        List <ObjectTask> selectedTasks = (List <ObjectTask>)SessionUtil.getAttribute("taskSelectedForJob");
        selectedTasks.add(task);
        try {
            TaskPropertiesValidator.extractSkillType(CopyUtils.copyProperties(Task.class,selectedTasks));
        } catch (BusinessException e) {
            rtn = e.getMessage();
            selectedTasks.remove(task);
        }
        return rtn;
    }

    /**
     * Disgard task previously selected for the job
     * @param taskId
     * @return
     * @throws Exception
     */
    public String unselectTask(Long taskId) throws Exception{
        String rtn = ITEM_SAVED_OK_MSG;
        List <ObjectTask> selectedTasks = (List<ObjectTask>)SessionUtil.getAttribute("taskSelectedForJob");
        for (Iterator iterator = selectedTasks.iterator(); iterator.hasNext();) {
            ObjectTask task =  (ObjectTask)iterator.next();
            if(String.valueOf(taskId).equals(task.getId())) iterator.remove();
        }
        return rtn;
    }

    /**
     * Build job prototype to present the user for confirmation
     * @param projectId
     * @return
     * @throws Exception
     */
    public String buildJobPrototype(Long projectId) throws Exception{
        String rtn = ITEM_SAVED_OK_MSG;
        //List selectedTasks = (List)CopyUtils.copyProperties(Task.class,(List)SessionUtil.getAttribute("taskSelectedForJob"));
        
        List<Task> selectedTasks = new ArrayList<Task>();
        Task task = new Task();
        List<ObjectTask> taskSelectedForJob = (List<ObjectTask>)SessionUtil.getAttribute("taskSelectedForJob");
        for(ObjectTask objectMask: taskSelectedForJob){
        	CopyUtils.copyProperties(task, objectMask); 
        	
        	//task has a taskDescription, while taskMap has just 'description' - as such copyutils misses it
        	//set it here. --chris
        	task.setTaskDescription(objectMask.getDescription());
        	
        	task.setObject(new ObjectEntity(Long.parseLong(objectMask.getObjectId())));
        	/*String taskDefinitionIdStr = (String)taskMap.getTaskDefinitionId();
        	if(taskDefinitionIdStr!=null && taskDefinitionIdStr.length()>0){
        		task.setTaskDefinition(new TaskDefinition(Long.parseLong(taskDefinitionIdStr)));
        	}*/
        	  	
        	
        	String taskGroupIdStr = objectMask.getGroupId();
        	if(taskGroupIdStr!=null && taskGroupIdStr.length()>0){
        		task.setTaskGroup(new TaskGroup(Long.parseLong(taskGroupIdStr)));
        	}
        	selectedTasks.add(task);
        }
        
        try {
            Job vo;
            if(projectId==null)
                vo = CreateJobHelper.buildJobPrototype(null, selectedTasks);
            else
                vo = CreateJobHelper.buildProjectJobPrototype(null, selectedTasks,projectId);
            String createdBy = "super";
            if (WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName()!=null){
            	createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
            }
            
			vo.setCreatedBy(createdBy);
            SessionUtil.setAttribute("jobPrototype",vo);
        } catch (BusinessException e) {
            rtn = e.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.Job getPrototype() throws Exception {
    	org.mlink.iwm.bean.Job form = null;
    	Job job = (Job)SessionUtil.getAttribute("jobPrototype"); 
    	if(job!=null){
    		form = new org.mlink.iwm.bean.Job();
    		CopyUtils.copyProperties(form, job);
    		if(job.getProject()!=null){
    			form.setProjectId(Long.toString(job.getProject().getId()));
    		}
    	}
        return form;
    }

    /**
     * Last step: persist the job the job
     * @param bean
     * @return
     * @throws Exception
     */
    public String saveItem(HashMap bean) throws Exception {
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Job vo = new Job();
        
        CopyUtils.copyProperties(vo,bean);
        vo.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        
        if (bean.get("organizationId").equals("") || ("-1").equals(bean.get("organizationId"))){
        	vo.setOrganizationId(null);
        }
        
        String rtn = ITEM_SAVED_OK_MSG;
        try{
            //List<Task> selectedTasks = (List<Task>)CopyUtils.copyProperties(Task.class,(List)SessionUtil.getAttribute("taskSelectedForJob"));
        	List<Task> selectedTasks = new ArrayList<Task>();
        	List<ObjectTask> objectTasks = (List)SessionUtil.getAttribute("taskSelectedForJob");
        	Task task;
        	if(objectTasks!=null && objectTasks.size()>0){
        		for(ObjectTask objectTask : objectTasks) {
        			task = csf.get(Task.class, Long.parseLong(objectTask.getId()));
        			selectedTasks.add(task);
        		}
        	}
        	String createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
            ObjectEntity object = (ObjectEntity) SessionUtil.getAttribute("objectSelectedForJob");
            vo.setObjectId(object.getId());
            Job prototype = (Job) SessionUtil.getAttribute("jobPrototype");
            /**Long jobId = csf.createJob(createdBy,selectedTasks,prototype.getProjectId());
            Job vo0 = csf.getJob(jobId);
            CopyUtils.copyProperties(vo0,vo); //incorporate updates from ui and job created off tasks
            vo0.setId(jobId);  //make sure jobId is not lost during copyProperties
            csf.updateJob(vo0);*/
            Long projectId = null;
            
            if (bean.get("projectId")!=null && !bean.get("projectId").equals("")){
            	projectId = Long.valueOf((String)bean.get("projectId"));
            }
            
            //Newly created jobs are never sticky, and sticky can't be null or it breaks the agents. Setting it here. --chris
            vo.setSticky("N");
            
            csf.createJob(createdBy, selectedTasks, projectId, vo);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

}
