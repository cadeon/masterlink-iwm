package org.mlink.iwm.dwr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.TemplateTaskAction;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTaskActions implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplateTaskActionsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TemplateTaskAction.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        try{
            psf.removeActionDefinition(itemId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.TemplateTaskAction getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TemplateTaskAction form = new org.mlink.iwm.bean.TemplateTaskAction();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        ActionDefinition vo = psf.get(ActionDefinition.class, itemId);
        form.setTaskDefId(Long.toString(vo.getTaskDefinition().getId()));   
        CopyUtils.copyProperties(form,vo);
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        ActionDefinition vo = new ActionDefinition();
        CopyUtils.copyProperties(vo,bean);
        vo.setTaskDefinition(new TaskDefinition(Long.parseLong((String)bean.get("taskDefId"))));
        if(vo.getId() > 0){
            psf.updateActionDefinition(vo);
        }else{
            psf.createActionDefinition(vo);
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
        List <TemplateTaskAction> lst = (List <TemplateTaskAction>) SessionUtil.getAttribute("actionsForOrdering");
        float increment;
        for (TemplateTaskAction action : lst) {
            if(EqualsUtils.areEqual(action.getId(),actionId)){
                increment=Integer.valueOf(action.getSequence())>sequenceNumber?-0.1f:0.1f;
                action.setfSequence(sequenceNumber+increment);
            }else{
                action.setfSequence(Float.valueOf(action.getSequence()));
            }
        }
        Collections.sort(lst);
        int newSequence = 1;
        for (TemplateTaskAction action : lst) {
             action.setSequence(String.valueOf(newSequence++));
        }
        return new ResponsePage(lst.size(),lst);
    }

    public ResponsePage getOrderedActions(long taskId) throws Exception {
        SearchCriteria cr = new SearchCriteria(); cr.setId(taskId);
        PaginationRequest request = new PaginationRequest("sequence");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplateTaskActionsDAO,cr,request);
        List <TemplateTaskAction> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TemplateTaskAction.class);
        SessionUtil.setAttribute("actionsForOrdering",lst);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String saveOrderedActions() throws Exception {
        List <TemplateTaskAction> lst = (List <TemplateTaskAction>) SessionUtil.getAttribute("actionsForOrdering");
        Collection <ActionDefinition> vos = CopyUtils.copyProperties(ActionDefinition.class,lst);
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        psf.updateActionSequence(vos);
        SessionUtil.removeAttribute("actionsForOrdering");        
        return ITEM_SAVED_OK_MSG;
    }
}
