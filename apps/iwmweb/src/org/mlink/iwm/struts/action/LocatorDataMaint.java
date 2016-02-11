package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 21, 2006
 * Time: 11:03:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocatorDataMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(LocatorDataMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);

        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal();
            Long id = StringUtils.getLong(form.getId());
            Locator vo = psf.get(Locator.class, id);
            PageContext context = new PageContext(vo.getName());
            context.add("locator",vo.getFullLocator());
            form.setPageContext(context);
        }catch(Exception e){
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
