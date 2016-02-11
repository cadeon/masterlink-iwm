<%@ page import="org.mlink.iwm.dao.WorkersCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>  
<%@ taglib uri="struts-logic" prefix="logic" %>    

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Workers.js"></script>
<script type='text/javascript' src="dwr/interface/WorkerCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/Users.js"></script>

 <%
    WorkersCriteria cr =    session.getAttribute("WorkersCriteria")==null?new WorkersCriteria():(WorkersCriteria)session.getAttribute("WorkersCriteria");
    request.setAttribute("WorkersCriteria", cr);
%>
<script type="text/javascript">
	var displayAddEditSkills, displayAddEditShiftRefs, displayWorkersLocation;
	displayWorkersLocation=<%=String.valueOf(request.getAttribute("DISPLAY_WORKERS_LOCATION"))%>;

	var WorkersDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            searchCriteria.id=organizationFilter.currentSelectedId;
            searchCriteria.isActive=$('statusFilter').value;
            searchCriteria.filterText=$('filterText').value;
            return searchCriteria;
        },
        getName:function(item){
        	return "<a href='/WorkerDetails.do?id=" + item.id + "'\'>"+item.name+"</a>";
    		},
        getHierarchy: function(item){return item.hierarchy;},
        getActive:function(item){return item.active==1?'Yes':'No';},
        getManager: function(item) {
            var theIcon = "";
            theIcon += '<div class="icon"   onclick="document.location.href=\'WorkerManager.do?id=' + item.id +'\'">';
            theIcon += '<img src="images/manager_16.gif" alt="Manager" />';
            theIcon += '</div>';
            return theIcon;
        },
        getCalendar: function(item) {
            var theIcon = "";
            theIcon += '<div class="icon" id="cal" onclick="document.location.href=\'WorkerCalendar.do?id=' + item.id +'\'">';
            theIcon += '<img src="images/cal_16.gif" alt="Calendar" name="Calendar" id=\''+escapeJSString(item.name)+'\' />';
            theIcon += '</div>';
            return theIcon;
        },
        getSkills: function(item) {
            var theIcon = "";
            theIcon += '<div class="icon" id="skills" onclick="theWorkerSkillsPopup.show('+item.id+',\''+escapeJSString(item.name)+'\');">';
            theIcon += '<img src="images/confg_16.gif" alt="Skills" name="Skills" id=\''+escapeJSString(item.name)+'\' />';
            theIcon += '</div>';
            return theIcon;
        },
        getRights: function(item) {
            var theIcon = "";
            theIcon += '<div class="icon" id="rights" onclick="theWorkerRightsPopup.show('+item.userId+',\''+escapeJSString(item.name)+'\');">';
            theIcon += '<img src="images/shield_16.gif" alt="Rights" name="Rights" id=\''+escapeJSString(item.name)+'\' />';
            theIcon += '</div>';
            return theIcon;
        },
        getLogin: function(item) {
            var theIcon = "";
            theIcon += '<div class="icon"  onclick="thePopupManager.showPopup(\'worker_login\');">';
            theIcon += '<img src="images/lock_16.gif" alt="Login" />';
            theIcon += '</div>';
            return theIcon;
        },
        getDelete: function(item) {
            return '<div class="icon" id="delete" onclick="theWorkersTable.deleteItem('+item.id+', \''+escapeJSString(item.name) + '\');"><img src="images/trash_16.gif"  alt="Delete" name="Delete" id=\''+escapeJSString(item.name)+'\' /></div>';

        },
        getEdit: function(item){
            return '<div class="icon" id="edit"  onclick="theWorkerEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id=\''+escapeJSString(item.name)+'\' /></div>';
        },
        getLocInfo: function(item) {
        	var theIcon = "";
        	if(item.locInfo!=null && item.locInfo!=''){
            	theIcon += '<div class="icon" id="LocInfo" onclick="theMWLocationPopup.show(\''+escapeJSString(item.name)+'\',\''+escapeJSString(item.locInfo)+'\');">';
            	theIcon += '<img src="images/crosshair.gif" alt="Location Info" name="Location Info" id=\''+escapeJSString(item.name)+'\' />';
            	theIcon += '</div>';
        	}
            return theIcon;
        },
    /*
        getFormaters: function(){return [this.getName, this.getHierarchy, this.getRank, this.getActive,
                this.getManager, this.getCalendar, this.getSkills, this.getRights, this.getLogin, this.getEdit, this.getDelete];}
    */
        getFormaters: function(){
            if(displayWorkersLocation){
                return [this.getName, this.getHierarchy,  this.getActive,
            	    this.getCalendar, this.getSkills, this.getRights, this.getLocInfo, this.getEdit, this.getDelete];
            }else{
            	return [this.getName, this.getHierarchy,  this.getActive,
                	    this.getCalendar, this.getSkills, this.getRights, this.getEdit, this.getDelete];
            }
        }
    });

    var theWorkersTable;
    var organizationFilter;

    function init() {
        //setup the dataTable
        theConfig = new WorkersDataTableConfig(Workers, ["name","hierarchy","active"]);
        theWorkersTable = new IWMDataTable(theConfig);

        organizationFilter = new OrganizationChain("organizationFilter",
                function (){
                    theWorkersTable.pageNavigator.reset();
                    theWorkersTable.update();
                },
                true, true);
        SessionUtil.getCurrentOrganization({
            callback:function(curId){
                organizationFilter.populateChain(curId);
            },
            async:false});
        $('filterText').value="<%=cr.getFilterText2()%>";
        $('statusFilter').value="<%=cr.getIsActive()==null?"1":cr.getIsActive()%>"
        theWorkersTable.update();

        new EventThrottledTextInput(document.getElementById('filterText'), function(){ theWorkersTable.update() } );
        
    }
    callOnLoad(init);
