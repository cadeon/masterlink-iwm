package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.SystemProp;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: Raghavendra Kote
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class SystemProps implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(SystemProps.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.SystemPropsDAO, null,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.SystemProp.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long id) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        SystemProp systemProp = psf.get(SystemProp.class, id);
        psf.remove(systemProp);
        return rtn;
    }

    /**
     * Get worker for given id
     * @param id
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.SystemProp getItem(Long id) throws Exception{
    	org.mlink.iwm.bean.SystemProp form = new org.mlink.iwm.bean.SystemProp();
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        SystemProp systemProp = (SystemProp)psf.get(SystemProp.class, id);
        CopyUtils.copyProperties(form, systemProp);
		return form;
    }

    /**
     * Updates/Creates worker
     * @param map HashMap is analaog to SystemProp class
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
    	String rtn = ITEM_SAVED_OK_MSG;
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		SystemProp systemProp = new SystemProp();
		CopyUtils.copyProperties(systemProp, map);
        long id = systemProp.getId();
        
		if (id > 0) {
			psf.update(systemProp);
			Config.loadProperties();
		} else {
			
			id = psf.create(systemProp);
			systemProp.setId(id);
			if (id <= 0) {
				logger.error("SystemProp not created.");
			} else {
				logger.info("SystemProp created sucessfully id: "+id);
				Config.loadProperties();
			}
		}
		return rtn;
    }
    
}
