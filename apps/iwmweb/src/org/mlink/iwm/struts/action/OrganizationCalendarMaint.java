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
import org.mlink.iwm.bean.Worker;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.WorkersCriteria;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.lookup.DummyDropdown;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 22, 2007
 */
public class OrganizationCalendarMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(OrganizationCalendarMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long orgId = StringUtils.getLong(form.getId());
            PolicySF psf = ServiceLocator.getPolicySFLocal();
			Organization org = (Organization)psf.get(Organization.class, orgId);
			Party party = org.getParty();
            PageContext context = new PageContext(party.getName());
            context.add("organization", party.getName());
            form.setPageContext(context);

            request.setAttribute("WorkerRef",new DummyDropdown(prepareWorkers(orgId)));
        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }

    /**
     * Get all workers in organization   and pass along with request
     * It will be used by the forwarded page
     * @param orgId
     */
    private Collection prepareWorkers(Long orgId) throws Exception{
        WorkersCriteria cr= new WorkersCriteria(); cr.setId(orgId);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.WorkersDAO,cr,new PaginationRequest("name"));
        return  CollectionUtils.collect(response.convertRowsToClasses(org.mlink.iwm.bean.Worker.class), new Transformer(){
                public Object transform(Object input) {
                    Worker w = (Worker)input;
                    return new OptionItem(w.getId(),w.getName());
                }
        });
    }

}
