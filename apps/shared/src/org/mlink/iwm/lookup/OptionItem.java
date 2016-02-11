package org.mlink.iwm.lookup;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class OptionItem implements Serializable{
    protected Long value;
    protected String label;
    protected String code;
    protected boolean disabled;
    protected Map customProps = new HashMap();


    public OptionItem() {    }   //Required for BeanUtils reflection

    /**
     * This version is used to create a null entry in dropdown
     * @param label
     */
    public OptionItem(String label) {
        this.value = null;
        this.label = label;
        this.code = null;

    }

    public OptionItem(String value, String label) {
        this.value = Long.valueOf(value);
        this.label = label;
        this.code = null;

    }

    public OptionItem(String value, String label, String code) {
        this.value = Long.valueOf(value);
        this.label = label;
        this.code = code;
    }

    public OptionItem(Long value, String label) {
        this.value = value;
        this.label = label;
        this.code = null;
    }



    public void addProperty(String name, Object value){
        customProps.put(name,value);
    }

    public Object getProperty(String name){
        return customProps.get(name);
    }


    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String toString(){
        return "value="+getValue() + "\t parent=" + "\t label=" + getLabel();
    }
}
