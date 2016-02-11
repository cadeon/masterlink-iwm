<%@ page import="org.mlink.iwm.struts.action.WebConstants"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/TaskGroups.js"></script>
<script type='text/javascript' src="dwr/interface/Objects.js"></script>

<script type="text/javascript" >

var TaskPropertiesModerator = Class.create();
TaskPropertiesModerator.prototype = {
    initialize: function() {
//        this.freqMonthsRadio=$("freqMonthsRadio");
//        this.freqDaysRadio=$("freqDaysRadio");
        this.meterFreqRadio=$("meterFreqRadio");
        this.timeFreqRadio=$("timeFreqRadio");
        this.taskTypeId=$("taskTypeId");
        this.frequencyId=$("frequencyId");
        this.freqDays=$("freqDays");
        this.freqMonths=$("freqMonths");
        this.runHoursThreshInc=$("runHoursThreshInc");
        this.timeIntervalSelect=$("timeIntervalSelect");
        this.runHoursThreshInc.onchange = this.runHoursThreshIncOnChange.bindAsEventListener(this);

        this.timeIntervalSelect.onchange = this.timeIntervalSelectOnChange.bindAsEventListener(this);
//        this.freqMonthsRadio.onclick = this.freqMonthsRadioOnClick.bindAsEventListener(this);
//        this.freqDaysRadio.onclick = this.freqDaysRadioRadioOnClick.bindAsEventListener(this);
        this.meterFreqRadio.onclick = this.meterFreqRadioOnClick.bindAsEventListener(this);
        this.timeFreqRadio.onclick = this.timeFreqRadioOnClick.bindAsEventListener(this);
        this.taskTypeId.onchange = this.taskTypeOnChange.bindAsEventListener(this);
        this.frequencyId.onchange = this.frequencyOnChange.bindAsEventListener(this);
        this.freqDays.onchange = this.freqDaysOnChange.bindAsEventListener(this);
        this.freqMonths.onchange = this.freqMonthsOnChange.bindAsEventListener(this);
        this.runHoursThreshInc.onchange = this.runHoursThreshIncOnChange.bindAsEventListener(this);

    },
        /*
    freqMonthsRadioOnClick: function(event) {
        this.freqMonths.disabled=false;
        this.freqDays.disabled=true;
    },
    freqDaysRadioRadioOnClick: function(event) {
        this.freqMonths.disabled=true;
        this.freqDays.disabled=false;
    },
    */

    timeIntervalSelectOnChange: function(event) {
        if(this.timeIntervalSelect.value == 'days'){
            $('freqDays').style.display = "";
            $('freqMonths').style.display = "none";
            this.freqMonths.disabled=true;
            this.freqDays.disabled=false;

        } else {
            $('freqDays').style.display = "none";
            $('freqMonths').style.display = "";
            this.freqMonths.disabled=false;
            this.freqDays.disabled=true;

        }
    },

    meterFreqRadioOnClick: function(event) {
        this.runHoursThreshInc.disabled=false;
        this.frequencyId.disabled=true;
        $('timeBasedOptions').style.display = "none";
        $('meterBasedOptions').style.display = "block";
    },
    timeFreqRadioOnClick: function(event) {
        this.runHoursThreshInc.disabled=true;
        this.frequencyId.disabled=false;
        $('timeBasedOptions').style.display = "block";
        $('meterBasedOptions').style.display = "none";
    },
    taskTypeOnChange: function(event) {
        theTaskEditPopup.theTask.taskTypeId = DWRUtil.getValue(this.taskTypeId);
        this.populate(theTaskEditPopup.theTask);
    },
    frequencyOnChange: function(event) {
        theTaskEditPopup.clearRoutineProperties();
        theTaskEditPopup.theTask.frequencyId = DWRUtil.getValue(this.frequencyId);
        this.populate(theTaskEditPopup.theTask);
    },
    freqDaysOnChange: function(event) {
        theTaskEditPopup.clearRoutineProperties();
        theTaskEditPopup.theTask.freqDays = DWRUtil.getValue(this.freqDays);
        this.populate(theTaskEditPopup.theTask);
    },

    freqMonthsOnChange: function(event) {
        theTaskEditPopup.clearRoutineProperties();
        theTaskEditPopup.theTask.freqMonths = DWRUtil.getValue(this.freqMonths);
        this.populate(theTaskEditPopup.theTask);
    },
    runHoursThreshIncOnChange: function(event) {
        theTaskEditPopup.clearRoutineProperties();
        theTaskEditPopup.theTask.runHoursThreshInc = DWRUtil.getValue(this.runHoursThreshInc);
        var runHours=parseInt($('runHours').value);
        if(isNaN(runHours)) runHours=0;
        if (!isNaN(parseInt($('runHoursThreshInc').value))){
           var serviceDue = runHours + parseInt($('runHoursThreshInc').value);
           theTaskEditPopup.theTask.runHoursThresh = serviceDue;
        }
        this.populate(theTaskEditPopup.theTask);
    },
    clear: function(){
        $('RoutineFrequencySetings').style.display='none';
        $('CustomTimeInterval').style.display='none';
        this.frequencyId.disabled=true;
        this.runHoursThreshInc.disabled=true;
        this.freqDays.disabled=true;
        this.freqMonths.disabled=true;
        DWRUtil.setValue(this.frequencyId,"");
        DWRUtil.setValue(this.runHoursThreshInc,"");
        DWRUtil.setValue($('runHoursThresh'),"");
        DWRUtil.setValue(this.freqDays,"");
        DWRUtil.setValue(this.freqMonths,"");
    },
    populate: function(task) {
        this.clear();

        //if it is a routine task
        if(task.taskTypeId==<%=String.valueOf(WebConstants.TaskTypeIdRoutine)%>){
            $('startDateTR').style.display='';

            $('RoutineFrequencySetings').style.display='';

            //if it is meter based
            if(task.runHoursThreshInc!=null)    {
                this.runHoursThreshInc.disabled=false;
                this.meterFreqRadio.checked=true;
                DWRUtil.setValue(this.runHoursThreshInc,task.runHoursThreshInc);
                DWRUtil.setValue($('runHoursThresh'),task.runHoursThresh);
                $('meterBasedOptions').style.display = '';
            }

            //if time based and freq is days
            else if(task.freqDays!=null){
                $('meterBasedOptions').style.display = 'none';
                $('freqDays').style.display = "";
                $('freqMonths').style.display = "none";
                this.timeIntervalSelect.selectedIndex = 0;

                this.freqDays.disabled=false;
                //this.freqDaysRadio.checked=true;
                this.frequencyId.disabled=false;
                DWRUtil.setValue(this.frequencyId,"custom");
                DWRUtil.setValue(this.freqDays,task.freqDays);
                this.timeFreqRadio.checked=true;
                $('CustomTimeInterval').style.display='';
            }
            //if month based time
            else if(task.freqMonths!=null){

                this.timeIntervalSelect.selectedIndex = 1;
                $('meterBasedOptions').style.display = 'none';
                $('freqDays').style.display = "none";
                $('freqMonths').style.display = "";


                this.freqMonths.disabled=false;
                this.frequencyId.disabled=false;
                //this.freqMonthsRadio.checked=true;
                this.timeFreqRadio.checked=true;
                DWRUtil.setValue(this.frequencyId,"custom");
                DWRUtil.setValue(this.freqMonths,task.freqMonths);
                this.timeFreqRadio.checked=true;
                $('CustomTimeInterval').style.display='';
            }else{
                $('meterBasedOptions').style.display = 'none';

                //this.frequencyId.disabled=false;
                DWRUtil.setValue(this.frequencyId,task.frequencyId);
                this.timeFreqRadio.checked=true;
                this.frequencyId.disabled=false;
                if(task.frequencyId=='custom'){
                    $('CustomTimeInterval').style.display='';
                    this.freqDays.disabled=false;
                    //this.freqDaysRadio.checked=true;
                }
            }
        }else{
            $('startDateTR').style.display='none';
        }

        DWRUtil.setValue($('expiryTypeId'), task.expiryTypeId);
        DWRUtil.setValue($('expiryNumOfDays'), task.expiryNumOfDays);
    }
}


