<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ObjectsData.js"></script>
<script type='text/javascript' src="dwr/interface/MobileWorkerJobTasks.js"></script>
<script type='text/javascript' src="dwr/interface/MobileWorkerJobs.js"></script>
<script type='text/javascript' src="dwr/interface/JobTaskActions.js"></script>
<script type='text/javascript' src="dwr/interface/JobTaskTimes.js"></script>
<script type='text/javascript' src="dwr/interface/JobHistory.js"></script>
<script type='text/javascript' src="dwr/interface/JobCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>



<script type="text/javascript">
var jobId=<%=request.getParameter("id")%>;
    //Assignment History populator
    function getJobHistory() {
    var fillJobHistoryTable = function (response) {
        if(response.items && response.items.length > 0) $("jobHistory").style.display = 'block';
        DWRUtil.removeAllRows("jobHistoryTableBody");
            DWRUtil.addRows("jobHistoryTableBody", response.items, [
                    function(item){return item.dayDisplay;},
                    function(item){return item.totalTime;},
                    function(item){
                        return "<a href='/WorkerDetails.do?id=" + item.personId + "'\'>"+item.name+"</a>";
                        },
                    function(item){return item.hierarchy;},
                    function(item){return item.scheduledBy + '@' + item.scheduledTimeDisplay;} ],
                    {cellCreator:function(){return document.createElement("td");}}
                    );    }
    searchCriteria = new Object();
    searchCriteria.id=jobId;
    JobHistory.getData(fillJobHistoryTable, searchCriteria);
	}

    //Action list populator
        var MWActionTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject, theSortCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            searchCriteria.id=JobData.jobTaskId; /* NOTE: this is only grabbing the first task */
            return searchCriteria;
        },
        getSequence:function(item){return item.sequence},
        getAction:function(item){return item.verb + " "+ item.name + " " + item.modifier;},
        getFC:function(item){
        	var txt = item.fieldCondition==null?'':item.fieldCondition;
        	var txfld = '<textarea disabled rows="1" style="width:100%; resize:vertical;" id='+item.id+' >';
				txfld += txt+'</textarea>';
        	 return txfld;
            },

        
        getFormaters: function(){return [this.getAction, this.getFC];}
    });

    function editJobClick(){
    	theJobEditPopup.dataTable.DWRObject=Jobs; //Confuses the save() call into working. 
    	theJobEditPopup.show(jobId);
		//overide the existing save behavior to save and refresh the browser
		
		document.getElementById("bn_save").onclick = function() { theJobEditPopup.save(); document.location.reload(true); };
        }

    function addFollowupClick(){
    	theJobEditPopup.dataTable.DWRObject=Jobs; //Confuses the save() call into working. 
		if (JobData.objectRef.startsWith("AreaTarget")){
			//We start with step one, but use the locator to fill it out
			thePopupManager.showPopup('jobs_selectObject');
			objectLocatorFilter.populateChain(JobData.locatorId);
			objectClassFilter.populateChain();
			refreshObjectList();
			
			} else { 
			//Jump straight to step two :-D
    		createJobWizardStep2.show();
			}
		//overide the existing save behavior to save and refresh the browser
		document.getElementById("bn_save").onclick = function() { theJobEditPopup.save(); alert("Follow-up Job Created."); };
        }

    
    
	function init(){
		//TODO: In the future, this page will be accessed from several places. Be sure to make the "Back to Jobs" buttons behave properly.
		
		JobData = new Object(); //This holds all the stuff, mostly used for dwr lookups. Static info is dropped in by struts.
	    JobData.jobId='<bean:write name="CURRENT_FORM" property="id"/>';
	    JobData.objectId='<bean:write name="CURRENT_FORM" property="objectId"/>';
	    JobData.locatorId='<bean:write name="CURRENT_FORM" property="locatorId"/>';
	    JobData.objectRef='<bean:write name="CURRENT_FORM" property="objectRef"/>';
	    JobData.jobTypeId='<bean:write name="CURRENT_FORM" property="jobTypeId"/>';
	    JobData.jobTaskId='<bean:write name="CURRENT_FORM" property="jobTaskId"/>'; //Assumes 1:1 job:task

	    JobCreateWizard.selectObject(JobData.objectId); //Sets the object we're working with incase we add a job
		if (JobData.objectRef.startsWith("AreaTarget")){
			//We want to NOT display a link in the info at the top of the page
			document.getElementById("object").innerHTML = JobData.objectRef;
			}

		//Data set, build page

		//Actions
		var MWActionDataTable;
		var theConfig1 = new MWActionTableConfig(JobTaskActions, ["sequence"]);
        MWActionDataTable = new IWMDataTable(theConfig1);
        MWActionDataTable.dataTableId="actionDataTableBody";

        //Assignment history
        getJobHistory();


		//Batch the big stuff
        DWREngine.beginBatch();
        MWActionDataTable.update();
        DWREngine.endBatch();

	}
		
    callOnLoad(init);
