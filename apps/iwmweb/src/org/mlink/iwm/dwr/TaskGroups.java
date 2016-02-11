package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ObjectTask;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.TaskGroup;
import org.mlink.iwm.bean.TemplateTask;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.rules.TaskPropertiesValidator;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TaskGroups implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTaskGroupsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TaskGroup.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        try{
            isf.removeTaskGroup(itemId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.TaskGroup getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TaskGroup form = new org.mlink.iwm.bean.TaskGroup();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        org.mlink.iwm.entity3.TaskGroup taskGroup = isf.get(org.mlink.iwm.entity3.TaskGroup.class, itemId);
        CopyUtils.copyProperties(form,taskGroup);

        form.setObjectId(Long.toString(taskGroup.getObject().getId()));
        form.setTaskGroupDefId(taskGroup.getTaskGroupDef()!=null?Long.toString(taskGroup.getTaskGroupDef().getId()):null);
        
        List <ObjectTask> lst =  getTasks(taskGroup.getObject().getId());
        form.setTasks(lst);
        form.setTaskCount(Long.toString((lst!=null&&!lst.isEmpty())?lst.size():0));
        
        //prior v35 TG.skillType was not required. Line below does will help to clean the data
        // this IF statement can be removed when all Groups are clean, ie. skiltypeid is set in DB
        if(form.getSkillTypeId()==null && lst.size()>0) {
            for (ObjectTask task : lst) {
            	if(form.getSkillTypeId()!=null) break;
                if(EqualsUtils.areEqual(task.getGroupId(),form.getId()))
                    form.setSkillTypeId(task.getSkillTypeId());
            }
        }
        SessionUtil.setAttribute("groupObjectTasks",lst);
        return form;
    }

    private List <org.mlink.iwm.bean.ObjectTask> getTasks(Long objectId) throws Exception {
        TasksCriteria cr = new TasksCriteria(); cr.setId(objectId);
        PaginationRequest request = new PaginationRequest("description");
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTasksDAO,cr,request);
        List <org.mlink.iwm.bean.ObjectTask> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectTask.class);
        SessionUtil.setAttribute("groupObjectTasks",lst);
        return lst;
    }

    /**
     * Returns a form for the new TaskGroup
     * @param objectId
     * @return
     * @throws Exception
     */
    public TaskGroup getNewItem(Long objectId) throws Exception {
        TaskGroup form = new TaskGroup();
        form.setObjectId(String.valueOf(objectId));
        form.setId("-1");
        List <org.mlink.iwm.bean.ObjectTask> lst =  getTasks(objectId);
        form.setTasks(lst);
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        org.mlink.iwm.entity3.TaskGroup taskGroup = new org.mlink.iwm.entity3.TaskGroup();
        CopyUtils.copyProperties(taskGroup,bean);
        /*List <ObjectTask> tasks = (List<ObjectTask>)SessionUtil.getAttribute("groupObjectTasks");
        List <ObjectTask> selectedTasks = new ArrayList<ObjectTask> ();
        for (ObjectTask task : tasks) {
            if (EqualsUtils.areEqual(taskGroup.getId().toString(),task.getGroupId())) selectedTasks.add(task);
        }
        taskGroup.setTasks(CopyUtils.copyProperties(Task.class,selectedTasks));*/
        taskGroup.setObject(new ObjectEntity(Long.parseLong((String)bean.get("objectId"))));
        
        //taskGroup.setTasks(CopyUtils.copyProperties(org.mlink.iwm.entity3.Task.class,getSelectedTasks(Long.toString(taskGroup.getId()))));
        Set<TemplateTask> selTasks = getSelectedTasks(Long.toString(taskGroup.getId()));
        Integer skillTypeId = TaskPropertiesValidator.extractSkillType(CopyUtils.copyProperties(Task.class,selTasks));
        taskGroup.setSkillTypeId(skillTypeId);
        Task task;
        long taskId;
        String rtn=ITEM_SAVED_OK_MSG;
        try{
            if(taskGroup.getId() > 0){
            	String taskGroupDefIdStr = (String)bean.get("taskGroupDefId");
            	taskGroup.setTaskGroupDef(taskGroupDefIdStr!=null?isf.get(TaskGroupDefinition.class, Long.parseLong(taskGroupDefIdStr)):null);
                isf.updateTaskGroup(taskGroup);
                
                //strip taskgroup off tasks which are deselected in UI
	        	Set<TemplateTask> deselTasks = getDeSelectedTasks();
	            if(deselTasks!=null && !deselTasks.isEmpty()){
	            	for(TemplateTask templateTask: deselTasks){
	            		taskId = Long.parseLong(templateTask.getId());
	            		task = isf.get(Task.class, taskId);
	            		task.setTaskGroup(null);
	            		isf.update(task);
	            	}
	            }
            }else{
            	isf.createTaskGroup(taskGroup);
            }
            SessionUtil.removeAttribute("groupObjectTasks");
        }catch(BusinessException e){
            rtn = e.getMessage();
        }
        
        if(ITEM_SAVED_OK_MSG.equals(rtn)){
	        if(selTasks!=null && !selTasks.isEmpty()){
	        	for(TemplateTask templateTask: selTasks){
	        		taskId = Long.parseLong(templateTask.getId());
	        		task = isf.get(Task.class, taskId);
	        		task.setTaskGroup(taskGroup);
	        		isf.update(task);
	        	}
	        }
        }
        
        return rtn;
    }

    /**
     * reset task membership for the given group . Supports skill type change in ui
     * @param groupId
     * @return
     * @throws Exception
     */
    public ResponsePage reset(String groupId) throws Exception {
        List <ObjectTask> tasks = (List<ObjectTask>)SessionUtil.getAttribute("groupObjectTasks");
        for (ObjectTask task : tasks) {
            if(EqualsUtils.areEqual(groupId,task.getGroupId())) task.setGroupId(null);
        }
        return new ResponsePage(tasks.size(),tasks);
    }

    public void updateTaskMembership(String taskId, String groupId){
        List <ObjectTask> tasks = (List<ObjectTask>)SessionUtil.getAttribute("groupObjectTasks");
        for (ObjectTask task : tasks) {
            if(EqualsUtils.areEqual(taskId,task.getId())) task.setGroupId(groupId);
        }
    }

    public String extractGroupSkillType(String groupId) throws Exception{
        Integer groupSkillType = TaskPropertiesValidator.extractSkillType(CopyUtils.copyProperties(Task.class,getSelectedTasks(groupId)));
        return SkillTypeRef.getLabel(groupSkillType);
    }

    private Set <TemplateTask> getSelectedTasks(String groupId){
        List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupObjectTasks");
        Set <TemplateTask> selectedTasks = new HashSet<TemplateTask> ();
        for (TemplateTask task : tasks) {
            if (EqualsUtils.areEqual(groupId,task.getGroupId())) selectedTasks.add(task);
        }
        return selectedTasks;
    }
    
    private Set <TemplateTask> getDeSelectedTasks(){
    	String groupId = null;
        List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupObjectTasks");
        Set <TemplateTask> selectedTasks = new HashSet<TemplateTask> ();
        for (TemplateTask task : tasks) {
            if (EqualsUtils.areEqual(groupId,task.getGroupId())) selectedTasks.add(task);
        }
        return selectedTasks;
    }
}
