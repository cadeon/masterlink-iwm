/*-------------------------------------------------------------------
File: BaseAction.java
---------------------------------------------------------------------------------------*/
package org.mlink.iwm.struts.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.notification.MailUtils;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.ForwardUtils;
import org.mlink.iwm.util.UserTrackHelper;


public class  BaseAction extends Action {
    private static final Logger logger = Logger.getLogger(BaseAction.class);
    public static final String USER_PASSED_VALIDATION = "isValidated";

    protected static final String FORWARD_CREATE    = "create";
    protected static final String FORWARD_CANCEL    = "cancel";
    protected static final String FORWARD_READ      = "read";
    protected static final String FORWARD_REFRESH   = "refresh";


    /**
     * Check if current action is password as user may be trying to update password
     * need to bypass valid password check
     * @return boolean
     */
    private boolean isFromPasswordUpdate(){
       return getClass().getName().endsWith("Password");
    }

    /**
     * Anywhere in the app message may be set to session
     * propagate this message to form and it should show up as javascript alert
     * @param actionform
     * @param request
     */
    private void checkForMessage(ActionForm actionform, HttpServletRequest request){
        String msg =(String)request.getSession().getAttribute("message");
         if(msg!=null){
            ((BaseForm)actionform).setMessage(msg);
             request.getSession().removeAttribute("message"); //message processed, remove from session
         }
    }

    /**
     *
     * @param actionmapping
     * @param actionform
     * @param httpservletrequest
     * @param httpservletresponse
     * @return
     * @throws Exception
     */
    public ActionForward  execute(ActionMapping actionmapping,
                                 ActionForm actionform,
                                 HttpServletRequest httpservletrequest,
                                 HttpServletResponse httpservletresponse) throws Exception{

        String schema =(String)httpservletrequest.getSession().getAttribute(Constants.ACTIVE_DB_SCHEMA);
        UserTrackHelper.setSelectedSchema(schema);
        if(httpservletrequest.getUserPrincipal()!=null)
            UserTrackHelper.setUser(httpservletrequest.getUserPrincipal().getName());

        ActionForward forward = null;
        BaseForm form = (BaseForm)actionform;
        httpservletrequest.setAttribute("CURRENT_FORM",form);
        boolean isFailure = false;


        checkForMessage(actionform, httpservletrequest);
        try  {
            if(!validateUser(httpservletrequest)){
                return actionmapping.findForward("passwordUpdate");
            }

            if("dispatch".equals(actionmapping.getParameter()))
                forward = findDispatch(actionmapping, actionform, httpservletrequest, httpservletresponse);
            else{
                forward = executeLogic(actionmapping, actionform, httpservletrequest, httpservletresponse);
            }
        }
        catch(Exception exception)        {
            setException(httpservletrequest, exception);
            catchException(actionmapping, actionform, httpservletrequest, httpservletresponse);
            //forward=findForward(actionmapping, httpservletrequest,"failure");
            isFailure = true;
        }
        finally        {
            postProcess(actionmapping, actionform, httpservletrequest, httpservletresponse, forward);
        }
        if(isFailure)
            return findFailure(actionmapping, actionform, httpservletrequest, httpservletresponse);

        return forward;
    }


    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
	private boolean validateUser(HttpServletRequest request) throws Exception {
		if ("true".equals(get(request, USER_PASSED_VALIDATION)))
			return true;
		if (request.getUserPrincipal() == null)
			return true; // wait for authentication complete

		String username = request.getUserPrincipal().getName();
		logger.debug("validate user " + username);

		if (ServiceLocator.getPolicySFLocal( ).isPasswordResetRequired(username))
			return false; // new users will have password=username and are
		// required to change password
		else {

			// setting logged in personId and orgId in session
			Long personId, organizationId;
			if (request.getUserPrincipal() != null) {
				String userName = request.getUserPrincipal().getName();

				if (!"super".equals(userName)) {
					PolicySF psf = ServiceLocator.getPolicySFLocal( );
					Person person = null;
					User user = null;
					try {
						user = psf.getUserByName(userName);
						//person = psf.get(Person.class, userName);
						person = user.getPerson();
					} catch (DataException e) {
						// e.printStackTrace();
						throw new WebException(e);
					}

					if (person != null) {
						personId = person.getId();
						String personName = person.getParty().getName();
						organizationId = person.getOrganizationId();
						
						put(request, "personId", personId);
						put(request, "personName", personName);
						put(request, "organizationId", organizationId);
					}
				}
			}

			put(request, USER_PASSED_VALIDATION, "true");
			return true;
		}
	}
    /**
     *
     * @param httpservletrequest
     * @param exception
     */
    protected void setException(HttpServletRequest httpservletrequest, Exception exception)    {
        httpservletrequest.setAttribute(Globals.EXCEPTION_KEY, exception);
    }



