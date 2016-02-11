package org.mlink.iwm.struts.action;

import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.PriorityRef;
import org.mlink.iwm.lookup.SkillLevelRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.JobDetailsForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei Date: Oct 12, 2006
 */
public class JobDetails extends BaseAction {
	private static final Logger logger = Logger
			.getLogger(JobDetails.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		ControlSF csf = ServiceLocator.getControlSFLocal( );
		PolicySF psf = ServiceLocator.getPolicySFLocal( );
		JobDetailsForm form = (JobDetailsForm) aform;
		
		Job job = new Job(); //In case the try fails, we have a blank job.
        
    	try{
    		job = csf.get(Job.class, Long.decode(form.getId()));
    		BeanUtils.copyProperties(form,job);
    		
    	}catch(Exception e){
            String msg = "Error: "+e.getMessage()+" in copying Job info into Form.";
            logger.warn(msg);
            form.setMessage(msg);
        }
		
    	//Set up our job info
    	//Date formaters
    	SimpleDateFormat dateTime = new SimpleDateFormat("MMM dd, HH:mm");
    	SimpleDateFormat MDY = new SimpleDateFormat("MMM dd, yyyy");
    	
    	
    	if (job.getTotalTime() != null){
    	form.setTotalTime(Long.toString(job.getTotalTime()));
    	}
    	
		//Set specific properties here. 
    	//In general, we're setting text descriptions and ids. The ids are used for links.
    	//Often, you'll see things below set to null. Nulls are scary but correct in this case. 
    	//If something is null, it's 'not present' and struts can therefore remove labels and other things from the page.
    	ObjectEntity obj = csf.get(ObjectEntity.class, job.getObjectId());
    	form.setObjectRef(obj.getObjectRef());
    	form.setObjectId(job.getObjectId());
    	
    	Locator loc = csf.get(Locator.class, job.getLocatorId());
    	form.setFullLocator(loc.getFullLocator());
    	form.setLocatorId(job.getLocatorId());
    	
    	//Set skill and level string. This gets displayed as it is formatted here.
    	String skillName = SkillTypeRef.getLabel(job.getSkillTypeId());
    	String skillLevel = SkillLevelRef.getLabel(job.getSkillLevelId());
    	form.setskillAndLevel(skillName + " Level " + skillLevel);
    	
    	form.setPriority(PriorityRef.getLabel(job.getPriorityId()));
    	
    	if (job.getOrganizationId() != null && job.getOrganizationId() > 0L){
    	Organization jobOrg = csf.get(Organization.class, job.getOrganizationId());
    	form.setFullOrganization(jobOrg.getFullOrganization());
    	} else {
    		form.setFullOrganization("No Org requirement");
    	}
    	
    	//BIG NOTE: THIS ASSUMES 1:1 JOB -> JOBTASK. Model currently supports 1:many.
        Collection<JobTask> jts = job.getJobTasks();
        form.setJobTaskId(String.valueOf(jts.iterator().next().id));
    	
        //Pretty Estimated Time
        if (job.getEstTime() > 0){
    		//make time pretty
        	String estTime = "";
    		Integer hours = (int) (job.getEstTime() / 60);
    		Integer minutes = (int) (job.getEstTime() % 60);
    		if (hours >= 1){estTime += hours + " hr ";}
    		if (minutes >=1){estTime += minutes + " mins";}
    		form.setEstTime(estTime);
        }
        
        //State handling
        String stateString = null;
        if (job.getCompletedDate() !=null){        
        	form.setClosedDate(MDY.format(job.getCompletedDate()));
        	stateString = "Closed " + dateTime.format(job.getCompletedDate());
        	if (job.getTotalTime() != 0 && job.getTotalTime() != null){
        		stateString += ". ";
        		//make time pretty
        		Integer hours = (int) (job.getTotalTime() / 60);
        		Integer minutes = (int) (job.getTotalTime() % 60);
        		if (hours >= 1){stateString += hours + " hr ";}
        		if (minutes >=1){stateString += minutes + " mins ";}
        		stateString +="Recorded";
        }
        } else {
        //Other states
        //TODO: Use JSH to make this more useful
        stateString = JobStatusRef.getLabel(job.getStatusId());
        }
        
        form.setStateString(stateString);
        
        
        //Here we set our creator info
        
        form.setCreatedDate(dateTime.format(job.getCreatedDate()));
        
        if (job.getCreatedBy().equals("Planner") || job.getCreatedBy().equals("super")){
        	form.setName("System");
        	form.setEmail(null);
        	form.setPhone(null);
        	//For some reason we also have to *clear* the tenant note.
    		form.setTenantNote(null);
        } else {
        	if (job.getCreatedBy().equals("tenant")){
        		//Fill in tenant info
        		TenantRequest tr = job.getTenantRequest();
        		form.setName(tr.getTenantName());
        		form.setEmail(tr.getTenantEmail());
        		form.setPhone(tr.getTenantPhone());
        		
        		form.setTenantNote(tr.getNote());
        		
        	} else {
				//Made by a user. Find and fill in user info.
        		User user = psf.getUserByName(job.getCreatedBy());
        		Party creator = user.getPerson().getParty();
        		
        		form.setName(creator.getName());
        		form.setPhone(creator.getPhone());
        		form.setEmail(creator.getEmail());
        		
        		//For some reason we also have to *clear* the tenant note.
        		form.setTenantNote(null);
        	}
        }

        
        form.setEarliestDate(MDY.format(job.getEarliestStart()));
        if (job.getLatestStart() != null){
        form.setLatestDate(MDY.format(job.getLatestStart()));
        } else {
        	form.setLatestDate("Never Expires");
        }
        
        //Set a title
    	PageContext context = new PageContext(form.getId() +" - " + form.getDescription());
        form.setPageContext(context);
        
        
        
		String forward = form.getForward() == null ? "read" : form.getForward();
		logger.debug("execute " + forward);
		return findForward(mapping, request, forward);
	}
}
