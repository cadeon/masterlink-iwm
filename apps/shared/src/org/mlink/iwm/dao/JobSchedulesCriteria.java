package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;
import java.sql.Date;

/**
 * User: andrei
 * Date: Nov 17, 2006
 */
public class JobSchedulesCriteria extends SearchCriteria{
    private Date startDate;
    private Date endDate;

    public JobSchedulesCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
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

}
