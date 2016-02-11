package org.mlink.agent.util;

public class LessThanOrEqual extends LessThan {

	public LessThanOrEqual() {type=Condition.LESS_THAN_OR_EQUAL;}
	public LessThanOrEqual(boolean not) {
		super(not);
		type=Condition.LESS_THAN_OR_EQUAL;
	}
	
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		if (super.isNull(propertyValue, targetValue)) return false;
		if (super.compareValues(propertyValue,targetValue)) return true;
		return super.equals(propertyValue,targetValue);
	}

}
