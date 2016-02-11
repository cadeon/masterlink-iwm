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
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class ObjectMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(ObjectMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        
        Long objectId = StringUtils.getLong(form.getId());
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        PageContext context=null;
        if(objectId!=null){
        	ObjectEntity vo = isf.get(ObjectEntity.class, objectId);
        	context = new PageContext(vo.getObjectRef());
        	context.add("objectRef",vo.getObjectRef());
        	context.add("id",objectId);
        	request.setAttribute("locatorId", vo.getLocatorId());
        	request.setAttribute("classId", vo.getClassId());
        	request.setAttribute("objectRef",vo.getObjectRef());
        }else{
        	context = new PageContext();
        }
        form.setPageContext(context);
        return findForward(mapping,request,forward);
    }
}
