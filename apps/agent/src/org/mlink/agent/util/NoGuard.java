package org.mlink.agent.util;

public class NoGuard extends Condition {

	public NoGuard() {
		type=Condition.NO_GUARD;
	}
	@Override
	protected boolean compareValues(Object propertyValue, Object targetValue) {
		return true;
	}
	@Override
	public void setPropertyClass(String s) throws Exception {
		propertyClass=s;
	}
	@Override
	public void setValue(String s) throws Exception {
		value = s;
	}
}
