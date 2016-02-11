package org.mlink.iwm.rules;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.SimpleListDAO;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Role;
import org.mlink.iwm.entity3.Skill;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.DBAccess;

/**
 * Date: Dec 25, 2004
 * This class localizes busines rules for deletion of various business objects
 * If object can be deleted it gets deleted/hidden in according to bussiness rules that might include deleting/hiding other dependent objects,
 * if not then business exception is thrown
 *
 */
public class DeletionVisitor {
    private static DeletionRulesQueryFactory sqlFactory = new DeletionRulesQueryFactory();
    private static final Logger logger = Logger.getLogger(DeletionVisitor.class);

    private static  final int ACTIVE_JOBS_QUERY_TYPE    =   100;
    private static  final int TERMINAL_JOBS_QUERY_TYPE  =   101;
    private static  final int LOCATOR_QUERY_TYPE  =   102;
    private static  final int ACTION        =   1;
    private static  final int ACTION_DEF    =   2;
    private static  final int TASK          =   3;
    private static  final int TASK_DEF      =   4;
    private static  final int OBJECT_ENTITY =   5;
    private static  final int JOB           =   6;
    private static  final int PERSON        =   7;
    private static  final int LOCATOR_JOB    =   8;
    private static  final int LOCATOR_PARTY  =   9;
    private static  final int LOCATOR_LOCATOR  =   10;
    private static  final int LOCATOR_OBJECT  =   11;
    private static  final int LOCATOR_TENANT_REQUEST  =   12;

    // set to true if delete/update triggers are enabled
    private static boolean triggersEnabled = false;

    /**
     * Performs Deletion rules check and delete Action if allowed
     * 1.only stand-alone action can be deleted
     * 2.last action for a task  can not be deleted
     * 3.archive if  linked to an active job
     * 4.archive if  linked to a terminal job
     * 5.delete otherwise
     * @param action
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(Action action) throws BusinessException,RemoveException,SQLException{
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        if(!Constants.CUSTOMIZED_YES.equals(action.getCustom()) && !Constants.CUSTOMIZED_YES.equals(action.getTask().getCustom())){
            throw new BusinessException("Action is not stand-alone and cannot be deleted!");
        }

        //check if action is the last one for the Task then cannot delete
        if(csf.getActions(policy.getTask(action)).size() == 1){
            throw new BusinessException("Action cannot be deleted. Task must have at least one action");
        }

        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,ACTION), action.getId()));

        if(result.size()!=0){
        	logger.debug("Action is archived " + action.getId());
            action.setArchivedDate(getToday());
            policy.update(action);
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,ACTION), action.getId()));
        if(result.size()!=0){
            logger.debug("Action is archived " + action.getId());
            action.setArchivedDate(getToday());
            policy.update(action);
        }else{
            logger.debug("Action is deleted " + action.getId());
            Action bean = new Action();
            bean.setId(action.getId());
            policy.remove(bean);
        }
    }

    /**
     * Performs Deletion rules check and delete Task if allowed
     * 1.only stand-alone task can be deleted
     * 2.archive if  linked to an active job
     * 3.archive if  linked to a terminal job
     * 4.delete otherwise
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(Task bean)        throws BusinessException,RemoveException,SQLException{
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        if(bean.getTaskDefinition()!=null){
            throw new BusinessException("Task is not stand-alone and cannot be deleted");
        }
        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,TASK),bean.getId()));
        if(result.size()!=0){
        	logger.debug("Task  is archived " + bean.getId());
            bean.setArchivedDate(getToday());
            csf.update(bean);

            if(!triggersEnabled){
                //actions are cascade archived
                for (Action o : csf.getActions(bean)) {
                    o.setArchivedDate(getToday());
                    csf.update(o);
                }
        }
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,TASK),bean.getId()));
        if(result.size()!=0){
            logger.debug("Task  is archived " + bean.getId());
            bean.setArchivedDate(getToday());
            csf.update(bean);

            if(!triggersEnabled){
                //actions are cascade archived
                for (Action o : csf.getActions(bean)) {
                    o.setArchivedDate(getToday());
                    csf.update(o);
                }
            }

        }else{
            ObjectEntity object = csf.getObject(bean);
            logger.debug("Task  is deleted " + bean.getId());
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            policy.remove(bean);  //actions are cascade deleted, see dtd settings
                                //trigger not required as the expected ejb instance count is manageable (<1000)
            object.updateCustom();
        }
    }

    /**
     * Object Data,if stand-alone, can always be deleted
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     */
    public static void delete(ObjectData bean)  throws BusinessException,RemoveException{
        if(bean.getObjectDataDef()!=null){
            throw new BusinessException("Data is not stand-alone and cannot be deleted");
        }else{
            logger.debug("ObjectData  is archived " + bean.getId());
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            policy.remove(bean);
        }
    }

