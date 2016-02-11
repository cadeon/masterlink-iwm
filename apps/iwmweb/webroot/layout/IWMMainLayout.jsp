<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>


<tiles:importAttribute/>
<%/**
response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
response.setHeader("Pragma", "No-cache");       // Compatibility with http 1.0
response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
*/%>



<html>
<head>
<logic:present name="title">
    <title><tiles:getAsString name="title"/></title>
</logic:present>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/WebAppStyles.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/calendar/calendar-win2k-1.css" type="text/css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/ext-work-request.css">


<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/StaticJS.jsp"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/common.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/calendar/calendar-setup.js"></script>


</head>

<body class="PageBodyColor" id="bodyid">

        <logic:messagesPresent message="iwm.messages" >
            <logic:notPresent name="org.apache.struts.action.EXCEPTION">
                <c:set var="popUps" value=""/>
                <html:messages id="msg" bundle="iwm.messages" >
                    <c:set value="${popUps} \n ${msg}" var="popUps" />
                </html:messages>
            </logic:notPresent>
        </logic:messagesPresent>

        <c:set value="BorderColor" var="borderColor"/>
        <c:set value="140" var="tabOffset"/>
        <c:set value="60" var="leftPanelWidth"/>
        <c:set value="20" var="titleOffset"/>

        <logic:present name="menu">
            <c:set value="HeaderBgColor" var="borderColor"/>
            <c:set value="${leftPanelWidth+60}" var="leftPanelWidth"/>
            <c:set value="0" var="titleOffset"/>
        </logic:present>

    <!-- HEADER TABLE -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">

      <tr class="HeaderBgColor" height="20">
        <td colspan="3" align="center">
            <logic:present name="homebar">
                <tiles:insert attribute='homebar'/>
            </logic:present>
        </td>
      </tr>

      <tr>
        <td colspan="3" height="8" class="HeaderBgColor"></td>
      </tr>

      <tr class="HeaderBgColor" >
        <td colspan="2">
            <html-el:img page="/images0/Transparent.gif" width="${tabOffset+20}" height="20" border="0"  />
        </td>
        <td>
            <logic:present name="navbartop">
                <tiles:insert attribute='navbartop'/>
            </logic:present>
        </td>
      </tr>

      <tr class="HeaderBgColor">
        <td colspan="2"/>
        <td>
            <logic:present name="navbar">
                <tiles:insert attribute='navbar'/>
            </logic:present>
        </td>
      </tr>
    </table>

    <!-- APPLICATION SPACE TABLE -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">

	  <tr>
        <td class="HeaderBgColor" width="10"/>
        <td class="<c:out value='${borderColor}'/>" valign="bottom">
            <html-el:img page="/images0/Transparent.gif" width="${leftPanelWidth}" height="1" border="0"  />
            <hr>
        </td>

	    <td align="left" class="ScreenName" height="40" valign="bottom" >
                    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                   <logic:present name="title">
                        <tiles:getAsString name="title"/>
                    </logic:present>
                    <hr>
        </td>
	  </tr>

      <%/*logic:messagesPresent message="iwm.messages" >
      <tr>
        <td class="HeaderBgColor" width="10"/>
        <td class="<c:out value='${borderColor}'/>">
            <html-el:img page="/images0/Transparent.gif" width="${leftPanelWidth}" height="1" border="0"  />
        </td>

        <td>
		    <span class="MessageWarning"><!--html:errors  bundle="iwm.messages"/--></span>
            <!-- do not show popus for EXCEPTION*-->
            <logic:notPresent name="org.apache.struts.action.EXCEPTION">
                <c:set var="popUps" value=""/>
                <html:messages id="msg" bundle="iwm.messages" >
                    <c:set value="${popUps} \n ${msg}" var="popUps" />
                </html:messages>
            </logic:notPresent>
		</td>
        </tr>
       </logic:messagesPresent*/%>


      <tr>
        <td class="HeaderBgColor">
            <html-el:img page="/images0/Transparent.gif" width="10" height="1" border="0"  />
        </td>
        <logic:present name="menu">
                <td valign="top" width="<c:out value='${leftPanelWidth}'/>" class="HeaderBgColor">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr><td>
                            <tiles:insert attribute='menu'/>
                        </td></tr>
                    </table>
                </td>
        </logic:present>
        <logic:notPresent name="menu">
            <td class="BorderColor" width = "<c:out value='${leftPanelWidth}'/>"></td>
        </logic:notPresent>

        <td valign="top">
        <table align="left" border="0" width="860">
                        <logic:present name="body">
                            <tr><td><tiles:insert attribute='body' /></td></tr>
                        </logic:present>
                        <logic:present name="body2">
                            <tr><td><tiles:insert attribute='body2' /></td></tr>
                        </logic:present>
        </table>

        </td>
      </tr>
    <!--/table-->



    <!-- FOOTER TABLE -->
    <!--table width="95%" border="0" cellspacing="0" cellpadding="0"-->
      <tr>
        <td colspan="3" height="20" class="HeaderBgColor"><span class="LinkCopyright">&copy; 2006 MasterLink Corporation, All Rights Reserved</span></td>
      </tr>

    </table>
</body>
</html>

  <%-- handle pop up messages --%>
   <script language="JavaScript">
   <!--
   function messages()   {
      <logic:notEmpty  name="CURRENT_FORM">
      <logic:notEmpty property="message" name="CURRENT_FORM">
        alert('<c:out  value="${CURRENT_FORM.message}"/>');
      </logic:notEmpty>
      </logic:notEmpty>

      <c:if test="${popUps != null}">
        alert("<c:out value="${popUps}" escapeXml="" />");
      </c:if>
   }

   var initCheck;
   function init()   {
      if (window.document.readyState == "complete")      {
         clearTimeout(initCheck);
         messages();
      }
      else      {
         initCheck = setTimeout("init()", 10);
      }
   }

   //init();   //window.document.readyState is only IE property
   messages();
   -->
   </script>
