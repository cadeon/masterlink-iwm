package org.mlink.agent.util;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.Config;


public abstract class Condition {
	private static final Logger logger = Logger.getLogger(Condition.class);
	private static final DateFormat javaUtilDateFormatter = new SimpleDateFormat(Config.getProperty(Config.DATE_PATTERN));

	public static final String EQUAL                = "equal";
	public static final String GREATER_THAN         = "greaterThan";
	public static final String GREATER_THAN_OR_EQUAL= "greaterThanOrEqual";
	public static final String IS_NULL              = "isNull";
	public static final String LESS_THAN            = "lessThan";
	public static final String LESS_THAN_OR_EQUAL   = "lessThanOrEqual";
	public static final String NO_GUARD             = "noGuard";
	
	public static final String NULL                 = "null";

	String propertyClass;
	String propertyName;
	String propertyGetter;
	String value;
	Object targetValue;
	String type="";
	boolean not = false;
	
	public Condition(){
		//logger.debug("creating condition");
	}
	public Condition(boolean not){
		//logger.debug("creating condition");
		this.not=not;
	}
	
	public String getPropertyClass(){return propertyClass;}
	public String getPropertyName(){return propertyName;}
	public String getType(){return type;}
	public String getValue(){return value;}

	public void setPropertyName(String s){propertyName=s;propertyGetter=convert2Getter(s);}
	public void setType(String s){type=s;}
	public void setPropertyClass(String s) throws Exception {
		propertyClass=s;
		//just in case this is called after setValue
		if (value!=null) {
			setComparableValue(value);
		}
	}
	public void setValue(String s) throws Exception {
		//logger.debug("setting condition value to "+ s);
		value = s;
		//logger.debug("condition value set. Setting comparable value");
		setComparableValue(s);
	}
	
	private String convert2Getter(String s) {
		return "get".intern() + s.substring(0,1).toUpperCase() + s.substring(1);
	}
	private void setComparableValue(String s) throws Exception {
		if (NULL.equalsIgnoreCase(s) && !IS_NULL.equalsIgnoreCase(this.type)) 
			throw new BadComparisonException("Use condition type=\"isNull\" for comparisons to null values");
		try {
			if (propertyClass==null||propertyClass.length()<1) {
				targetValue=s;
			}
			else {
				Class c = Class.forName(propertyClass);
				if ("today".equalsIgnoreCase(s) && 
						(c.equals(java.sql.Date.class) ||
						 c.equals(java.sql.Timestamp.class) ||
						 c.equals(java.sql.Date.class) ))
					targetValue = s;
				else if (c.equals(java.sql.Date.class))
					targetValue = javaUtilDateFormatter.parse(s);	
				else if (c.equals(java.sql.Timestamp.class))
					targetValue = new java.sql.Date(Long.parseLong(s));	
				else if (c.equals(java.util.Date.class))
					targetValue = java.sql.Timestamp.valueOf(s);
				else if (c.equals(Boolean.class))
					targetValue = Boolean.valueOf(s);
				else if (c.equals(Double.class)) 
					targetValue = Double.parseDouble(s);			
				else if (c.equals(Float.class)) 
					targetValue = Float.parseFloat(s);
				else if (c.equals(Long.class)) 
					targetValue = Long.parseLong(s);
				else if (c.equals(Integer.class)) 
					targetValue = Integer.parseInt(s);
				else targetValue = s;
				
			}
		} catch (Exception e) {
			logger.debug("Error setting condition target value to "+s+" which is a "+propertyClass,e);
			throw e;
		}
	}
	
	public boolean verifyCondition(Object object) {
		boolean b = false;
		try {
			if (Condition.NO_GUARD.equals(this.type)) {
				return true;
			} else {
				//Use propertyName to get object property value
				Class c = object.getClass();
				Class[] args = {};
				Method propertyMethod = c.getMethod(propertyGetter, args); 
				Object[] argsO = {};
				Object propertyValue = propertyMethod.invoke(object, argsO);
				
				//Compare values
				b = compareValues(propertyValue,getTargetValue());
			}
		} catch (Exception e) {
			logger.error("Error during check of condition ", e);
			return false;
		}
		if (this.not) return !b;
		return b;
	}
	
	protected boolean isNull(Object propertyValue, Object targetValue) {
		if (propertyValue==null || targetValue==null) return true;
		return false;
	}
	protected boolean classMismatch(Object propertyValue,Object targetValue) {
		if (!propertyValue.getClass().equals(targetValue.getClass())) return true;
		return false;
	}
	protected boolean equals(Object propertyValue, Object targetValue) {
		if (isNull(propertyValue, targetValue)) return false;
		if (targetValue instanceof java.util.Date || targetValue instanceof java.sql.Date) {
			return compareMonthDayYear((java.util.Date)propertyValue,(java.util.Date)targetValue) == 0;
		}
		if (targetValue.equals(propertyValue)) return true;
		return false;
	}
	protected Object getTargetValue() throws ClassNotFoundException, ParseException {
		if (propertyClass==null) return null;
		Object value = null;
		Class c = Class.forName(propertyClass);
		if ("today".equals(targetValue)) {
			if (c.equals(java.sql.Date.class)) value = new java.sql.Date(System.currentTimeMillis());
			else if (c.equals(java.sql.Timestamp.class)) value = new java.sql.Timestamp(System.currentTimeMillis());
			else if (c.equals(java.util.Date.class)) value = new java.util.Date();
		}
		else value = targetValue;
		return value;		
	}
	protected int compareMonthDayYear(java.util.Date date1,java.util.Date date2) {
		// Compare just the date portion - use Calendar to extract the MONTH, DAY, and YEAR fields
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		int pmonth = c.get(Calendar.MONTH);
		int pday   = c.get(Calendar.DAY_OF_MONTH);
		int pyear  = c.get(Calendar.YEAR);
		
		c.setTime(date2);
		int tmonth = c.get(Calendar.MONTH);
		int tday   = c.get(Calendar.DAY_OF_MONTH);
		int tyear  = c.get(Calendar.YEAR);
		
		if (pyear > tyear) return 1;
		if (pyear < tyear) return -1;
		//pyear==tyear, check month
		if (pmonth > tmonth) return 1;
		if (pmonth < tmonth) return -1;
		//pmonth==tmonth, check day
		if (pday > tday) return 1;
		if (pday < tday) return -1;
		
		return 0;		
	}
	
	protected abstract boolean compareValues(Object propertyValue, Object targetValue);

	public String toString() {
		String notS = "";
		if (this.not) notS = "not=\"true\"";
		return "<condition type=\""+ type +"\" "+notS+"><propertyName>"+ propertyName +"</propertyName><propertyClass>"+
		       propertyClass+"</propertyClass><value>"+ value +"</value></condition>";
	}
}
