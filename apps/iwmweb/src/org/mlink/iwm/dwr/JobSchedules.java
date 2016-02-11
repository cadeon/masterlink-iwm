package org.mlink.iwm.dwr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.JobSchedulesCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DBAccess;

/**
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class JobSchedules implements MaintainanceTable{
    //final  String WORKER_ASSIGNED       ="Job has been added to the worked schedule";
    final  String WORKER_ASSIGNED       ="";
    //final  String WORKER_NOT_ASSIGNED   ="Job has been removed from worker schedule";
    final  String WORKER_NOT_ASSIGNED   ="";
    private static final Logger logger = Logger.getLogger(JobSchedules.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        JobSchedulesCriteria cr = new JobSchedulesCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobSchedulesDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.WorkSchedule.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long personId) throws Exception{
        //NA
        return null;
    }

    /**
     * Return jobs for person on given day. wsid is used as starting point to get person/day pair
     * Return is warapped into  WorkSchedule
     * @param wsId
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.WorkSchedule getItem(Long wsId) throws Exception{
    	org.mlink.iwm.bean.WorkSchedule ws = new org.mlink.iwm.bean.WorkSchedule();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
       //Collection jobs = CopyUtils.copyProperties(Job.class,csf.getJobsOnWorkSchedule(wsId));
        WorkSchedule wsVo  =csf.get(WorkSchedule.class, wsId);
        Collection<Job> wj = csf.getWorkerJobs(wsVo.getPersonId(),wsVo.getDay());
        Collection<org.mlink.iwm.bean.Job> jobBeans = new ArrayList<org.mlink.iwm.bean.Job>();// = CopyUtils.copyProperties(org.mlink.iwm.bean.Job.class, wj);
        org.mlink.iwm.bean.Job jobBean;
        if(wj!=null && !wj.isEmpty()){
        	for(Job job: wj){
        		jobBean = new org.mlink.iwm.bean.Job();
        		CopyUtils.copyProperties(jobBean, job);
        		jobBean.setSkillType(SkillTypeRef.getLabel(job.getSkillTypeId()));
        		jobBean.setStatus(JobStatusRef.getCode(job.getStatusId()));
        		jobBean.setFullLocator(LocatorRef.getFullLocator(job.getLocatorId().longValue()));
        		jobBeans.add(jobBean);
        	}
        }
        ws.setJobs(jobBeans);
        return ws;
    }

    /**
     * Qucker and finner (just number of jobs) version of getItem for quick number job reference for worker day
     * This resulted in too many ajax call especially in IE where users can inadvertedly invoke selection
     * Need this in favor getItem as change event on scheduledDateSelect.options (see jobschedule.jsp) which in turn resulted in application deadlock in jboss. 
     * Same jobbean instance was requested too many times when pessimistic concurrency was used
     */
    public org.mlink.iwm.bean.WorkSchedule getItem2(Long wsId) throws Exception{
    	org.mlink.iwm.bean.WorkSchedule ws = new org.mlink.iwm.bean.WorkSchedule();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        WorkSchedule wsVo  =csf.get(WorkSchedule.class, wsId);
        CopyUtils.copyProperties(ws, wsVo);
        String sql="SELECT DISTINCT J.ID, J.JOB_TYPE_ID as JOBTYPEID FROM Job j, Job_Schedule js, WORK_SCHEDULE WS " +
                "WHERE J.ID = JS.JOB_ID AND JS.WORK_SCHEDULE_ID=WS.ID AND WS.PERSON_ID=? AND WS.DAY=? AND JS.DELETED_TIME IS NULL";
        List params = new ArrayList(); 
        params.add(wsVo.getPersonId()); 
        params.add(wsVo.getDay());
        List rtn = DBAccess.executeQuery(sql,params);
        //Collection jobs = CopyUtils.copyProperties(org.mlink.iwm.bean.Job.class,rtn);
        Collection jobs = new HashSet();
        org.mlink.iwm.bean.Job job;
        Map map;
        if(rtn!=null && rtn.size()>0){
        	for (Object source : rtn) {
        		map = (Map)source;
        		job = new org.mlink.iwm.bean.Job();
        		job.setId(((BigDecimal)map.get("ID")).toString());
        		job.setJobTypeId(((BigDecimal)map.get("JOBTYPEID")).toString());
        		jobs.add(job);
        	}
        }
        ws.setJobs(jobs);
        return ws;
    }


    /**
     * Adds/Removes job to workschedule
     * @param map
     * @return
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
        String rtn;
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        JobSchedule js = new JobSchedule();
        CopyUtils.copyProperties(js, map);
        String isAssigned;
        
        try{
        	isAssigned = (String)map.get("isAssigned");
            if(Constants.YES.toString().equals(isAssigned)){
                rtn = WORKER_ASSIGNED;
                csf.createJobSchedule(Long.valueOf(js.getId()),Long.valueOf(js.getJobId()));
            }else{
                rtn = WORKER_NOT_ASSIGNED;
                csf.removeJobSchedule(Long.valueOf(js.getId()),Long.valueOf(js.getJobId()));
            }
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

}
