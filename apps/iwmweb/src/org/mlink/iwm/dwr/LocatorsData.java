package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.LocatorData;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 21, 2006
 */
public class LocatorsData implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        //PaginationResponse response = DaoFactory.get(DaoFactory.NAME.LocatorDataDAO).getData(cr,request);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.LocatorDataDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.LocatorData.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        psf.removeLocatorData(itemId);
        return rtn;
    }

    public org.mlink.iwm.bean.LocatorData getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.LocatorData form = new org.mlink.iwm.bean.LocatorData();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        LocatorData vo = psf.get(LocatorData.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setLocatorId(Long.toString(vo.getLocator().getId()));
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        LocatorData vo = new LocatorData();
        CopyUtils.copyProperties(vo,bean);
        vo.setLocator(psf.get(Locator.class, Long.parseLong((String)bean.get("locatorId"))));

        if(vo.getId() > 0){
            psf.update(vo);
        }else{
            psf.create(vo);
        }
        return null;
    }
}
