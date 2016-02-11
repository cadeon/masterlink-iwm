package org.mlink.agent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
	
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.model.Action;
import org.mlink.agent.util.CreateJobHelper;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobAction;
import org.mlink.agent.model.JobSpec;
import org.mlink.agent.model.JobTask;
import org.mlink.agent.model.PlannerTask;
import org.mlink.agent.model.Project;
import org.mlink.agent.model.ProjectSpec;
import org.mlink.agent.model.TaskView;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.util.DateUtil;

public class Planner extends BaseAgent {

	private static final int SEASON = 0;
	private static final int MONTH  = 1;
	private static final int DAY    = 2;
	
	private static final int SPRING = 0;
	private static final int SUMMER = 1;
	private static final int FALL   = 2;
	private static final int WINTER = 3;

	private static final int JAN = 4;
	private static final int FEB = 5;
	private static final int MAR = 6;
	private static final int APR = 7;
	private static final int MAY = 8;
	private static final int JUN = 9;
	private static final int JUL = 10;
	private static final int AUG = 11;
	private static final int SEP = 12;
	private static final int OCT = 13;
	private static final int NOV = 14;
	private static final int DEC = 15;	
	
	private static final int MON = 16;
	private static final int TUE = 17;
	private static final int WED = 18;
	private static final int THU = 19;
	private static final int FRI = 20;
	private static final int SAT = 21;
	private static final int SUN = 22;
	
	
	private Calendar todayCal; 
	private int[] today = new int[3]; // captures the indexes into the plan array that represent today
	
	Collection<Job> jobs = new ArrayList<Job>();
	Map<Long,JobSpec> jobSpecs  = new HashMap<Long,JobSpec>(); // job spec id, job spec
    Map<Long,JobSpec> scheduleGroups = new HashMap<Long,JobSpec>(); // schedule group id, job spec
    Map<Long,ProjectSpec> projectGroups  = new HashMap<Long,ProjectSpec>(); // project spec id, project spec
    Collection<Long> processedTasks = new ArrayList<Long>(); // Collect TaskView ids already processed
    Collection<TaskView> plannedTasks = new ArrayList<TaskView>(); // Collect Tasks for which jobs have been created
                                         
    Long nextJobId = 0L;
    Long nextProjectId = 0L;
    
    public Planner() {super("Planner");}   
    public Planner(String name) throws Exception {
		super(name);
	}
    
	/**
	 * Runs the Planner algorithm:
	 * 1. Examine tasks
	 * 2. Determine what tasks need to be done for the today
	 * 3. Examine precedes for selected tasks
	 * 4. Build jobs for tasks
	 * 
	 * @param c The collection of tasks to examine
	 * @returns The jobs created as a result of the run
	 */
    @Override
	public Collection<Job> run(Collection c) throws BusinessException, DAOException {
		Timestamp today = new java.sql.Timestamp(System.currentTimeMillis());
		return run(c,today);
	}
	/**
	 * Runs the Planner algorithm:
	 * 1. Examine tasks
	 * 2. Determine what tasks need to be done for the specified day
	 * 3. Examine precedes for selected tasks
	 * 4. Build jobs for tasks
	 * 
	 * @param c The collection of tasks to examine
	 * @param day The specified day to plan
	 * @returns The jobs created as a result of the run
	 */
	public Collection<Job> run(Collection<TaskView> tasks,Timestamp day) throws DAOException, BusinessException{
		log("Running planner on "+ tasks.size() +" tasks, "+ DateUtil.displayShortDateTime(day));
		
		long startTime = System.currentTimeMillis();
		log("Start time: "+ startTime);
		cleanCollections();
		if (tasks==null||tasks.size()<1) {
			log("Planner run completed -- No tasks to process");
			return jobs; // empty collection
		}
	    decodeDay(day);
	    // Build task lists
		for (TaskView taskview:tasks) {
			if (hasBeenProcessed(taskview)) {
				log("...task "+ taskview +" already processed");
				continue;
			}
			if (isReady(taskview)) processTask(taskview);
		}
		int jobSpecsSize = jobSpecs.size();
		// Job specs that are created as part of a project can be removed
		// from the general list of job specs
		Collection<JobSpec> remove = new ArrayList<JobSpec>();
		// TODO: Creation of projects tabled for further discussion
		/* Collection<Project> projects = new ArrayList<Project>();
		for (ProjectSpec ps:projectGroups.values()) {
			if (ps.isReady()) {
				Project p = Project.copyProperties(ps);
				Set<TaskView> pTasks = ps.getTasks();
				Set<JobSpec> jspecs = new HashSet<JobSpec>();
				// Turn tasks into jobspecs (handling schedule groups, etc)
				for (TaskView t:pTasks) {
					JobSpec js = createJobSpec(t);
					jspecs.add(js);
				}
				// Turn jobspecs into jobs
				Set<Job> projectJobs = new HashSet<Job>();
				for (JobSpec seed:jspecs)  {
					try {
						Job j = createJob(seed);
						j.setProject(p);
						projectJobs.add(j);
					} catch (BusinessException be) { 
						// catch any exceptions thrown while creating a specific job
						log("Error during job creation:"+ be.getMessage());
					}
					remove.add(seed);
				}
				p.setJobs(projectJobs);
				projects.add(p);
				jobs.addAll(projectJobs);
			}
		}
		jobSpecs.removeAll(remove);
		remove.clear(); */
		// Remove any jobs removable
		Collection<JobSpec> entries = jobSpecs.values();
		for (JobSpec js:entries) {
			if (js.getTasks().size()==0) {
				log("Removing job spec "+ js.getId() +": No tasks");
				remove.add(js);
			}
		}
		removeSpecs(remove);
		remove.clear();
		// Create stand-alone jobs
		entries = jobSpecs.values();
		for (JobSpec js:entries) {
			try {
			Job j = createJob(js);
			jobs.add(j);
			} catch (BusinessException be) {
				 // catch any exceptions thrown while creating a specific job
				log("Error during job creation:"+ be.getMessage());
			}
			remove.add(js);
		}
		removeSpecs(remove);
		// return
		long endTime = System.currentTimeMillis();
		log("End: "+ endTime +". Planner run took "+ (endTime-startTime) +" milliseconds.");
		log("Planner run complete");
		log("   Job specs : "+ jobSpecsSize);
	    // TODO: Creation of projects tabled for further discussion
		//log("   Project specs : "+ projectGroups.size());
		log("   Schedule groups: "+ scheduleGroups.size());
		log("   Jobs : "+ jobs.size() );
		return jobs;
	}
	
