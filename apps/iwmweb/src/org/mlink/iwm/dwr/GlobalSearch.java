package org.mlink.iwm.dwr;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.*;
import java.util.List;
import java.util.HashMap;

/**
 * User: andrei
 * Date: Apr 17, 2007
 */
public class GlobalSearch {

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,"id",orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.GlobalSearchDAO,cr,request);
        List lst =  response.convertRowsToHtml();
        return new ResponsePage(response.getTotalCount(),lst);
    }
}
