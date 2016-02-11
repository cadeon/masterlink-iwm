package org.mlink.iwm.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Sequence;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.entity3.TaskSequence;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.rules.CustomizationVisitor;
import org.mlink.iwm.rules.DeletionVisitor;
import org.mlink.iwm.rules.JobFactory;
import org.mlink.iwm.rules.TaskPropertiesValidator;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andreipovodyrev Date: Mar 21, 2009
 */
@Interceptors(SessionInterceptor.class)
@Stateless(name = "ImplementationSFBean")
@RemoteBinding(jndiBinding = "ImplementationSFRemote")
@LocalBinding(jndiBinding = "ImplementationSFLocal")
public class ImplementationSFBean  extends CommonSFBean implements ImplementationSFLocal, ImplementationSFRemote {
	private static final Logger logger = Logger.getLogger(ImplementationSFBean.class);

	@PersistenceContext(unitName = "iwm_dpc")
	private EntityManager manager;

	/**
    *
    * @param objectId
    * @param dataTypeId
    * @return collection
    */
   public Collection<ObjectData> getObjectDatas(Long objectId, Integer dataTypeId){
	   Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
	   fieldNameValueMap.put("objectId", objectId);
	   fieldNameValueMap.put("dataTypeId", dataTypeId);
	   //select object(p) from ObjectData as p where p.objectId = ?1 and p.dataTypeId = ?2;
	   return getData(ObjectData.class, null, fieldNameValueMap);
   }
   
   public Collection<TaskGroup> getTaskGroups(Long objectId){
       ObjectEntity bean = get(ObjectEntity.class, objectId);
       return bean.getTaskGroups();
   }

   /**
    * Get all ungrouped task defs for Object  given by objectId
    * @param objectId
    * @return Collection
    */
   public Collection<Task> getObjectTasksUngrouped(Long  objectId){
       //select object(p) from Task as p where p.objectId = ?1  and p.groupId is null
	   Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
	   fieldNameValueMap.put("objectId", objectId);
	   return getData(ObjectData.class, "groupId is null", fieldNameValueMap);
   }

    //Collection<Task> getTasksInSequence(Long sequenceId) throws , FinderException;

   /**
    * Flips  isDisplay flag
    * @param dataId
    * @
    */
   public void updateObjectDataDisplayStatus(Long dataId){
       ObjectData bean = get(ObjectData.class, dataId);
       if(Constants.YES.equals(bean.getIsDisplay())){
           bean.setIsDisplay(Constants.NO);
       }else{
           bean.setIsDisplay(Constants.YES);
       }
       update(bean);
   	}
   
    /**
     * Get Object by Tag name
     * @param name
     * @return  ObjectEntity
     * @ 
     */
    public ObjectEntity getObjectByReference(String  name) throws DataException{
    	Collection<ObjectEntity> col = getData(ObjectEntity.class, "objectRef", name);
    	return getUniqueData(col);
    	//select object(o) from ObjectEntity as o where o.objectRef = ?1
	}

    /**
	    * Update job sequence in job collection
	    * @param jobs
	    * @throws BusinessException
	*/
    public void updateJobSequences(Collection<Job> jobs)
			throws BusinessException {
    	for (Job job : jobs) {
	           if (job.getSequenceLevel() == null)
	               throw new BusinessException("sequence must be defined for job " + job.getId());
	           update(job);
	       }	
	}	
    //////
   
   /**
     * Delete Sequence of Sequence entity
     * @param sequenceId
     * @throws RemoveException
     */
    public void removeProjectStencil(Long  sequenceId) {
        //SequenceAccess.getHome().remove( sequenceId);
        Sequence bean = get(Sequence.class, sequenceId);
        bean.setArchivedDate(new java.sql.Date(System.currentTimeMillis()));
    }

    /**
     * Delete ObjectData of ObjectData entity
     * @param dataId
     * @throws BusinessException
     */
    public void removeObjectData(Long  dataId) throws BusinessException{
        try{
            ObjectData bean = get(ObjectData.class, dataId);
            DeletionVisitor.delete(bean);
         }catch(BusinessException be){
            throw be;
         }catch(Exception e){
             throw new IWMException(e);
         }
    }

