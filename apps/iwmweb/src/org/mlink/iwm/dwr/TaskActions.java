package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.TaskAction;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TaskActions implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTaskActionsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TaskAction.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        try{
            isf.removeAction(itemId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.TaskAction getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TaskAction form = new org.mlink.iwm.bean.TaskAction();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Action vo = isf.get(Action.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setTaskId(Long.toString(vo.getTask().getId()));
        if(vo.getActionDefinition()!=null){
        	form.setActionDefId(Long.toString(vo.getActionDefinition().getId()));
        }
        return form;
    }

    public String saveItem(HashMap map) throws Exception {
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Action vo = new Action();
        CopyUtils.copyProperties(vo,map);
        Task task = isf.get(Task.class, Long.valueOf((String)map.get("taskId")));
        vo.setTask(task);
        if(vo.getId() > 0){
        	String actionDefId = (String)map.get("actionDefId");
        	if(actionDefId!=null && actionDefId.length()>0){
        		ActionDefinition voDef = new ActionDefinition();
                CopyUtils.copyProperties(voDef,map);
                vo.setActionDefinition(new ActionDefinition(Long.valueOf(actionDefId)));
        	}
            isf.update(vo);
        }else{
        	vo.setCustom(1);	//we can insert for only custom
        	//TODO: assuming that as the action is created here there will be no action Def
        	
        	List <TaskAction> lst = (List <TaskAction>) SessionUtil.getAttribute("actionsForOrdering");
        	int sequence = 0;
        	if(lst==null){
        		ResponsePage rp = getOrderedActions(task.getId());
        		sequence = rp.getTotalCount();
        	}else{
        		sequence = lst.size();
        	}
        	sequence++;
        	vo.setSequence(sequence);
        	isf.create(vo);
        }
        return null;
    }

    /**
     * reorder action sequence based on new sequnce number for given action
     * increment will track the direction of change (up or down in sequence)
     * @param actionId
     * @param sequenceNumber
     * @return
     * @throws Exception
     */
    public ResponsePage updateActionSequence(String actionId, int sequenceNumber) throws Exception {
        List <TaskAction> lst = (List <TaskAction>) SessionUtil.getAttribute("actionsForOrdering");
        float increment;
        for (TaskAction action : lst) {
            if(EqualsUtils.areEqual(action.getId(),actionId)){
                increment=Integer.valueOf(action.getSequence())>sequenceNumber?-0.1f:0.1f;
                action.setfSequence(sequenceNumber+increment);
            }else{
                action.setfSequence(Float.valueOf(action.getSequence()));
            }
        }
        Collections.sort(lst);
        int newSequence = 1;
        for (TaskAction action : lst) {
             action.setSequence(String.valueOf(newSequence++));
        }
        return new ResponsePage(lst.size(),lst);
    }

    public ResponsePage getOrderedActions(long taskId) throws Exception {
        SearchCriteria cr = new SearchCriteria(); cr.setId(taskId);
        PaginationRequest request = new PaginationRequest("sequence");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTaskActionsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TaskAction.class);
        SessionUtil.setAttribute("actionsForOrdering",lst);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String saveOrderedActions() throws Exception {
        List <TaskAction> lst = (List <TaskAction>) SessionUtil.getAttribute("actionsForOrdering");
        //Collection<Action> vos = CopyUtils.copyProperties(Action.class,lst);
        Collection<Action> vos = new ArrayList<Action>();
        Action act;
        for(TaskAction ta : lst){
        	act = new Action();
        	CopyUtils.copyProperties(act,ta);
        	String actionDefId = ta.getActionDefId();
        	if(actionDefId!=null && actionDefId.length()>0){
        		act.setActionDefinition(new ActionDefinition(Long.parseLong(actionDefId)));
        	}
        	act.setTask(new Task(Long.parseLong(ta.getTaskId())));
        	vos.add(act);
        }
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        isf.updateTaskActions(vos);
        SessionUtil.removeAttribute("actionsForOrdering");
        return ITEM_SAVED_OK_MSG;
    }
}
