package org.mlink.iwm.struts.form;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.AutoGrowingList;
import org.mlink.iwm.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;


/**
 * User: Andrei
 * Date: May 1, 2006
 * Time: 10:09:33 AM
 */
public class ExternalWorkRequestForm extends MaintRequestForm{
    private static final String STATUS_OPEN="Open";
    private static final String STATUS_CLOSED="Closed";
    private String locationComment;
    private String urgent;
    private String organizationId;
    private String createdDate;
    private String completedDate;
    private String dispatchedDate;
    private java.lang.Integer jobStatusId;
    private java.util.List actions = new ArrayList();
    private java.util.List activeRequests = new ArrayList();
    private java.util.List jobWorkers = new ArrayList();


    public String getDispatchedDate() {
        return dispatchedDate;
    }

    public void setDispatchedDate(String dispatchedDate) {
        this.dispatchedDate = dispatchedDate;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }



    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getLocationComment() {
        return locationComment;
    }

    public void setLocationComment(String locationComment) {
        this.locationComment = locationComment;
    }


    public List getActiveRequests() {
        return activeRequests;
    }
    public String getLocatorName() {
        return org.mlink.iwm.lookup.LocatorRef.getDescription(StringUtils.getLong(getLocatorId()));
    }

    public String getProblemDesc() {
        return org.mlink.iwm.lookup.ExternalWorkRequestProblemRef.getLabel(StringUtils.getLong(getProblemId()));
    }

    public void setActiveRequests(List activeRequests) {
        this.activeRequests = activeRequests;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(Integer jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public String getStatus() {
        int id = ConvertUtils.intVal(jobStatusId);
        if( JobStatusRef.getIdByCode(JobStatusRef.Status.DUN) == id ||
           JobStatusRef.getIdByCode(JobStatusRef.Status.EJO) == id ||
           JobStatusRef.getIdByCode(JobStatusRef.Status.CIA) == id)
            return STATUS_CLOSED;
        else
            return STATUS_OPEN;
    }

    public boolean getIsResolved() {
        int id = ConvertUtils.intVal(jobStatusId);
        return JobStatusRef.getIdByCode(JobStatusRef.Status.DUN) == id;
    }

    public List getActions() {
        return actions;
    }

    public void setActions(List actions) {
        this.actions = actions;
    }

    public List getWorkers() {
        return jobWorkers;
    }

    public void setWorkers(List jobWorkers) {
        this.jobWorkers = jobWorkers;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        urgent = null;
        organizationId = null;
        locationComment = null;
        createdDate = null;
        completedDate = null;
        jobStatusId = null;
        activeRequests = new AutoGrowingList(ExternalWorkRequestForm.class);
        actions = new ArrayList();
        jobWorkers = new ArrayList();
    }
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
}
