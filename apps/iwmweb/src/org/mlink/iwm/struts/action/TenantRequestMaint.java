package org.mlink.iwm.struts.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.lookup.DummyDropdown;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.MaintRequestForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei Date: Nov 21, 2006
 */
public class TenantRequestMaint extends BaseAction {

	private static final Logger logger = Logger
			.getLogger(TenantRequestMaint.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		MaintRequestForm mform = (MaintRequestForm) aform;
		BaseForm form = (BaseForm) aform;
		String forward = form.getForward() == null ? "read" : form.getForward();
		logger.debug("execute " + forward);
		// lets play with parameters -Chris
		if ("submit".equals(forward)) {
			try {
				processSubmit(mapping, mform, request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			PageContext context = new PageContext();
			form.setPageContext(context);
			try {
				PolicySF psf = ServiceLocator.getPolicySFLocal( );
				Collection tasks = psf
						.getTaskDefinitions(StringUtils.getLong(Config
								.getProperty(Config.AREA_OBJECT_DEFINITION_ID)));
				put(request, "ProblemSelector", new DummyDropdown(
						transform(tasks)));
			} catch (Exception e) {
				throw new WebException(e);
			}
		}
		return findForward(mapping, request, forward);
	}

	private Collection transform(Collection lst) {
		return CollectionUtils.collect(lst, new Transformer() {
			public Object transform(Object input) {
				TaskDefinition vo = (TaskDefinition) input;
				return new OptionItem(vo.getId(), vo.getTaskDescription());
			}
		});
	}

	private void processSubmit(ActionMapping mapping, MaintRequestForm mform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException, Exception {
		TenantRequest vo = new TenantRequest();
		// check for user info, if it's not there, fill it in.
		// look at me, I'm not doing that check.
			PolicySF psf = ServiceLocator.getPolicySFLocal( );
			String userName = request.getUserPrincipal().getName();
			Person p = psf.getPersonByName(userName);
			Party party = p.getParty();
			mform.setTenantName(party.getName());
			mform.setTenantPhone(party.getPhone());
			mform.setTenantEmail(party.getEmail());
		try {
			CopyUtils.copyProperties(vo, mform);
			ControlSF csf = ServiceLocator.getControlSFLocal( );
			vo.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
			Long tenantRequestId = csf.create(vo);
			Job job = csf.getJobByTenantRequest(tenantRequestId);
			mform.setMessage("Request " + job.getId() + " has been submitted");
            mform.setJobId(String.valueOf(job.getId()));
			
		} catch (Exception e) {
			throw new WebException(e);
		}
	}
}
