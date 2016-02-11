package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TasksCriteria extends SearchCriteria{
    protected Integer taskTypeId;

    public TasksCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public TasksCriteria() {
    }

    public Integer getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(Integer taskTypeId) {
        this.taskTypeId = taskTypeId;
    }
}
