package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TimeSpecsCriteria;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 5, 2007
 */
public class ScheduledJobs {
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TimeSpecsCriteria cr = new TimeSpecsCriteria(criteria);
        SessionUtil.setAttribute("TimeSpecsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ScheduledJobsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ScheduledJob.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }
}
