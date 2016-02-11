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
import org.mlink.iwm.entity3.TaskDefinition;
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
public class TemplateTaskActionsMaint  extends BaseAction {
    private static final Logger logger = Logger.getLogger(TemplateTaskActionsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long taskId = StringUtils.getLong(form.getTaskId());
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            TaskDefinition vo = psf.get(TaskDefinition.class, taskId);
            ObjectDefinition odVo= psf.get(ObjectDefinition.class, vo.getObjectDefinition().getId());
            String classCode = TargetClassRef.getLabel(odVo.getClassId());
            PageContext context = new PageContext(vo.getTaskDescription());
            context.add("class",classCode);
            context.add("taskDescription",vo.getTaskDescription());            
            form.setPageContext(context);
        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
