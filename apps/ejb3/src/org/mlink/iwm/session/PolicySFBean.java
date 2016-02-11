package org.mlink.iwm.session;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.BaseEntity;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTask;
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
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.LocatorSchemaRef;
import org.mlink.iwm.lookup.ObjectClassSchemaRef;
import org.mlink.iwm.lookup.ObjectClassificationRef;
import org.mlink.iwm.lookup.OrganizationRef;
import org.mlink.iwm.lookup.OrganizationSchemaRef;
import org.mlink.iwm.lookup.PersonRef;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.rules.DeletionVisitor;
import org.mlink.iwm.rules.MigratedDataCleaner;
import org.mlink.iwm.rules.PropagationVisitor;
import org.mlink.iwm.rules.TaskPropertiesValidator;
import org.mlink.iwm.rules.TimeSpecsHelper;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andreipovodyrev Date: Mar 21, 2009
 */
@Interceptors(SessionInterceptor.class)
@Stateless(name = "PolicySFBean")
@RemoteBinding(jndiBinding = "PolicySFRemote")
@LocalBinding(jndiBinding = "PolicySFLocal")
public class PolicySFBean extends CommonSFBean implements PolicySFLocal, PolicySFRemote {
	private static final Logger logger = Logger.getLogger(PolicySFBean.class);

	@PersistenceContext(unitName = "iwm_dpc")
	private EntityManager manager;

	/**
	 * Get Entity
	 * 
	 * @param name
	 * @return Entity
	 */
	private <K> K get(Class<K> clazz, String name)  {
		K be = null;
		String errorStr;
		Collection<Object> lst = manager.createQuery(
				"select o from " + clazz.getName()
						+ " o where o.party.name = :name").setParameter("name",
				name).getResultList();
		if (lst.size() > 1) {
			errorStr = "get EntityByName for " + clazz.getName() + " returned "
					+ lst.size() + " records instead of one.";
			logger.error("Error: " + errorStr);
			throw new RuntimeException(errorStr);
		} else if (lst.size() < 1) {
			errorStr = "get EntityByName for " + clazz.getName() + " returned "
					+ lst.size() + " records instead of one.";
			logger.error("Error: " + errorStr);
			throw new RuntimeException(errorStr);
		} else {
			be = (K) (lst.iterator().next());
		}
		return be;
	}
	
	public Organization getOrganizationByName(String name) {
		return get(Organization.class, name);
	}
	
	public Person getPersonByName(String name) {
		return get(Person.class, name);
	}
	
	public Collection<Party> getAllParties() {
		Query q = manager.createQuery("select p from Party p");
		return q.getResultList();
	}

	public Collection<Organization> getOrganizationsByParentId(Long parentId) {
		return getData(Organization.class, "parentId", parentId);
	}

	public Collection<Person> getPersonsByOrganizationId(Long organizationId) {
		return getData(Person.class, "organizationId", organizationId);
	}

	/**
	 * Get UserVO of User entity by username
	 * 
	 * @param username
	 * @return UserVO
	 */
	public User getUserByName(String name) throws DataException {
		Collection<User> col = getData(User.class, "username", name);
		User user = getUniqueData(col);
		//user.setPassword(null);
		return user;
	}
	
	public void validateUsername(long personId, String username) throws  BusinessException{
        User user;
        try {
        	user = getUserByName(username);
            if(user==null){
                return; // no dups exist
            }
        } catch (DataException e) {
            return; //// no dups exist
        }

        //apparently there is at least one user with the same username
        //check if it is just the same user being updated
        if(personId<=0 || personId!=user.getPerson().getId()){
            throw new BusinessException("username " + username + " is not available. Please choose a different name!");
        }
    }
	
	/**
	 * Get User of User entity by personId
	 * 
	 * @param personId
	 * @return User
	 */	
	public User getUserByPersonId(Long personId) throws DataException {
		Collection<User> col = getData(User.class, "person.id", personId);
		User user = getUniqueData(col);
		//user.setPassword(null);
		return user;
	}

	public Collection<Skill> getSkillsbyPersonId(Long personId) {
		return getData(Skill.class, "personId", personId);
	}

	public void updatePersonSkills(Long personId, Collection<Skill> vos) {
		logger.debug("Updating Person skills " + personId);
		Collection<Skill> skills = getSkillsbyPersonId(personId);

		// handle updates
		for (Iterator<Skill> iterator = skills.iterator(); iterator.hasNext();) {
			Skill skill = (Skill) iterator.next();
			boolean isMatched = false;
			if (vos != null) {
				for (Iterator<Skill> iterator2 = vos.iterator(); iterator2.hasNext();) {
					Skill skill2 = (Skill) iterator2.next();
					if (skill.getId() == skill2.getId()) {
						isMatched = true;
						skill2.setPersonId(personId);
						update((BaseEntity) skill2);
						iterator2.remove();
					}
				}
			}
			if (!isMatched) { // is no longer valid
				iterator.remove();
				remove((BaseEntity) skill);
			}
		}
		// handle inserts
		if (vos != null) {
			for (Skill skill : vos) {
				skill.setPersonId(personId);
				create((BaseEntity) skill);
				skills.add(skill);
			}
		}
	}

	public void removeOrganization(Organization organization)
			throws BusinessException, SQLException, FinderException {
		DeletionVisitor.delete(organization);
	}

	public void removePerson(Person person) throws BusinessException,
			SQLException, FinderException {
		DeletionVisitor.delete(person);
	}

	/**
	 * Get Person of Person entity by the specified field information appliance
	 * id
	 * 
	 * @param fia
	 * @return Person
	 * @
	 */
	public Person getPersonByFIA(String fia) throws DataException {
		Collection<Person> col = getData(Person.class, "fia", fia);
		return getUniqueData(col);
	}

	/**
	 * users that have password=username and are required to change password
	 * 
	 * @param username
	 * @return boolean
	 */
	public boolean isPasswordResetRequired(String username)
			 {
		User user = getUserByName(username);
		return user.getUsername().equals(user.getPassword());
	}

	/**
	 * Update user passw
	 * 
	 * @param userId
	 */
	public void updateUserPassword(Long userId, String oldPassword,
			String newPassword) throws BusinessException {
		User user = get(User.class, userId);
		if (!user.getPassword().equals(oldPassword)) {
			throwBusinessException("Current Password is incorrect");
		}
		if (user.getUsername().equals(newPassword)) {
			throwBusinessException(
					"Please choose a different word for your password!");
		}
		user.setPassword(newPassword);
		update(user);
	}

	public User getUserNRoles(Long userId) {
		User user = get(User.class, userId);
		if(user.getRoles()!=null){
			user.getRoles().size();
		}
		return user;
	}

	/*public Person getPerson(User user) {
		return user.getPerson();
	}*/

	public void updateDependents(Organization organization)
			throws BusinessException {
		Collection<Organization> beans;
		beans = getOrganizationsByParentId(organization.getId());
		if (beans != null) {
			for (Organization childOrg : beans) {
				Long newSchema = organization.getSchemaId() + 1;
				if (OrganizationSchemaRef.schemaIdBottom < newSchema) {
					String msg = childOrg.getParty().getName()
							+ ", "
							+ OrganizationSchemaRef
									.getLabel(OrganizationSchemaRef.schemaIdBottom)
							+ " level organization cannot be assigned as parent!";
					throwBusinessException(msg);
				}
				childOrg.setSchemaId(newSchema);
				update(childOrg);
				
				updateDependents(childOrg);
			}
		}
	}

