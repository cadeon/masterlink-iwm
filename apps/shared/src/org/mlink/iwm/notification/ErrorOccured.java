package org.mlink.iwm.notification;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.velocity.VelocityConfig;

/**
 * Created by Andrei Povodyrev
 * Date: Feb 28, 2006
 */
public class ErrorOccured extends  MailNotificator {
    Throwable error;
    String subject;

    public ErrorOccured(String subject, Throwable error) {
        this.error = error;
        this.subject = subject;
    }

    public Template getEmailTemplate() throws Exception {
        return VelocityConfig.getTemplate("org/mlink/iwm/velocity/ErrorOccured.vm");
    }

    protected Template getSMSTemplate() throws Exception {
        return getEmailTemplate();
    }

    protected String getToLine() throws Exception {
        return(String)System.getProperties().get(MailSender.SYSTEM_MAIL_ADDRESS);
        //return "masterlink.iwm@gmail.com";
    }

    protected MailMessage createMessage() throws Exception {
        StringBuilder sb = new StringBuilder();
        MailMessage mail = new MailMessage();

        if(Config.getProperty(Config.PRODUCTION_SCHEMA)!=null)
            sb.append(Config.getProperty(Config.PRODUCTION_SCHEMA) + ":");

        sb.append(subject);
        mail.setSubjectLine(sb.toString());

        return mail;
    }

    protected VelocityContext createContext(Template template) throws Exception{
        VelocityContext context = new VelocityContext();
        Throwable root = error;
        while(root.getCause()!=null){
            root = root.getCause();
        }
        context.put("exceptionmessage",root.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        root.printStackTrace(pw);
        context.put("exceptionstacktrace",sw);
        return context;
    }



}
