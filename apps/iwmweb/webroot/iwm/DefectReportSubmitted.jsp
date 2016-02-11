<%@ page import="org.mlink.iwm.struts.action.BaseAction"%>
<%@ page import="org.mlink.iwm.util.Constants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-fmt" prefix="fmt" %>


<jsp:useBean id="now" class="java.util.Date"/>

<html>
<head>
    <title>Defect Report</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/styles/defect-report.css'/>">
</head>

<body>
<div id="theBody">
    <div id="header">
        Defect Report
    </div>

    <div id="content">
        <div id="defectInfo">
            Your report has been submitted to the MasterLink support team.
            <p>
            <fieldset>
                <legend>Summary</legend>
                <div class="formField">
                    <label>Date</label>
                    <fmt:formatDate value="${now}"/>
                </div>
                <div class="formField">
                    <label>Issue Name</label>
                    <c:out value="${DefectReportForm.issueName}"/>
                </div>
                <div class="formField">
                    <label>Description</label>
                    <c:out value="${DefectReportForm.note}" /> 
                </div>
            </fieldset>

            <p>
            <div>Thank you for helping to make the application better!</div>

            <button class="button" style="margin-top:20pt;"
                    onclick="alert('Thank you for submitting your report'); window.close();"> Close </button>

        </div>
    </div>

    <div id="footer">&copy; 2006 MasterLink Corporation, All Rights Reserved </div>

</div>
</body>

<%
    request.getSession().removeAttribute(Constants.USER_EXCEPTION);
    request.getSession().removeAttribute(Constants.USER_EXCEPTION_REF);
%>

</html>