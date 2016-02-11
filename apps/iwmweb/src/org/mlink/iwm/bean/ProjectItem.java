package org.mlink.iwm.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 12, 2007
 */
public class ProjectItem implements Comparable{
    private long id;
    private float sequenceNumber;
    private int referenceNumber;
    private int indent = 0;
    private String description;
    private boolean expand=true;
    private boolean display=true;     //deprecated
    private ProjectItem parent;
    private List<ProjectItem> children = new ArrayList<ProjectItem>();
    private List<ProjectItem> precedes = new ArrayList<ProjectItem>();
    private ProjectItem previous;
    private ProjectItem next;
    private enum TYPE{project,job}    // project is parent(aka group), job is job(or a task)
    private TYPE type = TYPE.job;

    public String getType() {
        return type.toString();
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setType(String type) {
        TYPE [] types = TYPE.values();
        for (TYPE tmp : types) {
            if (tmp.toString().equals(type)) {
                this.type = tmp;
                break;
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getSequenceNumber() {
        return sequenceNumber;
    }

    public float getLastChildSequenceNumber() {
        float rtn = sequenceNumber;
        for (ProjectItem child : children) {
            rtn = child.getLastChildSequenceNumber();
        }
        return rtn;
    }

    /**
     * SeqNumber is (list index + 1)
     * if this item is a parent, then seqNumbers of all children are incremented relative to parent
     * @param sequenceNumber
     */
    public void setSequenceNumber(float sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        for (ProjectItem child : children) {
            child.setSequenceNumber(sequenceNumber+0.1f);
        }
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
        for (ProjectItem child : children) {
            child.setIndent(indent+child.getIndent());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void addIndent(){
        if(getPrevious()!=null){
            setParent(previous);
        }
    }

    /**
     * Add Indent will have effect if this item can be indented which means either previous is a parent or previous is a child
     */
    private void addIndent2(){
        if(getPrevious()!=null){
            if(getPrevious().isProject()) {
                setParent(previous);
            }
            else if(getPrevious().getParent()!=null){
                setParent(getPrevious().getParent());
            }
        }
    }

    /**
     *
     * @param item
     * @return true if parameter is a child
     */
    public boolean isParent(ProjectItem item){
        ProjectItem parent = item.getParent();
        while(parent!=null){
            if(parent.equals(this)) return true;
            parent = parent.getParent();
        }
        return false;
    }


    public void placeAfter(ProjectItem item){
        if(item==null)  {
            this.setSequenceNumber(0);  //very first item
            return;
        }

        /* Must check if index is a valid move. Ex: Parent cannot be positioned after a child*/
        boolean isValidItem=false;
        while(!isValidItem && item!=null){
            if(this.isParent(item)) {
                item = item.getNext();
            }else{
                isValidItem=true;
            }
        }
        if(!isValidItem) return; // placement is not possible

        /* now we have identified a valid item to place this after*/
        /* if the item is a project or a child then this becomes a child. Unless this and item have the same indent*/
        if(this.getIndent()!=item.getIndent()){
            if(item.isProject()){
                this.setParent(item);
            }else if(item.getParent()!=null){
                this.setParent(item.getParent());
            }else{
                setParent(null);
            }
        }
        this.setSequenceNumber(item.getLastChildSequenceNumber()+0.1f);
    }

    /**
     * recover entire sequence of items,sort and reset the links
     */
    private void sort(){
        ProjectItem item = this;
        while(item.getPrevious()!=null){     //find the first item
            item=item.getPrevious();
        }
        List <ProjectItem> all = new ArrayList<ProjectItem>();
        while(item!=null){
            all.add(item);
            item=item.getNext();
        }

        Collections.sort(all);
        for (int i = 0; i < all.size(); i++) {
            item = all.get(i);
            item.setSequenceNumber(i+1);
            if(i!=0)
                item.setPrevious(all.get(i-1));
            else{
                item.setPrevious(null);
            }
        }
    }
    private boolean isProject(){
        return getType().equals(TYPE.project.toString());
    }

    public void removeIndent(){
        if(getPrevious()!=null){
            if(getPrevious().isParent(this)){
                getPrevious().getChildren().remove(this);
                setParent(getPrevious().getParent());
            }
            if(getPrevious().getParent()!=null){
                setParent(getPrevious().getParent());
            }
        }
    }

    private void removeIndent2(){
        if(previous==null){
            this.indent=0;
        }else if (previous.isProject()){
            setParent(previous.getParent());
        }
    }

    public List<ProjectItem> getChildren() {
        return children;
    }

    public void addChild(ProjectItem item){
        children.add(item);
    }

    public void setChildren(List<ProjectItem> children) {
        this.children = children;
    }

    public ProjectItem getParent() {
        return parent;
    }

    public void setParent(ProjectItem parent) {
        if(getParent()!=null){  //remove from old parent first
            getParent().getChildren().remove(this);
        }
        this.parent = parent;
        if(parent!=null){
            setIndent(parent.getIndent()+1);
            parent.addChild(this);
        }
        else{
            this.indent=0;
        }
    }

    public ProjectItem getPrevious() {
        return previous;
    }

    public void setPrevious(ProjectItem previous) {
        this.previous = previous;
        if(previous!=null) previous.setNext(this);
    }

    public ProjectItem getNext() {
        return next;
    }

    public void setNext(ProjectItem next) {
        this.next = next;
    }

    public boolean getExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
        for (ProjectItem item : getChildren()) {
            item.setDisplay(expand);
            if(item.getChildren().size()!=0){
                item.setExpand(expand);
            }
        }
    }

    public boolean getDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public int compareTo(Object o){
        if(o == null) return 1;
        Float other = ((ProjectItem)o).getSequenceNumber();
        Float thisOne	=	getSequenceNumber();
        return thisOne.compareTo(other);
    }


    public String toString() {
        return "Desc="+ getDescription() + " SeqNumber=" + getSequenceNumber();
    }
}
