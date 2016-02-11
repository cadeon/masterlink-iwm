package org.mlink.iwm.session;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.mlink.iwm.entity3.BaseEntity;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.DataException;

import org.mlink.iwm.entity3.JobStatusHist;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.access3.ServiceLocator;

/**
 * User: andreipovodyrev Date: Mar 21, 2009
 */
public class CommonSFBean implements CommonSF {
	private static final Logger logger = Logger.getLogger(CommonSFBean.class);

	@Resource 
	private SessionContext sessionContext;

	@PersistenceContext(unitName = "iwm_dpc")
	private EntityManager manager;

	public String echo(String str) {
		logger.info("I heard you said " + str);

		Party party = get(Party.class, 6);
		logger.info(party);
		return str;
	}
	
	/**
	 * Generic method for creating entities with single column long PK
	 * 
	 * @param object
	 * @return
	 */
	public long create(BaseEntity object) {
		object.setId(0);
		logger.info("Creating " + object);manager.persist(object);
		return object.getId();
	}

	/**
	 * Update generic ejb3 object
	 * 
	 * @param object
	 */
	public void update(BaseEntity object) {
		logger.info("Updating " + object);
		//All Entities update through here. As such, this is where the audit trail creation and update rules execute.

		if (object instanceof org.mlink.iwm.entity3.Job)
			{
			//We're going to be comparing new vs old a lot.
			Job newJob = (Job) object;
			ControlSF csf = ServiceLocator.getControlSFLocal();
			Job oldJob = csf.get(Job.class, newJob.getId());
			
			//UPDATE RULE / AUDIT Test for a state change.
			if ( !oldJob.getStatusId().equals(newJob.getStatusId()) ) {
				logger.info("Job Status updated");
				JobStatusHist jsh = new JobStatusHist(newJob.getId(), newJob.getStatusId());
				csf.create(jsh);
			}
			//UPDATE RULE Check for a change in start date. If so, update last planned on associated tasks.
			if (!oldJob.getEarliestStart().equals(newJob.getEarliestStart())){
				logger.info("Job Earliest start updated. Updating Tasks");
				Collection<JobTask> jts = csf.getJobTasks(object.getId());
				for (JobTask jt : jts){
					jt.getTask().setLastPlannedDate(newJob.getEarliestStart());
				}
			}
			//UPDATE RULE Clear sticky if job requirements have changed.
			if (oldJob.getSkillTypeId() != newJob.getSkillTypeId() ||
				oldJob.getSkillLevelId() != newJob.getSkillLevelId() ||
				oldJob.getOrganizationId() != newJob.getOrganizationId()){
				newJob.setSticky("N");
			}
			
			}
		
		//Object-specific update rules go here
		if (object instanceof org.mlink.iwm.entity3.ObjectEntity)
		{
			//It's an object. 
			ControlSF csf = ServiceLocator.getControlSFLocal();
			ObjectEntity oldObject = csf.get(ObjectEntity.class, object.getId());
			ObjectEntity newObject = (ObjectEntity) object;
			
			//Does the old object even have runhours?
			if (oldObject.getRunHours() != null && newObject.getRunHours() != null){
				//Is the runhours recorded lower than they were?
				int diff = oldObject.getRunHours() - newObject.getRunHours();
				if (diff >= 0){ //if the difference is negative, everything's fine. If not, execute:
					Hibernate.initialize(oldObject); //We're going to need the whole thing.
					oldObject.updateTasksThresh(diff);
				}
			}
		}
		
		manager.merge(object);
	}

	/**
	 * Persist generic ejb3 object
	 * 
	 * @param object
	 */
	private void persist(Object object) {
		logger.info("Creating " + object);
		manager.persist(object);
	}
	
	/**
	 * Generic method for removing entities
	 * 
	 * @param object
	 */
	public void remove(BaseEntity object) {
		logger.info("Removing " + object);
		Object mergedObject = manager.merge(object);
		manager.remove(mergedObject);
	}

	/**
	 * Generic method for getting entities with single column long PK
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <K> K get(Class<K> clazz, long id) {
		return manager.find(clazz, id);
	}

	/***
	 * Get Data from db given a fieldname and its req value
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public <K> Collection<K> getData(Class clazz, String fieldName, Object fieldValue) {
		return getData(clazz, null, fieldName, fieldValue);
	}
	
	public <K> Collection<K> getData(Class clazz, String addnwhereClause, String fieldName, Object fieldValue) {
		Map<String, Object> fieldNameValueMap=null;
		if(fieldName!=null){
			fieldNameValueMap = new HashMap<String, Object>();
			fieldNameValueMap.put(fieldName, fieldValue);
		}
		return getData(clazz, addnwhereClause, fieldNameValueMap);
	}

	/***
	 * Get Data from db given a fieldname and its req value
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public <K> Collection<K> getData(Class clazz, String addnwhereClause, Map<String, Object> fieldNameValueMap) {
		return (Collection<K>) createQuery(clazz, addnwhereClause, fieldNameValueMap).getResultList();
	}
	
	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	private Query createQuery(Class clazz, String addnwhereClause,
			Map<String, Object> fieldNameValueMap) {
		StringBuffer sqlBfr = new StringBuffer("select distinct(o) from "
				+ clazz.getName() + " o ");
		if (addnwhereClause != null) {
			sqlBfr.append(" where " + addnwhereClause + " and");
		} else if (fieldNameValueMap != null && !fieldNameValueMap.isEmpty()) {
			sqlBfr.append(" where ");
		}

		Map<String, Object> paramValueMap = new HashMap<String, Object>();
		if (fieldNameValueMap != null && !fieldNameValueMap.isEmpty()) {
			int i = 0;
			for (Map.Entry<String, Object> e : fieldNameValueMap.entrySet()) {
				sqlBfr.append(" o." + e.getKey() + " = :name" + i + " and");
				paramValueMap.put("name" + i, e.getValue());
				i++;
			}
		}
		
		int length = sqlBfr.length();
		int pos = sqlBfr.lastIndexOf("and");
		String sqlStr;
		if(pos+3 == length){
			sqlStr = sqlBfr.substring(0, pos);
		}else{
			sqlStr = sqlBfr.toString();
		}
		Query query = manager.createQuery(sqlStr);
		if (!paramValueMap.isEmpty()) {
			int i = 0;
			for (Map.Entry<String, Object> e : paramValueMap.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		}
		return query;
	}

	public <K> K getUniqueData(Collection<K> lst) throws DataException {
		K be = null;
		String errorStr;
		if (lst.size() > 1) {
			errorStr = "getUniqueData returned " + lst.size()
					+ " records instead of one.";
			logger.error("Error: " + errorStr);
			throw new DataException(errorStr);
		} else if (lst.size() < 1) {
			errorStr = "getUniqueData for returned " + lst.size()
					+ " records instead of one.";
			logger.error("Error: " + errorStr);
			throw new DataException(errorStr);
		} else {
			be = (K) (lst.iterator().next());
		}
		return be;
	}
	
	/**
	 * return all elements in the class
	 * 
	 * @param class
	 */
	public <K> Collection<K> findAll(Class<K> clazz) {
		return getData(clazz, null, null);
	}
	
	protected void throwBusinessException(String message)
			throws BusinessException {
		sessionContext.setRollbackOnly();
		BusinessException be = new org.mlink.iwm.exception.BusinessException(
				message);
		logger.debug("Rollback BusinessException: " + be.getMessage());
		throw be;
	}
	
	public Principal getCallerPrincipal() {
		return sessionContext.getCallerPrincipal();
	}
}
