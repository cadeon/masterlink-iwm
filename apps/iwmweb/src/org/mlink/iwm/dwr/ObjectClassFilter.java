/**
 * User: andrei
 * Date: Oct 17, 2006
 * Time: 5:02:28 PM
 * To change this template use File | Settings | File Templates.
 */
package org.mlink.iwm.dwr;

import java.util.Collection;

import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.ObjectClassSchemaRef;
import org.mlink.iwm.lookup.OptionItem;

public class ObjectClassFilter implements FilterInt{
    private static ObjectClassFilter ourInstance = new ObjectClassFilter();

    public static ObjectClassFilter getInstance() {
        return ourInstance;
    }

    private ObjectClassFilter() {
    }


    /**
     * get Filter labels for each level as a List
     * @return List <OptionItem>
     */
    public Collection<OptionItem> getLabels() {
        ObjectClassSchemaRef lsr = (ObjectClassSchemaRef) LookupMgr.getInstance(ObjectClassSchemaRef.class);
        return lsr.getOptions();
    }

    /**
     * Get all parents in the tree starting with the first parent given by the parameter parentClassId
     * The return collection is used to preselect options in previous htmls-selects  in the filter
     * @param parentClassId
     * @return OptionItem Collection of ancestors ordered by level (schemaId)
     * @throws Exception
     */
    public Collection getAncestors(Long parentClassId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.PARENTS);
        cr.setId(parentClassId);
        //PaginationResponse response = DaoFactory.get(DaoFactory.NAME.ObjectClassDAO).getData(cr,new PaginationRequest("schemaId"));
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectClassDAO,cr,new PaginationRequest("schemaId"));
        return response.convertRowsToClasses(OptionItem.class);
    }

    /**
     * Get first children for Class. The return collection is used to populate next html-select  in the filter
     * @param parentClassId
     * @return   OptionItem Collection of first generation descendants
     * @throws Exception
     */
    public Collection getDescendants(Long parentClassId) throws Exception{
        SearchCriteria cr = new SearchCriteria(SearchCriteria.ResultCategory.FIRST_CHILDREN);
        cr.setId(parentClassId);
        //PaginationResponse response = DaoFactory.get(DaoFactory.NAME.ObjectClassDAO).getData(cr,new PaginationRequest("code"));
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectClassDAO,cr,new PaginationRequest("code"));
        return response.convertRowsToClasses(OptionItem.class);
    }

    public void setSelectedId(Long id) throws Exception {
        SessionUtil.setCurrentClass(id);
    }
}

