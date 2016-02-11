<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ page import="org.mlink.iwm.util.Config"%>

<script type='text/javascript' src="dwr/interface/JobTaskActions.js"></script>

<script type="text/javascript" >
    var MWTaskActionsPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(objectId, jobTaskId, jobTaskTimeId, jobTypeId, workerStatus, jobStatus, taskTimeKeeping, inventoryKeeping, scheduledDate){
            this.theData = new Object();
            this.theData.id=jobTaskTimeId;
            this.theData.objectId=objectId;
            this.theData.jobTaskId=jobTaskId;
            this.theData.workerStatus=workerStatus;
            this.theData.jobStatus=jobStatus;
            this.theData.taskTimeKeeping=taskTimeKeeping;
            this.theData.inventoryKeeping=inventoryKeeping;
            this.theData.scheduledDate=scheduledDate;
            this.theData.jobActions = new Array();   // array for keeping modified FC
            var fillTasksDataTable = function (response) {
                DWRUtil.removeAllRows("mobileWorkerTasksTableBody");
                DWRUtil.addRows("mobileWorkerTasksTableBody", response.items, [getTaskDescription, getEstTime, getTotalTime, getActions]);
            }
            
            var searchCriteria = new Object();
            searchCriteria.id=jobTaskId;
            var _this=this;
            JobTaskActions.getData(searchCriteria,0,1000,"sequence","ASC",{callback:function(response){_this.populate(response);}});
            MobileWorkerJobTasks.getItem(function(item){
                if(item.time){
                    var hourMinute=HourMinuteConveter.splitMinutes(item.time);
                    DWRUtil.setValue("timeHours",hourMinute.hours);
                    DWRUtil.setValue("timeMinutes",hourMinute.minutes);
                }
            },jobTaskTimeId);

        	if(this.theData.workerStatus=='DUN' || this.theData.workerStatus== 'NYS' || this.theData.workerStatus== 'BRK'){
        		$('saveButton').style.visibility = 'hidden';
        	}

        	$('replaceButton').style.visibility = 'hidden';
        	
        	if(this.theData.taskTimeKeeping=='true'){
            	$('timeHours').disabled=true;
            	$('timeMinutes').disabled=true;
            	var taskButton = document.getElementById('taskButton');
            	if(this.theData.workerStatus=='DUN' || this.theData.workerStatus== 'NYS' || this.theData.workerStatus== 'BRK'){
            		$('taskButton').style.visibility = 'hidden';
               		//$('saveButton').style.visibility = 'hidden'; 
                }else if(this.theData.workerStatus=='IP'){
                	if(this.theData.jobStatus=='JIP'){
                    	taskButton.value='Stop Task';
                    	$('saveButton').style.visibility = 'visible';

                    	if(this.theData.inventoryKeeping=='true'){
	                    	if(jobTypeId==1130){
	            				$('replaceButton').style.visibility = 'visible';
	                        }
                    	}
                	}else if(this.theData.jobStatus=='DJO' || this.theData.jobStatus=='DPD' || this.theData.jobStatus=='SJO'){
                    	$('taskButton').style.visibility = 'visible';
                    	$('saveButton').style.visibility = 'hidden';
                   	}else{
                   		$('saveButton').style.visibility = 'hidden';
                   		$('taskButton').style.visibility = 'hidden';
                	}
                }
            }else{
            	$('taskButton').style.visibility = 'hidden';
            	if(this.theData.inventoryKeeping=='true'){
                    if(jobTypeId==1130){
    					$('replaceButton').style.visibility = 'visible';
                	}
            	}
            }
        },
        populate: function(response){
            DWRUtil.removeAllRows("JobActionsTable");
            DWRUtil.addRows("JobActionsTable", response.items, [
                    function(item){return item.verb + ' ' + item.name + ' ' + item.modifier;},
                    function(item){
                        var txt = item.fieldCondition==null?'':item.fieldCondition;
                        return  '<textarea rows="2" cols="20" id=\''+item.id+'\' onchange=\'updateFieldCondition(this)\'>'+txt+'</textarea>';} ]);

            thePopupManager.showPopup('mobileWorker-tasks_actions');
        },

        save: function(){
            this.theData.time=HourMinuteConveter.toMinutes(DWRUtil.getValue("timeHours"),DWRUtil.getValue("timeMinutes"));
            var callback = function(message){
                if(message && message.length > 0)
                    alert(message);
            }

            var list = theMWTaskActionsPopup.theData.jobActions;
            for (var i = 0; i < list.length; i++) {
                var  textarea = $(list[i].id);
                if(!verifylength(textarea)){
                    alert('Text size can not be greater than '+ maxlength+ ' characters \n' + textarea.value );
                    list[i].fieldCondition=textarea.value;
                    return false;
                }
            }
            DWREngine.beginBatch();
            for (var i = 0; i < list.length; i++) {
                JobTaskActions.saveItem(callback,list[i])
            }
            
            if(this.theData.taskTimeKeeping!='true'){
	            this.persist(this.theData);
            }
            DWREngine.endBatch();
        },

        saveClose: function(){
        	 time=HourMinuteConveter.toMinutes(DWRUtil.getValue("timeHours"),DWRUtil.getValue("timeMinutes"));
        	 if (time < 1)
             {alert("You must record time before closing this job.");
              return;}
        	 theMWTaskActionsPopup.save();
        	 closeJob();}
    });
    
    var maxlength=150;
    function updateFieldCondition(textarea){
        var alreadyPresent = false;
        var list = theMWTaskActionsPopup.theData.jobActions;
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
        theMWTaskActionsPopup.theData.jobActions = list;
    }

    function verifylength(source){
        var number = source.value.length;
        if(number > maxlength-1) {
            source.value=source.value.substring(0,(maxlength-1));
            return false;
        }
        return true;
    }

    function saveSSTask(taskButton, jobTaskId, jobTaskTimeId, scheduledDate){
    	if(taskButton.value=='Start Task'){
   		 	 JobTaskActions.startTask(jobTaskId, jobTaskTimeId, scheduledDate
   	    			 ,{callback:function(message){handleReturn(true, message)}});
    	}else{
   	    	 JobTaskActions.stopTask(jobTaskId, jobTaskTimeId, scheduledDate
   					,{callback:function(message){handleReturn(false, message)}});
    	}
    }

    function doReplace(objectId, jobId, jobTaskId, jobTaskTimeId){
   	 	 JobTaskActions.doReplace(objectId, jobId, jobTaskId, jobTaskTimeId
			 ,{callback:function(message){handleReplaceReturn(message)}});
    }

    function handleReturn(isStart, message){
    	var taskButton = document.getElementById('taskButton');
    	if(isStart==true){
   			if(message && message.length > 0){
   				alert(message);
            }else{
        		taskButton.value='Stop Task';
           		$('saveButton').style.visibility = 'visible';
           		$('replaceButton').style.visibility = 'visible';
            }
        }else{
        	if(message && message.length > 0){
   				alert(message);
	            taskButton.value='Start Task';
	       		$('saveButton').style.visibility = 'hidden';
	       		$('replaceButton').style.visibility = 'hidden';
	
	       		MobileWorkerJobTasks.getItem(function(item){
	                if(item.time){
	                    var hourMinute=HourMinuteConveter.splitMinutes(item.time);
	                    DWRUtil.setValue("timeHours",hourMinute.hours);
	                    DWRUtil.setValue("timeMinutes",hourMinute.minutes);
	                }
	            },theMWTaskActionsPopup.theData.id);
        	}
        }
    }

    function handleReplaceReturn(message){
		if(message && message.length > 0){
			alert(message);
        }else{
            alert('Replace completed successfully.');
       		if($('replaceButton')!=null){
           		$('replaceButton').style.visibility = 'hidden';
       		}
        }
    }
    
    callOnLoad(function(){
        theMWTaskActionsPopup = new MWTaskActionsPopup("mobileWorker-tasks_actions", "MWTaskActionsForm", MWTaskDataTable, true);
    });

    
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#mobileWorker-tasks_actions {
        width: 650px;
        left: -335px;
    }

    div#actionList {
        overflow: auto;
        min-height: 100px;
        background-color:#fafafa;
    }	

    div#mobileWorker-tasks_actions textarea {
        width: 380px;
    }
