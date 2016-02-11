package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 12, 2007
 */
public class ProjectItems extends BaseAction {
    private static final Logger logger = Logger.getLogger(ProjectItems.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;

        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            /*ControlSF csf = ServiceLocator.getControlSFLocal( );
            Long projectId = StringUtils.getLong(form.getProjectId());
            Project vo = csf.getProject(projectId);
            PageContext context = new PageContext(vo.getName());
            context.add("project",vo.getName());
            context.add("projectType", ProjectTypeRef.getLabel(vo.getProjectTypeId()));
            context.add("status", ProjectStatusRef.getLabel(vo.getStatusId()));
            form.setPageContext(context);*/

            PageContext context = new PageContext();
            context.add("pageInfo","This page is for dev purpose.");
            form.setPageContext(context);

        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
