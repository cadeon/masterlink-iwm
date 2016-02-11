package org.mlink.agent.util;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;
import org.xml.sax.Attributes;

public class ConditionFactory implements ObjectCreationFactory {
	
	public ConditionFactory() {}
	
	/**
	 * Get Condition as determined by the specified attribute describing the type.
	 * @param Attributes Contains the "type" key/value pair specifying the type of condition
	 * @returns Condition
	 * @throws Exception
	 */
	public Object createObject(Attributes attributes) throws Exception {
		if (attributes==null) throw new Exception("Invalid Condition: type was null");
		String type = attributes.getValue("type");
		String sNot = attributes.getValue("not");
		boolean not = "true".equalsIgnoreCase(sNot);
		if (Condition.EQUAL.equalsIgnoreCase(type)) return new Equal(not);
		if (Condition.GREATER_THAN.equalsIgnoreCase(type)) return new GreaterThan(not);
		if (Condition.GREATER_THAN_OR_EQUAL.equalsIgnoreCase(type)) return new GreaterThanOrEqual(not);
		if (Condition.IS_NULL.equalsIgnoreCase(type)) return new IsNull(not);
		if (Condition.LESS_THAN.equalsIgnoreCase(type)) return new LessThan(not);
		if (Condition.LESS_THAN_OR_EQUAL.equalsIgnoreCase(type)) return new LessThanOrEqual(not);
		if (Condition.NO_GUARD.equalsIgnoreCase(type)) return new NoGuard();
		throw new Exception("Invalid Condition: type was "+ type);
		
	}

	public Digester getDigester() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDigester(Digester arg0) {
		// TODO Auto-generated method stub
		
	}

}
