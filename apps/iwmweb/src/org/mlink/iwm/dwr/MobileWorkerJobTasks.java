package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Jan 12, 2007
 */
public class MobileWorkerJobTasks implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(MobileWorkerJobTasks.class);

    /**
     * May choose to implement this method using ListDAOTemplate pattern as opposite to EJB call
     */
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Long jobScheduleId = cr.getId();
        //List lst = (List)CopyUtils.copyProperties(org.mlink.iwm.bean.JobTaskTime.class, csf.getTasksAndTimes(jobScheduleId));
        Collection<JobTaskTime> jobTaskTimes = csf.getTasksAndTimes(jobScheduleId);
        List lst = new ArrayList();
        org.mlink.iwm.bean.JobTaskTime jobTaskTimeBean;
        if(jobTaskTimes!=null && !jobTaskTimes.isEmpty()){
        	for(JobTaskTime jobTaskTime: jobTaskTimes){
        		jobTaskTimeBean = new org.mlink.iwm.bean.JobTaskTime();
    			jobTaskTimeBean.setDescription(jobTaskTime.getJobTask().getTaskDescription());
        		//jobTaskTimeBean.setActionCount(Long.toString(jobTaskTime.getJobTask().getActions().size()));
        		int noOfActions = 0;
        		/*Collection<JobAction> actions = csf.getActions(jobTaskTime.getJobTask());
        		if(actions!=null){
        			noOfActions = actions.size();
        		}*/
        		jobTaskTimeBean.setActionCount(Integer.toString(noOfActions));
        		jobTaskTimeBean.setEstTime(Long.toString(jobTaskTime.getJobTask().getEstTime()));
        		jobTaskTimeBean.setId(Long.toString(jobTaskTime.getId()));
        		jobTaskTimeBean.setJobTaskId(Long.toString(jobTaskTime.getJobTask().getId()));
        	
	    		Long totalTime= jobTaskTime.getJobTask().getTotalTime();
	    		jobTaskTimeBean.setTime(totalTime!=null?(Long.toString(totalTime)):"0");
	    		lst.add(jobTaskTimeBean);
        	}
        }
        return new ResponsePage(lst.size(),lst);
    }

    public org.mlink.iwm.bean.JobTaskTime getItem(Long jobTaskTimeId) throws Exception {
    	org.mlink.iwm.bean.JobTaskTime form = new org.mlink.iwm.bean.JobTaskTime();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        JobTaskTime jtt = csf.get(JobTaskTime.class, jobTaskTimeId);
        CopyUtils.copyProperties(form,jtt);
        return form;
    }
    
    public org.mlink.iwm.bean.JobTaskTime getJobTaskTime(Long jobScheduleId) throws Exception {
        //Returns ONE jobtasktime associated with this JS.
    	
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        Collection<JobTaskTime> jobTaskTimes = csf.getTasksAndTimes(jobScheduleId);
        org.mlink.iwm.bean.JobTaskTime form = new org.mlink.iwm.bean.JobTaskTime();
        
        if(jobTaskTimes==null || jobTaskTimes.isEmpty()) return null; //No existing JTT for this schedule.
        JobTaskTime jtt = jobTaskTimes.iterator().next();
        form.setDescription(jtt.getJobTask().getTaskDescription());
		form.setEstTime(Long.toString(jtt.getJobTask().getEstTime()));
		form.setId(Long.toString(jtt.getId()));
		form.setJobTaskId(Long.toString(jtt.getJobTask().getId()));
		Long totalTime= jtt.getJobTask().getTotalTime();
		form.setTime(totalTime!=null?(Long.toString(totalTime)):"0");
        
        CopyUtils.copyProperties(form,jtt);
        return form;
    }
    
    public String saveItem(HashMap bean) throws Exception {
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        JobTaskTime vo = new JobTaskTime();
        CopyUtils.copyProperties(vo,bean);
        //cumilate is false as here -manual time keeping- we just enter manually the correct time
        csf.updateJobTaskTime(vo.getId(), vo.getTime(), false);
        return ITEM_SAVED_OK_MSG;
    }

    public String deleteItem(Long itemId) throws Exception {
        return null;  //NA
    }
}
