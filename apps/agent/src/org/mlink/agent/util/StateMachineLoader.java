package org.mlink.agent.util;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

public class StateMachineLoader {
	private static final Logger logger = Logger.getLogger(StateMachineLoader.class);

	/*
     * <states>
     *  <state>
     *    <name>RFS</name>
     *    <transition>
     *      <targetState>DPD</targetState>
     *      <condition type="lessThan">
     *          <property>latestStartDate</property>
     *          <value>today</value>
     *      </condition>
     *      <condition type="">
     *          <property></property>
     *          <value></value>
     *      </condition>
     *    </transition>
     *  </state>
     * </states>
     * 
     */
    public static StateMachine load(String propFile) throws Exception {
    	Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("states", StateMachine.class);
        
        digester.addObjectCreate("states/state",State.class);
        
        digester.addBeanPropertySetter("states/state/name","name");
        
        digester.addObjectCreate("states/state/transition",Transition.class);
        
        digester.addBeanPropertySetter("states/state/transition/targetState","targetState");

        digester.addFactoryCreate("states/state/transition/condition", ConditionFactory.class);
        digester.addBeanPropertySetter("states/state/transition/condition/propertyName","propertyName");
        digester.addBeanPropertySetter("states/state/transition/condition/propertyClass","propertyClass");
        digester.addBeanPropertySetter("states/state/transition/condition/value","value");
        digester.addSetNext("states/state/transition/condition", "addCondition");
        
        digester.addSetNext("states/state/transition", "addTransition");
        digester.addSetNext("states/state","addState");
        
        log("Parsing input file: "+ propFile);
        StateMachine sm  =  (StateMachine) digester.parse(PropertyStream.getPropertyResourceAsStream(propFile));
        log("Parse complete");
        log("State machine loaded ("+ sm.getStateCount() +" states)");
        //log(sm.toString());
        
        return sm;
    }

    
    public static void main(String[] args) {
        try {
           StateMachine sm = load("jobstatemanager.xml"); 
           System.out.println( sm.toString() );
    
        } catch( Exception exc ) {
           exc.printStackTrace();
        }
    }
    
    private static void log(Object o) {
    	logger.debug(o);
    }
}
