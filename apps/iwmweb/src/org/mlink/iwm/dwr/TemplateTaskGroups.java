package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.TemplateTask;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.jms.PostCreateTaskGroupTemplate;
import org.mlink.iwm.jms.PropagateTaskGroupTemplate;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.rules.TaskPropertiesValidator;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.EqualsUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTaskGroups implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplateTaskGroupsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TaskGroupTemplate.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        psf.removeTaskGroupDefinition(itemId);
        return rtn;
    }

    /**
     *
     * @param itemId   taskGroupDefId
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.TaskGroupTemplate getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TaskGroupTemplate form = new org.mlink.iwm.bean.TaskGroupTemplate();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        TaskGroupDefinition vo = psf.getTaskGroupDefinition(itemId);
        CopyUtils.copyProperties(form,vo);
        form.setObjectDefId(Long.toString(vo.getObjectDefinition().getId()));
        List <TemplateTask> lst =  getTasks(vo.getObjectDefinition().getId());

        form.setTasks(lst);
        //prior v35 TG.skillType was not required. Line below  will help to clean the data
        // this IF statement can be removed when all Groups are clean, ie. skiltypeid is set in DB
        if(form.getSkillTypeId()==null && lst.size()>0 ) {
            /**for (TemplateTask task : lst) {
                if(EqualsUtils.areEqual(task.getGroupId(),form.getId()))
                    form.setSkillTypeId(task.getSkillTypeId());
            }*/
            form.setSkillType(extractGroupSkillType(StringUtils.valueOf(itemId)));

        }
        return form;
    }

    private List <org.mlink.iwm.bean.TemplateTask> getTasks(Long objectDefId) throws Exception {
        TasksCriteria cr = new TasksCriteria(); 
        cr.setId(objectDefId);
        PaginationRequest request = new PaginationRequest("description");
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.TemplateTasksDAO,cr,request);
        List <org.mlink.iwm.bean.TemplateTask> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TemplateTask.class);
        SessionUtil.setAttribute("groupTemplateTasks",lst);
        return lst;
    }

    /**
     * Returns a form for the new TaskGroup
     * @param objectDefId
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.TaskGroupTemplate getNewItem(Long objectDefId) throws Exception {
    	org.mlink.iwm.bean.TaskGroupTemplate form = new org.mlink.iwm.bean.TaskGroupTemplate();
        form.setObjectDefId(String.valueOf(objectDefId));
        form.setId("-1");
        List <org.mlink.iwm.bean.TemplateTask> lst =  getTasks(objectDefId);
        form.setTasks(lst);
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        PolicySF policySF = ServiceLocator.getPolicySFLocal( );
        TaskGroupDefinition vo = new TaskGroupDefinition();
        CopyUtils.copyProperties(vo,bean);
        long objectDefId = Long.parseLong((String)bean.get("objectDefId"));
        vo.setObjectDefinition(new ObjectDefinition(objectDefId));
        vo.setSkillTypeId(SkillTypeRef.getIdByLabel((String)bean.get("skillType")));
        /*List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupTemplateTasks");
        List <TemplateTask> selectedTasks = new ArrayList<TemplateTask> ();
        for (TemplateTask task : tasks) {
            if (EqualsUtils.areEqual(vo.getId().toString(),task.getGroupId())) selectedTasks.add(task);
        }
        vo.setTaskDefs((List)CopyUtils.copyProperties(TaskDefinition.class,selectedTasks));*/

        //vo.setTaskDefs((List)CopyUtils.copyProperties(TaskDefinition.class,getSelectedTasks(Long.toString(vo.getId()))));
        List <TemplateTask> selectedTasks = getSelectedTasks(Long.toString(vo.getId()));
        List <TaskDefinition> selectedTaskDefs = new ArrayList <TaskDefinition>();
        vo.setTaskDefs(selectedTaskDefs);
        
        TaskDefinition td;
    	List <TemplateTask> tasks = getTasks(objectDefId);
        if(tasks!=null && !tasks.isEmpty()){
        	for (TemplateTask task : tasks) {
	            if (bean.get("id").equals(task.getGroupId()) && !selectedTasks.contains(task)) {
	        		td = policySF.get(TaskDefinition.class, (Long.parseLong(task.getId())));
	        		td.setTaskGroupDef(null);
	        		policySF.update(td);
	            }
        	}
        }
        
        if(selectedTasks!=null && !selectedTasks.isEmpty()){
        	for(TemplateTask selectedTask : selectedTasks){
        		td = policySF.get(TaskDefinition.class, (Long.parseLong(selectedTask.getId())));
        		td.setTaskGroupDef(vo);
        		selectedTaskDefs.add(td);
        	}
        }
        String rtn=ITEM_SAVED_OK_MSG;
        try{
            if(vo.getId() > 0){
                policySF.updateTaskGroupDefinition(vo);
                //PTPClient.sendObjectAsync(new PropagateTaskGroupTemplate(vo.getId()));
                PropagateTaskGroupTemplate ptgt = new PropagateTaskGroupTemplate(vo.getId());
                ptgt.execute();
            }else{
                //PTPClient.sendObjectAsync(new PostCreateTaskGroupTemplate(psf.createTaskGroupDefinition(vo)));
            	PostCreateTaskGroupTemplate ptgt = new PostCreateTaskGroupTemplate(policySF.createTaskGroupDefinition(vo));
            	ptgt.execute();
            }
            SessionUtil.removeAttribute("groupTemplateTasks");
        }catch(BusinessException e){
            rtn = e.getMessage();
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
        List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupTemplateTasks");
        for (TemplateTask task : tasks) {
            if(EqualsUtils.areEqual(groupId,task.getGroupId())) task.setGroupId(null);
        }
        return new ResponsePage(tasks.size(),tasks);
    }

    public void updateTaskMembership(String taskId, String groupId){
        List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupTemplateTasks");
        for (TemplateTask task : tasks) {
            if(EqualsUtils.areEqual(taskId,task.getId())) {
            	task.setGroupId(groupId);
            }
        }
    }

    public String extractGroupSkillType(String groupId) throws Exception{
        Integer groupSkillType = TaskPropertiesValidator.extractSkillType(CopyUtils.copyProperties(TaskDefinition.class,getSelectedTasks(groupId)));
        return SkillTypeRef.getLabel(groupSkillType);
    }

    private List <TemplateTask> getSelectedTasks(String groupId){
        List <TemplateTask> tasks = (List<TemplateTask>)SessionUtil.getAttribute("groupTemplateTasks");
        List <TemplateTask> selectedTasks = new ArrayList<TemplateTask> ();
        for (TemplateTask task : tasks) {
            if (EqualsUtils.areEqual(groupId,task.getGroupId())) {
            	selectedTasks.add(task);
            }
        }
        return selectedTasks;
    }
}
