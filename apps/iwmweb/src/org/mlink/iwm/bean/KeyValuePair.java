package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 20, 2006
 */
public class KeyValuePair {
    Object id;
    Object value;

    public KeyValuePair(Object id, Object value) {
        this.id = id;
        this.value = value;
    }

    public Object getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

}
