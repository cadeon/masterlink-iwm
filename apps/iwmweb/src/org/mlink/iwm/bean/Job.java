package org.mlink.iwm.bean;

import java.text.SimpleDateFormat;

import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OrganizationRef;
import org.mlink.iwm.lookup.ScheduleResponsibilityRef;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Nov 7, 2006
 */
public class Job {
    private    String id;
    private    String status;
    private    String objectId;
    private    String objectRef;
    private    String objectClassId;
    private    String locatorId;
    private    String description;
    private    String fullLocator;
    private    String skillType;
    private    String jobType;
    private    String taskCount;
    private    String createdDate;
    private    String sticky;
    private    String priority;
    private    String age;
    private    String refId;

    private String completedDate;
    private String priorityId;
    private String estTime;
    private String earliestStart;
    private String latestStart;
    private String lastUpdated;
    private String finishBy;
    private String createdBy;
    private String dispatchedDate;
    private String startedDate;
    private String jobTypeId;
    private String projectId;
    private String statusId;
    private String skillTypeId;
    private String skillLevelId;
    private String numberOfWorkers;
    private String scheduleResponsibilityId;
    private String organizationId;
    private String scheduledDate;
    private String note;
    private String tenantRequestId;
    private String tenantName;
    private String tenantPhone;
    private String tenantEmail;
    private String tenantNote;

    private String scheduledTime;
    private String scheduledBy;
    private String totalTime;

    public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public String getObjectClassId() {
        return objectClassId;
    }

    public void setObjectClassId(String objectClassId) {
        this.objectClassId = objectClassId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSticky() {
        return sticky;
    }

    public void setSticky(String sticky) {
        this.sticky = sticky;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjectRef() {
        return objectRef;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullLocator() {
        return fullLocator;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getEarliestStart() {
        return earliestStart;
    }

    public void setEarliestStart(String earliestStart) {
        this.earliestStart = earliestStart;
    }

    public String getLatestStart() {
        return latestStart;
    }

    public void setLatestStart(String latestStart) {
        this.latestStart = latestStart;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFinishBy() {
        return finishBy;
    }

    public void setFinishBy(String finishBy) {
        this.finishBy = finishBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(String dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getEstTimeDisp() {
        return StringUtils.parseMinutes(estTime);
    }

    public String getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(String jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getSkillTypeId() {
        return skillTypeId;
    }


    public void setSkillTypeId(String skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public String getSkillLevelId() {
        return skillLevelId;
    }

    public void setSkillLevelId(String skillLevelId) {
        this.skillLevelId = skillLevelId;
    }

    public String getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(String numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public String getScheduleResponsibilityId() {
        return scheduleResponsibilityId;
    }

    public String getScheduleResponsibility() {
        return   LookupMgr.getInstance(ScheduleResponsibilityRef.class).getLabel(scheduleResponsibilityId);

    }

    public void setScheduleResponsibilityId(String scheduleResponsibilityId) {
        this.scheduleResponsibilityId = scheduleResponsibilityId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getOrganization() {
        return LookupMgr.getInstance(OrganizationRef.class).getLabel(organizationId);
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String getTenantRequestId() {
		return tenantRequestId;
	}

	public void setTenantRequestId(String tenantRequestId) {
		this.tenantRequestId = tenantRequestId;
	}

    public String getTenantNote() {
        return tenantNote;
    }

    public void setTenantNote(String tenantNote) {
        this.tenantNote = tenantNote;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }
    
    public String getScheduledTimeDisplay(){
        return getScheduledTime();
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

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

}
