package org.mlink.iwm.bean;

import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Nov 9, 2006
 */
public class JobTask {

    protected String id;
    protected String description;
    protected String estTime;
    protected String totalTime;
    protected String skillType;
    protected String taskType;
    protected String actionCount;

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

    public String getEstTime() {
        return StringUtils.parseMinutes(estTime);
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getTotalTime() {
        return StringUtils.parseMinutes(totalTime);
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getActionCount() {
        return actionCount;
    }

    public void setActionCount(String actionCount) {
        this.actionCount = actionCount;
    }


}
