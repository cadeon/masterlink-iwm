package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.dwr.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class WorkerMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkerMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        PageContext context = new PageContext();
        form.setPageContext(context);

        if(form.getId()!=null){  //organizationId is supplied
            request.getSession().setAttribute(SessionUtil.CURRENT_ORG_ID,Long.valueOf(form.getId()));   // set the filter to the given organization
        }
        
        request.setAttribute("DISPLAY_ADDEDIT_SKILLS", Config.getProperty(Config.DISPLAY_ADDEDIT_SKILLS));
        request.setAttribute("DISPLAY_ADDEDIT_SHIFTREFS", Config.getProperty(Config.DISPLAY_ADDEDIT_SHIFTREFS));
        request.setAttribute("DISPLAY_WORKERS_LOCATION", Config.getProperty(Config.DISPLAY_WORKERS_LOCATION));
        
        return findForward(mapping,request,forward);
    }
}
