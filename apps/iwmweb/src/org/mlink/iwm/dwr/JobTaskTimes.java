package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.SearchCriteria;

/**
 * User: andrei
 * Date: Dec 18, 2006
 */
public class JobTaskTimes {
    /**
     * Will retrieve  workers with time record  against the given jobTaskId
     * @param criteria
     * @return
     * @throws Exception
     */
    public ResponsePage getData(HashMap criteria) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria); //jobTaskId is expected
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.JobTaskTimesDAO,cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.WorkSchedule.class);
        return new ResponsePage(lst.size(),lst);
    }

}
