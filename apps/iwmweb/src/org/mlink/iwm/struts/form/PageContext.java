package org.mlink.iwm.struts.form;

import org.mlink.iwm.struts.form.ContextEntry;

import java.util.List;
import java.util.ArrayList;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class PageContext {
    List<ContextEntry> entries;
    String title;

    public PageContext() {
        this.title = "";
    }

    public PageContext(String title) {
        this.title = title;
    }

    public void add(String key, String value){
        if(entries==null) entries = new ArrayList<ContextEntry>();
        entries.add(new ContextEntry(key,value));
    }

    public void add(String key, Object value){
        if(value!=null){
            add(key,value.toString());
        }
    }

    public String getTitle() {
        return title;
    }

    public List<ContextEntry> getEntries() {
        return entries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(entries!=null) {
            for (ContextEntry contextEntry : entries) {
                sb.append(contextEntry.getKey()).append("=").append(contextEntry.getValue());
            }
        }
        return sb.toString();
    }

}

