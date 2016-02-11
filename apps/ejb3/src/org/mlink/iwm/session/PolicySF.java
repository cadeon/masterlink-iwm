package org.mlink.iwm.session;

import java.rmi.RemoteException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.LocatorData;
import org.mlink.iwm.entity3.ObjectClassification;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.Role;
import org.mlink.iwm.entity3.Sequence;
import org.mlink.iwm.entity3.Skill;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.entity3.TaskSequence;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.rules.MigratedDataCleaner;

/**
 * User: andreipovodyrev
 * Date: Mar 5, 2009
 */
public interface PolicySF extends CommonSF{
	public Organization getOrganizationByName(String name) ;
	public Person getPersonByName(String name) ;
	public void updateUserPassword(Long userId, String oldPassword,
			String newPassword) throws BusinessException;
    public Collection<Organization> getOrganizationsByParentId(Long parentId);
    public void removeOrganization(Organization organization) throws BusinessException, SQLException, FinderException;
    public void validateUsername(long personId, String username) throws  BusinessException;
      
    public Collection<Person> getPersonsByOrganizationId(Long organizationId);
    public Collection<Party> getAllParties();
    public void removePerson(Person person) throws BusinessException, SQLException, FinderException;
    public Person getPersonByFIA (java.lang.String fia) throws DataException;

	public boolean isPasswordResetRequired(String username) ;
    public void resetPassword(Long personId) ;
    //public Set<Role> getRoles(User user);
    public User getUserNRoles(Long userId);
    //public Person getPerson(User user);
	
    public Collection<Skill> getSkillsbyPersonId(Long personId);
    public void updatePersonSkills(Long personId, Collection<Skill> vos);
    
    public void updateDependents(Organization organization) throws BusinessException;
    
	/**
     * Generates timespecs for given Date plus for a number of days. Timespecs are generated for active workers that have no TimeSpecs for this day yet.
     * Timespecs are created via UI commonly. This method gives a default automated version.
     * Attributes of the new Timespec are copied from Timespec for the same day of week from last week. If no last week Tispec is found then the new one is not created.
     * To make auto creation work, users must create Timespecs for every worker manually for the firat time.
     */
    void generateTimeSpecs(Date startDate, int numberOfDays) throws BusinessException, RemoteException;

    /**
     * Create timspces for a person/location/day/shift in a range of dates
     * @param personId
     * @param locatorId
     * @param dateRangeStart
     * @param dateRangeEnd
     * @param shiftId
     * @param time
     * @param includeWeekDays           Collection of exculded week days as defined by java Calendar week days constants
     * @throws BusinessException
     */
    public void generateTimeSpecs(Long personId, Long locatorId,
                                   Date dateRangeStart,
                                   Date dateRangeEnd,
                                  Integer shiftId,Integer time,
                                  int [] includeWeekDays) throws BusinessException;
 
    /**
     * Delete ObjectDataDefinition of ObjectDataDefinition entity
     * @param dataDefId
     */
    public void removeObjectDataDefinition (java.lang.Long  dataDefId);

    /**
     * Update ObjectDataDefinition entity. vo.getDataDefId() will be used to identify the ObjectDataDefinition instance to update
     * @param vo ObjectDataDefinition
     */
    public void updateObjectDataDefinition (ObjectDataDefinition vo, long objectDefId);
    /**
     *
     * @param vo
     * @return
     * @throws CreateException
     */
    public Long createObjectDataDefinition(ObjectDataDefinition vo, long objectDefId) throws CreateException;
    
    /**
     * Get data for a locator
     * @param locatorId
     * @return collection of LocatorData
     */
    public Collection<LocatorData> getLocatorDatums(Long locatorId);
    public Collection<LocatorData> getLocatorData(Locator bean);
    

    /**
     * Get TaskGroupDefinitions for a ObjectDefinition
     * @param objectDefId
     * @return collection of TaskGroupDefinition
     */
    public Collection<TaskGroupDefinition> getTaskGroupDefs(Long objectDefId);
    public Collection<TaskGroupDefinition> getTaskGroupDefs(ObjectDefinition bean);

    public Collection<ObjectDataDefinition> getObjectDataDefs(Long objectDefId, Integer dataTypeId);
    
    /**
     * Create LocatorData entity
     * @param vo
     * @return Long primarykey
     * @throws CreateException
    */
    public Long createLocatorData(LocatorData bean);
    
    /**
     * Flips  isDisplay flag
     * @param dataDefId
     */
    public void updateDataDefDisplayStatus(Long dataDefId);
    
    /**
     * Get locator by FullLocator name
     * @param name
     * @return  Locator
     * @ 
     */
    public Locator getLocator(String  name) throws DataException ;
    
