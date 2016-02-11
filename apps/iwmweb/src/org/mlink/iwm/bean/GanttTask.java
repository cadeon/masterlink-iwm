package org.mlink.iwm.bean;

import java.util.Date;

public class GanttTask{
    String description;
    Date earliestStart;
    Date startedDate;
    String estTime;
    String totalTime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEarliestStart() {
        return earliestStart;
    }

    public void setEarliestStart(Date earliestStart) {
        this.earliestStart = earliestStart;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}

