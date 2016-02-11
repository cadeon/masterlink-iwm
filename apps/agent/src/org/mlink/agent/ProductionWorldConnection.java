package org.mlink.agent;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.hibernate.HibernateException;
import org.mlink.agent.algorithm.ScheduleWrapper;
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.model.JSMJob;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.JobStatusRef;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.ScheduleResponsibilityRef;
import org.mlink.agent.model.SchedulerJob;
import org.mlink.agent.model.ShiftMgrJob;
import org.mlink.agent.model.Skill;
import org.mlink.agent.model.TaskView;
import org.mlink.agent.model.WorkSchedule;
import org.mlink.agent.model.WorkScheduleStatusRef;
import org.mlink.agent.model.World;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.util.UserTrackHelper;
import org.apache.log4j.Logger;

public class ProductionWorldConnection extends WorldConnection {
    private static final Logger logger = Logger.getLogger(ProductionWorldConnection.class);

    private static ProductionWorldConnection prodWorld;
    private static ReentrantLock lock = new ReentrantLock();
    private static String lockingAgent = "";

    private ProductionWorldConnection() throws Exception {
        super();
        planner = new Planner();
        jsm = new JobStateManager();
        scheduler = new Scheduler();
        shiftMgr = new ShiftManager();
    }
    private ProductionWorldConnection(World world) throws Exception {
        super();
        this.world = world;
        planner = new Planner(world.getName());
        jsm = new JobStateManager(world.getName());
        scheduler = new Scheduler(world.getName());
        shiftMgr = new ShiftManager(world.getName());
    }
    /**
     * Gets the singleton instance of the production world, to be used for agent runs.
     *
     * @return ProductionWorldConnection
     * @throws DAOException
     */
    protected static synchronized ProductionWorldConnection getInstance(World world) throws Exception {
        if (prodWorld==null) prodWorld = new ProductionWorldConnection(world);
        return prodWorld;
    }

    /**
     * Prepares the production world to be used for agent runs. Checkout locks the run methods
     * against invocation by other entities with a handle to this world. Use
     * checkin() to unlock the world.
     *
     * @return ProductionWorldConnection
     * @throws DAOException
     * @see checkin
     */
    public static ProductionWorldConnection checkoutProductionWorld() throws DAOException, InvalidAccessException, Exception {
        if (prodWorld!=null) return prodWorld;
        World world = WorldDAO.getInstance().getMatrix();
        if(world==null) world=createProductionWorld();
        prodWorld = (ProductionWorldConnection) checkout(world,true);
        return prodWorld;
    }

    /**
     * This method is provided for new schema initialization purpose. Too many time we forgot to insert the record into world table!
     * @return
     * @throws DAOException
     */
    private static World createProductionWorld() throws DAOException{
        World w = new World();
        w.setIsProduction(1);
        w.setName("MATRIX");
        w.setSchema(UserTrackHelper.getProductionSchema());
        WorldDAO.getInstance().saveWorld(w);
        return WorldDAO.getInstance().getMatrix();
    }