var objectsCallback = function(item){
    if(item.runHours!=null){
        $('runHours').value=item.runHours;
    }
    if(item.active == 1){
        $('active').checked=true;
    }
}


var TaskEditPopup = Class.extend( AbstractCrudPopup, {
    initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        this.theTask;
        this.theObject;
        //Tasks no longer have orgs
        //this.taskOrganizationFilter =
        //        new OrganizationChain("taskOrganizationFilter", function(id){return}, false,false);
        
    },
    clearRoutineProperties: function(){
        this.theTask.frequencyId=null;
        this.theTask.runHoursThreshInc=null;
        this.theTask.freqDays=null;
        this.theTask.freqMonths=null;
        this.theTask.theTaskTypeId=null;
    },
    show: function(theId){
        this.theTask = new Object();
        _this = this;
        if(theId != null && theId != -1){
            $("taskPopupTitle").innerHTML= "Edit Task";
            ObjectTasks.getItem(function(response){_this.populate(response);}, theId);
        } else {
            //if no locator id is passed in that means it is an add. make a blank form.
            $("taskPopupTitle").innerHTML= "Add Task";
            this.theTask.id=-1;
            this.theTask.objectId=<%= request.getParameter("id") %>;
            this.theTask.custom=1;
            this.theTask.expiryTypeId=3;
            this.theTask.expiryNumOfDays=180;

            taskPropertiesModerator.populate(this.theTask);

            Objects.getItem(
                    function(response){
                        if(response.runHours!=null){
                            $('runHours').value=response.runHours;
                        }
                        if(response.active == 1){
                            $('active').checked=true;
                        }
                        //No more task orgs
                        //_this.taskOrganizationFilter.populateChain(response.organizationId);
                        },
                    this.theTask.objectId);

            $("custom").checked=true;
            $('customizeTask').style.display='none';
            disableEdit(false);

            thePopupManager.showPopup('objects-tasks_edit');
            $('description').focus();
        }
    },

    populate: function(item){
        //set DWR object
        this.theTask = item;
        FormValuesUtil.setFormValues($("TaskForm"),this.theTask);
        if(item.estTime){
            var hourMinute=HourMinuteConveter.splitMinutes(item.estTime);
            DWRUtil.setValue("estTimeHours",hourMinute.hours);
            DWRUtil.setValue("estTimeMinutes",hourMinute.minutes);
        }

        if(this.theTask.active==1) $("active").checked=true; else $("active").checked=false;

        taskPropertiesModerator.populate(this.theTask);

        if(this.theTask.custom==1){
            $("custom").checked=true;
            $('customizeTask').style.display='none';
            disableEdit(false);
        }else{
            $("custom").checked=false;
            $('customizeTask').style.display='';
            disableEdit(true);
        }

        Objects.getItem(
                function(response){
                    if(response.runHours!=null){
                        $('runHours').value=response.runHours;
                    }
                },
                this.theTask.objectId);
        //No more task orgs
        //this.taskOrganizationFilter.populateChain(this.theTask.organizationId);

        thePopupManager.showPopup('objects-tasks_edit');
        if(this.theTask.custom==1) $('description').focus();     // do not set focus on disbaled fields, make sure it us custom!
        else{
        	//Disable the org filters if not custom
        	/* No more task orgs.
            $('taskOrganizationFilter_0').disabled=true;
            $('taskOrganizationFilter_1').disabled=true;
            $('taskOrganizationFilter_2').disabled=true;
            $('taskOrganizationFilter_3').disabled=true;
            */
        }
        	

    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("TaskForm"),this.theTask);
            if(this.theTask.frequencyId=='custom') this.theTask.frequencyId=null;
            this.theTask.estTime=HourMinuteConveter.toMinutes(DWRUtil.getValue("estTimeHours"),DWRUtil.getValue("estTimeMinutes"));
            if($("active").checked)
                this.theTask.active=1; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
            else
                this.theTask.active=0;

            if($("custom").checked)
                this.theTask.custom=1;
            else
                this.theTask.custom=0;

            //use filter if($("organizationId").value=="-1") this.theTask.organizationId="";

            //this.theTask.organizationId = this.taskOrganizationFilter.currentSelectedId==null?'':this.taskOrganizationFilter.currentSelectedId;
            var _this = this;
            this.persist(this.theTask);
            return true;
        }
        else {
            return false;
        }
    }
});
var theTaskEditPopup;
callOnLoad(function(){
    taskPropertiesModerator = new TaskPropertiesModerator();
    theTaskEditPopup = new TaskEditPopup('objects-tasks_edit',"TaskForm", dataTable);
});

