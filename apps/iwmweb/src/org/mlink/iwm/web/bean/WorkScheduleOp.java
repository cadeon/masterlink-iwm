package org.mlink.iwm.web.bean;

import java.util.Collection;
import java.util.LinkedList;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.DateUtil;

public class WorkScheduleOp implements BaseExtOp{
	private long workScheduleId;
	private long shiftId;
	private String date;
	private String location;
	private String time;
	
	private String stime;
	private String etime;
	
	private String status;
	
	private LinkedList<JobOp> jobOps;
	
	public WorkScheduleOp(){
	}
	
	public boolean logicEquals(WorkSchedule ws){
		boolean logicEquals = true;
		WorkScheduleStatusRef me = (WorkScheduleStatusRef) LookupMgr.getInstance(WorkScheduleStatusRef.class);
		Long value = me.getValueByLabel(this.getStatus());
		if(value.intValue() != ws.getStatusId().intValue()){
			logicEquals = false;
		}
		return logicEquals;
	}
	
	public WorkScheduleOp(WorkSchedule ws){
		this.setWorkScheduleId(ws.getId());
		Integer shiftStMins = Integer.parseInt(org.mlink.iwm.lookup.ShiftRef.getShiftStart(ws.getShiftId()));
		Integer shiftEnMins = Integer.parseInt(org.mlink.iwm.lookup.ShiftRef.getShiftEnd(ws.getShiftId()));
		this.setStime(DateUtil.convertMinsToHMMfotmat(shiftStMins));
		this.setEtime(DateUtil.convertMinsToHMMfotmat(shiftEnMins));
		this.setShiftId(ws.getShiftId());
		this.jobOps = new LinkedList<JobOp>(); //So we always send a jobs object, even if blank
		
		WorkScheduleStatusRef me = (WorkScheduleStatusRef) LookupMgr.getInstance(WorkScheduleStatusRef.class);
		OptionItem option = me.getOptionItem(String.valueOf(ws.getStatusId()));
		this.setStatus(option.getLabel());
		this.setLocation(ws.getLocation());
		Integer shiftTimeMins = Integer.parseInt(ws.getTime()!=null?ws.getTime().toString():"0");
		this.setTime(DateUtil.convertMinsToHMMfotmat(shiftTimeMins));
		
		if(ws.getDay()!=null ){
			this.setDate(DateUtil.displayShortDateDay(ws.getDay()));
		}
		Collection<Job> jobs = ws.getJobs();
		JobOp jobOp;
		if(jobs!=null && !jobs.isEmpty()){
			LinkedList<JobOp> jobOps = new LinkedList<JobOp>();
			this.setJobOps(jobOps);
			for(Job job: jobs){
				if(job.getCompletedDate()==null){
					jobOp = new JobOp(job, ws.getPersonId(), ws.getDay());
					jobOps.add(jobOp);
				}
			}
		}
	}

	public void update(WorkSchedule ws){
		String status =(String)this.getStatus();
		Integer statusId = WorkScheduleStatusRef.getIdByCode(status);
		ws.setStatusId(statusId);
		
		ControlSF mCsf = ServiceLocator.getControlSFLocal( );
        mCsf.update(ws);
	}
	
	public long getShiftId() {
		return shiftId;
	}

	public void setShiftId(long shiftId) {
		this.shiftId = shiftId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getWorkScheduleId() {
		return workScheduleId;
	}

	public void setWorkScheduleId(long workScheduleId) {
		this.workScheduleId = workScheduleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LinkedList<JobOp> getJobOps() {
		return jobOps;
	}

	public void setJobOps(LinkedList<JobOp> jobOps) {
		this.jobOps = jobOps;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
