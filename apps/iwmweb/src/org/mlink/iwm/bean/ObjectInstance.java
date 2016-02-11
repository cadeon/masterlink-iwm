package org.mlink.iwm.bean;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 26, 2006
 */
public class ObjectInstance extends ObjectCommon{
    protected java.lang.String objectId;
    protected java.lang.String objectRef;
    protected java.lang.String objectDefId;
    protected java.lang.String locatorId;
    protected java.lang.String fullLocator;
    protected java.lang.String tag;
    protected java.lang.String custom;
    protected java.lang.String hasCustomData;
    protected java.lang.String hasCustomTask;
    protected java.lang.String hasCustomTaskGroup;
    protected java.lang.String active;

    protected java.lang.String objectTypeId;
    protected java.lang.String runHours;
    protected java.lang.String description;
    protected java.lang.String organizationId;
    
    protected java.lang.String childrenCount;

    public ObjectInstance() {

    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public ObjectInstance(Map map) throws Exception{
        CopyUtils.copyProperties(this,map);
    }
    public String getObjectRef() {
        return objectRef;
    }

    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }


    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getFullLocator() {
        return fullLocator;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectDefId() {
        return objectDefId;
    }

    public void setObjectDefId(String objectDefId) {
        this.objectDefId = objectDefId;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getHasCustomData() {
        return hasCustomData;
    }

    public void setHasCustomData(String hasCustomData) {
        this.hasCustomData = hasCustomData;
    }

    public String getHasCustomTask() {
        return hasCustomTask;
    }

    public void setHasCustomTask(String hasCustomTask) {
        this.hasCustomTask = hasCustomTask;
    }

    public String getHasCustomTaskGroup() {
        return hasCustomTaskGroup;
    }

    public void setHasCustomTaskGroup(String hasCustomTaskGroup) {
        this.hasCustomTaskGroup = hasCustomTaskGroup;
    }


    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getRunHours() {
        return runHours;
    }

    public void setRunHours(String runHours) {
        this.runHours = runHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public java.lang.String getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(java.lang.String childrenCount) {
		this.childrenCount = childrenCount;
	}
}
