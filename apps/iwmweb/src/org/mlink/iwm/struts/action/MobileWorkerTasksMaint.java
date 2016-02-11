package org.mlink.iwm.struts.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.MobileWorkerTasksMaintForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class MobileWorkerTasksMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(MobileWorkerTasksMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        MobileWorkerTasksMaintForm form  = (MobileWorkerTasksMaintForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            ControlSF csf = ServiceLocator.getControlSFLocal( );

            String username = request.getUserPrincipal().getName();
            if(username== null){
                throw new WebException("user is not logged in!");
            }
            JobSchedule js = csf.get(JobSchedule.class, StringUtils.getLong(form.getId()));
            WorkSchedule ws = csf.get(WorkSchedule.class, js.getWorkScheduleId());
            Person p = psf.get(Person.class, ws.getPersonId());

            Job job = csf.get(Job.class, js.getJobId());
            TenantRequest tr = job.getTenantRequest();
            
            if(tr != null){
            	CopyUtils.copyProperties(form,tr);
            	form.setTenantNote(tr.getNote());
            }else{
            	CopyUtils.copyProperties(form, new TenantRequest());
            	form.setTenantNote(null);
            }
            CopyUtils.copyProperties(form,job);
            ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
            ObjectEntity ovo = isf.get(ObjectEntity.class, job.getObjectId());
            Party party = p.getParty();
            PageContext context = new PageContext(party.getName());
            context.add("jobId",String.valueOf(job.getId()));
            context.add("jobDescription",job.getDescription());
            context.add("object",ovo.getObjectRef());
            String classCode = TargetClassRef.getLabel(ovo.getClassId());
            context.add("class",classCode);
            String fullLocator = LocatorRef.getFullLocator(job.getLocatorId());
            context.add("locator",fullLocator);
            form.setPageContext(context);
            if(job.getProject()!=null){
                Project pvo = job.getProject();
                context.add("project", pvo.getName() + "(Id=" + pvo.getId() + ")");
            }
            String jobStatusStr = JobStatusRef.getCode(job.getStatusId());
            String workerStatusStr = WorkScheduleStatusRef.getCode(ws.getStatusId());
            context.add("jobStatus", jobStatusStr);
            
            form.setJobStatus(jobStatusStr);
            form.setWorkerStatus(workerStatusStr);
            form.setId(String.valueOf(js.getId()));
            form.setJobId(String.valueOf(job.getId()));
            form.setObjectId(String.valueOf(ovo.getId()));
            form.setJobTypeId(job.getJobTypeId());
            form.setObjectRef(ovo.getObjectRef());
            form.setFullLocator(LocatorRef.getFullLocator(job.getLocatorId()));
            
            //BIG NOTE: THIS ASSUMES 1:1 JOB -> JOBTASK. Model currently supports 1:many.
            Collection<JobTask> jts = job.getJobTasks();
            form.setJobTaskId(String.valueOf(jts.iterator().next().id));
            
            //Here we set our creator info
            SimpleDateFormat createdDT = new SimpleDateFormat("MMM dd, HH:mm:ss");
            form.setCreatedDate(createdDT.format(job.getCreatedDate()));
            
            SimpleDateFormat closedDT = new SimpleDateFormat("MMM dd, HH:mm:ss");
            if (job.getCompletedDate() !=null){
            form.setClosedDate(closedDT.format(job.getCompletedDate()));
            } else {
            	form.setClosedDate("");
            }
            
            if (job.getCreatedBy().equals("Planner") || job.getCreatedBy().equals("super")){
            	form.setName("System");
            	form.setEmail("");
            	form.setPhone("");
            } else {
            	if (job.getCreatedBy().equals("tenant")){
            		//Fill in tenant info
            		tr = job.getTenantRequest();
            		form.setName(tr.getTenantName());
            		form.setEmail(tr.getTenantEmail());
            		form.setPhone(tr.getTenantPhone());
            		
            	} else {
            		//Made by a user. Find and fill in user info.
            		User user = psf.getUserByName(job.getCreatedBy());
            		Party creator = user.getPerson().getParty();
            		
            		form.setName(creator.getName());
            		form.setPhone(creator.getPhone());
            		form.setEmail(creator.getEmail());
            	}
            }
            
            
        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
