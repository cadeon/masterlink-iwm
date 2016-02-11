package org.mlink.agent;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.sql.Timestamp;
import java.util.HashMap;

import org.mlink.agent.dao.DAOException;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobTask;
import org.mlink.agent.model.JobTaskTime;
import org.mlink.agent.model.Task;
import org.mlink.agent.model.TaskView;
import org.mlink.agent.model.World;
import org.mlink.iwm.util.DateUtil;

public class SimWorldConnection extends WorldConnection implements WorldInfo, SimWorld {
	

	private Integer simStatus;
	private HashMap<Long, RepairJob> repairJobs = new HashMap<Long, RepairJob>();
	class RepairJob {
		Job job;
		JobTask jobTask;
		JobTaskTime jobTaskTime;
	}
	
	protected SimWorldConnection() {
		super();
	}

	protected SimWorldConnection(World world) throws Exception {
		super();
		this.world = world;
		planner = new Planner(world.getName());
		//jsm = new JobStateManager(world.getName());
		//log(jsm.printStateMachine());		
	}


	/** Prepares the specified simulated world to be used for agent runs. Checkout locks the run methods
	 * against invocation by other entities with a handle to this world. Use
	 * checkin() to unlock the world.
	 * 
	 * @param world The world name
	 * @return True if the world was successfully checked out. False otherwise
	 * @throws DAOException
	 * @see checkin
	 */
	public static SimWorldConnection checkoutSimulation(World world) throws DAOException, InvalidAccessException, Exception {
		return (SimWorldConnection) checkout(world,false);
	}
	
	// *******************************************************

	public Date getLastSimulationRun() {
		return this.world.getLastSimulationRun();
	}

	public String getDisplayLastSimulationRun() {
		return this.world.getDisplayLastSimulationRun();
	}

	public Integer getSimStatus() {
		return simStatus;
	}

	public String getDisplaySimStatus() {
		return (simStatus == null ? "no sim running" : simStatus.toString());
	}

	private void setSimStatus(Integer i) {
		simStatus = i;
	}


	public void setLastSimulationRun(Date d) {
		this.world.setLastSimulationRun(d);
	}

	/**
     * Create a repair job for the specified object
     * @param task
     * @param repairTime
     * @param day
     * @return
     * @throws Exception
     */
    public RepairJob createRepairJob(Task task, Integer repairTime,Timestamp day)
			throws Exception {
		validate();
		// create job
		Job job = new Job();
		job.setId(getNextJobId());
		job.setObjectId(task.getObjectId());
		job.setEstimatedTime(repairTime.intValue());
		
		JobTask jt = new JobTask();
		jt.setJob(job);
		jt.setTaskId(task.getId());
		
		RepairJob rj = new RepairJob();
		rj.job = job;
		rj.jobTask = jt;
		
		//FIXME: Assign job to available worker. See  in v2 for getting list of workers for manual scheduling.
		
		// add to repair job list
		if (repairJobs.containsKey(job.getId()))
			throw new Exception("Duplicate job id created. Job id " + job.getId()
					+ " already exists as a repair job.");
		repairJobs.put(job.getId(), rj);
		jobs.add(job);

		return rj;
	}
	
	private Long getNextJobId() {
		return nextJobId + 1L;
	}

	/**
	 * Complete jobs for the day.
	 * 
	 */
	public void completeRoutineJobs(Timestamp day) {
		validate();
		for (Iterator<Job> it = jobs.iterator(); it.hasNext();) {
			Job j = it.next();
			j.setCompletedDate(DateUtil.sqlDate(day));
		}
		// Get all jobs, set their task time to their estimated time and set
		// completed date
		// job.complete(currentDay);
	}

	/**
	 * Mark the repair job specified by the job id as complete.
	 * 
	 * @param rj The repair job to mark completed.
	 */
	public void completeRepairJob(RepairJob rj, Timestamp day) {
		if (rj == null)
			return;
		if (!repairJobs.containsKey(rj.job.getId()))
			return;
		rj.job.setCompletedDate(DateUtil.sqlDate(day));
		/*FIXME: Really need to set the completedDate in the jobs list, which means that it might 
		 * be useful to create a jobs hash for job state management, with the hash key being the
		 * job state, and the value being an arraylist of indexes into the jobs list, pointing to the correct jobs.
		 */
	}
	
    /**
     * Ends all worker shifts for the day
     * 
     * @param day The completion date for the shifts
     */

	public void endWorkerShifts(Timestamp day) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @throws InvalidAccessException
	 */
	/*public void run(String s) {
		validate();
	}*/

	public void prepareSimulation() throws Exception{
		this.jsm = new JobStateManager(this.getName());
		this.planner = new Planner(this.getName());
		this.scheduler = new Scheduler(this.getName());
		this.shiftMgr = new ShiftManager(this.getName());
	}

    public void runAllButPlanner() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public void runAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runJSM() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Job> runJSM(Collection jobs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runPlanner() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Job> runPlanner(Collection<TaskView> c, Timestamp day) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runScheduler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection runScheduler(Collection jobs, Collection workschedules, Timestamp t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runShiftManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection runShiftManager(Collection schedules, Timestamp t) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void log(Object o) {super.log(world.getName() +":"+ o);}
	
} // End class WorldConnection

