package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public class TenantRequests implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TenantRequestsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TenantRequest.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        //NA
        return null;
    }

    public Object getNewItem() throws Exception {
        TenantRequest request = new TenantRequest();
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest req = ctx.getHttpServletRequest();
        PolicySF psf = ServiceLocator.getPolicySFLocal( );

        String userStr = req.getUserPrincipal().getName();
        if(userStr!=null){
        	User user = psf.getUserByName(userStr);
        	Person uvo = user.getPerson();
            if(uvo !=null){     // supeuser does not have a person record	
	            Party party = uvo.getParty();
	            request.setTenantName(party.getName());
	            request.setTenantPhone(party.getPhone());
	            request.setTenantEmail(party.getEmail());
	        }else {
	            request.setTenantName(userStr);
	        }
        }
        return request;
    }

    public org.mlink.iwm.bean.TenantRequest getItem(Long jobId) throws Exception {
    	org.mlink.iwm.bean.TenantRequest request = new org.mlink.iwm.bean.TenantRequest();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        TenantRequest tr = csf.getTenantRequestByJob(jobId);
        BeanUtils.copyProperties(request,tr);
        return request;
    }

    public String saveItem(HashMap bean) throws Exception {
    	TenantRequest vo = new TenantRequest();
        CopyUtils.copyProperties(vo,bean);
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        vo.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
        Long tenantRequestId = csf.createTenantRequest(vo);
        Job job = csf.getJobByTenantRequest(tenantRequestId);
        StringBuilder rtn = new StringBuilder();
        rtn.append("Your request has been submitted and job is scheduled\n");
        rtn.append("You may track the job status by using Job Tracker. Job id = " +job.getId());
        return rtn.toString();
    }
}
