package org.mlink.iwm.struts.form;

/**
 * User: andrei
 * Date: Oct 13, 2006
 */
public class ContextEntry{
    String key,value;

    public ContextEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
