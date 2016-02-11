package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class WorkerCalendarJobsMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkerCalendarJobsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long workScheduleId = StringUtils.getLong(request.getParameter("wsId"));
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            WorkSchedule wsVo = psf.get(WorkSchedule.class, workScheduleId);
            Person p = psf.get(Person.class, wsVo.getPersonId());
            Organization org = (Organization)psf.get(Organization.class, p.getOrganizationId());
            Party party = p.getParty();
			PageContext context = new PageContext(party.getName() + " " + ConvertUtils.formatDate(wsVo.getDay()));
            context.add("scheduledDate", ConvertUtils.formatDate(wsVo.getDay()));
            context.add("workerInfo", party.getName());
            context.add("organization", org.getParty().getName());
            form.setPageContext(context);

        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
