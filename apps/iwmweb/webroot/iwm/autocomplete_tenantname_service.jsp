<%@ taglib uri="jstl-sql" prefix="sql" %>
<%@ taglib uri="jstl-core" prefix="c" %>


<%
    /* TODO:
     1.should redesign as jMaki component unless we are not using autocomplete anywhere but here
     2. org.apache.taglibs.standard.tag.el.sql.QueryTag is not closing ResultSet in doEnd which
            make JBoss org.jboss.resource.adapter.jdbc.WrappedStatement angry with message
            'Closing a result set you left open! Please close it yourself'
            Moreover this is not considered big issue by apache, see http://issues.apache.org/bugzilla/show_bug.cgi?id=17388
            I do not like the exceptions in the log. Need to write our own implementation!
     */
    String tname = request.getParameter("tenantName");
    if(tname!=null) tname = tname.trim().toLowerCase();
    pageContext.setAttribute("tname",tname);
%>

<sql:query var="tenants" maxRows="10" dataSource="iwm_ds" >
    select distinct tenant_name , email, phone from tenant_request_unique where lower(tenant_name) like ?
    <sql:param value="%${tname}%" />
</sql:query>

<ul>
    <c:forEach var="row" items="${tenants.rows}">
        <li>
            <div class="selected"><c:out value="${row.tenant_name}"/></div>
            <div class="extrainfo">
                <span class="email"><c:out value="${row.email}"/></span>
                <span class="phone"><c:out value="${row.phone}"/></span>
            </div>
        </li>
    </c:forEach>
</ul>

