package org.mlink.agent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.TaskView;
import org.mlink.agent.model.WorkObject;
import org.mlink.agent.model.WorkSchedule;
import org.mlink.agent.model.World;

public abstract class WorldConnection {
	private static final Logger logger = Logger.getLogger(WorldConnection.class);

	private static final Integer ZERO = new Integer(0);
	private static final Integer ONE  = new Integer(1);
	
	private static HashMap<String, Integer> mCheckedOut = new HashMap<String, Integer>();
	private static HashMap<String, Integer> mInstances = new HashMap<String, Integer>();
	private static Random mRandom = new Random();
	private static List<WorkObject> mWorkObjects;
	private Integer instance;
	private Integer token;
	protected World world;
	protected JobStateManager jsm;
	protected Planner planner;
	protected Scheduler scheduler;
	protected ShiftManager shiftMgr;
	protected Long nextJobId = new Long(0);
	protected Collection<Job> jobs = new ArrayList<Job>();
	private HashMap<Timestamp,List<WorkSchedule>> schedules = new HashMap<Timestamp,List<WorkSchedule>>();
	private Collection<TaskView> tasks = new ArrayList<TaskView>();



	public WorldConnection() {super();}
	
	public Long    getId() {return this.world.getId();}
	public String  getName() {return this.world.getName();}
	public String  getParent() {return this.world.getParent();}
	public String  getSchema() {return this.world.getSchema();}
	public Integer getToken() {return token;}
	public long    getCurrentRuntime() {return 0;}
	public Integer getInstanceNumber() {return instance;}
	
	private void setToken(Integer i) {token=i;}
	private void setInstanceNumber(Integer i) {
		validate();
		instance = i;
	}

	/**
	 * Roll over to the next calendar day. Used during simulation to advance the
	 * clock used during the agent runs to allow the agent to run on sequential
	 * days.
	 * 
	 */
	protected void validate() throws InvalidAccessException {
		if (token==null || !token.equals(mCheckedOut.get(world.getSchema())))
			throw new InvalidAccessException("Instance no longer valid.");
	}

	protected HashMap<String, Integer> getMap() {return mCheckedOut;}
	
	/**
	 * Return the current number of states in the state machine
	 * @return Number of states
	 */
	public int getStateCount() {return jsm.getStateCount();}
	
	/** 
	 * Create a xml string of current state machine used by the Job State Manager
	 * @return String (xml) representing the current state machine
	 */
	public String printStateMachine() {return jsm.printStateMachine();}
		
	/** 
	 * Reload current state machine used by the Job State Manager
	 * @throws Exception 
	 */
	public void reloadStateMachine() throws Exception {jsm.reloadStateMachine();}
	
	/**
	 * Export a world into its IWML representation.
	 * 
	 * @return A string representing a world in IWML format
	 * @throws InvalidAccessException
	 */
	public String exportWorld() {
		validate();
		return null;
	}

	/**
	 * Import a world from its IWML representation contained in the specified
	 * String.
	 * 
	 * @param s
	 *            The string containing the IWM-ized world
	 * @throws InvalidAccessException
	 */
	public void importWorld(String s) {validate();}

	/**
	 * Return all objects (things that require work, and as such have tasking
	 * attached) in the current world.
	 * 
	 * @return All objects in the current world.
	 */
	public List<WorkObject> getActiveWorkObjects() throws DAOException {
		if (null == mWorkObjects) {
			mWorkObjects = WorldDAO.getInstance().getActiveWorkObjects();
		}
		return mWorkObjects;
	}

	/**
	 * Run the entire suite of agents. Execution order: 1. ShiftManager 2.
	 * Planner 3. Job State Manager 4. Scheduler 5. Job State Manager
	 * 
	 */
	public abstract void runAll();


    public abstract void runAllButPlanner();

	/**
	 * Run a single invocation of the JobStateManager. This method should supply its own
	 * data internally - either through database access or keeping some other in-memory
	 * storage of jobs.
	 * 
	 */
	public abstract void runJSM();

	/**
	 * Run the JobStateManager on the specified collection of jobs.
	 * 
	 * @param jobs Jobs to process for state changes
	 * @return Collection of jobs
	 */
	public abstract Collection<Job> runJSM(Collection jobs);
	
	/**
	 * Run a single invocation of the Planner. This method should keep track of its own
	 * data internally - either through database access or keeping some other in-memory
	 * storage of jobs.
	 * 
	 */
	public abstract void runPlanner();
	
	/**
	 * Run the planner on the specified collection of tasks for the specified day timestamp
	 * 
	 * @param c  Tasks to examine for job creation
	 * @param day The active days timestamp
	 * @return Collection of newly-created jobs
	 */
	public abstract Collection<Job> runPlanner(Collection<TaskView> c, Timestamp day);

