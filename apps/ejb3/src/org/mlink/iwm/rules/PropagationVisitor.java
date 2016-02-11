package org.mlink.iwm.rules;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.DBAccess;

/**
 * This class localizes busines rules for maintaning propagation of properties
 * between definitions and instances
 * Terms:
 * propagateUpdate is to distribute  a change in a Definition property to all instances
 * propagateCreate   is to distribute a Definition itself when it is created to all object instances
 * User: andrei
 * Date: Feb 6, 2005
 */
public class PropagationVisitor {
    private static final Logger logger = Logger.getLogger(PropagationVisitor.class);


    /**
     * Change in Object Definition propogates to the instance level
     * The only property to propagate is Plan, which is propogated to Tasks (not Objects)
     */
    public static void propagateUpdate(ObjectDefinition definition) {
        //no object property should be updated, but plans for tasks

        /** andrei 03/09/05 commenting as v1 to v2 migration shows massive differecnes btw objectDefPlan and Task plans
         * casusing 90% of tasks become custom. Plan is no longer customizable property per Garry/Douglas input

        Collection<TaskDefinition> taskDefs = definition.getTaskDefs();
        for (TaskDefinition td : taskDefs) {
            TaskDefinition td = (TaskDefinition) iterator.next();
            try{
                Collection<Task> tasks = TaskAccess.getHome().findByTaskDefinition(td.getId());
                for (Task task : tasks) {
                    Task task = (Task) iterator2.next();
                    task.setPlan(definition.getPlan());
                }

            }catch(javax.ejb.FinderException fe){
                //it is ok
            }
        }
         )*/
    }

    /**
     * Propagates properties of definition to all dependent (non-custom) instances
     * @param definition
     */
    public static void propagateUpdate(TaskDefinition definition){
        logger.debug("propagateUpdate TaskDefinition " + definition.getId());
        PolicySF policy = ServiceLocator.getPolicySFLocal();

        Collection<Task> tasks = policy.findTaskByTaskDefinition(definition.getId());
		for (Task task : tasks) {
		    policy.synchronize(task);
		}
    }

    /**
     * Propagates properties of definition to all dependent instances.
     * Note: TaskGroups never lose their relation to TaskGroupDefinitions even if
     * TaskDefs in TaskGroupDefinition are different from Tasks in TaskGroup.
     * Then TaskGroupDefinition is propogated, in actuality, memebership property of all TaskDefinitions for the ObjectDefinition is
     * propogated to all non-custom Tasks
     * @param definition
     * @deprecated
     */
    public static void propagateUpdate2(TaskGroupDefinition definition){
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        //Collection<TaskDef> taskDefs = definition.getTaskDefs();  // must use all object task, to account for tasks are being removed from the group
		Collection<TaskDefinition> taskDefs = policy.getObjectDefinition(definition).getTaskDefs();
		for (TaskDefinition taskDef : taskDefs) {
		    Collection<Task>tasks = policy.findTaskByTaskDefinition(taskDef.getId());
		    for (Task task : tasks) {
		        policy.synchronizeGroupMembership(task, isf.getTaskGroups(csf.getObject(task)));
		    }
		}

		//TaskGroupDef has one property that may need be propagated, description
		//another property since v35 is skilltypeid
		Collection<TaskGroup>taskGroups = policy.getTaskGroups(definition);
		for (TaskGroup taskGroup : taskGroups) {
		    taskGroup.setDescription(definition.getDescription());
		    taskGroup.setSkillTypeId(definition.getSkillTypeId());
		}
    }

    /**A better optimized version  of propagateUpdate
     * Propagates properties of definition to all dependent instances.
     * Note: TaskGroups never lose their relation to TaskGroupDefinitions even if
     * TaskDefs in TaskGroupDefinition are different from Tasks in TaskGroup.
     * Then TaskGroupDefinition is propogated, in actuality, memebership property of all TaskDefinitions for the ObjectDefinition is
     * propogated to all non-custom Tasks
     * @param definition
     */
    public static void propagateUpdate(TaskGroupDefinition definition)  {
        logger.debug("propagateUpdate TaskGroupDefinition " + definition.getDescription());
        /*Collection<ObjectEntity> objects = definition.getObjectDefinition().getObjects();
        logger.debug("number objects " + objects.size());
        for (ObjectEntity object : objects) {
            Collection<Task>tasks = object.getTasks();
            logger.debug("number tasks " + tasks.size() + " for Object " + object.getObjectRef());
            for (Task task : tasks) {
                task.synchronizeGroupMembership(object.getTaskGroups());
            }
        }*/
        /*TO MUCH ejb OVERHEAD HERE, SWITCHING TO JDBC, see propagateTaskGroupDefinitionTasks which must be called outside EJB transactions as
         EJB container commits after transaction is completed, making the updates not available for JBDC calls*/

        Collection<TaskGroup> taskGroups = definition.getTaskGroups();
        logger.debug("number task groups " + taskGroups.size());
        for (TaskGroup taskGroup : taskGroups) {
        	if(taskGroup.getCustom().intValue()==0){
        		taskGroup.setDescription(definition.getDescription());
        		taskGroup.setSkillTypeId(definition.getSkillTypeId());
        	}
        }

        /** propagateTaskGroupDefinitionTasks Must be called outside EJB transaction
        propagateTaskGroupDefinitionTasks(definition);
         */
   }

