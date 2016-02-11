package org.mlink.iwm.upload;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.action.BaseAction;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.FileAccess;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import javax.activation.FileTypeMap;
import java.net.URLEncoder;

/**
 * User: andreipovodyrev
 * Date: Jun 6, 2007
 */
public class UploadsAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(UploadAction.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        if("view".equals(forward)){
            try{
                FileAccess fa = new FileAccess(Config.getFileStoreLocation());
                ServletOutputStream os = response.getOutputStream();
                FileTypeMap fMap=FileTypeMap.getDefaultFileTypeMap();
                String contentType=fMap.getContentType(fa.getFile(form.getName()));
                if(contentType!=null)
                    response.setContentType(contentType);
                else
                    response.setContentType("text/plain");

                response.setHeader("content-disposition","attachment; filename=" + form.getName().replaceAll(" ","_"));
                response.setHeader("content-type",contentType);

                os.write(fa.getBytes(form.getName()));
                os.flush();
                return null;
            }catch(Exception e){
                throw new WebException(e);
            }
        }

        PageContext context = new PageContext();
        context.add("pageInfo","This page provides historical information about  " +
                "data uploaded via application interace. Data includes sailor information and their qualifications and " +
                "etc. Uploaded context can be viewed. If uploaded data has resulted in persistent storing, log is also available.");
        form.setPageContext(context);
        return findForward(mapping,request,forward);
    }

}
