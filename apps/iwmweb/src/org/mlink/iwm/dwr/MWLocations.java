package org.mlink.iwm.dwr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.MWJobsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.ConvertUtils;

/**
 * User: andrei
 * Date: Dec 14, 2006
 */
public class MWLocations implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(MWLocations.class);

    /**
     * May choose to implement this method using ListDAOTemplate pattern as opposite to EJB call
     */
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        MWJobsCriteria cr = new MWJobsCriteria(criteria);
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        
        if(cr.getScheduledDate()==null){
    		cr.setScheduledDate(ConvertUtils.formatDate(new Date()));
        }
        
        SessionUtil.setAttribute("MWJobsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.MWLocationsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.MWLocation.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * This one CLOSES the job (not delete as method name enforced by interface suggests)
     * @param jobId
     * @return
     * @throws Exception
     */
    public String deleteItem(Long jobId) throws Exception {
        return null;
    }

    public Object getItem(Long itemId) throws Exception {
        return null;
    }

    public String saveItem(HashMap bean) throws Exception {
        return null;
    }
}

