package org.mlink.iwm.dwr;

import org.mlink.iwm.lookup.LocatorSchemaRef;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.bean.Locator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import java.util.Collection;
import org.mlink.iwm.dao.*;

/**
 * User: andrei
 * Date: Oct 17, 2006
 */

public class LocatorFilter implements FilterInt{
    private static LocatorFilter ourInstance = new LocatorFilter();
    public static LocatorFilter getInstance() {
        return ourInstance;
    }
    public LocatorFilter() {}     //must be public since there is one call out of common framework usage. see EWRequest which was transferred from v3.0

    /**
     * get Filter labels for each level as a List
     * @return List <OptionItem>
     */
    public Collection <OptionItem> getLabels() {
        LocatorSchemaRef lsr = (LocatorSchemaRef) LookupMgr.getInstance(LocatorSchemaRef.class);
        return lsr.getOptions();
    }

    /**
     * Get all parents in the tree starting with the first parent given by the parameter firstParentLocatorId
     * The return collection is used to preselect options in previous htmls-selects  in the filter
     * @param firstParentLocatorId
     * @return OptionItem Collection of ancestors ordered by level (schemaId)
     * @throws Exception
     */
    public Collection getAncestors(Long firstParentLocatorId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.PARENTS);
        cr.setId(firstParentLocatorId);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.LocatorsDAO,cr,new PaginationRequest("schemaId"));
        return transform(response.convertRowsToClasses(org.mlink.iwm.bean.Locator.class));
    }

    /**
     * Get first children for locator. The return collection is used to populate next html-select  in the filter
     * @param parentLocatorId
     * @return   OptionItem Collection of first generation descendants
     * @throws Exception
     */
    public Collection getDescendants(Long parentLocatorId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.FIRST_CHILDREN);
        cr.setId(parentLocatorId);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.LocatorsDAO,cr,new PaginationRequest("name"));
        return transform(response.convertRowsToClasses(org.mlink.iwm.bean.Locator.class));
    }

    public void setSelectedId(Long id) throws Exception {
        SessionUtil.setCurrentLocator(id);
    }

    /**
     * Utility method to transdrom collection of Locator objects to Collection of OptionItem objects
     * @param lst Collection of Locator objects
     * @return Collection of OptionItem objects
     */
    private Collection  transform(Collection lst){
        return CollectionUtils.collect(lst, new Transformer(){
            public Object transform(Object input) {
                Locator loc = (Locator)input;
                return new OptionItem(loc.getLocatorId(),loc.getName());
            }
        });
    }
}
