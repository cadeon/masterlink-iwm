package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.EqualsUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: jmirick
 * Email: john@mirick.us
 * Date: Oct 25, 2006
 */
public class ObjectTaskActionsMaint  extends BaseAction {
    private static final Logger logger = Logger.getLogger(ObjectTaskActionsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);

        try {
            Long taskId = StringUtils.getLong(form.getTaskId());
            ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
            Task vo = isf.get(Task.class, taskId);
            ObjectEntity oVo= isf.get(ObjectEntity.class, vo.getObject().getId());
            String classCode = TargetClassRef.getLabel(oVo.getClassId());
            PageContext context = new PageContext(vo.getTaskDescription());
            context.add("objectRef",oVo.getObjectRef());
            context.add("class",classCode);
            boolean isCustom = EqualsUtils.areEqual(vo.getCustom(), Constants.YES);
            context.add("customTask",isCustom?"Yes":"No");
            context.add("taskDescription",vo.getTaskDescription());
            request.setAttribute("isCustomTask",isCustom);
            form.setPageContext(context);
        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
