<%@ page import="java.util.List"%>
<%@ page import="org.mlink.iwm.bean.ShiftTiming"%>
<%@ page import="org.mlink.iwm.util.Config"%>

<%@ page contentType="text/csv; charset=UTF-8" %>

<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>

<%
String scheduledDateStr=(String)request.getParameter("REPORT_DATE");
String delimiter=Config.getProperty(Config.REPORT_OUT_DELIMITER);
%>
<html>
<head>
    <title>Shift Timing Report for:<%=scheduledDateStr%></title>
</head>
<body>

<html:form action="ShiftTimingReport">
	Shift Timing Report for:<%=scheduledDateStr%><br/>
	Organization Name<%=delimiter%>Name<%=delimiter%>Start Time<%=delimiter%>End Time<%=delimiter%>Total Time<%=delimiter%>Break Time<%=delimiter%>Paid Time<%=delimiter%>Job Time<%=delimiter%>NPT<br/>
       <c:forEach items="${CURRENT_FORM.items}" var="item">
			<bean:write name="item" property="orgName"/><%=delimiter%><bean:write name="item" property="userName"/><%=delimiter%><bean:write name="item" property="startDate"/><%=delimiter%><bean:write name="item" property="stopDate"/><%=delimiter%><bean:write name="item" property="shiftDurIncBreaks"/><%=delimiter%><bean:write name="item" property="timeOnBreaks"/><%=delimiter%><bean:write name="item" property="shiftDur"/><%=delimiter%><bean:write name="item" property="timeOnJobTasks"/><%=delimiter%><bean:write name="item" property="npt"/><br/>
		</c:forEach>
</html:form>
</body>
</html>