    public  Collection<Locator> findByFullLocator(String fullLocator);
    
    /**
     * Get TaskGroupDefinition of TaskGroupDefinition entity
     * @param taskGroupDefId
     * @return TaskGroupDefinition
     */
    public TaskGroupDefinition getTaskGroupDefinition(Long taskGroupDefId);

    public Collection<TaskDefinition> getTaskDefs(TaskGroupDefinition bean);

    /**
     * Update TaskGroupDefinition entity. vo.getTaskGroupDefId() will be used to identify the TaskGroupDefinition instance to update
     * @param vo TaskGroupDefinition
     */
    public void updateTaskGroupDefinition (TaskGroupDefinition bean) throws BusinessException;
    
    public void propagateTaskGroupDefinition(Long taskGroupDefId);
    
    public void propagateTaskGroupDefinitionCreate(Long taskGroupDefId);
    
    /**
     * Create new Task Group Definition
     * @param vo
     * @return
     * @throws BusinessException
     */
    public Long createTaskGroupDefinition (TaskGroupDefinition bean) throws BusinessException;
    
    /**
     * Get all ungrouped task defs for ObjectDefinition  given by objectDefId
     * @param objectDefId
     * @return  collection
     */
    public Collection<TaskDefinition> getObjectDefinitionTasksUngrouped (java.lang.Long  objectDefId);
    
    /**
     * Delete Locator of Locator entity
     * @param id
     * @throws RemoveException
     */
    public void removeLocator (java.lang.Long  id) throws RemoveException, BusinessException;
    
    /**
     * Get task defs for given object definition
     * @param objectDefId
     * @return col
     */
    public Collection<TaskDefinition> getTaskDefinitions(Long objectDefId);
    
    /**
     * Delete ObjectDefinition of ObjectDefinition entity
     * @param objectDefId
     * @throws RemoveException
     */
    public void removeObjectDefinition(Long  objectDefId) throws BusinessException,RemoveException;
    
    /**
     * Update ObjectDefinition entity. bean.getObjectDefId() will be used to identify the ObjectDefinition instance to update
     * @param bean ObjectDefinition
     */
    public void updateObjectDefinition (ObjectDefinition bean);

    /**
     * Delete TaskDefinition of TaskDefinition entity
     * @param taskDefId
     * @throws RemoveException
     */
    public void removeTaskDefinition(Long taskDefId) throws RemoveException,BusinessException;
    
    /**
     * Create TaskDefinition entity
     * @param bean
     * @return
     * @throws BusinessException
     */
    public Long createTaskDefinition(TaskDefinition bean) throws CreateException,BusinessException;
    
    /**
     * Copy Task definitions from one Template  to another
     * @param sourceObjectDefinitionId
     * @param targetObjectDefinitionId
     * @return number of copied tasks
     * @throws CreateException
     */
    public int copyTaskDefinitions(Long sourceObjectDefinitionId, Long targetObjectDefinitionId) throws CreateException;
    
    /**
     * Update TaskDefinition entity. vo.getId() will be used to identify the TaskDefinition instance to update
     * @param vo TaskDefinition
     * @throws BusinessException
     */
    public void updateTaskDefinition (TaskDefinition bean) throws BusinessException;
    
    public void propagateTaskDefinition(Long taskDefinitionId);

    /**
	 * When Task created it must have default actions created also
	 */
	public void addDefaultActions(TaskDefinition bean);
    
	public void addDefaultActions(Task bean);
	
    /**
     * Delete ActionDefinition of ActionDefinition entity
     * @param actionDefId
     * @throws BusinessException
     */
    public void removeActionDefinition(Long  actionDefId) throws BusinessException;
    
    /**
     * Update ActionDefinition entity. vo.getActionDefId() will be used to identify the ActionDefinition instance to update
     * @param vo ActionDefinition
     */
    public void updateActionDefinition (ActionDefinition bean);

    /**
     * Update action definitions sequences
     * @param actions    Collection of ActionDefinition
     * @throws CreateException
     */
    public void updateActionSequence(Collection<ActionDefinition> actions) throws CreateException;
    
    /**
     * Create action def. Create actions for all Task that linked to TaskDef
     * @param bean
     * @return
     * @throws CreateException
     */
    public Long createActionDefinition(ActionDefinition bean) throws  CreateException;

    /**
     *
     * @param cleaner
     * @throws Exception
     */
    public void cleanNext(MigratedDataCleaner cleaner) throws Exception;
   
   /**
   *
   * @param cleaner   instance of MigratedDataCleaner
   */public void prepareDataCleaner(MigratedDataCleaner cleaner);

    /**
     * Delete LocatorData of LocatorData entity
     * @param id
     * @throws RemoveException
     */
    public void removeLocatorData (java.lang.Long  id) throws RemoveException;

