package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Apr 17, 2007
 */
public class GlobalSearchResponseItem {
    private String text;
    private String type;
    private String link;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
