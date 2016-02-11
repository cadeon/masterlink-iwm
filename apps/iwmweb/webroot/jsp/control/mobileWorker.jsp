<%@ page import="org.mlink.iwm.bean.MWJob"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/MobileWorkerJobs.js"></script>

<script type="text/javascript">
var Worker = new Object();
Worker.id = <bean:write name="CURRENT_FORM" property="id"/>;
Worker.scheduledDate = '<bean:write name="CURRENT_FORM" property="scheduledDate"/>';
Worker.workerStatus= '<bean:write name="CURRENT_FORM" property="status"/>';
Worker.breakTimekeeping= '<bean:write name="CURRENT_FORM" property="breakTimekeeping"/>';
Worker.shiftTimekeeping= '<bean:write name="CURRENT_FORM" property="shiftTimekeeping"/>';

var MobileWorkerJobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=Worker.id;
        //searchCriteria.filterText=$('filterText').value;
        searchCriteria.locatorId=locatorFilter.currentSelectedId;
        <%if(request.getParameter("scheduledDate")!=null){%>
            searchCriteria.scheduledDate='<%=request.getParameter("scheduledDate")%>';
        <%}%>
        //searchCriteria.completionStatus=$('completionStatus').options[$('completionStatus').selectedIndex].value;
        searchCriteria.completionStatus=DWRUtil.getValue($('completionStatus'))=="-1"?undefined:DWRUtil.getValue($('completionStatus'));
        return searchCriteria;
    },
    getJobId: function(item){return item.id;},
    getPriority: function(item){return item.priority;},
    getStatus: function(item){return item.completionStatus;},
    getJobType: function(item){return item.jobType;},
    getFullLocator: function(item){return item.fullLocator;},
    getDesc: function(item){return item.description;},
    getObject: function(item){return item.objectRef;},
    getHours: function(item){
        var rtn = item.estTime;
        if(rtn.length>0){
            var hm=HourMinuteConveter.splitMinutes(rtn);
            rtn=hm.hours+":"+hm.minutes;
        }
        return rtn;
    },
    getTasks: function(item) {
        var theIcon = "";
	        theIcon += '<div class="icon"   onclick="document.location.href=\'MobileWorkerTasks.do?id=' + item.jobScheduleId +'\'">';
	        theIcon += '<img src="images/help_16.gif" alt="Tasks" />';
	        //theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
	        theIcon += '</div>';
        return theIcon;
    },
    getAge: function(item){
        if (item.age > 3){
        return '<div class="icon"> <font color = red>'+item.age+'</font></div>';
        } else {
        return '<div class="icon"> '+item.age+'</div>';
        }
    },
    getFormaters: function(){return [this.getStatus, this.getJobId, this.getPriority, this.getJobType, this.getDesc, this.getObject,
            this.getFullLocator, this.getHours, this.getTasks ]}
});

var locatorFilter;
var dataTable;

function init() {
    //setup the dataTable
    theConfig = new MobileWorkerJobsDataTableConfig(MobileWorkerJobs, [], [2,3,4,5]);
    dataTable = new IWMDataTable(theConfig);

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                dataTable.pageNavigator.reset();
                dataTable.update();
            },
            false, true,1);
    locatorFilter.populateChain();
    //new EventThrottledTextInput(document.getElementById('filterText'), function(){ dataTable.update() } );

    dataTable.update();

    var shiftButton = document.getElementById('shiftButton');
    var breakButton = document.getElementById('breakButton');
    if(Worker.workerStatus=='DUN'){
        $('shiftButton').style.visibility = 'hidden';
    }else if(Worker.workerStatus== 'IP'){
    	shiftButton.value='End Shift';
    }else if(Worker.workerStatus== 'BRK'){
        $('shiftButton').style.visibility = 'hidden';
    }else if('true'!=Worker.shiftTimekeeping){
    	shiftButton.value='End Shift';
    }

    if('true'==Worker.breakTimekeeping){
    	if(Worker.workerStatus=='DUN'){
            $('breakButton').style.visibility = 'hidden';
        }else if(Worker.workerStatus== 'NYS'){
            $('breakButton').style.visibility = 'hidden';
        }else if(Worker.workerStatus== 'BRK'){
            breakButton.value='End Break';
        }
    }else{
        $('breakButton').style.visibility = 'hidden';
    }
}
callOnLoad(init);

