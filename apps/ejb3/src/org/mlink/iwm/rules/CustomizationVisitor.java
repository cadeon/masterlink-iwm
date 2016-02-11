package org.mlink.iwm.rules;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.session.PolicySFLocal;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.EqualsUtils;

/**
 * This class localizes busines rules for maintaning/breaking links beetween
 * instances and definitions. vist() business methods should be called from
 * uses cases that update instances, such as Task. visit(Task) method contains logic to
 * determine if link between Task and TaskDef should be broken, i.e. Task becomes customized.
 *
 * User: andrei
 * Date: Sep 8, 2004
 */
public class CustomizationVisitor {
    private static final Logger logger = Logger.getLogger(CustomizationVisitor.class);

    /**
     * Compare properties of Task and TaskDefinition and make decision whether task should be set custom
     * @param instance
     * @return customFlag
     */
    public static Integer visit(Task instance){
        boolean isEqual = true;
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        
        if(instance.getTaskDefinition()==null) {   //it is a stand-alone task
            isEqual = false;
        }else{
            TaskDefinition  definition = null;
            definition = instance.getTaskDefinition();
            isEqual = EqualsUtils.areEqual(definition.getTaskDescription(),instance.getTaskDescription())&
                    //EqualsUtils.areEqual(definition.getActive(),instance.getActive())&
                    EqualsUtils.areEqual(definition.getTaskTypeId(),instance.getTaskTypeId())&
                    EqualsUtils.areEqual(definition.getPriorityId(),instance.getPriorityId())&
                    EqualsUtils.areEqual(definition.getEstTime(),instance.getEstTime())&
                    EqualsUtils.areEqual(definition.getNumberOfWorkers(),instance.getNumberOfWorkers())&
                    //feb bootcamp05 decision EqualsUtils.areEqual(definition.getWorkerTypeId(),instance.getWorkerTypeId())&
                    EqualsUtils.areEqual(definition.getFrequencyId(),instance.getFrequencyId())&
                    EqualsUtils.areEqual(definition.getFreqDays(),instance.getFreqDays())&
                    EqualsUtils.areEqual(definition.getFreqMonths(),instance.getFreqMonths())&
                    EqualsUtils.areEqual(definition.getSkillTypeId(),instance.getSkillTypeId())&
                    EqualsUtils.areEqual(definition.getSkillLevelId(),instance.getSkillLevelId())&
                    //EqualsUtils.areEqual(definition.getRunHoursThresh(),instance.getRunHoursThresh());
                    EqualsUtils.areEqual(definition.getRunHoursThreshInc(),instance.getRunHoursThreshInc());
            //EqualsUtils.areEqual(definition.getStartDate(),instance.getStartDate());
            //EqualsUtils.areEqual(definition.getEndDate(),instance.getEndDate());

            /** andrei 03/09/05 commenting as v1 to v2 migration shows massive differecnes btw objectDefPlan and Task plans
             * casusing 90% of tasks become custom. Plan is no longer customizable property per Garry/Douglas input
            if(isEqual){
                ObjectDefinition objectDef = definition.getObjectDefinition();
                isEqual = EqualsUtils.areEqual(objectDef.getPlan(),instance.getPlan());
            }*/

        }

        Integer customFlag=isEqual?Constants.CUSTOMIZED_NO:Constants.CUSTOMIZED_YES;

        //do set the code in either case (true or false) will ensure migrated data cleanup
        //performance overhead should be reasonable
        instance.setCustom(customFlag);
        Collection<Action> col = csf.getActions(instance);
        for (Action action : col) {
            //all actions for custom task become also custom
            action.setCustom(customFlag);
            csf.update(action);
        }

        return customFlag;
    }

    /**
     * Compare properties of instance and definition and make decision whether task should be set custom
     * Custom flag for Actions is identical to custom flag of the Task
     * @param instance
     */
    public static void visit(Action instance){

    }


