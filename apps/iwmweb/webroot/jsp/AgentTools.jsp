<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>


    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/IWMMain.css" type="text/css">
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/global.js"></script>

	<!-- right click menu -->
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/menu.js"></script>

	<!-- behviour support -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/behaviour.js"></script>

	<!-- modal dialogs -->
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/modalDialog.js"></script>

    <script type='text/javascript' src="dwr/interface/AgentToolsLog.js"></script>


    <script type="text/javascript">
		function runJSM() {
			document.forms[0].forward.value = "jsm";
			document.forms[0].submit();
		}
		function runPlanner() {
			document.forms[0].forward.value = "planner";
			document.forms[0].submit();
		}
		function printStateMachine() {
			document.forms[0].forward.value = "printStateMachine";
			document.forms[0].submit();
		}
		function reloadStateMachine() {
			document.forms[0].forward.value = "reloadStateMachine";
			document.forms[0].submit();
		}
		function runScheduler() {
			document.forms[0].forward.value = "scheduler";
			document.forms[0].submit();
		}
		function runShiftManager() {
			document.forms[0].forward.value = "shiftManager";
			document.forms[0].submit();
		}

        function runAllAgents() {
			document.forms[0].forward.value = "runAll";
			document.forms[0].submit();
		}

        function runAllAgents2() {
			document.forms[0].forward.value = "runAllButPlanner";
			document.forms[0].submit();
		}

        function setAgentInterval() {
			document.forms[0].forward.value = "setAgentInterval";
			document.forms[0].submit();
		}
	</script>

<style type="text/css">
    .nomargin {
        margin:0;
        padding:0;
    }
</style>
<html:form action="/AgentTools.do" styleClass="nomargin">
	<%/*div id="planner" >
	<b>Planner results:</b><br/>
	Number of active Tasks: <html:text property="numActiveTasks"/><br/>
	Number of jobs created: <html:text property="numCreatedJobs"/><br/>
	</div*/%>


<html:hidden property="dispatch"/>
<html:hidden property="forward"/>

	<div>
		Enter agent run interval in minutes (0 to disable): <html:text property="agentInterval" />
		<input type="button" value="Set Interval" class="button" id="intervalButton" onclick="setAgentInterval();"><br/><br/>

        <input type="button" value="ShiftManager" class="button" id="shiftMgrButton" onclick="runShiftManager();">
        <input type="button" value="Planner" class="button" id="plannerButton" onclick="runPlanner();">&nbsp;&nbsp;&nbsp;
        <input type="button" value="JSM" class="button" id="jsmButton" onclick="runJSM();">&nbsp;&nbsp;&nbsp;
		<input type="button" value="Scheduler" class="button" id="schedulerButton" onclick="runScheduler();">&nbsp;&nbsp;&nbsp;

        <input type="button" value="Print State Machine" class="button" id="printStateMachineButton" onclick="printStateMachine();">&nbsp;&nbsp;&nbsp;
		<input type="button" value="Reload State Machine" class="button" id="reloadStateMachineButton" onclick="reloadStateMachine();">&nbsp;&nbsp;&nbsp;
		<br/>
	</div>

    <div>
        <input type="button" value="Run All" class="button" id="runAll" onclick="runAllAgents();">
        <input type="button" value="Run All But Not Planner" class="button" id="runAllButPlanner" onclick="runAllAgents2();">        
    </div>

    <div style="margin-top:20px; width:90%; height: 150px; overflow:auto;" id="agentLog"></div>

</html:form>
&nbsp; <%/*nbsp removes white space on the bottom of page, strange but works*/%>

<script type="text/javascript">
    /*planner.isVisible="false";
         if (document.forms[0].forward.value == "planner" )
            planner.isVisible="true";*/

    function getAgentLog(){
        AgentToolsLog.getLog({callback:function(response) {
            $('agentLog').innerHTML=response;
            fetchLog();
        }});
    }
    function fetchLog(){
        setTimeout(getAgentLog, 5000);
    }
    callOnLoad(fetchLog);
</script>