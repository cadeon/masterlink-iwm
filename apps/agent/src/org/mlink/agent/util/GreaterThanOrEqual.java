package org.mlink.agent.util;

public class GreaterThanOrEqual extends GreaterThan {

	public GreaterThanOrEqual(){type=Condition.GREATER_THAN_OR_EQUAL;}
	public GreaterThanOrEqual(boolean not) {
		super(not);
		type=Condition.GREATER_THAN_OR_EQUAL;
	}
	
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		if (super.isNull(propertyValue, targetValue)) return false;
		if (super.compareValues(propertyValue,targetValue)) return true;
		return super.equals(propertyValue,targetValue);
	}

}
