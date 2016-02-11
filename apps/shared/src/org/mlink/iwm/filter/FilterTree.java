package org.mlink.iwm.filter;

import org.mlink.iwm.lookup.LocatorOption;
import org.mlink.iwm.lookup.TreeOptionItem;

import java.util.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * @author Andrei Povodyrev
 * Date: May 16, 2004
 * This class represents a tree and intended for IWM filters such as LocatorTree
 * It should not be used directly but should be facaded with concrete filter implementation
 * see LocatorTree
 * Tree consists of nodes(elements) which are TreeOptionItem classes
 * Each TreeOptionItem will be set with parent, peers, and immediate children elements
 */
public class FilterTree {
    Collection elements;
    Collection rootElements;
    public static final long nullId = -1;
    public static final Long NullId = new Long(nullId);
    /**
     * Collection of of TreeOptionItem objects
     * @param optionItems
     */
    public FilterTree(Collection optionItems) {
        this.elements = optionItems;
        linkElements();
    }



    public Collection getElements() {
        return elements;
    }

    public Collection getRootElements(){
        return rootElements;
    }


    /**
     * Skim FilterTree makes a subset of the current  FilterTree instance by trimming (discarding)
     * optiond found in parallel trees and keeping the tree on which given TreeOptionItem(or optionId) resides
     * @param optionId
     * @return
     */
    private FilterTree skim(Long optionId){
        List trim = new ArrayList();
        TreeOptionItem selectedOption = getTreeOptionItem(optionId);
        trim.addAll(selectedOption.getChildren());
        trim.addAll(selectedOption.getPeers());
        trim.addAll(getElementsAbove(selectedOption));
        return new FilterTree(trim);
    }


    public List splitInLevels(Long selectedId){
        return splitInLevels(getTreeOptionItem(selectedId));
    }

    /**
     * Get tree levels, a convinience method for creating tree-based collection of dropdowns
     * @param selected
     * @return Collection of collections which one of them represents elements that belong to the same level
     *         Starts from root elements till selected. The last collection represent children of the selected.
     */


    public List splitInLevels(TreeOptionItem selected){
        List levels = new ArrayList();
        if(selected ==null){
            levels.add(getRootElements());
        }else{
            levels.add(selected.getChildren());
            levels.add(selected.getPeers());

            TreeOptionItem parent = selected.getParent();
            while(parent != null){
                levels.add(parent.getPeers());
                parent = parent.getParent();
            }
        }

        List levelsInversed = new ArrayList();
        for (int i = levels.size(); i > 0; i--) {
            levelsInversed.add(levels.get(i-1));
        }
        return levelsInversed;
    }