    /**
     * If TaskGroup is stand-alone then delete it, overwise deletion is not allowed
     * @param bean
     * @throws RemoveException
     */
    public static void delete(TaskGroup bean)   throws RemoveException,BusinessException{
        if(bean.getTaskGroupDef()==null){
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            policy.remove(bean);
            logger.debug("TaskGroup  is deleted " + bean.getId());
        }else{
            throw new BusinessException("Deletion is not allowed as TaskGroup is not stand alone");
        }
    }


    /**
     * Performs Deletion rules check and delete/archive Object if allowed
     * Dependents also deleted/archived where applicable
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(ObjectEntity bean) throws BusinessException,RemoveException,SQLException{
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        
        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,OBJECT_ENTITY),bean.getId()));
        if(result.size()!=0){
            throw new BusinessException("Object linked to active job(s) and cannot be deleted " + format(result));
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,OBJECT_ENTITY),bean.getId()));
        if(result.size()!=0){
            Timestamp today = getToday();
            logger.debug("Object  is archived " + bean.getId());
            bean.setActive(0); //Chris added this to make sure archived objects are marked inactive.
            bean.setArchivedDate(today); //db trigger set archived_date on Task,Action records (Data and Task Groups are not required
                                         // as they do not have archived_date because do not need it
            csf.update(bean);
            
            if(!triggersEnabled){
                Collection<Task> tasks = isf.getTasks(bean);
                Task task;
                Action action;
                for (Object task1 : tasks) {
                    task = (Task) task1;
                    task.setActive(0); //Make sure the tasks are inactivated too.
                    task.setArchivedDate(today);
                    csf.update(task);
                    
                    for (Object o : csf.getActions(task)) {
                        action = ((Action) o);
                        action.setArchivedDate(today);
                        csf.update(action);
                    }
                }
            }
        }else{
            logger.debug("Object  is deleted " + bean.getId());
            csf.remove(bean);  //will cascade Task,Actions,Data and TaskGroup via dtd setttings
                            //no trigger required
        }

    }

    /**
     * Delete operation applied to a job never deletes it. Job can only be archived if in a terminal state
     * @param bean
     * @throws BusinessException
     * @throws SQLException
     */
    public static void delete(Job bean)   throws BusinessException, SQLException{
        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,JOB),bean.getId()));
        if(result.size()!=0){
            throw new BusinessException("Job is active and cannot be deleted " + format(result));
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,JOB),bean.getId()));
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        if(result.size()!=0){
            bean.setArchivedDate(getToday());
            logger.debug("Job is archived " + bean.getId());
            policy.update(bean);
        }else{
        	policy.remove(bean);
        	logger.debug("Job is deleted " + bean.getId());
        }
    }
    /**
     * project deletion depends on deletion rules of jobs contained within
     * @param project
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(Project project)     throws BusinessException,RemoveException,SQLException{
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        Collection<Job> jobs = csf.getJobs(project);
        if(jobs.size()==0){
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            policy.remove(project);
            logger.debug("Project  is deleted " + project.getId());
        }else{
	        //try to delete (current implementation will try to archive them) each job
	        for (Job job: jobs) {
	            delete(job);
	        }
	
	        // if jobs deletion succeded (we got to this point withou exception)
	        //archive the project
	        project.setArchivedDate(getToday());
	        logger.debug("Project  is archived " + project.getId());
        }
    }

    /**
     * Performs Deletion rules check and delete ActionDefinition if allowed
     * 1.last action for a task  can not be deleted
     * 2.archive if  linked to an active job
     * 3.archive if  linked to a terminal job
     * 4.delete otherwise
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(ActionDefinition bean)    throws BusinessException,FinderException,RemoveException,SQLException{
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        
        //need to check if action is the last one for the TaskDef then cannot delete.
        TaskDefinition td = policy.getTaskDefinition(bean);
        Collection<ActionDefinition> actionDefs = policy.getActionDefs(td);
        if(actionDefs.size()==1){
            throw new BusinessException("Action Definition cannot be deleted. Task must have at least one action");
        }

        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,ACTION_DEF),bean.getId()));

        if(result.size()!=0){
        	Timestamp today = getToday();
            logger.debug("ActionDef is archived " + bean.getId());
            bean.setArchivedDate(today);
            if(!triggersEnabled){
            	Collection<Action> col = policy.findActionByActionDefinition(bean.getId());
                for (Action action : col) {
                    action.setArchivedDate(today);
                    policy.update(action);
                }
            }
            policy.update(bean);
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,ACTION_DEF),bean.getId()));
        if(result.size()!=0){
            Timestamp today = getToday();
            logger.debug("ActionDef is archived " + bean.getId());
            bean.setArchivedDate(today);
            if(!triggersEnabled){
            	Collection<Action> col = policy.findActionByActionDefinition(bean.getId());
                for (Action action : col) {
                    action.setArchivedDate(today);
                    policy.update(action);
                }
            }
            policy.update(bean);
        }else{
            if(!triggersEnabled){   //CASCADE DELETE on ACTION_DEF table to take care of deleting all ACTION records
                Collection<Action> col = policy.findActionByActionDefinition(bean.getId());
                for (Action action : col) {
                    policy.remove(action);
                }
            }
            logger.debug("ActionDef is deleted " + bean.getId());
            bean.setTaskDefinition(null);
            bean.setActions(null);
            policy.remove(bean);
        }
    }

    /**
     * Performs Deletion rules check and delete TaskDefinition if allowed
     * 1.archive if  linked to an active job
     * 2.archive if  linked to a terminal job
     * 3.delete otherwise
     * 4. cascade update or delete to children (impelemented on the db side)
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(TaskDefinition bean)      throws BusinessException,RemoveException,SQLException{
        List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,TASK_DEF),bean.getId()));
        Timestamp today = getToday();
        
        if(result.size()!=0){
        	
            logger.debug("TaskDefinition is archived " + bean.getId());
            bean.setArchivedDate(today);   //db Trigger should also cascade ACTION_DEF, TASK, ACTION records
            if(!triggersEnabled){
                DBAccess.executeUpdate("UPDATE ACTION_DEF SET ARCHIVED_DATE=SYSDATE WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("UPDATE TASK SET ARCHIVED_DATE=SYSDATE, ACTIVE=0 WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("UPDATE ACTION A SET ARCHIVED_DATE=SYSDATE WHERE  EXISTS ( SELECT 1 FROM TASK T WHERE T.TASK_DEF_ID=" + bean.getId() + " AND T.ID=A.TASK_ID )");
        }
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,TASK_DEF),bean.getId()));
        if(result.size()!=0){
            
            logger.debug("TaskDefinition is archived " + bean.getId());
            bean.setArchivedDate(today);   //db Trigger should also cascade ACTION_DEF, TASK, ACTION records
            if(!triggersEnabled){
                DBAccess.executeUpdate("UPDATE ACTION_DEF SET ARCHIVED_DATE=SYSDATE WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("UPDATE TASK SET ARCHIVED_DATE=SYSDATE, ACTIVE=0 WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("UPDATE ACTION A SET ARCHIVED_DATE=SYSDATE WHERE  EXISTS ( SELECT 1 FROM TASK T WHERE T.TASK_DEF_ID=" + bean.getId() + " AND T.ID=A.TASK_ID )");
            }

        }else{
            logger.debug("TaskDefinition is deleted " + bean.getId());
            if(!triggersEnabled){  //db Trigger should also cascade ACTION_DEF, TASK, ACTION records
                DBAccess.executeUpdate("DELETE FROM ACTION_DEF WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("DELETE FROM TASK_SEQUENCE A WHERE  EXISTS ( SELECT 1 FROM TASK T WHERE T.TASK_DEF_ID=" + bean.getId() + " AND T.ID=A.TASK_ID )");                
                DBAccess.executeUpdate("DELETE FROM TASK WHERE TASK_DEF_ID="+bean.getId());
                DBAccess.executeUpdate("DELETE FROM ACTION A WHERE  EXISTS ( SELECT 1 FROM TASK T WHERE T.TASK_DEF_ID=" + bean.getId() + " AND T.ID=A.TASK_ID )");
            }
            PolicySF policy = ServiceLocator.getPolicySFLocal();
            policy.remove(bean);
        }
        
    }

    /**
     * Remove TaskGroupDefinition and all dependent TaskGroups
     * TaskGroups are always removed when TaskGroupDefinition is removed. No restrictions
     * @param definition
     * @throws SQLException
     */
    public static void delete(TaskGroupDefinition definition) throws SQLException{
        /**if(!triggersEnabled){   //CASCADE DELETE on TASK_GROUP_DEF table to take care of deleting all TASK_GROUP records
            Collection<TaskGroup> col = definition.getTaskGroups();
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                TaskGroup group = (TaskGroup) iterator.next();
                iterator.remove();
                group.remove();
            }
        }
        definition.remove();        TO MUCH ejb OVERHEAD HERE, SWITCHING TO JDBC */
        Long id= definition.getId();
        DBAccess.executeUpdate("UPDATE TASK T SET GROUP_ID=NULL WHERE EXISTS (SELECT 1 FROM TASK_GROUP G WHERE G.ID=T.GROUP_ID AND G.TASK_GROUP_DEF_ID="+definition.getId()+")");
        DBAccess.executeUpdate("DELETE FROM TASK_GROUP G WHERE G.TASK_GROUP_DEF_ID="+definition.getId());
        DBAccess.executeUpdate("UPDATE TASK_DEF T SET GROUP_ID=NULL WHERE T.GROUP_ID="+definition.getId());        
        DBAccess.executeUpdate("DELETE FROM TASK_GROUP_DEF G WHERE G.ID="+definition.getId());
        logger.debug("TaskGroupDef is deleted " + id);
    }

    /**
     * ObjectDataDefinition can always be deleted
     * @param bean
     * @throws RemoveException
     */
    public static void delete(ObjectDataDefinition bean)    throws RemoveException{
        logger.debug("ObjectDataDefinition is deleted " + bean.getObjectDefinition());
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        
        if(!triggersEnabled){//CASCADE DELETE on Object_Data_Def table to take care of deleting all OBJECT_DATA records
            Collection<ObjectData> col;
            col = policy.findObjectDataByDataDefinition(bean.getId());
			for (ObjectData od: col) {
			    policy.remove(od);
			}
        }
        policy.remove(bean);
    }

    /**
     * Performs Deletion rules check and delete Locator if allowed
     * 1.protect if  linked to an active job or party or object
     * 2.archive if  linked to a terminal job
     * 3.archive if  linked to a tenant request job
     * 3.delete otherwise
     * @param bean
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static void delete(Locator bean)                 throws BusinessException,RemoveException,SQLException{
    	List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(LOCATOR_QUERY_TYPE,LOCATOR_LOCATOR),bean.getId()));
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        
        if(result.size()!=0){
            throw new BusinessException("Locator " + bean.getFullLocator() + " is a parent to other locators and cannot be deleted " + format(result));
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(LOCATOR_QUERY_TYPE,LOCATOR_PARTY),bean.getId()));

        if(result.size()!=0){
            throw new BusinessException("Locator " + bean.getFullLocator() + " linked to party(s) and cannot be deleted " + format(result));
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(LOCATOR_QUERY_TYPE,LOCATOR_OBJECT),bean.getId()));

        if(result.size()!=0){
            throw new BusinessException("Locator " + bean.getFullLocator() + " linked to object(s) and cannot be deleted " + format(result));
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,LOCATOR_JOB),bean.getId()));

        if(result.size()!=0){
            throw new BusinessException("Locator " + bean.getFullLocator() + " linked to active job(s) and cannot be deleted " + format(result));
        }
        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,LOCATOR_JOB),bean.getId()));
        if(result.size()!=0){
            Timestamp today = getToday();
            logger.debug("Locator is archived " + bean.getFullLocator());
            bean.setArchivedDate(today);
            //see DB CASCADE archive ON WORK SCHEDULES
            return;
        }

        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(LOCATOR_QUERY_TYPE,LOCATOR_TENANT_REQUEST),bean.getId()));
        if(result.size()!=0){
            Timestamp today = getToday();
            logger.debug("Locator is archived " + bean.getFullLocator());
            bean.setArchivedDate(today);
            //see DB CASCADE archive ON WORK SCHEDULES
            return;
        }

        logger.debug("Locator is deleted " + bean.getFullLocator());
        policy.remove(bean);
        //LocatorData cascaded , see ejb-jar.xml
        //see DB CASCADE DELETE ON WORK SCHEDULES
    }

    /**
     * @since v35
     * 1.Workschedules cannot be deleted in the past
     * 2.Workschedules with jobs cannot be deleted
     * @param ws
     * @throws BusinessException
     */
    public static void delete(WorkSchedule ws)   throws BusinessException{
        if(ws.getDay().before(DateUtils.truncate(new Date(),Calendar.DATE))){
		    throw new BusinessException("Cannot remove schedule for a past date " +  ws.getDay());
		}
		PolicySF policy = ServiceLocator.getPolicySFLocal();
		if(policy.findJobScheduleByWorkSchedule(ws.getId()).size()>0){
		    throw new BusinessException("Jobs have already been schedulled. Cannot be deleted!");
		}
		policy.remove(ws);
    }


    /**
     * @since v35
     * @param js
     * @throws BusinessException
     */
    public static void delete(JobSchedule js) throws BusinessException{
        try {
        	PolicySF policy = ServiceLocator.getPolicySFLocal();
    		Job job = policy.get(Job.class, js.getJobId());
            if (JobStatusRef.getIdByCode(JobStatusRef.Status.DUN)==job.getStatusId()) {
            	//if DUN dont change it.
                return;
            }
            
            WorkSchedule ws = policy.get(WorkSchedule.class, js.getWorkScheduleId());
            logger.debug("Removing job schedule where jobid="+js.getJobId()+
                    " personid="+ws.getPersonId()+ " locatorid="+ws.getLocatorId()+ " date="+ ws.getDay());

            if(ws.getDay().before(DateUtils.truncate(new Date(),Calendar.DATE))){
                throw new BusinessException("Cannot remove schedule for a past date " +  ws.getDay());
            }
            if (policy.hasTimeRecorded(js.getId())) {
                //"Job schedule has already recorded time and cannot be removed"--no changes here
            }else{
                removeJobTaskTimes(js.getId());
                removeJobSchedule(js.getId());
            }
            
            // per Mike 08/20/07 the status of jobs with scheduled date set by JSM
            // todo on-demand call to JSM is desirable job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.RFS));
            job.setScheduledDate(null);
            policy.update(job);
        } catch (FinderException e) {
            throw new IWMException(e);
        } catch (RemoveException e) {
            throw new IWMException(e);
        }
    }
   
    private static Timestamp getToday(){
        return new Timestamp(System.currentTimeMillis());
    }

    private static int maxMessageCount=10;
    private static String format(List messages){
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < (maxMessageCount<messages.size()?maxMessageCount:messages.size()); i++) {
            Object o =  messages.get(i);
            sb.append("\n"+o.toString());
        }
        sb.append(maxMessageCount<messages.size()?"...":"");
        return sb.toString();
    }

    /**
     * SQL query factory to support outer class
     */
    static class DeletionRulesQueryFactory{
        String prepareSql(int queryType,int entityType){
            String sql;
            switch(queryType){
                case ACTIVE_JOBS_QUERY_TYPE:
                    sql = "SELECT ID JOB, PROJECT_ID PROJECT,STATE FROM ACTIVE_JOBS_VIEW AJ ";
                    break;
                case TERMINAL_JOBS_QUERY_TYPE:
                    sql = "SELECT ID JOB, PROJECT_ID PROJECT,STATE FROM TERMINAL_JOBS_VIEW AJ ";
                    break;
                case LOCATOR_QUERY_TYPE:
                    sql = "";
                    break;
                default:
                    throw new IWMException("wrong queryType type");

            }
            switch(entityType){
                case TASK:
                    sql = sql + ", (SELECT JOB_ID FROM JOB_TASK WHERE TASK_ID=?) JT WHERE JT.JOB_ID=AJ.ID";
                    break;
                case ACTION:
                    sql = sql + ", (SELECT JOB_ID FROM JOB_TASK, JOB_ACTION WHERE JOB_TASK.ID=JOB_ACTION.JOB_TASK_ID AND ACTION_ID=?) JT " +
                                    "WHERE JT.JOB_ID=AJ.ID";
                    break;
                case ACTION_DEF:
                    sql = sql + ", (SELECT JOB_ID FROM JOB_TASK, JOB_ACTION, ACTION WHERE JOB_TASK.ID=JOB_ACTION.JOB_TASK_ID AND ACTION.ID=JOB_ACTION.ACTION_ID AND ACTION.ACTION_DEF_ID=?) JT " +
                                    " WHERE JT.JOB_ID=AJ.ID";
                    break;
                case TASK_DEF:
                    sql = sql + ", (SELECT JOB_ID FROM JOB_TASK, TASK WHERE JOB_TASK.TASK_ID=TASK.ID AND TASK.TASK_DEF_ID=?) JT " +
                                    " WHERE JT.JOB_ID=AJ.ID";
                    break;
                case OBJECT_ENTITY:
                    sql = sql + " WHERE AJ.OBJECT_ID=?";
                    break;
                case JOB:
                    sql = sql + " WHERE AJ.ID=?";
                    break;
                case PERSON:
                    sql = sql + ", (SELECT DISTINCT JOB_ID FROM JOB_SCHEDULE, WORK_SCHEDULE WHERE WORK_SCHEDULE.PERSON_ID=? AND WORK_SCHEDULE.ID=JOB_SCHEDULE.WORK_SCHEDULE_ID) JT WHERE JT.JOB_ID=AJ.ID";
                    break;
                case LOCATOR_PARTY:
                    sql = "select name from party where locator_id=?";
                    break;
                case LOCATOR_OBJECT:
                    sql = "SELECT OBJECT_REF || TAG FROM OBJECT WHERE LOCATOR_ID=?";
                    break;
                case LOCATOR_TENANT_REQUEST:
                    sql = "SELECT ID FROM TENANT_REQUEST_UNIQUE WHERE LOCATOR_ID=?";
                    break;
                 case LOCATOR_JOB:
                    sql = sql + ", (SELECT DISTINCT JOB_ID FROM JOB_SCHEDULE, WORK_SCHEDULE WHERE WORK_SCHEDULE.LOCATOR_ID=? AND WORK_SCHEDULE.ID=JOB_SCHEDULE.WORK_SCHEDULE_ID) JT WHERE JT.JOB_ID=AJ.ID";
                    break;
                 case LOCATOR_LOCATOR:
                    sql = "select full_locator from locator where parent_id=?";
                    break;
                default:
                    throw new IWMException("wrong entity type");

            }
            return sql;
        }

    }
    
    /**
     * Bulk remove Job Task Times  -- note this method does NOT check
     * to see that the time attribute is not null or zero
     * You must check using hasTimeRecorded before calling this, or
     * you'll blow away some captured values
     * We don't do the check here because in some instances you might
     * *want* to remove all Job Task Time objects.
     */
    public static void removeJobTaskTimes(Long jobScheduleId)
            throws FinderException, RemoveException
    {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<JobTaskTime> col = policy.findJobTaskTimeByJobSchedule(jobScheduleId);
        for (JobTaskTime jtt :col){
        	policy.remove(jtt);
        }
    }
    
    /**
     * Remove JobSchedule instance specified by primarykey value
     * @param p0 primarykey value
     */
    public static void removeJobSchedule (java.lang.Long p0)   {
        PolicySF policy = ServiceLocator.getPolicySFLocal();
		JobSchedule js = policy.get(JobSchedule.class, p0);
		policy.signUser(js);
	    js.setDeletedTime(new java.sql.Timestamp(System.currentTimeMillis()));   //no longer removing job schedules but marking as inactive.
		//if(!"true".equals(Config.getProperty(Config.JAVA_AGENTS_ENABLED,"false"))){
			policy.update(js);
		//}
    }
    
    /**
     * Organization can be deleted if no people there
     * @param org
     * @throws BusinessException
     * @throws SQLException 
     */
    public static void delete(Organization org) throws BusinessException, SQLException, FinderException{
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
        Collection<Organization> children = psf.getOrganizationsByParentId(org.getId());
        if(children.size()>0){
        	for (Organization child : children){
        		if (child.getArchivedDate() == null){
        			throw new BusinessException("Organization is a parent to other organizations and cannot be deleted!");
        		}
        	}
           
        }
        
        Collection<Person> people = psf.getPersonsByOrganizationId(org.getId());
        //convert to List to avoid possibility of ConcurremtModException
        if(people.size()>0){
        	for (Person person : people){
        		if (person.getArchivedDate() == null){
        			throw new BusinessException("Organization has workers and cannot be deleted. Move workers out first!");
        		}
        	}
            
        }
        

        // if people deletion succeded (we got to this point without exception)
        //archive the organization
        org.setArchivedDate(getToday());
        psf.update(org);
        logger.debug("Organization is archived " + org.getId());
    }
    
    /**
     * Performs Deletion rules check and delete/archive Person if allowed
     * 1.protect if  linked to an active job
     * 2.archive if  linked to a terminal job
     * 3.delete otherwise
     * @param person
     * @throws BusinessException
     * @throws RemoveException
     * @throws SQLException
     */
    public static boolean delete(Person person) throws BusinessException, SQLException, FinderException {
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	List result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(ACTIVE_JOBS_QUERY_TYPE,PERSON),person.getId()));
        boolean isDeleted = false;
        if(result.size()!=0){
            throw new BusinessException("Person " + person.getParty().getName() + " linked to active job(s) and cannot be deleted " + format(result));
        }
        result = DBAccess.executeDAO(new SimpleListDAO(sqlFactory.prepareSql(TERMINAL_JOBS_QUERY_TYPE,PERSON),person.getId()));
        if(result.size()!=0){
            Timestamp today = getToday();
            logger.debug("Person is archived " + person.getId());
            person.setActive(0); //Chris added this to make sure archived people arn't active
            person.setOrganizationId(null); //Move deleted person out of orgs
            person.setArchivedDate(today);
            //TODO: archive all  WORK SCHEDULES, NYS WS delete them
			psf.update(person);
        }else{
            User user1 = psf.getUserByPersonId(person.getId());
            psf.updatePersonSkills(person.getId(), new ArrayList<Skill>());
            
            User user = psf.getUserNRoles(user1.getId());
            Set<Role> roles = user.getRoles();
    		Set<User> users;
    		User userI;
    		Iterator<User> iterU;
    		if (roles != null) {
    			for(Role role: roles){
    				users = psf.getUsers(role);
	    			iterU = users.iterator();
					while (iterU.hasNext()) {
						userI = iterU.next();
						if (user1.getId() == userI.getId()) {
							iterU.remove();
							break;
						}
					}
    			}
    		}
    		
            psf.remove(person);
            psf.remove(user);
            isDeleted=true;
            //TODO: delete all work schedules
            logger.debug("Person is deleted " + person.getId());
        }

        return isDeleted;
    }
}
