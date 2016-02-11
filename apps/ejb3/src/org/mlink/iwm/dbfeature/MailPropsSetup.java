package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 25, 2006
 * Time: 2:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailPropsSetup extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(MailPropsSetup.class);

    public boolean isInstalled(){
        boolean isInstalled = false;
        String sql = "SELECT 1 FROM SYSTEM_PROPS WHERE PROPERTY='mail.smtp.host'";
        try {
            if((DBAccess.execute(sql)).size()>0) 
                isInstalled=true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
        return isInstalled;
    }

    public void install() {
    	String sql1 = "INSERT INTO SYSTEM_PROPS (PROPERTY,VALUE,DESCRIPTION) VALUES ('mail.smtp.host','mail.bellsouth.net','smtp server for outgoing email')";
        String sql2 = "INSERT INTO SYSTEM_PROPS (PROPERTY,VALUE,DESCRIPTION) VALUES ('system.mail.address','iwm-mailer@masterlinkcorp.com','system email address')";
        try {
            DBAccess.executeUpdate(sql1);
            DBAccess.executeUpdate(sql2);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
}
