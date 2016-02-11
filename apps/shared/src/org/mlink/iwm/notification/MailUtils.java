package org.mlink.iwm.notification;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.UserTrackHelper;
import org.mlink.iwm.jms.PTPClient;

import javax.mail.MessagingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 20, 2006
 */
public class MailUtils {
    private static final Logger logger = Logger.getLogger(MailUtils.class);

    /**
     * Parses the parameter for valid email addresses
     * @param emails is one or more email adresses separated by space(\\s), or | or ; or ,
     * @return String comma delimeted list of email addresses
     */
    public static String format(String emails){
        if(emails==null) return "";
        StringBuilder target=new StringBuilder();
        // Create a pattern to match breaks
        Pattern p = Pattern.compile("[,\\s|;]+");
        // Split input with the pattern
        String[] result = p.split(emails);
        for (int i=0; i<result.length; i++)       {
            if(!isEmailValid(result[i])) continue;
            target.append(result[i]);
            if(i+1!=result.length) target.append(",");
        }
        return target.toString();
    }

    /**
     * Helper method to convert comma delimeted list of email strings to array
     * @param emails
     * @return array
     */
    public static String [] toArray(String emails){
        if(emails==null) return new String[0];

        String [] tmp = emails.split(",");
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = tmp[i].trim();
        }
        return tmp;
    }

    /**
     * Helper method to convert array of strings to comma delimeted string
     * @param emails
     * @return comma delimeted string
     */
    public static String toString(String [] emails){
        if(emails==null) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < emails.length; i++) {
            sb.append(emails[i]);
            if(i+1!=emails.length) sb.append(",");
        }
        return sb.toString();
    }

    public static boolean isEmailValid(String emailAddress){
        try{
            //InternetAddress.parse(emailAddress);
            Pattern alpha = Pattern.compile("[a-zA-Z0-9]@([\\w-]+\\.)+[\\w-]{2,4}$");
            Matcher matcher = alpha.matcher(emailAddress.trim());
            if(matcher.find()) return true;
        }catch(Exception ae){
            return false;
        }
        return false;
    }

    /**
     * filter array of Strings according to regExp parameter
     * @param emails
     * @param regExp filter criteria
     * @return filtered array
     */
    public static String [] filter(String [] emails, String regExp){
        if(emails==null) return new String[0];
        Pattern pattern = Pattern.compile(regExp);
        List <String>result = new ArrayList<String>();
        for(String s : emails) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) result.add(s);
        }
        String [] rtn = new String [result.size()];
        for (int i = 0; i < result.size(); i++) {
            rtn[i] =  result.get(i);

        }
        return rtn;
    }

    /**
     * Non mobile recipients are those that do not start with a number in the email address
     * @return List of strings representing email addresses
     */
    public static String [] filterEmailAddresses(String [] emails){
        String regex = "\\D[a-zA-Z0-9]@([\\w-]+\\.)+[\\w-]{2,4}$";
        return filter(emails,regex);
    }

    /**
     * Similar to above but takes comma delimeted String and pasres Non mobile emails
     * @param emails
     * @return String
     */
    public static String  filterEmailAddresses(String  emails){
        return toString(filterEmailAddresses(toArray(format(emails))));
    }

    public static String [] filterSMSAddresses(String [] emails){
        String regex = "[0-9]@([\\w-]+\\.)+[\\w-]{2,4}$";
        return filter(emails,regex);
    }

    /**
     * Similar to above but takes comma delimeted String and parses mobile emails
     * @param emails
     * @return String
     */
    public static String  filterSMSAddresses(String  emails){
        return toString(filterSMSAddresses(toArray(format(emails))));
    }

    public static void informSupport(String subject, String body){
        logger.info("Informing support:\nSubject:"+subject);
        try {
            MailMessage  msg = new MailMessage();
            msg.setToLine((String)System.getProperties().get(MailSender.SYSTEM_MAIL_ADDRESS));
            msg.setSubjectLine(subject);
            if(subject!=null && subject.length()>200){
                subject=subject.substring(0,200);
            }
            msg.setSubjectLine("user " + UserTrackHelper.getUser() + ":" + subject);
            msg.setBodyText(body);
            MailSender.send(msg);
        } catch (MessagingException e) {
            logger.error("Error sending email message!" + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void informSupport(String subject, Throwable e){
        logger.info("Informing support:\nSubject:"+subject+"\nException:"+e.getMessage());
        if(subject!=null && subject.length()>200){
            subject=subject.substring(0,200);
        }
        new ErrorOccured(subject, e).execute();
        //PTPClient.sendObjectAsync(new ErrorOccured(subject, e));
    }

    public static void informSupport(Throwable e){
        informSupport("Exception:"+e.getMessage(),e);
    }
}
