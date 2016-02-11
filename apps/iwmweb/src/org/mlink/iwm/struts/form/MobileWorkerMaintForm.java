package org.mlink.iwm.struts.form;

import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletRequest;

/**
 * User: andrei
 * Date: Dec 15, 2006
 */
public class MobileWorkerMaintForm extends BaseForm{
    private String scheduledDate;
    private String workerId;
    private String status;
    private String breakTimekeeping;
    private String shiftTimekeeping;
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void reset(ActionMapping actionMapping, ServletRequest servletRequest) {
        super.reset(actionMapping, servletRequest);
        scheduledDate=null;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

	public String getBreakTimekeeping() {
		return breakTimekeeping;
	}

	public void setBreakTimekeeping(String breakTimekeeping) {
		this.breakTimekeeping = breakTimekeeping;
	}

	public String getShiftTimekeeping() {
		return shiftTimekeeping;
	}

	public void setShiftTimekeeping(String shiftTimekeeping) {
		this.shiftTimekeeping = shiftTimekeeping;
	}
}