    /**
     * Run the suite of agents minus the Planner. Execution order: 
     * 1. ShiftManager 
     * 2. Job State Manager
	 * 3. Scheduler 
	 * 4. Job State Manager
     *
     */
    public void runAllButPlanner() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to do a complete agent run. "+ lockingAgent+" is running.");
                return;
            }
            log("Run agents acquired agent lock.");
            lockingAgent="RunAll";
            AgentFlashLog.getInstance().clear();
            AgentFlashLog.getInstance().add("Agents run started");
            ProductionWorldConnection.prodWorld.runOneShotShiftManager();
            ProductionWorldConnection.prodWorld.runOneShotJSM();
            ProductionWorldConnection.prodWorld.runOneShotScheduler();
            ProductionWorldConnection.prodWorld.runOneShotJSM();
            AgentFlashLog.getInstance().add("Agents run completed");
        } catch (Exception e) {
            logger.error(e); //print to agents.log
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by Run agents).");
            }
        }
    }

    /**
     * Run the entire suite of agents. Execution order: 
     * 1. ShiftManager 
     * 2. Planner 
     * 3. Job State Manager 
     * 4. Scheduler 
     * 5. Job State Manager
     *
     */
    @Override
    public void runAll() {
        try {
	        if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
	            log("Could not acquire lock to do a complete agent run. "+ lockingAgent+" is running.");
	            return;
	        }
	        log("RunAll (run all agents) acquired agent lock.");
	        lockingAgent="RunAll";
	        AgentFlashLog.getInstance().clear();
	        AgentFlashLog.getInstance().add("All agents run started");
	        ProductionWorldConnection.prodWorld.runOneShotShiftManager();
	        ProductionWorldConnection.prodWorld.runOneShotPlanner();
	        ProductionWorldConnection.prodWorld.runOneShotJSM();
	        ProductionWorldConnection.prodWorld.runOneShotScheduler();
	        ProductionWorldConnection.prodWorld.runOneShotJSM();
	        AgentFlashLog.getInstance().add("All agents run completed");
	    } catch (Exception e) {
	        throw new IWMException(e);
	    } finally {
	        if (lock.isHeldByCurrentThread()) {
	            lockingAgent="";
	            lock.unlock();
	            log("Released agent lock held by RunAll (run all agents).");
	        }
	    }
    }

    public void runAll2() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to do a complete agent run. "+ lockingAgent+" is running.");
                return;
            }
            log("RunAll (run all agents) acquired agent lock.");
            lockingAgent="RunAll";
            AgentFlashLog.getInstance().clear();
            AgentFlashLog.getInstance().add("All agents run started");
            ProductionWorldConnection.prodWorld.runOneShotShiftManager();
            ProductionWorldConnection.prodWorld.runOneShotPlanner();
            ProductionWorldConnection.prodWorld.runOneShotJSM();
            ProductionWorldConnection.prodWorld.runOneShotScheduler();
            ProductionWorldConnection.prodWorld.runOneShotJSM();
            AgentFlashLog.getInstance().add("All agents run completed");
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by RunAll (run all agents).");
            }
        }
    }
    @Override
    public void runJSM() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to run JSM. "+ lockingAgent+" is running.");
                return;
            }
            log("JSM acquired agent lock.");
            lockingAgent="JSM";

            AgentFlashLog.getInstance().clear();
            ProductionWorldConnection.prodWorld.runOneShotJSM();
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by JSM.");
            }
        }
    }
    @Override
    public void runPlanner() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to run Planner. "+ lockingAgent+" is running.");
                return;
            }
            log("Planner acquired agent lock.");
            lockingAgent="Planner";
            AgentFlashLog.getInstance().clear();
            ProductionWorldConnection.prodWorld.runOneShotPlanner();
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by Planner.");
            }
        }
    }
    @Override
    public void runScheduler() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to run Scheduler. "+ lockingAgent+" is running.");
                return;
            }
            log("Scheduler acquired agent lock.");
            lockingAgent="Scheduler";
            AgentFlashLog.getInstance().clear();
            ProductionWorldConnection.prodWorld.runOneShotScheduler();
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by Scheduler.");
            }
        }
    }
    @Override
    public void runShiftManager() {
        try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to run ShiftManager. "+ lockingAgent+" is running.");
                return;
            }
            log("ShiftManager acquired agent lock.");
            lockingAgent="ShiftManager";
            AgentFlashLog.getInstance().clear();
            ProductionWorldConnection.prodWorld.runOneShotShiftManager();
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by ShiftManager.");
            }
        }
    }

    public static String getLockingAgent() {return lockingAgent;}

    /**
     * Run the JobStateManager once and persist changes.
     *
     * @return Number of jobs processed (not necessarily the number that changed state)
     */
    private Integer runOneShotJSM() {
        AgentFlashLog.getInstance().add("JSM started");
        validate();
        log("Preparing data for JSM");
        Collection<Job> newList;
        try {
            long start = System.currentTimeMillis();
            log("...loading non-final jobs");
            Collection<Job> jobs = WorldDAO.getInstance().getNonFinalJobs();
            long end = System.currentTimeMillis();
            log("...loaded "+jobs.size()+" jobs in "+ (end-start)+ " milliseconds.");

            log("Data ready. Invoking JSM");
            newList = jsm.run(jobs);

            // Post-process to set jobStatusRef
            start = System.currentTimeMillis();
            log("Update job status");
            int count = updateStatus(newList);
            end = System.currentTimeMillis();
            log("Jobs updated. "+count+" jobs changed state in "+ (end-start) +" milliseconds.");
        } catch (Exception e) {
            throw new AgentException("Error running Job State Manager: "+ e,e);
        }
        log("JSM run completed");
        AgentFlashLog.getInstance().add("JSM run completed");
        return newList.size();
    }
    private int updateStatus(Collection<Job> jobs) throws DAOException {
        List<JSMJob> jsmJobs = new ArrayList<JSMJob>();
        int count = 0;
        for (Job job : jobs) {
        	if (job.getIsChanged()) {
	            try {
	                JobStatusRef jsr = JobStatusRef.getRef(job.getStatus());
	                if (jsr==null) {
	                    log("Could not find job status for job "+ job.getId() +
	                        ". Tried looking up status '"+ job.getStatus() +"'.");
	                    continue;
	                }
	                JSMJob jsmjob = new JSMJob();
	                jsmjob.setId(job.getId());
	                jsmjob.setStatusId(jsr.getId());
	                jsmJobs.add(jsmjob);
	                count++;
	            } catch (Exception e) {
	                log("Error updating job status", e);
	            }
        	}
        }
        WorldDAO.getInstance().batchUpdatePaginated(jsmJobs);
        return count;
    }

    /**
     * Run the planner for today
     *
     * @return The number of jobs planned
     */
    private Integer runOneShotPlanner() {
        AgentFlashLog.getInstance().add("Planner started");
        log("Preparing data for Planner");
        int numJobs = 0;
        try {
            long start = System.currentTimeMillis();
            log("...loading active tasks");
            List<TaskView> activeTasks = WorldDAO.getInstance().getActiveTasks();
            long end = System.currentTimeMillis();
            log("...loaded "+activeTasks.size() +" active tasks in "+ (end-start) +" milliseconds.");

            log("Data ready. Invoking Planner");
            Collection<Job> jobs = planner.run(activeTasks);

            numJobs=jobs.size();
            log("Number of jobs created = "+numJobs);
            if (numJobs>0) {
                Collection<TaskView> plannedTasks = planner.getTasksPlannedLastRun();

                // Post-process to set jobStatusRef, jobTypeRef, priorityRef, and skillLevelRef
                for (Job job:jobs) {
                    job.setStatusRef(JobStatusRef.getRef(job.getStatus()));
                    if (job.getJobType()!=null)
                        job.setJobTypeId(
                                org.mlink.iwm.lookup.TaskTypeRef.getIdByCode(job.getJobType()));
                    job.setPriorityRef(
                            org.mlink.agent.model.PriorityRef.getRef(""+job.getPriority()));
                    job.setSkillLevelRef(
                            org.mlink.agent.model.SkillLevelRef.getRef(""+job.getSkillLevel()));
                    job.setScheduleResponsibilityRef(ScheduleResponsibilityRef.getRef(ScheduleResponsibilityRef.SYSTEM));
                }

                // Save results of one shot run
                start = System.currentTimeMillis();
                log("Saving new jobs");
                WorldDAO.getInstance().batchSavePaginated(jobs);
                end = System.currentTimeMillis();
                log("Jobs saved in "+ (end-start) +" milliseconds");
                log("Update last planned date on "+ plannedTasks.size() +" tasks");
                start = System.currentTimeMillis();
                WorldDAO.getInstance().batchUpdatePlannedTasks(plannedTasks);
                end = System.currentTimeMillis();
                log("Updated in "+ (end-start) +" milliseconds");
        		Collection<TaskView> meteredTasks = new ArrayList<TaskView>();
                for (TaskView tv:plannedTasks) {
	    			if (tv.getThreshold()!=null && tv.getThreshold()>0 && !meteredTasks.contains(tv)) {
	    				meteredTasks.add(tv);
	    			}
                }
        		// process metered tasks
                log("Update run hours threshold on "+ meteredTasks.size() +" tasks");
                start = System.currentTimeMillis();
        		WorldDAO.getInstance().updateRunHoursThreshold(meteredTasks);
                end = System.currentTimeMillis();
                log("Updated in "+ (end-start) +" milliseconds");
            }
        } catch (Exception e) {
            throw new AgentException("Error running Planner: "+ e,e);
        }
        log("Planner run completed");
        AgentFlashLog.getInstance().add("Planner run completed");
        return numJobs;
    }

    /**
     * Run the scheduler for today
     *
     * @throws HibernateException
     *
     */
    private void runOneShotScheduler() {
        AgentFlashLog.getInstance().add("Scheduler started");
        log("Preparing data for Scheduler");
        Map<Long,Set<ScheduleWrapper>> schedules4Location = new HashMap<Long,Set<ScheduleWrapper>>();
        Map<Long,HashSet<Job>> rfsJobs4Location = new HashMap<Long,HashSet<Job>>();
        try {
            long start, end;
            List<Locator> locations = WorldDAO.getInstance().getLocations();
            for (Locator location:locations) {
                log("location: "+ location.getFullLocator() +" : "+ location.getId() );
                start=System.currentTimeMillis();
                log("...loading schedules");
                List<WorkSchedule> schedules = WorldDAO.getInstance().getSchedules(location.getId());
                Set<ScheduleWrapper> wrappers = new HashSet<ScheduleWrapper>();
                for (WorkSchedule schedule:schedules) {
                    ScheduleWrapper wrapper = new ScheduleWrapper();
                    wrapper.setWorkSchedule(schedule);
                    wrapper.setOrganizationId(schedule.getPerson().getOrganizationId());
                    // Copy skills from person to schedule wrapper
                    Set<Skill> skills = schedule.getPerson().getSkills();
                    for (Skill skill : skills){
                        wrapper.addSkillLevel(skill.getSkillTypeRef().getId(),
                                skill.getSkillLevelRef().getValue());
                    }
                    wrappers.add(wrapper);
                }
                end=System.currentTimeMillis();
                log("...loaded "+ wrappers.size() +" schedules in "+(end-start)+" milliseconds.");
                schedules4Location.put(location.getId(), wrappers);
                start=System.currentTimeMillis();
                log("...loading RFS jobs");
                List<Job> rfsJobs = WorldDAO.getInstance().getRFSJobs(location.getId());
                end=System.currentTimeMillis();
                log("...loaded "+ rfsJobs.size() +" RFS jobs in "+(end-start)+" milliseconds.");
                start=System.currentTimeMillis();
                log("...loading incomplete jobs");
                List<Job> incompleteJobs = WorldDAO.getInstance().getIncompletes(location.getId());
                end=System.currentTimeMillis();
                log("...loaded "+ incompleteJobs.size() +" incomplete jobs in "+(end-start)+" milliseconds.");

                if (rfsJobs4Location.get(location.getId())==null)
                    rfsJobs4Location.put(location.getId(), new HashSet<Job>());
                rfsJobs4Location.get(location.getId()).addAll(rfsJobs);
                rfsJobs4Location.get(location.getId()).addAll(incompleteJobs);
            }
            log("Data ready. Invoking Scheduler");
            List<Map> schedulesAndJobs = scheduler.run(schedules4Location,rfsJobs4Location);

            // Extract schedules and jobs
            Map<Long,Set<ScheduleWrapper>> changedSchedules = schedulesAndJobs.get(0);
            Map<Long,Set<Job>> unassignedJobs = schedulesAndJobs.get(1);

            // Post-process to save schedules
            start = System.currentTimeMillis();
            log("Saving schedules and jobs. Start: "+ start);
            for(Long lid:changedSchedules.keySet()) {
                log("location "+ lid);
                updateChangedSchedules(changedSchedules.get(lid));
                updateUnassignedJobs(unassignedJobs.get(lid));
            }
            end = System.currentTimeMillis();
            log("Schedules and jobs saved. End: "+ end +". Save took "+ (end-start) +" milliseconds.");

        } catch (Exception e) {
            throw new AgentException("Error running Scheduler: "+ e,e);
        }
        log("Scheduler run completed");
        AgentFlashLog.getInstance().add("Scheduler run completed");

    }
    private Set<ScheduleWrapper> cleanSchedulerData(Set<ScheduleWrapper> wrappers) {
    	
    	return wrappers;    	
    }
    private void updateChangedSchedules(Set<ScheduleWrapper> wrappers) throws DAOException {
        List<JobSchedule> jobSchedules = new ArrayList<JobSchedule>();
        List<SchedulerJob> jobs = new ArrayList<SchedulerJob>();
        List<Long>checkDupes = new ArrayList<Long>();
        int count = 0;
        for (ScheduleWrapper sw:wrappers)
            if (sw.isChanged()) {
            	count++;
                jobSchedules.addAll(sw.getWorkSchedule().getJobSchedules());
                for (Job job:sw.getJobs()) {
                	SchedulerJob sj = SchedulerJob.copy(job);
                    if (!checkDupes.contains(sj.getId())) { // remove duplicates due to multi-worker assignment
                    	jobs.add(sj);
                    	checkDupes.add(sj.getId());
                    }
                }
            }
        if (jobSchedules.size()>0) {
            log("..."+ count +" schedules changed. Saving...");
        	WorldDAO.getInstance().batchSaveOrUpdatePaginated(jobSchedules);
        } else if (count==0) log("...no schedules changed");
        if (jobs.size()>0) WorldDAO.getInstance().batchUpdatePaginated(jobs);
    }
    private void updateUnassignedJobs(Set<Job> unassignedJobs) throws DAOException {
        List<JobSchedule> jobSchedules = new ArrayList<JobSchedule>();
        List<SchedulerJob> jobs = new ArrayList<SchedulerJob>();
        for (Job job:unassignedJobs){
            if (job.getIsChanged()) {
            	SchedulerJob sj = SchedulerJob.copy(job);
                jobs.add(sj);
            	// Update of latestJobSchedule, if any, since deleted time must have
            	// been set inside the Scheduler
            	if (job.getLatestJobSchedule()!=null)
            		jobSchedules.add(job.getLatestJobSchedule());
            }
        }
        if (jobSchedules.size()>0) WorldDAO.getInstance().batchSaveOrUpdatePaginated(jobSchedules);
        if (jobs.size()>0) {
            log("..."+jobs.size()+" unassigned jobs. Updating...");
            WorldDAO.getInstance().batchUpdatePaginated(jobs);
        } else log ("no unassigned jobs");
    }

    /**
     * Run the shiftmanager for today
     *
     * @return The number of schedules updated
     */
    private Integer runOneShotShiftManager() {
        AgentFlashLog.getInstance().add("Shift Manager started");
        log("Preparing data for ShiftManager");
        int i = 0;
        try {
            log("...loading schedules");
            List<WorkSchedule> ip = WorldDAO.getInstance().getSchedulesIPNYS();
            log("...loaded "+ ip.size() +" in-progress or not-yet-started schedules");
            log("Data ready. Invoking ShiftManager");
            Collection<WorkSchedule> mods = shiftMgr.run(ip);
            i = mods.size();
            log("Number of schedules that changed status = "+i);
            if (i>0) {
                long start = System.currentTimeMillis();
                log("Saving schedules. Start: "+ start);
                updateShiftsDone(mods);
                long end = System.currentTimeMillis();
                log("Schedules saved. End: "+ end +". Save took "+ (end-start) +" milliseconds.");
            }
        } catch (Exception e) {
            throw new AgentException("Error running Shift Manager: "+ e,e);
        }
        log("ShiftManager run completed");
        AgentFlashLog.getInstance().add("ShiftManager run completed");
        return i;
    }
    private void updateShiftsDone(Collection<WorkSchedule> schedules) throws DAOException {
    	WorkScheduleStatusRef status = WorkScheduleStatusRef.getRef(WorkScheduleStatusRef.Status.DUN.toString());
    	List<ShiftMgrJob> jobs = new ArrayList<ShiftMgrJob>();
    	List<Long> processedJobs = new ArrayList<Long>();
    	for (WorkSchedule schedule:schedules) {
    		schedule.setStatusRef(status);
    		Set<JobSchedule> jobSchedules = schedule.getJobSchedules();
    		for (JobSchedule js:jobSchedules) {
    			if (!processedJobs.contains(js.getJob().getId())) {
	    			jobs.add(ShiftMgrJob.copy(js.getJob()));
	    			processedJobs.add(js.getJob().getId());
    			}
    		}
    	}
    	WorldDAO.getInstance().batchUpdatePaginated(schedules);
    	WorldDAO.getInstance().batchUpdatePaginated(jobs);
    }

    @Override
    public Collection<Job> runJSM(Collection jobs) {
        // TODO This should be more robust
    	//return jsm.run(jobs);
    	Collection<Job> jobsOp=null;
    	try {
            if (!lock.tryLock(3L, TimeUnit.SECONDS)) {
                log("Could not acquire lock to run JSM. "+ lockingAgent+" is running.");
                return jobsOp;
            }
            log("JSM acquired agent lock.");
            lockingAgent="JSM";

            AgentFlashLog.getInstance().clear();
            jobsOp = ProductionWorldConnection.prodWorld.runOneShotJSM(jobs);
        } catch (Exception e) {
            throw new IWMException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lockingAgent="";
                lock.unlock();
                log("Released agent lock held by JSM.");
            }
        }
        return jobsOp;
    }
    
    /**
     * Run the JobStateManager once and persist changes.
     *
     * @return Number of jobs processed (not necessarily the number that changed state)
     */
    private Collection<Job> runOneShotJSM(Collection jobs) {
        AgentFlashLog.getInstance().add("JSM started");
        validate();
        log("Preparing data for JSM");
        Collection<Job> newList;
        try {
            long start = System.currentTimeMillis();
            log("...loading non-final jobs");
            long end = System.currentTimeMillis();
            log("...loaded "+jobs.size()+" jobs in "+ (end-start)+ " milliseconds.");

            log("Data ready. Invoking JSM");
            newList = jsm.run(jobs);

            // Post-process to set jobStatusRef
            start = System.currentTimeMillis();
            log("Update job status");
            int count = updateStatus(newList);
            end = System.currentTimeMillis();
            log("Jobs updated. "+count+" jobs changed state in "+ (end-start) +" milliseconds.");
        } catch (Exception e) {
            throw new AgentException("Error running Job State Manager: "+ e,e);
        }
        log("JSM run completed");
        AgentFlashLog.getInstance().add("JSM run completed");
        return newList;
    }

    @Override
    public Collection<Job> runPlanner(Collection<TaskView> c, Timestamp day) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection runScheduler(Collection jobs, Collection workschedules, Timestamp t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection runShiftManager(Collection schedules, Timestamp t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void log(Object o) {super.log(world.getName() +":"+ o);}
    public void log(Object o, Throwable t) {super.log(o,t);}
}
