package org.mlink.iwm.dwr;

import org.mlink.iwm.lookup.OptionItem;

import java.util.Collection;

/**
 * User: andrei
 * Date: Oct 17, 2006
 */
public interface FilterInt {

    /**
     * get Filter labels for each level as a List
     * @return List <OptionItem>
     */
    public  Collection<OptionItem> getLabels() throws Exception;

    /**
     * Get all parents in the tree starting with the first parent given by the parameter firstParentId
     * The return collection is used to preselect options in previous htmls-selects  in the filter
     * @param firstParentId
     * @return OptionItem Collection of ancestors ordered by level (schemaId)     todo: use generics
     * @throws Exception
     */
    public  Collection getAncestors(Long firstParentId) throws Exception;

    /**
     * Get first children for locator. The return collection is used to populate next html-select  in the filter
     * @param parentId
     * @return   OptionItem Collection of first generation descendants  todo: use generics
     * @throws Exception
     */
    public  Collection getDescendants(Long parentId) throws Exception;


    public  void setSelectedId(Long id) throws Exception;

}
