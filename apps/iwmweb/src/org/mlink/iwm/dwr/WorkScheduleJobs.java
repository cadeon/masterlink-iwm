package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;

/**
 * User: andrei
 * Date: Dec 21, 2006
 */
public class WorkScheduleJobs {

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.WorkScheduleJobsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Job.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

}
