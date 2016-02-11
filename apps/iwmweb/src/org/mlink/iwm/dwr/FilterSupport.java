package org.mlink.iwm.dwr;

import org.mlink.iwm.lookup.OptionItem;
import java.util.Collection;

/**
 * User: andrei
 * Date: Oct 17, 2006
 */
public class FilterSupport  {

    public  Collection<OptionItem> getLabels(String filterType) throws Exception{
        return getFilter(filterType).getLabels();
    }

    public  Collection getAncestors(String filterType,Long firstParentId) throws Exception{
        return getFilter(filterType).getAncestors(firstParentId);
    }

    public  Collection getDescendants(String filterType,Long parentId) throws Exception{
        return getFilter(filterType).getDescendants(parentId);
    }

    public void setSelectedId(String filterType,Long id) throws Exception{
        getFilter(filterType).setSelectedId(id);
    }


    private FilterInt getFilter(String filterType){
        if("LOCATOR_FILTER".equals(filterType)){
            return LocatorFilter.getInstance();
        }else if("OBJECT_CLASS_FILTER".equals(filterType)){
            return ObjectClassFilter.getInstance();
        }else if("ORGANIZATION_FILTER".equals(filterType)){
            return OrganizationFilter.getInstance();
        }else{
            throw new RuntimeException("Unsupported filter type "+ filterType);
        }

    }

}
