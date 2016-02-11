package org.mlink.iwm.dwr;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * User: andrei
 * Date: Sep 20, 2006
 */
public class SessionUtil {
    public final static String CURRENT_LOCATOR_ID = "currentLocatorId";
    public final static String CURRENT_CLASS_ID = "currentClassId";
    public final static String CURRENT_ORG_ID = "currentOrganizationId";
    public final static String PAGESIZE = "pagesize";

    public static void setAttribute(String name, Object value){
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest req = ctx.getHttpServletRequest();
        req.getSession().setAttribute(name,value);

    }

    public static Object getAttribute(String name){
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest req = ctx.getHttpServletRequest();
        Object atr =  req.getSession().getAttribute(name);
        //System.out.println("getAttribute " + name + "="+atr);
        return atr;
    }

    public static void removeAttribute(String name){
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest req = ctx.getHttpServletRequest();
        req.getSession().removeAttribute(name);
    }    

    public static void setCurrentLocator(Long locatorId){
        setAttribute(CURRENT_LOCATOR_ID,locatorId);
    }

    public static Long getCurrentLocator(){
        return(Long)getAttribute(CURRENT_LOCATOR_ID);
    }

    public static void setCurrentClass(Long classId){
        setAttribute(CURRENT_CLASS_ID,classId);
    }

    public static Long getCurrentClass(){
        return(Long)getAttribute(CURRENT_CLASS_ID);
    }

    public static void setCurrentOrganization(Long orgId){
        setAttribute(CURRENT_ORG_ID,orgId);
    }

    public static Long getCurrentOrganization(){
        return(Long)getAttribute(CURRENT_ORG_ID);
    }

    public static void setPagesize(Integer size){
        setAttribute(PAGESIZE,size);
    }

    public static Integer getPagesize(){
        return (Integer)getAttribute(PAGESIZE);
    }
}
