<%@ page import="net.sf.jasperreports.j2ee.servlets.BaseHttpServlet"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript">
    var scheduledDateStr = new Object();
    scheduledDateStr='<%=request.getParameter("REPORT_DATE")%>';
    var orgIdStr = new Object();
    orgIdStr='<%=request.getParameter("ORGANIZATION_ID")%>';
</script>

<%
//jasperPrintParamName is name of jasperprint object in HttpSession
String jasperPrintParamName = BaseHttpServlet.JASPER_PRINT_REQUEST_PARAMETER;
String jasperPrintParamValue = (String) request.getAttribute(BaseHttpServlet.JASPER_PRINT_REQUEST_PARAMETER);
String reportName=(String)request.getParameter("report");
%>

<head>
    <title>Report</title>
    <link type="text/css" href="styles/IWMMain.css" rel="stylesheet">
</head>

<div style="text-align:right;margin-bottom:0.5em">
	<input type="button" class="button" id="history"  value="Go Back" onclick="history.back();">
    <input type="button" class="button" value="Export to PDF"
           onclick="window.location.href='pdf?<%=jasperPrintParamName%>=<%=jasperPrintParamValue%>';">
	<%if("shift_timings".equals(reportName)){ %>
	<input type="button" class="button" value="Export to CSV" 
			onclick="document.location.href = 'ShiftTimingReport.do?REPORT_DATE='+scheduledDateStr+'&ORGANIZATION_ID='+orgIdStr"/>
	<%} %>
    <input type="button" class="button" value="Export to Excel"
           onclick="window.location.href='xls?<%=jasperPrintParamName%>=<%=jasperPrintParamValue%>';">
    <input type="button" class="button" value="Print Report" onclick="window.print();">
</div>

<script type="text/javascript">
    function init(){
        if(history.length==0) {
            el = document.getElementById("history");
            el.style.display='none';
        }
    }
    window.onload=init;
</script>