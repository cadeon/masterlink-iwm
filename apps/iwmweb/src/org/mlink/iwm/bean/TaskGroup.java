package org.mlink.iwm.bean;

import java.util.List;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TaskGroup extends TaskGroupCommon{
    protected java.lang.String objectId;
    protected java.lang.String taskGroupDefId;
    protected java.lang.String custom;
    private List<ObjectTask> tasks;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTaskGroupDefId() {
        return taskGroupDefId;
    }

    public void setTaskGroupDefId(String taskGroupDefId) {
        this.taskGroupDefId = taskGroupDefId;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public List<ObjectTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ObjectTask> tasks) {
        this.tasks = tasks;
    }
}