	/**
	 * Run the planner on the specified collection of tasks for today
	 * 
	 * @param tasks  Tasks to examine for job creation
	 * @return Collection of newly-created jobs
	 */
	public Collection<Job> runPlanner(Collection<TaskView> tasks) {
		return runPlanner(tasks, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Run a single invocation of the Scheduler. This method should keep track of its own
	 * data internally - either through database access or keeping some other in-memory
	 * storage of jobs.
	 * 
	 */
	public abstract void runScheduler();

	/**
	 * Run the scheduler on the specified collection of jobs which are ready for
	 * scheduling, and the specified collection of work schedules, for the specified day.
	 * 
	 * @param jobs  Jobs ready for scheduling
	 * @param workschedules Work schedules for today, on which to schedule jobs
	 * @param t The active day
	 * @return Collection of job schedules
	 */
	public abstract Collection runScheduler(Collection jobs, Collection workschedules, Timestamp t);

	/**
	 * Run the scheduler on the specified collection of jobs which are ready for
	 * scheduling, and the specified collection of work schedules, for today
	 * 
	 * @param jobs  Jobs ready for scheduling
	 * @param workschedules Work schedules for today, on which to schedule jobs
	 * @return Collection of job schedules
	 */
	public Collection runScheduler(Collection jobs, Collection workschedules) {
		return runScheduler(jobs,workschedules,new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Run a single invocation of the ShiftManager. This method should keep track of its own
	 * data internally - either through database access or keeping some other in-memory
	 * storage of jobs.
	 * 
	 */
	public abstract void runShiftManager();
	/**
	 * Run the shift manager on the specified collection of schedules for the
	 *  specified day.
	 *  
	 * @param schedules Schedules to examine for shift eligibility
	 * @param t The active day
	 * @return Collection schedules (some may be activated)
	 */
	public abstract Collection runShiftManager(Collection schedules, Timestamp t);

	/**
	 * Run the shift manager on the specified collection of schedules for
	 * today.
	 * 
	 * @param schedules Schedules to examine for shift eligibility
	 * @return Collection schedules (some may be activated)
	 */
	public Collection runShiftManager(Collection schedules) {
		return runShiftManager(schedules, new Timestamp(System.currentTimeMillis()));
	}


	/**
	 * Prepares world to be used for agent runs. Checkout locks the run methods
	 * against invocation by other entities with a handle to this world. Use
	 * checkin() to unlock the world.
	 * 
	 * @param world The world name
	 * @param isreal  Whether the world is real or simulated
	 * @return True if the world was successfully checked out. False otherwise
	 * @throws DAOException
	 * @see checkin
	 */
	protected static WorldConnection checkout(World world,boolean isreal) throws DAOException, InvalidAccessException, Exception {
		if (world == null) throw new InvalidAccessException("Cannot check out null world");
		
		WorldConnection wxc = null;
		logger.debug("Testing if world previously checked out");
		Object o = mCheckedOut.get(world.getSchema());
		if (null == o) { // no entry for this world
			logger.debug("Checking out world "+ world);
			// Activate world's schema
			WorldDAO.getInstance().activateWorld(world.getSchema());
			wxc = WorldConnectionFactory.getConnection(world, isreal);
			int ran = mRandom.nextInt();
			wxc.setToken(new Integer(ran));
			mCheckedOut.put(world.getSchema(),wxc.getToken()); // mark world
	
			wxc.instance = ONE; // first locked instance -- for keeping track of
								// instances in simulation
			mInstances.put(world.getName(), wxc.instance);

			logger.debug("Got world connection: "+ wxc +"; token: "+wxc.getToken());
			logger.debug("Recorded token is "+ mCheckedOut.get(wxc.world.getSchema()));
		} else
			throw new InvalidAccessException("World " + world.getName()
					+ " already checked out.");
		return wxc;
	}

	private static synchronized Integer nextInstance(String world) {
		Integer inst = (Integer) mInstances.get(world);
		inst = new Integer(inst.intValue() + 1);
		mInstances.put(world, inst);
		return inst;
	}

	/**
	 * Returns another agent-enabled instance of the specified world, with
	 * initial world data. This operation is only allowed if the world has been
	 * checked out by the invoking entity.
	 * 
	 *            The world to duplicate
	 * @return Another instance of the specified world
	 * @throws DAOException
	 */
	public WorldConnection duplicate() throws DAOException, InvalidAccessException, Exception {
		validate();
		WorldConnection x = null;
	
		Object o = mCheckedOut.get(this.getName());
		if (null == o) { // no entry for this world
			throw new InvalidAccessException("Instance no longer valid.");
		} else if (o.equals(this.getToken())) { // valid operation
			x = loadWorld(this.world, nextInstance(this.getName()));
			// TODO: copy data to new WorldConnection
		} else { // invalid operation
			throw new InvalidAccessException("Instance no longer valid.");
		}
		return x;
	}

	private static WorldConnection loadWorld(World world, Integer instance) throws DAOException, Exception {
		WorldConnection x = new SimWorldConnection(world);
		x.setInstanceNumber(instance);
		return x;
	}

	/**
	 * Releases the lock on this world.
	 * 
	 * @throws InvalidAccessException
	 * @throws DAOException
	 */
	public void checkin() throws DAOException, InvalidAccessException {
		validate();
		Object o = mCheckedOut.get(this.getName());
		if (null == o) { // no entry for this world
			return;
		} else if (o.equals(this.getToken())) { // valid operation
			mCheckedOut.put(this.getName(), null);
			mInstances.put(this.getName(), ZERO);
			// update to remove token
		} else { // invalid operation
			throw new InvalidAccessException("Instance no longer valid.");
		}
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (null == obj || this.getClass() != obj.getClass())
			return false;
		WorldConnection x = (WorldConnection) obj;
		return (this.instance.equals(x.instance) && this.world.equals(x.world));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + instance.hashCode();
		hash = 31 * hash + world.hashCode();
		return hash;
	}

	protected void log(Object o) {logger.debug(o);}
	protected void log(Object o,Throwable t) {logger.error(o,t);}
}