    /**
     * Update ObjectData entity. vo.getDataId() will be used to identify the ObjectData instance to update
     * @param vo ObjectData
     * @
     */
    public void updateObjectData (ObjectData vo){
        logger.debug("Updating Object Data " + vo.getId());
        update(vo);

        //if(Constants.CUSTOMIZED_NO.equals(bean.getCustom())){  give them option to de-customize object data by checking the match template data
            CustomizationVisitor.visit(vo);
            update(vo);
            ObjectEntity object = get(ObjectEntity.class, vo.getObject().getId());
            object.updateCustom();
            update(object);
        //}
    }

    /**
     * Create object data for
     * @param vo
     * @return
     * @throws CreateException
     * @
     */
    public Long createObjectData(ObjectData vo ){
        //logger.debug("Creating Object Data " + vo);
        vo.setCustom(Constants.CUSTOMIZED_YES);
        vo.setIsDisplay(Constants.YES);
        create(vo);
        ObjectEntity object = get(ObjectEntity.class, vo.getObject().getId());
        object.updateCustom();
        update(object);
        return vo.getObject().getId();
    }
    
    public long createObjectEntity(ObjectEntity object)	{
    	ObjectDefinition od = object.getObjectDefinition();
    	ObjectDefinition od1 = get(ObjectDefinition.class, od.getId());
		if(od1.getDataDefs()!=null)
			od1.getDataDefs().size();
    	if(od1.getObjects()!=null)
    		od1.getObjects().size();
    	if(od1.getTaskDefs()!=null)
    		od1.getTaskDefs().size();
    	if(od1.getTaskGroupDefs()!=null)
    		od1.getTaskGroupDefs().size();
    	object.setObjectDefinition(od1);
		JobFactory.createObjectEntity(object, od1);
		return object.getId();
	}

    /**
     * Delete ObjectEntity of ObjectEntity entity
     * @param objectId
     */
    public void removeObject(Long  objectId) throws BusinessException{
        try{
            ObjectEntity object = get(ObjectEntity.class, objectId);
            DeletionVisitor.delete(object);
        }catch(BusinessException be){
            throw be;
        }catch(Exception e){
            throw new IWMException(e);
        }
    }

    /**
     * Update ObjectEntity entity. vo.getObjectId() will be used to identify the ObjectEntity instance to update
     * if object is being deactivated deactivate all tasks. Reverse procedure for task activation when object is deactivated does not exist. Manual activation is requred for each task ia UI
     * See Task Activation rules in TaskBean
     * @param vo ObjectEntity
     */
    public void updateObject (ObjectEntity bean){
        logger.debug("Updating Object " + bean.getId());
        
        if(!EqualsUtils.areEqual(bean.getActive(),bean.getActive()) && Constants.STATUS_NOT_ACTIVE.equals(bean.getActive())){
            List<Task> tasks = bean.getTasks();
            for (Task task : tasks) {
                task.setActive(Constants.STATUS_NOT_ACTIVE);
                task.setStartDate(null);
                update(task);
            }
        }

        //ObjectEntityAccess.merge(bean);
        Long orgIfBeforeUpdate = bean.getOrganizationId();

        if(!EqualsUtils.areEqual(orgIfBeforeUpdate,bean.getOrganizationId())){
            List<Task> tasks = bean.getTasks();
            for (Task task : tasks) {
                task.setOrganizationId(bean.getOrganizationId());
                update(task);
            }
        }
        update(bean);
    }

    /**
     * Delete Task of Task entity
     * @param taskId
     */
    public void removeTask(Long  taskId) throws BusinessException{
        try{
            Task bean = get(Task.class, taskId);
            DeletionVisitor.delete(bean);
         }catch(BusinessException be){
            throw be;
         }catch(Exception e){
             throw new IWMException(e);
         }
    }

    /**
     * Update Task entity. vo.getTaskId() will be used to identify the Task instance to update
     * @param vo Task
     * @throws BusinessException
     */
    public void updateTask (Task bean) throws BusinessException{
        ObjectEntity od;
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        policy.validateTask(bean);
        logger.debug("Updating Task " + bean.getId());
        od = get(ObjectEntity.class, bean.getId());
        
        CustomizationVisitor.visit(bean);
        od.updateCustom();
        update(bean);
        update(od);
    }

