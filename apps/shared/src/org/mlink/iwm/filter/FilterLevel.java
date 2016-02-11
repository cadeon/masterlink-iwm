package org.mlink.iwm.filter;

import org.mlink.iwm.lookup.CodeLookupValues;
import org.mlink.iwm.lookup.TreeOptionItem;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a level in collection of levels that comprise a filter (tree)
 * User: andrei
 * Date: Sep 28, 2004
 */
public class FilterLevel {
    private Long selectedId = TreeOptionItem.emptyItem.getValue();  // represents active (selected) item in options
    private Collection options = new ArrayList();  //TreeOptionItems
    private String schemaName;


    public FilterLevel(String schemaName) {
        this.options.add(TreeOptionItem.emptyItem);  //first item in the options list is empty
        this.schemaName = schemaName;
    }

    public FilterLevel() {
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }


    public Collection getOptions() {
        return options;
    }

    public void setOptions(Collection options) {
        this.options.addAll(options);
    }
}
