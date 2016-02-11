package org.mlink.iwm.dwr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.mlink.agent.dao.DAOException;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.bean.MWJob;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.MWJobsCriteria;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobStatusHist;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Dec 14, 2006
 */
public class MobileWorkerJobs implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(MobileWorkerJobs.class);

    /**
     * May choose to implement this method using ListDAOTemplate pattern as opposite to EJB call
     */
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        MWJobsCriteria cr = new MWJobsCriteria(criteria);
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Collection <WorkSchedule> cws;
        java.sql.Date d=null;
        
        if(cr.getScheduledDate()!=null){
        	try{
            d = ConvertUtils.string2Date(cr.getScheduledDate());
        	}catch(Exception e){
        		int x = 0;
        		//taken care of below
        	}
        }
        
        if(d==null){
        	Person person = csf.get(Person.class, cr.getId());
    		if(person==null)
    			return null;
    		Date dt = csf.getEarliestActiveScheduledDate(person);
    		d = new java.sql.Date(dt.getTime());
        }
        
        cws = csf.getWorkSchedulesForDate(cr.getId(),d);
        
        List <MWJob> lst = new ArrayList <MWJob> ();
        List<Job> jobs;
        MWJob mwJob;
        for (WorkSchedule ws : cws) {         //implements filter by locator
            if(cr.getLocatorId()==null || ws.getLocatorId().equals(cr.getLocatorId())){
                logger.debug("WorkSchedule   : " + ws.toString());
                logger.debug("       # jobs  : " + (ws.getJobs()!=null?ws.getJobs().size():0));
                //lst.addAll(CopyUtils.copyProperties(MWJob.class, ws.getJobs()));
                if(ws.getJobs()!=null && !ws.getJobs().isEmpty()){
                	jobs = (List<Job>)ws.getJobs();
                	for(Job job: jobs){
                		mwJob = new MWJob();
                		CopyUtils.copyProperties(mwJob, job);
                		if(cr.getCompletionStatus()!=null && !mwJob.getCompletionStatus().equals(cr.getCompletionStatus())){
                			continue;
                		}
                		//add age
                		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
                		Date earliestStart = formatter.parse(mwJob.getEarliestStart());
                		Long age = (System.currentTimeMillis() - earliestStart.getTime())/86400000; //86400000- number of millis in a day  
                		
                		mwJob.setAge(age.toString());
                		
                		//Get job type
                		mwJob.setJobType(TaskTypeRef.getLabel(job.getJobTypeId()));
                		
                		lst.add(mwJob);
                		
                		
                		int taskCount = 0;
                		//set task count
                    	mwJob.setTaskCount(Integer.toString(taskCount));
                	}
                }
            }
        }
        
        //Sort the list. We want to return lowest job id first
        Collections.sort(lst, new Comparator<MWJob>() {
			@Override
			public int compare(MWJob o1, MWJob o2) {
				return o1.getId().compareTo(o2.getId());
			}
        });
        
        
		return new ResponsePage(lst.size(),lst);
    }

    /**
     * This one CLOSES the job (not delete as method name enforced by interface suggests)
     * @param jobId
     * @return
     * @throws Exception
     */
    public String deleteItem(Long jobId) throws Exception {
        String rtn = ITEM_SAVED_OK_MSG;
           ControlSF csf = ServiceLocator.getControlSFLocal( );
           csf.closeJob(jobId);
        return rtn;
    }

    public Object getItem(Long itemId) throws Exception {
        return null;
    }

    public String saveItem(HashMap bean) throws Exception {
        return null;
    }
}