    /**
     * Create Task entity
     * @param vo
     * @return
     * @throws BusinessException
     */
    public Long createTask(Task bean ) throws  BusinessException{
        ObjectEntity object;
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        ControlSF csf = ServiceLocator.getControlSFLocal();
        policy.validateTask(bean);

        logger.debug("Creating Task " + bean.getTaskDescription());
        try{
        	object = get(ObjectEntity.class, bean.getObject().getId());
            bean.setObject(object);
            // add default actions
            csf.addDefaultActions(bean);
        }catch(Exception e){
            throw  new IWMException(e);
        }

        bean.setCustom(Constants.YES);
        bean.setPlan(object.getObjectDefinition().getPlan());
        create(bean);
        
        object.setHasCustomTask(Constants.YES);
        update(object);
        return bean.getId();
    }

    /**
     * @deprecated Task status is defined by presence of start date. this is effective since Feb2005 bootcamp per Douglas requirement
     * updates task status. Status can be active or not active
     * calling this metod will revert the status
     * @param taskId
     */
    public void updateTaskStatus(Long taskId) throws BusinessException{
        Task task = get(Task.class, taskId);
        Integer curStatus = task.getActive();
        if(curStatus==null || Constants.STATUS_NOT_ACTIVE.equals(curStatus)){
            if(task.getStartDate()==null){
                throw new BusinessException("Start date must be defined for a task to be activated!");
            }
            if(task.getActions()==null || task.getActions().size()==0){
                throw new BusinessException("Actions must be defined for task to be activated!");
            }
            task.setActive(Constants.STATUS_ACTIVE);
        } else{
            task.setStartDate(null);
            task.setActive(Constants.STATUS_NOT_ACTIVE);
        }
        update(task);
    }

    /**
     * Update actions for a task
     * @param actions    Collection of Action
     * @
     * @throws CreateException
     */
    public void updateTaskActions(Collection<Action> actions) {
    	logger.debug("Updating Object Task Actions ");
        for (Action aVo : actions) {
            update(aVo);
        }
    }

    /**
     * Delete Action of Action entity
     * @param actionId
     * @throws BusinessException
     */
    public void removeAction(Long  actionId) throws BusinessException{
        try{
            Action bean = get(Action.class, actionId);
            DeletionVisitor.delete(bean);
         }catch(BusinessException be){
            throw be;
         }catch(Exception e){
             throw new IWMException(e);
         }
    }

    /**
     * Update Action entity. vo.getActionId() will be used to identify the Action instance to update
     * @param vo Action
     * @
     */
    public void updateAction (Action vo) {
        logger.debug("Updating Action " + vo.getId());
        update(vo);
        CustomizationVisitor.visit(vo);
    }

    public void createAction (Action action) {
    	logger.debug("Creating Action " + action.getVerb());
        Task task = get(Task.class, action.getId());
        action.setCustom(Constants.CUSTOMIZED_YES);
        action.setSequence((task.getActions()!=null)?task.getActions().size():0);
        action.setTask(task);
        create(action);
        CustomizationVisitor.visit(action);
    }

    /**
     * Remove TaskGroup
     * @param taskGroupId
     */
    public void removeTaskGroup(Long  taskGroupId) throws BusinessException{
        try{
            TaskGroup tg = get(TaskGroup.class, taskGroupId);
            DeletionVisitor.delete(tg);
         }catch(BusinessException be){
            throw be;
         }catch(Exception e){
             throw new IWMException(e);
         }
    }

    /**
     * Get TaskGroup of TaskGroup entity
     * @param taskGroupId
     * @return TaskGroup
     * @
     */
    public TaskGroup getTaskGroup(Long  taskGroupId) {
        TaskGroup bean = get(TaskGroup.class, taskGroupId);
        bean.getTasks();
        return bean;
    }

    /**
     * Update TaskGroup entity. vo.getTaskGroupId() will be used to identify the TaskGroup instance to update
     * @param vo TaskGroup
     */
    public void updateTaskGroup (TaskGroup bean) throws  BusinessException{
        /*TaskGroup bean = TaskGroupAccess.getTaskGroup(vo.getId());
        try{
            TaskGroupAccess.consume(bean,vo);
        }catch(BusinessException be){
            throwBusinessException(be);
        }*/
        logger.debug("Updating Task Group " + bean.getId());
        //setTaskGroupTasks(bean);
        if(bean.getDescription()==null) throwBusinessException("Description required!");
        update(bean);
        CustomizationVisitor.visit(bean);
    }

