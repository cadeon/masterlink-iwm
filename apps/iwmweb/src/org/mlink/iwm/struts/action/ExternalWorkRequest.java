package org.mlink.iwm.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.bean.JobTaskAction;
import org.mlink.iwm.bean.Worker;
import org.mlink.iwm.dao.ExternalWorkRequestListDAO;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.lookup.DummyDropdown;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.ExternalWorkRequestForm;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Nov 21, 2006
 */
public class ExternalWorkRequest extends BaseAction {
    private static final Logger logger = Logger.getLogger(ExternalWorkRequest.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        ExternalWorkRequestForm form  = (ExternalWorkRequestForm)aform;
        String forward = form.getForward()==null?"welcome":form.getForward();
        logger.debug("execute " + forward);

        if("new".equals(forward)){
            processNew(mapping,form,request,response);
        }else if("track".equals(forward)){
            processRead(mapping,form,request,response);
        }else if("submit".equals(forward)){
            processSubmit(mapping,form,request,response);
        }else{
            processWelcome(mapping,form,request,response);
        }
        return findForward(mapping,request,forward);

    }

    private void processWelcome(ActionMapping mapping, ExternalWorkRequestForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try {
            List list = DBAccess.executeDAO(new ExternalWorkRequestListDAO());
            //Collection col = CopyUtils.copyProperties(ExternalWorkRequestForm.class,list);
            Collection col = new ArrayList();
            ListIterator li = list.listIterator();
            TenantRequest tr;
            ExternalWorkRequestForm form1;
            while(li.hasNext()){
            	tr = (TenantRequest)li.next();
            	form1 = new ExternalWorkRequestForm();
            	CopyUtils.copyProperties(form1, tr);
            	copyTRIntoForm(form1, tr, tr.getJobId());
            	col.add(form1);
            }
            form.setActiveRequests((List)col);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }
    private void processRead(ActionMapping mapping, ExternalWorkRequestForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        String jobIdStr = form.getJobId();
        long jobId;
        if(jobIdStr==null) return;
        try{
            ControlSF csf = ServiceLocator.getControlSFLocal( );
            jobId = StringUtils.getLong(jobIdStr);
            TenantRequest tr = csf.getTenantRequestByJob(jobId);
            copyTRIntoForm(form, tr, jobId);
        }catch(Exception e){
            String msg = "Request "+ jobIdStr + " not found";
            form.setTenantRequestId(null);
            logger.warn(msg);
            form.setMessage(msg);
        }
    }
    
    private void copyTRIntoForm(ExternalWorkRequestForm form, TenantRequest tr, long jobId){
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
    	try{
    		BeanUtils.copyProperties(form,tr);
	        form.setTenantRequestId(Long.toString(tr.getId()));
	        form.getActions().addAll(CopyUtils.copyProperties(JobTaskAction.class, tr.getJobActions()));
	        Collection workers = copyPersonProperties(tr.getWorkers());
	        form.setWorkers((List) workers);
	        Job jobVo = csf.get(Job.class, jobId);
	        form.setDispatchedDate(ConvertUtils.formatDate(jobVo.getDispatchedDate()));
    	}catch(Exception e){
            String msg = "Error: "+e.getMessage()+" in copying Tenant info into Form.";
            form.setTenantRequestId(null);
            logger.warn(msg);
            form.setMessage(msg);
        }
    }

    private void processSubmit(ActionMapping mapping, ExternalWorkRequestForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        TenantRequest vo = new TenantRequest();

        try{
            CopyUtils.copyProperties(vo,form);
            ControlSF csf = ServiceLocator.getControlSFLocal( );
            vo.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
            Long tenantRequestId = csf.createExternalWorkRequest(vo);
            Job job = csf.getJobByTenantRequest(tenantRequestId);
            form.setMessage("Request " + job.getId() + " has been submitted");
            form.setJobId(String.valueOf(job.getId()));

        }catch(Exception e){
            throw new WebException(e);
        }
    }

    private void processNew(ActionMapping mapping, ExternalWorkRequestForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try{
            //LookupMgr.reloadCDLV(ExternalWorkRequestProblemRef.class); // reload to make sure that possibly  recently problems are read. Not realy efficient but a shortcut for now
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            Collection tasks = psf.getTaskDefinitions(StringUtils.getLong(Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID)));
            put(request,"ProblemSelector", new DummyDropdown(transform(tasks)));
        }catch(Exception e){
            throw new WebException(e);
        }
    }

    private static Collection copyPersonProperties(Collection persons) throws WebException{
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	Collection rtnCol = new ArrayList();
    	if(persons!=null && !persons.isEmpty()){
    		try{
	            for (Iterator iterator = persons.iterator(); iterator.hasNext();) {
	                Worker li = new Worker();
	                Person person = (Person) iterator.next();
	                BeanUtils.copyProperties(li,person.getParty());
	                BeanUtils.copyProperties(li,person);
	                rtnCol.add(li);
	            }
	        } catch (Exception e) {
	            throw new WebException(e);
	        }
    	}
        return rtnCol;
    }

    private Collection  transform(Collection lst){
        return CollectionUtils.collect(lst, new Transformer(){
            public Object transform(Object input) {
                TaskDefinition vo = (TaskDefinition) input;
                return new OptionItem(vo.getId(),vo.getTaskDescription());
            }
        });
    }
}

