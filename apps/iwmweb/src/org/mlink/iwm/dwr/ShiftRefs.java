package org.mlink.iwm.dwr;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.ShiftRef;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: Raghavendra Kote
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class ShiftRefs implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(ShiftRefs.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ShiftRefsDAO, null,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ShiftRef.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long shiftId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        ShiftRef shiftRef = psf.get(ShiftRef.class, shiftId);
        //they cannot archive if there is a ws with that shift in IP and when u archive it 
        //archive schedules-future and current-NYS
        
        Collection<WorkSchedule> wss = psf.getActiveWSByShift(new Long(shiftId).intValue());
        boolean canArchive=true;
        if(wss!=null && !wss.isEmpty()){
        	for(WorkSchedule ws: wss){
        		if(WorkScheduleStatusRef.Status.IP.toString().equals(
        				WorkScheduleStatusRef.getCode(ws.getStatusId()))){
        			canArchive = false;
        			break;
        		}
        	}
        }
        if(canArchive){
        	//archive NYA work schedules
        	if(wss!=null && !wss.isEmpty()){
        		Timestamp archivedDate = new java.sql.Timestamp(System.currentTimeMillis());
            	for(WorkSchedule ws: wss){
            		ws.setStatusId(WorkScheduleStatusRef.getIdByCode(WorkScheduleStatusRef.Status.DUN));
            		ws.setArchivedDate(archivedDate);
            		psf.update(ws);
            	}
        	}
        	
        	shiftRef.setArchivedDate(new java.sql.Timestamp(System.currentTimeMillis()));
        	psf.update(shiftRef);
			LookupMgr.reloadCDLV(org.mlink.iwm.lookup.ShiftRef.class);
        }else{
        	rtn = "Delete failed due to presence of InProgress WorkSchedules in this shift: "+shiftId;
        }
        return rtn;
    }

    /**
     * Get worker for given id
     * @param id
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.ShiftRef getItem(Long id) throws Exception{
    	org.mlink.iwm.bean.ShiftRef form = new org.mlink.iwm.bean.ShiftRef();
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        ShiftRef shiftRef = (ShiftRef)psf.get(ShiftRef.class, id);
        CopyUtils.copyProperties(form, shiftRef);
		return form;
    }

    /**
     * Updates/Creates worker
     * @param map HashMap is analaog to ShiftRef class
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
    	String rtn = ITEM_SAVED_OK_MSG;
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		ShiftRef shiftRef = new ShiftRef();
		CopyUtils.copyProperties(shiftRef, map);
        long id = shiftRef.getId();
        
		if (id > 0) {
			psf.update(shiftRef);
			LookupMgr.reloadCDLV(org.mlink.iwm.lookup.ShiftRef.class);
		} else {
			shiftRef.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
			id = psf.create(shiftRef);
			shiftRef.setId(id);
			if (id <= 0) {
				logger.error("ShiftRef not created.");
			} else {
				logger.info("ShiftRef created sucessfully id: "+id);
				LookupMgr.reloadCDLV(org.mlink.iwm.lookup.ShiftRef.class);
			}
		}
		return rtn;
    }
    
}
