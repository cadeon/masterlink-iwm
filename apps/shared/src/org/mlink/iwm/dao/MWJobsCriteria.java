package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Dec 15, 2006
 */
public class MWJobsCriteria extends ObjectsCriteria{
	String type;
	String status;
    String scheduledDate;

    public MWJobsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public String getCompletionStatus() {
        return status;
    }

    public void setCompletionStatus(String status) {
        this.status = status;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

