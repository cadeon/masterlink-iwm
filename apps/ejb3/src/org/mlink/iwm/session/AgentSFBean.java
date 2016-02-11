package org.mlink.iwm.session;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.mlink.iwm.exception.BusinessException;

@Interceptors(SessionInterceptor.class)
@Stateless(name = "ActionBean")
@RemoteBinding(jndiBinding = "ActionRemote")
@LocalBinding(jndiBinding = "ActionLocal")
public class AgentSFBean implements AgentSFLocal, AgentSFRemote {

	public String printStateMachine() throws FinderException, RemoteException,
			BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void reloadStateMachine() throws FinderException, RemoteException,
			BusinessException {
		// TODO Auto-generated method stub
		
	}

	public void runJobStateManager() throws FinderException, RemoteException,
			BusinessException {
		// TODO Auto-generated method stub
		
	}

	public void runPlanner() throws FinderException, RemoteException,
			BusinessException {
		// TODO Auto-generated method stub
		
	}

    /*private static final Logger logger = Logger.getLogger(AgentSFBean.class);
    ProductionWorldConnection worldConnection = null;
    

	public void worldConnect() throws EJBException {
    	try {
	    	if (worldConnection==null) {
				worldConnection = ProductionWorldConnection.checkoutProductionWorld();
	    	}
    	} catch (Exception e) {
    		throw new EJBException(e);
    	}
	}
    *//**
     * Print the state machine currently being used by the Job State Manager
     * @return String representing the xml of the state machine
     * @throws BusinessException
     *//*
    public String printStateMachine () throws BusinessException {
    	String s = new String("<none/>");
		try {
	    	worldConnect();
			log("Printing state machine (# states: "+ worldConnection.getStateCount() +")");
			s=worldConnection.printStateMachine();
			log(s);
		} catch (Exception e) {
			logger.error("Error printing state machine.", e);
			throw new BusinessException(e.getMessage());
		}
    	return s;
    }

    *//**
     * Reload the state machine currently being used by the Job State Manager
     * @throws BusinessException
     *//*
    public void reloadStateMachine () throws BusinessException {
		try {
			log("Call state machine reload");
	    	worldConnect();
			worldConnection.reloadStateMachine();
			log("State machine reloaded");
		} catch (Exception e) {
			logger.error("Error reloading state machine.", e);
			throw new BusinessException(e.getMessage());
		}
    }
    
    *//**
     * Run JobStateManager Agent
     * @throws BusinessException
     *//*
    public void runJobStateManager () throws BusinessException {
		try {
			log("Call job state manager");
	    	worldConnect();
			worldConnection.runJSM();
		} catch (Exception e) {
			logger.error("Error during JobStateManager run.", e);
			throw new BusinessException(e.getMessage());
		}
    }
    
    *//**
     * Run Planner Agent
     * @throws BusinessException
     *//*
    public void runPlanner () throws BusinessException {
		try {
			log("Call planner");
			worldConnect();
			worldConnection.runPlanner();
		} catch (Exception e) {
			logger.error("Error during Planner run.", e);
			throw new BusinessException(e.getMessage());
		}
    }
    

    private void log(String s) {
        logger.debug(s);
    }
*/}
