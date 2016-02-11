package org.mlink.iwm.web.bean;

import java.util.LinkedList;

public class WorkerOp implements BaseExtOp{
	private String name;
	private String username;
	private String password;
	private long id;
	private String errStr;
	private double latitude;
	private double longitude;
	private int accuracy;
			
	private LinkedList<WorkScheduleOp> workScheduleOps;
	
	public WorkerOp(){
	}
	
	public String getErrStr() {
		return errStr;
	}

	public void setErrStr(String errStr) {
		this.errStr = errStr;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public LinkedList<WorkScheduleOp> getWorkScheduleOps() {
		return workScheduleOps;
	}

	public void setWorkScheduleOps(LinkedList<WorkScheduleOp> workScheduleOps) {
		this.workScheduleOps = workScheduleOps;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
}
