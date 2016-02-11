package org.mlink.iwm.struts.form;

import javax.servlet.ServletRequest;

import org.apache.struts.action.ActionMapping;

public class AgentToolsForm extends BaseForm {

	protected boolean agentsEnabled = false;
	protected long agentInterval=0;
	protected int numActiveTasks;
	protected int numCreatedJobs;
	protected String output;
	
	public boolean getAgentsEnabled(){return agentsEnabled;}
	public long    getAgentInterval(){return agentInterval;}
	public int    getNumActiveTasks(){return numActiveTasks;}
	public int    getNumCreatedJobs(){return numCreatedJobs;}
	public String getOutput(){return output;}

	public void setAgentsEnabled(boolean b){agentsEnabled=b;}
	public void setAgentInterval(long i){agentInterval=i;}
	public void setNumActiveTasks(int i){numActiveTasks=i;}
	public void setNumCreatedJobs(int i){numCreatedJobs=i;}
	public void setOutput(String s){output=s;}

	public void reset(ActionMapping actionMapping, ServletRequest servletRequest) {
		super.reset(actionMapping, servletRequest);
        agentsEnabled = false;
        agentInterval=0;
        output = null;
    }
}
