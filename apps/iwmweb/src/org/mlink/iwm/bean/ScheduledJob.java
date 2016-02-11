package org.mlink.iwm.bean;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 5, 2007
 * Time: 7:26:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledJob extends TimeSpec{
    private String jobStatus;
    private String priority;
    private String estTime;
    private String skill;
    private String skillLevel;

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}
