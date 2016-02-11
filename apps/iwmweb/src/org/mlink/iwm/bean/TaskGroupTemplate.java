package org.mlink.iwm.bean;

import java.util.List;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TaskGroupTemplate extends TaskGroupCommon{
    private String instanceCount;
    private String objectDefId;
    private List<TemplateTask> tasks;
    

    public String getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(String instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getObjectDefId() {
        return objectDefId;
    }

    public void setObjectDefId(String objectDefId) {
        this.objectDefId = objectDefId;
    }

    public List<TemplateTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<TemplateTask> tasks) {
        this.tasks = tasks;
    }

}
