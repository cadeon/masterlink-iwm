<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%@ page import="org.mlink.iwm.util.Config"%>
<%@ page import="org.mlink.iwm.dao.JobsCriteria"%>
<%@ page import="org.mlink.iwm.bean.MWJob"%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>
<script type='text/javascript' src="dwr/interface/MobileWorkerJobs.js"></script>
<script type='text/javascript' src="dwr/interface/Workers.js"></script>
<script type='text/javascript' src="dwr/interface/WorkerCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/Users.js"></script>

<script type="text/javascript">
var personId=<%=request.getParameter("id")%>;

var MobileWorkerJobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols, thePageMeter, theDataTableBody) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols, thePageMeter, theDataTableBody);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=personId;
        return searchCriteria;
    },
    getJobId:function(item){
        
        return "<div style='text-align:center'><a href='/JobDetails.do?id=" + item.id + "'\'>"+item.id+"</a></div>";
    },
    getAge:function(item){
        //TODO: Boy, this is ugly. It should be done server side.
        //item.earliestStart is in 06/10/2012 format
		var ONE_DAY = 86400000; // ms per day        
        var parts= item.earliestStart.split("/");
     	// new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
        var estart = new Date(parts[2], parts[0]-1, parts[1]); // months are 0-based
        var now = new Date();
		estart_ms = estart.getTime();
        now_ms = now.getTime();
                
		return "<div style='text-align:center'>"+parseInt((now_ms - estart_ms)/ONE_DAY)+"</div>"; // no rounding. 
    },
    getStatus: function(item){return "<div style='text-align:center'>"+item.completionStatus+"</div>";},
    getObjectRef2:function(item){return item.objectRef},
    getObjectRef:function(item){
        if (item.objectRef.startsWith("AreaTarget")){
			return item.objectRef;
            }
        //It's a real object, set up the details link
        var rtn = "";
        rtn += '<a href="/ObjectDetails.do?id=' +item.objectId + '">';
        rtn += item.objectRef;
        rtn += '</a>';
        return rtn;
    },
    getPriority: function(item){return "<div style='text-align:center'>"+item.priority+"</div>";},
    getDesc: function(item){return item.description;},
    getJobType: function(item){
        return "<div style='text-align:center'>"+item.jobType+"</div";
        },
    getSkillType: function(item){
        return "<div style='text-align:center'>"+item.skillType+"</div>";
        },
    getCreatedDate: function(item){return item.createdDate;},
    
    getTotalTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.totalTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},

    getEstTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.estTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},
        getFullLocator: function(item){
            var rtn = "";
            rtn += '<span onclick="autoscrollToLocator(' + item.locatorId +','+ item.id +')">';
            rtn += item.fullLocator;
            rtn += '</span>';
            return rtn;
        },

    
    getFormaters: function(){return [this.getJobId, this.getAge, this.getStatus, this.getJobType, this.getDesc,
            this.getObjectRef, this.getFullLocator, this.getEstTime];}
});

var WorkerJobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols, thePageMeter, theDataTableBody) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols, thePageMeter, theDataTableBody);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.completingWorkerId=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getJobId:function(item){
       
        return "<div style='text-align:center'><a href='/JobDetails.do?id=" + item.id + "'\'>"+item.id+"</a></div>";
    },
    getAge:function(item){
        //TODO: Boy, this is ugly. It should be done server side.
        //item.earliestStart is in 06/10/2012 format
		var ONE_DAY = 86400000; // ms per day        
        var parts= item.earliestStart.split("/");
     	// new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
        var estart = new Date(parts[2], parts[0]-1, parts[1]); // months are 0-based
        var now = new Date();
		estart_ms = estart.getTime();
        now_ms = now.getTime();
                
		return "<div style='text-align:center'>"+parseInt((now_ms - estart_ms)/ONE_DAY)+"</div>"; // no rounding. 
    },
    getStatus:function(item){
        if("Action Req"==item.status){     //highlight NYA jobs
            return "<div  style='text-align:center;background-color:khaki;'>"+item.status+ "</div>"}
        else{
            return "<div style='text-align:center'>"+item.status+"</div>";
        }
    },
    getObjectRef2:function(item){return item.objectRef},
    getObjectRef:function(item){
        if (item.objectRef.startsWith("AreaTarget")){
			return item.objectRef;
            }
        //It's a real object, set up the details link
        var rtn = "";
        rtn += '<a href="/ObjectDetails.do?id=' +item.objectId + '">';
        rtn += item.objectRef;
        rtn += '</a>';
        return rtn;
    },
    getPriority: function(item){return item.priority;},
    getDesc: function(item){return item.description;},
    getJobType: function(item){
        return "<div style='text-align:center'>"+item.jobType+"</div";
        },
    getSkillType: function(item){
        return "<div style='text-align:center'>"+item.skillType+"</div>";
        },
    getCreatedDate: function(item){return item.createdDate;},
    
    getTotalTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.totalTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},

    getEstTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.estTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},
        getFullLocator: function(item){
            var rtn = "";
            rtn += '<span onclick="autoscrollToLocator(' + item.locatorId +','+ item.id +')">';
            rtn += item.fullLocator;
            rtn += '</span>';
            return rtn;
        },

    
    getFormaters: function(){return [this.getJobId, this.getAge, this.getStatus, this.getJobType, this.getDesc,
            this.getObjectRef, this.getFullLocator, this.getEstTime, this.getTotalTime];}


});

