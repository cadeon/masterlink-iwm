package org.mlink.iwm.notification;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.hibernate.Hibernate;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.velocity.VelocityConfig;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 19, 2006
 */
public class JobAssigned extends MailNotificator{
    private static final Logger logger = Logger.getLogger(WorkRequestCreated.class);

    Job job;
    Person assignee;
    Person assigner;
    public JobAssigned(Job job, Person assignee, Person assigner) {
        this.job=job;
        this.assignee=assignee;
        this.assigner=assigner;
    }

    public Template getEmailTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/JobAssigned.vm");
    }

    public Template getSMSTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/JobAssigned_SMS.vm");
    }

    protected String getToLine() throws Exception {
        return assignee.getParty().getEmail();
    }

    protected MailMessage createMessage() throws Exception {
        MailMessage mail = new MailMessage();
        mail.setSubjectLine("Assigned Job #" + job.getId());
        return mail;
    }

    protected VelocityContext createContext(Template template) throws Exception{
        logger.debug("populating template");
        
        VelocityContext context = new VelocityContext();
        context.put("assigner_name",assigner.getParty().getName());
        
        context.put("job_id",job.getId());
        context.put("job_description",job.getDescription());
        context.put("full_locator", LocatorRef.getDescription(job.getLocatorId()));
        context.put("job_type", TaskTypeRef.getLabel(job.getJobTypeId()));
        context.put("job_est", job.getEstTime());
        
        
        //All of the above should exist for every job. The below we need to check if null and potentially create labels / formatting
        context.put("assigner_email", (assigner.getParty().getEmail() == null) ? "" : assigner.getParty().getEmail());
        context.put("assigner_phone", (assigner.getParty().getPhone() == null) ? "" : assigner.getParty().getPhone());
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal(); 
        ObjectEntity jobObject = isf.get(ObjectEntity.class, job.getObjectId());
        context.put("job_object", (jobObject.getObjectRef().startsWith("AreaTarget")) ? "" : ("<B>Object:</b> " + jobObject.getObjectRef()));
        context.put("job_super_note", (job.getNote() == null) ? "" : ("<b>Supervisor note: </b>" + job.getNote()));
        
        if (job.getTenantRequest() != null){
        	TenantRequest tr = job.getTenantRequest();
        context.put("requester_info", 
        		"<b>Requester Info</b><br>" +
        		"Name: " + tr.getTenantName() +
        		"<br>Email: " + tr.getTenantEmail() +
        		"<br>Phone: " + tr.getTenantPhone() +
        		"<br>Note: " + tr.getNote()
        		);
        } else {
        	context.put("requester_info", "");
        }
        
        
        return context;
    }
}
