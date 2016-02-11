package org.mlink.iwm.upload;

import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.log4j.Logger;
import org.mlink.iwm.struts.action.BaseAction;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.util.FileAccess;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.iwml.ParserFactory;
import org.mlink.iwm.iwml.Parser;
import org.mlink.iwm.dao.AttachmentDAO;
import org.mlink.iwm.notification.Attachment;


/**
 * Refictored from UploadAction from struts example application
 */


public class UploadAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(UploadAction.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws WebException {


        //this line is here for when the input page is upload-utf8.jsp,
        //it sets the correct character encoding for the response
        String encoding = request.getCharacterEncoding();
        if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
            response.setContentType("text/html; charset=utf-8");
        }

        UploadForm theForm = (UploadForm) aform;
        logger.debug("execute");
        PageContext context = new PageContext();
        context.add("pageInfo","Shows information about uploaded file");
        theForm.setPageContext(context);
        //retrieve the text data
        String text = theForm.getTheText();

        //retrieve the query string value
        //String queryValue = theForm.getQueryParam();

        //retrieve the file representation
        FormFile file = theForm.getTheFile();

        //retrieve the file name
        String fileName = file.getFileName();

        //retrieve the content type
        String contentType = file.getContentType();


        //retrieve the file size
        String size = (file.getFileSize() + " bytes");

        String data = null;
        String newfname=(text==null || text.length()==0)?file.getFileName():text;

        String forward = theForm.getForward()==null?"integration":theForm.getForward();

        try {
            //retrieve the file data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream stream = file.getInputStream();
            //only write files out that are less than 1MB
            if (file.getFileSize() < (4 * 1024000)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                data = new String(baos.toByteArray());

                if("integration".equals(forward)){
                    processIntegrationUpload(request,newfname,data);
                }else if("attachment".equals(forward)){
                    processAttachment(new Attachment(baos.toByteArray(),contentType,newfname,theForm.getFileDescription()));
                }else{
                    //processIntegrationUpload(request,newfname,data);
                    saveFile(newfname,baos.toByteArray());
                }


            }
            //close the stream
            stream.close();


            //place the data into the request for retrieval from display.jsp
            request.setAttribute("text", newfname);
            //request.setAttribute("queryValue", "Successful");
            request.setAttribute("fileName", fileName);
            request.setAttribute("contentType", contentType);
            request.setAttribute("size", size);
            request.setAttribute("data", data);

            //destroy the temporary file created
            file.destroy();

        }catch (IOException e) {
            addError(request,new ActionMessage("exception",e.getMessage()));
            return mapping.findForward("input");
        }catch (Exception e) {
            throw new WebException(e);
        }

        //return a forward to display.jsp
        return mapping.findForward(forward);

    }

    private void processAttachment(Attachment at) throws Exception{
        new AttachmentDAO().addAttachment(at);
    }

    private void processIntegrationUpload(HttpServletRequest request, String fileName, String data) throws Exception{
        //see if user wanted have a new name instead of original name of file being uploaded
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        fa.saveFile(fileName,data);
        if(fa.getFile(fileName+".log").exists()) fa.deleteFile(fileName+".log");

        /*Parser dg = ParserFactory.getParser(data);
        dg.process(data);

        FileAccess fLog = new FileAccess(Config.getFileStoreLocation());
        fLog.saveFile(fileName+".log",dg.getLog());

        if(dg.getErrors().size()>0){
            //throw new Exception("File upload resulted in errors. See the upload log for details.");
            addError(request,new ActionMessage("exception","File upload errors: " + dg.getErrors().get(0)));
            addError(request,new ActionMessage("exception","See the upload log for details"));
        }*/
    }


    private void saveFile(String fileName, byte [] data) throws Exception{
        //see if user wanted have a new name instead of original name of file being uploaded
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        fa.saveFile(fileName,data);
        if(fa.getFile(fileName+".log").exists()) fa.deleteFile(fileName+".log");
    }

}