var locatorFilter;
var dataTable;

var theWorkerJobDataTable;

function setupWorkerJobsTable() {
    //setup the dataTable
    theWorkerJobsConfig = new WorkerJobsDataTableConfig(Jobs, ["id","earliestStart","status","description",
           "jobType", "locator"], [4,5,6],"jobsPagemeter",workerJobsTableBody); 
    theWorkerJobDataTable = new IWMDataTable(theWorkerJobsConfig);
    theWorkerJobDataTable.tableSort.currentSortColumn="id";
    theWorkerJobDataTable.tableSort.currentSortDirection="DESC";

    theWorkerJobDataTable.update();
}

function editWorkerClick(){
	theWorkerEditPopup.dataTable.DWRObject=Workers; //Confuses the save() call into working. 
	theWorkerEditPopup.show(personId);
	//overide the existing save behavior to save and refresh the browser
	
	document.getElementById("saveButton").onclick = function() { theWorkerEditPopup.save(); document.location.reload(true); };
    }

function init() {
    theConfig = new MobileWorkerJobsDataTableConfig(MobileWorkerJobs, [], [4,5,6], "assignmentsPagemeter", assignmentsTableBody);
    assignmentsDataTable = new IWMDataTable(theConfig);
    assignmentsDataTable.update();
	
	setupWorkerJobsTable();
	//Clear JobsCriteria session attribute to keep filters from being set to complete jobs only
	// TODO: this better.
	
}
callOnLoad(init);

</script>
<div id="blarg" style="visibility: none;">
<!-- save() from the edit worker popup needs a table to reference -->
<table id="theWorkersTable"></table>
</div>
<!-- We have to remove a pagenator since they don't work here.-->
<style type="text/css">
    #assignmentsPagemeter {
    display:none; 
    }
    #assignmentsTable {
    margin-bottom: 20px;
    }
</style>

<nested:root name="WorkerDetailsForm">
<table width="945" style="font-size:8pt">
		<colgroup>
            <col style="width:85px;" />
            <col  />
            
            <col style="width:85px;" />
            <col style="width:100px;" />
        </colgroup>
<tr>
		<td>Name:</td>
		<td id="personName"><nested:write property="personName"/> </td>
		
		
		<td>Email:</td>
		<td id="email"><nested:write property="email"/></td>
</tr>

<tr>
	<td>Organization: </td>
	<td id="organization"><nested:write property="organization"/></td>
	
	
	<td>Phone: </td>
	<td id="phone"><nested:write property="phone"/></td>
</tr>

</table>
<P>

<input type="button" class="button" value="Edit this Worker" onclick="editWorkerClick()"/>
<%if(request.isUserInRole("DefOrg")){%>
<input type="button" class="button" value="Worker Calendar" onclick="window.location='/WorkerCalendar.do?id=' + personId"/>
<%}%>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1)"/>
</nested:root>
<a:ajax id="assignmentsPagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3>Currently Assigned Jobs</h3>

<table class="dataTable" id="assignmentsTable">
    <colgroup>
		<col class="jobIdCol" />
        <col class="jobAgeCol" />
        <col class="statusCol" />
        <col class="typeCol" />
        <col class="descriptionCol" />
        <col class="objectIdCol" />
        <col class="locationCol" />
        <col class="tasksCol" />
    </colgroup>

    <thead>
        <tr>
            <td style='text-align:center;'><bean:message key="jobId"/></td>
            <td style='text-align:center;'>Age</td>
            <td style='text-align:center;'><bean:message key="status"/></td>
            <td style='text-align:center;'><bean:message key="jobType"/></td>
            <td><bean:message key="description"/></td>
            <td><bean:message key="object"/></td>            
            <td><bean:message key="location"/></td>
            <td><div style='text-align: center'>Est<br/>Time</div></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="assignmentsTableBody">
 
    </tbody>
</table>

<a:ajax id="jobsPagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3>Jobs Completed by this Worker</h3> 
<table class="dataTable" id="workerJobsTable">
    <colgroup>
        <col class="jobIdCol" />
        <col class="jobAgeCol" />
        <col class="statusCol" />
        <col class="typeCol" />
        <col class="descriptionCol" />
        <col class="objectIdCol" />
        <col class="locationCol" />
        <col class="tasksCol" />
        <col class="tasksCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_id' style='text-align:center;'><bean:message key="jobId"/></td>
            <td id='sort_earliestStart' style='text-align:center;'>Age</td>
            <td id='sort_status' style='text-align:center;'><bean:message key="status"/></td>
            <td id='sort_jobType' style='text-align:center;'><bean:message key="jobType"/></td>
            <td id='sort_description'><bean:message key="description"/></td>
            <td><bean:message key="object"/></td>
            <td id='sort_locator'><bean:message key="location"/></td>
            <td class="tasksCol">Est<br/>Time</td>
            <td class="tasksCol">Total<br/>Time</td>      
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="workerJobsTableBody">
    </tbody>
</table>
<input type="button" class="button" value="Back to Previous Page" onclick="history.go(-1)"/>