    /**
     * Creates relation between elements
     */
    private void linkElements(){
        rootElements = new ArrayList();
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            TreeOptionItem option = (TreeOptionItem) iterator.next();
            if(option.getParentId() == null){
                rootElements.add(option);
            }else{
                TreeOptionItem parent = getTreeOptionItem(option.getParentId());
                option.setParent(parent);
            }
            option.setPeers(getPeers(option));
            option.setChildren(getFirstChildren(option));
        }
    }

    private Collection getElementsAbove(TreeOptionItem child){
        Collection col = new ArrayList();
        TreeOptionItem parent = child.getParent();//getParent(child);
        if(parent == null){
            return col;
        }else{
            col.addAll(parent.getPeers()); //getPeers(parent));
            col.addAll(getElementsAbove(parent));
        }
        return col;
    }

    /**
     * Get all parents till the root element for a given child
     * @param child
     * @return Collection
     */
    public Collection getParents(TreeOptionItem child){
        Collection col = new ArrayList();
        if(child == null)
            return col;

        TreeOptionItem parent = child.getParent();
        while(parent!= null){
            col.add(parent);
            parent = parent.getParent();
        }

        return col;
    }

    /**
     * Get all parent Ids (incuding self) for the given element id(Long). Parent order starts from the top
     * @param nodeId
     * @return
     */
    public Collection getFamily(Long nodeId){
        Collection parents = getParents(getTreeOptionItem(nodeId));
        Collection ids = new ArrayList();
        if(nodeId != null) ids.add(nodeId);
        for (Iterator iterator = parents.iterator(); iterator.hasNext();) {
            TreeOptionItem item = (TreeOptionItem) iterator.next();
            ids.add(item.getValue());
        }
        Collections.reverse((List)ids);
        return ids;
    }

    /**
     * Return top most parent for given nodeId
     * if noo parent found, return itself
     * @param nodeId
     * @return  TreeOptionItem
     */
    public TreeOptionItem getTopMostParent(Long nodeId){
        TreeOptionItem parent = getTreeOptionItem(nodeId);
        while(parent.getParent()!= null){
            parent = parent.getParent();
        }
        return parent;
    }


    /**
     * Get options that reside on the same root as the given option including itself
     * @param option
     * @return
     */
    private Collection getPeers(TreeOptionItem option){
        if(option.getParent()==null){ //getParent(option)== null){
            return getRootElements();
        }else{
            return getFirstChildren(option.getParent());//getParent(option));
        }
    }

    /**
     * Get first level children for the given option
     * @param currentItem
     * @return   Collection
     */
    public Collection getFirstChildren(TreeOptionItem currentItem){
        Collection col = new ArrayList();
        if(currentItem == null)
            return col;

        Long parentId = currentItem.getValue();
        Collection rtn = new ArrayList();
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            TreeOptionItem option = (TreeOptionItem) iterator.next();
            if(parentId.equals(option.getParentId())){
                rtn.add(option);
            }
        }
        return rtn;
    }

    /**
     * Get all children for the given option (including children of children, etc..)
     * @param currentItem
     * @return
     */
    public Collection getChildren(TreeOptionItem currentItem){
        Collection children = new ArrayList();
        buildChildren(currentItem,children);
        return children;
    }

    public void buildChildren(TreeOptionItem currentItem, Collection col){

        if(currentItem == null)
            return;
        if(col==null)
            col=new ArrayList();

        Collection children = currentItem.getChildren();
        if(children.size()>0){
            col.addAll(children);
            for (Iterator iterator = children.iterator(); iterator.hasNext();) {
                TreeOptionItem child = (TreeOptionItem) iterator.next();
                buildChildren(child,col);
            }
        }

    }


    /**
     * Get TreeOptionItem object for given optionId
     * @param optionId
     * @return
     */
    public TreeOptionItem getTreeOptionItem(Long optionId){
        if(optionId == null || optionId.longValue() == nullId)   // negative values are bogus and equivalent to null
            return null;

        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            TreeOptionItem option = (TreeOptionItem) iterator.next();
            if(optionId.equals(option.getValue())){
                return option;
            }
        }
        return null;
    }


    public void printTree(PrintWriter pw){
        Collection rootElements = getRootElements();
        String indent = "";
        for (Iterator iterator = rootElements.iterator(); iterator.hasNext();) {
            TreeOptionItem option = (TreeOptionItem) iterator.next();
            pw.println(option.toString());
            printChildren(pw,option,indent);
        }
    }

    private void printChildren(PrintWriter pw, TreeOptionItem parent, String indent){
        Collection children = parent.getChildren(); //getChildren(parent);
        indent = indent + "\t";
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            TreeOptionItem childOption = (TreeOptionItem) iterator.next();
            pw.println(indent + childOption.toString());
            printChildren(pw,childOption,indent);
        }
    }

    public static void main(String [] args){
        Collection options = new ArrayList();
        LocatorOption o;
        printMemory();

        long start = System.currentTimeMillis();

        o = new LocatorOption(new Long(1), "Building1", null);
        options.add(o);
        o = new LocatorOption(new Long(2), "Building2", null);
        options.add(o);
        o = new LocatorOption(new Long(3), "b2-Floor1", new Long(2));
        options.add(o);
        o = new LocatorOption(new Long(4), "b1-Floor1", new Long(1));
        options.add(o);
        o = new LocatorOption(new Long(6), "b1-Floor2", new Long(1));
        options.add(o);
        o = new LocatorOption(new Long(7), "b1-Floor3", new Long(1));
        options.add(o);
        o = new LocatorOption(new Long(5), "b1-f1_Room1", new Long(4));
        options.add(o);
        o = new LocatorOption(new Long(8), "b1-f3_Room1", new Long(7));
        options.add(o);


        for(int i = 0; i <10; i++){
            o = new LocatorOption(new Long(10+i), "b1-f3_Room"+(10+i), new Long(7));
            options.add(o);
        }

        FilterTree tree = new FilterTree(options);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tree.printTree(pw);
        System.out.println(sw.toString());

        FilterTree trim = tree.skim(new Long(7));
        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        trim.printTree(pw1);
        System.out.println(sw1.toString());



        System.out.println("getLevels");
        Collection col = tree.splitInLevels(new Long(7));
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            Collection tmp = (Collection) iterator.next();
            for (Iterator iterator2 = tmp.iterator(); iterator2.hasNext();) {
                TreeOptionItem option = (TreeOptionItem) iterator2.next();
                System.out.print(option.getValue() + "\t");
            }
            System.out.print("\n");
        }

        System.out.println("exec time: " + (System.currentTimeMillis()-start));

        printMemory();

    }

    private static void printMemory(){
        Runtime.getRuntime().gc();
        System.out.println("max   = " + Runtime.getRuntime().maxMemory()/1e6);
        System.out.println("free  = " +Runtime.getRuntime().freeMemory()/1e6);
        //System.out.println("total = " + Runtime.getRuntime().totalMemory()/1e6);
    }

}
