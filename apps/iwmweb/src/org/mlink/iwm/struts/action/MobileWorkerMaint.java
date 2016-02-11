package org.mlink.iwm.struts.action;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.BaseAccess;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.lookup.MWAccessTypeRef;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.form.MobileWorkerMaintForm;
import org.mlink.iwm.struts.form.PageContext;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.DateUtil;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class MobileWorkerMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(MobileWorkerMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        MobileWorkerMaintForm form  = (MobileWorkerMaintForm)aform;
        String forward = form.getForward()==null?"read":form.getForward();
        logger.debug("execute " + forward);
        Long personId = StringUtils.getLong(form.getWorkerId());
        String statusStr = null;
        
        if(personId==null){
            //try session
            personId=(Long)request.getSession().getAttribute("personId");
        }
        try {
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            String username;
            Person person = null;
            if(personId!=null) {
            	person = psf.get(Person.class, personId);
                username= person.getUsername();
                request.getSession().setAttribute("personId",personId);
            }else{
                username = request.getUserPrincipal().getName();
                User user;
                user = psf.getUserByName(username);
                person = user.getPerson();
            }
            if(username== null){
                throw new WebException("invalid user name!");
            }

            Date dt=null;
            String scheduledDate=null;
            ControlSF csf = ServiceLocator.getControlSFLocal( );
            
            String dateStr = request.getParameter("scheduledDate");
            boolean noDate = false;
            if(dateStr!=null && !"".equals(dateStr.trim())){
            }else{
            	dateStr = form.getScheduledDate();
            }
            
            if(dateStr!=null && !"".equals(dateStr.trim())){
            	try{
                dt = ConvertUtils.string2Date(dateStr);
            	}catch(ParseException pe){
            		logger.error("ScheduledDate: "+dateStr+" for User: "+username+" is in correct. Retrieving earliest active scheduled Date instead.");
            		noDate = true;
            	}
            }else{
            	noDate = true;
            }
            
            if(noDate){
                dt = new java.sql.Timestamp(System.currentTimeMillis());
            }
            scheduledDate = dt!=null? DateUtil.displayShortDate(dt):"None";
            java.sql.Date sqlDt = new java.sql.Date(dt.getTime());
            Date now = new java.sql.Timestamp(System.currentTimeMillis());
        	
            if("read".equals(forward)){
                if (personId==null) {
                    form.setMessage(StringUtils.javascriptSafeString(
                            "User "+ username +" is not associated with "+
                                    "an existing person in the system"));
                    return findForward(mapping,request,"message");
                }
                
            	// Log MW access
                csf.logMWAccess(person.getUsername(), scheduledDate, MWAccessTypeRef.Type.UIA, null, 0d, 0d, 0);
                
                String shiftTimeKeeping = Config.getProperty(Config.SHIFT_TIMEKEEPING, "false");
                if("false".equals(shiftTimeKeeping) || shiftTimeKeeping == null){
                	//assume its start shift and csf.startShift 
                	forward = "startshift";
                }
            }
            
            if("startshift".equals(forward)){
                //mark schedules IP
            	long workerStatus = csf.startShift(person, scheduledDate, now);
            	statusStr = WorkScheduleStatusRef.getLabel((int)workerStatus);//Status.IP.toString();
                forward = "read";
            }else if("endshift".equals(forward)){
            	csf.endShift(person, scheduledDate, now); 
                statusStr = WorkScheduleStatusRef.Status.DUN.toString();
            }else if("startbreak".equals(forward)){
                //mark schedules break
            	csf.startBreak(person, scheduledDate, now);
            	statusStr = WorkScheduleStatusRef.Status.BRK.toString();
                forward = "read";
            }else if("endbreak".equals(forward)){
                //mark schedules IP
            	csf.endBreak(person, scheduledDate, now);
            	statusStr = WorkScheduleStatusRef.Status.IP.toString();
                forward = "read";
            }
            
            if("read".equals(forward)){
            	Collection <WorkSchedule> cws;
            	cws = csf.getWorkSchedulesForDate(new Long(person.getId()), sqlDt);
            	int workerStatusIP = WorkScheduleStatusRef.getIdByCode(WorkScheduleStatusRef.Status.IP);
            	HashSet<Long> jobIds = new HashSet<Long>();
                
            	for (WorkSchedule ws : cws) {         //implements filter by locator
                	statusStr = (WorkScheduleStatusRef.getCode(ws.getStatusId()));
                	
                	if(workerStatusIP==ws.getStatusId().longValue()){
	                	Collection<Job> jobs;
	            		jobs = csf.getJobsOnWorkSchedule(ws.getId());
	        			if(jobs!=null && !jobs.isEmpty()){
	        				// Set dispatch date b/c this method invocation means
	        				// the worker is viewing the job list
	        				for (Job job : jobs) {
	        					if(job.getCompletedDate()==null){
	        						// setting dispatched date is done in Start Shift but if this is after start shift we will need to do it here
		        					if (job.getDispatchedDate() == null){
		        						job.setDispatchedDate(new java.sql.Date(System
		        								.currentTimeMillis()));
		        						csf.update(job);
		        						jobIds.add(job.getId());
		        					}
	        					}
	        				}
	        			}
                	}
                }
            	
    			if(!jobIds.isEmpty())
		            try {
		    			Thread tr = new Thread() {
		    				public void run() {
		    					BaseAccess.getWorldConnection().runJSM();
		    				}
		    			};
		    			tr.start();
		    		} catch (Exception e) {
		    			logger.error("Error during Job State Manager run.", e);
		    		}
		        }
	        	
            	Party party = person.getParty();
                form.setName(party.getName());
                form.setId(String.valueOf(person.getId()));
                 
                String name = person.getParty().getName() ;
                PageContext context = new PageContext(name + ", Scheduled Date: " +scheduledDate);
                context.add("worker", name);
                context.add("scheduledDate",scheduledDate);
                form.setScheduledDate(scheduledDate);
                form.setStatus(statusStr);
                form.setBreakTimekeeping(Config.getProperty(Config.BREAK_TIMEKEEPING));
                form.setShiftTimekeeping(Config.getProperty(Config.SHIFT_TIMEKEEPING));
                form.setPageContext(context);
            
        } catch (Exception e) {
            throw new WebException(e);
        }

        return findForward(mapping,request,forward);
    }
}
