package org.mlink.iwm.struts.form;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jul 20, 2006
 */
public class DefectReportForm extends MaintRequestForm{
    private String reportType;
    private String issueName;
    private String exception;
    private String exceptionRef;

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getExceptionRef() {
        return exceptionRef;
    }

    public void setExceptionRef(String exceptionRef) {
        this.exceptionRef = exceptionRef;
    }

}
