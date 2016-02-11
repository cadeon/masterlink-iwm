package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Feb 8, 2007
 */
public class ProjectStencilsCriteria extends SearchCriteria{
    Long locatorId;
    Long organizationId;
    String activeStatus = "1";

    public ProjectStencilsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public ProjectStencilsCriteria(){
    }

    public String getActiveStatus() {
        return activeStatus;
    }


    public void setActiveStatus(String active) {
        this.activeStatus = active;
    }

    public Long getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(Long locatorId) {
        this.locatorId = locatorId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
