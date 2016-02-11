package org.mlink.agent;

import java.util.ArrayList;
import java.util.Collection;

import org.mlink.agent.model.Job;
import org.mlink.agent.util.State;
import org.mlink.agent.util.StateMachine;
import org.mlink.agent.util.StateMachineLoader;

public class JobStateManager extends BaseAgent {
    private final static String PROPERTY_FILE = "jobstatemanager.xml";
	
	private StateMachine stateMachine;
	
	public JobStateManager() throws Exception {
		super("JobStateManager");
		stateMachine = StateMachineLoader.load(PROPERTY_FILE);
	}
    public JobStateManager(String name) throws Exception {
		super(name);
		stateMachine = StateMachineLoader.load(PROPERTY_FILE);
	}
    
	/**
	 * Runs the JSM algorithm:
	 * 1. Examine all jobs
	 * 2. Run each job through the state transition rules
	 * 3. Repeat until the job no longer changes state
	 * 
	 * @param c The collection of jobs to examine
	 * @returns The jobs modified as a result of the run
	 */
    @Override
	public Collection<Job> run(Collection c) {
    	log("Running Job State Manager");
		ArrayList<Job> changed   = new ArrayList<Job>();
		ArrayList<Job> unchanged = new ArrayList<Job>();
		if (c==null || c.isEmpty()) return changed;
		changed.addAll(c);
		int i = 1;
		while (changed.size()>0) {
			log("Transition loop "+i++ +": "+changed.size()+" jobs to check");
			ArrayList<Job> currentIter = new ArrayList<Job>();
			for (Job job : changed) {
				State state = stateMachine.transition(job,job.getStatus());
				if (state==null || state.getName().equals(job.getStatus()) ) {
					unchanged.add(job);
					currentIter.add(job);
				} else {
					job.setStatus(state.getName());
					job.setIsChanged(true);
				}
			}
			log("Loop done. Jobs unchanged this iteration: "+currentIter.size());
			changed.removeAll(currentIter);
		}
		return unchanged;
	}
    
    public int getStateCount(){return stateMachine.getStateCount();} 
    public String printStateMachine(){return stateMachine.toString();}    
    public void reloadStateMachine() throws Exception {stateMachine = StateMachineLoader.load(PROPERTY_FILE);}
}
