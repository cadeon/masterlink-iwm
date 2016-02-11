package org.mlink.iwm.notification;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.lookup.ExternalWorkRequestProblemRef;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.velocity.VelocityConfig;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 19, 2006
 */
public class WorkRequestCreated extends MailNotificator{
    private static final Logger logger = Logger.getLogger(WorkRequestCreated.class);

    TenantRequest tr;
    public WorkRequestCreated(TenantRequest tr) {
        this.tr=tr;
    }

    public Template getEmailTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/WorkRequestCreated.vm");
    }

    public Template getSMSTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/WorkRequestCreated_SMS.vm");
    }

    protected String getToLine() throws Exception {
        return tr.getEmergencyContact();
    }

    protected MailMessage createMessage() throws Exception {
        MailMessage mail = new MailMessage();
        mail.setSubjectLine("Job " + tr.getJobId() + ". Urgent");
        return mail;
    }

    protected VelocityContext createContext(Template template) throws Exception{
        logger.debug("populating template");

        VelocityContext context = new VelocityContext();
        context.put("emergencyContact",tr.getEmergencyContact());
        context.put("jobId",tr.getJobId());
        context.put("requestCategory",ExternalWorkRequestProblemRef.getLabel(Long.valueOf(tr.getProblemId())));
        context.put("tenantName",tr.getTenantName());
        context.put("tenantPhone",tr.getTenantPhone());
        //context.put("jobDescription",htmlWithBrakes(tr.getNote()));  html formatting shows up on cell phones
        context.put("jobDescription",tr.getNote());                    // rather prefer no line breaks here to keep cell phones clean
        context.put("location", LocatorRef.getDescription(tr.getLocatorId()));
        return context;
    }
}
