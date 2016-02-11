package org.mlink.iwm.notification;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.UserTrackHelper;
import org.mlink.iwm.jms.Command;

import java.util.Collection;
import java.util.ArrayList;
import java.io.StringWriter;

public abstract class MailNotificator implements Command {
    private static final Logger logger = Logger.getLogger(MailNotificator.class);
    public void execute() {
        try{
            for (Object mail1 : prepareMessages()) {
                MailMessage mail = (MailMessage) mail1;
                mail.validate();

                // send email message
                MailSender.send(mail);
            }
        } catch (Exception e) {
            //to avoid recursion check for class name
            /*String clazz=getClass().getName();
            if(!ErrorOccured.class.getName().equals(clazz)){
                new ErrorOccured(clazz.substring(clazz.lastIndexOf(".")+1), e).execute();
            }*/
            e.printStackTrace();
        }
    }


    // call to createContext implemented by a subclass with specifics to its business content
    protected abstract VelocityContext createContext(Template template) throws Exception;
    protected abstract MailMessage createMessage() throws Exception;
    protected abstract String getToLine() throws Exception;
    protected abstract Template getEmailTemplate() throws Exception;
    protected abstract Template getSMSTemplate() throws Exception;


    public static boolean isEmailValid(String emailAddress){
        return MailUtils.isEmailValid(emailAddress);
    }

    /**
     * Does the work of parsing address line to filter cell phones addresses.
     * Cell phones have limited display options thus, separate templates desirable for them
     * Subclasses are responsible for implementeing velocity templates since they are specific to each business case
     * @return
     * @throws Exception
     */
    protected Collection<MailMessage> prepareMessages() throws Exception {
        Collection <MailMessage> messages = new ArrayList<MailMessage>();
        String emails = MailUtils.filterEmailAddresses(getToLine());
        String smses = MailUtils.filterSMSAddresses(getToLine());
        StringWriter sw;
        if (emails != null && emails.length() != 0) {
            MailMessage mail = createMessage();
            mail.setToLine(emails);
            Template template = getEmailTemplate();
            VelocityContext context = createContext(template);
            messages.add(mail);
            sw = new StringWriter();
            template.merge(context, sw);

            String sendingSystem = java.net.InetAddress.getLocalHost().getHostName();
            String identifyingDB = Config.getProperty(Config.PRODUCTION_SCHEMA);
            String user="";
            if(UserTrackHelper.getUser()!=null){
                user = UserTrackHelper.getUser();
            }
            sw.write("<div style='color:#AAA;font-size:10px;margin-top:20pt;'>&copy; MasterLink Corporation, All Rights Reserved "+ sendingSystem + "/"+ identifyingDB + "/"+user + " </div>");
            mail.setBodyText(sw.toString());
        }
        if (smses != null && smses.length() != 0) {
            MailMessage mail = createMessage();
            mail.setContentType(MailMessage.PLAIN);
            mail.setToLine(smses);
            Template template = getSMSTemplate();
            VelocityContext context = createContext(template);
            messages.add(mail);
            sw = new StringWriter();
            template.merge(context, sw);
            mail.setBodyText(sw.toString());
        }
        return messages;
    }


    /**
     * nice little method to replace cariage return characters with html break <br>
     * @param source
     * @return modified string
     */
    protected  String htmlWithBrakes(String source)   {
        return ConvertUtils.htmlWithBrakes(source);
    }
}
