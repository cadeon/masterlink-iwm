package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Sep 18, 2006
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */
public class Locators implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(Locators.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria,SearchCriteria.ResultCategory.CHILDREN);
        SessionUtil.setAttribute("LocatorsCriteria",cr);    //store in session, reuse in later when coming back
        return getData(cr,offset,pageSize,orderBy,orderDirection);
    }

    private ResponsePage getData(SearchCriteria cr, int offset, int pageSize, String orderBy, String orderDirection) throws Exception{
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.LocatorsDAO,cr,request);
        //System.out.println("Total Count=" + response.getTotalCount());
        //List rows = response.getRows();
        /*for (Object row : rows) {
            HashMap map = (HashMap) row;
            System.out.println(map);
        }*/

        List<org.mlink.iwm.bean.Locator> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Locator.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long locatorId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            psf.removeLocator(locatorId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public org.mlink.iwm.bean.Locator getItem(Long locatorId) throws Exception{
    	org.mlink.iwm.bean.Locator form = new org.mlink.iwm.bean.Locator();
        if(locatorId==null){
            //this option supports LocatorEdit popup when parent locator is set to null, ie top level//
            logger.warn("Locator is requested with null id. OK, returning empty form");
        }else{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            
            Locator vo = psf.get(Locator.class, locatorId);
            CopyUtils.copyProperties(form,vo);
            
        }
        return form;
    }

    /**
     *
     * @param map
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        Locator vo = new Locator();
        CopyUtils.copyProperties(vo,map);
        psf.validateLocator(vo); 
        String rtn = ITEM_SAVED_OK_MSG;
        if(vo.getId() > 0){
        	if(vo.getParentId()==null || CopyUtils.isNullAlias(vo.getParentId())){
            	vo.setTopParentId(vo.getId());
            }else{
            	vo.setTopParentId(Long.parseLong((String)map.get("parentLocatorFilter_0")));
            }
        	psf.update(vo);
		}else{
			vo.setSecurityLevel(new Long(174));
		    psf.create(vo);
		    
		    if(vo.getParentId()==null || CopyUtils.isNullAlias(vo.getParentId())){
	        	vo.setTopParentId(vo.getId());
		    }else{
            	vo.setTopParentId(Long.parseLong((String)map.get("parentLocatorFilter_0")));
            }
		    psf.update(vo);
		}
        LookupMgr.reloadCDLV(LocatorRef.class);
        return rtn;
    }

}
