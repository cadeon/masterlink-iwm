/*-----------------------------------------------------------------------------------
	File: ImplementationSFRemote.java
	Package: org.mlink.iwm.session
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.session;
import java.util.Collection;

import javax.ejb.RemoveException;

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

public interface ImplementationSF extends CommonSF {
	public Collection<ObjectData> getObjectDatas(Long objectId, Integer dataTypeId);

    public Collection<TaskGroup> getTaskGroups(Long objectId);

    /**
     * Get all ungrouped task defs for Object  given by objectId
     * @param objectId
     */
    public Collection<Task> getObjectTasksUngrouped(Long  objectId);

    //Collection<Task> getTasksInSequence(Long sequenceId) throws , FinderException;

    /**
     * Flips  isDisplay flag
     * @param dataId
     */
    public void updateObjectDataDisplayStatus(Long dataId) ;

    /**
     * Update job sequence in job collection
     * @param jobs
     * @throws BusinessException
     */
    public void updateJobSequences(Collection<Job> jobs) throws  BusinessException;   

    /**
     * Get Object by Tag name
     * @param name
     * @return  ObjectEntity
     * @ 
     */
    public ObjectEntity getObjectByReference(String name) throws DataException;
    
    /**
     * Delete Sequence of Sequence entity
     * @param sequenceId
     * @throws RemoveException
     */
    public void removeProjectStencil(Long  sequenceId);
    
    /**
     * Delete ObjectData of ObjectData entity
     * @param dataId
     * @throws BusinessException
     */
    public void removeObjectData(Long  dataId) throws BusinessException;

    /**
     * Update ObjectData entity. vo.getDataId() will be used to identify the ObjectData instance to update
     * @param vo ObjectData
     * @
     */
    public void updateObjectData (ObjectData vo) ;

    /**
     * Create object data for
     * @param vo
     * @return
     * @
     * @
     */
    public Long createObjectData(ObjectData vo );
    
    /**
     * Delete ObjectEntity of ObjectEntity entity
     * @param objectId
     */
    public void removeObject(Long  objectId) throws BusinessException;

    /**
     * Update ObjectEntity entity. vo.getObjectId() will be used to identify the ObjectEntity instance to update
     * if object is being deactivated deactivate all tasks. Reverse procedure for task activation when object is deactivated does not exist. Manual activation is requred for each task ia UI
     * See Task Activation rules in TaskBean
     * @param vo ObjectEntity
     */
    public void updateObject (ObjectEntity bean);

    /**
     * Delete Task of Task entity
     * @param taskId
     */
    public void removeTask(Long  taskId) throws BusinessException;

    /**
     * Update Task entity. vo.getTaskId() will be used to identify the Task instance to update
     * @param vo Task
     * @throws BusinessException
     */
    public void updateTask (Task bean) throws BusinessException;

    /**
     * Create Task entity
     * @param vo
     * @return
     * @throws BusinessException
     */
    public Long createTask(Task bean ) throws  BusinessException;

    /**
     * @deprecated Task status is defined by presence of start date. this is effective since Feb2005 bootcamp per Douglas requirement
     * updates task status. Status can be active or not active
     * calling this metod will revert the status
     * @param taskId
     */
    public void updateTaskStatus(Long taskId) throws BusinessException;

    /**
     * Update actions for a task
     * @param actions    Collection of Action
     * @
     * @
     */
    public void updateTaskActions(Collection<Action> actions); 
    
    /**
     * Delete Action of Action entity
     * @param actionId
     * @throws BusinessException
     */
    public void removeAction(Long  actionId) throws BusinessException;

    /**
     * Update Action entity. vo.getActionId() will be used to identify the Action instance to update
     * @param vo Action
     * @
     */
    public void updateAction (Action vo) ;
    
    public void createAction (Action action);
    /**
     * Remove TaskGroup
     * @param taskGroupId
     */
    public void removeTaskGroup(Long  taskGroupId) throws BusinessException;

    /**
     * Get TaskGroup of TaskGroup entity
     * @param taskGroupId
     * @return TaskGroup
     * @
     */
    public TaskGroup getTaskGroup(Long  taskGroupId) ;

    /**
     * Update TaskGroup entity. vo.getTaskGroupId() will be used to identify the TaskGroup instance to update
     * @param vo TaskGroup
     */
    public void updateTaskGroup (TaskGroup bean) throws  BusinessException;

    public Long createTaskGroup (TaskGroup bean) throws BusinessException;

   /**
     * Create TaskSequence (or in other words Ads Task to ProjectStencil)
     * @param sequenceId
     * @param taskId
     * @
     */
    public Long addTaskToSequence(Long sequenceId, Long taskId) ;

    /**
     * Update task sequences in task collection (project stencil)
     * @param tasks
     * @throws BusinessException
     */
    public void updateTaskSequences(Collection<TaskSequence> tasks) throws  BusinessException;
    
    public ObjectDataDefinition getDataDefinition(ObjectData objectData);
	public ObjectEntity getObject(ObjectData objectData);
	
	public Collection<TaskGroup> getTaskGroups(ObjectEntity oe);	
	public Collection<Task> getTasks(ObjectEntity oe);	
	public ObjectDefinition getObjectDefinition(ObjectEntity oe);	
	public Collection<ObjectData> getDatums(ObjectEntity oe);
	
	public TaskGroupDefinition getGroupDefinition(TaskGroup tg);
	public Collection<Task> getTasks(TaskGroup tg);	
	public ObjectEntity getObject(TaskGroup tg);
	
	public Sequence getSequence(TaskSequence ts);
	public Task getTask(TaskSequence ts);
	
	public long createObjectEntity(ObjectEntity object);
	
	public Collection<ObjectEntity> getChildren(ObjectEntity object);
	public void updateObjectTasksOrg(Long objectId, Long orgId);
}