package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.notification.Attachment;
import org.mlink.iwm.dao.AttachmentDAO;
import org.mlink.iwm.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 26, 2007
 */
public class AttachmentMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(AttachmentMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        if("view".equals(forward)){
            try{
                Attachment at = AttachmentDAO.getAttachment(StringUtils.getLong(form.getId()));
                ServletOutputStream os = response.getOutputStream();
                String contentType = at.getContentType();
                if(contentType!=null)
                    response.setContentType(contentType);
                else
                    response.setContentType("text/plain");

                response.setHeader("Content-Disposition","attachment; filename=" + at.getName().replaceAll(" ","_") );
                os.write(at.getBytes());
                os.flush();
                return null;
            }catch(Exception e){
                throw new WebException(e);
            }
        }
        else{
            PageContext context = new PageContext();
            context.add("pageInfo","All attachments in the system. Dev purpose only page");
            form.setPageContext(context);
        }
        return findForward(mapping,request,forward);
    }

}
