package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.jms.PropagateTaskTemplate;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTasks implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TasksCriteria cr = new TasksCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplateTasksDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TemplateTask.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        try{
            psf.removeTaskDefinition(itemId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.TemplateTask getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TemplateTask form = new org.mlink.iwm.bean.TemplateTask();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        TaskDefinition vo = psf.get(TaskDefinition.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setDescription(vo.getTaskDescription());
        form.setObjectDefId(Long.toString(vo.getObjectDefinition().getId()));
        return form;
    }

    /**
     * Update/Create Template.
     * @param bean
     * @return
     * @throws Exception
     */
    public String saveItem(HashMap bean) throws Exception {
        String rtn = null;
        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            TaskDefinition vo = new TaskDefinition();
            CopyUtils.copyProperties(vo,bean);
            vo.setTaskDescription((String)bean.get("description"));
            Long objectDefId = Long.parseLong((String)bean.get("objectDefId"));
            
            if(vo.getId() > 0){
                ObjectDefinition od = psf.get(ObjectDefinition.class, objectDefId);
                vo.setObjectDefinition(od);
                psf.updateTaskDefinition(vo);
                //PTPClient.sendObjectAsync(new PropagateTaskTemplate(vo.getId()));
                new PropagateTaskTemplate(vo.getId()).execute();

                // taskdef propagation is taking too much time. Use separate thread to do so
                // see comments in PolicySF.propagateTaskDefinition
                /*final Long taskDefId=vo.getId(); // to support the anonymous class below
                Thread tr = new Thread(new Runnable(){
                    public void run(){
                        PolicySF psf = ServiceLocator.getPolicySFLocal( );
                        try {
                            psf.propagateTaskDefinition(taskDefId);
                        } catch (RemoteException e) {
                            throw new IWMException(e);
                        }
                    }
                }
                );
                tr.start();*/
            }else{
            	ObjectDefinition od = new ObjectDefinition();
                od.setId(objectDefId);
                vo.setObjectDefinition(od);
                psf.createTaskDefinition(vo);
            }
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    /**
     * Get all templates to choose as the copy source
     * @param criteria
     * @return
     * @throws Exception
     */
    public ResponsePage getTemplates(HashMap criteria) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.TemplatesForObjectCreateDAO,cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Template.class);
        return new ResponsePage(lst.size(),lst);
    }

    /**
     * Copy Task Templates from one Template  to another
     * @param sourceTemplateId
     * @param targetTemplateId
     * @return number of copied tasks
     * @throws Exception
     */
    public int copyTaskTemplates(long sourceTemplateId, long targetTemplateId) throws Exception {
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        return psf.copyTaskDefinitions(sourceTemplateId,targetTemplateId);
    }

}
