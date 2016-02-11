package org.mlink.iwm.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class JobTaskActionsMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(JobTaskActionsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        BaseForm form  = (BaseForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        try {
            Long id = StringUtils.getLong(form.getTaskId());
            ControlSF csf = ServiceLocator.getControlSFLocal( );
            
            JobTask jtvo = csf.get(JobTask.class, id);
            Job jvo = jtvo.getJob();
            ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
            ObjectEntity ovo = isf.get(ObjectEntity.class, jvo.getObjectId());
            PageContext context = new PageContext(jvo.getDescription());
            context.add("jobId",String.valueOf(jvo.getId()));
            context.add("jobDescription",jvo.getDescription());
            context.add("object",ovo.getObjectRef());
            String classCode = TargetClassRef.getLabel(ovo.getClassId());
            context.add("class",classCode);
            String fullLocator = LocatorRef.getFullLocator(jvo.getLocatorId());
            context.add("locator",fullLocator);
            Task tvo = jtvo.getTask();
            context.add("taskDescription",tvo.getTaskDescription());
            context.add("totalHoursRequired",StringUtils.parseMinutes(tvo.getEstTime()));
            //context.add("totalTime",StringUtils.parseMinutes(jtvo.getTotalTime()));

            if(jvo.getOrganizationId()!=null){
            	Organization org = (Organization)csf.get(Organization.class, jvo.getOrganizationId());
    			context.add("organization",org.getParty().getName()); 
            }

            if(jvo.getProject()!=null){
                Project pvo = jvo.getProject();
                context.add("project", pvo.getName() + "(Id=" + pvo.getId() + ")");
            }

            form.setPageContext(context);
        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
