package org.mlink.agent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.JobStatusRef;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.PriorityRef;
import org.mlink.agent.model.SkillLevelRef;
import org.mlink.agent.model.SkillTypeRef;
import org.mlink.agent.model.TaskView;

public class HibernateInspector {
	private static Logger logger = Logger.getLogger(HibernateInspector.class);

	private static final int ROUTINE = 1132;
	private static Locator theLocator;
	private static JobStatusRef theJsr;
	private static PriorityRef thePr;
	private static SkillLevelRef theSlr;
	private static SkillTypeRef theStr;

	private HibernateInspector(){}

	public static void run() {
		HibernateInspector hi = new HibernateInspector();
		//hi.testTaskLoad();
		//hi.testJobCreation();
		//hi.testJobUpdate();
		
		// Incomplete job check
		List<Job> incompletes = new ArrayList<Job>();
		try {
			log("Getting locations");
			List<Locator> locations = WorldDAO.getInstance().getLocations();
			log("Got "+ locations.size()+" locations");
			for (Locator location:locations){
				log("Processing location "+ location.getId() +". Start "+ System.currentTimeMillis());
				List<Job> localInc = WorldDAO.getInstance().getIncompletes(location.getId());
				log("Got "+ localInc.size() +" incomplete jobs for location "+ location.getId()+". End "+ System.currentTimeMillis());
				incompletes.addAll(localInc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		log(incompletes.size() +" incomplete jobs for all locations ");
	}
    /**
     * JUnit-like method to explore job-creation
	 *
     */
    public void testJobCreation() {
		log("Running Job Creation Test");
    	List<Job> flushless = createJobs(100,"Flushless");
    	//List<Job> flush     = createJobs(100,"Flushed");
    	List<Job> paginated = createJobs(100,"Paginated");
    	List<Job> jdbc = createJobs(100,"JDBC");

    	start("Job Creation - flushless");
    	try {
    		WorldDAO.getInstance().batchSaveFlushless(flushless);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	end("Job Creation - flushless");
    	/*start("Job Creation - flushed");
    	try {
    		WorldDAO.getInstance().batchSave(flushless);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	end("Job Creation - flushed");*/
    	
    	start("Job Creation - paginated");
    	try {
    		WorldDAO.getInstance().batchSavePaginated(paginated);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	end("Job Creation - paginated");
    	
    	start("Job Creation - JDBC");
    	try {
    		WorldDAO.getInstance().saveJDBC(jdbc);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	end("Job Creation - JDBC");
    }

	/**
	 * JUnit-like method to explore task-loading
	 *
	 */
    public void testJobUpdate(){
		log("Running Job Update Test");
    	List<Job> l = new ArrayList<Job>();
		try {
			l = WorldDAO.getInstance().getNonFinalJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log(l.size()+" jobs loaded");
		
		List<Job> mods = modify(l,"Hibernat");
		start("Job Update - Hibernate 'update' statement");
		try {
			WorldDAO.getInstance().batchUpdate(mods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		end("Job Update - Hibernate 'update' statement");
		
		mods = modify(l,"Hibernat");
		start("Job Update - Hibernate 'merge' statement");
		try {
			WorldDAO.getInstance().batchMerge(mods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		end("Job Update - Hibernate 'merge' statement");
		
		mods = modify(l,"JDBC");
		start("Job Update - JDBC");
		try {
			WorldDAO.getInstance().batchUpdate(mods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		end("Job Update - JDBC");
    }
    
    private List<Job> modify(List<Job> jobs, String test){
    	List<Job> mods = new ArrayList<Job>();
		Iterator<Job> it = jobs.iterator();
		for (int i=0;i<100;i++) {
			if (!it.hasNext()) {
				log("Running "+test +" update test with "+ (i+1) +"jobs");
				break;
			}
			Job job = it.next();
			job.setStartedDate(new java.sql.Date(System.currentTimeMillis()));
			job.setDescription(job.getDescription()+" ("+test +" test)");
			mods.add(job);
		}
		return mods;
    }
    
    private List<Job> createJobs(int numJobs,String description) {
    	List<Job> l = new ArrayList<Job>();
    	log("Creating job objects");
    	for (int i = 0;i<numJobs;i++) {
    		Job job = new Job();
    		job.setCreatedDate(timestamp());
    		job.setDescription("Job "+ description +" "+i);
    		job.setEarliestStart(date());
    		job.setEstimatedTime(i);
    		job.setFinishby(date());
    		job.setJobTypeId(ROUTINE);
    		job.setLatestStart(date());
    		job.setLocator(locator());
    		job.setNumberOfWorkers(i);
    		job.setPriorityRef(priorityRef());
    		job.setSkillLevelRef(skillLevelRef());
    		job.setSkillTypeRef(skillTypeRef());
    		job.setStatusRef(jobStatusRef());
    		l.add(job);
    	}
    	return l;
    }
    private JobStatusRef jobStatusRef() {
    	if (theJsr==null){
    		theJsr = new JobStatusRef();
    		theJsr.setId(1);
    		theJsr.setCode("RFS");
    		theJsr.setDescription("Ready For Scheduling");
    	}
    	return theJsr;
    }
    private Locator locator() {
    	if (theLocator==null) {
    		theLocator=new Locator();
    		theLocator.setId(20000718228L);
    		theLocator.setFullLocator("West");
    	}
    	return theLocator;
    }
    private PriorityRef priorityRef() {
    	if (thePr==null){
    		thePr = new PriorityRef();
    		thePr.setId(439);
    		thePr.setCode(""+15);
    	}
    	return thePr;
    }
    private SkillLevelRef skillLevelRef() {
    	if (theSlr==null){
    		theSlr = new SkillLevelRef();
    		theSlr.setId(1011);
    		theSlr.setCode(""+5);
    	}
    	return theSlr;
    }
    private SkillTypeRef skillTypeRef() {
    	if (theStr==null){
    		theStr = new SkillTypeRef();
    		theStr.setId(1065);
    		theStr.setDescription("Trades Electrician");
    	}
    	return theStr;
    }
    private java.sql.Timestamp timestamp(){return new java.sql.Timestamp(System.currentTimeMillis());}
    private java.sql.Date date(){return new java.sql.Date(System.currentTimeMillis());}
    private void start(String s){log("Start "+s+": "+ System.currentTimeMillis());}
    private void end(String s)  {log("End   "+s+": "+ System.currentTimeMillis());}
    private static void log(Object o){logger.debug(o);}
    
}
