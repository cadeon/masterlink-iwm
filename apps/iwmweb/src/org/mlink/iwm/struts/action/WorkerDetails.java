package org.mlink.iwm.struts.action;

import java.io.File;
import java.text.DateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectClassification;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.struts.form.WorkerDetailsForm;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.StringUtils;


/**
 * User: jmirick
 * Email: john@mirick.us
 * Date: Oct 25, 2006
 */
public class WorkerDetails  extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkerDetails.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        WorkerDetailsForm form  = (WorkerDetailsForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long personId = StringUtils.getLong(form.getId());
            PolicySF psf = ServiceLocator.getPolicySFLocal();
            Person vo = psf.get(Person.class, personId);
            //Set title
            PageContext context = new PageContext(vo.getParty().getName());
            form.setPageContext(context);
            //Set Worker info
            form.setPersonId(personId.toString());
            form.setPersonName(vo.getParty().getName());
            form.setEmail(vo.getParty().getEmail());
            form.setPhone(vo.getParty().getPhone());
            if (vo.getActive() == 0){
            	form.setActive("No");
            } else {
            	form.setActive("Yes");
            }
            
            form.setTitle(vo.getTitle());
            form.setOrganizationId(vo.getOrganizationId().toString());
            
            if (vo.getOrganizationId() != null && vo.getOrganizationId() > 0L){
            	Organization wkrOrg = psf.get(Organization.class, vo.getOrganizationId());
            	form.setOrganization(wkrOrg.getFullOrganization());
            	} else {
            		form.setOrganization("Worker not in Org");
            	}

        } catch (Exception e) {
            throw new WebException(e);
        }
        return findForward(mapping,request,forward);
    }
}
