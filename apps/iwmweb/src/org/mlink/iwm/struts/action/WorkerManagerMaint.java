package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class WorkerManagerMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkerManagerMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        WorkerManagerMaint.logger.debug("execute " + forward);
        PageContext context = new PageContext();
        form.setPageContext(context);
        context.add("workerInfo", "John Doe");
        context.add("organization", "Org.Goes.Here");        
        return findForward(mapping,request,forward);
    }
}
