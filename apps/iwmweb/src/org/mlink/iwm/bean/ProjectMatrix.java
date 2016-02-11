package org.mlink.iwm.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 24, 2007
 */
public class ProjectMatrix {
    private List<ProjectUnit> items;


    public List <ProjectUnit> generateTestData(){
        items = new ArrayList<ProjectUnit>();
        int jobId=1;
        for(int i=0;i<15;i++){
            ProjectUnit item = new ProjectUnit();
            item.setDescription("job " + jobId);
            item.setRefNumber(i+1);
            item.setSequenceNumber(i+1);
            item.setIndent(0);
            if(i==1) {
                item.setGroup(true);
                item.setDescription("subproject/group A" );
            }
            if(i==2) {
                item.setParent(items.get(1));
                jobId++;
            }else if(i==4){
                item.setGroup(true);
                item.setDescription("subproject/group B");
            }else{
                jobId++;
            }
            items.add(item);
        }
        buildMatrix();
        return items;
    }

    private void _moveItem(int index,ProjectUnit item){
        /* sanity check on upper limit*/
        if(index>=items.size()) index=items.size()-1;
        /* sanity check on lower limit*/
        if(index<0) index=0;

        /* if item is moved down when it is placed after current item at index
           if is moved up it is placed before current item at index*/
        int beforeMoveIndex=items.indexOf(item);
        int dir  = index-beforeMoveIndex;
        ProjectUnit itemAtIndex = items.get(index);
        if(dir<0){
            //itemAtIndex = getPrevious(itemAtIndex);
            if(itemAtIndex!=null){
                item.setSequenceNumber(itemAtIndex.getSequenceNumber()-1);
                item.setParent(itemAtIndex.getParent());
                //itemAtIndex = getPrevious(itemAtIndex);
            }
            //item.setSequenceNumber(itemAtIndexSeqNumber-1);
        }else if(dir>0){
            /*float itemAtIndexSeqNumber = itemAtIndex.getSequenceNumber();
            while(itemAtIndex!=null && itemAtIndex.getIndent()>item.getIndent()){
                itemAtIndexSeqNumber = itemAtIndex.getSequenceNumber();
                itemAtIndex = getNext(itemAtIndex);
            }
            item.setSequenceNumber(itemAtIndexSeqNumber+1); */
            //itemAtIndex = getNext(itemAtIndex);

            /* parent cannot be moved after its child*/
            while(itemAtIndex!=null && itemAtIndex.isChild(item)){
                itemAtIndex = getNext(itemAtIndex);
            }
            if(itemAtIndex!=null){
                item.setSequenceNumber(itemAtIndex.getSequenceNumber()+0.1f);
                item.setParent(getNext(itemAtIndex)==null?null:getNext(itemAtIndex).getParent());
            }
        }else{
            return; //no move
        }
        buildMatrix();
    }
    public void moveItem(int index,int refNumber){
        ProjectUnit item = getItem(refNumber);
        _moveItem(index,item);
    }


    public void addIndent(int refNumber) throws Exception{
        ProjectUnit item = getItem(refNumber);
        ProjectUnit prevItem = getPrevious(item);
        if(prevItem!=null){
            ProjectUnit parent = prevItem;
            if(prevItem.getIndent()>item.getIndent()){
                parent = prevItem.getParent();
                while(parent!=null && parent.getIndent()!=item.getIndent()){      //should only indent not more than 1 position
                    parent = parent.getParent();
                }
            }
            item.setParent(parent);
            buildMatrix();
        }
    }

    public void removeIndent(int refNumber) throws Exception{
        ProjectUnit item = getItem(refNumber);
        ProjectUnit parent = item.getParent();
        parent = parent==null?parent:parent.getParent();
        item.setParent(parent);
        buildMatrix();
    }

    /**
     * @param refNumber
     */
    public void removeItem(int refNumber){
        ProjectUnit item = getItem(refNumber);
        _removeItem(item);
        buildMatrix();
    }

    /**
     * cascade remove item
     * @param item
     */
    public void _removeItem(ProjectUnit item){
        item.remove();
        for (ProjectUnit unit : items) {
            if (unit.getParent() == item) {
                _removeItem(unit);
            }
        }
    }

    public void expand(int refNumber){
        ProjectUnit item = getItem(refNumber);
        _expand(item,!item.isExpand());
    }

    public void _expand(ProjectUnit item, boolean expand){
        item.setExpand(expand);
        for (ProjectUnit unit : items) {
            ProjectUnit parent = unit.getParent();
            if (parent == item) {
                _expand(unit,expand);
                unit.setDisplay(parent.isExpand());
            }
        }
    }
    private ProjectUnit getPrevious(ProjectUnit item){
        int itemIndex = items.indexOf(item);
        if(itemIndex!=0)
            return items.get(itemIndex-1);
        else
            return null;
    }

