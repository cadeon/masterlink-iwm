<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sim-style.css" type="text/css">
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/global.js"></script>
	
	<!-- right click menu -->
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/menu.js"></script>
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/menu-sim.js"></script>
	
	<!-- behviour support -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/behaviour.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/behave-simList.js"></script>
	
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/simulator.js"></script>

	<!-- modal dialogs -->
    <script language="JavaScript" src="<%=request.getContextPath()%>/scripts/modalDialog.js"></script>
	<script type="text/javascript">
		var theSimulator;
		function init(){
			cloneDialog = new ModalDialog("simulators/popups/clone.htm", 290, 219);
			exportDialog = new ModalDialog("simulators/popups/export.htm", 290, 157);
			renameDialog = new ModalDialog("simulators/popups/rename.htm", 300, 157);
			runDialog = new ModalDialog("simulators/popups/run.htm", 600, 510);
			importDialog = new ModalDialog("simulators/popups/import.htm", 290, 219);
		}
	</script>
	<script type="text/javascript">
		function runPlanner() {
			document.forms[0].forward.value="read";
			document.forms[0].agent.value = "planner";
			document.forms[0].submit();
		}
	</script>
	
	
	<html-el:form action="/WorldSummary.do" >
	<div><html:text property="numActiveTasks"/></div>
	<table id="worldtable" class="selectTable" >
		<thead>
		<tr>
			<th>Name</th>
			<th>Parent</th>
			<th>Last simulation run</th>
			<th>Status</th>
		</tr>
		</thead>
		<tbody>
        <nested:iterate id="item" property="worlds" indexId="nIndex">
			<tr id="<nested:write property='id'/>" class="simRow">
				<td><nested:write property="name"/></td>
				<td><nested:write property="parent"/></td>
				<td><nested:write property="displayLastSimulationRun"/></td>
				<td><nested:write property="displaySimStatus"/></td>
			</tr>
		</nested:iterate>
		</tbody>
	</table>
<html:hidden property="dispatch"/>
<html:hidden property="forward"/>
<html:hidden property="agent"/>

</html-el:form>
	<div>
		<input type="button" value="Test" class="button" id="importButton"><br/>
		<input type="button" value="Planner" class="button" id="importButton" onclick="runPlanner();">
	</div>

	<!-- RIGHT CLICK MENU-->
	<div id="eventshield"></div>
	<div id="mainmenu" class="menu">
		<div class="menuItem" id="run">Run</div>
		<div class="menuItem" id="rename">Rename</div>
		<div class="menuItem" id="clone">Clone</div>
		<div class="menuItem" id="modify">Modify</div>
		<div class="menuItem" id="export">Export</div>
		<div class="menuItem" id="reports">Reports</div>
		<div class="menuItem" id="delete">Delete</div>
	</div>

	<!-- MODAL DIALOG -->
	<div id="overlay"></div>
	<div id="modalDialog">
		<iframe src="" width="100%" height="100%" frameborder="0" id="dialogIframe" scrolling="no">
	</div>
	