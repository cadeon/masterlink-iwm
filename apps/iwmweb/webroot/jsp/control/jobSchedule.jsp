<%@ page import="org.mlink.iwm.lookup.ScheduleResponsibilityRef"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/JobSchedules.js"></script>
<script type='text/javascript' src="dwr/interface/JobHistory.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>

<script type="text/javascript">

var workSchedule;
var dataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        searchCriteria.startDate=$('startDate').value;
        searchCriteria.endDate=$('endDate').value;
        return searchCriteria;
    },
    getIsAssigned:function(item){
        var onWorkScheduleClick =   "onWorkScheduleClick("+ item.id + ",this.checked);";
        var rtn = '<input onclick="return ' + onWorkScheduleClick + ';" type="checkbox"'+(item.isAssigned==1?'checked':'') +'>'
        return  rtn;
    },

    getName: function(item){return "<a href='/WorkerDetails.do?id=" + item.personId + "'\'>"+item.name+"</a>";},
    getOrganizationName: function(item){return item.organizationName;},
    getJobCount: function(item){
        var theIcon = "";
        theIcon += '<a  onclick="showJobsForWSchedule('+item.id+')">';
        theIcon += '<div class="icon">';
        theIcon += '<img src="images/db_16.gif" alt="Jobs" />';
        theIcon += '<span id="'+item.id+'jobCount" class="theNumber">' +  item.jobCount + '</span>';
        theIcon += '</div>';
        theIcon += '</a>';
        return theIcon;
    },
    getTime: function(item) {
      return '<img src="images/edit_16.gif" alt="Time"></td>';
    },
    getDay: function(item) {
      return item.dayDisplay;
    },
    getFormaters: function(){return [this.getIsAssigned,  this.getName, this.getDay, this.getOrganizationName, this.getJobCount];}

});
var jobScheduleDataTable; var jobHistoryTable;

function updateAdtlInfo(workschedules){
    var callback = function(item){
       $(item.id+"jobCount").innerHTML=item.jobCount;
    }
    for (var i=0; i < workschedules.length; i++) {
        var wsid=workschedules[i].id;
        //JobSchedules.getItem(wsid,{callback:callback,async:true});
        JobSchedules.getItem2(wsid,{callback:callback,async:true});
    }
}

function onWorkScheduleClick(wsId,assigned){
    workSchedule = new Object();
    workSchedule.id=wsId;
    workSchedule.jobId=<%=request.getParameter("id")%>;
    workSchedule.isAssigned=assigned==true?"1":"0";
    var rtn = false;
    if(!assigned && confirm("Remove worker from job schedule?")){
        rtn = true; jobScheduleDataTable.saveItem(workSchedule);
    }else if (assigned && confirm("Add worker to job schedule?")){
        rtn = true; jobScheduleDataTable.saveItem(workSchedule);
    }

    if(rtn)   {
        getJob();
        getJobHistory();
    }
    return rtn;
}

function getJob(){
    var jobId = <%=request.getParameter("id")%>;
    var filJobDetails = function (item) {
        if(item.sticky=='Y' && item.scheduleResponsibility=='<%=ScheduleResponsibilityRef.SYSTEM%>')
            $('sticky').style.display = 'block';
        else
            $('sticky').style.display = 'none';

        if(item.scheduleResponsibility=='<%=ScheduleResponsibilityRef.SYSTEM%>')
            $('scheduleby').innerHTML="automatically by the system";
        else
            $('scheduleby').innerHTML="manually";


		if(item.status=='CIA' || item.status=='DUN' || item.status=='EJO') { 
		//The job is in a final state and therefore we don't display the assignment box
			$('jobScheduleTable').style.display='none'; 
			$('historyJob').style.display='block';
			} else { 
				$('historyJob').style.display='none'; 
				};
    }
    Jobs.getItem(filJobDetails,jobId);
}


function getJobHistory() {
    var fillJobHistoryTable = function (response) {
        if(response.items && response.items.length > 0) $("jobHistory").style.display = 'block';
        DWRUtil.removeAllRows("jobHistoryTableBody");
            DWRUtil.addRows("jobHistoryTableBody", response.items, [
                    function(item){return item.dayDisplay;},
                    function(item){return item.totalTime;},
                    function(item){return item.name;},
                    function(item){return item.hierarchy;},
                    function(item){return item.phone;},
                    function(item){return item.email;},
                    function(item){return item.scheduledBy + '@' + item.scheduledTimeDisplay;} ],
                    {cellCreator:cellsWithToolTips}
                    );    }
    searchCriteria = new Object();
    searchCriteria.id=<%=request.getParameter("id")%>;
    JobHistory.getData(fillJobHistoryTable, searchCriteria);
}


