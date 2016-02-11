package org.mlink.iwm.bean;

import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.StringUtils;
import org.mlink.iwm.lookup.ShiftRef;
import org.mlink.iwm.lookup.WorkScheduleStatusRef;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
/**
 * User: andrei
 * Date: Nov 15, 2006
 */
public class TimeSpec {

    private String id;
    private String fullLocator;
    private Date day;
    private String shiftId;
    private String shiftTime;
    private String name;
    private String personId;
    private String jobCount;
    private String status;
    private String statusDesc;
    protected String hierarchy;


    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullLocator() {
        return fullLocator;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public String getDayDisplay() {
        String s;
        SimpleDateFormat sdfout = new SimpleDateFormat(Config.getProperty("day.pattern"));
        //SimpleDateFormat sdfin = new SimpleDateFormat(SQLDateConverter.getDatePattern());
        s= sdfout.format(day);
        return s;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getShiftStart() {
        return  ShiftRef.getShiftStart(Integer.parseInt(shiftId));
    }


    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getShift() {
        String shiftStart = ShiftRef.getShiftStart(Integer.parseInt(shiftId));
        String shiftEnd = ShiftRef.getShiftEnd(Integer.parseInt(shiftId));
        return StringUtils.parseMinutes(shiftStart) + " - " + StringUtils.parseMinutes(shiftEnd);
    }

    /*public String getShiftTime() {
        String shiftTime = ShiftRef.getShiftTime(Integer.parseInt(shiftId));
        return StringUtils.parseMinutes(shiftTime);
    }*/

    public String getJobCount() {
        return jobCount;
    }

    public void setJobCount(String jobCount) {
        this.jobCount = jobCount;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getDay() {
        return day;
    }

    public boolean getIsResetAllowed(){
        Date today = DateUtils.truncate(new Date(),Calendar.DATE);
        return today.equals(getDay()) && WorkScheduleStatusRef.Status.DUN.equals(getStatus());
    }

    public boolean getIsAbsentAllowed(){
        Date today = DateUtils.truncate(new Date(),Calendar.DATE);
        return today.equals(getDay()) && WorkScheduleStatusRef.Status.NYS.equals(getStatus());
    }


}
