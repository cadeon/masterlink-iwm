<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/WorkScheduleJobs.js"></script>

<script type="text/javascript">

var WSJobsTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("wsId")%>;
        return searchCriteria;
    },
    getId:function(item){return item.id},
    getObjectRef:function(item){return item.objectRef},
    getDesc: function(item){return item.description;},
    getFullLocator: function(item){return item.fullLocator;},
    getAge: function(item){return item.age;},
    getPriority: function(item){return item.priority;},
    getStatus: function(item){return item.status;},
    getAssignedBy: function(item){return item.scheduledBy + '@' + item.scheduledTimeDisplay;},
    getHistory: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="showJobHistory('+item.id+')">';
        theIcon += '<img src="images/hist_16.gif" alt="data" />';
        theIcon += '</div>';
        return theIcon;
    },

    getFormaters: function(){return [this.getId, this.getStatus, this.getDesc, this.getObjectRef, this.getFullLocator,this.getAge, this.getPriority,
            this.getAssignedBy,this.getHistory];}

});


var dataTable;
function init() {
    //setup the dataTable
    theConfig = new WSJobsTableConfig(WorkScheduleJobs, ["id","status","description","objectRef","fullLocator", "age", "priority"],[2,3,4,6,7]);
    dataTable = new IWMDataTable(theConfig);
    dataTable.update();

    var breadcrumbLinks = $$('div.breadcrumbs a');
    $('buttonTop').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }
    $('buttonBottom').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }
}
callOnLoad(init);

</script>

<!-- Table showing locators that match the filter -->
<h3>Jobs</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<%/*input type="button" class="button" value="Back to Calendar" onclick="document.location.href = 'WorkerCalendar.do?id=<=request.getParameter("id")>'"/*/%>
<input id="buttonTop" type="button" class="button" value="Back to Calendar"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="workerCalendarJobsTable">

    <colgroup>
        <col class="jobIdCol" />
        <col class="jobStatusCol" />
        <col class="descriptionCol" />
        <col class="objectCol" />
        <col class="locatorCol" />
        <col class="ageCol" />
        <col class="priorityCol" />
        <col class="assignedByCol" />
        <col class="historyCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_id'>Job Id</td>
            <td id='sort_status'>Curr Status</td>
            <td id='sort_description'>Description</td>
            <td id='sort_objectRef'>Object</td>
            <td id='sort_fullLocator'>Locator</td>
            <td id='sort_age'>Age</td>
            <td id='sort_priority'>Priority</td>
            <td id='sort_priority'>Assigned By</td>
            <td class="historyCol"  >Hist</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input id="buttonBottom" type="button" class="button" value="Back to Calendar"/>
