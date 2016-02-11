package org.mlink.iwm.dwr;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class JobTaskActions implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobTaskActionsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.JobTaskAction.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        // jobTaskActions are never deleted
        return null;
    }

    public org.mlink.iwm.bean.JobTaskAction getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.JobTaskAction form = new org.mlink.iwm.bean.JobTaskAction();
        ControlSF csf = ServiceLocator.getControlSFLocal();
        JobAction javo = csf.get(JobAction.class, itemId);
        CopyUtils.copyProperties(form,javo);
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        ControlSF csf = ServiceLocator.getControlSFLocal();
        JobAction javo = new JobAction();
        CopyUtils.copyProperties(javo,bean);
        JobAction javo1 = csf.get(JobAction.class, javo.getId());
        javo1.setFieldCondition(javo.getFieldCondition());
        csf.updateJobAction(javo1);
        return ITEM_SAVED_OK_MSG;
    }

    public String startTask(Long jobTaskId, Long jobTaskTimeId, String scheduledDate){
    	String returnVal = ITEM_SAVED_OK_MSG;
    	ControlSF csf = ServiceLocator.getControlSFLocal();
        Long jobId = csf.checkAnyTaskInProgress(jobTaskTimeId);
        if(jobId!=null){
        	returnVal="A task in Job: "+jobId+" in progress. So please end it before starting this task.";
        }else{
        	try {
				csf.startTask(jobTaskId, jobTaskTimeId, scheduledDate, new Timestamp(System.currentTimeMillis()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return returnVal;
    }
    
    public String stopTask(Long jobTaskId, Long jobTaskTimeId, String scheduledDate) throws Exception{
    	String returnVal = ITEM_SAVED_OK_MSG;
    	ControlSF csf = ServiceLocator.getControlSFLocal();
        csf.stopTask(jobTaskId, jobTaskTimeId, scheduledDate, new Timestamp(System.currentTimeMillis()));
        return returnVal;
    }
    
    public String doReplace(Long objectId, Long jobId, Long jobTaskId, Long jobTaskTimeId) throws Exception{
    	String returnVal = ITEM_SAVED_OK_MSG;
    	ControlSF csf = ServiceLocator.getControlSFLocal();
        String userName = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
    	try{
    		csf.doReplace(userName, objectId, jobId, jobTaskId, jobTaskTimeId, new Timestamp(System.currentTimeMillis()));
    	}catch(Exception e){
    		returnVal = e.getMessage();
    	}
        return returnVal;
    }
}
