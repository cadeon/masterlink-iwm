<%@ page import="org.mlink.iwm.struts.action.WebConstants"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/TemplateTaskGroups.js"></script>

<script type="text/javascript" >
var TaskPropertiesModerator = Class.create();
TaskPropertiesModerator.prototype = {
    initialize: function() {
        this.freqMonthsRadio=$("freqMonthsRadio");  this.freqDaysRadio=$("freqDaysRadio");
        this.meterFreqRadio=$("meterFreqRadio");    this.timeFreqRadio=$("timeFreqRadio");
        this.taskTypeId=$("taskTypeId");            this.frequencyId=$("frequencyId");
        this.freqDays=$("freqDays"); this.freqMonths=$("freqMonths"); 
        this.runHoursThreshInc=$("runHoursThreshInc"); 
        this.nonRoutineRadio=$("nonRoutineRadio");
        
        this.runHoursThreshInc.onchange = this.runHoursThreshIncOnChange.bindAsEventListener(this);
        this.freqMonthsRadio.onclick = this.freqMonthsRadioOnClick.bindAsEventListener(this);
        this.freqDaysRadio.onclick = this.freqDaysRadioRadioOnClick.bindAsEventListener(this);
        this.meterFreqRadio.onclick = this.meterFreqRadioOnClick.bindAsEventListener(this);
        this.timeFreqRadio.onclick = this.timeFreqRadioOnClick.bindAsEventListener(this);
        this.nonRoutineRadio.onclick = this.nonRoutineRadioOnClick.bindAsEventListener(this);
        this.taskTypeId.onchange = this.taskTypeOnChange.bindAsEventListener(this);
        this.frequencyId.onchange = this.frequencyOnChange.bindAsEventListener(this);
        this.freqDays.onchange = this.freqDaysOnChange.bindAsEventListener(this);
        this.freqMonths.onchange = this.freqMonthsOnChange.bindAsEventListener(this);
        this.runHoursThreshInc.onchange = this.runHoursThreshIncOnChange.bindAsEventListener(this);
    },
    freqMonthsRadioOnClick: function(event) {
        this.freqMonths.disabled=false;
        this.freqDays.disabled=true;
    },
    freqDaysRadioRadioOnClick: function(event) {
        this.freqMonths.disabled=true;
        this.freqDays.disabled=false;
    },
    meterFreqRadioOnClick: function(event) {
        this.runHoursThreshInc.disabled=false;
        this.frequencyId.disabled=true;

        $('CustomTimeInterval').style.display='none';
        DWRUtil.setValue(this.frequencyId,"");
        DWRUtil.setValue(this.freqDays,"");
        DWRUtil.setValue(this.freqMonths,"");
    },
    timeFreqRadioOnClick: function(event) {
        this.runHoursThreshInc.disabled=true;
        this.frequencyId.disabled=false;   
        DWRUtil.setValue(this.runHoursThreshInc,"");
    },
    nonRoutineRadioOnClick: function(event){
    	this.runHoursThreshInc.disabled=true;
        this.frequencyId.disabled=true;
        DWRUtil.setValue(this.frequencyId,"");
        DWRUtil.setValue(this.runHoursThreshInc,"");
        DWRUtil.setValue(this.freqDays,"");
        DWRUtil.setValue(this.freqMonths,"");

        $('CustomTimeInterval').style.display='none';
    },
    taskTypeOnChange: function(event) {
        theTaskEditPopup.theTask.taskTypeId = DWRUtil.getValue(this.taskTypeId);

        if(theTaskEditPopup.theTask.taskTypeId!=<%=String.valueOf(WebConstants.TaskTypeIdRoutine)%>){
            this.nonRoutineRadio.checked=true;
            theTaskEditPopup.theTask.runHoursThreshInc=null;
            theTaskEditPopup.theTask.frequencyId=null;
        }
        
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
        DWRUtil.setValue(this.freqDays,"");
        DWRUtil.setValue(this.freqMonths,"");
    },
    populate: function(task) {
        this.clear(); 
        $('RoutineFrequencySetings').style.display='';
        
        if(task.runHoursThreshInc!=null)    {
            this.runHoursThreshInc.disabled=false;
            this.meterFreqRadio.checked=true;
            DWRUtil.setValue(this.runHoursThreshInc,task.runHoursThreshInc);
        }
        else if(task.freqDays!=null){
            this.freqDays.disabled=false;
            this.freqDaysRadio.checked=true;
            this.frequencyId.disabled=false;
            DWRUtil.setValue(this.frequencyId,"custom");
            DWRUtil.setValue(this.freqDays,task.freqDays);
            this.timeFreqRadio.checked=true;
            $('CustomTimeInterval').style.display='';
        }
        else if(task.freqMonths!=null){
            this.freqMonths.disabled=false;
            this.frequencyId.disabled=false;
            this.freqMonthsRadio.checked=true;
            //this.timeFreqRadio.checked=true;
            DWRUtil.setValue(this.frequencyId,"custom");
            DWRUtil.setValue(this.freqMonths,task.freqMonths);
            this.timeFreqRadio.checked=true;
            $('CustomTimeInterval').style.display='';
        }else if(task.frequencyId!=null){
            //this.frequencyId.disabled=false;
            DWRUtil.setValue(this.frequencyId,task.frequencyId);
            this.timeFreqRadio.checked=true;
            this.frequencyId.disabled=false;
            if(task.frequencyId=='custom'){
                $('CustomTimeInterval').style.display='';
                this.freqDays.disabled=false;
                this.freqDaysRadio.checked=true;
            }
        }else{
            this.freqMonthsRadio.checked=false;
            this.freqDaysRadio.checked=false;
                
            this.frequencyId.disabled=true;
            this.runHoursThreshInc.disabled=true;
            this.nonRoutineRadio.checked=true;            
        }
    }
}
var TaskEditPopup = Class.extend( AbstractCrudPopup, {
    initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        this.theTask;
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
        if(theId != null && theId != -1){
            $("taskPopupTitle").innerHTML= "Edit Task";
            _this = this;
            TemplateTasks.getItem(function(response){_this.populate(response);}, theId);
        } else {
            $("taskPopupTitle").innerHTML= "Add Task";
            this.theTask.id=-1;
            this.theTask.objectDefId=<%= request.getParameter("id") %>;
            this.theTask.expiryTypeId=3;
            this.theTask.expiryNumOfDays=180;
            this.populate(this.theTask);
        }
        thePopupManager.showPopup('templates-tasks_edit');
        $('description').focus();
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
        displaySkillType();
        taskPropertiesModerator.populate(this.theTask);
    },


    save: function(){
        if(this.validate()){
        	FormValuesUtil.getFormValues($("TaskForm"),this.theTask);
            if(this.theTask.taskTypeId==<%=String.valueOf(WebConstants.TaskTypeIdRoutine)%>){
                if(this.theTask.nonRoutineRadio==true){
                    return false;
                }
        	}

            if(this.theTask.frequencyId=='custom') this.theTask.frequencyId=null;
            this.theTask.estTime=HourMinuteConveter.toMinutes(DWRUtil.getValue("estTimeHours"),DWRUtil.getValue("estTimeMinutes"));
            var _this = this;
            //TemplateTasks.saveItem(function(message){_this.showCallBackMessage(message)}, this.theTask);
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
    theTaskEditPopup = new TaskEditPopup('templates-tasks_edit',"TaskForm", dataTable);
});

function displaySkillType(){
    var item= theTaskEditPopup.theTask;
    if(item && item.groupId!=null){
        $('skillTypeId').disabled=true;
        $('taskGroupWarning').style.display='block';
        TemplateTaskGroups.getItem(function(group){$('taskGroupName').innerHTML=group.description;}, item.groupId)
    }else{
        $('skillTypeId').disabled=false;
        $('taskGroupWarning').style.display='none';
    }
}



</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#templates-tasks_edit {
        width: 400px;
        left: -200px;
    }


    div#templates-tasks_edit input#numberOfWorkers {
        width: 50px;
    }
    div#templates-tasks_edit input#expiryNumOfDays {
        width: 50px;
    }

    div#templates-tasks_edit input#description {
        width: 200px;        
    }
    div#templates-tasks_edit select {
        width: 200px;
    }

    div#templates-tasks_edit td.label {
        width: 100px;
    }

    div#templates-tasks_edit select.frequency {
        width: 100px;
    }
