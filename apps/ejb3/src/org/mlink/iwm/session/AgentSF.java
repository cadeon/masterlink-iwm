package org.mlink.iwm.session;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import org.mlink.iwm.exception.BusinessException;

public interface AgentSF{

    /**
     * Print the state machine currently being used by the Job State Manager
     * @return String representing the xml of the state machine
     * @throws FinderException
     * @throws RemoteException
     */
    public String printStateMachine () throws  FinderException, RemoteException, BusinessException;

    /**
     * Reload the state machine currently being used by the Job State Manager
     * @throws FinderException
     * @throws RemoteException
     */
    public void reloadStateMachine () throws  FinderException, RemoteException, BusinessException;

    /**
     * Run JobStateManager Agent
     * @throws FinderException
     * @throws RemoteException
     */
    public void runJobStateManager () throws  FinderException, RemoteException, BusinessException;

    /**
     * Run Planner Agent
     * @throws FinderException
     * @throws RemoteException
     */
    public void runPlanner () throws  FinderException, RemoteException, BusinessException;
   
}
