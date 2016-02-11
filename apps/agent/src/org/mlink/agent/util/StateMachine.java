package org.mlink.agent.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;


public class StateMachine {
	private static Logger logger= Logger.getLogger(StateMachine.class);
	private static final String ANY = "ANY";
    
	private HashMap<String,State> states;
	
	
	public StateMachine() {
		states = new HashMap<String,State>();
	}
    
	public State                 getState(String s){return states.get(s);}
	public HashMap<String,State> getStates()       {return states;}
	
	public void setState(String s,State st)        {states.put(s,st);}
	public void setStates(HashMap<String,State> hm){states = hm;}
	
	public void addState(State state) {
		//log("adding state {name:"+ state.getName()+";#transitions:"+state.getAllTransitions().size()+"}");
		states.put(state.getName(), state);
		//log("state added");
	}
	
	public State transition(Object o, String currentState) {
		State state = null;
		String targetState = null;
		try {
			state = states.get(currentState);
      if (state == null) return null;
			State any = states.get(ANY); // must also check states that can be reached from any state, e.g. CIA, EJO
			List<Transition> transitions = new ArrayList<Transition>();
			if (state!=null) transitions.addAll(state.getAllTransitions());
			if (any!=null) transitions.addAll(any.getAllTransitions());
            for (Transition t : transitions) {  
            	targetState = t.getTargetState(); // assigment made for readability/use in error msg
            	if (targetState==null)	throw new BadComparisonException("Transition must have a target state");
				ArrayList<Condition> conditions = t.getConditions();
				if (canTransition(o,conditions)) return this.getState(targetState);
			}
			
		} catch (Exception e) {
			log("Error in transition for Job "+ o.toString() +" from state "+ currentState +
			    " to state "+ targetState, e);
		}
		return state;
	}
	
	private boolean canTransition(Object o, ArrayList<Condition> conditions) {
		for (Condition c : conditions) {
			if (!c.verifyCondition(o)) return false;
		}
		return true;
	}
	
	public int getStateCount() {return states.size();}
	public String toString() {
		StringBuffer sb = new StringBuffer("<states>");
		Collection<State> values = states.values();
		for (State state : values) {
			sb.append(state.toString());
		}
		sb.append("</states>");
		return sb.toString();
	}
	
	private void log(Object o)            {logger.debug(o);}
	private void log(Object o,Exception e){logger.error(o,e);}
}
