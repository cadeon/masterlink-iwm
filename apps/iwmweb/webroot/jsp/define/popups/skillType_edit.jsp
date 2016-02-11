<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

var SkillTypeEditPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

//custom. you can use base class clean most of the time.
    clean: function(){
        Form.reset($(this.formName));
        $("code").disabled=true;
        $("EDIT_SKILL_TYPE").innerHTML='';
    },

//abstract in base class
    show: function(theId, isAdd){

        this.theSkillType = new Object();
        if(isAdd){
            this.isAdd = isAdd;
            $("skillTypeEditTitle").innerHTML =  "Add SkillType: <span id='EDIT_SKILL_TYPE'></span>"            
        } else {
            this.isAdd = false;
            $("skillTypeEditTitle").innerHTML =  "Edit SkillType: <span id='EDIT_SKILL_TYPE'></span>"
        }

        //edit
        if(theId != null && theId != -1){
            this.theSkillType.id=theId;
            $('code').disabled=true;
            $('description').disabled=true;
            var _this = this;
            SkillTypes.getItem(function(response){_this.populate(response);},theId);
        }
        //add
        else {
             this.theSkillType.id=0;
            $("code").disabled=false;
            $('description').disabled=false;
            thePopupManager.showPopup('skillType_edit');
            $('code').focus();

        }
        $("saveButton").value = "Save";
    },

//abstract in base class
    populate: function(item){
        //set DWR object
        this.theSkillType = item;
        FormValuesUtil.setFormValues($("SkillTypeForm"),this.theSkillType);
       
        $("EDIT_SKILL_TYPE").innerHTML=this.theSkillType.code;
        thePopupManager.showPopup('skillType_edit');
        $('value').focus();

    },
    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theSkillTypeSkillsPopup.show(null, null, true);
        }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("SkillTypeForm"),this.theSkillType);
            
            var _this = this;
            this.persist(this.theSkillType);
            return true;
        }
        else {
            return false;
        }

    }
});

callOnLoad(function(){
    theSkillTypeEditPopup = new SkillTypeEditPopup("skillType_edit", "SkillTypeForm", theSkillTypesDataTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div.popup#skillType_edit{
        width: 330px;
        left: -165px;
    }

    div.popup#skillType_edit input.textField {
        width: 185px;
    }

    div.popup#skillType_edit select {
        width: 185px;
    }

    label {
        width: 90px;
        display: block;
    }
</style>


<div class="popup" id="skillType_edit">
    <div class="popupHeader">
        <h2 id="skillTypeEditTitle">Edit SkillType: <span id="EDIT_SKILL_TYPE"></span></h2>
    </div>

    <div class="popupBody">
        <form action="" id="SkillTypeForm" name="SkillTypeForm">
            <h3><bean:message key="properties"/></h3>
            <table >
                <tr>
                    <td><label><bean:message key="code"/>:<span class="required">*</span></label></td>
                    <td><input id="code" name="code" class="textField"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="description"/>:</label></td>
                    <td><textarea id="description" name="description" onkeyup="maxLength(50);" rows=2 cols=28></textarea></td>
                </tr>
            </table>

            <input id="saveButton" type="button" class="button" value="Save" onclick="theSkillTypeEditPopup.save()">
            <input type="button" class="button" value="Cancel" onclick="theSkillTypeEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated code (Validator works with names of the form elements)
*/%>
<html:javascript formName="SkillTypeForm" dynamicJavascript="true" staticJavascript="false"/>
