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

<!-- 
Get a list of JobTasks. Throw out anything but the first one (TODO? maybe not).

Use the first Job Task to get the Action list, display that. 

Use the first Job Task to get the Job Task Time. Display that.

Get Object Data, display if needed. 

 -->

<script type="text/javascript">
	var maxlength = 4000; //Action FC size limit
	
    var JobData = new Object(); //This holds all the stuff
    JobData.jobScheduleId=<bean:write name="CURRENT_FORM" property="id"/>;
    JobData.jobId=<bean:write name="CURRENT_FORM" property="jobId"/>;
    JobData.objectId=<bean:write name="CURRENT_FORM" property="objectId"/>;
    JobData.jobStatus='<bean:write name="CURRENT_FORM" property="jobStatus"/>';
    JobData.workerStatus='<bean:write name="CURRENT_FORM" property="workerStatus"/>';
    JobData.jobTypeId='<bean:write name="CURRENT_FORM" property="jobTypeId"/>';
    JobData.objectRef='<bean:write name="CURRENT_FORM" property="objectRef"/>';
    JobData.jobTaskId='<bean:write name="CURRENT_FORM" property="jobTaskId"/>';
    JobData.modifiedFcs = new Array(); //for holding modified field conditions
	JobData.TaskTime = new Object(); //Holds time info


    
	MobileWorkerJobTasks.getJobTaskTime(JobData.jobScheduleId, 
		function(callback){
		JobData.TaskTime = callback;
		document.getElementById('estTime').innerHTML = JobData.TaskTime.estTime;

		if(JobData.TaskTime.time){
            var hourMinute=HourMinuteConveter.splitMinutes(JobData.TaskTime.time);
            DWRUtil.setValue("timeHours",hourMinute.hours);
            DWRUtil.setValue("timeMinutes",hourMinute.minutes);
        }
		});
	
	
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
        	var txfld = '<textarea rows="1" style="width:100%; resize:vertical;" id='+item.id+' name='+item.name+' ';
				if (JobData.jobStatus == 'DUN'){
					txfld += 'disabled="disabled">';
					}else{
						txfld += 'onchange="updateFieldCondition(this)">';
						}
        		txfld += txt+'</textarea>';
        	 return txfld;
            },

        
        getFormaters: function(){return [this.getAction, this.getFC];}
    });
    
    var MWObjectDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols, theToolTipCols, thePageMeter) {
            this.superInit(theDWRObject,theSortCols, theToolTipCols, thePageMeter);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            searchCriteria.id=JobData.objectId;
            searchCriteria.isDisplayOnly=true;
            return searchCriteria;
        },
        getLabel: function(item){
			//If we're running this, then we have object data to display
			document.getElementById("objectdata").style.display="block";
            return item.dataLabel;},
        getValue:  function(item){return item.dataValue;},
        getEdit:  function(item){
            if(item.isEditInField == 1)
                return '<div class="icon" onclick="theObjectDataPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
            else
                return '';
        },
        getFormaters: function(){return [this.getLabel, this.getValue, this.getEdit];}
    });

    var MWActionDataTable;
    function init() {        
        var theConfig1 = new MWActionTableConfig(JobTaskActions, ["sequence"]);
        MWActionDataTable = new IWMDataTable(theConfig1);
        MWActionDataTable.dataTableId="actionDataTableBody";

        var theConfig2 = new MWObjectDataTableConfig(ObjectsData, []);
        MWObjectDataTable = new IWMDataTable(theConfig2);
        MWObjectDataTable.dataTableId="mobileWorkerObjectDataTableBody";

        DWREngine.beginBatch();
        MWActionDataTable.update();
        MWObjectDataTable.update();
        DWREngine.endBatch();

		if (JobData.jobStatus == 'DUN'){
			//Job is already closed.
			var button = document.getElementById("closeJobButton");
			button.disabled=true;
			button.value="Job Closed";
			document.getElementById("timeHours").disabled=true;
			timeHoursSpinner.decrease=function() {};
			timeHoursSpinner.increase=function() {};
			
			document.getElementById("timeMinutes").disabled=true;
			timeMinutesSpinner.decrease=function() {};
			timeMinutesSpinner.increase=function() {};
			
			}
    }
    callOnLoad(init);

   	function updateFieldCondition(textarea){
		var alreadyPresent = false;
        var list = JobData.modifiedFcs;
        for (var i = 0; i < list.length; i++) {
            if(list[i].id == textarea.id){
                alreadyPresent = true;
                list[i].fieldCondition=textarea.value;
            }
        }
        if(!alreadyPresent){
            var jobAction = new Object();
            jobAction.id=textarea.id;
            jobAction.fieldCondition=textarea.value;
            list[list.length] = jobAction;
        }
        JobData.jobActions = list;
        //Save it. This only executes when there's a valid change.
        save();
   	   	}

   	function save(){
		//If the job is DUN, we can skip saving, always. Nothing can change.
   	   	if (JobData.jobStatus == 'DUN') return true;
   	   	
       	JobData.TaskTime.time=HourMinuteConveter.toMinutes(DWRUtil.getValue("timeHours"),DWRUtil.getValue("timeMinutes"));
        var callback = function(message){
            if(message && message.length > 0)
                alert(message);
        }
        var list = JobData.modifiedFcs;
        for (var i = 0; i < list.length; i++) {
            var  textarea = $(list[i].id);
            if(!verifylength(textarea)){
                alert('Text size can not be greater than '+ maxlength+ ' characters- \n It has been shortened.');
                list[i].fieldCondition=textarea.value;
                return false;
            }
            //Check if it's a meter reading and complain if it's not just numbers.
            if ((textarea.name == "Meter" || textarea.name == "meter") && (/\D/.test(textarea.value))){
                alert('Meter readings must be numbers only');
                list[i].fieldCondition=textarea.value;
                return false;
            }
        }
        DWREngine.setAsync(false);
        document.getElementById("disabledZone").style.visibility="visible";
        //Save time
        MobileWorkerJobTasks.saveItem(callback,JobData.TaskTime);
        //Save Actions
        for (var i = 0; i < list.length; i++) {
            JobTaskActions.saveItem(callback,list[i]);
        }
        DWREngine.setAsync(true);
        document.getElementById("disabledZone").style.visibility="hidden";
        return true;
    }

    function verifylength(source){
        var number = source.value.length;
        if(number > maxlength-1) {
            source.value=source.value.substring(0,(maxlength-1));
            return false;
        }
        return true;
    }


   	/* BUTTON HANDLERS */
   	
   	function emailFeedbackClick() {
   	   	//Every button saves.
   	   	save(false); //Technically, this a navigation event, so we can't async.
	   
 	   var link ="mailto:";
 		if (document.getElementById("byEmail").innerHTML != ""){
 			link += escape(document.getElementById("byEmail").innerHTML);
 			}
 		link += "?subject=Job ID " + JobData.jobId;

 		link += "&body=";
 		link += escape('Job ID: '+JobData.jobId+ '\n');
 		link += escape("Created: " +document.getElementById("createdDate").innerHTML+ "\n");
 		link += escape("Location: " + document.getElementById("locator").innerHTML + '\n');
 		//no need to include object if it's an AreaTarget
 		if (JobData.objectRef.substr(0,10) != "AreaTarget"){ 
 	 	link += escape("Object: " + JobData.objectRef + '\n');
 	 	}
 		link += escape("Description: " + document.getElementById("description").innerHTML + '\n');
 		link += escape("Estimated Time: " +JobData.TaskTime.estTime+ "\n");
 		
 		
 		link += escape("\n");
 		//Include notes, if they exist
 		if (document.getElementById("tenantNote") != null) link += escape("Requestor Note: " + document.getElementById("tenantNote").innerHTML + "\n" );
 		if (document.getElementById("note") != null) link += escape("Supervisor Note: " + document.getElementById("note").innerHTML + "\n");
 		
 		//blank space
 		link += escape("---------- \n");
 		link += escape("Job Status: ")
 		if (document.getElementById("closedDate").innerHTML == "") {link += escape("Open \n");
 		} else {
 	 	link += escape("Closed, " + document.getElementById("closedDate").innerHTML + "\n");
 	 	} 
 		link += escape("Time Recorded: " + document.getElementById("timeHours").value + ":" + document.getElementById("timeMinutes").value + "\n");
 		link += escape("\n");
 		link += escape("Actions: \n\n");

 		//now we loop through the actions and FCs. We'll get what's currently in the JobActionsTable directly
 		var table = document.getElementById("actionDataTableBody");
 		for (var i = 0; i < table.rows.length; i++){
 			//Each row. 
 			link += escape(table.rows[i].cells[0].innerHTML + '\n');
 			link += escape(table.rows[i].cells[1].children[0].value + '\n\n');
 			}
		link += escape("Access this job in IWM: "+ "http://valencia.intelligentworkmanagement.com/JobDetails.do?id=" +JobData.jobId );
 	window.location=link;
    	};

   function backToJobsClick(){
       if (save()){
    	   document.location.href = 'MWJobs.do';
           }
	   
	   }

   function closeJobClick(){
	   save();
	   time=HourMinuteConveter.toMinutes(DWRUtil.getValue("timeHours"),DWRUtil.getValue("timeMinutes"));
  	 	if (time < 1) {alert("You must record time before closing this job."); return;}
		save();
	    if (confirm("Are you sure you want to close job " + JobData.jobId + "?")) {
	    	var callback=function(message){
	           if(message && message.length > 0)
	                    alert(message);
	                else
	                    document.location.href = 'MobileWorkerTasks.do?id=' +JobData.jobScheduleId;
	            }
	            MobileWorkerJobs.deleteItem(JobData.jobId,{ callback:callback,async:false});
	        }
	   }

    	