function init() {
    //setup the dataTable
    theConfig = new dataTableConfig(JobSchedules, ["day"]);
    jobScheduleDataTable = new IWMDataTable(theConfig);
    jobScheduleDataTable.setOnUpdateHook(updateAdtlInfo);
    getJob();
    $('scheduledDateSelect').value="today";
    updateScheduledDate();
    getJobHistory();
}

callOnLoad(init);

var today = new Date();
var weekFromToday =  new Date();  weekFromToday.setDate(today.getDate()+7);
var monthFromToday =  new Date(); monthFromToday.setMonth(today.getMonth()+1);
function updateScheduledDate(){
    var theDate = $('scheduledDateSelect').value;
    if(theDate == 'today'){
        $('startDate').style.visibility = 'hidden';
        $('startDate').value=convertDateToString(today);
        $('endDate').value=convertDateToString(today);
    } else if(theDate == 'nextWeek'){
        $('startDate').style.visibility = 'hidden';
        $('startDate').value=convertDateToString(today);
        $('endDate').value=convertDateToString(weekFromToday);
    } else if(theDate == 'nextMonth'){
        $('startDate').value=convertDateToString(today);
        $('endDate').value=convertDateToString(monthFromToday);
    } else if(theDate == 'userDefinedDate'){
        $('startDate').style.visibility = 'visible';
        $('endDate').value=$('startDate').value
    }
    jobScheduleDataTable.update();
}

</script>

<style type="text/css">
    #startDate {
        visibility: hidden;
    }

    div#info  {
        font-size:8pt;
        font-family:sans-serif;
        width: 600px;
        left:  215px;
        top: 10px;
        position:absolute;
        color:green;
    }
</style>

    <div id="info">
        <div  id="sticky" style="display:none;">
            <!--This job is marked as sticky. Unless overriden manually, it will be automatically assigned to the last previously assigned worker.-->
            This job is marked as sticky and will be automatically assigned to the last previously assigned worker if available.
        </div>
        <div>
            This job is scheduled <span id="scheduleby">automatically by the system</span>. To change this, modify '<bean:message key="scheduleBy"/>' property in Job Edit section.
        </div>
    </div>

<!-- FILTER -->
<h3>Job Schedule Filter</h3>
<div class="filterGroup">
    <div class="filter">
        <h4>Scheduled Date</h4>
        <select id="scheduledDateSelect" onchange="updateScheduledDate()">
            <option value="today">Today</option>
            <option value="nextWeek">Next Week</option>
            <option value="nextMonth">Next Month</option>
            <option value="userDefinedDate">User defined date</option>
        </select>
        <input type="hidden" id="endDate">
        <a:ajax id="startDate"  type="iwm.calendar" name="iwm.calendar" />
    </div>
</div>

<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3>Assign Worker(s)</h3>
<span id="historyJob" style="display:none;">This job is in a final state and therefore cannot be assigned.</span> 
<table class="dataTable" id="jobScheduleTable">
    <colgroup>
        <col class="selectCol" />
        <col class="workerCol" />
        <col class="dateCol" />
        <col class="organizationCol" />
        <col class="jobsCol" />
    </colgroup>
    <thead>
        <tr>
            <td >Assigned?</td>
            <td >Available Worker</td>
            <td id='sort_day'><bean:message key="date"/></td>
            <td><bean:message key="organization"/></td>
            <td><bean:message key="jobs"/></td>
        </tr>
    </thead>
    <tbody id="dataTableBody"></tbody>
</table>


<div id="jobHistory" style="display:none">
    <h3>Assignment History</h3>
    <input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1)"/>


    <table class="dataTable" id="jobHistoryTable">
        <colgroup>
            <col class="scheduledDateCol" />
            <col class="timeCol" />
            <col class="workerCol" />
            <col class="hierarchyCol" />
            <col class="phoneCol" />
            <col class="emailCol" />
            <col class="assignedByCol" />
        </colgroup>
        <thead>
            <tr>
                <td>Scheduled Date</td>
                <td>Recorded<br/> Time</td>
                <td><bean:message key="worker"/></td>
                <td><bean:message key="organization"/></td>
                <td><bean:message key="phone"/></td>
                <td><bean:message key="email"/></td>
                <td>Assigned By</td>
            </tr>
        </thead>

        <tbody id="jobHistoryTableBody">
        </tbody>
    </table>

</div>

<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1)"/>

<%--
<h3>
    <ul>
        <li>
            <a href="javascript:openWindow('ReadReport.do?report=jobs_history&jobId=<%=request.getParameter("id")%>','jobhistory', 600, 800)">
                Job History Report </a>
        </li>
    </ul>
</h3>
--%>

<script type="text/javascript">
    function onDateUpdate(){
        $('endDate').value=$('startDate').value
        //DWREngine.setAsync(false);
        jobScheduleDataTable.update();
    }

</script>
