package org.mlink.iwm.bean;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei    WorksSchedule is very close to TimeSpec by meaning. Consider refactoring and merging
 * Date: Nov 17, 2006
 */
public class WorkSchedule extends Worker{
	private String id;
    private String isAssigned;
    private String isSticky;
    private String jobId;
    private Date day;
    private String totalTime;
    private Collection<Job> jobs;
    private String organizationName;
    private String status;
    private String scheduledTime;
    private String scheduledBy;

  	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public WorkSchedule(Map map) throws Exception{
        CopyUtils.copyProperties(this,map);
    }

    public WorkSchedule(){
    }

    public String getScheduledTimeDisplay(){
        return getScheduledTime();
    }

    public String getScheduledTime(){
    	return this.scheduledTime;
    }
    
    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getScheduledBy() {
        return scheduledBy==null?"System":scheduledBy;
    }

    public void setScheduledBy(String scheduledBy) {
        this.scheduledBy = scheduledBy;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getDay() {
        return day;
    }

    public String getDayDisplay() {
        String s=null;
        if(day!=null) {
            SimpleDateFormat sdfout = new SimpleDateFormat(Config.getProperty("day.pattern"));
            s= sdfout.format(day);
        }
        return s;
    }

    public String getTotalTime() {
        return StringUtils.parseMinutes(totalTime);
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Collection<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Collection<Job> jobs) {
        this.jobs = jobs;
    }

    public String getIsAssigned() {
        return isAssigned;
    }

    public boolean isAssigned() {
        return "1".equals(isAssigned);
    }

    public void setIsAssigned(String assigned) {
        isAssigned = assigned;
    }

    public String getJobCount() {
        return jobs==null?"0":String.valueOf(jobs.size());
    }

    public String getIsSticky() {
        return isSticky;
    }

    public void setIsSticky(String sticky) {
        isSticky = sticky;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
