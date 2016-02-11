package org.mlink.iwm.bean;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 16, 2007
 *  ProjectItemCol represents a collection of project items and has a number of convinience methods for manipulation of the collection
 */
public class ProjectItemCol {
    private List<ProjectItem> items;


    /**
     * @param refNumber
     * @return  ProjectItem for given itemId
     */
    public ProjectItem getItem(long refNumber) throws Exception{
        for (ProjectItem projectItem : this.items) {
            if (projectItem.getReferenceNumber() == refNumber) {
                return projectItem;
            }
        }
        throw new Exception("Item not found refNumber=" + refNumber);
    }

    public void setItems(List<ProjectItem> items) {
        this.items = items;
        sort();
    }

    public void removeItem(ProjectItem item){
        List <ProjectItem> children = item.getChildren();
        if(children!=null){
            for (ProjectItem child : children) {
                getItems().remove(getItems().indexOf(child));
            }
        }
        getItems().remove(getItems().indexOf(item));
        sort();
    }

    public void addItem(int index,ProjectItem item){
        items.add(index,item);
        sort();
    }

    private final ProjectItem indexHolder = new ProjectItem();
    private void moveTo(int index,ProjectItem item){
        /* sanity check on upper limit*/
        if(index>items.size()) index=items.size();
        /* sanity check on lower limit*/
        if(index<0) index=0;
        /* get item currently located at the given index */
        ProjectItem itemAtTarget = items.get(index);

    }
    public void moveItem(int index,ProjectItem item){
        /* sanity check on upper limit*/
        if(index>=items.size()) index=items.size()-1;
        /* sanity check on lower limit*/
        if(index<0) index=0;

        /* if item is moved down when it is placed after current item at index
           if is moved down it is placed before current item at index*/
        int beforeMoveIndex=items.indexOf(item);
        int dir  = index-beforeMoveIndex;
        ProjectItem itemAtIndex = items.get(index);
        if(dir<0){
            item.placeAfter(itemAtIndex.getPrevious());
        }else if(dir>0){
            item.placeAfter(itemAtIndex);
        }else{
            return; //no move
        }
        sort();
    }
    public void moveItem2(int index,ProjectItem item){
        /* sanity check on upper limit*/
        if(index>items.size()) index=items.size();
        /* sanity check on lower limit*/
        if(index<0) index=0;


        /* if item is moved down when it needs to be placed after current item at index
           if is moved down it needs to be placed before current item at index*/
        int beforeMoveIndex=items.indexOf(item);
        int dir  = (index-beforeMoveIndex)>0?1:-1;

        /* Must check if index is a valid move. Ex: Parent cannot be positioned after a child*/
        boolean indexVerified=false;
        while(!indexVerified && index < items.size()){
            ProjectItem itemAtIndex = items.get(index);
            //if(item.isParent(itemAtIndex)) {
            if(item.getIndent()!=itemAtIndex.getIndent()) {
                index=index+dir;
            }else{
                indexVerified=true;
            }
        }

        if(!indexVerified) return; //item cannot be moved




        items.remove(item);
        //items.add(beforeMoveIndex,indexHolder);
        items.add(index,item);

        if(item.getParent()!=null){

        }

        //items.remove(beforeMoveIndex);
        moveChildren(item);

        List<ProjectItem> col = new ArrayList<ProjectItem>();
        for (ProjectItem pi : getItems()) {
            //col.add(pi);
            if(item.getParent()!=pi){   //item does not need be moved as already moved, allowing the second move screwing if moved item is a child
                _moveItem(pi,col);
            }else{
                if(col.indexOf(item.getParent())<0) col.add(item.getParent());
            }
        }

        setItems(col);
        sort();
    }

    private void moveChildren(ProjectItem item){
        int parentIndex  = items.indexOf(item);
        List <ProjectItem> children = item.getChildren();
        for (int i = 0; i < children.size(); i++) {
            ProjectItem child =  children.get(i);
            int beforeMoveIndex=items.indexOf(child);
            items.remove(child);
            items.add(beforeMoveIndex,indexHolder);
            items.add(parentIndex+1+i,child);
            moveChildren(child);
        }
    }

    private void _moveItem(ProjectItem item, List <ProjectItem> col){
        if(col.indexOf(item)<0) col.add(item);
        List <ProjectItem> children = item.getChildren();
        for (int i = 0; i < children.size(); i++) {
            ProjectItem child =  children.get(i);
            if(col.indexOf(child)<0) col.add(child);
            _moveItem(child,col);
        }
    }

    public void addItem(ProjectItem item){
        items.add(item);
        sort();
    }

    public List<ProjectItem> getItems() {
        return items;
    }

    public List <ProjectItem> getDisplayItems(){
        List <ProjectItem> rtn = new ArrayList<ProjectItem>();
        List <ProjectItem> lst = getItems();
        for (ProjectItem item : lst) {
            if (item.getDisplay()) rtn.add(item);
        }
        return rtn;
    }

    /**
     * Sort and reset the links
     */
    private void sort(){
        Collections.sort(items);
        ////while(getItems().indexOf(indexHolder)>=0){
        //    getItems().remove(indexHolder);
        //}

        for (int i = 0; i < getItems().size(); i++) {
            ProjectItem item = getItems().get(i);
            item.setSequenceNumber(i+1);
            if(i!=0)
                item.setPrevious(getItems().get(i-1));
            else{
                item.setPrevious(null);
            }
        }
    }


}
