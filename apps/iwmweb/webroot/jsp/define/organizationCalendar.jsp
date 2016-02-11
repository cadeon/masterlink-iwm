<%@ page import="org.mlink.iwm.dao.TimeSpecsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%
    TimeSpecsCriteria cr =    session.getAttribute("TimeSpecsCriteria")==null?new TimeSpecsCriteria():(TimeSpecsCriteria)session.getAttribute("TimeSpecsCriteria");
    request.setAttribute("TimeSpecsCriteria", cr);
%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TimeSpecs.js"></script>


<script type="text/javascript">
var Worker = new Object();
var TimeSpecsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.locatorId=orgLocatorFilter.currentSelectedId;
        searchCriteria.organizationId=<%=request.getParameter("id")%>;
        searchCriteria.id=$('personId').value;
        searchCriteria.shiftId=$('shiftId').value;
        searchCriteria.dateRange=$('dateRange').value;
        return searchCriteria;
    },
    getShift:function(item){
        var hourMinute = HourMinuteConveter.splitMinutes(item.shiftStart);
        var shiftStart = hourMinute.hours + ':' + hourMinute.minutes;
        hourMinute = HourMinuteConveter.splitMinutes(parseInt(item.shiftStart) + parseInt(item.shiftTime));
        var shiftEnd = hourMinute.hours + ':' + hourMinute.minutes;
        return shiftStart + '-' + shiftEnd;
    },
    getHours:function(item){
        var hourMinute = HourMinuteConveter.splitMinutes(item.shiftTime);
        return hourMinute.hours + ':' + hourMinute.minutes;
    },
    getStatus:function(item){return item.statusDesc;},
    getDate: function(item){return item.dayDisplay;},
    getFullLocator: function(item){return item.fullLocator;},
    getWorkerName: function(item){return item.name;},
    getJobCount: function(item) {
        <%/*item.id is workschedid*/%>
        var theIcon = '<div class="icon" onclick="document.location.href=\'OrganizationWorkerCalendarJobs.do?wsId=' + item.id + '&id='+<%=request.getParameter("id")%>+'\'">';
        theIcon += '<img src="images/tools_16.gif" alt="data" />';
        theIcon += '<span class="theNumber">' + item.jobCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },

    getDelete: function(item) {
        var rtn;
        if(item.isResetAllowed)
            rtn = '<a href="javascript:resetSchedule(\''+item.id+ '\')">Reset</a>';
        else if(item.isAbsentAllowed)
            rtn = '<a href="javascript:makeAbsent(\''+item.id+ '\')">Absent</a>';
        else
            rtn = '<input name="deletedItem" id=\'' + item.id + '\'type="checkbox">';
        return rtn;

    },

    getFormaters: function(){return [this.getDelete, this.getWorkerName, this.getDate, this.getFullLocator,
            this.getHours, this.getShift, this.getStatus, this.getJobCount];}
});

function getDeleted(){
    var elements = document.getElementsByName("deletedItem");
    var ids=new Array();
    for(var i=0;i<elements.length;i++){
        if(elements[i].checked==true) {
            ids[ids.length]=elements[i].id;
        }
    }
    if(ids.length>0)
        workerCalendarTable.deleteItems(ids, "selected calendar entries");
    else
        alert("You need to select items for deletion first!")
}


var orgLocatorFilter;
var workerCalendarTable;

function callbackFunc(message){
     workerCalendarTable.update();
     if(message && message.length > 0) alert(message);
}

function makeAbsent(id){
    if(confirm("Schedule will be removed. Proceed?")){
        TimeSpecs.makeAbsent(id,{callback:callbackFunc,async:false});
    }
}

function resetSchedule(id){
    if(confirm("Schedule will be reset. Proceed?")){
        TimeSpecs.resetWorkschedule(id,{callback:callbackFunc,async:false});
    }
}

function init() {
    //setup the dataTable
    theConfig = new TimeSpecsDataTableConfig(TimeSpecs, ["name", "day","fullLocator","jobCount","statusDesc"]);
    workerCalendarTable = new IWMDataTable(theConfig);
    orgLocatorFilter = new LocatorChain("locatorFilter",
            function (){
                workerCalendarTable.pageNavigator.reset();
                workerCalendarTable.update();
            },
            false, true, 1);
    orgLocatorFilter.populateChain(<%=cr.getLocatorId()%>);
    workerCalendarTable.update();
}

callOnLoad(init);

function onWorkerChange(){
    var option = $("personId").options[$("personId").selectedIndex];
    var selectedPersonId = option.value;
    Worker.id=selectedPersonId==""?undefined:selectedPersonId;
    workerCalendarTable.update();
}

function calendarAdd(){
    var personId=$('personId').value;
    if(!personId){
        alert("Add to Calendar requires worker to be selected!");
    }else{
        showCalendarSpecs(personId,orgLocatorFilter.currentSelectedId);
    }
}

</script>

<!-- FILTER -->
<h3>Calendar Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <table><tr>
            <td valign="top">
                <h4>Date Range</h4>
                <html:select property="dateRange" styleId="dateRange" name="TimeSpecsCriteria" onchange="workerCalendarTable.update();">
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.NextWeek)%>">Next Week</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.NextMonth)%>">Next Month</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.Next3Months)%>">Next 3 Months</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.LastWeek)%>">Last Week</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.LastMonth)%>">Last Month</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.Last3Months)%>">Last 3 Months</html:option>
                    <html:option value="<%=String.valueOf(TimeSpecsCriteria.DATERANGE.All)%>">All Dates</html:option>
                </html:select>
            </td>
            <td valign="top">
                <h4>Location</h4>
                <a:ajax id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
            </td>
            <td valign="top">
                <h4>Shift</h4>
                <html:select property="shiftId" styleId="shiftId" name="TimeSpecsCriteria" onchange="workerCalendarTable.update();">
                    <option value="">- Show All -</option>
                    <html:optionsCollection property="options"  name="ShiftRef"/>
                </html:select>
            </td>
            <td valign="top">
                <h4>Worker</h4>
                <html:select property="id" styleId="personId" name="TimeSpecsCriteria" onchange="workerCalendarTable.update();">
                    <option value="">- All Workers -</option>
                    <html:optionsCollection property="options"  name="WorkerRef"/>
                </html:select>
            </td>
        </tr></table>
    </div>
</div>

<!-- Table showing locators that match the filter -->
<h3>Matching Calendar Items</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add to Calendar" onclick="calendarAdd();"/>
<input type="button" class="button" value="Delete Selected" onclick="getDeleted();"/>
<input type="button" class="button" value="Back to Organization List" onclick="document.location.href = 'OrganizationList.do'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="workerCalendarTable">


    <colgroup>
        <col class="deleteCol" />
        <col class="workerCol" />
        <col class="dateCol" />
        <col class="locationCol" />
        <col class="hoursCol" />
        <col class="shiftCol" />
        <col class="statusCol" />
        <col class="jobsCol" />

    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="deleteCol" id='sort_delete'></td>
            <td id='sort_name'>Worker</td>
            <td id='sort_day'>Date</td>
            <td id='sort_fullLocator'>Location</td>
            <td >Hrs</td>
            <td >Shift</td>
            <td id='sort_statusDesc'>Status</td>
            <td id='sort_jobCount'>Jobs</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="8">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add to Calendar" onclick="calendarAdd();"/>
<input type="button" class="button" value="Delete Selected" onclick="getDeleted();"/>
<input type="button" class="button" value="Back to Organization List" onclick="document.location.href = 'OrganizationList.do'"/>
