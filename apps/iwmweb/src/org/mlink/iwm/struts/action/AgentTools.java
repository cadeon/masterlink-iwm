package org.mlink.iwm.struts.action;

import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.struts.form.AgentToolsForm;
import org.mlink.iwm.struts.form.PageContext;
//import org.mlink.iwm.timer.AgentTimer;
import org.mlink.iwm.timer.IWMTimer;
import org.mlink.iwm.timer.RunAgents;
import org.mlink.iwm.timer.IWMTask;
import org.mlink.iwm.util.Config;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AgentTools extends BaseAction {

	private static final Logger logger = Logger.getLogger(AgentTools.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		// no form for this servlet -- AJAX receiver

        String forward = request.getParameter("forward");
		if (forward == null)
			forward = "read";
		AgentToolsForm form;
		if (aform != null)
			form = (AgentToolsForm) aform;
		else
			form = new AgentToolsForm();


        PageContext context = new PageContext();
        context.add("pageInfo","This page is for dev purpose.");
        form.setPageContext(context);

        boolean javaAgents = false;
        if("true".equals(Config.getProperty(Config.JAVA_AGENTS_ENABLED,"false"))){
        	javaAgents = true;
        }
        form.setAgentsEnabled(javaAgents);

		ActionForward af = findForward(mapping, request);
		
		if (javaAgents) {
			try {
	
				logger.debug("execute " + forward);
				if ("read".equals(forward)) {
				} else if ("jsm".equals(forward)) {
					runJSM(form);
				} else if ("planner".equals(forward)) {
					runPlanner(form);
				} else if ("printStateMachine".equals(forward)) {
					printStateMachine(form);
				} else if ("reloadStateMachine".equals(forward)) {
					reloadStateMachine(form);
				} else if ("scheduler".equals(forward)) {
					runScheduler(form);
				} else if ("shiftManager".equals(forward)) {
					runShiftManager(form);
				}  else if ("setAgentInterval".equals(forward)) {
					setAgentInterval(form);
                }  else if ("runAllButPlanner".equals(forward)) {
                    BaseAccess.getWorldConnection().runAllButPlanner();
                }  else if ("runAll".equals(forward)) {
                    BaseAccess.getWorldConnection().runAll();
				}
			} catch (BusinessException be) {
				form.setMessage(be.getMessage());
			}catch(Exception ex){
	            throw new WebException(ex);
	        }
		} else {
			logger.debug("Java agents are current turned off. Check iwm.properties file for key java.agent.enabled");
		}

        //form.setAgentInterval(AgentTimer.getInterval());
        IWMTask agents = IWMTimer.getInstance().getTask(RunAgents.class);
        if(agents!=null){
            form.setAgentInterval(agents.getInterval()/1000L/60);
        }else{
            form.setAgentInterval(-1);
        }

        return af;
	}
	
	private void printStateMachine(AgentToolsForm form) throws  Exception {
		try {
			String s = BaseAccess.getWorldConnection().printStateMachine();
			logger.debug("State Machine :");
			logger.debug(s);
			form.setOutput(s);	
		} catch (Exception e) {
			logger.error("Error printing Job State Manager state machine.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}		
	}

	private void reloadStateMachine(AgentToolsForm form) throws  Exception {
		try {
			BaseAccess.getWorldConnection().reloadStateMachine();	
			form.setOutput("State machine reloaded.");
		} catch (Exception e) {
			logger.error("Error reloading Job State Manager state machine.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}		
	}
	private void runJSM(AgentToolsForm form) throws Exception {
		try {
			Thread tr = new Thread() {
				public void run() {
					BaseAccess.getWorldConnection().runJSM();
				}
			};
			tr.start();
		} catch (Exception e) {
			logger.error("Error during Job State Manager run.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}
	}

	private void runPlanner(AgentToolsForm form) throws  Exception {
		try {
			Thread tr = new Thread() { 
				public void run(){
					BaseAccess.getWorldConnection().runPlanner();
				} 
			};
			tr.start();
		} catch (Exception e) {
			logger.error("Error during Planner run.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}
	}
	private void runScheduler(AgentToolsForm form) throws  Exception {
		try {
			Thread tr = new Thread() {
				public void run() {
					BaseAccess.getWorldConnection().runScheduler();	
				}
			};
			tr.start();	
		} catch (Exception e) {
			logger.error("Error during Scheduler run.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}
	} 
	private void runShiftManager(AgentToolsForm form) throws  Exception {
		try {
			Thread tr = new Thread() {
				public void run() {
					BaseAccess.getWorldConnection().runShiftManager();	
				}
			};
			tr.start();	
		} catch (Exception e) {
			logger.error("Error during Shift Manager run.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}
	}
	private void setAgentInterval(AgentToolsForm form) throws  Exception {
		try {
			long interval = form.getAgentInterval();
            IWMTimer.getInstance().schedule(new RunAgents(interval*1000L*60));


            /*if (AgentTimer.isStarted())	{
				AgentTimer.stop();
				AgentTimer.start(interval);
			} else {
				AgentTimer.start(interval);
			}*/
		} catch (Exception e) {
			logger.error("Error turning on/off automated run of agents.", e);
			// Don't throw error here b/c if Java agents not enabled, 
			// getWorldConnection will return null. Just log and continue.
			
			//throw new BusinessException(e.getMessage());
		}
	}


    /*private void runAll() throws  Exception {
		try {
            BaseAccess.getWorldConnection().runAll();
        } catch (Exception e) {
			logger.error("Error turning on/off automated run of agents.", e);
		}
	}*/
	
}
