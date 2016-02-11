/**
 * Created by IntelliJ IDEA.
 * @author Andrei Povodyrev
 * Date: May 16, 2004
 */
package org.mlink.iwm.filter;

import org.mlink.iwm.lookup.*;
import org.mlink.iwm.util.Config;
import org.apache.log4j.Logger;



import java.util.*;

public class LocatorTree {
    private static final Logger logger = Logger.getLogger(LocatorTree.class);

    private static LocatorTree ourInstance;
    private LocatorRef lRef;

    public synchronized static LocatorTree getInstance() {
        if (ourInstance == null) {
            ourInstance = new LocatorTree();
        }
        return ourInstance;
    }

    private LocatorTree() {load();}

    private FilterTree tree;

    public LocatorRef getlRef() {
        return lRef;
    }

    public void load(){
        lRef = new LocatorRef();
        try{
            Collection results = CDLVLoader.getInstance().load(lRef);
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Map map = (Map) iterator.next();
                lRef.addOption(map);
            }

            //log(lRef.toString());
            log("LocatorTree loaded");

        }catch(Throwable e){
            e.printStackTrace();
            log("could not load lookup values for " + lRef.getClass() + " Exception: " + e.getMessage());
            log("sql select statement failed to execute " + lRef.getSql());
        }

        tree = new FilterTree(lRef.getOptions());
    }




    /**
     * Get all locators for given schema
     * @param schemaId
     * @return FilterLevel
     */
    public FilterLevel getSchemaFilterLevel(Integer schemaId){
        Collection locators = tree.getElements();
        Collection selected = new ArrayList();
        for (Iterator iterator = locators.iterator(); iterator.hasNext();) {
            LocatorOptionItem option = (LocatorOptionItem) iterator.next();
            if(schemaId.equals(option.getSchemaId())){
                selected.add(option);
            }
        }
        //FilterLevel level = new FilterLevel(LocatorSchemaRef.getLabel(schemaId));
        FilterLevel level = new FilterLevel();
        level.setOptions(selected);
        return level;
    }

    /**
     * Return assignment level parent for given nodeId
     * if noo parent found, return itself
     * Assignment level is the LOCATION level also called magic location
     * @param nodeId
     * @return  TreeOptionItem
     */
    public TreeOptionItem getAssignmentLevelParent(Long nodeId){
        Integer theLocationId = new Integer(Config.getProperty(Config.LOCATOR_SCHEMA_TOP,"-1"));
        TreeOptionItem parent = tree.getTreeOptionItem(nodeId);
        while(parent.getParent()!= null && !theLocationId.equals(parent.getSchemaId())){
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Get filter levels,  Each filter level represents a colletion of locators for a filter schema (Deck, Compartment, etc)
     * @param selectedId   active locatorId in the stack of filter levels
     * @return Collection of FilterLevel objects. The size of collection is defined by the size of Locator schema
     */
    public List getFilterLevels(Long selectedId){
        //first get the number of levels in the schema
        LocatorSchemaRef locatorSchema = LocatorSchemaRef.getInstance();
        List schemaLevels = locatorSchema.getOptions();
        List levels = new ArrayList(schemaLevels.size());

        // init tree levels
        for (int i = 0; i < schemaLevels.size(); i++) {
            OptionItem option = (OptionItem) schemaLevels.get(i);
            FilterLevel level = new FilterLevel(locatorSchema.getLabel(option.getValue().toString()));
            if(i==0) {//root level
                level.setOptions(tree.getRootElements());
            }
            levels.add(i, level);
        }

        // populate filter levels based on the selected node id (locatorid)
        LocatorOptionItem selected = (LocatorOptionItem)tree.getTreeOptionItem(selectedId);
        if(selected !=null){
            int childrenSchemaId = selected.getSchemaId().intValue() + 1;
            FilterLevel childrenLevel = new FilterLevel(LocatorSchemaRef.getLabel(new Integer(childrenSchemaId)));
            childrenLevel.setOptions(selected.getChildren());
            substituteLevel(levels,childrenLevel);

            FilterLevel selectedLevel = new FilterLevel(LocatorSchemaRef.getLabel(selected.getSchemaId()));
            selectedLevel.setSelectedId(selected.getValue());
            selectedLevel.setOptions(selected.getPeers());
            substituteLevel(levels,selectedLevel);


            LocatorOptionItem parent = (LocatorOptionItem)selected.getParent();
            while(parent != null){
                FilterLevel parentLevel = new FilterLevel(LocatorSchemaRef.getLabel(parent.getSchemaId()));
                parentLevel.setSelectedId(parent.getValue());
                parentLevel.setOptions(parent.getPeers());
                substituteLevel(levels,parentLevel);
                parent = (LocatorOptionItem)parent.getParent();
            }
        }
        return levels;
    }

    private void substituteLevel(List levels, FilterLevel level){
        for (int i = 0; i < levels.size(); i++) {
            FilterLevel blankLevel = (FilterLevel) levels.get(i);
            if(blankLevel.getSchemaName().equals(level.getSchemaName())){
                levels.set(i,level);
            }
        }
    }



    public Collection getFamily(Long nodeId){
        return tree.getFamily(nodeId);
    }

    /**
     * Get all child  ids for the given element id(Long)
     * @param nodeId
     * @return  Collection
     */
    public Collection getChildIds(Long nodeId){
        Collection elements;
        if(nodeId==null || nodeId.longValue() < 0){
            elements = tree.getRootElements();
        }else{
            elements = tree.getChildren(tree.getTreeOptionItem(nodeId));
        }

        Collection ids = new ArrayList();
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            TreeOptionItem item = (TreeOptionItem) iterator.next();
            ids.add(item.getValue());
        }
        return ids;
    }

    /**
     * Get First level children for the given element id(Long)
     * @param nodeId
     * @return  Collection
     */
    public Collection getFirstChildren(Long nodeId){
        Collection elements;
        if(nodeId==null || nodeId < 0){
            elements = tree.getRootElements();
        }else{
            elements = tree.getFirstChildren(tree.getTreeOptionItem(nodeId));
        }
        return elements;
    }


    private void log(String str){
        logger.debug(str);
    }






}

