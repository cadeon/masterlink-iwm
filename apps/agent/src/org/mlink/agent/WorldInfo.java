package org.mlink.agent;

import java.util.Date;

public interface WorldInfo {

	public Long    getId();
	public String  getName();
	public String  getSchema();
	public Integer getSimStatus();
	public Date    getLastSimulationRun();
	public String  getParent();
	
	public void setLastSimulationRun(Date d);
}
