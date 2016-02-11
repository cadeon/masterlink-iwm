<%@ page import="org.mlink.jfreechart.GanttChart"%>
<%@ page import="java.io.PrintWriter"%>
<%
    //String filename = new GanttChart("blabla").generateImage(session, new PrintWriter(out));
    String filename = request.getParameter("file");
    String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
%>
<html>
<head>
<title>Gantt Chart</title>
</head>
<body>
	<!--img src="<%= graphURL %>" width=500 height=300 border=0 usemap="#<%= filename %>"-->
	<img src="<%= graphURL %>"  border=0 usemap="#<%= filename %>">
</body>
</html>