    /**
     *
     * @param httpservletrequest
     * @return Exception
     */
    protected Exception getException(HttpServletRequest httpservletrequest)    {
        return (Exception)httpservletrequest.getAttribute(Globals.EXCEPTION_KEY);
    }

    /**
     *
     * @param httpservletrequest
     * @param flag
     * @return action errors
     */
    protected ActionErrors getMessages(HttpServletRequest httpservletrequest, boolean flag)    {
        ActionErrors actionerrors = (ActionErrors)httpservletrequest.getAttribute(Globals.MESSAGE_KEY);
        if(null == actionerrors && flag)        {
            actionerrors = new ActionErrors();
            httpservletrequest.setAttribute(Globals.MESSAGE_KEY, actionerrors);
        }
        return actionerrors;
    }



    protected void addError(HttpServletRequest request, ActionMessage error){
        ActionErrors actionerrors = getErrors(request,true);
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, error);
    }


    protected ActionErrors getErrors(HttpServletRequest httpservletrequest, boolean flag)    {
        ActionErrors actionerrors = (ActionErrors)httpservletrequest.getAttribute(Globals.ERROR_KEY);
        if(null == actionerrors && flag)        {
            actionerrors = new ActionErrors();
            httpservletrequest.setAttribute(Globals.ERROR_KEY, actionerrors);
        }
        return actionerrors;
    }


    protected void addMessage(HttpServletRequest request, ActionMessage error){
        ActionErrors actionerrors = getMessages(request,true);
        actionerrors.add(Globals.MESSAGE_KEY, error);
    }

    protected boolean isErrors(HttpServletRequest httpservletrequest)    {
        return null != getErrors(httpservletrequest, false);
    }

    /**
     *
     * @param s
     * @return string array
     */
    public String[] tokenize(String s)   {
        StringTokenizer stringtokenizer = new StringTokenizer(s, ";");
        int i = 0;
        String as[] = new String[stringtokenizer.countTokens()];
        while(stringtokenizer.hasMoreTokens())        {
            String s1 = stringtokenizer.nextToken().trim();
            if(s1 != null && s1.length() != 0)
                as[i++] = s1;
        }
        return as;
    }

    protected ActionForward findFailure(ActionMapping actionmapping,
                                        ActionForm actionform,
                                        HttpServletRequest httpservletrequest,
                                        HttpServletResponse httpservletresponse)    {
        //if(actionmapping.getInput() != null && getException(httpservletrequest) == null)
        //    return new ActionForward(actionmapping.getInput());
       // else        always dirct to failure, andrei 2/23/05
            return actionmapping.findForward("failure");
    }

    /**
     *
     * @param actionmapping
     * @param actionform
     * @param httpservletrequest
     * @param httpservletresponse
     * @return
     * @throws Exception
     */
    public ActionForward executeLogic(ActionMapping actionmapping,
                                      ActionForm actionform,
                                      HttpServletRequest httpservletrequest,
                                      HttpServletResponse httpservletresponse) throws Exception {
        return findForward(actionmapping, httpservletrequest);
    }

    protected void catchException(ActionMapping actionmapping,
                                  ActionForm actionform,
                                  HttpServletRequest httpservletrequest,
                                  HttpServletResponse httpservletresponse)    {
        Exception exception = getException(httpservletrequest);
        super.servlet.log("*** ACTION EXCEPTION: ", exception);
        ActionErrors actionerrors = getErrors(httpservletrequest, true);
        //actionerrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.message",exception.toString()));
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.message",exception.toString()));


        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw); //to browser
        exception.printStackTrace();  // to standard output

        /**if(exception instanceof  WebException && exception.getCause() !=null){
            sw.write("<br>ROUTE CAUSE:<br>");
            Throwable throwable = exception.getCause();
            throwable.printStackTrace(pw);
        }*/
        //actionerrors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.detail", sw.toString()));
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.path",sw.toString()));


        //actionerrors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.separator", "----------------------------"));
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.separator", "----------------------------"));
        //actionerrors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.path", httpservletrequest.getRequestURI()));
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.path",httpservletrequest.getRequestURI()));

        StringBuffer sb = new StringBuffer();
        Map params = httpservletrequest.getParameterMap();
        for (Object o : params.keySet()) {
            String key =(String) o;
            String [] values =(String[]) params.get(key);
            String value = "";
            if (values != null) {
                for(String value1 : values) value = value + " " + value1;
                sb.append(key).append(" = ").append(value).append("<br>");
            }
        }
        //actionerrors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.params", sb.toString()));
        actionerrors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.params",sb.toString()));
        String excRef= String.valueOf(Math.round(Math.random()*100000));
        MailUtils.informSupport("Exception Ref "+excRef,exception);
        // keep in session in case of user submits Defect Report
        httpservletrequest.getSession().setAttribute(Constants.USER_EXCEPTION,sw.toString());
        httpservletrequest.getSession().setAttribute(Constants.USER_EXCEPTION_REF,excRef);
    }

    protected void postProcess(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest httpservletrequest,
                               HttpServletResponse httpservletresponse,
                               ActionForward forward)    {
    }
    /**
     *
     * @param actionmapping
     * @param actionform
     * @param httpservletrequest
     * @param httpservletresponse
     * @return ActionForward
     */
    protected ActionForward findSuccess(ActionMapping actionmapping,
                                        ActionForm actionform,
                                        HttpServletRequest httpservletrequest,
                                        HttpServletResponse httpservletresponse)    {
        return actionmapping.findForward("success");
    }

    /**
     *
     * @param actionmapping
     * @param actionform
     * @param httpservletrequest
     * @param httpservletresponse
     * @return ActionForward
     */

    protected ActionForward findDispatch(ActionMapping actionmapping,
                                         ActionForm actionform,
                                         HttpServletRequest httpservletrequest,
                                         HttpServletResponse httpservletresponse)    {
        String dispValue = httpservletrequest.getParameter("dispatch");
        ActionForward forward = actionmapping.findForward(dispValue);
        if(forward.getRedirect()){   //  then redirect = true therefore all form params are lost
            // if forward.path contains dynamic params (id=) fill them in from request
            String path = forward.getPath();
            String filledPath = ForwardUtils.fillInQueryString(path,httpservletrequest);
            forward = new ActionForward(filledPath,forward.getRedirect());
        }

        return forward;
    }



    protected ActionForward findForward(ActionMapping mapping, HttpServletRequest request){
        String forward = request.getParameter("forward");
        return _findForward(mapping, request, forward);
    }


    protected ActionForward findForward(ActionMapping mapping, HttpServletRequest request, String forward){
        return _findForward(mapping, request, forward);
    }
    private ActionForward _findForward(ActionMapping mapping, HttpServletRequest request, String forward){
        if(forward == null){
            forward = "read";  //defaultForward
        }
        ActionForward af = mapping.findForward(forward);
        if(af.getRedirect()){
            String path = af.getPath();
            String filledPath = ForwardUtils.fillInQueryString(path,request);
            af = new ActionForward(filledPath,af.getRedirect());
        }
        return af;
    }



    protected String getMessage(String key){
        MessageResources resources = (MessageResources)getServlet().getServletContext().getAttribute("iwm.messages");
        return resources.getMessage(key);
    }

    protected void put(HttpServletRequest request, String key, Object value){
        request.getSession().setAttribute(key,value);
    }


    protected Object get(HttpServletRequest request,String key){
       return request.getSession().getAttribute(key);
    }
    protected Object remove(HttpServletRequest request,String key){
       return request.getSession().getAttribute(key);
    }
}

// EOF:  BaseAction.java
