package org.mlink.iwm.dwr;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.JobsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.BaseEntity;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DBAccess;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public class Jobs implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
    	//test code for using ejb3 to access hibernate-agent 
    	/*Context ctx = new InitialContext();
        CustomerRemote customerBean = (CustomerRemote) ctx.lookup("CustBean");
        // let's create customer#1
        long id = customerBean.createCustomer("Jai", "Pai");
        System.out.println("Jai Pai created with id = " + id);
        
        // now let's find customers with first name=Jai
        System.out.println("Searching for customers with first name Jai");
        List<Customer> customers = customerBean.getCustomers("Jai");
        System.out.println("Found " + customers.size() + " customers with first name Jai");
        */
        JobsCriteria cr = new JobsCriteria(criteria);
        SessionUtil.setAttribute("JobsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Job.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ControlSF csf = ServiceLocator.getControlSFLocal( );
		//TODO:
		Job job = new Job();
		job.setId(itemId);
		csf.remove((BaseEntity) job);
        return rtn;
    }

    public org.mlink.iwm.bean.ProjectJob getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.ProjectJob form = new org.mlink.iwm.bean.ProjectJob();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Job vo = csf.get(Job.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setStatus(JobStatusRef.getCode(vo.getStatusId()));
        if(vo.getProject()!=null){
        	form.setProjectId(Long.toString(vo.getProject().getId()));
        }
        TenantRequest tr = vo.getTenantRequest();
        if(tr!=null){
        	form.setTenantRequestId(Long.toString(tr.getId()));
        	form.setTenantName(tr.getTenantName());
        	form.setTenantEmail(tr.getTenantEmail());
        	form.setTenantNote(tr.getNote());
        	form.setTenantPhone(tr.getTenantPhone());
        }
        return form;
    }

    /**
     * This method is used by job update page and job create wizard
     * in case of the wizard the prep work is done by JobCreateWizard
     * @param bean
     * @return
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception {
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        String projectIdStr = (String)map.get("projectId");
        
        String rtn = ITEM_SAVED_OK_MSG;
        try{
        long id = Long.parseLong((String)map.get("id"));
        if(id > 0){
        	//form.setObjectId(Long.toString(vo.getObjectId()));
            //form.setLocatorId(Long.toString(vo.getLocatorId()));
        	//No tenent req info can be updated here. but TR should still be set in the Job while updating
        	Job job = csf.get(Job.class, id);
            Job job1 = new Job();
        	CopyUtils.copyProperties(job1,map);
        	job.setDescription(job1.getDescription());
        	job.setNote(job1.getNote());
        	job.setNumberOfWorkers(job1.getNumberOfWorkers());
        	job.setEstTime(job1.getEstTime());
        	job.setPriorityId(job1.getPriorityId());
        	job.setSkillTypeId(job1.getSkillTypeId());
        	job.setSkillLevelId(job1.getSkillLevelId());
        	job.setJobTypeId(job1.getJobTypeId());
        	job.setOrganizationId(job1.getOrganizationId());
        	job.setScheduleResponsibilityId(job1.getScheduleResponsibilityId());
        	job.setStatusId(job1.getStatusId());
        	job.setEarliestStart(job1.getEarliestStart());
        	job.setLatestStart(job1.getLatestStart());
        	
        	Integer status = job.getStatusId();
        	int dunStatus = JobStatusRef.getIdByCode(JobStatusRef.Status.DUN);
        	if((status==dunStatus) &&job.getCompletedDate()==null){
        		//set completed date as null
        		job.setCompletedDate(new Timestamp(System.currentTimeMillis()));
        	}
        	
        	String tenantRequestIdStr = (String)map.get("tenantRequestId");
            if(tenantRequestIdStr!=null){
            	Long tenantRequestId = Long.parseLong(tenantRequestIdStr);
            	TenantRequest tr = csf.get(TenantRequest.class, tenantRequestId);
            	//CopyUtils.copyProperties(tr,map);
                //tr.setNote((String)map.get("tenantNote"));
                //tr.setId(tenantRequestId);
                //csf.update(tr);
                job.setTenantRequest(tr);
            }     
            if(projectIdStr!=null && projectIdStr.length()>0){
            	job.setProject(csf.get(Project.class, Long.parseLong(projectIdStr)));
            }
            
            csf.update(job);
        }else{
            /*List selectedTasks = (List)CopyUtils.copyProperties(TaskVO.class,(List)SessionUtil.getAttribute("taskSelectedForJob"));
            String createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
            Long jobId = csf.createJob(createdBy,selectedTasks,vo.getProjectId());
            Job vo0 = csf.getJob(jobId);
            CopyUtils.copyProperties(vo0,vo); //incorporate updates from ui and job created off tasks
            vo0.setId(jobId);  //make sure jobId is not lost during copyProperties
            csf.updateJob(vo0);*/
            // now is taken care if by JobCreateWizard
            JobCreateWizard action = new JobCreateWizard();
            return action.saveItem(map);
        }
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public void runScheduler() throws Exception {
        BaseAccess.getWorldConnection().runAllButPlanner();
    }

    /**
     * Purge (expire all active jobs)
     * @return number of jobs purged
     * @throws Exception
     */
    public int purgeJobs() throws Exception {
        //expire all active jobs but the ones scheduled
        String sql = "UPDATE JOB J SET J.STATUS_ID=(SELECT JSR.ID FROM JOB_STATUS_REF JSR WHERE JSR.CODE='"+JobStatusRef.Status.EJO+"'), J.COMPLETED_DATE=SYSDATE " +
                     "WHERE EXISTS (SELECT 1 FROM ACTIVE_JOBS_VIEW AJ WHERE AJ.ID=J.ID AND AJ.CODE <> '"+JobStatusRef.Status.DPD+"')";
        int jobsExpired =  DBAccess.executeUpdate(sql);

        //complete all scheduled (DPD) jobs
        String sql2 = "UPDATE JOB J SET J.STATUS_ID=(SELECT JSR.ID FROM JOB_STATUS_REF JSR WHERE JSR.CODE='"+JobStatusRef.Status.DUN+"'), J.COMPLETED_DATE=SYSDATE " +
                     "WHERE EXISTS (SELECT 1 FROM ACTIVE_JOBS_VIEW AJ WHERE AJ.ID=J.ID AND AJ.CODE = '"+JobStatusRef.Status.DPD+"')";
        int jobsCompleted = DBAccess.executeUpdate(sql2);

        //remove all todays job schedules, so worker's schedule is clean
        String sql3 = "UPDATE JOB_SCHEDULE js SET DELETED_TIME=SYSDATE WHERE JS.WORK_SCHEDULE_ID IN (SELECT ID FROM WORK_SCHEDULE ws WHERE ws.DAY=today() and ws.id=js.work_schedule_id)" ;
        int jobSchedules = DBAccess.executeUpdate(sql3);

        return jobsCompleted+jobsExpired;
    }
}