	/**
	 * Resets the specified person's password
	 * 
	 * @param personId
	 */
	public void resetPassword(Long personId)  {
		Person person = get(Person.class, personId);
		if (person == null) {
			throw new RuntimeException("Person with id: " + personId
					+ " is null.");
		}
		User u = getUserByPersonId(personId);
		if (u == null) {
			throw new RuntimeException("User with id: " + personId + " is null.");
		}
		u.setPassword(u.getUsername());
		update(person);
	}

	private Collection<Person> findActiveWorkers() {
		return getData(Person.class, "active", new Integer(1));
	}

	private Collection<WorkSchedule> getWSByPersonLocatorDayAndShift(Long personId,
			Long locatorId, Date day, Integer shiftId)  {
		// select object(c) from WorkSchedule as c where c.personId=?1 and
		// c.locatorId=?2 and c.day=?3 and c.shiftId=?4]]>
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
		fieldNameValueMap.put("personId", personId);
		fieldNameValueMap.put("locatorId", locatorId);
		fieldNameValueMap.put("day", day);
		fieldNameValueMap.put("shiftId", shiftId);
		return getData(WorkSchedule.class, null, fieldNameValueMap);
	}
	
	public Collection<WorkSchedule> getActiveWSByShift(int shiftId) {
		// select object(c) from WorkSchedule as c where c.personId=?1 and
		// c.locatorId=?2 and c.day=?3 and c.shiftId=?4]]>
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
		fieldNameValueMap.put("shiftId", shiftId);
		return getData(WorkSchedule.class, " o.archivedDate is null", fieldNameValueMap);
	}

