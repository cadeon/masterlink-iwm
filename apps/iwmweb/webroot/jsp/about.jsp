<%@ page import="org.mlink.iwm.util.Config"%>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<h3>IWM version 3.5</h3>

<h3>Build Propeties</h3>
<c:forEach var="item" items="${buildprops}">
    <ul>
        <li><c:out value="${item.key}"/>=<c:out value="${item.value}"/></li>
    </ul>
</c:forEach>
<h3>Runtime Propeties</h3>
<ul>
    <li>java.home=<%=System.getProperty("java.home")%></li>
    <li>java.version=<%=System.getProperty("java.version")%></li>
    <li>os.name=<%=System.getProperty("os.name")%></li>
    <li>Host=<%=java.net.InetAddress.getLocalHost().getHostName()%></li>
    <li>server=<%=System.getProperty("jboss.home.dir")%></li>    
    <li>schema=<%=Config.getProperty(Config.PRODUCTION_SCHEMA)%></li>
    <li>system.mail.address=<%=System.getProperty("system.mail.address")%></li>

</ul>
<%/*h3>System Propeties</h3>
<c:forEach var="item" items="${systemprops}">
    <ul>
        <li><c:out value="${item.key}"/>=<c:out value="${item.value}"/></li>
    </ul>
</c:forEach*/%>

<h3>Links</h3>
    <ul>
        <li>
            <a href="Uploads.do">
                Uploads </a>
        </li>
                <li>
            <a href="MobileWorkerList.do">
                Browse Worker Schedules </a>
        </li>
        <li>
            <a href="ScheduledJobs.do">
                Browse Scheduled Jobs </a>
        </li>

        <li>
            <a href="AgentTools.do">
                Agent Tools </a>
        </li>
        
        <li>
            <a href="SystemPropList.do">
                System Properties</a>
        </li>
        
        <li>
            <a href="ShiftRefList.do">
                Shift Refs</a>
        </li>
        
        <li>
            <a href="SkillTypeList.do">
                Skill Types</a>
        </li>
        
        <li>
            <a href="MWLocationList.do">
                Mobile Worker Location</a>
        </li>
    </ul>
</h3>


