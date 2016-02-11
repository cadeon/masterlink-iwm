package org.mlink.agent.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class State {

	private static Logger logger= Logger.getLogger(State.class);
	private String name; 
	private ArrayList<Transition> transitions;
	
	public State() {
		//logger.debug("creating state");
		transitions = new ArrayList<Transition>();
	}

	public List<Transition>     getAllTransitions(){return transitions;}
	public String               getName(){return name;}
	
	public void setAllTransitions(ArrayList<Transition> map){transitions=map;}
	public void setName(String s){name=s;}
	
	public void addTransition(Transition t){
		//logger.debug("Adding transition {targetState:"+ t.getTargetState()+";#conditions:"+t.getConditions().size()+"}");
		transitions.add(t);
		//logger.debug("transition added");
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("<state>");
		sb.append("<name>").append(name).append("</name>");
		for (Transition t : transitions) {
			sb.append(t.toString());
		}
		sb.append("</state>");
		return sb.toString();
	}
}