	/**
	 * Generates timespecs for given Date plus for a number of days. Timespecs
	 * are generated for active workers that have no TimeSpecs for this day yet.
	 * Timespecs are created via UI commonly. This method gives a default
	 * automated version. Attributes of the new Timespec are copied from
	 * Timespec for the same day of week from last week. If no last week Timespec
	 * is found then the new one is not created. To make auto creation work,
	 * users must create Timespecs for every worker manually for the firat time.
	 */
	public void generateTimeSpecs(Date startDate, int numberOfDays)
			throws BusinessException {
		Calendar scheduledDate = Calendar.getInstance();
		scheduledDate.setTimeInMillis(startDate.getTime());
		for (int i = 0; i < numberOfDays; i++) {
			generateTimeSpecs(scheduledDate.getTimeInMillis());
			scheduledDate.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	private void generateTimeSpecs(Long timeInMillis) throws BusinessException {
		ControlSF control = ServiceLocator.getControlSFLocal();
		Calendar targetDate = Calendar.getInstance();
		targetDate.setTimeInMillis(timeInMillis);
		/** get all active workers with no timespec for target date **/
		targetDate.add(Calendar.DAY_OF_MONTH, -7);
		Date weekago = new Date(targetDate.getTimeInMillis());
		Date scheduledDate = new Date(timeInMillis);
		Collection<Person> workers = findActiveWorkers();
		for (Person p : workers) {
			Collection<WorkSchedule> schedules = control.getActiveWorkSchedulesByPersonAndDate(p.getId(),
					scheduledDate);
			if (schedules==null || schedules.size() == 0) {
				/* the person does not have schedule. Find his schedules one
				 * week ago and use as prototype for today. If there ano
				 * schedules week ago, then create nothing
				 */
				Collection<WorkSchedule> schedulesWeekago = control.getActiveWorkSchedulesByPersonAndDate(p
						.getId(), weekago);
				for (Object aSchedulesWeekago : schedulesWeekago) {
					WorkSchedule ws = (WorkSchedule) aSchedulesWeekago;
					logger.debug("Generating timespecs for "
							+ p.getParty().getName() + " Locator "
							+ ws.getLocatorId() + " Day " + targetDate.getTime());
					generateTimeSpecs(p.getId(), ws.getLocatorId(),
							scheduledDate, scheduledDate, ws.getShiftId(), ws
									.getTime(), new int[] { targetDate
									.get(Calendar.DAY_OF_WEEK) });
				}
			}
		}
		logger.debug("lazy number" + workers.size());
	}

	/**
	 * Create timspces for a person/location/day/shift in a range of dates
	 * 
	 * @param personId
	 * @param locatorId
	 * @param dateRangeStart
	 * @param dateRangeEnd
	 * @param shiftId
	 * @param time
	 * @param includeWeekDays
	 *            Collection of exculded week days as defined by java Calendar
	 *            week days constants
	 * @throws BusinessException
	 */
	public void generateTimeSpecs(Long personId, Long locatorId,
			Date dateRangeStart, Date dateRangeEnd, Integer shiftId,
			Integer time, int[] includeWeekDays) throws BusinessException {
		if (locatorId == null || shiftId == null || time == null)
			throwBusinessException(
					"Create calendar params are not properly defined!");

		try {
			Person person = get(Person.class, personId);
			if (Constants.NO.equals(person.getActive())) {
				throwBusinessException(
						"Cannot create calendar for inactive worker!");
			}
			List<Date> dates = TimeSpecsHelper.getDaysInRange(dateRangeStart,
					dateRangeEnd, includeWeekDays);
			for (Date day : dates) {
				Collection<WorkSchedule> dups = getWSByPersonLocatorDayAndShift(personId, locatorId,
						(Date) day, shiftId);
				// in reality there should be no more then one duplicate. check
				// for collection anyway
				for (WorkSchedule duplicate : dups) {
					//do if not archived.
					if(duplicate.getArchivedDate()==null){
						try {
							remove(duplicate);
						} catch (Exception e) {
							String personName = PersonRef.getLabel(personId);
							String location = LocatorRef.getFullLocator(locatorId);
							throwBusinessException(
									"Schedule may not be overriden ("
											+ personName
											+ "/"
											+ location
											+ "/"
											+ day
											+ ")\nChoose another date range or location");
						}
					}
				}

				WorkSchedule ws = new WorkSchedule();
				ws.setPersonId(personId);
				ws.setLocatorId(locatorId);
				ws.setDay(new Date(day.getTime()));
				ws.setTime(time);
				ws.setShiftId(shiftId);
				ws.setStatusId(WorkScheduleStatusRef
						.getIdByCode(WorkScheduleStatusRef.Status.NYS));
				// create new entity
				create(ws);
			}
		} catch (Exception e) {
			throwBusinessException(e.getMessage());
		}
	}

	/**
	 * Delete ObjectDataDefinition of ObjectDataDefinition entity
	 * 
	 * @param dataDefId
	 */
	public void removeObjectDataDefinition(Long dataDefId) {
		try {
			ObjectDataDefinition odd = get(ObjectDataDefinition.class,
					dataDefId);
			DeletionVisitor.delete(odd);
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Update ObjectDataDefinition entity. bean.getDataDefId() will be used to
	 * identify the ObjectDataDefinition instance to update
	 * 
	 * @param bean
	 *            ObjectDataDefinition
	 */
	public void updateObjectDataDefinition(ObjectDataDefinition bean, long objectDefId) {
		logger.debug("Updating ObjectDataDefinition " + bean);
		ObjectDefinition od = get(ObjectDefinition.class, objectDefId);
		bean.setObjectDefinition(od);
		update(bean);
		PropagationVisitor.propagateUpdate(bean);
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @throws CreateException
	 */
	public Long createObjectDataDefinition(ObjectDataDefinition bean, long objectDefId)
			throws CreateException {
		logger.debug("Creating ObjectDataDefinition " + bean);
		ObjectDefinition od = get(ObjectDefinition.class, objectDefId);
		bean.setObjectDefinition(od);
		create(bean);
		
		PropagationVisitor.propagateCreate(bean, od);
		return bean.getId();
	}

	/**
	 * Get data for a locator
	 * 
	 * @param locatorId
	 * @return collection of LocatorData
	 */
	public Collection<LocatorData> getLocatorDatums(Long locatorId) {
		Locator bean = get(Locator.class, locatorId);
		return getLocatorData(bean);
	}

	public Collection<LocatorData> getLocatorData(Locator bean) {
		return bean.getLocatorData();
	}
	
	public Locator getLocator(LocatorData locatorData) {
		return locatorData.getLocator();
	}

	/**
	 * Get ObjectDataDefinition(s) for an ObjectDefinition
	 * 
	 * @param objectDefId
	 * @return collection of ObjectDataDefinition
	 */
	public Collection<ObjectDataDefinition> getObjectDataDefs(Long objectDefId) {
		ObjectDefinition bean = get(ObjectDefinition.class, objectDefId);
		return (bean.getDataDefs());
	}

	/**
	 * Get TaskGroupDefinitions for a ObjectDefinition
	 * 
	 * @param objectDefId
	 * @return collection of TaskGroupDefinition
	 */
	public Collection<TaskGroupDefinition> getTaskGroupDefs(Long objectDefId) {
		ObjectDefinition bean = get(ObjectDefinition.class, objectDefId);
		return getTaskGroupDefs(bean);
	}

	public Collection<TaskGroupDefinition> getTaskGroupDefs(ObjectDefinition bean) {
		return bean.getTaskGroupDefs();
	}

	/**
	 * Get DataDefinitions of a specific type for ObjectDefinition
	 * 
	 * @param objectDefId
	 * @param dataTypeId
	 * @return collection of ObjectDataDefinition beans
	 */
	public Collection<ObjectDataDefinition> getObjectDataDefs(Long objectDefId,
			Integer dataTypeId) {
		return findByObjectDefAndDataType(objectDefId, dataTypeId);
	}

	private Collection<ObjectDataDefinition> findByObjectDefAndDataType(
			Long objectDefId, Integer dataTypeId) {
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
		fieldNameValueMap.put("objectDefId", objectDefId);
		fieldNameValueMap.put("dataTypeId", dataTypeId);
		// select object(p) from ObjectDataDefinition as p where p.objectDefId =
		// ?1 and p.dataTypeId = ?2
		return getData(ObjectDataDefinition.class, null, fieldNameValueMap);
	}

	/**
	 * Create LocatorData entity
	 * 
	 * @param bean
	 * @return Long primarykey
	 * @throws CreateException
	 */
	public Long createLocatorData(LocatorData bean) {
		logger.debug("Creating Locator Data " + bean);
		create(bean);
		/*Locator locator = get(Locator.class, bean.getLocatorId());
		Collection<LocatorData> locData = getLocatorData(locator);
		locData.add(bean);
		*/return bean.getId();
	}

	/**
	 * Flips isDisplay flag
	 * 
	 * @param dataDefId
	 */
	public void updateDataDefDisplayStatus(Long dataDefId) {
		logger.debug("Updating DataDefDisplayStatus " + dataDefId);

		ObjectDataDefinition bean = get(ObjectDataDefinition.class, dataDefId);
		if (Constants.YES.equals(bean.getIsDisplay())) {
			bean.setIsDisplay(Constants.NO);
		} else {
			bean.setIsDisplay(Constants.YES);
		}
		update(bean);
	}

	/**
	 * Get locator by FullLocator name
	 * 
	 * @param name
	 * @return Locator
	 * @
	 */
	public Locator getLocator(String name) throws DataException {
		Collection<Locator> col = findByFullLocator(name);
		return getUniqueData(col);
	}

	public Collection<Locator> findByFullLocator(String fullLocator) {
		// select object(l) from Locator as l where l.fullLocator = ?1
		return getData(Locator.class, "fullLocator", fullLocator);
	}

	/**
	 * Get TaskGroupDefinition of TaskGroupDefinition entity
	 * 
	 * @param taskGroupDefId
	 * @return TaskGroupDefinition
	 */
	public TaskGroupDefinition getTaskGroupDefinition(Long taskGroupDefId) {
		TaskGroupDefinition bean = get(TaskGroupDefinition.class,
				taskGroupDefId);
		bean.setTaskDefs(getTaskDefs(bean));
		return bean;
	}

	public Collection<TaskDefinition> getTaskDefs(TaskGroupDefinition bean) {
		return bean.getTaskDefs();
	}

	/**
	 * Delete TaskGroupDefinition of TaskGroupDefinition entity
	 * 
	 * @param taskGroupDefId
	 */
	public void removeTaskGroupDefinition(Long taskGroupDefId) {
		try {
			DeletionVisitor.delete(get(TaskGroupDefinition.class,
					taskGroupDefId));
			// jms would require one more screen refresh as it is async.
			// PTPClient.sendObjectAsync(new
			// DeleteTaskGroupTemplate(taskGroupDefId));
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Update TaskGroupDefinition entity. bean.getTaskGroupDefId() will be used
	 * to identify the TaskGroupDefinition instance to update
	 * 
	 * @param bean
	 *            TaskGroupDefinition
	 */
	public void updateTaskGroupDefinition(TaskGroupDefinition bean)
			throws BusinessException {
		logger
				.debug("Start updateTaskGroupDefinition "
						+ bean.getDescription());
		try {
			if (bean.getDescription() == null)
				throwBusinessException("Description required!");
			
			Collection<TaskDefinition> taskDefs = getTaskDefs(bean);
			
			if(taskDefs.size()<=1){
	               //if more than one memeber of array then we have disparate skill types
	                throw new BusinessException("Two or more tasks must be assigned to a Task Group!");
	        }
			// business rule check for the same skillTypeId
			Integer skillTypeId = TaskPropertiesValidator.extractSkillType(taskDefs);
			bean.setSkillTypeId(skillTypeId);
			bean.setTaskDefs(taskDefs);
			update(bean);

			// should be called outside TX. Let container commimt first.
			// PTPClient.sendObjectAsync(new
			// PropagateTaskGroupTemplate(bean.getTaskGroupDefId()));
			logger.debug("End updateTaskGroupDefinition "
					+ bean.getDescription());
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	public void propagateTaskGroupDefinition(Long taskGroupDefId) {
		TaskGroupDefinition bean = get(TaskGroupDefinition.class,
				taskGroupDefId);
		PropagationVisitor.propagateUpdate(bean);
		PropagationVisitor.propagateTaskGroupDefinitionTasks(bean);
	}

	public void propagateTaskGroupDefinitionCreate(Long taskGroupDefId) {
		TaskGroupDefinition bean = get(TaskGroupDefinition.class,
				taskGroupDefId);
		PropagationVisitor.propagateCreate(bean);
	}

	/**
	 * Create new Task Group Definition
	 * 
	 * @param bean
	 * @return
	 * @throws BusinessException
	 */
	public Long createTaskGroupDefinition(TaskGroupDefinition bean)
			throws BusinessException {
		logger
				.debug("Start createTaskGroupDefinition "
						+ bean.getDescription());
		try {
			List <TaskDefinition> selectedTasks = (List<TaskDefinition>) bean.getTaskDefs();
			
			if(selectedTasks.size()<=1){
	               //if more than one memeber of array then we have disparate skill types
	                throw new BusinessException("Two or more tasks must be assigned to a Task Group!");
	        }
			bean.setTaskDefs(null);
			create(bean);
			
			if(selectedTasks!=null && !selectedTasks.isEmpty()){
	        	for(TaskDefinition selectedTask : selectedTasks){
	        		selectedTask.setTaskGroupDef(bean);
	        		update(selectedTask);
	        	}
			}
			
			bean.setTaskDefs(selectedTasks);
			
			ObjectDefinition od = get(ObjectDefinition.class, bean.getObjectDefinition().getId());
			bean.setObjectDefinition(od);
			Collection<TaskDefinition> taskDefs = getTaskDefs(bean);

			// business rule check for the same skillTypeId
			Integer skillTypeId = TaskPropertiesValidator.extractSkillType(taskDefs);
			bean.setSkillTypeId(skillTypeId);
			bean.setTaskDefs(taskDefs);

			// add TaskGroupDefinition to ObjectDefinition
			Collection<TaskGroupDefinition> taskGroupDefs = od
					.getTaskGroupDefs();
			if (taskGroupDefs == null) {
				taskGroupDefs = new HashSet<TaskGroupDefinition>();
			}
			taskGroupDefs.add(bean);
			od.setTaskGroupDefs(taskGroupDefs);
			update(od);
			update(bean);

			// create TaskGroup Instances
			// PropagationVisitor.propagateCreate(bean);
			// PropagationVisitor.propagateUpdate(bean);
			// must be called outside create TX. Let container commimt first.
			// PTPClient.sendObjectAsync(new
			// PostCreateTaskGroupTemplate(bean.getTaskGroupDefId()));
			logger.debug("End createTaskGroupDefinition "
					+ bean.getDescription());
		} catch (Exception e) {
			throw new IWMException(e);
		}

		return bean.getId();
	}

	/**
	 * Get all ungrouped task defs for ObjectDefinition given by objectDefId
	 * 
	 * @param objectDefId
	 * @return collection
	 */
	public Collection<TaskDefinition> getObjectDefinitionTasksUngrouped(Long objectDefId) {
		return findByObjectDefAndNotInGroup(objectDefId);
	}

	private Collection<TaskDefinition> findByObjectDefAndNotInGroup(
			Long objectDefId) {
		// select object(p) from TaskDefinition as p where p.objectDefId = ?1
		// and p.groupId is null
		Map<String, Object> fieldNameValueMap = new HashMap<String, Object>();
		fieldNameValueMap.put("objectDefId", objectDefId);
		return getData(TaskDefinition.class, "p.groupId is null",
				fieldNameValueMap);
	}

	/**
	 * Delete Locator of Locator entity
	 * 
	 * @param id
	 * @throws RemoveException
	 */
	public void removeLocator(Long id) throws RemoveException,
			BusinessException {
		try {
			Locator bean = get(Locator.class, id);
			DeletionVisitor.delete(bean);
			// filter is not caching as of new implementation
			// forg.mlink.iwm.filter.LocatorTree.getInstance().load();
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Get task defs for given object definition
	 * 
	 * @param objectDefId
	 * @return col
	 */
	public Collection<TaskDefinition> getTaskDefinitions(Long objectDefId) {
		//ObjectDefinition bean = get(ObjectDefinition.class, objectDefId);
		//return bean.getTaskDefs();
		return getData(TaskDefinition.class, "objectDefinition.id", objectDefId);
	}

	/**
	 * Delete ObjectDefinition of ObjectDefinition entity
	 * 
	 * @param objectDefId
	 * @throws RemoveException
	 */
	public void removeObjectDefinition(Long objectDefId)
			throws BusinessException, RemoveException {
		ObjectDefinition od = get(ObjectDefinition.class, objectDefId);

		if (od.getTaskDefs()!=null && od.getTaskDefs().size() > 0)
			throwBusinessException("Object Definition has Task Definitions. Delete them first!");
		if (od.getDataDefs()!=null && od.getDataDefs().size() > 0)
			throwBusinessException("Object Definition has Data Definitions. Delete them first!");
		if (od.getTaskGroupDefs()!=null && od.getTaskGroupDefs().size() > 0)
			throwBusinessException("Object Definition has Task Group Definitions. Delete them first!");

		Collection<ObjectEntity> col = findObjectEntityByObjectDefinition(objectDefId);
		if (col.size() > 0)
			throwBusinessException("Object Definition has Instances. Cannot be deleted");
		remove(od);
	}

	private Collection<ObjectEntity> findObjectEntityByObjectDefinition(
			Long objectDefId) {
		return getData(ObjectEntity.class, "objectDefId", objectDefId);
	}

	/**
	 * Update ObjectDefinition entity. bean.getObjectDefId() will be used to
	 * identify the ObjectDefinition instance to update
	 * 
	 * @param bean
	 *            ObjectDefinition
	 */
	public void updateObjectDefinition(ObjectDefinition bean) {
		logger.debug("Updating ObjectDefinition " + bean);
		update(bean);
		PropagationVisitor.propagateUpdate(bean);
	}

	/**
	 * Delete TaskDefinition of TaskDefinition entity
	 * 
	 * @param taskDefId
	 * @throws RemoveException
	 */
	public void removeTaskDefinition(Long taskDefId) throws RemoveException,
			BusinessException {
		try {
			TaskDefinition bean = get(TaskDefinition.class, taskDefId);
			DeletionVisitor.delete(bean);
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Create TaskDefinition entity
	 * 
	 * @param bean
	 * @return
	 * @throws BusinessException
	 */
	public Long createTaskDefinition(TaskDefinition bean)
			throws CreateException, BusinessException {
		logger.debug("Creating TaskDefinition " + bean);
		ObjectDefinition od;
		od = get(ObjectDefinition.class, bean.getObjectDefinition().getId());
		bean.setObjectDefinition(od);
		
		// add default action
		addDefaultActions(bean);
		create(bean);

		od.getTaskDefs().add(bean);
		update(od);

		PropagationVisitor.propagateCreate(bean);
		return bean.getId();
	}

	/**
	 * When Task created it must have default actions created also
	 */
	public void addDefaultActions(TaskDefinition bean) {
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	ActionDefinition action1 = new ActionDefinition();
        action1.setVerb("Record");
        action1.setName("Worker");
        action1.setModifier("Feedback");
        action1.setSequence(new Integer(1));
        action1.setTaskDefinition(bean);
        ActionDefinition action2 = new ActionDefinition();
        action2.setVerb("Does This");
        action2.setName("Job Require");
        action2.setModifier("Further Action?");
        action2.setSequence(new Integer(2));
        action2.setTaskDefinition(bean);
        
        Collection<ActionDefinition> actionDefs = bean.getActionDefs();
        if(actionDefs == null){
        	actionDefs = new HashSet<ActionDefinition>();
        	bean.setActionDefs(actionDefs);
        }
        actionDefs.add(action1);
        actionDefs.add(action2);
    }

	/**
	 * When Task created it must have default actions created also
	 */
	public void addDefaultActions(Task bean) {
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	Action action1 = new Action();
        action1.setVerb("Record");
        action1.setName("Worker");
        action1.setModifier("Feedback");
        action1.setSequence(new Integer(1));
        action1.setTask(bean);
        Action action2 = new Action();
        action2.setVerb("Does This");
        action2.setName("Job Require");
        action2.setModifier("Further Action?");
        action2.setSequence(new Integer(2));
        action2.setTask(bean);
        
        Collection<Action> actions = bean.getActions();
        if(actions == null){
        	actions = new HashSet<Action>();
        	bean.setActions(actions);
        }
        actions.add(action1);
        actions.add(action2);
    }
	
	/**
	 * Copy Task definitions from one Template to another
	 * 
	 * @param source
	 *            Template from which to copy task templates
	 * @param target
	 *            Template to which to copy task templates
	 */
	private void copyTaskDefinitions(ObjectDefinition source,
			ObjectDefinition target) throws CreateException {
		logger.debug("Copying tasks from to Template "
				+ TargetClassRef.getCode(source.getClassId()) + " to Template "
				+ TargetClassRef.getCode(target.getClassId()));
		Collection<TaskDefinition> tasks = source.getTaskDefs();
		for (TaskDefinition td : tasks) {
			copyTaskDefinition(td, target);
		}
		logger.debug(" Done copying task templates");
	}

	/**
	 * Copy task def to given object def
	 * 
	 * @return
	 * @throws CreateException
	 */
	private TaskDefinition copyTaskDefinition(TaskDefinition task,
			ObjectDefinition object) throws CreateException {
		logger.debug("Copying Task Template " + task.getId() + "/"
				+ task.getTaskDescription());
		TaskDefinition newTask = new TaskDefinition();
		newTask.setEstTime(task.getEstTime());
		newTask.setFreqDays(task.getFreqDays());
		newTask.setFreqMonths(task.getFreqMonths());
		newTask.setFrequencyId(task.getFrequencyId());
		newTask.setNumberOfWorkers(task.getNumberOfWorkers());
		newTask.setPriorityId(task.getPriorityId());
		newTask.setRunHoursThreshInc(task.getRunHoursThreshInc());
		newTask.setSkillLevelId(task.getSkillLevelId());
		newTask.setSkillTypeId(task.getSkillTypeId());
		newTask.setTaskDescription(task.getTaskDescription());
		newTask.setTaskTypeId(task.getTaskTypeId());
		newTask.setWorkerTypeId(task.getWorkerTypeId());
		newTask.setExpiryTypeId(task.getExpiryTypeId());
		newTask.setExpiryNumOfDays(task.getExpiryNumOfDays());
		newTask.setObjectDefinition(object);
		create(newTask);

		Collection<ActionDefinition> actions = task.getActionDefs();
		for (ActionDefinition action : actions) {
			copyActionDefinition(action, newTask);
		}
		
		Collection<TaskDefinition> taskDefs = object.getTaskDefs();
		if(taskDefs == null){
			taskDefs = new HashSet<TaskDefinition>();
		}
		taskDefs.add(newTask);
		update(object);
		return newTask;
	}

	/**
	 * Copy Action Def to a given Task Def
	 * 
	 * @param action
	 * @param task
	 * @return
	 * @throws CreateException
	 */
	private ActionDefinition copyActionDefinition(ActionDefinition action,
			TaskDefinition taskDefinition) throws CreateException {
		logger.debug("Copying Action Template " + action.getId());
		ActionDefinition newAction = new ActionDefinition();
		newAction.setTaskDefinition(taskDefinition);
		newAction.setModifier(action.getModifier());
		newAction.setName(action.getName());
		newAction.setSequence(action.getSequence());
		newAction.setVerb(action.getVerb());
		create(newAction);

		Set<ActionDefinition> actionDefs = (Set<ActionDefinition>)taskDefinition.getActionDefs();
		if(actionDefs==null){
			actionDefs = new HashSet<ActionDefinition>();
			taskDefinition.setActionDefs(actionDefs);
		}
		actionDefs.add(newAction);
		update(taskDefinition);
		return newAction;
	}

	/**
	 * Copy Task definitions from one Template to another
	 * 
	 * @param sourceObjectDefinitionId
	 * @param targetObjectDefinitionId
	 * @return number of copied tasks
	 * @throws CreateException
	 */
	public int copyTaskDefinitions(Long sourceObjectDefinitionId,
			Long targetObjectDefinitionId) throws CreateException {
		ObjectDefinition source = get(ObjectDefinition.class,
				sourceObjectDefinitionId);
		ObjectDefinition target = get(ObjectDefinition.class,
				targetObjectDefinitionId);
		int size1 = target.getTaskDefs().size();
		copyTaskDefinitions(source, target);
		int size2 = target.getTaskDefs().size();
		return size2 - size1;
	}

	/**
	 * Update TaskDefinition entity. bean.getId() will be used to identify the
	 * TaskDefinition instance to update
	 * 
	 * @param bean
	 *            TaskDefinition
	 * @throws BusinessException
	 */
	public void updateTaskDefinition(TaskDefinition bean)
			throws BusinessException {
		logger.debug("Updating TaskDefinition " + bean);
		// TaskPropertiesValidator.validateTaskType(bean);
		update(bean);

		// this call takes too much time. Idealy should configure MDB for this.
		// For lack of Messaging framewor in place for now will split in two
		// calls
		// see propagateTaskDefinition(). Client action must call two methods to
		// properly update taskDef
		// updateTaskDefinition and propagateTaskDefinition() . andrei 07/24/05
		// org.mlink.iwm.rules.PropagationVisitor.propagateUpdate(bean);

		/**
		 * have to move it outside to prevent race condition betwenn JMS and
		 * bean commit to DB: observed a few occurences!
		 * PTPClient.sendObjectAsync(new
		 * PropagateTaskTemplate(bean.getTaskDefId()));
		 */
	}

	public void propagateTaskDefinition(Long taskDefinitionId) {
		TaskDefinition bean = get(TaskDefinition.class, taskDefinitionId);
		org.mlink.iwm.rules.PropagationVisitor.propagateUpdate(bean);
	}

	/**
	 * Delete ActionDefinition of ActionDefinition entity
	 * 
	 * @param actionDefId
	 * @throws BusinessException
	 */
	public void removeActionDefinition(Long actionDefId)
			throws BusinessException {
		try {
			ActionDefinition bean = get(ActionDefinition.class, actionDefId);
			DeletionVisitor.delete(bean);
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	/**
	 * Update ActionDefinition entity. bean.getActionDefId() will be used to
	 * identify the ActionDefinition instance to update
	 * 
	 * @param bean
	 *            ActionDefinition
	 */
	public void updateActionDefinition(ActionDefinition bean) {
		logger.debug("Updating ActionDefinition " + bean);
		TaskDefinition taskDef = get(TaskDefinition.class, bean.getTaskDefinition().getId());
        bean.setTaskDefinition(taskDef);
        
		update(bean);

		// now update all instances
		PropagationVisitor.propagateUpdate(bean);
	}

	/**
	 * Update action definitions sequences
	 * 
	 * @param actions
	 *            Collection of ActionDefinition
	 * @throws CreateException
	 */
	public void updateActionSequence(Collection<ActionDefinition> actions) throws CreateException {
		for (ActionDefinition adVo : actions) {
			ActionDefinition adBean = get(ActionDefinition.class, adVo.getId());
			adBean.setSequence(adVo.getSequence());
			update(adBean);
			
			// update sequence for all instances linked to this actionDef
			PropagationVisitor.propagateUpdate(adBean);
		}
	}

	/**
	 * Create action def. Create actions for all Task that linked to TaskDef
	 * 
	 * @param bean
	 * @return
	 * @throws CreateException
	 */
	public Long createActionDefinition(ActionDefinition bean)
			throws CreateException {
		logger.debug("Creating ActionDefinition " + bean);
		TaskDefinition taskDef = get(TaskDefinition.class, bean.getTaskDefinition().getId());
        bean.setTaskDefinition(taskDef);
        bean.setSequence(taskDef.getActionDefs()!=null?taskDef.getActionDefs().size():0);
		create(bean);
		PropagationVisitor.propagateCreate(bean);
		return bean.getId();
	}

	/**
	 * 
	 * @param cleaner
	 * @throws Exception
	 */
	public void cleanNext(MigratedDataCleaner cleaner) throws Exception {
		cleaner.cleanNext();
	}

	/**
	 * 
	 * @param cleaner
	 *            instance of MigratedDataCleaner
	 */
	public void prepareDataCleaner(MigratedDataCleaner cleaner) {
		cleaner.prepareIterator();
	}

	/**
	 * Delete LocatorData of LocatorData entity
	 * 
	 * @param id
	 * @throws RemoveException
	 */
	public void removeLocatorData(java.lang.Long id) throws RemoveException {
		LocatorData bean = get(LocatorData.class, id);
		remove(bean);
	}

	/**
	 * Delete WorkSchedule of WorkSchedule entity
	 * 
	 * @param workScheduleId
	 * @throws BusinessException
	 */
	public void removeWorkSchedule(java.lang.Long workScheduleId)
			throws BusinessException {
		logger.debug("removeWorkSchedule  " + workScheduleId);
		WorkSchedule ws = get(WorkSchedule.class, workScheduleId);
		
		Collection<JobSchedule> beans = findJobScheduleByWorkSchedule(workScheduleId);
		if(beans!=null && !beans.isEmpty()){
			for (JobSchedule js : beans) {
				DeletionVisitor.delete(js);
				// if(js!=null) js.setWorkScheduleId(null); as we are no longer
				// deleting ws
			}
			ws.setArchivedDate(new Timestamp(System.currentTimeMillis()));
			ws.setStatusId(WorkScheduleStatusRef.getIdByCode(WorkScheduleStatusRef.Status.DUN));
			update(ws);
		}else{
			DeletionVisitor.delete(ws);
		}
	}

	public void resetWorkSchedule(java.lang.Long workScheduleId) {
		try {
			WorkSchedule ws = get(WorkSchedule.class, workScheduleId);
			ws.setStatusId(WorkScheduleStatusRef
					.getIdByCode(WorkScheduleStatusRef.Status.IP));
		} catch (Exception e) {
			throw new IWMException(e);
		}
	}

	public Collection<JobSchedule> findJobScheduleByWorkSchedule(
			Long workScheduleId) {
		return getData(JobSchedule.class, "workScheduleId", workScheduleId);
	}

	public Collection<Action> findActionByActionDefinition(long actionDefId) {
		// select object(p) from Action as p where p.actionDefId = ?1 and
		// p.custom = 0
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actionDefinition.id", actionDefId);
		map.put("custom", new Integer(0));
		return getData(Action.class, null, map);
	}

	public boolean hasTimeRecorded(Long jobScheduleId) {
		Collection<JobTaskTime> col = findJobTaskTimeByJobSchedule(jobScheduleId);
		// should throw exception if none found, but let's play safe
		if (col == null && col.size() == 0)
			return false;
		for (JobTaskTime jtt : col) {
			if (jtt.getTime() != null && jtt.getTime().intValue() > 0)
				return true;
		}
		return false;
	}

	public Collection<JobTaskTime> findJobTaskTimeByJobSchedule(
			Long jobScheduleId) {

		return getData(JobTaskTime.class, "jobScheduleId", jobScheduleId);
	}

	public Collection<ObjectData> findObjectDataByDataDefinition(Long dataDefId) {
		// select object(p) from ObjectData as p where p.dataDefId = ?1 and
		// p.custom=0
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("objectDataDef.id", dataDefId);
		map.put("custom", new Integer(0));
		return getData(ObjectData.class, null, map);
	}

	public Collection<Task> findTaskByTaskDefinition(Long taskDefId) {
		return getData(Task.class, "taskDefinition.id", taskDefId);
	}

	public Collection<TaskGroup> getTaskGroups(TaskGroupDefinition taskGroupDef) {
		return taskGroupDef.getTaskGroups();
	}

	public ObjectDefinition getObjectDefinition(ObjectDataDefinition definition) {
		return definition.getObjectDefinition();
	}

	public Collection<ObjectEntity> getObjects(ObjectDefinition objectDef) {
		return objectDef.getObjects();
	}

	public Collection<ObjectEntity> findObjectByObjectDefinition(
			Long objectDefId) {
		return getData(ObjectEntity.class, "objectDefinition.id", objectDefId);
	}

	public Collection<ObjectEntity> findByClassAndLocator(Long classId,
			Long locatorId)  {
		// select object(p) from ObjectEntity as p where p.classId = ?1 and
		// p.locatorId = ?2
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		map.put("locatorId", locatorId);
		
		Collection<ObjectEntity> oes = getData(ObjectEntity.class, null, map);
		Collection<ObjectEntity> oes1 = new ArrayList<ObjectEntity>();
		if(oes!=null && !oes.isEmpty()){
			oes1.add(oes.iterator().next());
			for(ObjectEntity oe: oes1){
				if(oe.getDatums()!=null)
					oe.getDatums().size();
				oe.getObjectDefinition();
				oe.getObjectRef();
				if(oe.getTaskGroups()!=null)
					oe.getTaskGroups().size();
				List<Task> tasks = oe.getTasks();
				if(tasks!=null){
					for(Task task:tasks){
						if(task.getActions()!=null)
							task.getActions().size();
						if(task.getJobTasks()!=null)
							task.getJobTasks().size();
					}
				}
			}
		}
		return oes1;
	}

	public ActionDefinition getActionDefinition(Action action) {
		return action.getActionDefinition();
	}

	public Task getTask(Action action) {
		return action.getTask();
	}

	public TaskDefinition getTaskDefinition(ActionDefinition ad) {
		return ad.getTaskDefinition();
	}

	public Collection<Action> getActions(ActionDefinition ad) {
		return ad.getActions();
	}

	public Collection<TaskDefinition> getTaskDefs(ObjectDefinition od) {
		return od.getTaskDefs();
	}
	
	public List<TaskSequence> getTaskSequences(Sequence sequence) {
		return sequence.getTaskSequences();
	}
		
	public Collection<ActionDefinition> getActionDefs(TaskDefinition td) {
		return td.getActionDefs();
	}
	
    public TaskGroupDefinition getGroupDefinition(TaskDefinition td) {
		return td.getTaskGroupDef();
	}
	
	public ObjectDefinition getObjectDefinition(TaskDefinition td) {
		return td.getObjectDefinition();
	}
	
	public ObjectDefinition getObjectDefinition(TaskGroupDefinition tgd) {
		return tgd.getObjectDefinition();
	}
	
	/*public Party getParty(Organization org) {
		manager.merge(org);
		Party party = org.getParty();
		toString(party);
		return org.getParty();
	}
	
	public Address getAddress(Party party) {
		return party.getAddress();
	}
	
	public Party getParty(Person person) {
		manager.merge(person);
		Party party = person.getParty();
		toString(party);
		return party;
	}
	
	public User getUser(Person person){
		return person.getUser();
	}*/
	
	public Set<User> getUsers(Role role){
		return role.getUsers();
	}
	
	/**
     * Sync up properties with the ActionDefinition
     * Should be called of the ActionDefinition change
     */
	public void synchronize(Action action){
		if(Constants.CUSTOMIZED_YES.equals(action.getCustom()))
            return; // custom instances are not logically linked to their definitions

        ActionDefinition ad = getActionDefinition(action);
        action.setVerb(ad.getVerb());
        action.setName(ad.getName());
        action.setModifier(ad.getModifier());
        action.setSequence(ad.getSequence());
    }
	
	/**
     * Sync up properties with the TaskDefinition
     * Should be called of the TaskDefinition change
     */
    public void synchronize(Task task){
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        if(Constants.CUSTOMIZED_YES.equals(task.getCustom()))
            return; // custom instances are not logically linked to their definitions

        logger.debug("synchronize Task " + task.getId());

        TaskDefinition td = csf.getTaskDefinition(task);
        /** andrei 03/09/05 commenting as v1 to v2 migration shows massive differecnes btw objectDefPlan and Task plans
         * causing 90% of tasks become custom. Plan is no longer customizable property per Garry/Douglas input
        setPlan(td.getObjectDefinition().getPlan());
         */
        task.setEstTime(td.getEstTime());
        //setStartDate(td.getStartDate()); not in CustomizationVisitor
        //setRunHoursThresh(td.getRunHoursThresh()); is not TaskDefinition property
        task.setRunHoursThreshInc(td.getRunHoursThreshInc());

        //if task type is changing need to check if RoutineType which has special treatment
        if(!EqualsUtils.areEqual(td.getTaskTypeId(), task.getTaskTypeId())){
            //task type property is changed
        	task.setTaskTypeId(td.getTaskTypeId());
        }

        task.setPriorityId(td.getPriorityId());
        task.setSkillTypeId(td.getSkillTypeId());
        task.setSkillLevelId(td.getSkillLevelId());
        task.setNumberOfWorkers(td.getNumberOfWorkers());
        task.setFreqMonths(td.getFreqMonths());
        task.setFreqDays(td.getFreqDays());
        task.setFrequencyId(td.getFrequencyId());
        task.setTaskDescription(td.getTaskDescription());
        task.setExpiryTypeId(td.getExpiryTypeId());
        task.setExpiryNumOfDays(td.getExpiryNumOfDays());
        
        //The new task should take the object's organization
        ObjectEntity thisObj = task.getObject();
        task.setOrganizationId(thisObj.getOrganizationId());
    }
    
    /**
     * Non-Custom tasks follow their definitions as they change group memebership
     * Find task group for object the current task belongs to and if available move the task into the task group
     */
    public void synchronizeGroupMembership(Task task, Collection<TaskGroup> taskGroups){
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        if(Constants.CUSTOMIZED_YES.equals(task.getCustom()))
            return; // custom instances are not logically linked to their definitions
        logger.debug("in synchronizeGroupMembership for Task " + task.getTaskDescription() + " id="+ task.getId());
        TaskDefinition td = csf.getTaskDefinition(task);
        if(td.getTaskGroupDef()==null) {
        	task.setTaskGroup(null);
        }else{
        	Long taskGroupDefinitionId  = td.getTaskGroupDef().getId();
            for (TaskGroup taskGroup : taskGroups) {
                if (taskGroupDefinitionId.equals(taskGroup.getTaskGroupDef().getId())) {
                	task.setTaskGroup(taskGroup);
                    break;
                }
            }
        }
    }
    
    /**
     * Activate/deactivate task
     * Task is active if startdate is set (see ejbStore)
     * RoutineMaint task can not be activated by this method
     * Start date must be set manually by users for RoutineMaint type
     * @param status
     * @deprecated
     */
    public void processStatusUpdate(Task task, Integer status){
        /*if(EqualsUtils.areEqual(status,getActive())){
            return;       // status is not changing, do nothing
        }else if(EqualsUtils.areEqual(Constants.STATUS_ACTIVE,status)){
            makeActive();
        }else{
            setStartDate(null); //see ejbstore for details on seting  active status
            setActive(Constants.STATUS_NOT_ACTIVE);
        }


        Integer routineTask = TaskTypeRef.getIdByCode(TaskTypeRef.ROUTINE_MAINT);
        if(Constants.STATUS_ACTIVE.equals(status) && !routineTask.equals(getTaskTypeId())){
            //set startdate if not set already
            if(getStartDate()== null) setStartDate(new java.sql.Date(System.currentTimeMillis()));
        }else{
            setStartDate(null);
        }
        setActive(status);
        */
    }
    
    /**
     * RoutineMaint task can not be activated by default
     * Start date must be set manually by users for RoutineMaint type
     * @deprecated
     */
    public void makeActive(Task task){
        /*Integer routineTask = TaskTypeRef.getIdByCode(TaskTypeRef.ROUTINE_MAINT);
        if(!EqualsUtils.areEqual(routineTask,getTaskTypeId())){
            setStartDate(new java.sql.Date(System.currentTimeMillis()));
            setActive(Constants.STATUS_ACTIVE);
        }*/
    }
    
    /**
     * Creates Action off ActionDefinition and adds to this Task
     * @param actionDef
     * @throws CreateException
     */
    public void inherit(Task task, ActionDefinition actionDef) {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        Action action = new Action(actionDef);
        //action.setSequence(ad.getSequence());
		//action.setActionDefId(ad.getActionDefId());
        action.setCustom(Constants.CUSTOMIZED_NO);
        action.setTask(task);
        policy.create(action);
    }
    
    public void validateTask(Task vo) throws BusinessException  {
        TaskPropertiesValidator.validateTaskType(vo);
        TaskPropertiesValidator.validateStartDate(vo);
        TaskPropertiesValidator.validateEstimatedTime(vo);
    }
    
    public void signUser(JobSchedule js)    {
        js.setUser(getCallerName());
    }
    
    private String getCallerName(){
    	return getCallerPrincipal().getName();
    }
    
    private String toString(Party party){
		StringBuffer sb = new StringBuffer();
        sb.append("\n").append(this.getClass().getName());
        sb.append("\n\tid=").append(party.getId());
    	
        sb.append("\n\temail=").append(party.getEmail());
        sb.append("\n\turl=").append(party.getUrl());
        sb.append("\n\tname=").append(party.getName());
        sb.append("\n\tfax=").append(party.getFax());
        sb.append("\n\tzip=").append(party.getPhone());
        sb.append("\n\tlocatorId=").append(party.getLocatorId());
        return sb.toString();
    }
    
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
    public void updateTime(int time,Long jobTaskId,Long jobId,Collection<WorkSchedule> workSchedules)
		throws Exception
	{
		logger.debug("Updating Job task time");
		PolicySF policy = ServiceLocator.getPolicySFLocal();
		ControlSF control = ServiceLocator.getControlSFLocal();
        Long jttId = getJobTaskTime(jobTaskId,workSchedules);
		logger.debug("jobtasktime = "+ jttId);
		if (jttId!=null) {
			JobTaskTime jtt = policy.get(JobTaskTime.class, jttId);
			jtt.setTime(time);
		}
		else { //insert new job task time instance
			Long jsId = getJobSchedule(jobId,workSchedules);
			logger.debug("job schedule id ="+ jsId);
			if (jsId!=null) {
				JobTaskTime jttNew = new JobTaskTime();
				JobTask jobTask = new JobTask(jobTaskId);
				jttNew.setJobTask(jobTask);
				jttNew.setTime(time);
				jttNew.setJobScheduleId(jsId);
				policy.create(jttNew);
			}
		}

		if(time > 0){
	        //andrei: 11/09/06: set total time for job task
	        JobTask jt = policy.get(JobTask.class, jobTaskId);
	        jt.setTotalTime(control.getTotalTimeByJobTaskId(jt.getId()));
	        //andrei added:10/10/07
	        Job job = jt.getJob();
	        job.setTotalTime(control.getTotalTimeByJobId(job.getId()));
	        
	        update(jt);
	        update(job);
		}
    }

   private static synchronized Long getJobSchedule(Long jobId, Collection<WorkSchedule> workSchedules)
	throws Exception
	{
		// We must use a collection of work schedules, b/c a worker can
		// have more than one work schedule per day. So we take the list
		// in order to find out which work schedule contains our job id.
		List l = DBAccess.execute(
			" select js.id "+
			" from job_schedule js "+
			" where js.job_id = "+ jobId +
			" and js.work_schedule_id in ( "+
			ConvertUtils.stringify(workSchedules) +
			" ) " , true );
	
		if (l.size()>0) {
			Map m = (Map)l.iterator().next();
			return(Long)m.get("ID");
		}
		return null;
	}

   	private static synchronized Long getJobTaskTime(Long jobTaskId, Collection<WorkSchedule> workSchedules)
		throws Exception
	{
   		// We must use a collection of work schedules, b/c a worker can
		// have more than one per day. So we take the list in order to find
		// out which work schedule contains the job schedule to which our
		// job task belongs.
		String s =
			" select jtt.id " +
			" from job_task_time jtt, job_schedule js "+
			" where jtt.job_schedule_id = js.id "+
			" and jtt.job_task_id = "+ jobTaskId +
			" and js.work_schedule_id in ( "+
			ConvertUtils.stringify(workSchedules) +
			" ) ";
		logger.debug(s);
      List l = DBAccess.execute(s,true);
		if (l.size()>0) {
			Map m = (Map)l.iterator().next();
			logger.debug(m.toString());
			logger.debug(m.get("ID").getClass().getSimpleName());
			return(Long)m.get("ID");
		}
		return null;
	}
   	
   	public void validateLocator(Locator vo) throws BusinessException{
	    if(vo.getParentId()==null || CopyUtils.isNullAlias(vo.getParentId())){
	        vo.setSchemaId(Long.parseLong(Config.getProperty(Config.LOCATOR_SCHEMA_TOP)));
	    }else{
	        Long parentSchemaId = (long)LocatorRef.getSchemaId(vo.getParentId());
	        if(parentSchemaId!=null){ //increment schema
	            vo.setSchemaId(parentSchemaId+1);
	        }
	    }
	    if(LocatorSchemaRef.schemaIdBottom < vo.getSchemaId()){
	        String msg = LocatorSchemaRef.getLabel(LocatorSchemaRef.schemaIdBottom) + " level locator cannot be assigned as parent!";
	        throwBusinessException(msg);
	    }
	    if(Long.valueOf(vo.getId())==(vo.getParentId())){
	        String msg = "Locator cannot be parent to itself!";
	        throwBusinessException(msg);
	    }
	}
   	
   	public void validateObjectClassification(ObjectClassification vo) throws BusinessException{
        if(vo.getParentId()==null || CopyUtils.isNullAlias(vo.getParentId())){
            vo.setSchemaId(Integer.parseInt(Config.getProperty(Config.CLASSFICATION_SCHEMA_TOP)));
        }else{
            Integer parentSchemaId = ObjectClassificationRef.getSchemaId(vo.getParentId());
            if(parentSchemaId!=null){ //increment schema
                vo.setSchemaId(parentSchemaId+1);
            }
        }
        if(ObjectClassSchemaRef.schemaIdBottom < vo.getSchemaId()){
            String msg = ObjectClassSchemaRef.getLabel(ObjectClassSchemaRef.schemaIdBottom) + " level ObjectClass cannot be assigned as parent!";
            throwBusinessException(msg);
        }
        if(Long.valueOf(vo.getId())==(vo.getParentId())){
    	    String msg = "ObjectClassification cannot be parent to itself!";
            throwBusinessException(msg);
        }
    }
   	
   	public void removeWorkerSkillGivenSkillType(long skillTypeId){
   		Collection<Skill> skills = getWorkerSkillGivenSkillType(skillTypeId);
   		if( skills!=null && !skills.isEmpty()){
   			for(Skill skill: skills){
   				remove(skill);
   			}
   		}
   	}
   	
   	public Collection<Skill> getWorkerSkillGivenSkillType(long skillTypeId){
   		return getData(Skill.class, "skillTypeId", skillTypeId);	
   	}
   	
   	public void validateOrganization(Organization vo) throws BusinessException{
	    if(vo.getParentId()==null || CopyUtils.isNullAlias(vo.getParentId())){
	        vo.setSchemaId(OrganizationSchemaRef.schemaIdTop);
	    }else{
	        Long parentSchemaId = (long)OrganizationRef.getSchemaId(vo.getParentId());
	        if(parentSchemaId!=null){ //increment schema
	            vo.setSchemaId(parentSchemaId+1);
	        }
	    }
	    if(OrganizationSchemaRef.schemaIdBottom < vo.getSchemaId()){
	        String msg = OrganizationSchemaRef.getLabel(OrganizationSchemaRef.schemaIdBottom) + " level org cannot be assigned as parent!";
	        throwBusinessException(msg);
	    }
	    if(Long.valueOf(vo.getId())==(vo.getParentId())){
	        String msg = "Org cannot be parent to itself!";
	        throwBusinessException(msg);
	    }
	}
}