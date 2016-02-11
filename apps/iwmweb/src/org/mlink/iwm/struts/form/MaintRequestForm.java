/*---------------------------------iwm
	File: MaintRequestForm.java
	Package: org.mlink.iwm.struts.form
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.struts.form;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;

public class MaintRequestForm   extends BaseForm   {

    protected java.lang.String tenantRequestId;
    protected java.lang.String jobId;
    protected java.lang.String problemId;
    protected java.lang.String tenantEmail;
    protected java.lang.String tenantName;
    protected java.lang.String tenantPhone;
    protected java.lang.String note;
    protected java.lang.String jobDescription;
    protected java.lang.String locatorId;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
    * Accessor: tenantRequestId
    * @return current value for tenantRequestId
    */
    public java.lang.String getTenantRequestId() { return tenantRequestId; }

    /**
    * Accessor: jobId
    * @return current value for jobId
    */
    public java.lang.String getJobId() { return jobId; }

    /**
    * Accessor: problemId
    * @return current value for problemId
    */
    public java.lang.String getProblemId() { return problemId; }

    /**
    * Accessor: tenantEmail
    * @return current value for tenantEmail
    */
    public java.lang.String getTenantEmail() { return tenantEmail; }

    /**
    * Accessor: tenantName
    * @return current value for tenantName
    */
    public java.lang.String getTenantName() { return tenantName; }

    /**
    * Accessor: tenantPhone
    * @return current value for tenantPhone
    */
    public java.lang.String getTenantPhone() { return tenantPhone; }

    /**
    * Accessor: note
    * @return current value for note
    */
    public java.lang.String getNote() {
        return note;
    }



    /**
    *Mutator: tenantRequestId
    * @param value new value for tenantRequestId
    */
    public void setTenantRequestId(java.lang.String value) { this.tenantRequestId = value; }

    /**
    *Mutator: jobId
    * @param value new value for jobId
    */
    public void setJobId(java.lang.String value) { this.jobId = value; }

    /**
    *Mutator: problemId
    * @param value new value for problemId
    */
    public void setProblemId(java.lang.String value) { this.problemId = value; }

    /**
    *Mutator: tenantEmail
    * @param value new value for tenantEmail
    */
    public void setTenantEmail(java.lang.String value) { this.tenantEmail = value; }

    /**
    *Mutator: tenantName
    * @param value new value for tenantName
    */
    public void setTenantName(java.lang.String value) { this.tenantName = value; }

    /**
    *Mutator: tenantPhone
    * @param value new value for tenantPhone
    */
    public void setTenantPhone(java.lang.String value) { this.tenantPhone = value; }

    /**
    *Mutator: note
    * @param value new value for note
    */
    public void setNote(java.lang.String value) { this.note = value; }



    public void reset(ActionMapping mapping, HttpServletRequest request){
    super.reset(mapping, request);
        tenantRequestId= null;
        jobId= null;
        problemId= null;
        tenantEmail= null;
        tenantName= null;
        tenantPhone= null;
        jobDescription=null;
        note= null;
        locatorId = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        ActionErrors errors = super.validate(mapping, request);

        return errors;
    }

}
// EOF:  MaintRequestForm.java
