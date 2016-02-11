package org.mlink.agent.util;

public class GreaterThan extends Condition {

	public GreaterThan(){type=Condition.GREATER_THAN;}
	public GreaterThan(boolean not) {
		super(not);
		type=Condition.GREATER_THAN;
	}
	
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		// Just check the propertyValue class in this method, b/c if the targetValue class,
		// which we already know from the job state manager xml, doesn't match, we want a 
		// ClassCastException to be thrown from here so that either the xml file, the code,
		// or both, can be examined and fixed.

		if (super.isNull(propertyValue, targetValue)) return false;
		if (propertyValue instanceof java.util.Date || propertyValue   instanceof java.sql.Date) {
			return super.compareMonthDayYear((java.util.Date)propertyValue,(java.util.Date)targetValue) > 0;
		}
		if (propertyValue instanceof java.sql.Timestamp) {
			return ((java.sql.Timestamp)propertyValue).after((java.sql.Timestamp)targetValue);				
		}
		if (propertyValue instanceof String) {
			return (propertyValue.toString().compareTo(targetValue.toString()) > 0);	
		}
		if (propertyValue instanceof Boolean) {
			throw new BadComparisonException("Cannot use > on Boolean values: {propertyValue:"+propertyValue+";targetValue:"+targetValue+"}");		
		}
		if (propertyValue instanceof Double) {
			return ((Double)propertyValue).compareTo((Double)targetValue) > 0;			
		}
		if (propertyValue instanceof Float) {
			return ((Float)propertyValue).compareTo((Float)targetValue) > 0;
		}
		if (propertyValue instanceof Long) {
			return ((Long)propertyValue).compareTo((Long)targetValue) > 0;
		}
		if (propertyValue instanceof Integer) {
			return ((Integer)propertyValue).compareTo((Integer)targetValue) > 0;
		}
		return false;
	}

}