    /**
     * Delete WorkSchedule of WorkSchedule entity
     * @param workScheduleId
     * @throws BusinessException
     */
    public void removeWorkSchedule (java.lang.Long  workScheduleId) throws BusinessException;
    
    public void resetWorkSchedule (java.lang.Long  workScheduleId);

    public Collection<Action> findActionByActionDefinition(long actionDefId);
    
    public Collection<JobSchedule> findJobScheduleByWorkSchedule(Long  workScheduleId);
    
    public boolean hasTimeRecorded(Long jobScheduleId);
    public Collection<JobTaskTime> findJobTaskTimeByJobSchedule(Long jobScheduleId);
    
    public Principal getCallerPrincipal();
    
    public Collection<ObjectData> findObjectDataByDataDefinition(Long dataDefId);
    
    public Collection<Task> findTaskByTaskDefinition(Long taskDefId);
    
    public Collection<TaskGroup> getTaskGroups(TaskGroupDefinition taskGroupDef);
    
    public ObjectDefinition getObjectDefinition(ObjectDataDefinition definition);
    
    public Collection<ObjectEntity> getObjects(ObjectDefinition objectDef);
    
    public Collection<ObjectEntity> findObjectByObjectDefinition(Long objectDefId);
    
    public Collection<ObjectEntity> findByClassAndLocator(Long classId, Long locatorId) ;
    
    /**
     * Delete TaskGroupDefinition of TaskGroupDefinition entity
     * @param taskGroupDefId
     */
    public void removeTaskGroupDefinition(Long  taskGroupDefId);
    
    /**
     * Get User of User entity by username
     * @param username
     * @return User
     */
    public User getUserByName(String name) throws DataException;
    public User getUserByPersonId(Long personId) throws DataException;
	
    public ActionDefinition getActionDefinition(Action action);
    public Task getTask(Action action);
    public TaskDefinition getTaskDefinition(ActionDefinition ad);   
    public Collection<Action> getActions(ActionDefinition ad);
    
    public Collection<TaskDefinition> getTaskDefs(ObjectDefinition od);
	
	public List<TaskSequence> getTaskSequences(Sequence sequence);
	
    public Collection<ActionDefinition> getActionDefs(TaskDefinition td);
    public TaskGroupDefinition getGroupDefinition(TaskDefinition td);
	public ObjectDefinition getObjectDefinition(TaskDefinition td);
	
	public ObjectDefinition getObjectDefinition(TaskGroupDefinition tgd);
	
	public Locator getLocator(LocatorData locatorData);
	
	/*public Party getParty(Organization org);
	
	public Address getAddress(Party party);
	
	public Party getParty(Person person);
	public User getUser(Person person);	
	*/
	public Set<User> getUsers(Role role);
	
	public void synchronize(Action action);
	
	public void synchronize(Task task);
	public void synchronizeGroupMembership(Task task, Collection<TaskGroup> taskGroups);
	
	/**
     * Activate/deactivate task
     * Task is active if startdate is set (see ejbStore)
     * RoutineMaint task can not be activated by this method
     * Start date must be set manually by users for RoutineMaint type
     * @param status
     * @deprecated
     */
    public void processStatusUpdate(Task task, Integer status);
    
    /**
    * RoutineMaint task can not be activated by default
    * Start date must be set manually by users for RoutineMaint type
    * @deprecated
    */
   public void makeActive(Task task);

   /**
    * Creates Action off ActionDefinition and adds to this Task
    * @param actionDef
    * @throws CreateException
    */
   public void inherit(Task task, ActionDefinition actionDef);
   public void validateTask(Task vo) throws BusinessException;
   
   public void signUser(JobSchedule js);
   
   /**
	 * Update job task time, either updating existing if a job task time
	 * instance exists for today, or create a new JobTaskTime instance
	 * otherwise
	 *
	 * @param time The time to record
	 * @param jobTaskId The JobTask for which to record time
	 * @param jobId The parent job of the JobTask
	 * @param workSchedules A collection of workSchedules for the worker.
	 */
   public void updateTime(int time,Long jobTaskId,Long jobId,Collection<WorkSchedule> workSchedules) throws Exception;
   
   public void validateLocator(Locator vo) throws BusinessException;
   
   public void validateObjectClassification(ObjectClassification vo) throws BusinessException;
   
   public void removeWorkerSkillGivenSkillType(long skillTypeId);
   
   public Collection<Skill> getWorkerSkillGivenSkillType(long skillTypeId);
   
   public Collection<WorkSchedule> getActiveWSByShift(int shiftId);
   
   public void validateOrganization(Organization org) throws BusinessException;
}
