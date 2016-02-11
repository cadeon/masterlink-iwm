package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Sequence;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.OrganizationRef;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Feb 12, 2007
 */
public class ProjectStencilTaskMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(ProjectStencilTaskMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;

        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
            Long projectStencilId = StringUtils.getLong(form.getId());
            Sequence vo = isf.get(Sequence.class, projectStencilId);
            PageContext context = new PageContext(vo.getName());
            context.add("name",vo.getName());
            context.add("active", Constants.YES.equals(vo.getActive())?"Yes":"No");
            if(vo.getLocatorId()!=null)
                context.add("locator", LocatorRef.getLabel(vo.getLocatorId()));
            if(vo.getOrganizationId()!=null)
                context.add("organization", OrganizationRef.getLabel(vo.getOrganizationId()));
            form.setPageContext(context);

        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