</script>

<!-- We have to remove the pagenators since they don't work here.
Also, hiding the Object Data table unless the JS specifically shows it -->
<style type="text/css">
    td.jobinfo {
        font-weight: bold;
        padding-right:30px;
        vertical-align:top;
    }
    .pagemeter {
    display:none; 
    }
    #objectdata {
    display:none;
    }
    
</style>

<h3>Job Information</h3>
<nested:root name="MobileWorkerTasksMaintForm">

<!-- New table -->
<table width="945" style="font-size:8pt">
		<colgroup>
            <col style="width:85px;" />
            <col  />
            
            <col style="width:85px;" />
            <col style="width:100px;" />
        </colgroup>
<tr>
		<td>Job ID: </td>
		<td id="jobId"><nested:write property="jobId"/> </td>
		
		
		<td>Created: </td>
		<td id="createdDate"><nested:write property="createdDate"/></td>
</tr>

<tr>
	<td>Description: </td>
	<td id="description"><nested:write property="description"/></td>
	
	
	<td>By: </td>
	<td id="by"><nested:write property="name"/></td>
</tr>

<tr>

	<td>Object: </td>
	<td><bean:write name="CURRENT_FORM" property="objectRef"/></td>

	<td>Email: </td>
	<td id="byEmail"><nested:write property="email"/></td>

