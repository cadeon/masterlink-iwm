package org.mlink.iwm.lookup;

import org.mlink.iwm.util.Constants;

import java.util.Collection;
import java.util.ArrayList;


public class TreeOptionItem extends OptionItem {
    protected Long parentId;

    protected TreeOptionItem parent;
    protected Collection children = new ArrayList();
    protected Collection peers  = new ArrayList();
    protected int level;
    private Integer schemaId;


    public final static TreeOptionItem emptyItem = new TreeOptionItem(Constants.emptyOptionId, Constants.emptyOptionValue,null,null);   // first(empty element of the drop down

    public TreeOptionItem(Long value, String label, Long parentId, Integer schemaId) {
        super(value, label);
        this.parentId = parentId;
        this.schemaId = schemaId;
    }

    public Integer getSchemaId() {
        return schemaId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public TreeOptionItem getParent() {
        return parent;
    }

    public void setParent(TreeOptionItem parent) {
        this.parent = parent;
    }

    public Collection getChildren() {
        return children;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public Collection getPeers() {
        return peers;
    }

    public void setPeers(Collection peers) {
        this.peers = peers;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString(){
        return "value="+getValue() + "\t parent=" + getParentId() + "\t label=" + getLabel();
    }
}
