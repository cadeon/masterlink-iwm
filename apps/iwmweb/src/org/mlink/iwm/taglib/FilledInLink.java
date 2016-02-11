package org.mlink.iwm.taglib;

import org.apache.strutsel.taglib.utils.EvalHelper;
import org.apache.strutsel.taglib.html.ELLinkTag;
import org.mlink.iwm.util.ForwardUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

/**
 * User: andrei
 * This tag extends ELLink but limits ELLinkTag to 'page' attribute only.
 * Other attributes can be supported if needed
 * Date: Oct 12, 2006
 */
public class FilledInLink extends ELLinkTag {

    /**
     * Process the start tag.
     *
     * @exception javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        evaluateExpressions();
        return (super.doStartTag());
    }

    /**
     * If page  containes HttpRequest parameters (ex: ?locatorId=), ForwardUtils.fillInQueryString will populate
     * values for the parameters if the parameters are available in HttpRequest. That is
     * Task.do?objectId= will transaform to Task.do?objectId=<%=request.getParameter('objectId')%>
     *
     * @exception JspException if a JSP exception has occurred
     */
    private void evaluateExpressions() throws JspException {
        String  string ;
        if ((string = EvalHelper.evalString("page", getPageExpr(), this, pageContext)) != null){
            HttpServletRequest request = ((HttpServletRequest)pageContext.getRequest());
            String urlWithFilledparams = ForwardUtils.fillInQueryString(string,request);
            setPageExpr(urlWithFilledparams);
        }
    }
}