</tr>

<tr>

	<td>Location: </td>
	<td id="locator"><bean:write name="CURRENT_FORM" property="fullLocator"/></td>
	

	<td>Phone: </td>
	<td><nested:write property="phone"/></td>

	
</tr>
<tr><td>Estimated Time:</td><td id='estTime'></td>
<td>Closed: </td><td id='closedDate'><nested:write property="closedDate"/></td>

</tr>

<nested:present property="tenantNote">
<tr>
<td>Requester Note: </td>
<td colspan=3 id="tenantNote">
<nested:write property="tenantNote"/></td>
</tr>
</nested:present>

<nested:present property="note">
<tr>
<td>Supervisor Note: </td>
<td colspan=3 id="note">
<nested:write property="note"/></td>
</tr>
</nested:present>

</table>



</nested:root>

<h3><bean:write name="CURRENT_FORM" property="description"/></h3>
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

<div  id="buttons" style="text-align: center">
<br>
Record Time: <a:ajax id="timeHours" name="iwm.spinner"/> hrs <a:ajax id="timeMinutes" name="iwm.spinner"/> mins 
<br>
    <input type="button" class="button" id="emailFeedback" value="Email Feedback" onclick="emailFeedbackClick()"/>
    <input type="button" class="button" id="closeJobButton" value="Close Job" onclick="closeJobClick()"/>
    <input type="button" class="button" value="Back To Jobs" onclick="backToJobsClick()"/>
    
</div>

<div id="objectdata">
<h3><bean:message key="datums"/></h3>
<table class="dataTable" id="mobileWorkerObjectDataTable">
    <colgroup>
        <col class="labelCol" style="width:200px;"/>
        <col class="valueCol" />
        <col class="editCol" style="width:50px;"/>
    </colgroup>
    <thead >
        <tr>
            <td class="labelCol" id='sort_dataLabel'><bean:message key="label"/></td>
            <td class="valueCol" id='sort_dataValue'><bean:message key="value"/></td>
            <td class="editCol" ><center><bean:message key="edit"/></center></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="mobileWorkerObjectDataTableBody">
    </tbody>
</table>
</div>

