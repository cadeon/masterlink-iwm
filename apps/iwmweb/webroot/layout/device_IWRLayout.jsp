<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
    <logic:present name="title">
        <title><tiles:getAsString name="title"/></title>
    </logic:present>

    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/device-int-work-request.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validatorstatic.jsp"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/common.js"></script>

    <script type='text/javascript' src="scripts/prototype.js"></script>
    <script type='text/javascript' src="scripts/class-extend.js"></script>
    <script type='text/javascript' src="<%=request.getContextPath()%>/dwr/engine.js"></script>
    <script type='text/javascript' src="<%=request.getContextPath()%>/dwr/util.js"></script>
<meta name="viewport" content="width=320; initial-scale=1.0 user-scalable=0;" />

</head>


<tiles:insert attribute='body' />


</html>

