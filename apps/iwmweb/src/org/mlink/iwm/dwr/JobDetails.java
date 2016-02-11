package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TasksCriteria;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public class JobDetails implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(JobDetails.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TasksCriteria cr = new TasksCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobTasksDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.JobTask.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        // jobTasks are never deleted
        return null;
    }

    public org.mlink.iwm.bean.JobTask getItem(Long jobTaskId) throws Exception {
        // there is no edit on JobTask direct properties
        return null;
    }

    public String saveItem(HashMap bean) throws Exception {
        // there is no save on JobTask direct properties
        return null;
    }
}
