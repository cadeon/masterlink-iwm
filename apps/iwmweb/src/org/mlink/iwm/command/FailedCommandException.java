package org.mlink.iwm.command;

public class FailedCommandException extends RuntimeException {

	private String command;
	
    public FailedCommandException() {
        super();
    }
    public FailedCommandException(String message) {
        super(message);
    }
    public FailedCommandException(String message,String command) {
        super(message);
        this.command=command;
    }
    public FailedCommandException(Throwable cause) {
        super(cause);
    }
    public FailedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
   
	public String getCommand(){return command;}
	public void setCommand(String s) {command=s;}
}