</script>


<style type="text/css">
    td.jobinfo {
        font-weight: bold;
        padding-right:30px;
        vertical-align:top;
    }
    
    .pagemeter {
    display:none; 
    }
    
</style>
<nested:root name="CURRENT_FORM">

<div id="blarg" style="visibility: none;">
<!-- save() from the edit job popup needs a table to reference -->
<table id="theJobDataTable"></table>
</div>

<!-- New table -->
<table width="945" style="font-size:8pt">
		<colgroup>
            <col style="width:85px;" />
            <col style="width:437px;" />
            
            <col style="width:85px;" />
            <col style="width:337px;" />
        </colgroup>
<tr>
		<td>Priority: </td>
		<td id="priority"><nested:write property="priority"/></td>
		
		
		<td>Created: </td>
		<td id="createdDate"><nested:write property="createdDate"/></td>
</tr>

<tr>
	<td>Object: </td>
	<td id="object">
	<a href=/ObjectDetails.do?id=<nested:write property="objectId"/>>
	<nested:write property="objectRef"/></a></td>
	
	
	<td>By: </td>
	<td id="by"><nested:write property="name"/></td>
</tr>

<tr>
	<td>Location: </td>
	<td id="locator">
	<nested:write property="fullLocator"/>
	</td>
	

	<td>
	<nested:present property="email">
	Email: 
	</nested:present>
	</td>
	<td id="byEmail"><nested:write property="email"/>
	</td>

</tr>

<tr>
	<td>Skill: </td>
	<td><nested:write property="skillAndLevel"/></td>

	<td>
	<nested:present property="phone">
	Phone: 
	</nested:present>
	</td>
	<td><nested:write property="phone"/></td>
	
</tr>

<tr>
	<td>
	Organization:
	</td>
	
	<td>
	<nested:write property="fullOrganization"/>
	</td>
</tr>

<tr>
	<td valign="top">
	Note: 
	</td>

	<td>
	<nested:write property="note"/>
	</td>
<td valign="top">
<nested:present property="tenantNote">
Requester Note:  
</nested:present>
</td>

<td>
<nested:present property="tenantNote">
<nested:write property="tenantNote"/>
</nested:present>
</td>
</tr>

<tr>

<td>Est Time:</td><td id='estTime'><nested:write property="estTime"/></td>

<td>Earliest start:</td><td><nested:write property="earliestDate"/></td>
</tr>
<tr>
<td>Current State: </td>
<td id="stateString">
<nested:write property="stateString"/>
</td>

<td>Latest start:</td><td><nested:write property="latestDate"/></td>

</tr>

</table>

<input type="button" class="button" id="editButton" value="Edit this Job" onclick="editJobClick()"/>
<input type="button" class="button" id="followupButton" value="Add Follow-up Job" onclick="addFollowupClick()"/>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1)"/>
</nested:root>


<div id="jobHistory" >
    <h3>Assignment History and Time Recorded</h3>
    <table class="dataTable" id="jobHistoryTable">
        <colgroup>
            <col class="scheduledDateCol" style="width:50px;"/>
            <col class="timeCol" style="width:25px;"/>
            <col class="workerCol" />
            <col class="hierarchyCol" />
            <col class="assignedByCol" style="width:80px;" />
        </colgroup>
        <thead>
            <tr>
                <td>Assignment Date</td>
                <td>Recorded<br/> Time</td>
                <td><bean:message key="worker"/></td>
                <td><bean:message key="organization"/></td>
                <td>Assigned By</td>
            </tr>
        </thead>

        <tbody id="jobHistoryTableBody">
        </tbody>
    </table>
    <input type="button" class="button" id="assignmentButton" value="Change Worker Assignment" onclick="window.location='/JobSchedule.do?id=' + jobId"/>
</div>

<h3>Actions and Worker Feedback</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator"/>
<table class="dataTable" id="mobileWorkerActionsTable">
    <colgroup>
        <col class="actionCol" style="width:325px;"/>
        <col class="fcCol" />
    </colgroup>

    <thead >
        <tr>
            <td class="actionCol" id='sort_sequence'>Action</td>
            <td class="fcCol">Field Condition</td>
            
        </tr>
    </thead>
                              
    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="actionDataTableBody">
    </tbody>
</table>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1);"/>