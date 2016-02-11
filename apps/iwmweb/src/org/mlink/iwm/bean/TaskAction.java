package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Nov 2, 2006
 */
public class TaskAction extends TemplateTaskAction {
    protected String custom;
    protected String taskId;
    protected String actionDefId;

    public String getActionDefId() {
		return actionDefId;
	}

	public void setActionDefId(String actionDefId) {
		this.actionDefId = actionDefId;
	}

	public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
