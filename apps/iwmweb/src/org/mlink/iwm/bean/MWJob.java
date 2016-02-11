package org.mlink.iwm.bean;

import org.mlink.iwm.lookup.PriorityRef;


/**
 * User: andrei
 * Date: Dec 15, 2006
 */
public class MWJob extends Job{
    public enum Status {Open,Closed}

    private String jobScheduleId;


    public String getPriority() {
        if(getPriorityId()!=null)
            return PriorityRef.getLabel(Integer.parseInt(getPriorityId()));
        else return null;
    }

    public String getCompletionStatus() {
        return (null!=getCompletedDate()?Status.Closed.toString():Status.Open.toString());
    }

    public String getJobScheduleId() {
        return jobScheduleId;
    }

    public void setJobScheduleId(String jobScheduleId) {
        this.jobScheduleId = jobScheduleId;
    }


}
