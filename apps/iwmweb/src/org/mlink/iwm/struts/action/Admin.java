package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: andrei
 * Date: Nov 21, 2006
 */
public class Admin extends BaseAction{
    private static final Logger logger = Logger.getLogger(Admin.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        PageContext context = new PageContext();
        form.setPageContext(context);
        request.setAttribute("buildprops", Config.getBuildProperties());
        request.setAttribute("systemprops", System.getProperties());        
        return findForward(mapping,request,forward);
    }
}
