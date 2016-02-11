<%@ page import="org.mlink.iwm.util.ForwardUtils"%>
<%@ taglib uri="struts-tiles" prefix="tiles" %>         
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="iwm" prefix="iwm" %>                                                                    
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

<%--
  User: jmirick
  Date: Oct 5, 2006
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
  <head>

      <title><tiles:insert attribute="title"/></title>

      <script src="scripts/prototype.js" type="text/javascript"></script>
      <script src="scripts/PopupManager.js" type="text/javascript"></script>
      <script src="scripts/IWMmenu.js" type="text/javascript"></script>
      <script src="scripts/EventThrottledTextInput.js" type="text/javascript"></script>

      <link type="text/css" href="styles/IWMMain.css" rel="stylesheet">
      <link type="text/css" href="styles/tableLayout.css" rel="stylesheet">
      <link type="text/css" href="styles/popup.css" rel="stylesheet">

      <!-- ANDREI  CODE -->
      <script type='text/javascript' src="scripts/class-extend.js"></script>
      <script type='text/javascript' src="resources/scriptaculous/scriptaculous.js?load=effects,dragdrop"></script>

      <script type='text/javascript' src="scripts/common.js"></script>
      <script type='text/javascript' src="scripts/tablesort.js"></script>
      <script type='text/javascript' src="dwr/engine.js"></script>
      <script type='text/javascript' src="dwr/util.js"></script>
      <script type='text/javascript' src="scripts/dwrsetup.jsp"></script>
      <script type='text/javascript' src="scripts/validatorstatic.jsp"></script>

      <script type="text/javascript" src="scripts/IWMCommon.js"></script>
      <script type='text/javascript' src="scripts/SessionMonitor.js"></script>
      <script type='text/javascript' src="dwr/interface/SessionUtil.js"></script>
      <script type='text/javascript' src="dwr/interface/CustomRequest.js"></script>

      <script type="text/javascript">
          var thePopupManager = null;
          var theToolTipManager = null;
          //var timeoutId;
          //var timeoutInterval=1000*60*1;
          function myLoad(){
              $("loading").style.display = 'none';
              theNav = new IWMNavigator();
              var theClass = document.body.className;
              theNav.init(theClass, "templates");
              $("theBody").style.display = '';
              thePopupManager = new PopupManager();
              theToolTipsManager = new ToolTips();              
          }
      </script>
  </head>

    <body class='<tiles:insert attribute="section"/>' id='<tiles:insert attribute="pageid"/>' onload="myLoad();">


    <div id="loading"><img src="images/loading.gif" alt="loading"></div>

    <div id="theBody" style="display:none;">
    <div id="topOptions">
            <span>
                <%=request.getUserPrincipal()==null?"":request.getUserPrincipal().getName()%></span> |
        <!--            <a href="ExternalWorkRequest.do?forward=welcome">Work Request</a> | -->
        <%if(request.isUserInRole("Admin")){%>
        <a href="Admin.do">Admin</a> |
        <%}%>
        <a href="Settings.do">Settings</a> |
        <a href="javascript:popUp('<c:url value='/helpmanual/index.html'/>', 'help', 570,960);">Help</a> |
        <a href="javascript:popUp('<c:url value='/DefectReport.do?forward=read'/>', 'errorreport', 570,960);">Defect</a> |
        <a href="Logout.do">Logout</a>
    </div>

        <!-- HEADER -->
        <h1><span>Intelligent Work Management</span></h1>

        <!-- MENU -->
        <div id="sectionLinks">
            <div id="topNav">
                <div id="defineTopNav" class="topNavItem"><h4 class="navHeader">Define</h4></div>
                <div id="implementTopNav" class="topNavItem"><h4 class="navHeader">Implement</h4></div>
                <div id="controlTopNav" class="topNavItem"><h4 class="navHeader">Control</h4></div>
                <!--  DO NOT DELETE: WE ARE JUST DISABLING THESE FOR NOW.
                <div id="measureTopNav" class="topNavItem"><h4 class="navHeader">Measure</h4></div>
                <div id="analyzeTopNav" class="topNavItem"><h4 class="navHeader">Analyze</h4></div>
                -->
            </div>

            <div id="defineSubNav" class="topSubNav">
                <div id="workerSubNav" class="subNavItem" onClick="document.location.href='WorkerList.do'"><a href="WorkerList.do" onclick="return false;">Workers</a></div>
                <div id="organizationSubNav" class="subNavItem" onClick="document.location.href='OrganizationList.do'"><a href="OrganizationList.do"  onclick="return false;">Organizations</a></div>
                <div id="locatorsSubNav" class="subNavItem" onClick="document.location.href='LocatorList.do'"><a href="LocatorList.do" onclick="return false;">Locators</a></div>
                <div id="templatesSubNav" class="subNavItem" onClick="document.location.href='TemplateList.do'"><a href="TemplateList.do" onclick="return false;">Templates</a></div>
            </div>

            <div id="implementSubNav" class="topSubNav">
                <div id="objectsSubNav" class="subNavItem" onClick="document.location.href='ObjectList.do'"><a href="ObjectList.do" onclick="return false;">Objects</a></div>
                <div id="stencilsSubNav" class="subNavItem"  onClick="document.location.href='StencilList.do'"><a href="StencilList.do" onclick="return false;">Project&nbsp;Stencils</a></div>
            </div>
            <div id="controlSubNav" class="topSubNav">
                <div id="jobsSubNav" class="subNavItem" onClick="document.location.href='JobList.do'"><a href="JobList.do" onclick="return false;">Jobs</a></div>
                <div id="projectsSubNav" class="subNavItem" onClick="document.location.href='ProjectList.do'"><a href="ProjectList.do" onclick="return false;">Projects</a></div>
                <div id="mobileWorkerSubNav" class="subNavItem" onClick="document.location.href='MWJobs.do'"><a href="MWJobs.do" onclick="return false;">Mobile&nbsp;Worker</a></div>
                <div id="maintenanceSubNav" class="subNavItem" onClick="document.location.href='TenantRequestMaint.do'"><a href="TenantRequestMaint.do" onclick="return false;">Maintenance&nbsp;Req</a></div>
                <div id="reportsSubNav" class="subNavItem" onClick="document.location.href='<%=System.getProperty("reportserver.url")%>'"><a href="<%=System.getProperty("reportserver.url")%>" target="_blank" onclick="return false;">Reports</a></div>
            </div>

            <div id="measureSubNav" class="topSubNav">
                <div id="measureReportsSubNav" class="subNavItem" onClick="document.location.href='/jasperserver'"><a href="/jasperserver" onclick="return false;">Reports</a></div>
                <div class="subNavItem" id="globalSearchSubNav" onClick="document.location.href='GlobalSearch.do'"><a href="GlobalSearch.do" onclick="return false;">Search</a></div>
            </div>

            <div id="analyzeSubNav" class="topSubNav">
                <div class="subNavItem"><a href="#">Test</a></div>
                <div class="subNavItem" id="menuItemSubNav"><a href="#">MenuItem</a></div>
            </div>

        </div>

        <!-- CONTENT -->
        <div id="contentPane">
            <h2><span><tiles:insert attribute="title"/>
                <nested:root name="CURRENT_FORM" >
                    <nested:nest property="pageContext">
                        <nested:notEmpty property="title">
                            : <nested:write property="title" />
                        </nested:notEmpty>
                    </nested:nest>
                </nested:root>
            </span></h2>
            <div class="breadcrumbs">
                <tiles:importAttribute/>
                <c:forEach items="${Breadcrumbs}" var="breadcrumb" varStatus="status">

                    <c:choose>
                        <c:when test="${status.last}">
                            <c:out value="${breadcrumb.value}" />
                        </c:when>
                        <c:otherwise>
                            <iwm:filledlink page="${breadcrumb.link}"> <c:out value="${breadcrumb.value}" /> </iwm:filledlink>  /
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
            
            <div class="content">
          
                <tiles:insert attribute="content"/>
                
                
            </div>
        </div>

        <div id="navigationGroup">

            <!-- LEFT NAVIATION -->
            <div id="quickSearchNav" class="navSection">
                <h2><span>Quick Search</span></h2>
                <div>

                    <label>Search Type:</label>
                    <select>
                        <option>- Select Type -</option>
                    </select>

                    <label>Search Text:</label>
                    <input type="text">

                    <input type="button" class="button" value="Search">
                </div>
            </div>

            <!-- LEFT NAVIATION -->
            <div id="pageContextNav" class="navSection">
                <h2><span>Page Context</span></h2>
                <div>
                    <nested:root name="CURRENT_FORM" >
                        <nested:present property="pageContext">
                            <nested:empty property="pageContext.entries">
                                <p>No current context.</p>
                                <p>
                                    As you navigate deeper into application, data will be
                                    displayed here to help you stay aware of what things
                                    you are currently working on.
                                </p>
                            </nested:empty>
                            <nested:notEmpty property="pageContext.entries">
                                <nested:iterate id="entry"  property="pageContext.entries">
                                    <h3><nested:message property="key"/></h3>
                                    <div><nested:write  property="value" /></div>
                                </nested:iterate>
                            </nested:notEmpty>
                        </nested:present>
                    </nested:root>
                </div>
            </div>

        </div>


        <div id="img1"></div>
        <div id="img2"></div>

        <!-- POP UPS -->
        <div id="popups">
            <div id="overlay"></div>

                <%--
                ** THIS IS THE STYLE THE POPUPS SHOULD FOLLOW. **
                The popups file should have 1...n popups in it, with the following style:

                <div class="popup" id="numberPopup">
                    <div class="popupHeader">
                        <h2>TITLE HERE</h2>
                    </div>

                    <div class="popupBody">
                        <div>
                            YOUR BODY HERE
                        </div>
                    </div>
                </div>

                --%>

            <tiles:importAttribute/>
            <c:forEach items="${Popups}" var="popup" varStatus="status">
                <c:import url="${popup}" />
            </c:forEach>

        </div>

    </div>

    <div id="toolTip"></div>

    </body>

</html>