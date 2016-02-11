package org.mlink.iwm.bean;

import org.mlink.iwm.lookup.LocatorRef;
import org.mlink.iwm.util.StringUtils;

/**
 * User: andrei
 * Date: Nov 21, 2006
 */
public class TenantRequest {

    private String id;
    private String jobId;
    private String status;
    private String jobDescription;
    private String fullLocator;
    private String note;
    private String requestType;
    private String createdDate;
    private String tenantEmail;
    private String tenantName;
    private String tenantPhone;
    private String problemId;
    private String locatorId;

    public String getProblemDesc() {
        return org.mlink.iwm.lookup.ExternalWorkRequestProblemRef.getLabel(StringUtils.getLong(getProblemId()));
    }
    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getFullLocator() {
        String rtn =fullLocator;
        if(rtn==null && locatorId!=null) {//try locatorid
              rtn = LocatorRef.getFullLocator(Long.valueOf(locatorId));
        }
        return rtn;
    }

    public void setFullLocator(String fullLocator) {
        this.fullLocator = fullLocator;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
