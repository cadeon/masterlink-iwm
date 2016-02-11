package org.mlink.agent.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Transition {

	private static Logger logger= Logger.getLogger(Transition.class);
	private String targetState;
	private ArrayList<Condition> conditions;
	
	public Transition() {
		//logger.debug("creating transition");
		conditions=new ArrayList<Condition>();
	}
	
	public String getTargetState(){return targetState;}
	public ArrayList<Condition> getConditions(){return conditions;}
	
	public void setTargetState(String s){targetState=s;}
	public void setConditions(ArrayList<Condition> al){conditions=al;}
	
	public void addCondition(Condition c){
		//logger.debug("Adding condition {type:"+ c.getType() +";property:"+c.getPropertyName() +";value:"+ c.getValue() +")");
		conditions.add(c);
		//logger.debug("condition added");}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("<transition>");
		sb.append("<targetState>").append(targetState).append("</targetState>");
		for (Condition condition : conditions) {
			sb.append(condition.toString());
		}
		sb.append("</transition>");
		return sb.toString();
	}

}