    /** Helper method for v1 to v2 migration procedure
     * compare action sets for task with its definition (if not stand-aone)
     * @param instance
     */
    public static boolean isActionSetIdentical(Task instance){
        //if(true) return true;    //todo comment
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        
        TaskDefinition definition = csf.getTaskDefinition(instance);
        //if no definition define as actionSet is same
        if(definition==null) return true;

        Collection<ActionDefinition> actionDefs = policy.getActionDefs(definition);
        Collection<Action> actions = csf.getActions(instance);
        
        //if size is defferent then obviosly action sets are different
        if(actionDefs.size() != actions.size())   return false;
        
        //now its time to compare one by one
        for (Iterator iterator = actions.iterator(); iterator.hasNext();) {
            boolean isEqual = false;
            Action action = (Action) iterator.next();
            ActionDefinition actionDef = policy.getActionDefinition(action);
            if(actionDef==null) break; //if action has no action def then action sets are different
            StringBuffer tmp = new StringBuffer();
            for (Iterator iterator2 = actionDefs.iterator(); (iterator2.hasNext() && !isEqual);) {
                ActionDefinition anotherOne = (ActionDefinition) iterator2.next();
                //isEqual = actionDef.isIdentical(anotherOne);
                isEqual =  EqualsUtils.areEqual(actionDef.getId(),anotherOne.getId()) &&
                        EqualsUtils.areEqual(actionDef.getModifier(),anotherOne.getModifier()) &&
                        EqualsUtils.areEqual(actionDef.getVerb(),anotherOne.getVerb()) &&
                        EqualsUtils.areEqual(actionDef.getName(),anotherOne.getName()) &&
                        EqualsUtils.areEqual(actionDef.getSequence(),anotherOne.getSequence());


                        tmp.append("\n"+ actionDef.getId()+ " vs " +anotherOne.getId());
                        tmp.append("\n"+actionDef.getModifier()+ " vs " +anotherOne.getModifier());
                        tmp.append("\n"+actionDef.getVerb()+ " vs " +anotherOne.getVerb());
                        tmp.append("\n"+actionDef.getName()+ " vs " +anotherOne.getName());
                        tmp.append("\n"+actionDef.getSequence()+ " vs " +anotherOne.getSequence());
            }
            if(!isEqual) {
                logger.debug(tmp.toString());
                return false;
            }

        }

        return true;
    }
    /**
     * Compare properties of instance and definition and make decision whether task should be set custom
     * @param instance
     */
    public static Integer visit(ObjectData instance){
        boolean isIdentical = true;
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        if(instance.getObjectDataDef()==null) { //it is a stand-alone
            isIdentical = true;
        } else{
            ObjectDataDefinition definition = instance.getObjectDataDef();
            isIdentical =   EqualsUtils.areEqual(definition.getDataTypeId(),instance.getDataTypeId()) &&
                    EqualsUtils.areEqual(definition.getUomId(),instance.getUomId()) &&
                    EqualsUtils.areEqual(definition.getDataLabel(),instance.getDataLabel());

        }
        //do set the code in either case (true or false) will ensure migrated data cleanup
        Integer customFlag=isIdentical?Constants.CUSTOMIZED_NO:Constants.CUSTOMIZED_YES;
        instance.setCustom(customFlag);
        policy.update(instance);
        return customFlag;

    }
    /**
     * TaskGroup is never custom, but can be stand-alone (which is still customFlag=yes)
     * Group memebership is Task property thus custom concept do not apply for TaskGroup
     * If Task is not custom (attached to TaskDef) it cannot change it group membership
     * @param instance
     */
    public static Integer visit(TaskGroup instance) throws BusinessException{
        //do set the code in either case (true or false) will ensure migrated data cleanup
        Integer customFlag;
        if(instance.getTaskGroupDef()==null) { //it is a stand-alone
            customFlag = Constants.YES;
        }
        else{
            customFlag = Constants.NO;
            TaskGroupDefinition template = instance.getTaskGroupDef();
            if(!EqualsUtils.areEqual(template.getSkillTypeId(),instance.getSkillTypeId())
                    && !EqualsUtils.areEqual(template.getSkillTypeId(),Integer.valueOf(SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN))))
                throw new BusinessException("Skill type of Task Group \'"+ SkillTypeRef.getLabel(instance.getSkillTypeId())
                        + "\' does not match skill type of the Group Template \'" + SkillTypeRef.getLabel(template.getSkillTypeId()) + "\' !");
        }

        instance.setCustom(customFlag);
        
        PolicySF policy = ServiceLocator.getPolicySFLocal();
    	policy.update(instance);
        return customFlag;
    }
}