function submitForm(formId, forward){
    $("forward").value=forward;
    $("workerStatus").value=Worker.workerStatus;
    $(formId).submit();
}

function changeShift(shiftButton){
	if(shiftButton.value=='Start Shift'){
		 if(confirm('Are you sure you want to start the shift?')){
			submitForm('MWJobsForm','startshift');
		 }
	}else{
	    if(confirm('Are you sure you want to End Shift?')){
	        submitForm('MWJobsForm','endshift');
	    }
	}
}

function changeBreak(breakButton){
	if(breakButton.value=='End Break'){
		if(confirm('Are you sure you want to end the break?')){
		    submitForm('MWJobsForm','endbreak');
		}
	}else{
	    if(confirm('Are you sure you want to start the break?')){
	        submitForm('MWJobsForm','startbreak');
	    }
	}
}

Stamp  =  new  Date();
var Hours;
var Mins;
var Time;
Hours = Stamp.getHours();
if (Hours >= 12) {
        Time = " PM";
}
        else {
                Time = " AM";
        }

if (Hours > 12) {
        Hours -= 12;
        }

if (Hours == 0) {
        Hours = 12;
        }

Mins = Stamp.getMinutes();

if (Mins < 10) {
        Mins = "0" + Mins;
        }
</script>

<style type="text/css">
    .nomargin {
        margin:0;
        padding:0;
    }
</style>

<html:form styleId="MWJobsForm" action="MWJobs.do" styleClass="nomargin">
    <input type="hidden" name="forward" id="forward">
    <input type="hidden" name="workerStatus" id="workerStatus">
    <input type="button" value="Log Out" class="button" onclick="submitForm('MWJobsForm','logout')">
    <input type="hidden" id="shiftButton" name="shiftButton" value="Start Shift" class="button" onclick="changeShift(this);">
    <input type="hidden" id="breakButton" name="breakButton" value="Start Break" class="button" onclick="changeBreak(this);">
</html:form>
<br>
<script>document.write('Current time: ' + Hours + ":" + Mins + Time)</script>
<p>
<h3><bean:message key="assignmentFilter"/></h3>
<div class="filterGroup">
    <table>
        <tr>
            <td>
                <div class="filter"  >
                    <h4><bean:message key="statusFilter"/></h4>
                    <select id="completionStatus" onchange="dataTable.update();">
                        <option value="-1">- Select Status - </option>
                        <option value="<%=MWJob.Status.Open%>">Open</option>
                        <option value="<%=MWJob.Status.Closed%>">Closed</option>
                    </select>
                </div>
            </td>
            <td>
                <div class="filter" id="locationFilter" >
                    <h4><bean:message key="locationFilter"/></h4>
                    <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
                </div>
            </td>
        </tr>
    </table>

    <!--div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div-->
</div>

<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3><bean:message key="matchingAssignments"/></h3>

<table class="dataTable" id="workerAssignmentsTable">
    <colgroup>
        <col class="statusCol" />
        <col class="jobIdCol" />
        <col class="priorityCol" />
        <col class="priorityCol"/>
        <col class="descriptionCol" />
		<col class="objectIdCol"/>
        <col class="locatorCol" />
        <col class="hrsCol" />
        <col class="tasksCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="statusCol" id='sort_status'>Status</td>
            <td class="jobIdCol"  id='sort_id'>Job Id</td>
            <td class="priorityCol" id='sort_priority'>Priority</td>
            <td class="priorityCol" id='sort_type'>Type</td>
            <td class="descriptionCol" id='sort_description'>Description</td>
            <td class="objectCol" id='sort_object'>Object</td>
            <td class="locatorCol" id='sort_locator'>Locator</td>
            <td class="hrsCol" id='sort_hrs'>Est.</td>
            <td class="tasksCol" id='sort_tasks'>Details</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
 
    </tbody>
</table>