</style>

<!--  -->
<div class="popup" id="templates-tasks_edit">
<div class="popupHeader">
    <h2><span id="taskPopupTitle"></span></h2>
</div>

<div class="popupBody">
<form name="TaskForm" id="TaskForm" action="">

<table>
<tr>
    <td><label>Description:<span class="required">*</span></label></td>
    <td><input id="description" type="text"></td>
</tr>
<tr>
    <td><label>Est. Time:</label></td>
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
    <td valign="top"><label>Skill:<span class="required">*</span></label></td>
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
    <td valign="top"><label>Skill Level:<span class="required">*</span></label></td>
    <td>
        <html:select styleClass="filterSelect" property="skillLevelId" styleId="skillLevelId" value="">
            <option value="">-- Select Level --</option>
            <html:optionsCollection property="options"  name="SkillLevelRef"/>
        </html:select>
    </td>
</tr>
<!-- Hiding per #263 -->
<!-- 
<tr>
    <td><label>Worker Type:<span class="required">*</span></label></td>
    <td>
        <html:select styleId="workerTypeId"  property="workerTypeId" value="">
            <html:option value="">- Select Type-</html:option>
            <html:optionsCollection property="options"  name="WorkerTypeRef"/>
        </html:select>
    </td>
</tr>
-->
<input class="select" id="workerTypeId" name="workerTypeId" type="hidden" value="1079">

