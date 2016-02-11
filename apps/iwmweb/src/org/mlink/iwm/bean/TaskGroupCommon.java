package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TaskGroupCommon {

    protected java.lang.String id;
    protected java.lang.String description;
    protected java.lang.String taskCount;
    protected java.lang.String skillTypeId;
    protected java.lang.String skillType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getSkillTypeId() {
        return skillTypeId;
    }

    public void setSkillTypeId(String skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
}
