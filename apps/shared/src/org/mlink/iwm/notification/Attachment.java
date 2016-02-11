package org.mlink.iwm.notification;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * @author Andrei Povodyrev
 * Date: Mar 5, 2004
 */
public class Attachment implements Serializable {
    byte [] bytes;
    String contentType;
    String name;
    String description;

    public Attachment(byte[] bytes, String contentType, String name) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.name = name;
    }

    public Attachment(byte[] bytes, String contentType, String name, String description) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.name = name;
        this.description = description;
    }

    public Attachment(byte[] bytes, String contentType) {
        this.bytes = bytes;
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }
    public String getName(){return name;}
    public void setName(String aname){ this.name = aname;}
    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

