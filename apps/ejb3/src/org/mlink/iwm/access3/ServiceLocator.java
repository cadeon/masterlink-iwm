package org.mlink.iwm.access3;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.mlink.iwm.session.AgentSF;
import org.mlink.iwm.session.AgentSFLocal;
import org.mlink.iwm.session.AgentSFRemote;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ControlSFLocal;
import org.mlink.iwm.session.ControlSFRemote;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.ImplementationSFLocal;
import org.mlink.iwm.session.ImplementationSFRemote;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.session.PolicySFLocal;
import org.mlink.iwm.session.PolicySFRemote;
import org.mlink.iwm.util.EnvUtils;

/**
 * User: andreipovodyrev
 * Date: Mar 21, 2009
 */
public class ServiceLocator {
    private static final Logger logger = Logger.getLogger(ServiceLocator.class);

    private static Object lookup(String jndiName) {
        Object object=null;
        try {
            Context context = EnvUtils.getInitialContext();
            object = context.lookup(jndiName);
        } catch (NamingException e) {
            e.printStackTrace();
            logger.error("Error trying to create jndiName:" + e.getMessage());
        }
        return object;
    }

    public static PolicySF getPolicySFRemote(){
        return (PolicySFRemote)lookup("PolicySFRemote");
    }

    public static PolicySF getPolicySFLocal(){
        return (PolicySFLocal)lookup("PolicySFLocal");
    }
    
    public static ImplementationSF getImplementationSFRemote(){
        return (ImplementationSFRemote)lookup("ImplementationSFRemote");
    }

    public static ImplementationSF getImplementationSFLocal(){
        return (ImplementationSFLocal)lookup("ImplementationSFLocal");
    }
    
    public static AgentSF getAgentSFRemote(){
        return (AgentSFRemote)lookup("AgentSFRemote");
    }

    public static AgentSF getAgentSFLocal(){
        return (AgentSFLocal)lookup("AgentSFLocal");
    }
    
    public static ControlSF getControlSFRemote(){
        return (ControlSFRemote)lookup("ControlSFRemote");
    }

    public static ControlSF getControlSFLocal(){
        return (ControlSFLocal)lookup("ControlSFLocal");
    }
}
