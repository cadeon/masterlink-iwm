package org.mlink.iwm.struts.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.http.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.Enumeration;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * User: andrei
 * Date: Dec 14, 2006
 */
public class Login extends Action {
    private static final Logger logger = Logger.getLogger(Login.class);
    //public static String isUserValidated  ="isUserValidated";
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm aform,
                                 HttpServletRequest request,
                                 HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        request.setAttribute("CURRENT_FORM",form);

        String forward=form.getForward()==null?"home":form.getForward();
        logger.debug("execute " + forward);

        try {
            if("error".equals(forward)){
                form.setMessage("Invalid login. Try again");
            }
            else if("extend".equals(forward)){    //used to extend user session (an ajax request sent by user, returns nothing
                logger.debug("session touched by user " + request.getUserPrincipal());
                return null;
            }else if("access_denied".equals(forward)){
                form.setMessage("You are not authorized to view the requested page");
                //this will result in login prompt unless autologin will process the autologin cookie
            }else if("login".equals(forward)){
                if(autologin(request,response)) return null;    //if autologin then j_security_check will itself forward to the requested url
            }else if("auth".equals(forward)){
                auth(request,response);
                return null;
            }else if("logout".equals(forward)){
                invaidate(request,response);
            }
        } catch (Exception e) {
            throw new WebException(e);
        }

        return mapping.findForward(forward);
    }


    /**
     * Checks if user autologin cookie is available and login the user through j_security_check
     * @param request
     * @param response
     * @return
     * @throws WebException
     */
    private boolean autologin(HttpServletRequest request, HttpServletResponse response) throws WebException{
        Cookie userCookie = CookieUtil.getCookie(request, "username");
        Cookie passCookie = CookieUtil.getCookie(request, "password");
        //if(request.getSession().getAttribute(isUserValidated)==null &&
        //        (request.getRequestURL().indexOf("Logout.do") == -1)){  //when user logs out the control ends up in Home.do

        if(userCookie==null || passCookie==null) return false;

        //String username = "super";
        //String password = "man";
        try {
            logger.info("User cookie is found. Proceed with autologin ");
            String username = URLDecoder.decode(userCookie.getValue(), "UTF-8");
            String password = URLDecoder.decode(passCookie.getValue(), "UTF-8");
            String route = "j_security_check?j_username=" + username + "&j_password=" + password;
            response.sendRedirect(response.encodeRedirectURL(route));
            //request.getSession().setAttribute(isUserValidated,true);
        } catch (IOException e) {
            throw new WebException(e);
        }
        return true;

        //}
        //return true;
    }

    /**
     * will forward user credentials from login page to j_security_check
     * @param request
     * @param response
     */
    private void auth(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");
        String route = "j_security_check?j_username=" + username + "&j_password=" + password;
        try {
            if("true".equals(request.getParameter("rememberMe"))){
                CookieUtil.setCookie(response, "username",username,false);
                CookieUtil.setCookie(response, "password",password,false);
            }
            response.sendRedirect(response.encodeRedirectURL(route));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //request.getSession().setAttribute(isUserValidated,true);
    }


    private void invaidate(HttpServletRequest request, HttpServletResponse response){
        Enumeration en = request.getSession().getAttributeNames();
        StringBuilder sb = new StringBuilder("Invalidating session. Attributes in session:");
        while (en.hasMoreElements()) {
            Object o =  en.nextElement();
            sb.append("\n").append(o);
        }
        Cookie userCookie = CookieUtil.getCookie(request, "username");
        Cookie passCookie = CookieUtil.getCookie(request, "password");
        CookieUtil.deleteCookie(response,userCookie);
        CookieUtil.deleteCookie(response,passCookie);
        logger.debug(sb.toString());
        request.getSession().invalidate();
    }
}