	private void removeSpecs(Collection<JobSpec> specs) {
		for (JobSpec rm:specs) {
			jobSpecs.remove(rm.getId());
		}
	}
	public Collection<TaskView> getTasksPlannedLastRun() {
		return plannedTasks;
	}
	
	// modifiers for list of processed tasks
	private void addToProcessed(TaskView taskview) {processedTasks.add(taskview.getId());}
	private boolean hasBeenProcessed(TaskView taskview) {return processedTasks.contains(taskview.getId());}
	
	private void cleanCollections() {	
		jobs           = new ArrayList<Job>();
		jobSpecs       = new HashMap<Long,JobSpec>();
    	scheduleGroups = new HashMap<Long,JobSpec>(); 
        // TODO: Creation of projects tabled for further discussion
    	//projectGroups  = new HashMap<Long,ProjectSpec>();
    	processedTasks = new ArrayList<Long>(); 
    	plannedTasks   = new ArrayList<TaskView>();
	}
	
	private boolean isReady(TaskView t) {
		if (!isPlannable(t)) {
			log(" task "+ t.getId() +" not planned due to task plan");
			return false;
		} 
		if (isExpired(t)) {
			log(" task "+ t.getId() +" planned due to frequency");
			return true;
		}
		if (isOverRunHours(t)) {
			log(" task "+ t.getId() +" planned due to run hours");
			return true;
		}
		log(" task "+ t.getId() +" not planned due to frequency/run hours");
		return false;
	}
	private boolean isPlannable(TaskView t) {
		String s = t.getPlan();
        if(s==null) return false;
        int[] plan = decodePlan(s);
        try {
            if (isInPlan(today,plan)) return true;
            return false;
        } catch (Exception e){
            error("Invalid Plan Data format:"+ e.getMessage());
            return false;
        }
	}
    public boolean isExpired(TaskView t) {
		log("  lastplanned: "+ t.getLastPlanned());
		log("  months:      "+ t.getMonths());
		log("  days:        "+ t.getDays());
		
		if ( t.getMonths()==null && t.getDays()==null ) {
			log(" no months or days");
			return false;
		}
		if ( t.getLastPlanned()==null ) {
			log(" no last planned");
			return true;
		};
	    Calendar threshold = Calendar.getInstance();
	    threshold.setTime(new Date(t.getLastPlanned().getTime()));
	    threshold.add(Calendar.MONTH,(t.getMonths()!=null?t.getMonths().intValue():0));
	    int days = 0;
	    if (t.getDays()!=null) days = t.getDays().intValue();
	    threshold.add(Calendar.DATE,days);
		
		int res = threshold.compareTo(todayCal); 
		if (res < 0 ){
			log(" today after threshold");
			return true;
		} else if (res == 0) {
			log(" today is threshold");
			return true;
		} else  {
			log(" today before threshold");
			return false;
		} 
	}
    private boolean isOverRunHours(TaskView t) {
    	if (t.getRunHours()==null || t.getThreshold()==null) return false;
    	log(" run hours: "+ t.getRunHours());
    	log(" threshold: "+ t.getThreshold());
		if (t.getRunHours() > t.getThreshold()) {
			log(" task is over run hours");
			return true;
		} 
		return false;
    }
	
