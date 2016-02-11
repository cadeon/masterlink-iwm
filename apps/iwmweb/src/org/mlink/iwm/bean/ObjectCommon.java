package org.mlink.iwm.bean;

import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectCommon {
    protected String classId;
    protected String classCode;
    protected String taskCount;
    protected String taskGroupCount;
    protected String dataCount;


    public ObjectCommon() {
    }

    public String getDataCount() {
        return dataCount;
    }

    public void setDataCount(String dataCount) {
        this.dataCount = dataCount;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String code) {
        this.classCode = code;
    }

    public String getClassDesc() {
        return  org.mlink.iwm.lookup.TargetClassRef.getLabel(StringUtils.getLong(getClassId()));
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getTaskGroupCount() {
        return taskGroupCount;
    }

    public void setTaskGroupCount(String taskGroupCount) {
        this.taskGroupCount = taskGroupCount;
    }

}
