package org.mlink.iwm.web.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.mlink.iwm.entity3.BaseEntity;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.entity3.JobTask;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.session.ControlSFLocal;
import org.mlink.iwm.web.bean.BaseExtOp;
import org.mlink.iwm.web.bean.JobActionOp;
import org.mlink.iwm.web.bean.JobOp;
import org.mlink.iwm.web.bean.JobTaskOp;
import org.mlink.iwm.web.bean.WorkScheduleOp;
import org.mlink.iwm.web.bean.WorkerOp;

public class PalmHandler
{	
	private static final Logger logger = Logger.getLogger(PalmHandler.class);
	private FunkyStringBuffer fsb = new FunkyStringBuffer();
	
	// *** Constructor(s) ***

	private void log(String s) {
		logger.debug(s);
	}
	
	/**
	 ** Instances of the Visitor interface are applied to each string
	 ** in a Vector, after mapifying the string, so that the loop does
	 ** not have to be rewritten.
	 **/
	private interface NVisitor {
		public void process(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception;
	}
	
	/**
	 ** The apply function does the mapifying and calls process on the
	 ** visitor.
	 **/
	private void apply(BaseEntity baseEntity, BaseExtOp baseExtOp, NVisitor vi) throws Exception{
			vi.process(baseEntity, baseExtOp);
	}
	
	/**
	 ** Upload the given WorkerOp
	 **
	 ** @see NVisitor
	 **/
	public void upload(Collection<WorkSchedule> presentWorkSchedules, WorkerOp workerOp) throws Exception{
		log("Resident worker:"+(String)workerOp.getName()+":"+workerOp.getId());
		LinkedList<WorkScheduleOp> wsOps = workerOp.getWorkScheduleOps();
		if(wsOps!=null){
			for(WorkScheduleOp wsOp: wsOps){
				for(WorkSchedule ws: presentWorkSchedules){
					if(wsOp.getWorkScheduleId() == ws.getId()){
						apply((BaseEntity)ws, (BaseExtOp)wsOp, mWorkScheduleNVisitor); //this must go first to set the day properly
						break;
					}
				}
			}
		}
	}
	
