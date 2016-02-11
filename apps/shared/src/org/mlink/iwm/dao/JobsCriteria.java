package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 31, 2006
 */
public class JobsCriteria extends ObjectsCriteria{
    Long organizationId;
    Long objectId;
    Integer jobTypeId;
    Integer jobStatusId;
    Integer skillTypeId;
    Long projectId;
    Integer jobCategory;
    public enum FailureMode{None,NoWorker,WorkerAvailableNotAutoAssigned, ManualSchedule,NYAJobs,SoonToExpire,Expired,MustAssignSkill}
    FailureMode failureMode;
    String failure;
    Long completingWorkerId;


    public JobsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public JobsCriteria() {
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public FailureMode getFailureMode() {
        FailureMode rtn;
        if(FailureMode.NoWorker.toString().equals(failure))  rtn =  FailureMode.NoWorker;
        else if(FailureMode.WorkerAvailableNotAutoAssigned.toString().equals(failure))  rtn =  FailureMode.WorkerAvailableNotAutoAssigned;
        else if(FailureMode.ManualSchedule.toString().equals(failure))  rtn =  FailureMode.ManualSchedule;
        else if(FailureMode.NYAJobs.toString().equals(failure))  rtn =  FailureMode.NYAJobs;
        else if(FailureMode.SoonToExpire.toString().equals(failure))  rtn =  FailureMode.SoonToExpire;
        else if(FailureMode.Expired.toString().equals(failure))  rtn =  FailureMode.Expired;
        else if(FailureMode.MustAssignSkill.toString().equals(failure))  rtn =  FailureMode.MustAssignSkill;
        else rtn =  FailureMode.None;
        return rtn;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Integer getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(Integer jobCategory) {
        this.jobCategory = jobCategory;
    }

    public Integer getJobType() {
        return jobTypeId;
    }

    public void setJobType(Integer jobTypeId) {
        this.jobTypeId = jobTypeId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getJobStatus() {
        return jobStatusId;
    }

    public void setJobStatus(Integer jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public Integer getSkillType() {
        return skillTypeId;
    }

    public void setSkillType(Integer skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

	public Long getCompletingWorkerId() {
		return completingWorkerId;
	}
	
	public void setCompletingWorkerId(Long completingWorkerId) {
        this.completingWorkerId = completingWorkerId;
    }

	
}
