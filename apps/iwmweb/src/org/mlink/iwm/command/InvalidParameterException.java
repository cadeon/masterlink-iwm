package org.mlink.iwm.command;

public class InvalidParameterException extends RuntimeException {

	String paramName;
	String paramValue;
	
    public InvalidParameterException() {
        super();
    }
    public InvalidParameterException(String param,String value) {
        super();
        paramName=param;
        paramValue=value;
    }
    public InvalidParameterException(String message) {
        super(message);
    }
    public InvalidParameterException(String message,String param,String value) {
        super(message);
        paramName=param;
        paramValue=value;
    }
    public InvalidParameterException(Throwable cause) {
        super(cause);
    }
    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
    
	public String getParamName() {return paramName;}
	public String getParamValue(){return paramValue;}
	
	public void setParamName(String s) {paramName=s;}
	public void setParanValue(String s){paramValue=s;}
}
