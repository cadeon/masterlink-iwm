package org.mlink.iwm.dwr;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.mlink.iwm.bean.Organization;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.OrganizationSchemaRef;

/**
 * User: andrei
 * Date: Nov 2, 2006
 */

public class OrganizationFilter implements FilterInt{
    private static OrganizationFilter ourInstance = new OrganizationFilter();
    public static OrganizationFilter getInstance() {
        return ourInstance;
    }
    private OrganizationFilter() {}

    /**
     * get Filter labels for each level as a List
     * @return List <OptionItem>
     */
    public Collection <OptionItem> getLabels() {
        OrganizationSchemaRef lsr = (OrganizationSchemaRef) LookupMgr.getInstance(OrganizationSchemaRef.class);
        return lsr.getOptions();
    }

    /**
     * Get all parents in the tree starting with the first parent given by the parameter firstParentId
     * The return collection is used to preselect options in previous htmls-selects  in the filter
     * @param firstParentId
     * @return OptionItem Collection of ancestors ordered by level (schemaId)
     * @throws Exception
     */
    public Collection getAncestors(Long firstParentId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.PARENTS);
        cr.setId(firstParentId);
        //PaginationResponse response = DaoFactory.get(DaoFactory.NAME.OrganizationsDAO).getData(cr,new PaginationRequest("schemaId"));
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.OrganizationsDAO,cr,new PaginationRequest("schemaId"));
        return transform(response.convertRowsToClasses(org.mlink.iwm.bean.Organization.class));
    }

    /**
     * Get first children for locator. The return collection is used to populate next html-select  in the filter
     * @param parentId
     * @return   OptionItem Collection of first generation descendants
     * @throws Exception
     */
    public Collection getDescendants(Long parentId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.FIRST_CHILDREN);
        cr.setId(parentId);
        //PaginationResponse response = DaoFactory.get(DaoFactory.NAME.OrganizationsDAO).getData(cr,new PaginationRequest("name"));
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.OrganizationsDAO,cr,new PaginationRequest("name"));
        return transform(response.convertRowsToClasses(org.mlink.iwm.bean.Organization.class));
    }

    public void setSelectedId(Long id) throws Exception {
        SessionUtil.setCurrentOrganization(id);
    }

    /**
     * Utility method to transdrom collection of Locator objects to Collection of OptionItem objects
     * @param lst Collection of Locator objects
     * @return Collection of OptionItem objects
     */
    private Collection  transform(Collection lst){
        return CollectionUtils.collect(lst, new Transformer(){
            public Object transform(Object input) {
                Organization loc = (Organization)input;
                return new OptionItem(loc.getId(),loc.getName());
            }
        });
    }
}
