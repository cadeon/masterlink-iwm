package org.mlink.iwm.session;

import org.apache.log4j.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * User: andreipovodyrev
 * Date: Mar 21, 2009
 * This class will be called before and after method execution of a session bean if used as interceptor
 */
public class SessionInterceptor {
    private static final Logger logger = Logger.getLogger(SessionInterceptor.class);
    @AroundInvoke public Object aroundInvoke(InvocationContext invocation) throws Exception{
        String className= invocation.getTarget().getClass().getName();
        String methodName = invocation.getMethod().getName();
        StringBuilder info = new StringBuilder(className.substring(className.lastIndexOf(".")+1) + "." + methodName);
        info.append("(");
        Object [] params = invocation.getParameters();
        if(params!=null){
            for(Object param:params){
                //info.append(param.getClass().getName().substring(param.getClass().getName().lastIndexOf(".")+1));
            	if(param!=null){
            		info.append(param.toString());
            	}else{
            		info.append("null");
            	}
            	info.append(" ");
            }
        }
        info.append(")");
        try {
            logger.info(info);
            return invocation.proceed();
        } catch (Exception e) {
            throw e;  //rethrow unless we want to handle errors here
        } finally {
            //logger.info("Exiting " + info);   uncomment if need after execution logging
        }
    }
}