function displaySkillType(){
    var item= theTaskEditPopup.theTask;
    if(item && item.groupId!=null){
        $('skillTypeId').disabled=true;
        $('taskGroupWarning').style.display='block';
        TaskGroups.getItem(function(group){$('taskGroupName').innerHTML=group.description;}, item.groupId)
    }else{
        $('skillTypeId').disabled=false;
        $('taskGroupWarning').style.display='none';
    }

    if(item && item.custom!=1) $('skillTypeId').disabled=true;     //if not custom skill type must be disabled as any other customizable property

}

function updateActive(){
    var callback = function(item){
        if(item.active != 1){
            alert("Cannot activate task for inactive object. Activate Object first!");
            $('active').checked=false;
        }
        else if(theTaskEditPopup.theTask.taskTypeId==<%=String.valueOf(WebConstants.TaskTypeIdRoutine)%>)
            $('startDate').value=convertDateToString(new Date());
        else{
            $('startDate').value="";
        }
    }                            
    if($('active').checked){
        Objects.getItem(callback, theTaskEditPopup.theTask.objectId);
    }else
        $('startDate').value="";
}



function makeCustom(value){
    if(value){
        if(confirm("Changing properties (customization) will permanently detach task from its template. Changes to the template will not propagate to task level. Proceed?")){
            theTaskEditPopup.theTask.custom=1;
            disableEdit(false);
            return true;
        }else{
            return false;
        }
    }
}