	private void processTask (TaskView taskView) throws DAOException {
		log("processing task "+ taskView);
	    // TODO: Creation of projects tabled for further discussion
		/*if (taskView.getSequenceGroupId()!=null &&
			taskView.getSequenceLevel()!= null) {
			log("task is part of a project");
				// Note: We assume that circular references were checked for during sequence creation
				addToProjectSpec(taskView);
		} */
		createJobSpec(taskView);
		addToProcessed(taskView);
	}
	private JobSpec createJobSpec(TaskView t) {
		JobSpec js = null;
		if (t.getScheduleGroup()!=null &&
			scheduleGroups.containsKey(t.getScheduleGroup()))  {
			log("task is part of a schedule group");
			js = scheduleGroups.get(t.getScheduleGroup());
		}
		else { // create new JobSpec
			log("creating new job spec");
			js = new JobSpec();
			js.setId(nextJobId++);
			if (t.getScheduleGroup()!=null) scheduleGroups.put(t.getScheduleGroup(),js);
		}
		if (js!=null) {
			js.addTask(t);
			jobSpecs.put(js.getId(), js);
		}
		return js;
	}		

    // TODO: Creation of projects tabled for further discussion
	/* private void addToProjectSpec(TaskView t) {
		ProjectSpec ps = null;
		if (projectGroups.containsKey(t.getSequenceGroupId()))  {  // add to ProjectSpec 
			log ("already created project spec for this group");
			ps = (ProjectSpec)projectGroups.get(t.getSequenceGroupId());
		}
		else  {// create new ProjectSpec
			log("creating new project spec");
			ps = new ProjectSpec();
			ps.setId(t.getSequenceGroupId());
			ps.setCreatedBy("Planner");
			ps.setCreatedDate(new Timestamp(new Date().getTime()));
			projectGroups.put(t.getSequenceGroupId(),ps);
		}
		ps.add(t);
	}*/

	private Job createJob(JobSpec js) throws DAOException, BusinessException {
		
		List<TaskView> tasks = js.getTaskViews();
		Job job = (CreateJobHelper.buildJobPrototype(tasks));
		job.setCreatedBy("Planner");
		job.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		job.setSticky(false);

		Set<JobTask> jobTasks = new HashSet<JobTask>();
		for (TaskView tv:tasks) {
			JobTask jobTask = JobTask.copyProperties(tv); 
			jobTask.setTaskId(tv.getId());
			jobTask.setJob(job);
			jobTasks.add(jobTask);
			if (!plannedTasks.contains(tv)) plannedTasks.add(tv);
			
			Set<JobAction> jobActions = new HashSet<JobAction>();
			Set<Action> actions = tv.getActions(); 
			for (Action action:actions) {
				JobAction jobAction = JobAction.copyProperties(action);				
				jobAction.setAction(action);
				jobAction.setJobTask(jobTask);
				
				jobActions.add(jobAction);
			}
			jobTask.setJobActions(jobActions);
			
		}
		job.setJobTasks(jobTasks);
		return job;
	}
	
