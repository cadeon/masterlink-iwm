package org.mlink.iwm.struts.action;

import java.io.File;
import java.text.DateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectClassification;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.struts.form.ObjectDetailsForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * User: jmirick
 * Email: john@mirick.us
 * Date: Oct 25, 2006
 */
public class ObjectDetailsMaint  extends BaseAction {
    private static final Logger logger = Logger.getLogger(ObjectDetailsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        ObjectDetailsForm form  = (ObjectDetailsForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long objectId = StringUtils.getLong(form.getId());
            ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
            ObjectEntity vo = isf.get(ObjectEntity.class, objectId);
            //Set title
            PageContext context = new PageContext(vo.getObjectRef());
            form.setPageContext(context);
            //Set Object info
            
            form.setObjectRef(vo.getObjectRef());
            Locator locator = isf.get(Locator.class, vo.getLocatorId());
            form.setFullLocator(locator.getFullLocator());
            form.setLocatorId(vo.getLocatorId().toString());
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM); 
            
            form.setCreatedDate(df.format(vo.getCreatedDate()));
            
            ObjectClassification oc = isf.get(ObjectClassification.class, vo.getClassId());
            form.setClassName("[" + oc.getCode() + "] " + oc.getDescription());
            form.setClassId(vo.getClassId().toString());
            
            if (vo.getActive() == 0){
            	form.setActive("No");
            } else {
            	form.setActive("Yes");
            }
            if (vo.getOrganizationId() == null){
            	form.setOrganization("No Organization");
            } else{
            	Organization o = isf.get(Organization.class, vo.getOrganizationId());
            	form.setOrganization(o.getParty().getName());
            	form.setOrganizationId(vo.getOrganizationId().toString());
            }

            if(forward.equals("printLabel")){
            	String url1 = Config.getProperty(Config.PRINTLABEL_QRCODE_URL);
                request.setAttribute(Config.PRINTLABEL_QRCODE_URL, url1);
            	String url3="/ObjectData.do?id="+objectId;
            	int serverPort=request.getServerPort();
            	String scheme=request.getScheme();
            	String serverName=request.getServerName();
            	String contextPath=request.getContextPath();
            	String url2;

            	if ( ( serverPort == 80 ) || ( serverPort == 443 ) )
            		url2 = scheme + "://" + serverName + contextPath;
            	else
            		url2 = scheme + "://" + serverName + ":" + serverPort + contextPath;

            	String url=url2+url3;
            	Writer  qrCode = new QRCodeWriter(); 
            	try { 
            		ByteMatrix bb = qrCode.encode(url, BarcodeFormat.QR_CODE, 350, 350); 
            		String fileStr=System.getProperty("jboss.server.home.dir")+ "/deploy/"+ Config.getProperty("iwmearname");
            		fileStr+="/iwmweb.war/images/out.png";
            		MatrixToImageWriter.writeToFile(bb, "png", new File(fileStr)); 
            	} catch (Exception e) { 
            		System.out.println(e); 
            	} 
            }
        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
