package org.mlink.iwm.web.bean;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.entity3.JobSchedule;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.JobTaskTime;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.web.action.PalmHandler;

public class JobTaskOp implements BaseExtOp {
	private Long id;
	private String objId;
	private String hrs;
	private String mins;
	private String numw;
	private String desc;
	
	private Long personId;
	private Date mDay;
	
	private LinkedList<JobActionOp> jobActionOps;

	public JobTaskOp(){	
	}
	
	public JobTaskOp(JobTask jt, Long mNewWorker, Date mDay){
		this.personId = mNewWorker;
		this.mDay = mDay;
		
		Task t = jt.getTask();//Get job task time for worker and work scheduled Date
		this.setId(jt.getId());
		this.setObjId(Long.toString(t.getObject().getId()));
		this.setNumw(numw);
		this.setDesc(jt.getTaskDescription());
		
		// (this requires using global mNewWorker and mDay)
		int itime = 0;
		try {
			itime = getJobTaskTime(jt, mNewWorker, mDay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        int ihr   = itime / 60;
        int imin  = itime - ihr*60; 
		
		this.setHrs(Long.toString(ihr));
		this.setMins(Long.toString(imin));
		
		/*	// Populate jobtask buffer 
			taskItems.put("td", funkify(t.getTaskDescription()) );		 */
		
		// Populate jobactions 
		this.setJobActionOps(new LinkedList<JobActionOp>());
		Collection<JobAction> jobActions = jt.getActions();
		if(jobActions!=null && !jobActions.isEmpty()){
			LinkedList<JobActionOp> jobActionOps = this.getJobActionOps();
			this.setJobActionOps(jobActionOps);
			JobActionOp jobActionOp;
			for(JobAction jobAction : jobActions){
				jobActionOp = new JobActionOp(jobAction);
				jobActionOps.add(jobActionOp);
			}			
		}	
	}
	
	private int getJobTaskTime(JobTask jt, Long mNewWorker, Date mDay) 
	throws Exception // CreateException, FinderException
	{
		JobTaskTime jtt = getJobTaskTimeObj(jt, mNewWorker, mDay);
		return jtt.getTime().intValue();
	}
	
	private JobTaskTime getJobTaskTimeObj(JobTask jt, Long mNewWorker, Date mDay) 
	throws Exception // CreateException, FinderException
	{
		ControlSF mCsf = ServiceLocator.getControlSFLocal( );
		JobSchedule js = mCsf.getJobScheduleByPersonDateAndJob(
			mNewWorker,
			mDay,
			jt.getJob().getId() );
		JobTaskTime jtt = null;
		try { // see if task time exists for the job task and job schedule
			jtt = mCsf.getJTTByJobScheduleAndJobTask(
				js.getId(),
				jt.getId());
		} 
		catch (Exception e) {
			//e.printStackTrace();
			//JobTaskTime not found so will be created.
			jtt=null;//place holder
		};
		if (jtt==null) {
			// create entity and insert values
			jtt = new JobTaskTime();
			jtt.setJobTask(new JobTask(jt.getId()));
			jtt.setJobScheduleId(js.getId());
			jtt.setTime(new Integer(0));
			mCsf.create(jtt);
		}
		return jtt;
	}
	
	public boolean logicEquals(JobTask jt){
		boolean logicEquals = true;
		int itime = 0;
		
		try {
			itime = getJobTaskTime(jt, personId, mDay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        int ihr   = itime / 60;
        int imin  = itime - ihr*60; 
		
		if(ihr != Integer.parseInt(this.getHrs())||
				imin != Integer.parseInt(this.getMins())){
			logicEquals = false;
		}
		return logicEquals;
	}
	
	public void update(JobTask jt) throws Exception{
		JobTaskTime jtt=null;
	    ControlSF csf = ServiceLocator.getControlSFLocal();
	    	
		jtt = getJobTaskTimeObj(jt, personId, mDay);
		
		String shr = this.getHrs();
		String smn = this.getMins();
		
		if ( !PalmHandler.noe(shr) || !PalmHandler.noe(smn))	{
			int ihr = PalmHandler.intValue(shr);
			int imn = PalmHandler.intValue(smn);
		
			int ntime = (ihr * 60) + imn;
			jtt.setTime(ntime);
        
			csf.update(jtt);
			
			if(ntime>0){
				//andrei: 11/09/06: set total time for job task
		        jt.setTotalTime(csf.getTotalTimeByJobTaskId(jt.getId()));
		        //andrei added:10/10/07
		        Job job = jt.getJob();
		        job.setTotalTime(csf.getTotalTimeByJobId(job.getId()));
		        
		        csf.update(jt);
		        csf.update(job);
			}
		}    
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public LinkedList<JobActionOp> getJobActionOps() {
		return jobActionOps;
	}

	public void setJobActionOps(LinkedList<JobActionOp> jobActionOps) {
		this.jobActionOps = jobActionOps;
	}

	public void setHrs(String hrs) {
		this.hrs = hrs;
	}

	public String getHrs() {
		return hrs;
	}

	public void setMins(String mins) {
		this.mins = mins;
	}

	public String getMins() {
		return mins;
	}

	public void setNumw(String numw) {
		this.numw = numw;
	}

	public String getNumw() {
		return numw;
	}
}