</style>

<!--  -->
<div class="popup" id="mobileWorker-tasks_actions">
    <div class="popupHeader">
        <h2>Task Actions - Job ID: <script>document.write(JobData.jobId);</script></h2>
    </div>
    <div class="popupBody">
        <form action="" id="MWTaskActionsForm">

            <h3>Actions</h3>

            <div id="actionList">
                <table class="dataTable">
                    <tr>
                        <th>Action</th>
                        <th>Field Notes</th>
                    </tr>
                    <tbody id="JobActionsTable">
                    </tbody>
                </table>
            </div>
            <center><h3>Elapsed Time</h3>
            <P>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td><a:ajax id="timeHours" name="iwm.spinner"/></td>
                    <td><a:ajax id="timeMinutes" name="iwm.spinner"/></td>
                    <td><input type="button" class="button" id="closeJobButton" value="Save & Close Job" onclick="theMWTaskActionsPopup.saveClose()"/></td>
                    <td><input type="button" class="button" id="emailFeedback" value="Email Feedback" onclick="emailFeedbackClick()"/></td>
        		</tr>
                <tr><td class="fieldComment">Hours</td><td class="fieldComment">Mins</td></tr>
            </table></center>
        </form>

        <input type="button" id="saveButton" value="Save" class="button" onclick="theMWTaskActionsPopup.save();">
        <input type="button" id="cancelButton" value="Cancel" class="button" onClick="thePopupManager.hidePopup();">

    </div>
</div>

