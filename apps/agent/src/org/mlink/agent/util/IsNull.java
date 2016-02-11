package org.mlink.agent.util;

public class IsNull extends Condition {

	public IsNull() {type=Condition.IS_NULL;}
	public IsNull(boolean not) {
		super(not);
		type=Condition.IS_NULL;
	}
	
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		return propertyValue==null;
	}

}