function disableEdit(value){
    $('description').disabled=value;
    $('estTimeHours').disabled=value;
    $('estTimeMinutes').disabled=value;
    $('skillLevelId').disabled=value;
    $('workerTypeId').disabled=value;
    $('priorityId').disabled=value;
    $('taskTypeId').disabled=value;
    $('numberOfWorkers').disabled=value;
    $('frequencyId').disabled=value;
    $('runHoursThreshInc').disabled=value;
    $('freqDays').disabled=value;
    $('freqMonths').disabled=value;
    $('expiryTypeId').disabled=value;
	$('expiryNumOfDays').disabled=value;
	
	//Disable the org filters
	/*
    $('taskOrganizationFilter_0').disabled=value;
    $('taskOrganizationFilter_1').disabled=value;
    $('taskOrganizationFilter_2').disabled=value;
    $('taskOrganizationFilter_3').disabled=value;
    */
    displaySkillType();
}

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#objects-tasks_edit {
        width: 650px;
        left: -325px;
    }
    div#objects-tasks_edit table.formTable td{
        padding: 2px;
    }

    div#objects-tasks_edit input#numberOfWorkers {
        width: 30px;
    }
    
    div#objects-tasks_edit input#expiryNumOfDays {
        width: 50px;
    }
    div#objects-tasks_edit table#estTimeTable td{
        padding: 0px;
    }
    div#objects-tasks_edit select {
        width: 180px;
    }

    div#objects-tasks_edit select.frequency {
        width: 100px;
        margin-bottom: 3px;
    }

    div.popup table td.fieldComment{
        padding: 0px;
        font-size:9px;
        color: #777;
    }


    div#objects-tasks_edit label.filterLabel{
        margin-right:10px;
        padding-top:6px;
    }

    div#objects-tasks_edit label.filterLabel{
        margin-right:13px;
        padding-top:6px;
    }

    div#objects-tasks_edit .label{
        width: 100px;
    }


    div#objects-tasks_edit select.filterSelect{
        margin-left:2px;
    }

</style>

<!--  -->
<div class="popup" id="objects-tasks_edit">
<div class="popupHeader">
    <h2><span id="taskPopupTitle"></span></h2>