    /** 
     * Extracts the planning array from the specified string in the following order (starting from index 0):
     * 0. spring	4. january		8. may		12. september	16. monday		20. friday
     * 1. summer	5. february		9. june		13. october		17. tuesday		21. saturday
     * 2. fall		6. march		10. july	14. november	18. wednesday	22. sunday
     * 3. winter	7. april		11. august	15. december	19. thursday
     * 
     * @param s The String containing the task plan
     * @return An integer array of 0s and 1s, the 1s denoting which elements have been selected.
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
	private int[] decodePlan(String s) throws IllegalArgumentException, NumberFormatException {
		log("Decoding plan: "+ s);
		int[] plan = new int[23];
		int i = 0;
        if (s.length()>=23) {
            plan[i]=Integer.parseInt(s.substring(i,++i)); // spring   - 0
            plan[i]=Integer.parseInt(s.substring(i,++i)); // summer   - 1
            plan[i]=Integer.parseInt(s.substring(i,++i)); // fall     - 2
            plan[i]=Integer.parseInt(s.substring(i,++i)); // winter   - 3
            plan[i]=Integer.parseInt(s.substring(i,++i)); // january  - 4
            plan[i]=Integer.parseInt(s.substring(i,++i)); // february - 5
            plan[i]=Integer.parseInt(s.substring(i,++i)); // march    - 6
            plan[i]=Integer.parseInt(s.substring(i,++i)); // april    - 7
            plan[i]=Integer.parseInt(s.substring(i,++i)); // may      - 8
            plan[i]=Integer.parseInt(s.substring(i,++i)); // june     - 9
            plan[i]=Integer.parseInt(s.substring(i,++i)); // july     - 10
            plan[i]=Integer.parseInt(s.substring(i,++i)); // august   - 11
            plan[i]=Integer.parseInt(s.substring(i,++i)); // september- 12
            plan[i]=Integer.parseInt(s.substring(i,++i)); // october  - 13
            plan[i]=Integer.parseInt(s.substring(i,++i)); // november - 14
            plan[i]=Integer.parseInt(s.substring(i,++i)); // december - 15
            plan[i]=Integer.parseInt(s.substring(i,++i)); // monday   - 16
            plan[i]=Integer.parseInt(s.substring(i,++i)); // tuesday  - 17
            plan[i]=Integer.parseInt(s.substring(i,++i)); // wednesday- 18
            plan[i]=Integer.parseInt(s.substring(i,++i)); // thursday - 19
            plan[i]=Integer.parseInt(s.substring(i,++i)); // friday   - 20
            plan[i]=Integer.parseInt(s.substring(i,++i)); // saturday - 21
            plan[i]=Integer.parseInt(s.substring(i,++i)); // sunday   - 22
        } else {
        	throw new IllegalArgumentException("Incorrect number of elements in plan."+
        			" Expected 23 (but accept >= 23), got "+ s.length());
        }
        return plan;
	}
	private void decodeDay(Timestamp day) {
		today = new int[3];
        today[2] = -999;
        today[1] = -999;
        today[0] = -999;
        // Calendar must be initialized to day passed in
        todayCal = Calendar.getInstance();todayCal.setTime(day);
        
        int dayOfWeek = todayCal.get(Calendar.DAY_OF_WEEK);
        // We differ from the Java Calendar constants by starting on Monday, 
        // rather than Sunday so we have to adjust by using our own day-of-week constants
        if (dayOfWeek==Calendar.SUNDAY)        {today[2]=Planner.SUN;}
        else if (dayOfWeek==Calendar.SATURDAY) {today[2]=Planner.SAT;}
        else if (dayOfWeek==Calendar.FRIDAY)   {today[2]=Planner.FRI;}
        else if (dayOfWeek==Calendar.THURSDAY) {today[2]=Planner.THU;}
        else if (dayOfWeek==Calendar.WEDNESDAY){today[2]=Planner.WED;}
        else if (dayOfWeek==Calendar.TUESDAY)  {today[2]=Planner.TUE;}
        else if (dayOfWeek==Calendar.MONDAY)   {today[2]=Planner.MON;}
        
        int month = todayCal.get(Calendar.MONTH);
        if (month==Calendar.DECEMBER) {today[1]=Planner.DEC; today[0]=Planner.WINTER;}
        if (month==Calendar.NOVEMBER) {today[1]=Planner.NOV; today[0]=Planner.FALL;}
        if (month==Calendar.OCTOBER)  {today[1]=Planner.OCT; today[0]=Planner.FALL;}
        if (month==Calendar.SEPTEMBER){today[1]=Planner.SEP; today[0]=Planner.FALL;}
        if (month==Calendar.AUGUST)   {today[1]=Planner.AUG; today[0]=Planner.SUMMER;}
        if (month==Calendar.JULY)     {today[1]=Planner.JUL; today[0]=Planner.SUMMER;}
        if (month==Calendar.JUNE)     {today[1]=Planner.JUN; today[0]=Planner.SUMMER;}
        if (month==Calendar.MAY)      {today[1]=Planner.MAY; today[0]=Planner.SPRING;}
        if (month==Calendar.APRIL)    {today[1]=Planner.APR; today[0]=Planner.SPRING;} 
        if (month==Calendar.MARCH)    {today[1]=Planner.MAR; today[0]=Planner.SPRING;}
        if (month==Calendar.FEBRUARY) {today[1]=Planner.FEB; today[0]=Planner.WINTER;}
        if (month==Calendar.JANUARY)  {today[1]=Planner.JAN; today[0]=Planner.WINTER;}
        
        log("Today's plan indexes: SEASON="+today[0]+"; MONTH="+today[1] +"; DAY="+today[2]);
	}
	private boolean isInPlan(int[] day,int[] plan) {
		boolean inSeason = false;
		boolean inMonth  = false;
		boolean onDay    = false;

		log("Task plan season v. today's season: "+ plan[day[SEASON]]);
		log("Task plan month  v. today's month : "+ plan[day[MONTH]]);
		log("Task plan day    v. today's day   : "+ plan[day[DAY]]);
		
		if (plan[day[SEASON]]==1) 
		    {inSeason = true;}
		if (plan[day[MONTH]]==1)  
			{inMonth  = true;}
		if (plan[day[DAY]]==1)
			{onDay    = true;}
		
		return (inSeason || inMonth) && onDay;
	}
}