    /**
     * It might be very time consuming to update all Task memeberships using EJB.
     * Thts is why JDBC implementation is in use here
     * @param definition
     */
    public static void propagateTaskGroupDefinitionTasks(TaskGroupDefinition definition){
        logger.debug("propagateUpdate TaskGroupDefinition Tasks " + definition.getDescription());
        /*Update task group membership for all non-custom tasks that can be traversed to this object definition
         Every non-custom task must belong to taskgroup which extends taskgroup definition for this object definition*/
        try {
            int cnt = DBAccess.executeUpdate("UPDATE TASK T SET GROUP_ID = (SELECT TG.ID FROM TASK_DEF TD, TASK_GROUP TG WHERE T.TASK_DEF_ID=TD.ID \n" +
                    " AND TG.TASK_GROUP_DEF_ID=TD.GROUP_ID AND T.OBJECT_ID=TG.OBJECT_ID )" +
                    " WHERE T.ID IN " +
                    " (SELECT T.ID FROM TASK_DEF TD  WHERE T.TASK_DEF_ID=TD.ID AND TD.OBJECT_DEF_ID= " +definition.getObjectDefinition().getId()+ " AND T.CUSTOM=0)");
        } catch (SQLException e) {
            throw new IWMException(e);
        }
    }

    /** deprecated in favor of propagateTaskGroupDefinitionTasks
     */
    private static void propagateTaskGroupDefinitionTasks0(TaskGroupDefinition definition) throws SQLException {
        logger.debug("propagateUpdate TaskGroupDefinition Tasks" + definition.getDescription());
        //First reset any existing between Tasks and TaskGroups
        DBAccess.executeUpdate("UPDATE TASK T SET GROUP_ID=NULL WHERE EXISTS " +
                "(SELECT 1 FROM TASK_DEF TD WHERE T.TASK_DEF_ID=TD.ID AND TD.OBJECT_DEF_ID ="+definition.getObjectDefinition().getId()+")");
        //Set new relations between Tasks and TaskGroups based on TaskDef membership in this TaskGroupDefinition
        // may need db index. Andrei: noticed it took 10secs to update a group for an object_def (AREA) which had nearly 9000 Task instances (via 2000 Area Objects)
        // It is unlikely though we will need to create groups for AREA, and unlikely that will currently may have other object templates with such a great family of descendants
        DBAccess.executeUpdate("UPDATE TASK T SET GROUP_ID = " +
                "(SELECT TG.ID FROM TASK_DEF TD, TASK_GROUP TG WHERE T.TASK_DEF_ID=TD.ID AND TD.GROUP_ID="
                +definition.getId()+ " AND TG.TASK_GROUP_DEF_ID=TD.GROUP_ID  AND T.OBJECT_ID=TG.OBJECT_ID)");
    }


    /**
     * Propagates properties of definition to all dependent (non-custom) instances
     * @param definition
     */
    public static void propagateUpdate(ActionDefinition definition){
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<Action> actions = policy.findActionByActionDefinition(definition.getId());
		for (Action action : actions) {
		    policy.synchronize(action);
		}
    }

    /**
     * Propagates properties of definition to all dependent (non-custom) instances
     * @param definition
     */
    public static void propagateUpdate(ObjectDataDefinition definition){
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<ObjectData> data = policy.findObjectDataByDataDefinition(definition.getId());
		for (ObjectData d : data) {
		    d.synchronize();
		}
    }

    /**
     * Should be called when new ObjectDataDefinition is created.
     * Create ObjectData instances for all Objects linked to the ObjectDefinition
     * @param definition
     * @throws CreateException
     */
    public static void propagateCreate(ObjectDataDefinition definition, ObjectDefinition objectDef) throws CreateException{
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<ObjectEntity> objects = policy.getObjects(objectDef);
		for (ObjectEntity object : objects) {
		    object.inherit(definition);
		}
    }

    /**
     * Create Task instances for all Objects linked to the ObjectDefinition
     * If TaskDefinition is NOT_INITED task defs cannot be used to create Tasks
     * @param definition
     */
    public static void propagateCreate(TaskDefinition definition) {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<ObjectEntity> objects = policy.findObjectByObjectDefinition(definition.getObjectDefinition().getId());
		for (ObjectEntity object : objects) {
			object.inherit(definition);
		}

    }

    /**
     * Should be called when new ActionDefinition is created.
     * Create Action instances for all Tasks linked to the TaskDefinition
     * @param definition
     */
    public static void propagateCreate(ActionDefinition definition) {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Collection<Task> tasks = policy.findTaskByTaskDefinition(definition.getTaskDefinition().getId());
		for (Task task : tasks) {
		    policy.inherit(task, definition);
		    logger.debug("ActionDef " + definition.getId() + " propagated to Task " + task.getId());
		}
    }

    /**
     * Should be called when new TaskGroupDefinition is created.
     * Create TaskGroup instances for all Objects linked to the ObjectDefinition
     * @param definition
     */
    public static void propagateCreate(TaskGroupDefinition definition) {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        //if(!EqualsUtils.areEqual(Long.valueOf(Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID)), definition.getObjectDefId())){   //do not propagate are template. makes no sence
        ObjectDefinition od = policy.getObjectDefinition(definition);
		Collection<ObjectEntity> objects = od.getObjects();
		for (ObjectEntity o : objects) {
		    o.inherit(definition);
		}
    }

}