    private ProjectUnit getNext(ProjectUnit item){
        int itemIndex = items.indexOf(item);
        if(itemIndex!=(items.size()-1))
            return items.get(itemIndex+1);
        else
            return null;
    }

    private void buildMatrix(){
        Iterator it = items.iterator();
        while (it.hasNext()) {
            /* remove all marked deleted*/
            ProjectUnit unit =  (ProjectUnit)it.next();
            if(unit.isRemoved()){
                it.remove();
                continue;
            }
            /* make sure that all children placed after their parents in case parent moved or was pushed by moveItem()*/
            ProjectUnit parent = unit.getParent();
            if (parent != null) {
                unit.setIndent(parent.getIndent() + 1);
                unit.setSequenceNumber(parent.getSequenceNumber() + (1-1/unit.getSequenceNumber()));
            }else{
                unit.setIndent(0);
            }
        }
        /*for (ProjectUnit unit : items) {
            ProjectUnit parent = unit.getParent();
            if (parent != null) {
                unit.setIndent(parent.getIndent() + 1);
                unit.setSequenceNumber(parent.getSequenceNumber() + 0.1f);
            }
        }*/
        /* sort items by seqNumber*/
        Collections.sort(items);
        for (int i = 0; i < items.size(); i++) {
            ProjectUnit unit =  items.get(i);
            unit.setSequenceNumber(i+1);
        }

    }

    /**
     * @param refNumber
     * @return  ProjectItem for given itemId
     */
    public ProjectUnit getItem(int refNumber) throws RuntimeException{
        for (ProjectUnit item : this.items) {
            if (item.getRefNumber() == refNumber) {
                return item;
            }
        }
        throw new RuntimeException("Item not found refNumber=" + refNumber);
    }

    public List <ProjectItem> getDisplayItems(){
        if(items==null) generateTestData();
        List <ProjectItem> rtn = new ArrayList<ProjectItem>();
        for (ProjectUnit unit : items) {
            if(!unit.isDisplay()) continue;
            ProjectItem pi = new ProjectItem();
            pi.setDescription(unit.getDescription());
            pi.setDisplay(unit.isDisplay());
            pi.setIndent(unit.getIndent());
            pi.setReferenceNumber(unit.getRefNumber());
            pi.setSequenceNumber(unit.getSequenceNumber());
            pi.setType(unit.isGroup?"project":"job");
            rtn.add(pi);
        }
        return rtn;
    }

    class ProjectUnit implements Comparable{
        private long id;
        private int refNumber;
        private float sequenceNumber;
        private int indent = 0;
        private String description;
        private boolean expand=true;
        private boolean display=true;
        private boolean isGroup=false;
        private boolean removed=false;
        private ProjectUnit parent;
        private ProjectUnit prev;
        private ProjectUnit next;

        public boolean isRemoved() {
            return removed;
        }

        public void remove() {
            this.removed = true;
        }

        public ProjectUnit getPrev() {
            return prev;
        }

        public void setPrev(ProjectUnit prev) {
            this.prev = prev;
        }

        public ProjectUnit getNext() {
            return next;
        }

        public void setNext(ProjectUnit next) {
            this.next = next;
        }

        public ProjectUnit getParent() {
            return parent;
        }

        public boolean isChild(ProjectUnit item){
            ProjectUnit parent = getParent();
            while(parent!=null){
                if(parent==item) return true;
                parent = parent.getParent();
            }
            return false;
        }

        public void setParent(ProjectUnit parent) {
            this.parent = parent;
            //if(parent!=null){
            //    setIndent(parent.getIndent()+1);
            //}else{
            //    indent=0;
            //}
        }

        public float getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(float sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }


        public int getRefNumber() {
            return refNumber;
        }

        public void setRefNumber(int refNumber) {
            this.refNumber = refNumber;
        }

        public int getIndent() {
            return indent;
        }

        public void setIndent(int col) {
            this.indent = col;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isExpand() {
            return expand;
        }

        public void setExpand(boolean expand) {
            this.expand = expand;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }

        public boolean isGroup() {
            return isGroup;
        }

        public void setGroup(boolean group) {
            isGroup = group;
        }

        public int compareTo(Object o){
            if(o == null) return 1;
            Float other = ((ProjectUnit)o).getSequenceNumber();
            Float thisOne	=	getSequenceNumber();
            return thisOne.compareTo(other);
        }

        public String toString() {
            return "Desc="+ getDescription() + " SeqNumber=" + getSequenceNumber() + " indent=" + getIndent();
        }
    }

}