</script>


<!--<html:form styleId="WorkerListForm" action="MWJobs.do" styleClass="nomargin">-->
<!-- FILTER -->
<h3><bean:message key="workerFilter"/></h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4><bean:message key="organizationFilter"/></h4>
        <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
    </div>
    <div class="filter"  >
        <table class="filterTable">
            <tbody class="filterEdit">
                <tr>
                    <td>
                        <h4><bean:message key="statusFilter"/></h4>
                        <select class="filterSelect"  id="statusFilter" onchange="theWorkersTable.update();">
                            <option value="1"><bean:message key="active"/></option>
                            <option value="0"><bean:message key="inactive"/></option>
                        </select>
                    </td>
                    <td>
                        <h4><bean:message key="textFilter"/></h4>
                        <input type="text" id="filterText">
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<!-- Table showing locators that match the filter -->
<h3><bean:message key="matching"/> <bean:message key="workers"/></h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />

<input type="button" class="button" value="Add Worker" onclick="theWorkerEditPopup.show(null, true);"/>
<%if(request.isUserInRole("WkrSkills")){%>
<input type="button" class="button" id="skillsButton1" name="skillsButton1" value="Add/Edit Skill Type" 
  onclick="window.location.href='<c:url value='/SkillTypeList.do'/>';" />
<%}%>
  
<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="workerDataTable">

    <colgroup>
        <col class="nameCol" />
        <col class="organizationCol" />
        <col class="activeCol" />
        <!--col class="managerCol" /-->
        <col class="clndrCol" />
        <col class="skillsCol" />
        <col class="rightsCol" />
        <logic:equal name="DISPLAY_WORKERS_LOCATION" scope="request" value="true">
			<col class="locInfoCol" />
		</logic:equal>
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="nameCol" id='sort_name'><bean:message key="name"/></td>
            <td id='sort_hierarchy'><bean:message key="organization"/></td>
            <td class="activeCol" id='sort_active'><bean:message key="active"/></td>
            <!--td class="managerCol">Mngr</td-->
            <td class="clndrCol" >Clndr</td>
            <td class="skillsCol" >Skills</td>
            <td class="rightsCol" >Rights</td>
            <logic:equal name="DISPLAY_WORKERS_LOCATION" scope="request" value="true">
            	<td class="locInfoCol"><bean:message key="location"/></td>
     		</logic:equal>
            <td class="editCol">Edit</td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <!--  <tr>
            <td colspan="8">Loading ...</td>
        </tr>
        -->
    </tbody>
</table>
<!--</html:form>-->
<input type="button" class="button" value="Add Worker" onclick="theWorkerEditPopup.show(null, true);"/>

  
  
