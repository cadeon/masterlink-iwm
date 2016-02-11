<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

var SystemPropEditPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

//custom. you can use base class clean most of the time.
    clean: function(){
        Form.reset($(this.formName));
        $("property").disabled=true;
        $("EDIT_SYSTEM_PROP_NAME").innerHTML='';
    },

//abstract in base class
    show: function(theId, isAdd){

        this.theSystemProp = new Object();
        if(isAdd){
            this.isAdd = isAdd;
            $("systemPropEditTitle").innerHTML =  "Add SystemProp: <span id='EDIT_SYSTEM_PROP_NAME'></span>"            
        } else {
            this.isAdd = false;
            $("systemPropEditTitle").innerHTML =  "Edit SystemProp: <span id='EDIT_SYSTEM_PROP_NAME'></span>"
        }

        //edit
        if(theId != null && theId != -1){
            this.theSystemProp.id=theId;
            $('property').disabled=true;
            $('description').disabled=true;
            var _this = this;
            SystemProps.getItem(function(response){_this.populate(response);},theId);
        }
        //add
        else {
             this.theSystemProp.id=0;
            $("property").disabled=false;
            $('description').disabled=false;
            thePopupManager.showPopup('systemProp_edit');
            $('property').focus();

        }
        $("saveButton").value = "Save";
    },

//abstract in base class
    populate: function(item){
        //set DWR object
        this.theSystemProp = item;
        FormValuesUtil.setFormValues($("SystemPropForm"),this.theSystemProp);
       
        $("EDIT_SYSTEM_PROP_NAME").innerHTML=this.theSystemProp.property;
        thePopupManager.showPopup('systemProp_edit');
        $('value').focus();

    },
    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theSystemPropSkillsPopup.show(null, null, true);
        }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("SystemPropForm"),this.theSystemProp);
            
            var _this = this;
            this.persist(this.theSystemProp);
            return true;
        }
        else {
            return false;
        }

    }
});

callOnLoad(function(){
    theSystemPropEditPopup = new SystemPropEditPopup("systemProp_edit", "SystemPropForm", theSystemPropsTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div.popup#systemProp_edit{
        width: 330px;
        left: -165px;
    }

    div.popup#systemProp_edit input.textField {
        width: 185px;
    }

    div.popup#systemProp_edit select {
        width: 185px;
    }

    label {
        width: 90px;
        display: block;
    }
</style>


<div class="popup" id="systemProp_edit">
    <div class="popupHeader">
        <h2 id="systemPropEditTitle">Edit SystemProp: <span id="EDIT_SYSTEM_PROP_NAME"></span></h2>
    </div>

    <div class="popupBody">
        <form action="" id="SystemPropForm" name="SystemPropForm">
            <h3><bean:message key="properties"/></h3>
            <table >
                <tr>
                    <td><label><bean:message key="property"/>:<span class="required">*</span></label></td>
                    <td><input id="property" name="property" class="textField"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="description"/>:</label></td>
                    <td><textarea id="description" name="description" onkeyup="maxLength(100);" rows=4 cols=28></textarea></td>
                </tr>
                <tr>
                    <td><label><bean:message key="value"/>:<span class="required">*</span></label></td>
                    <td><input class="textField" id="value" name="value" type="text"/></td>
                </tr>
            </table>

            <input id="saveButton" type="button" class="button" value="Save" onclick="theSystemPropEditPopup.save()">
            <input type="button" class="button" value="Cancel" onclick="theSystemPropEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="SystemPropForm" dynamicJavascript="true" staticJavascript="false"/>
