package org.mlink.iwm.bean;

import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Feb 8, 2007
 */
public class ProjectStencil {
    private String id;
    private String name;
    private String organization;
    private String taskCount;
    private String autoplanning;
    private String description;

    private String frequencyId;
    private String projectTypeId;
    private String active;
    private String frequencyValue;
    private String locatorId;
    private String organizationId;
    private String startDate;
    private String lastPlannedDate;

    private String createdBy;
    private String createdDate;
    
    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastPlannedDate() {
        return lastPlannedDate;
    }

    public void setLastPlannedDate(String lastPlannedDate) {
        this.lastPlannedDate = lastPlannedDate;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(String frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public String getFullLocator() {
        return LocatorRef.getFullLocator(StringUtils.getLong(locatorId));
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getAutoplanning() {
        return autoplanning;
    }

    public String getAutoplanningDisplay() {
        return "1".equals(autoplanning)?"Auto":"Manual";
    }

    public void setAutoplanning(String autoplanning) {
        this.autoplanning = autoplanning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
