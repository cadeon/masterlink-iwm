<%@ page import="org.mlink.iwm.dao.SearchCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%
    SearchCriteria cr =    session.getAttribute("TemplatesCriteria")==null?new SearchCriteria():(SearchCriteria)session.getAttribute("TemplatesCriteria");
    request.setAttribute("TemplatesCriteria", cr);
%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Templates.js"></script>

<script type="text/javascript">

var TemplatesDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=classFilter.currentSelectedId;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },

    getCode: function(item) { return  item.classDesc },
    getInstanceCount: function(item) {
        centeredText = '<div class="centerText">' + item.instanceCount  + '</div>';
        return centeredText;
    },
    getInventoryCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="centerText" id="inventory" onclick="theTemplateInventoryPopup.show('+item.id+');" >';
        theIcon += '<img src="images/edit_16.gif" alt="Data" name="Data" id='+escapeJSString(item.classDesc)+' />';
        theIcon += '<span class="theNumber">' + item.presentInventory + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getCalendar: function(item){
      var theIcon = "";
        theIcon += '<div class="icon" onclick="readCalendar('+item.id+')">';
        theIcon += '<img src="images/cal_16.gif" alt="Calendar" name="Calendar" id=\''+escapeJSString(item.classDesc)+'\' />';
        theIcon += '</div>';
        return theIcon;

    },
    getDataCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'TemplateData.do?id=' + item.id + '\'">';
        theIcon += '<img src="images/db_16.gif" alt="Data" name="Data" id=\''+escapeJSString(item.classDesc)+'\' />';
        theIcon += '<span class="theNumber">' + item.dataCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getTaskCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'TemplateTasks.do?id=' + item.id + '\'">';
        theIcon += '<img src="images/tasks_16.gif" alt="Tasks" name="Tasks" id=\''+escapeJSString(item.classDesc)+'\' />';
        theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },

    getTaskGroupCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'TemplateTaskGroup.do?id=' + item.id + '\'">';
        theIcon += '<img src="images/db_16.gif" alt="Task Groups" name="Task Groups" id=\''+escapeJSString(item.classDesc)+'\' />';
        theIcon += '<span class="theNumber">' + item.taskGroupCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },

    getFormaters: function(){return [this.getCode, this.getInstanceCount, /*this.getInventoryCount,*/ this.getCalendar, this.getDataCount, this.getTaskCount, this.getTaskGroupCount];}


});

var classFilter;
var theTemplateDataTable;

function init() {
    //setup the dataTable
    theConfig = new TemplatesDataTableConfig(Templates, ["code","instanceCount",/*"inventoryCount",*/"dataCount","taskCount","taskGroupCount"]);
    theTemplateDataTable = new IWMDataTable(theConfig);

    classFilter = new ObjectClassChain("classFilter",
            function (){
                theTemplateDataTable.pageNavigator.reset();
                theTemplateDataTable.update();
            },
        true, true);

    SessionUtil.getCurrentClass({
            callback:function(id){
            classFilter.populateChain(id);
        },
        async:false});

    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theTemplateDataTable.update() } );
    $('filterText').value="<%=cr.getFilterText2()%>";
    theTemplateDataTable.update();
}
callOnLoad(init);

</script>

<%-- ************************************************************************ --%>
<%-- START HTML ************************************************************* --%>

<!-- FILTER -->
<h3>Template Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4>Class Filter</h4>
        <a:ajax  id="classFilter" type="iwm.filter" name="iwm.filter"/>
    </div>

  <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing locators that match the filter -->
<h3>Object Templates</h3><p>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Classification" onclick="theObjectClassificationEditPopup.show();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="templatesTable">
    <colgroup>
        <col class="classCol" />
        <col class="instancesCol" />
        <!-- col class="inventoryCol" />  -->
        <col class="dataCol" />
        <col class="calendarCol" />
        <col class="tasksCol" />
        <col class="taskGroupsCol" />
    </colgroup>
    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_code'>Template Class</td>
            <td id='sort_instanceCount'>Instances</td>
            <!-- td id='sort_inventoryCount'>Inventory</td>  -->
            <td class="calendarCol" id='sort_calendar'>Clndr</td>
            <td class="dataCol"  id='sort_dataCount'>Data</td>
            <td class="tasksCol" id='sort_taskCount'>Tasks</td>
            <td class="taskGroupsCol" id="sort_taskGroupCount">Task Groups</td>
        </tr>
    </thead>

    <%-- JPM: id locatorsbody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="6">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Add Classification" onclick="theObjectClassificationEditPopup.show();"/>
