/*---------------------------------iwm----------------------------------
	File: MWSync.java
	Package: org.mlink.iwm.web.action
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.web.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.DataException;
import org.mlink.iwm.lookup.MWAccessTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.struts.action.BaseAction;
import org.mlink.iwm.web.bean.WorkScheduleOp;
import org.mlink.iwm.web.bean.WorkerOp;

import com.google.gson.Gson;

public class MWSync extends BaseAction {
	private static final Logger logger = Logger.getLogger(MWSync.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		logger.debug("In MYSync.executeLogic.");
		processSync(request, response);
		return null;
	}

	private String processSync(HttpServletRequest request,
			HttpServletResponse response) throws WebException {
		logger.debug("Entering MWSync.processSync.");
		String json;
		String workerJSON = request.getParameter("worker");
		
		try {
			json = processSync(workerJSON);
			boolean redo = false;
			if(redo){
				logger.debug("Redo MWSync.processSync with o/p of first operaton.");
				String json2 = processSync(json);
				logger.debug("Done redo MWSync.processSync. output: "+json2);
			}
			
			//No need to escape the output -Chris
			PrintWriter pw = response.getWriter();
			pw.write(json);			
		} catch (Exception e) {
			throw new WebException(e);
		}

		logger.debug("Leaving MWSync.processSync");
		return json;
	} // end method processSync()

	private String processSync(String workerJSON){
		logger.debug("processSync returned");
		WorkerOp workerOp;
		String json = "";
		if (workerJSON == null) {
			logger.debug("No parameter either, punting.");
			workerOp = getBlankWorkerOp();
			workerOp.setErrStr("No user info or data sent.");
		}else{
			Gson gson = new Gson();
			logger.debug("workerJSON: "+workerJSON);
			WorkerOp workerOpIn = gson.fromJson(workerJSON, WorkerOp.class);   
			if(workerOpIn != null){
				workerOp = processExtInput(workerOpIn);
			}else{
				workerOp = getBlankWorkerOp();
				workerOp.setErrStr("Bad JSON string sent.");		
			}
		}
		
		Gson gson = new Gson();
		json = gson.toJson(workerOp);
		logger.debug("outputAsXml: "+json);
		return json;
	}
	
	private WorkerOp processExtInput(WorkerOp workerOp){
		String username = workerOp.getUsername();
		String passwd = workerOp.getPassword();
		
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		ControlSF csf = ServiceLocator.getControlSFLocal();
        User user = null;
		try {
			user = psf.getUserByName(username);
		} catch (DataException e) {
			//do not do anything. taken care of in if(user == null){			
		}
		
		if(user == null){
			//in correct user
			workerOp.setId(0);
			workerOp.setWorkScheduleOps(new LinkedList<WorkScheduleOp>());
		}else if(passwd.equals(user.getPassword())){
			//authenticated
			workerOp.setId(user.getPerson().getId());
			Person person = user.getPerson();
			if(person!=null){
				Party pty = person.getParty();
				workerOp.setName(pty.getName());
			
				LinkedList<WorkScheduleOp> workScheduleOps = workerOp.getWorkScheduleOps();
				Collection<WorkSchedule> presentWorkSchedules = csf.getCurrentWorkSchedules(person);
				boolean updateSuccess = true;
				boolean inLoginProc = false;
				boolean inUploadProc = false;
				String updateErrStr="";
				if(workScheduleOps!=null && !workScheduleOps.isEmpty()){
					//these work schedules should be updated
					inUploadProc = true;
					try {
						//upload
						csf.logMWAccess(person.getUsername(), null, MWAccessTypeRef.Type.MWU, null, workerOp.getLatitude(), workerOp.getLongitude(), workerOp.getAccuracy());
						uploadWorkerData( presentWorkSchedules, workerOp);
						//presentWorkSchedules = csf.getCurrentWorkSchedules(person);
					} catch (Exception e) {
						// update unsuccessfull
						updateSuccess = false;
						updateErrStr = e.getMessage();
					}
				}else{
					inLoginProc = true;
					//first login-no update
					csf.logMWAccess(person.getUsername(), null, MWAccessTypeRef.Type.MWL, null, workerOp.getLatitude(), workerOp.getLongitude(), workerOp.getAccuracy());
				}
				
				//download
				if(!inLoginProc && !inUploadProc){
					csf.logMWAccess(person.getUsername(), null, MWAccessTypeRef.Type.MWD, null, workerOp.getLatitude(), workerOp.getLongitude(), workerOp.getAccuracy());
				}
				
				downloadWorkerData(presentWorkSchedules, workerOp);
				
				if(!updateSuccess){
					workerOp.setErrStr("Update unsuccessfull. updateErrStr: "+updateErrStr);
				}
			}
		}else{
			//password is wrong
			workerOp.setId(0);
			workerOp.setWorkScheduleOps(new LinkedList<WorkScheduleOp>());
		}
		return workerOp;
	}
	
	private void uploadWorkerData(Collection<WorkSchedule> presentWorkSchedules, WorkerOp worker) throws Exception{
		PalmHandler ph = new PalmHandler();
		ph.upload(presentWorkSchedules, worker);
	}

	private WorkerOp downloadWorkerData(Collection<WorkSchedule> presentWorkSchedules, WorkerOp worker)  {
		PalmHandler ph = new PalmHandler();
		return ph.download(presentWorkSchedules, worker);
	}
	
	private WorkerOp getBlankWorkerOp(){
		WorkerOp workerOp = new WorkerOp();
		workerOp.setUsername("username");
		workerOp.setPassword("password");
		workerOp.setLatitude(0);
		workerOp.setLongitude(0);
		workerOp.setAccuracy(0);
		workerOp.setWorkScheduleOps(new LinkedList<WorkScheduleOp>());
		workerOp.setErrStr("PlaceHolder Worker object");
		return workerOp;
	}
}

// EOF: MWSync.java