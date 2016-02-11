<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

var ShiftRefEditPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

//custom. you can use base class clean most of the time.
    clean: function(){
        Form.reset($(this.formName));
        $("code").disabled=true;
        $("SHIFT_REF_NAME").innerHTML='';
    },

//abstract in base class
    show: function(theId, isAdd){

        this.theShiftRef = new Object();
        if(isAdd){
            this.isAdd = isAdd;
            $("shiftRefEditTitle").innerHTML =  "Add ShiftRef: <span id='SHIFT_REF_NAME'></span>"            
        } else {
            this.isAdd = false;
            $("shiftRefEditTitle").innerHTML =  "Edit ShiftRef: <span id='SHIFT_REF_NAME'></span>"
        }

        //edit
        if(theId != null && theId != -1){
            this.theShiftRef.id=theId;
            $('code').disabled=true;
            $('description').disabled=true;
            var _this = this;
            ShiftRefs.getItem(function(response){_this.populate(response);},theId);
        }
        //add
        else {
             this.theShiftRef.id=0;
            $("code").disabled=false;
            $('description').disabled=false;
            thePopupManager.showPopup('shiftRef_edit');
            $('code').focus();

        }
        $("saveButton").value = "Save";
    },

//abstract in base class
    populate: function(item){
        //set DWR object
        this.theShiftRef = item;
        FormValuesUtil.setFormValues($("ShiftRefForm"),this.theShiftRef);

        var hourMinute = HourMinuteConveter.splitMinutes(item.shiftStart);
        $('shiftStart').value=hourMinute.hours + ':' + hourMinute.minutes;
        hourMinute = HourMinuteConveter.splitMinutes(item.shiftEnd);
        $('shiftEnd').value=hourMinute.hours + ':' + hourMinute.minutes;
        hourMinute = HourMinuteConveter.splitMinutes(item.time);
        $('time').value=hourMinute.hours + ':' + hourMinute.minutes;
       
        $("SHIFT_REF_NAME").innerHTML=this.theShiftRef.code;
        thePopupManager.showPopup('shiftRef_edit');
        $('shiftStart').focus();

    },
    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theShiftRefSkillsPopup.show(null, null, true);
        }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("ShiftRefForm"),this.theShiftRef);

            var shiftStartResult = this.theShiftRef.shiftStart.split(":");
            this.theShiftRef.shiftStart = HourMinuteConveter.toMinutes(shiftStartResult[0], shiftStartResult[1]);
            var shiftEndResult = this.theShiftRef.shiftEnd.split(":");
            this.theShiftRef.shiftEnd = HourMinuteConveter.toMinutes(shiftEndResult[0], shiftEndResult[1]);
            var timeResult = this.theShiftRef.time.split(":");
            this.theShiftRef.time = HourMinuteConveter.toMinutes(timeResult[0], timeResult[1]);
            
            var _this = this;
            this.persist(this.theShiftRef);
            return true;
        }
        else {
            return false;
        }

    }
});

callOnLoad(function(){
    theShiftRefEditPopup = new ShiftRefEditPopup("shiftRef_edit", "ShiftRefForm", theShiftRefsTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div.popup#shiftRef_edit{
        width: 330px;
        left: -165px;
    }

    div.popup#shiftRef_edit input.textField {
        width: 185px;
    }

    div.popup#shiftRef_edit select {
        width: 185px;
    }

    label {
        width: 90px;
        display: block;
    }
</style>


<div class="popup" id="shiftRef_edit">
    <div class="popupHeader">
        <h2 id="shiftRefEditTitle">Edit ShiftRef: <span id="SHIFT_REF_NAME"></span></h2>
    </div>

    <div class="popupBody">
        <form action="" id="ShiftRefForm" name="ShiftRefForm">
            <h3><bean:message key="properties"/></h3>
            <table >
                <tr>
                    <td><label><bean:message key="code"/>:<span class="required">*</span></label></td>
                    <td><input id="code" name="code" class="textField"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="description"/>:<span class="required">*</span></label></td>
                    <td><textarea id="description" name="description" onkeyup="maxLength(100);" rows=4 cols=26></textarea></td>
                </tr>
                <tr>
                    <td><label><bean:message key="shiftStart"/>:<span class="required">*</span></label></td>
                    <td><input class="textField" id="shiftStart" name="shiftStart" type="text"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="shiftEnd"/>:<span class="required">*</span></label></td>
                    <td><input class="textField" id="shiftEnd" name="shiftEnd" type="text"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="time"/>:<span class="required">*</span></label></td>
                    <td><input class="textField" id="time" name="time" type="text"/></td>
                </tr>
            </table>

            <input id="saveButton" type="button" class="button" value="Save" onclick="theShiftRefEditPopup.save()">
            <input type="button" class="button" value="Cancel" onclick="theShiftRefEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="ShiftRefForm" dynamicJavascript="true" staticJavascript="false"/>
