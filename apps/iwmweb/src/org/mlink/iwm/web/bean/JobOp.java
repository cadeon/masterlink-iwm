package org.mlink.iwm.web.bean;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.FunkyStringBuffer;

public class JobOp implements BaseExtOp{
	private long jobId;
	private long objId;
	private String objectRef;
	private String loc;
	private String desc;
	private String note;
	
	private String tenantRequestId;
	private String ct;
	private String ph;
	private String xt;
	private String cm;
	
	private String typ;
	private String status;
	
	private LinkedList<JobTaskOp> jobTaskOps;
	
	public JobOp(){
	}
	
	public JobOp(Job j, Long mNewWorker, Date mDay){
		ControlSF mCsf = ServiceLocator.getControlSFLocal( );
		FunkyStringBuffer fsb = new FunkyStringBuffer();
		
		// Populate job buffer
		this.setJobId(j.getId());
		this.setObjId(j.getObjectId());
		
		ObjectEntity oe = mCsf.get(ObjectEntity.class, j.getObjectId());
		if(oe!=null){
			this.setObjectRef(oe.getObjectRef());
		}
		
		this.setNote(fsb.funkify(j.getNote()));
		this.setDesc(fsb.funkify(j.getDescription()));
		
		Locator l = mCsf.get(Locator.class, j.getLocatorId());
		String locator = "";
		if (l!=null) {
			locator = l.getFullLocator();
		}
		this.setLoc(fsb.funkify(locator));
		
		TenantRequest tr = null;
		tr = j.getTenantRequest();
		if (tr!= null) {
			// FIXME: The old version, on the maintenance request
			// page, had a problem request type of "Other" 
			// available to be selected -- meaning
			// that the list did not contain the specific problem
			// the user wanted to report. In that case, in the 
			// old version, the user-entered comment was displayed
			// instead of the problem request description. 
			//
			// This may not be an issue now, since we always display
			// the comment to the user, and since the task def 
			// description gets copied to the job description,
			// eventually adding and "Other" task def to the
			// area target should cover the case where the user
			// doesn't see an appropriate problem from which to
			// select.
			//log("03-A3 description");
			this.setTenantRequestId(Long.toString(tr.getId()));
			this.setCt(fsb.funkify(tr.getTenantName()));
			this.setPh(fsb.funkify(tr.getTenantPhone()));
			this.setXt(fsb.funkify(tr.getTenantEmail()));
			String scom = tr.getNote();
			//PalmHandler used to cut cm at 60 characters, I commented it here as the iPhone webapp can do more
			//I hope it doesn't break the Palms but they are on the way out anyway -Chris
			//	if (scom.length() > 60) {
			//	log("TenantRequest note length > 60, trimming...");
			//		scom = scom.substring(0,60);
			//	}
			this.setCm(fsb.funkify(scom));
		}
	//		sb.append("typ=Assessment||");
	//	}
    //    else {
	//		log("03-B1 type is routine");
	//        sb.append("typ=Routine||");
    //    }
	    //c'mon, Mike, lookup the real job type -Chris.
		String taskType = TaskTypeRef.getLabel(j.getJobTypeId());
		this.setTyp(fsb.funkify(taskType));
	    
	    JobStatusRef me = (JobStatusRef) LookupMgr.getInstance(JobStatusRef.class);
		OptionItem option = me.getOptionItem(String.valueOf(j.getStatusId()));
		this.setStatus(option.getCode());
		
	    Date dispatchedDate = j.getDispatchedDate();
	    if(dispatchedDate==null){
	    	j.setDispatchedDate(new java.sql.Date(
				new java.util.Date().getTime()));
			mCsf.update(j);
	    }
		
	    this.setJobTaskOps(new LinkedList<JobTaskOp>()); //create, even if empty	
		Collection<JobTask> jobTasks = j.getJobTasks();
		if(jobTasks!=null && !jobTasks.isEmpty()){
			// Populate jobtask buffer
			LinkedList<JobTaskOp> jobTaskOps = this.getJobTaskOps();
			JobTaskOp jobTaskOp;
			for(JobTask jobTask : jobTasks){
				jobTaskOp = new JobTaskOp(jobTask, mNewWorker, mDay);
				jobTaskOps.add(jobTaskOp);
			}
		}
	}
	
	public boolean logicEquals(Job job){
		boolean logicEquals = true;
		JobStatusRef me = (JobStatusRef) LookupMgr.getInstance(JobStatusRef.class);
		Long value = me.getValueByCode(this.getStatus());
		if(value.intValue() != job.getStatusId().intValue()){
			logicEquals = false;
		}
		return logicEquals;
	}
	
	public void update(Job job){
		String status =(String)this.getStatus();
		Integer statusId = JobStatusRef.getIdByCode(status);
		job.setStatusId(statusId);
		
		if(JobStatusRef.Status.DUN.equals(status)){
			java.sql.Date now = new java.sql.Date(
					new java.util.Date().getTime());
			job.setCompletedDate(now);
		}
		
		ControlSF mCsf = ServiceLocator.getControlSFLocal( );
        mCsf.update(job);
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LinkedList<JobTaskOp> getJobTaskOps() {
		return jobTaskOps;
	}
	public void setJobTaskOps(LinkedList<JobTaskOp> jobTaskOps) {
		this.jobTaskOps = jobTaskOps;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTenantRequestId() {
		return tenantRequestId;
	}
	public void setTenantRequestId(String tenantRequestId) {
		this.tenantRequestId = tenantRequestId;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public String getXt() {
		return xt;
	}
	public void setXt(String xt) {
		this.xt = xt;
	}
	public String getCm() {
		return cm;
	}
	public void setCm(String cm) {
		this.cm = cm;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	
	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(String objectRef) {
		this.objectRef = objectRef;
	}

	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
