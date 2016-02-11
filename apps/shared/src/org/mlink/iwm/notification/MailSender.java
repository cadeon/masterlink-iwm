package org.mlink.iwm.notification;

import java.util.Date;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

/**
 */
public class MailSender {
    private static final Logger logger = Logger.getLogger(MailSender.class);
    public final static String SYSTEM_MAIL_ADDRESS = "system.mail.address";
    public final static String MAIL_HOST = "mail.smtp.host";

    /**
     * System.getProperties().get(MAIL_HOST) and System.getProperties().get(SYSTEM_MAIL_ADDRESS) must exist
     * @param message
     * @throws MessagingException
     */
    public static void send(MailMessage message) throws MessagingException{
    	String mailHost = (String)System.getProperties().get(MAIL_HOST);
        if(mailHost==null || mailHost.length()==0 || "null".equalsIgnoreCase(mailHost)){
            throw new MessagingException("System property " + MAIL_HOST  + " is not defined! System is not capable of sending emails");
        }
        if(System.getProperties().get(SYSTEM_MAIL_ADDRESS)==null){
            throw new MessagingException("System property " + SYSTEM_MAIL_ADDRESS  + " is not defined! System is not capable of sending emails");
        }

        // Get a Session object, alternatively use second argument Authenticator if smtp server requires login
        Session session = Session.getDefaultInstance(System.getProperties());

        logger.debug("session acquired ");

        if(logger.isDebugEnabled()){
            logger.debug("setting debug true");            
            session.setDebug(true);
        }

        // construct the message
        Message msg = new MimeMessage(session);
        if (message.getFromLine() != null)
            msg.setFrom(new InternetAddress(message.getFromLine()));
        else
            msg.setFrom(new InternetAddress(System.getProperty(SYSTEM_MAIL_ADDRESS)));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getToLine(), false));
        if(message.getCcLine()!=null)
            msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(message.getCcLine(), false));
        msg.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(System.getProperty(SYSTEM_MAIL_ADDRESS), false));
        if(message.getSubjectLine()!=null)
            msg.setSubject(message.getSubjectLine());



        MimeMultipart mp = new MimeMultipart();
        mp.setSubType("related");
        MimeBodyPart mbp1= new MimeBodyPart();
        mbp1.setContent(message.getBodyText(),message.getContentType());
        mp.addBodyPart(mbp1);

        if(message.getAttachment()!=null){
            MimeBodyPart mbp= new MimeBodyPart();
            //mbp.setDataHandler(new DataHandler(attachment.getBytes(),attachment.getContentType()));
            //06/21/05 nothing works but "image/jpeg"
            //I (andrei) believe contentType here does not mean much. it is file extension will be used to process correct mime type on the client side
            // when email is received
            mbp.setDataHandler(new DataHandler(message.getAttachment().getBytes(),"image/jpeg"));
            mbp.setFileName(message.getAttachment().getName());
            mbp.setDisposition(Part.ATTACHMENT);
            mp.addBodyPart(mbp);
        }
        msg.setContent(mp);
        msg.setSentDate(new Date());
        logger.debug("message assembly complete " + System.currentTimeMillis());

        Transport.send(msg);
        logger.debug("Mail to " + message.getToLine() + " was sent successfully." + System.currentTimeMillis());
    }
}

