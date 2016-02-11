<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>

<html>
<head>
    <title>Application Generated an Error</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/styles/defect-report.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>

    <script type="text/javascript">
        function toggleException() {
            el = document.getElementById('exception-text');
            if (el.style.display == '') {
                el.style.display = 'block';
                document["ic_exc"].src = "<c:url value='/images/minus.gif'/>";
            } else {
                el.style.display = '';
                document["ic_exc"].src = "<c:url value='/images/plus.gif'/>";
            }
        }
    </script>
</head>

<body>
<div id="theBody">
    <div id="header">
        Application Generated an Error
    </div>

    <p/>

    <div id="exception" style="margin-left:20pt;width:650px;">
        We regret that your operation resulted in
        <a href="javascript:toggleException()"> error
            <img src="<c:url value='/images/plus.gif'/>" name="ic_exc" border="0">
        </a>.
        The error is logged and our support team will be informed.
        We encourage you to submit the
        <a href="javascript:popUp('<c:url value='/DefectReport.do?forward=read'/>', 'errorreport', 570,960);">
            Defect report</a> and describe your actions that resulted in this problem.
        <p>
        Your report will ensure that the problem will be resolved promptly.
        Please help us to improve the application!
    </div>

    <div id="exception-text">
        <span class="EMBMessageWarning"><html:errors bundle="iwm.messages"/></span>
        <%
            /** do not know why it is here
             * do not show popus for EXCEPTION
             <logic:notPresent name="org.apache.struts.action.EXCEPTION">
             <html:messages id="popUps" bundle="iwm.messages"/>
             </logic:notPresent>*/
        %>
    </div>
</div>

</body>
</html>