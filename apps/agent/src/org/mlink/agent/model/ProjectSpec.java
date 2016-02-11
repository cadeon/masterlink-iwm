package org.mlink.agent.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProjectSpec {
	private Set<TaskView> tasks = new HashSet<TaskView>(); 
	//private Map mJobs = new HashMap();
	private Long       projectId;
	
	// Project properties
	private String    createdBy;
	private Timestamp createdDate;
	
	public ProjectSpec() {
		super();
		tasks = new HashSet<TaskView>();		
	}
	
	public Long   getId()            {return projectId;}
	public String getCreatedBy()     {return createdBy;}
	public Timestamp getCreatedDate(){return createdDate;}
	
	public void setId(Long l)              {this.projectId=l;}
	public void setCreatedBy(String s)     {this.createdBy=s;}
	public void setCreatedDate(Timestamp t){this.createdDate=t;}

	public void add(TaskView tv) {
		tasks.add(tv);
	}
	
	/**
	 * Returns a set of the task views in that make up this project spec.
	 * 
	 * @return A set representation of task views.
	 */
	public Set<TaskView> getTasks() {
		return tasks;
	}
	
	public boolean isReady() {
		for (TaskView t:tasks) {
			if (t.getSequenceLevel()==1)
				return true;
		}
		return false;
	}
	
}
