package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 1, 2006
 */
public class ObjectTask extends TemplateTask {
    private    String custom;
    private    String active;
	private String startDate;
	private String lastPlannedDate;
	private String runHoursThresh;
    private String scheduleResponsibilityId;
    private String organizationId;
    private String objectId;


    private ServicePlan servicePlan = new ServicePlan();

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getScheduleResponsibilityId() {
        return scheduleResponsibilityId;
    }

    public void setScheduleResponsibilityId(String scheduleResponsibilityId) {
        this.scheduleResponsibilityId = scheduleResponsibilityId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getLastPlannedDate() {
        return lastPlannedDate;
    }

    public void setLastPlannedDate(String lastPlannedDate) {
        this.lastPlannedDate = lastPlannedDate;
    }

    public String getRunHoursThresh() {
        return runHoursThresh;
    }

    public void setRunHoursThresh(String runHoursThresh) {
        this.runHoursThresh = runHoursThresh;
    }

    public ServicePlan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(ServicePlan servicePlan) {
        this.servicePlan = servicePlan;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