</div>

<div class="popupBody">
<form name="TaskForm" id="TaskForm" action="">

<table>

<%/*tr id="customizeTask">
    <td colspan="2">

        <table>
            <tr>
                <td>Customize: </td>
                <td>
                    <input type="checkbox" id="custom" onclick="makeCustom(this.checked)">
                </td>
                <td class="fieldComment">
                        Checking the box will detach task from its template (customize). Changes to the Template will not propagate to this task.
                        Warning: This procedure cannot be reversed
                </td>
            </tr>
        </table>
    </td>
</tr*/%>

<tr>
<td valign="top" width="300px">

<h3>Set Task Properties</h3>
<table>
<tr>
    <td><label>Description:</label></td>
    <td><input id="description" type="text">
    </td>
</tr>
<tr>
    <td>Est. Time:</td>
    <td>
        <table cellpadding="0" cellspacing="0" style="margin-left: 2px;">
            <tr>
                <td><a:ajax id="estTimeHours" name="iwm.spinner"/>&nbsp;</td>
                <td><a:ajax id="estTimeMinutes" name="iwm.spinner"/></td>
            </tr>
            <tr><td class="fieldComment">Hours</td><td class="fieldComment">Mins</td></tr>
        </table>
    </td>
</tr>
<tr>
    <td valign="top">Skill:</td>
    <td>
        <html:select styleClass="filterSelect" property="skillTypeId" styleId="skillTypeId" value="">
            <option value="">-- Select Skill --</option>
            <html:optionsCollection property="options"  name="SkillTypeRef"/>
        </html:select>
        <div class="fieldComment" id="taskGroupWarning">
            You may not change the skill while the task is in a group (group: <span id="taskGroupName"></span>).
            You can ungroup task in Edit Task Group page.
        </div>
    </td>
</tr>
<tr>
    <td valign="top">Skill Level:</td>
    <td>
        <html:select styleClass="filterSelect" property="skillLevelId" styleId="skillLevelId" value="">
            <option value="">-- Select Level --</option>
            <html:optionsCollection property="options"  name="SkillLevelRef"/>
        </html:select>
    </td>
</tr>
<!-- Hidden per #263 -->
<!-- 
<tr>
    <td>Worker Type:</td>
    <td>
        <html:select styleId="workerTypeId"  property="workerTypeId" value="">
            <html:option value="">- Select Type-</html:option>
            <html:optionsCollection property="options"  name="WorkerTypeRef"/>
        </html:select>
    </td>
</tr>
-->
<input class="select" id="workerTypeId" name="workerTypeId" type="hidden" value="1079">

<!-- Removed per #254 -->
<!--   
<tr >
    <td><bean:message key="numberOfWorkers"/></td>
    <td><input id="numberOfWorkers"type="text"></td>
</tr>
-->
<input id="numberOfWorkers" type="hidden" value="1" >

<tr>
    <td>Priority:</td>
    <td>
        <html:select styleClass="filterSelect" property="priorityId" styleId="priorityId" value="">
            <option value="">--- Select Proirity --</option>
            <html:optionsCollection property="options"  name="PriorityRef"/>
        </html:select>
    </td>
</tr>
<tr>
    <td>Task Type:</td>
    <td>
        <html:select styleClass="filterSelect" property="taskTypeId" styleId="taskTypeId" value="">
            <option value="">-- Select Task Type --</option>
            <html:optionsCollection property="options"  name="TaskTypeRef"/>
        </html:select>
    </td>
</tr>

