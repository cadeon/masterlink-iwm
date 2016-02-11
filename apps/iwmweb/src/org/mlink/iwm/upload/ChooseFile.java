package org.mlink.iwm.upload;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 7, 2007
 */
public class ChooseFile extends BaseAction {
    private static final Logger logger = Logger.getLogger(ChooseFile.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws WebException {
        BaseForm form = (BaseForm) aform;
        logger.debug("execute");
        PageContext context = new PageContext();
        context.add("pageInfo","Navigate to the file you intend to upload and click submit. Should you need you may also change name under which file will be stored by system. ");
        form.setPageContext(context);
        return mapping.findForward("read");
    }
}
