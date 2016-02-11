package org.mlink.iwm.session.exthandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.iwml.LoginUser;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.XMLParser;

//@Stateless
//@WebService(endpointInterface = "org.mlink.iwm.session.exthandler.ExtHandler")
//@WebContext(contextRoot = "/IWMWS")
public class ExtHandlerBean implements ExtHandlerLocal, ExtHandlerRemote {
	private static Log log = LogFactory.getLog(ExtHandlerBean.class);
	
	public String requestInfo(String text) {
		log.debug("doExtHandler in webservice ejb");
		Object obj = XMLParser.getBean(text);
		LoginUser x = authenticate((LoginUser)obj);
		String xmlOutput = XMLParser.toXml(x);
		return xmlOutput;
	}
	
	private LoginUser authenticate(LoginUser loginUser) {
        log.debug("authenticating user: " + loginUser.getJ_username());
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        String authenticated = Constants.NO.toString();
        
        User user;
		try {
			user = policy.getUserByName(loginUser.getJ_username());
			if(user!=null && user.getPassword().equals(loginUser.getJ_password())){
	        	authenticated = Constants.YES.toString();
	        }
	        loginUser.setAuthenticated(authenticated);
	    } catch (DataException e) {
			//do not do anything
		}
        log.debug("authenticated: " + authenticated);
        return loginUser;
	}
}
