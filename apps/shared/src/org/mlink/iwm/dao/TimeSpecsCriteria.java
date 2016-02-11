package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Nov 15, 2006
 */
public class TimeSpecsCriteria extends SearchCriteria{
    Long locatorId;
    Long organizationId;
    Integer shiftId;
    public enum DATERANGE {All,Last3Months,LastMonth,LastWeek,NextWeek,NextMonth,Next3Months,ExactDate}

    private String dateRange;
    private String date;


    public TimeSpecsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public TimeSpecsCriteria() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getLocatorId() {
        return locatorId;
    }

    public String getDateRange() {
        return dateRange;
    }

    public DATERANGE getDATERANGE(){
        for(DATERANGE dr:DATERANGE.values()){
            if(dr.toString().equals(dateRange)) return dr;
        }
        return DATERANGE.All;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public void setLocatorId(Long locatorId) {
        this.locatorId = locatorId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

}
