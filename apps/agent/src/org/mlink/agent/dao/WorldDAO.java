package org.mlink.agent.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.mlink.agent.model.Action;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobSchedule;
import org.mlink.agent.model.Locator;
import org.mlink.agent.model.PlannerTask;
import org.mlink.agent.model.Task;
import org.mlink.agent.model.TaskView;
import org.mlink.agent.model.WorkObject;
import org.mlink.agent.model.WorkSchedule;
import org.mlink.agent.model.WorkScheduleStatusRef;
import org.mlink.agent.model.World;
import org.mlink.agent.util.AgentConfig;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.util.DBAccess;


public class WorldDAO extends BaseDAO {
    private static final int ACTIVE = 1;
    private static final int PACKET_SIZE = 500;

    private static WorldDAO instance = null;


    private WorldDAO() {
    }

    public static WorldDAO getInstance()  throws DAOException {
    	if (instance == null) {
			try {
				cfg = new Configuration().configure();
				factory = cfg.buildSessionFactory();
			} catch (HibernateException e) {
				throw new DAOException(e.toString());
			}

            instance = new WorldDAO();
        }
        return instance;

    }

    //*******************************************************************
    //                          Entity Accessors
    //*******************************************************************
    public List<Action> getActions(TaskView tv) throws HibernateException, DAOException {
    	List<Action> l = new ArrayList<Action>();
        Session session = null;
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.Action as ta "+
	                                               " where ta.taskId = "+ tv.getId()	);
	        l = query.list();
        } finally {
        	close(session);
        }
        return l;
    }

    public List<WorkObject> getActiveWorkObjects() throws HibernateException, DAOException {
    	List<WorkObject> l = new ArrayList<WorkObject>();
        Session session = null;
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.WorkObject as wo "+
	                                               " where wo.active = "+ ACTIVE);
	        l = query.list();
        } finally {
        	close(session);
        }
        return l;
    }

    public List<TaskView> getActiveTasks() throws HibernateException, DAOException {
        List<TaskView> l = new ArrayList<TaskView>();
        Session session = null;
        //TODO: This should check if a task is truly routine or not via frequency_id and freq_days | freq_months instead of type
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.TaskView as tv "+
                                              " inner join fetch tv.priorityRef "+
	        		                          " inner join fetch tv.skillLevelRef "+
	        		                          " inner join fetch tv.skillTypeRef "+
	        								  " left join fetch tv.actions "+
	                                          " where tv.active= "+ ACTIVE +
	                                          "   and tv.taskTypeRef.code = 'Routine'");
	        l = query.list();
        } finally {
        	close(session);
        }
        return l;
    }
    /** 
     * Get list of jobs that have not yet been completed, but are on work schedules 
     * that are marked done. 
     * 
     * IMPLEMENTATION DEET: We start w/ getting job schedules b/c each job schedule is 
     * associate with 1 job and 1 work schedule. This will return the three associated 
     * objects. Starting with a Job or Work Schedule results in a set of Job Schedules,
     * which then makes traversal to find the correct object (Work Schedule/Job) for the 
     * other end of the association difficult.
     * 
     * @param locationId
     * @return List of job schedules
     * @throws HibernateException
     * @throws DAOException
     */
    public List<Job> getIncompletes(Long locationId) throws HibernateException, DAOException {
        List<Job> l = new ArrayList<Job>();
        Session session = null;
        try {
        	session = getSession();
	        // NOTE: Join fetches here must match those in getRFSJobs() and in getSchedules(),
	        // as the jobs returned by all are candidates for processing by the Scheduler and algorithms.
        	Query queryIncompletes = session.createQuery("from org.mlink.agent.model.JobSchedule as js "+
	 				  					      "  inner join fetch js.workSchedule workSchedule "+
	 				  					      "  inner join fetch workSchedule.person "+
	 				  					      "  inner join fetch workSchedule.locator "+
	        				 				  "  inner join fetch js.job job"+
	        				 				  "  inner join fetch job.priorityRef "+
	        				 				  "  inner join fetch job.statusRef "+
	        				 				  "  inner join fetch job.skillTypeRef "+
	        				 				  "  inner join fetch job.skillLevelRef "+
		                                      "  left  join fetch job.locator locator "+
		                                      " where js.workSchedule.locator.id = "+ locationId +
		                                      "   and js.workSchedule.statusRef.code = '"+WorkScheduleStatusRef.Status.DUN+"' "+
		                                      "   and js.job.statusRef.code in "+JobStatusRef.queryIncompletes+
                                              "   and js.job.scheduleResponsibilityRef.code = 'System' "+
		                                      "   and js.deletedTime is null "+
		                                      "   and js.createdTime = (select max(jobschedule.createdTime) "+                 // Make sure this is the latest schedule
		                                      "                        from org.mlink.agent.model.JobSchedule as jobschedule"+ // i.e. no later active schedules exist 
		                                      "                        where jobschedule.job.id = js.job.id "+                 // (with a later jobSchedule.createdTime
		                                      "                          and jobschedule.deletedTime is null )");               // and  workSchedule in 'IP' or 'NYS')
		     //js.createdTime should not be null. if null pops up then there is a problem"   and jobschedule.createdTime is not null)");
	        /* Query queryIncompletes = session.createQuery("from org.mlink.agent.model.JobSchedule as js "+
	 				  					      "  inner join fetch js.workSchedule workSchedule "+
	 				  					      "  inner join fetch workSchedule.person "+
	 				  					      "  inner join fetch workSchedule.locator "+
	        				 				  "  inner join fetch js.job job"+
	        				 				  "  inner join fetch job.priorityRef "+
	        				 				  "  inner join fetch job.statusRef "+
	        				 				  "  inner join fetch job.skillTypeRef "+
	        				 				  "  inner join fetch job.skillLevelRef "+
		                                      "  left  join fetch job.locator locator "+
	                                          "  where js.workSchedule.statusRef.code = "+ JobStatusRef.Status.DUN;
	                                          "    and js.workSchedule.day = "+
	                                          "        (select max(worksched.day) "+
	                                          "           from org.mlink.agent.model.JobSchedule as jobsched " +
	                                          "          where jobsched.job.id = js.job.id "+
	                                          "            and jobsched.deletedTime is null "+
	                                          "            and js.job.statusRef.code in "+queryIncompletes+
	                                          "    and js.deletedTime is null "+
	                                          "    and js.workSchedule.locator.id = "+ locationId);*/
            List<JobSchedule> incJS = queryIncompletes.list();
            for (JobSchedule jobSchedule:incJS) {
                Job job = jobSchedule.getJob();
                job.setLatestJobSchedule(jobSchedule);
                l.add(job);
            }
        } finally {
        	close(session);
        }
        return l;
    }
    public List<Locator> getLocations() throws HibernateException, DAOException {
    	List<Locator> l = new ArrayList<Locator>();
        Session session = null;
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.Locator as l "+
	                                          " where l.schemaRef.code = 'Location'");
	        l = query.list();
        } finally {
        	close(session);
        }
        return l;
    }
    /**
     * @deprecated Not used, but an example of using Hibernate Criteria class. Supposed to be slower performing than HQL, but can set e.g. dates easier
     */
   public List<WorkSchedule> getNextWorkSchedules(WorkSchedule ws) throws HibernateException, DAOException {
        List<WorkSchedule> l = null;
        Session session = null;
        try {
        	session = getSession();
	        l = session.createCriteria(WorkSchedule.class)
	        	.add(Restrictions.gt("day", ws.getDay()))           // < ws.day
	        	.add(Restrictions.eq("person", ws.getPerson()))     // = ws.person
	        	.add(Restrictions.eq("locator", ws.getLocator()))   // = ws.locator
	        	.list();
	       
        } finally {
	        // detach objects
        	close(session);
        }
        return l;
    }
   public List<Job> getNonFinalJobs() throws HibernateException, DAOException {
       List<Job> l = null;
       Session session = null;
       try {
    	   session = getSession();
    	   Query query = session.createQuery("from org.mlink.agent.model.Job as job " +
	        		                          " left join fetch job.jobPrecedes "+
	        		                          " inner join fetch job.priorityRef "+
	        		                          " inner join fetch job.skillLevelRef "+
	        		                          " inner join fetch job.skillTypeRef "+
	        		                          " inner join fetch job.statusRef "+
	                                          " where job.statusRef.code not in "+JobStatusRef.finalStatusesSQLClause );
	        l = query.list();
	        // detach objects
       } finally {
       	close(session);
       }
       return l;
   }
   
   //Get agent.model.Job(s) by id(s)
   public List<Job> getJobsByIds(HashSet ids) throws HibernateException, DAOException {
	   if (ids==null) return null;
	   
       List<Job> l = null;
       Session session = null;
	Iterator iterator = ids.iterator();
	String idString=iterator.next().toString();
       while (iterator.hasNext()){
    	   idString=idString + ", " + iterator.next().toString();
       }
           
       
       try {
    	   session = getSession();
    	   Query query = session.createQuery("from org.mlink.agent.model.Job as job " +
	        		                          " left join fetch job.jobPrecedes "+
	        		                          " inner join fetch job.priorityRef "+
	        		                          " inner join fetch job.skillLevelRef "+
	        		                          " inner join fetch job.skillTypeRef "+
	        		                          " inner join fetch job.statusRef "+
	                                          " where job.id in ("+ idString +" )" );
	        l = query.list();
	        // detach objects
       } finally {
       	close(session);
       }
       return l;
   }
    /**
     * Returns the list of jobs in state RFS.
     * 
     * @param locationId
     * @return List of jobs
     * @throws HibernateException
     * @throws DAOException
     */
    public List<Job> getRFSJobs(Long locationId) throws HibernateException, DAOException {
        List<Job> l = new ArrayList<Job>();
        Session session = null;
        try {
        	session = getSession();
	        // Find all jobs in RFS that have never been assigned ( -- QUERY RETURNS JOBS
            // (including jobs which were manually unscheduled or rescheduled by Scheduler due to better utility rate.
            //  A jobSchedule was created for the job, but the deletedTime value is set,
	        //  and no other jobSchedule with a null deletedTime exists for the job. Lets call these job schedules virtual job schedules)
            //
	        // NOTE: Join fetches here must match those in getIncompletes(), in getSchedules(), and below
	        // as the jobs returned by all are candidates for processing by the Scheduler and algorithms.
	        Query queryRFSnew = session.createQuery("from org.mlink.agent.model.Job as job"+
	        		        				 			   " inner join fetch job.priorityRef "+
										                   " inner join fetch job.statusRef "+
										                   " inner join fetch job.skillTypeRef "+
										                   " inner join fetch job.skillLevelRef "+
	        		                                       " left  join fetch job.locator locator "+
                                                           "  where job.statusRef.code = '"+JobStatusRef.Status.RFS+"' "+
                                                           "    and job.scheduleResponsibilityRef.code = 'System' "+
                                                           "    and job.id not in ( select distinct js.job.id "+
                                                           "        from org.mlink.agent.model.JobSchedule as js where js.deletedTime is null)"+
                                                           "    and job.locator.topParentId = "+ locationId );
	        List<Job> listj1 = queryRFSnew.list();
	        Map<Long,Job> jobMap = new HashMap<Long,Job>();
	        for (Job job:listj1) {
	        	if (!jobMap.containsKey(job.getId())) {
	        		jobMap.put(job.getId(), job);
	        	}
	        }
	        l.addAll(jobMap.values());
        } finally {
        	close(session);
        }
        return l;
    }
    
    public List<WorkSchedule> getSchedules(Long locationLevelLocatorId) throws HibernateException, DAOException {
        //this assumes work schedules are always hung off the "location" level locator
        //(which is what should be passed in).
    	List<WorkSchedule> l = new ArrayList<WorkSchedule>();
        Session session = null;
        try {
        	session = getSession();
        	// Load schedules with current jobs (jobSchedule.deletedTime is   null)
	        // NOTE: Job join fetches here must match those in getIncompletes() and in getRFSJobs(),
	        // as the jobs returned by all are candidates for processing by the Scheduler and algorithms.
        	Query queryWSJS = session.createQuery("from org.mlink.agent.model.JobSchedule as jobSchedule "+
        			                               " inner join fetch jobSchedule.workSchedule ws "+
        			                               " inner join fetch ws.locator "+
	        		                               " inner join fetch ws.shiftRef "+
	        		                               " inner join fetch ws.person "+
	        		                               " left join fetch ws.person.skills skill"+
	        		                               " inner join fetch skill.skillTypeRef "+
	        		                               " inner join fetch skill.skillLevelRef "+
	        		                               " inner join fetch jobSchedule.job job "+
    		        				 			   " inner join fetch job.priorityRef "+
								                   " inner join fetch job.statusRef "+
								                   " inner join fetch job.skillTypeRef "+
								                   " inner join fetch job.skillLevelRef "+
    		                                       " left  join fetch job.locator locator "+
	                                               " where jobSchedule.workSchedule.statusRef.code in ('IP','NYS', 'QUERY_WSJS') "+
	                                               "   and jobSchedule.workSchedule.day = today() "+
	                                               "   and jobSchedule.workSchedule.locator.id = "+ locationLevelLocatorId +
	                                               "   and jobSchedule.workSchedule.person.active = "+ ACTIVE +
	                                               "   and jobSchedule.deletedTime is null ");
        	/* USE FOR DB QUERY:
select * from job_schedule js, work_schedule ws, work_schedule_status_ref wssr, person p
where js.work_Schedule_id = ws.id
   and ws.status_id = wssr.id
   and wssr.code in ('IP','NYS')
   and ws.day = today()
   and ws.locator_id = LOCATION_LEVEL_LOCATOR_ID
   and ws.person_id = p.id
   and p.active = 1
   and js.deleted_Time is null 
        	 */
        	// Query returns jobschedules, so extract workschedules
	        List<JobSchedule> ljs = queryWSJS.list();
	        Map<Long,WorkSchedule> wsjsMap = new HashMap<Long,WorkSchedule>();
	        for (JobSchedule js:ljs) {
	        	WorkSchedule ws = js.getWorkSchedule();
	        	// prune final state jobs
	        	/*if (JobStatusRef.Status.CIA.equals(js.getJob().getStatusRef().getCode()) ||
	        		JobStatusRef.Status.DUN.equals(js.getJob().getStatusRef().getCode()) ||
	        		JobStatusRef.Status.EJO.equals(js.getJob().getStatusRef().getCode()) )
	        		continue; -- can't just continue b/c we lose the workschedule */
	        	if (wsjsMap.containsKey(ws.getId()))  {
	        		ws = wsjsMap.get(ws.getId());
	        		ws.getJobSchedules().add(js);
	        		js.setWorkSchedule(ws);
	        	} else 
	        		wsjsMap.put(ws.getId(), ws);
	        }
	        //l.addAll(wsjsMap.values()); -- still need map to eliminate duplicates that might arise from following query
	        
	        // Load active schedules that have never been assigned jobs, as well as those schedules 
	        // that have been assigned jobs previously, but with no jobs currently on the schedule 
	        // (jobSchedule.deletedTime is not null)
	        Query queryWS = session.createQuery("from org.mlink.agent.model.WorkSchedule as ws "+
	        									   " inner join fetch ws.locator "+
	        		                               " inner join fetch ws.shiftRef "+
	        		                               " inner join fetch ws.person person"+
	        		                               " left join fetch person.skills skill"+
	        		                               " inner join fetch skill.skillTypeRef "+
	        		                               " inner join fetch skill.skillLevelRef "+
	                                               " where ws.statusRef.code in ('IP','NYS','QUERY_WS') "+
	                                               "   and ws.day = today() "+
	                                               "   and ws.locator.id = "+ locationLevelLocatorId +
	                                               "   and ws.person.active = "+ ACTIVE );
	                                               //"   and ws.id not in (select js.workSchedule.id "+
	                                               //"                 from org.mlink.agent.model.JobSchedule js "+
	                                               //"                 where js.deletedTime is null)");
	        
	        /* USE FOR DB QUERY:
select * from work_schedule ws, work_schedule_status_ref wssr, person p
where ws.status_id = wssr.id
   and wssr.code in ('IP','NYS')
   and ws.day = today() 
   and ws.locator_id = LOCATION_LEVEL_LOCATOR_ID
   and ws.person_id = p.id
   and p.active = 1
   -- and ws.id not in (select js.work_schedule_id
                 from Job_Schedule js 
                 where js.deleted_Time is null)
                 */
	        List<WorkSchedule> lws = queryWS.list();
	        for (WorkSchedule ws:lws) {
	        	if (!wsjsMap.containsKey(ws.getId()))  wsjsMap.put(ws.getId(), ws);
            }
	        // now prepare return List
            l.addAll(wsjsMap.values());
        } finally {
        	close(session);
        }
        return l;
    }
    public List<WorkSchedule> getSchedulesIPNYS() throws HibernateException, DAOException {
    	List<WorkSchedule> l = new ArrayList<WorkSchedule>();
        Session session = null;
        //session.clear();
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.WorkSchedule as ws "+
                                                   " inner join fetch ws.shiftRef "+
                                                   " inner join fetch ws.statusRef "+
                                                   " left join fetch ws.jobSchedules jobSchedule "+
                                                   " left join fetch jobSchedule.job "+
	                                               " where ws.statusRef.code in ('IP','NYS') "+
	                                               "   and ws.day <= today() ");
	        l.addAll(query.list());
        } finally {
        	close(session);
        }
        return l;
    }
    public Task getTask(Long taskId) throws HibernateException, DAOException {
        List<Task> l = null;
        Session session = null;
        try {
        	session = getSession();
	        Query query = session.createQuery("from org.mlink.agent.model.Task as t "+
	                                               " where t.id = "+ taskId);
	        l = query.list();
        } finally {
        	close(session);
        }
        return l.iterator().next();
    }

    //*******************************************************************
    //                          Entity Persisters
    //*******************************************************************
    public void save(Object o) throws DAOException {
		Session session = null;
		try {
			session = getSession();
        	session.saveOrUpdate(o);
		} finally {
			close(session);
		}
    }
	public void batchMerge(Collection c) throws DAOException {
		if (c==null) return;
		Session session = getSession();
		for (Iterator it = c.iterator();it.hasNext();) {
			Object o = it.next();
		    session.merge(o);
		}
		close(session);
	}
    public void batchSaveFlushless(Collection c) throws DAOException {
        if (c==null) return;
        Session session = null;
        try {
	        session = getSession();
	        for (Iterator it = c.iterator();it.hasNext();) {
	            Object o = it.next();
	            session.save(o);
	        }
        } finally {
        	close(session);
        }
    }

    public void saveJDBC(Collection <Job> c)  {
        Connection conn=null;
        PreparedStatement stmt = null;

        try {
            conn = DBAccess.getDBConnection();
            conn.setAutoCommit(false);
            String sql="insert into JOB (CREATED_DATE, DISPATCHED_DATE, STARTED_DATE," +
                    "FINISHBY, ESTIMATEDTIME, LAST_UPDATED, NUMBER_WORKERS, COMPLETED_DATE, " +
                    "EARLIEST_START, LATEST_START, DESCRIPTION, SCHEDULED_DATE, " +
                    "SEQUENCE_LEVEL, STICKY, JOB_TYPE_ID, OBJECT_ID, " +
                    "ORGANIZATION_ID, LOCATOR_ID, SKILL_LEVEL_ID, SKILL_TYPE_ID, PRIORITY_ID, STATUS_ID, " +
                    " ID) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?)";
            stmt = conn.prepareStatement(sql);
            Long id = 1000000L;
            for (Iterator it = c.iterator();it.hasNext();) {
                Job job = (Job)it.next();
                
                //Long id = DBAccess.generateID("JOB_SEQ",conn);
                stmt.setTimestamp(1,job.getCreatedDate());
                stmt.setDate(2,(Date) job.getDispatchedDate());
                stmt.setDate(3,(Date) job.getStartedDate());
                stmt.setDate(4,(Date) job.getFinishby());
                stmt.setInt(5,job.getEstimatedTime());
                stmt.setDate(6,(Date) job.getLastUpdated());
                stmt.setInt(7,job.getNumberOfWorkers());
                stmt.setTimestamp(8,job.getCreatedDate());
                stmt.setDate(9,(Date) job.getEarliestStart());
                stmt.setDate(10,(Date) job.getLatestStart());
                stmt.setString(11,job.getDescription());
                stmt.setDate(12,(Date) job.getScheduledDate());
                stmt.setInt(13,1);
                stmt.setBoolean(14,Boolean.FALSE);
                stmt.setInt(15,job.getJobTypeId());
                stmt.setLong(16,20000756928L);
                stmt.setLong(17,1L);
                stmt.setLong(18,20000718228L);
                stmt.setInt(19,1011);
                stmt.setInt(20,1065);
                stmt.setInt(21,439);
                stmt.setInt(22,1);
                stmt.setLong(23,id);
                stmt.addBatch();
                id++;
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try{conn.rollback();}catch(Exception ee){ee.printStackTrace();}
        } finally {
            try {
            	stmt.close();
                conn.close();
            } catch (Exception ex){ex.printStackTrace();}
        }
    }
	public void batchSavePaginated(Collection c) throws DAOException {
		if (c==null) return;
		int i=0;
		boolean done = false;
		Session session=null;
		Iterator it = c.iterator();
		try {
			while (!done) {
				if (i % AgentConfig.batchPageConst()==0) {
					if (session!=null) {
						close(session);
					}
					session = getSession();
				}
				if (!it.hasNext()) {
					done = true;
					break;
				}
				Object o = it.next();
			    session.save(o);
			    i++;
			}
		} finally {
			close(session);
		}
	}
	public void batchSaveOrUpdatePaginated(Collection c) throws DAOException {
		if (c==null) return;
		int i=0;
		boolean done = false;
		Session session=null;
		Iterator it = c.iterator();
		try {
			while (!done) {
				if (i % AgentConfig.batchPageConst()==0) {
					if (session!=null) {
						close(session);
					}
					session = getSession();
				}
				if (!it.hasNext()) {
					done = true;
					break;
				}
				Object o = it.next();
			    session.saveOrUpdate(o);
			    i++;
			}
		} finally {
			close(session);
		}
	}
	public void batchUpdate(Collection c) throws DAOException {
		if (c==null) return;
		Session session = null;
		try {
			session = getSession();
			for (Iterator it = c.iterator();it.hasNext();) {
				Object o = it.next();
			    session.update(o);
			}
		} finally {
			close(session);
		}
	}
	public int batchUpdatePlannedTasks(Collection<TaskView> c) throws DAOException {
		StringBuffer sb = new StringBuffer();
		int updatedEntities = 0;
		// Oracle can only handle 1000 elements in a list. 
		// Likely a limit on other databases
		int loops = 0;
		String delim = "";
		for (Iterator<TaskView> it=c.iterator();it.hasNext();) {
			if (loops==PACKET_SIZE) {
				loops=0;
				delim="";
				updatedEntities += batchUPD(sb.toString());
				sb = new StringBuffer();
			}
			TaskView tv = it.next();
			sb.append(delim).append(tv.getId());
			delim=",";
			loops++;
		}
		updatedEntities += batchUPD(sb.toString());
		return updatedEntities;
	}
	private int batchUPD(String stmt) throws DAOException {
		Session session = null;
		int update = 0;
		try {
			session = getSession();
			update = session.createQuery("update org.mlink.agent.model.Task t set t.lastPlannedDate = :today where t.id in ("+ stmt +")")
					.setDate("today",new Date(System.currentTimeMillis()))
					.executeUpdate();
		} finally {
			close(session);
		}
		return update;
	}
	public void updateRunHoursThreshold(Collection<TaskView>meteredTasks) throws HibernateException, DAOException {
		Collection<PlannerTask> plannedTasks = new ArrayList<PlannerTask>();
		for (TaskView tv:meteredTasks) {
			double runhours  = tv.getRunHours().doubleValue();
			double threshold = tv.getThreshold().doubleValue();
			double newthreshold = threshold;
			if (runhours >= threshold) {
				Task task = getTask(tv.getId());
				double increment = task.getRunHoursThresholdIncrement().doubleValue();
				newthreshold = threshold + Math.ceil(((runhours-threshold)/increment)+0.25)*increment;				
			}
			PlannerTask pt = new PlannerTask();
			pt.setId(tv.getId());
			pt.setRunHoursThreshold(newthreshold);
			plannedTasks.add(pt);
		}
		batchUpdatePaginated(plannedTasks);
	}
	public void batchUpdatePaginated(Collection c) throws DAOException {
		if (c==null) return;
		int i=0;
		boolean done = false;
		Session session=null;
		Iterator it = c.iterator();
		try {
			while (!done) {
				if (i % AgentConfig.batchPageConst()==0) {
					if (session!=null) {
						close(session);
					}
					session = getSession();
				}
				if (!it.hasNext()) {
					done = true;
					break;
				}
				Object o = it.next();
			    session.update(o);
			    i++;
			}
		} finally {
			close(session);
		}
	}
	/** 
	 * Batch updates the scheduledDate attribute of the specified collection of Jobs. Sending in
	 * a null value for the date will mark the Jobs as unscheduled. DispatchedDate is also set to
	 * null as a side-effect, since a newly-scheduled job can't have been dispatched yet.
	 * 
	 * @param c The collection of ids of Jobs to update
	 * @param sqlDate The Date to which to set scheduledDate (null to unschedule)
	 * @param sticky Whether the jobs will be sticky or not
	 * @return The number of entities touched by this operation
	 * @throws DAOException
	 * 
	 * @deprecated No longer used as SchedulerJobs are used to persist job changes to db
	 */
	public int batchUpdateJobData(Collection<Long> c, Date sqlDate, boolean sticky) throws DAOException {
		Session session = null;
		StringBuffer sb = new StringBuffer();
		String delim = "";
		for(Long id : c) {
			sb.append(delim).append(id);
			if (delim.length()<1) delim=",";
		}
		log("Jobs to update "+ sb.toString());
		int updatedEntities = 0;
		try {
			session = getSession();
			String stick = sticky?"Y":"N";
			String hqlUpdate = "update org.mlink.agent.model.Job j set j.scheduledDate = :sqlDate "+
			                   " , j.sticky = :sticky, j.dispatchedDate = :dispatched where j.id in ("+ sb.toString() +")";
			updatedEntities = session.createQuery( hqlUpdate )
					.setDate("sqlDate", sqlDate)
					.setString("sticky", stick)
					.setString("dispatched", null)
			        .executeUpdate();
		} finally {
			close(session);
		}
		return updatedEntities;
	}
	/**
	 * @deprecated Currently does not work
	 * @return
	 * @throws HibernateException
	 * @throws DAOException
	 */
	public int batchUpdateSchedulesDone() throws HibernateException, DAOException {
		Session session = null;
		int updatedEntities = 0;
		try { 
			session = getSession(); 
			String hqlUpdate = "update org.mlink.agent.model.WorkSchedule ws "+
							   " set ws.statusRef = (select wssr from org.mlink.agent.model.WorkScheduleStatusRef wssr "+
							   "                     where wssr.code = "+WorkScheduleStatusRef.Status.DUN+")"+
					           " where ws.statusRef.code not in "+ WorkScheduleStatusRef.batchUpdateSchedulesDone+
					           "   and (ws.day+(ws.shiftRef.shiftStart/60/24)+(1/2))<now()";
			updatedEntities = session.createQuery( hqlUpdate ).executeUpdate();
		} finally {
			close(session);
		}
		return updatedEntities;
	}
	/**
	 * @deprecated Currently does not work
	 * @return
	 * @throws HibernateException
	 * @throws DAOException
	 */
	public int batchUpdateSchedulesTimedOut() throws HibernateException, DAOException {
		Session session = null;
		int updatedEntities = 0;
		try { 
			session = getSession(); 
			String hqlUpdate = "update org.mlink.agent.model.WorkSchedule ws "+
							   " set ws.statusRef = (select wssr from org.mlink.agent.model.WorkScheduleStatusRef wssr "+
							   "                     where wssr.code = "+WorkScheduleStatusRef.Status.TO+")"+
							   " where ws.statusRef.code not in "+WorkScheduleStatusRef.schedulesTimeOut+
							   "   and (ws.day+(ws.shiftRef.shiftEnd/60/24)+(1/2))<now()";
			updatedEntities = session.createQuery( hqlUpdate ).executeUpdate();
		} finally {
			close(session);
		}
		return updatedEntities;
	}


	//********************************************************************
	//                         World Accessors                            
	//********************************************************************
	public List<World> getAllWorlds() throws HibernateException, DAOException {
		List<World> l = null;
		Session session = null;
		try {
			session = getSession();
			Query query = session.createQuery("from org.mlink.agent.model.World");
			l = query.list();
		} finally {
			close(session);
		}
		return l;
	}
	public World getMatrix() throws HibernateException, DAOException {
		World x = null;
		Session session = null;
		try {
			session = getSession();
			Query query = session.createQuery("from org.mlink.agent.model.World as x "+
                                              " where x.isProduction = 1");
			x = extract(query.list());
		} finally {
			close(session);
		}
		return x;
	}
	public World findByName(String name) throws HibernateException, DAOException {
		World x = null;
		Session session = null;
		try {
			session = getSession();
			Query query = session.createQuery("from org.mlink.agent.model.World as x " +
					                          " where x.name = "+ name);
			x = extract(query.list());
		} finally {
			close(session);
		}
		return x;
	}

	public World findBySchema(String schema) throws HibernateException, DAOException {
		World x = null;
		Session session = null;
		try {
			session = getSession();
			Query query = session.createQuery("from org.mlink.agent.model.World as x " +
					                          " where x.schema = "+ schema);
			x = extract(query.list());
		} finally {
			close(session);
		}
		return x;
	}

	public World getWorld(Long id) throws DAOException {
		World x = null;
		Session session = null;
		try {
			session = getSession();
		    x = (World)session.get(World.class, id);
		} finally {
			close(session);
		}
		return x;
	}

	public void saveWorld(World world) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			session.saveOrUpdate(world);
		} finally {
			close(session);
		}
	}
	
	public void deleteWorld(World world) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			session.delete(world);
		} finally {
			close(session);
		}
	}
	
	/**
	 * Makes the specified schema the operative schema for all data access.
	 * 
	 * @param schema The schema to set as active
	 * @throws DAOException
	 */
	public void activateWorld(String schema) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			session.createSQLQuery("alter table set current_schema = "+ schema);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Could not active world",e);
		} finally {
			close(session);
		}
	}
	private World extract(List l) {
		if (l.size() > 0)
			return (World) l.iterator().next();
		return null;
	}
}