    private void setTaskGroupTasks(TaskGroup bean) throws BusinessException{
        if(bean.getDescription()==null) throwBusinessException("Description required!");

        //business rule check for the same skillTypeId
        Integer skillTypeId = TaskPropertiesValidator.extractSkillType(bean.getTasks());
        bean.setSkillTypeId(skillTypeId);
        //bean.setDescription(bean.getDescription());
        update(bean);
    }

    public Long createTaskGroup (TaskGroup bean) throws BusinessException{
        logger.debug("Creating Task Group " + bean.getDescription());
        ObjectEntity object = get(ObjectEntity.class, bean.getObject().getId());
        bean.setCustom(Constants.YES);
        bean.setObject(object);
        object.updateCustom();
        create(bean);
        update(object);
        /*TaskGroupAccess.set(bean,vo);
        try{
            TaskGroupAccess.consume(bean,vo);
        }catch(BusinessException be){
            throwBusinessException(be);
        } */
        //setTaskGroupTasks(bean);
        return bean.getId();
    }

   /**
     * Create TaskSequence (or in other words Ads Task to ProjectStencil)
     * @param sequenceId
     * @param taskId
     * @throws CreateException
     */
    public Long addTaskToSequence(Long sequenceId, Long taskId) {
        Sequence sequence = get(Sequence.class, sequenceId);
        Task task = get(Task.class, taskId);
        TaskSequence ts = new TaskSequence(sequence,task,1);
        create(ts);
        return ts.getId();
    }

    /**
     * Update task sequences in task collection (project stencil)
     * @param taskSeqs
     * @throws BusinessException
     */
    public void updateTaskSequences(Collection<TaskSequence> taskSeqs) throws  BusinessException{
        for (TaskSequence item : taskSeqs) {
            if (item.getSequenceLevel() == null)
                throwBusinessException("sequence must be defined for job " + item.getId());
            update(item);
        }
    }
    
    public ObjectDataDefinition getDataDefinition(ObjectData objectData) {
		return objectData.getObjectDataDef();
	}

	public ObjectEntity getObject(ObjectData objectData) {
		return objectData.getObject();
	}
	
	public Collection<TaskGroup> getTaskGroups(ObjectEntity oe) {
		return oe.getTaskGroups();
	}
	
	public List<Task> getTasks(ObjectEntity oe) {
		return oe.getTasks();
	}
	
	public ObjectDefinition getObjectDefinition(ObjectEntity oe) {
		return oe.getObjectDefinition();
	}
	
	public Collection<ObjectData> getDatums(ObjectEntity oe) {
		return oe.getDatums();
	}
	
	public TaskGroupDefinition getGroupDefinition(TaskGroup tg) {
		return tg.getTaskGroupDef();
	}
	
	public Collection<Task> getTasks(TaskGroup tg) {
		return tg.getTasks();
	}
	
	public ObjectEntity getObject(TaskGroup tg) {
		return tg.getObject();
	}
	
	public Sequence getSequence(TaskSequence ts) {
		return ts.getSequence();
	}
	
	public Task getTask(TaskSequence ts) {
		return ts.getTask();
	}
	
	public Collection<ObjectEntity> getChildren(ObjectEntity object){
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
	   fieldNameValueMap.put("parentObjectId", object.getId());
	   return getData(ObjectEntity.class, null, fieldNameValueMap);
	}
	
	/**
	 * Update Organization requirements on all tasks associated with an Object.
	 * This is used when an Organization is set on an object... all tasks need to match. 
	 * 
	 * @param objectId, orgId
	 */
	
	public void updateObjectTasksOrg(Long objectId, Long orgId) {
		ObjectEntity obj;
		obj = get(ObjectEntity.class, objectId);
		List<Task> tasks = obj.getTasks();
		Iterator<Task> iterator = tasks.iterator();
		while (iterator.hasNext()) {
			Task task = iterator.next();
			task.setOrganizationId(orgId);
		}
	}
}