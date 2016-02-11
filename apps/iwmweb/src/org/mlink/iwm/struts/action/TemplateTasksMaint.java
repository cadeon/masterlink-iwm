package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * User: jmirick
 * Email: john@mirick.us
 * Date: Oct 25, 2006
 */
public class TemplateTasksMaint  extends BaseAction {
    private static final Logger logger = Logger.getLogger(TemplateTasksMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long templateId = StringUtils.getLong(form.getId());
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            ObjectDefinition vo = psf.get(ObjectDefinition.class, templateId);
            String classCode = TargetClassRef.getLabel(vo.getClassId());
            PageContext context = new PageContext(classCode);
            context.add("class",classCode);
            form.setPageContext(context);
        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
