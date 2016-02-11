<%@ page import="org.mlink.reports.ReportWrapper"%>
<%@ page import="java.util.List"%>
<%@ page import="org.mlink.reports.ReportsDigester"%>

<h3>Reports</h3>

<%
    List files = (List)request.getSession().getServletContext().getAttribute(ReportsDigester.class.getName());
    for (int i = 0; i < files.size(); i++) {
        ReportWrapper rw = (ReportWrapper)files.get(i);
%>
            <div class="report">
                    <a href="javascript:openWindow('ReadReport.do?report=<%= rw.getFileName()%>','report', 600, 800)">
                <%= rw.getTitle() %>  </a>
                - <%= rw.getDescription() %>
            </div>
		<%
    }
		%>
</table>
