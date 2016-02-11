package org.mlink.agent;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mlink.agent.SimWorldConnection.RepairJob;
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobView;
import org.mlink.agent.model.Task;
import org.mlink.agent.model.TaskView;
import org.mlink.agent.model.WorkObject;

public interface SimWorld {

	public void runAll();	
	public Collection<Job> runJSM(Collection<Job> jobs);
	public Collection<Job> runPlanner(Collection<TaskView> tasks,Timestamp day);
	public Collection runScheduler(Collection<Job> jobs,Collection workschedules,Timestamp day);
	public Collection runShiftManager(Collection schedules,Timestamp day);
	
	public List<WorkObject> getActiveWorkObjects() throws DAOException;
	public RepairJob createRepairJob(Task task, Integer repairTime, Timestamp d) throws Exception;
	public void completeRepairJob(RepairJob rj, Timestamp day) ;
	public void completeRoutineJobs(Timestamp day) ;
	public void endWorkerShifts(Timestamp day);
}
