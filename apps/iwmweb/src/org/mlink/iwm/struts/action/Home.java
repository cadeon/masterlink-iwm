package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: andrei
 * Date: Jan 16, 2007
 */
public class Home  extends BaseAction {
    private static final Logger logger = Logger.getLogger(Home.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {

        logger.debug("execute");
        if(request.isUserInRole("ConJob")){
            return findForward(mapping,request,"jobs");
        }else{
            return findForward(mapping,request,"mobileWorker");
        }
    }
}