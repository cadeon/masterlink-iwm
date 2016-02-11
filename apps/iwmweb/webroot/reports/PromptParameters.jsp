<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>

<html>
<head>
    <title>Required Report Parameters</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/dynarch/calendar/calendar-win2k-1.css" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/dynarch/calendar/calendar.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/dynarch/calendar/calendar-en.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/dynarch/calendar/calendar-setup.js"></script>
    <style type="text/css">
        body {
            BACKGROUND-COLOR: #e5e5e5;
            FONT-SIZE: 12px;
            margin-top:8em;
            font-family: Verdana, helvetica, sans-serif;
            margin-left:5em;
        }
    </style>
</head>
<body>

<html:form action="ReadReport.do">
    <nested:hidden property="report"/>
    <table>
        <nested:iterate id="item"  property="parameters" indexId="nIndex" >
            <%/* if property has value do not display, but submit hidden*/%>
            <nested:empty property="errorMessage">
                <input type='hidden'  name='<nested:write property="name"/>' value='<nested:write property="formattedValue"/>'/>
            </nested:empty>
            <%/* otherwise offer the input*/%>
            <nested:notEmpty property="errorMessage">
                <tr>
                    <td><nested:write property="description"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${item.isDate}">
                                <input type='text'  id='<nested:write property="name"/>' name='<nested:write property="name"/>' />
                                <script type="text/javascript">
                                    Calendar.setup(
	                                    {
	                                        inputField  : '<nested:write property="name"/>',              // ID of the input field
	                                        ifFormat    : "%m/%d/%Y",                                     // the date format
	                                        button      : 'trigger.<nested:write property="name"/>',      // ID of the button
	                                        eventName   : "focus"
	                                    }
                             		);
                                </script>
                            </c:when>
                            <c:otherwise>
                                <input type='text'  name='<nested:write property="name"/>' />
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td colspan=2 style="color:red;font-size:10px;font-weight:bold"><nested:write property="errorMessage"/> </td>
                </tr>
            </nested:notEmpty>
        </nested:iterate>
        <tr>
            <TD style="TEXT-ALIGN:RIGHT" colspan="2">
                <BUTTON style="MARGIN-TOP: 0.5em" onclick=document.forms[0].submit();>Submit</BUTTON>
            </TD><td/>
        </tr>
    </table>
</html:form>
</body>
</html>




