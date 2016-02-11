package org.mlink.iwm.bean;

import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Jan 12, 2007
 */
public class JobTaskTime extends JobTask{
    private String time;
    private String jobTaskId;

    public String getTimeParsed() {
       return StringUtils.parseMinutes(time);
    }

    public String getTime() {
       return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJobTaskId() {
        return jobTaskId;
    }

    public void setJobTaskId(String jobTaskId) {
        this.jobTaskId = jobTaskId;
    }
}
