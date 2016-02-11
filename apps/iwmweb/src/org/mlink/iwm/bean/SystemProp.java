package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class SystemProp {  
	private String id;
	private String property;
    private String value;
    private String description;
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}    
}
