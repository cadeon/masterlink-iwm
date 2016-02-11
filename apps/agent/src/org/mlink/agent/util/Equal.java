package org.mlink.agent.util;

public class Equal extends Condition {

	public Equal() {type=Condition.EQUAL;}
	public Equal(boolean not){
		super(not);
		type=Condition.EQUAL;
	}
	
	
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		if (super.isNull(propertyValue, targetValue)) return false;
		// Compare just the date portion (and drop the time portion) of date values
		// by using java.util.Date
		if (targetValue instanceof java.util.Date || targetValue instanceof java.sql.Date) {
			return ((java.util.Date)targetValue).equals(propertyValue);
		}
		return super.equals(propertyValue,targetValue);
	}

}
