package org.mlink.iwm.notification;

import org.apache.log4j.Logger;


/**
 * Created by Andrei Povodyrev
 * Date: Aug 16, 2005
 */
public class MailMessage {
    private static final Logger logger = Logger.getLogger(MailMessage.class);

    public static String PLAIN = "text/plain";
    public static String HTML = "text/html";
    String toLine;
    String subjectLine;
    String fromLine;
    String ccLine;
    String bodyText;
    String contentType = HTML;
    Attachment attachment;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public String getToLine() {
        return toLine;
    }

    public void setToLine(String toLine) {
        this.toLine = MailUtils.format(toLine);
    }

    public String getSubjectLine() {
        return subjectLine;
    }

    public void setSubjectLine(String subjectLine) {
        this.subjectLine = subjectLine;
    }

    public String getFromLine() {
        return fromLine;
    }

    public void setFromLine(String fromLine) {
        this.fromLine = MailUtils.format(fromLine);
    }

    public String getCcLine() {
        return ccLine;
    }

    public void setCcLine(String ccLine) {
        this.ccLine = MailUtils.format(ccLine);
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String toString() {
        return "to:\n" + toLine + "\ncc:\n" + ccLine + "\nsubject:\n"+subjectLine + "\nbody:\n" + bodyText;
    }


    public void validate() throws NotificationException{
        if(!MailUtils.isEmailValid(getToLine()))
            throw new NotificationException("recipient.TO email address(s) is malformed! " + getToLine());

        if(getCcLine()!=null && !MailUtils.isEmailValid(getCcLine()))
            throw new NotificationException("recipient.CC email address(s) is malformed! " + getToLine());

        if(getFromLine()!=null && !MailUtils.isEmailValid(getFromLine()))
            throw new NotificationException("sender email address is malformed! " + getToLine());

        if(getSubjectLine()==null) logger.warn("Email message has no subject");
        if(getBodyText()==null) logger.warn("Email message has no body");
    }

}