	/**
	 ** Visitor for workerSchedule
	 **/
	NVisitor mWorkScheduleNVisitor = new NVisitor()	{
		public void process(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception{ 
			updateWorkSchedule(baseEntity, baseExtOp); 
		} 
	};
		
	/**
	 ** Process a worker from palm
	 **/
	public void updateWorkSchedule(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception	{
		WorkScheduleOp wsOp = (WorkScheduleOp)baseExtOp;
		WorkSchedule ws = (WorkSchedule)baseEntity;
		
		if(!wsOp.logicEquals(ws)){
			//needs update
			doUpdateWorkSchedule(ws, wsOp);
		}
		
		Collection<JobOp> jobOps = wsOp.getJobOps();
		Collection<Job> jobs = ws.getJobs();
		boolean match;
		for(JobOp jobOp: jobOps){
			match = false;
			for(Job job: jobs){
				if(job.getId()==jobOp.getJobId()){
					apply((BaseEntity)job, (BaseExtOp)jobOp, mJobNVisitor); //this must go first to set the day properly
					match = true;
					break;
				}
			}
			if(match == false){
				throw new Exception("jobOp: "+jobOp.getJobId()+" not matched.");
			}
		}
	}
	
	public void doUpdateWorkSchedule(WorkSchedule ws, WorkScheduleOp wsOp)	{
		wsOp.update(ws);
	}

	/**
	 ** Visitor for Job
	 **/
	NVisitor mJobNVisitor = new NVisitor()	{
		public void process(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception { 
			updateJob(baseEntity, baseExtOp); 
		} 
	};
		
	/**
	 ** Process a job from palm
	 * @throws Exception 
	 **/
	public void updateJob(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception	{
		JobOp jobOp = (JobOp)baseExtOp;
		Job job = (Job)baseEntity;
		
		if(!jobOp.logicEquals(job)){
			//needs update
			doUpdateJob(job, jobOp);
		}
		
		Collection<JobTaskOp> taskOps = jobOp.getJobTaskOps();
		Collection<JobTask> jobTasks = job.getJobTasks();
		boolean match;
		for(JobTaskOp taskOp: taskOps){
			match = false;
			for(JobTask jobTask: jobTasks){
				if(jobTask.getId()==taskOp.getId()){
					apply((BaseEntity)jobTask, (BaseExtOp)taskOp, mTaskNVisitor); //this must go first to set the day properly
					match = true;
					break;
				}
			}
			if(match == false){
				throw new Exception("taskOp: "+ taskOp.getId()+" not matched.");
			}
		}
	}
	
	public void doUpdateJob(Job job, JobOp jobOp)	{
		jobOp.update(job);
	}

	/**
	 ** Visitor for Task
	 **/
	NVisitor mTaskNVisitor = new NVisitor()	{
		public void process(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception{ 
			updateJobTask(baseEntity, baseExtOp); 
		} 
	};
		
	/**
	 ** Process a worker from palm
	 * @throws Exception 
	 **/
	public void updateJobTask(BaseEntity baseEntity, BaseExtOp baseExtOp) throws Exception	{
		JobTaskOp taskOp = (JobTaskOp)baseExtOp;
		JobTask jobTask = (JobTask)baseEntity;
		
		if(!taskOp.logicEquals(jobTask)){
			//needs update
			doUpdateJobTask(jobTask, taskOp);
		}
		
		Collection<JobActionOp> actionOps = taskOp.getJobActionOps();
		Collection<JobAction> actions = jobTask.getActions();
		boolean match;
		for(JobActionOp actionOp: actionOps){
			match = false;
			for(JobAction action: actions){
				if(action.getId()==actionOp.getId()){
					apply((BaseEntity)action, (BaseExtOp)actionOp, mActionNVisitor); //this must go first to set the day properly
					match = true;
					break;
				}
			}
			if(match == false){
				throw new Exception("actionOp: "+actionOp.getId()+" not matched.");
			}
		}
	}
	
	public void doUpdateJobTask(JobTask jobTask, JobTaskOp taskOp) throws Exception	{
		taskOp.update(jobTask);
	}

	/**
	 ** Visitor for Action
	 **/
	NVisitor mActionNVisitor = new NVisitor()	{
		public void process(BaseEntity baseEntity, BaseExtOp baseExtOp) { 
			updateJobAction(baseEntity, baseExtOp); 
		} 
	};
		
	/**
	 ** Process a worker from palm
	 **/
	public void updateJobAction(BaseEntity baseEntity, BaseExtOp baseExtOp)	{
		JobActionOp actionOp = (JobActionOp)baseExtOp;
		JobAction jobAction = (JobAction)baseEntity;
		
		if(!actionOp.logicEquals(jobAction)){
			//needs update
			doUpdateJobAction(jobAction, actionOp);
		}
	}
	
	public void doUpdateJobAction(JobAction jobAction, JobActionOp actionOp)	{
		actionOp.update(jobAction);
	}
		
	public WorkerOp download(Collection<WorkSchedule> presentWorkSchedules, WorkerOp worker1){
		log("PalmHandler downloading");
		//log("  person id="+personId);
		try {
			WorkerOp worker = loadWorkerData(presentWorkSchedules, worker1);
			log("All looks well");
			return worker;
		} catch ( Exception e ) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			log(sw.toString());
			e.printStackTrace();
            return worker1;
		} finally {
			log("PalmHandler done downloading");
		}
	}

	// string buffer used to eliminate unprintable chars
	class FunkyStringBuffer {
		private StringBuffer sb = new StringBuffer();
        
		public void setLength(int i) {
			sb.setLength(i);
		}
		public void append(String s) {
			// FIXME:This is a hack to avoid dane-bramage below
			char[] chars = s.toCharArray();
			int length = chars.length;
			if ( length < 2 ) {
				sb.append(s);
				return;
			};
			for (int i = 0; i < length-2; i++) {
				if (canDisplay(chars[i], chars[i+1])) {
						sb.append(chars[i]);
				}
			}
			sb.append(chars[length-2]);
			sb.append(chars[length-1]);
		}

		public boolean canDisplay(char c, char next) {
			// we print '||' as '|' and eliminate unprintable chars
		   if (c == '|' && next == '|')	return false;
		   return (c >0x1F && c < 0x7F); 
		}

		public String toString() {
			return sb.toString();
		}
	};

    public String funkify(String s) {  
        if (s == null || s.length() == 0) return "";      
        fsb.setLength(0);
        fsb.append(s);
        return fsb.toString();
	}
	
	private WorkerOp loadWorkerData(Collection<WorkSchedule> col, WorkerOp worker) throws Exception
	{
		LinkedList<WorkScheduleOp> workSchedules = new LinkedList<WorkScheduleOp>();
		worker.setWorkScheduleOps(workSchedules);
		
		if (col!=null)
		{
			log("Got workschedules for worker: "+ col.size());
			
			WorkScheduleOp wsOp;
			for (WorkSchedule wsvo : col){
				wsOp = new WorkScheduleOp(wsvo);
				workSchedules.add(wsOp);
			}
		}
        //worker.setWorkSchedule(mSchedulesN);
		return worker;
	}
	
	/**
	 ** Utility routine to determine safely if an object is one of:
	 **   null
	 **   an empty string
	 **   the string "null"
	 ** Please don't ask about the last, it's a sad story  :)
	 **/
	public static boolean noe( Object o ) {
		if ( o == null )
			return true;
		if ( o.equals("") )
			return true;
		if ( o instanceof String )
			return ((String)o).equalsIgnoreCase("null");
		if ( o instanceof Integer )
			return ((Integer)o).equals(0);
		if ( o instanceof Long )
			return ((Long)o).equals(0L);
		return false;
	}
	/**
	 ** Utility routine to fuzzily parse an integer from an object,
	 ** returning some value if at all possible.
	 **
	 ** Should we add the functionality here to the one in
	 ** Util, or do we only want to be this fuzzy here?
	 **
	 ** RGP
	 ** 
	 **/
	public static int intValue(Object o) {
		if ( o == null )
			return 0;
		if ( o instanceof Number )
			return ((Number)o).intValue();
		String s = o.toString();
		if ( s.equalsIgnoreCase("null") )
			return 0;
		char[] chars = s.toCharArray();
		if ( chars.length == 0 )
			return 0;
		char[] nchars = new char[chars.length];
		int j = 0;
		for ( int i = 0; i < chars.length; i++ ) {
			char ch = chars[i];
			if ( Character.isDigit(ch) )
				nchars[j++] = ch;
			else if ( Character.isWhitespace(ch) )
				continue;
			else if ( Character.isISOControl(ch) )
				continue;
			else {
				throw new NumberFormatException("converting '"+o+"'");
			}
		}
		if ( j == 0 )
			return 0;
		return Integer.parseInt(new String(nchars,0,j));
	}
}