<tr id="RoutineFrequencySetings">
    <td valign=top>Frequency:</td>
    <td>
        <div>
            <div style="margin-bottom: 5px;">
            <input type="radio" name="readingType" id="timeFreqRadio" checked >Time Based
            <input type="radio" name="readingType" id="meterFreqRadio">Meter Based
            </div>

            <div id="timeBasedOptions">
                <html:select styleClass="frequency" property="frequencyId" styleId="frequencyId" value="" >
                    <option value=""></option>  <!--do not add label/value for this option! you would need to clean this value on save-->
                    <html:optionsCollection property="options"  name="TaskFrequencyRef"/>
                    <option value="custom">Custom</option>
                </html:select>

                <div id="CustomTimeInterval">
                    <input size="4" id="freqDays" type="text">
                    <input size="4" id="freqMonths" type="text" style="display:none;">

                    <select style="width: 140px" id="timeIntervalSelect">
                        <option value="days">days between service</option>
                        <option value="months">months between service</option>
                    </select>

                </div>

            </div>
        </div>

        <div id="meterBasedOptions">

            <table cellpadding="0" cellspacing="0">
                <tr><td><input id="runHoursThreshInc" size="7" type="text">&nbsp;</td>
                    <td><input id="runHours" type="text" size="7" disabled="true">&nbsp;</td>
                    <td><input id="runHoursThresh" type="text" size="7">&nbsp;</td>
                    </tr>
                <tr>
                    <td valign="top" class="fieldComment">Meter<br>Interval</td>
                    <td valign="top" class="fieldComment">Curr<br>Reading</td>
                    <td valign="top" class="fieldComment">Next<br>Service</td>
                </tr>
            </table>

        </div>
    </td>
</tr>
</table>

</td>
<td valign="top">
<h3>Scheduling and Planning</h3>
<table>
    <%/*use filter tr>
        <td width="111">Organization:</td>
        <td>
        <html:select styleClass="filterSelect" property="organizationId" styleId="organizationId" value="">
            <option value="-1">Allow All Organizations</option>
            <html:optionsCollection property="options"  name="OrganizationRef"/>
        </html:select>
        </td>
    </tr*/%>
<!--  no more schedule responsibility. It's system until manually scheduled. 
    <tr>
        <td class="label"><bean:message key="scheduleBy"/>:</td>
        <td>
            <html:select styleClass="filterSelect" property="scheduleResponsibilityId" styleId="scheduleResponsibilityId" value="">
                <html:optionsCollection property="options"  name="ScheduleResponsibilityRef"/>
            </html:select>
        </td>
    </tr>
    -->
 <input id="scheduleResponsibilityId" type="hidden" value="1145" >
 
    <tr id="startDateTR">
        <td><bean:message key="startDate"/>:</td>
        <td>
            <a:ajax id="startDate"  type="iwm.calendar" name="iwm.calendar" />
        </td>
    </tr>

    <tr>
        <td><bean:message key="active"/>:</td>
        <td>
            <input type="checkbox" id="active" onclick="updateActive()">
        </td>
    </tr>

    <tr>
        <td><bean:message key="lastPlannedDate"/>:</td>
        <td>
            <a:ajax id="lastPlannedDate"  type="iwm.calendar" name="iwm.calendar" />
        </td>
    </tr>

<!--  No more org at the task level (in the UI). Tasks get their object's org. -->
<!-- <tr>
        <td colspan="2">
            <h3><bean:message key="organization"/></h3>
            <a:ajax  id="taskOrganizationFilter" type="iwm.filter" name="iwm.filter"/>
        </td>
    </tr>
-->

    <tr id="customizeTask">
        <td><bean:message key="custom"/>:</td>
        <td>
            <input type="checkbox" id="custom" onclick="return makeCustom(this.checked)">
        </td>
    </tr>
    
    <tr>
	    <td><label>Expiry Type:<span class="required">*</span></label></td>
	    <td>
	        <html:select styleClass="filterSelect" property="expiryTypeId" styleId="expiryTypeId" value="">
	            <option value="">-- Select Expiry Type --</option>
	            <html:optionsCollection property="options"  name="TaskExpiryTypeRef"/>
	        </html:select>
	    </td>
	</tr>
	<tr >
	    <td><label><bean:message key="expiryNumOfDays"/>:<span class="required">*</span></label></td>
	    <td><input id="expiryNumOfDays"type="text" value="0"></td>
	</tr>
</table>

</td>
</tr>


</table>



</form>
<input type="button" value="Save" class="button" onClick="theTaskEditPopup.save();">
<input type="button" value="Cancel" class="button" onClick="theTaskEditPopup.close();">

</div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="TaskForm" dynamicJavascript="true" staticJavascript="false"/>

