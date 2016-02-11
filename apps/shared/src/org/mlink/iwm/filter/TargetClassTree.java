/**
 * Created by IntelliJ IDEA.
 * @author Andrei Povodyrev
 * Date: May 16, 2004
 */
package org.mlink.iwm.filter;

import org.mlink.iwm.lookup.*;
import org.apache.log4j.Logger;

import java.util.*;

public class TargetClassTree {
    private static final Logger logger = Logger.getLogger(TargetClassTree.class);
    private static TargetClassRef ref;
    private static TargetClassTree ourInstance;

    public synchronized static TargetClassTree getInstance() {
        if (ourInstance == null) {
            ourInstance = new TargetClassTree();
        }
        return ourInstance;
    }

    public static TargetClassRef getTargetClassRef(){
        return ref;
    }
    private TargetClassTree() {load();}

    private FilterTree tree;

    public void load(){
        ref = new TargetClassRef();
        try{
            //List results = DBAccess.execute(cdlv.getSql());
            Collection results = CDLVLoader.getInstance().load(ref);
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Map map = (Map) iterator.next();
                ref.addOption(map);
            }

            //log(ref.toString());
            log("TargetClassTree loaded");
        }catch(Throwable e){
            e.printStackTrace();
            log("could not load lookup values for " + ref.getClass() + " Exception: " + e.getMessage());
            log("sql select statement failed to execute " + ref.getSql());
        }

        tree = new FilterTree(ref.getOptions());
    }

    /**
     * Get filter levels,  Each filter level represents a colletion of locators for a filter schema
     * @param selectedId   active locatorId in the stack of filter levels
     * @return Collection of FilterLevel objects. The size of collection is defined by the size of Object Class schema
     */
    public List getFilterLevels(Long selectedId){
        //first get the number of levels in the schema
        ObjectClassSchemaRef classSchema = ObjectClassSchemaRef.getInstance();
        List schemaLevels = classSchema.getOptions();
        List levels = new ArrayList(schemaLevels.size());

        // init tree levels
        for (int i = 0; i < schemaLevels.size(); i++) {
            OptionItem option = (OptionItem) schemaLevels.get(i);
            FilterLevel level = new FilterLevel(classSchema.getLabel(option.getValue().toString()));
            if(i==0) {//root level
                level.setOptions(tree.getRootElements());
            }
            levels.add(i, level);
        }

        // populate filter levels based on the selected node id (locatorid)
        TreeOptionItem selected = tree.getTreeOptionItem(selectedId);
        if(selected !=null){
            int childrenSchemaId = selected.getSchemaId().intValue() + 1;
            FilterLevel childrenLevel = new FilterLevel(ObjectClassSchemaRef.getLabel(new Integer(childrenSchemaId)));
            childrenLevel.setOptions(selected.getChildren());
            substituteLevel(levels,childrenLevel);

            FilterLevel selectedLevel = new FilterLevel(ObjectClassSchemaRef.getLabel(selected.getSchemaId()));
            selectedLevel.setSelectedId(selected.getValue());
            selectedLevel.setOptions(selected.getPeers());
            substituteLevel(levels,selectedLevel);


            TreeOptionItem parent = selected.getParent();
            while(parent != null){
                FilterLevel parentLevel = new FilterLevel(ObjectClassSchemaRef.getLabel(parent.getSchemaId()));
                parentLevel.setSelectedId(parent.getValue());
                parentLevel.setOptions(parent.getPeers());
                substituteLevel(levels,parentLevel);
                parent = parent.getParent();
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

    /**
     * Get all parent Ids for the given element id(Long)
     * @param nodeId
     * @return
     */
    public Collection getParentIds(Long nodeId){
        Collection parents = tree.getParents(tree.getTreeOptionItem(nodeId));
        Collection ids = new ArrayList();
        for (Iterator iterator = parents.iterator(); iterator.hasNext();) {
            TreeOptionItem item = (TreeOptionItem) iterator.next();
            ids.add(item.getValue());
        }
        return ids;
    }
    /**
     * Get all child  ids for the given element id(Long)
     * @param nodeId
     * @return
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


    private void log(String str){
        logger.debug(str);
    }

    /**
     * Get all parent Ids (incuding self) for the given element id(Long). Parent order starts from the top
     * @param nodeId
     * @return
     */
    public Collection getFamily(Long nodeId){
        return tree.getFamily(nodeId);
    }






}