<!-- Remove per #254 -->
<!--   
<tr >
    <td><label><bean:message key="numberOfWorkers"/>:<span class="required">*</span></label></td>
    <td><input id="numberOfWorkers"type="text" ></td>
</tr>
-->
<input id="numberOfWorkers" type="hidden" value="1" >
<tr>
    <td><label>Priority:<span class="required">*</span></label></td>
    <td>
        <html:select styleClass="filterSelect" property="priorityId" styleId="priorityId" value="">
            <option value="">--- Select Proirity --</option>
            <html:optionsCollection property="options"  name="PriorityRef"/>
        </html:select>
    </td>
</tr>
<tr>
    <td><label>Task Type:<span class="required">*</span></label></td>
    <td>
        <html:select styleClass="filterSelect" property="taskTypeId" styleId="taskTypeId" value="">
            <option value="">-- Select Task Type --</option>
            <html:optionsCollection property="options"  name="TaskTypeRef"/>
        </html:select>
    </td>
</tr>

<tr id="RoutineFrequencySetings" >
    <td><label>Frequency:</label></td>
    <td>
        <table>
            <tr>
                <td><input type="radio" name="readingType" id="nonRoutineRadio" checked >Non-Routine</td>
            </tr>
            <tr>
                <td><input type="radio" name="readingType" id="timeFreqRadio">Time Interval</td>
                <td >
                    <html:select styleClass="frequency" property="frequencyId" styleId="frequencyId" value="" >
                        <option></option>  <!--do not add label/value for this option! you need to clean this value on save-->
                        <html:optionsCollection property="options"  name="TaskFrequencyRef"/>
                        <option value="custom">Custom</option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td><input type="radio" name="readingType" id="meterFreqRadio">Meter Interval</td>
                <td >
                    <input id="runHoursThreshInc" size="12" type="text">
                </td>
            </tr>
        </table>
    </td>
</tr>

<tr id="CustomTimeInterval" >
    <td><label>Custom Interval:</label></td>
    <td>
        <table>
            <tr>
                <td>
                    <input type="radio" name="customFrequencyType" id="freqDaysRadio" checked><bean:message key="freqDays"/>
                </td>
                <td>
                    <input size="4" id="freqDays" type="text">
                </td>
            </tr>
            <tr>
                <td>
                    <input type="radio" name="customFrequencyType" id="freqMonthsRadio"><bean:message key="freqMonths"/>
                </td>
                <td>
                    <input size="4" id="freqMonths" type="text">
                </td>
            </tr>
        </table>
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

<input type="button" value="Save" class="button" onClick="theTaskEditPopup.save();">
<input type="button" value="Cancel" class="button" onClick="theTaskEditPopup.close();">

</form>
</div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="TaskForm" dynamicJavascript="true" staticJavascript="false"/>

