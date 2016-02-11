package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.lookup.ProjectStatusRef;
import org.mlink.iwm.lookup.ProjectTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class ProjectJobsMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(ProjectJobsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;

        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            ControlSF csf = ServiceLocator.getControlSFLocal( );
            Long projectId = StringUtils.getLong(form.getProjectId());
            Project vo = csf.get(Project.class, projectId);
            PageContext context = new PageContext(vo.getName());
            context.add("project",vo.getName());
            context.add("projectType", ProjectTypeRef.getLabel(vo.getProjectTypeId()));
            context.add("status", ProjectStatusRef.getLabel(vo.getStatusId()));
            form.setPageContext(context);

        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
