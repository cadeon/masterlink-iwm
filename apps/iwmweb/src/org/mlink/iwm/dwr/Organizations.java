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
import org.mlink.iwm.entity3.Address;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OrganizationRef;
import org.mlink.iwm.lookup.OrganizationSchemaRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: John Mirick Note: throwing Exceptions outside of methods allows use of
 * DWR error handling (alerts in UI) Note DWR does not support method oveloading
 * well. Avoid that!
 */


public class Organizations implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(Organizations.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria,SearchCriteria.ResultCategory.CHILDREN);
        SessionUtil.setAttribute("OrganizationCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.OrganizationsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Organization.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long organizationId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        Organization org = psf.get(Organization.class, organizationId);
        psf.removeOrganization(org);
        LookupMgr.reloadCDLV(OrganizationRef.class);            
        return rtn;
    }

	public org.mlink.iwm.bean.Organization getItem(Long organizationId) throws Exception {
		org.mlink.iwm.bean.Organization form = new org.mlink.iwm.bean.Organization();
		if (organizationId == null) {
			logger.warn("Organization is requested with null id. OK, returning empty form");
		} else {
			PolicySF psf = ServiceLocator.getPolicySFLocal();
			Organization org = (Organization)psf.get(Organization.class, organizationId);
			Party party = org.getParty();
			CopyUtils.copyProperties(form, party.getAddress());
			CopyUtils.copyProperties(form, party);
			CopyUtils.copyProperties(form, org);
			form.setType(org.getOrganizationTypeId().toString());
			form.setPartyId(Long.toString(org.getParty().getId()));
			form.setAddressId(Long.toString(org.getParty().getAddress().getId()));
			form.setType(org.getOrganizationTypeId().toString());
			
			//Organization organization = (Organization)psf.getOrganizationByPartyName(org.getParty().getName());
		}
		return form;
	}

	/**
	 * 
	 * @param map
	 * @return Optional Business message
	 * @throws Exception
	 */
	public String saveItem(HashMap map) throws Exception {
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		String rtn = ITEM_SAVED_OK_MSG;
		long addressId = 0, partyId = 0, orgId;
		
		Address address = new Address();
		CopyUtils.copyProperties(address, map);
		
		Party party = new Party();
		CopyUtils.copyProperties(party, map);
		party.setAddress(address);
		
		Organization org = new Organization();
		CopyUtils.copyProperties(org, map);
		psf.validateOrganization(org);
		org.setOrganizationTypeId(Integer.parseInt((String)map.get("type")));
		org.setParty(party);
		
		String addressIdStr = (String)map.get("addressId");
		if(addressIdStr != null){
			addressId = Long.parseLong(addressIdStr);
			address.setId(addressId);
		}
		
		String partyIdStr = (String)map.get("partyId");
		if(partyIdStr != null){
			partyId = Long.parseLong(partyIdStr);
			party.setId(partyId);
		}
		
		orgId = org.getId();
		
		if (orgId > 0) {
			psf.update(org);
		} else {
			address.setId(0);
			party.setId(0);
			org.setId(0);
			
			orgId = psf.create(org);
			party = org.getParty();
			psf.update(party);
			psf.update(org);
			if (orgId <= 0) {
				logger.error("Organization not created.");
			} else {
				logger.info("Organization created sucessfully Id: "+orgId);
				LookupMgr.reloadCDLV(OrganizationRef.class);
			}
		}
		return rtn;
	}
}
