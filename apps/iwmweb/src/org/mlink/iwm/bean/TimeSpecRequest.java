package org.mlink.iwm.bean;

import org.mlink.iwm.util.CopyUtils;

import java.sql.Date;
import java.util.Map;

/**
 * User: andrei
 * Date: Dec 10, 2006
 */
public class TimeSpecRequest {
    private long personId;
    private long locatorId;
    private long workScheduleId;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private int shiftId;
    private int time;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;

    public TimeSpecRequest(Map map) throws Exception{
        CopyUtils.copyProperties(this,map);
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(long locatorId) {
        this.locatorId = locatorId;
    }

    public long getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(long workScheduleId) {
        this.workScheduleId = workScheduleId;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean getMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean getThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean getFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean getSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }
}
