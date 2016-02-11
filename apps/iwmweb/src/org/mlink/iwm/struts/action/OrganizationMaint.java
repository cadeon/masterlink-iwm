package org.mlink.iwm.struts.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Address;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.session.PolicySFRemote;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;

/**
 * User: andrei Date: Oct 12, 2006
 */
public class OrganizationMaint extends BaseAction {
	private static final Logger logger = Logger
			.getLogger(OrganizationMaint.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
/*
		PolicyRemote policySF = ServiceLocator3.getPolicySF3();

		Address2 addr = new Address2("address1", "address2", "city", "state", "zip");
		long addrId = policySF.create(Address2.class, addr);
		addr.setId(addrId);
		if (addrId < 0) {
			logger.error("Address not created.");
		} else {
			Party2 prt = new Party2("orgName", "email", "url", "fax", "phone",	null, addrId);
			long prtId = policySF.create(Party2.class, prt);
			prt.setId(prtId);
			if (prtId < 0) {
				logger.error("Party not created.");
			} else {
				Organization2 org = new Organization2(1134, null, 1L, null, prtId);
				long orgId = policySF.create(Organization2.class, org);
				org.setId(orgId);
				if (orgId < 0) {
					logger.error("Organization not created.");
				}
				
				Organization2 org2 = (Organization2) policySF.getEntity(Organization2.class, orgId);
				if (!org.equals(org2)) {
					throw new WebException();
				}

				Party2 prt2 = (Party2) policySF.getEntity(Party2.class, prtId);
				if (!prt.equals(prt2)) {
					throw new WebException();
				}

				Address2 addr2 = (Address2) policySF.getEntity(Address2.class, addrId);
				if (!addr.equals(addr2)) {
					throw new WebException();
				}

				policySF.remove(org2);
				Organization2 address3 = (Organization2) policySF.getEntity(Organization2.class, orgId);
				if (address3 != null) {
					throw new WebException();
				}
				
				policySF.remove(prt);
				Party2 prt3 = (Party2) policySF.getEntity(Party2.class, prtId);
				if (prt3 != null) {
					throw new WebException();
				}
				
				policySF.remove(addr);
				Address2 addr3 = (Address2) policySF.getEntity(Address2.class, addrId);
				if (addr3 != null) {
					throw new WebException();
				}
			}
		}
*/
		BaseForm form = (BaseForm) aform;
		String forward = form.getForward() == null ? "read" : form.getForward();
		logger.debug("execute " + forward);
		PageContext context = new PageContext();
		form.setPageContext(context);
		return findForward(mapping, request, forward);
	}
}
