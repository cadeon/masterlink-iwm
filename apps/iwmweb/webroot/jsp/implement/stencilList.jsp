<%@ page import="org.mlink.iwm.dao.ProjectStencilsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%
    ProjectStencilsCriteria cr =    session.getAttribute("ProjectStencilsCriteria")==null?new ProjectStencilsCriteria():(ProjectStencilsCriteria)session.getAttribute("ProjectStencilsCriteria");
    request.setAttribute("ProjectStencilsCriteria", cr);
%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ProjectStencils.js"></script>

<script type="text/javascript">

var StencilsTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipsCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipsCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.organizationId=organizationFilter.currentSelectedId;
        searchCriteria.locatorId=locatorFilter.currentSelectedId;
        searchCriteria.activeStatus=$('activeStatusFilter').value;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },
    getId:function(item){return item.id;},
    getName:function(item){return item.name;},
    getOrganization: function(item){return item.organization;},
    getFullLocator: function(item){return item.fullLocator;},
    getAutoplanning: function(item){return item.autoplanningDisplay;},
    getTaskCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ProjectStencilTasks.do?id=' + item.id + '\'">';
        theIcon += '<img src="images/tasks_16.gif" alt="Tasks" />';
        theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getHistory: function(item) {
        return '<div class="icon" onclick="showStencilHistory('+item.id+');"><img src="images/hist_16.gif"  alt="History" /></div>';

    },
    getDelete: function(item) {
        return '<div class="icon" onclick="theStencilsTable.deleteItem('+item.id+', \''+item.name + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';

    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theStencilEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
    },
    getFormaters: function(){return [this.getId, this.getName, this.getAutoplanning, this.getOrganization, this.getFullLocator, this.getTaskCount,
            this.getHistory, this.getEdit, this.getDelete];}

});


var theStencilsTable;
var organizationFilter;
var locatorFilter;


function init() {
    //setup the dataTable
    theConfig = new StencilsTableConfig(ProjectStencils, ["id","name","autoplanning","organization","taskCount"],[1,3,4]);
    theStencilsTable = new IWMDataTable(theConfig);


    organizationFilter = new OrganizationChain("organizationFilter",
            function (){
                theStencilsTable.pageNavigator.reset();
                theStencilsTable.update();
            },
            true, true);
    SessionUtil.getCurrentOrganization({
        callback:function(curId){
            organizationFilter.populateChain(curId);
        },
        async:false});

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                theStencilsTable.pageNavigator.reset();
                theStencilsTable.update();
            },
            true, true, 1);
    SessionUtil.getCurrentLocator({
        callback:function(curLocatorId){
            locatorFilter.populateChain(curLocatorId);
        },
        async:false});

    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theStencilsTable.update() } );
    $('filterText').value="<%=cr.getFilterText2()%>";    
    theStencilsTable.update();
}

callOnLoad(init);

</script>

<%-- ************************************************************************ --%>
<%-- START HTML ************************************************************* --%>



<!-- FILTER -->
<h3>Project Stencil Filter</h3>
<div class="filterGroup">

     <div class="filter"  >
        <h4>Organization Filter</h4>
        <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
     </div>

    <table class="filterTable">
        <tbody class="filterEdit">
        <tr>
            <td>
            <div class="filter"  >
                <h4>Locator Filter</h4>
                <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
            </div>
            </td>
            <td>
            <div class="filter"  >
                <h4>Active Filter</h4>
                <html:select styleClass="filterSelect" property="activeStatus" styleId="activeStatusFilter" name="ProjectStencilsCriteria" onchange="theStencilsTable.update();">
                    <html:option value="1">Active</html:option>
                    <html:option value="0">Inactive</html:option>
                </html:select>
            </div>
            </td>
            <td>
            <div class="filter"  >
                <h4>Search by Name or Id</h4>
                <input type="text" id="filterText">
            </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<!-- Table showing ProjectStencils that match the filter -->
<h3>Matching Project Stencils</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Project Stencil" onclick="theStencilEditPopup.show();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="projectStencilsTable">


    <colgroup>
        <col class="stencilIdCol" />
        <col class="name" />
        <col class="autoplanCol" />
        <col class="orgNameCol" />
        <col class="locatorCol" />
        <col class="tasksCol" />
        <col class="historyCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_id'>&nbsp;<bean:message key="id"/></td>
            <td id='sort_name'><bean:message key="name"/></td>
            <td id='sort_autoplanning'><bean:message key="planning"/></td>
            <td id='sort_organization'><bean:message key="organization"/></td>
            <td><bean:message key="fullLocator"/></td>
            <td id='sort_taskCount'><bean:message key="taskCount"/></td>
            <td >Hist</td>
            <td ><bean:message key="edit"/></td>
            <td ><bean:message key="delete"/></td>
        </tr>
    </thead>
    <tbody id="dataTableBody"></tbody>
</table>

<input type="button" class="button" value="Add Project Stencil" onclick="theStencilEditPopup.show();"/>
