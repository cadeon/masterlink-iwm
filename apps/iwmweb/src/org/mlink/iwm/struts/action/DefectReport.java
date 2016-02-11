package org.mlink.iwm.struts.action;

import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.DefectReportForm;
import org.mlink.iwm.notification.DefectReportSubmitted;
import org.mlink.iwm.util.Constants;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Andrei
 * Date: Apr 16, 2006
 * Time: 8:20:49 PM
 */
public class DefectReport extends BaseAction {
    private static final Logger logger = Logger.getLogger(DefectReport.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        DefectReportForm form  = (DefectReportForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        
        if("submit".equals(forward)){
            form.setException((String)request.getSession().getAttribute(Constants.USER_EXCEPTION));
            form.setExceptionRef((String)request.getSession().getAttribute(Constants.USER_EXCEPTION_REF));           
            new DefectReportSubmitted(form).execute();
            form.setMessage("Submitted");
        }else{
            //do nothing just forward
        }

        return findForward(mapping,request,forward);

    }


}

