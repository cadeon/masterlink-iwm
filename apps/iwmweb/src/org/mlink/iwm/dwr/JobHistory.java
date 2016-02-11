package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.SearchCriteria;

/**
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */
public class JobHistory {

    /**
     * Will retrieve all worker even without time record     
     * @param criteria
     * @return
     * @throws Exception
     */
    public ResponsePage getData(HashMap criteria) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);  //jobId is expected
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.JobHistoryDAO,cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.WorkSchedule.class);
        return new ResponsePage(lst.size(),lst);
    }

}
