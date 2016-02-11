package org.mlink.iwm.notification;

import org.mlink.iwm.struts.form.DefectReportForm;
import org.mlink.iwm.velocity.VelocityConfig;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: July 20, 2006
 */
public class DefectReportSubmitted extends MailNotificator{
    private static final Logger logger = Logger.getLogger(DefectReportSubmitted.class);

    DefectReportForm defect;
    public DefectReportSubmitted(DefectReportForm form) {
        this.defect=form;
    }

    public Template getEmailTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/DefectReportSubmitted.vm");
    }

    public Template getSMSTemplate() throws Exception {
        return getEmailTemplate();
    }

    protected String getToLine() throws Exception {
        return(String)System.getProperties().get(MailSender.SYSTEM_MAIL_ADDRESS);
    }

    protected MailMessage createMessage() throws Exception {
        MailMessage mail = new MailMessage();
        String subject = defect.getReportType() + " Report";
        if(defect.getExceptionRef()!=null) subject=subject + " for Ref " + defect.getExceptionRef();
        mail.setSubjectLine(subject);
        return mail;
    }

    protected VelocityContext createContext(Template template) throws Exception{
        logger.debug("populating template ");
        VelocityContext context = new VelocityContext();
        context.put("issueName",defect.getIssueName());
        context.put("reportType",defect.getReportType());
        context.put("tenantName",defect.getTenantName());
        context.put("tenantPhone",defect.getTenantPhone());
        context.put("tenantEmail",defect.getTenantEmail());
        context.put("description",htmlWithBrakes(defect.getNote()));
        context.put("exception",defect.getException());
        return context;
    }

}

