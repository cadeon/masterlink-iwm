package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public class ObjectsCriteria extends SearchCriteria {
    Long locatorId;
    Long classId;
    Long organizationId;

    public ObjectsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public ObjectsCriteria(){
    }

    public void setLocatorId(Long locatorId) {
        this.locatorId = locatorId;
    }

    public Long getLocatorId() {
        return locatorId;
    }
    
    public Long getOrganizationId() {
    	return organizationId;
    }
    
    public void setOrganizationId(Long organizationId) {
    	this.organizationId = organizationId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

}
