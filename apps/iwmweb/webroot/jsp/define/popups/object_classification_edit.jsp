<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" >
var ParentObjectClassificationFilter;
var ObjectClassificationEditPopup = Class.extend( AbstractCrudPopup, {
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },
    show: function(ObjectClassificationId){ 
        this.theObjectClassification = new Object();
        if(ObjectClassificationId) {
            $("popupTitle").innerHTML= "Edit ObjectClassification";
            _this = this;
            ObjectClassifications.getItem(function(response){_this.populate(response);}, ObjectClassificationId);
        } else {
            //if no ObjectClassification id is passed in that means it is an add. make a blank form.
            $("popupTitle").innerHTML= "Add User Defined Object Classification";
            this.theObjectClassification.ObjectClassificationId=-1;
            ParentObjectClassificationFilter.reset();
            ParentObjectClassificationFilter.isModified=true; // the parent filter will be refreshed on save
            Form.reset("ObjectClassificationForm");
            SessionUtil.getCurrentClass({
                callback:function(curObjectClassificationId){
                    ParentObjectClassificationFilter.populateChain(curObjectClassificationId);
                    theObjectClassificationEditPopup.theObjectClassification.parentId=curObjectClassificationId;
                 
                },
                async:false});
            Templates.getUCCode(function(response){document.getElementById('code').value=response;});
            $('code').disabled=true;
            thePopupManager.showPopup('ObjectClassifications_edit');
            $('description').focus();
        }
    },

    populate: function(item){
        this.theObjectClassification = item;
        DWRUtil.setValues(this.theObjectClassification);
        ParentObjectClassificationFilter.populateChain(this.theObjectClassification.parentId);
        thePopupManager.showPopup('ObjectClassifications_edit');
        $('description').focus();
    },

        /* an overide to enable optional parent filter update*/
    showCallBackMessage: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            this.dataTable.update();
            if(ParentObjectClassificationFilter.isModified) classFilter.refresh();
       }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("ObjectClassificationForm"),this.theObjectClassification);
            if(this.theObjectClassification.parentId==null) this.theObjectClassification.parentId=''; //must be empty not null, nulls will be ignored by Middle tier conversions, see CopyUtils.NullAliasValues
            this.persist(this.theObjectClassification);
            return true;
        }
        else {
            return false;
        }
    }
});


callOnLoad(function(){
    ParentObjectClassificationFilter = new ObjectClassChain("parentObjectClassificationFilter", onChange, false,false,4);
    theObjectClassificationEditPopup = new ObjectClassificationEditPopup("ObjectClassifications_edit", "ObjectClassificationForm", theTemplateDataTable);
    
});

function onChange(parentId){
    theObjectClassificationEditPopup.theObjectClassification.parentId=parentId;
    
}

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#ObjectClassifications_edit {
        width: 350px;
        left: -175px;
    }
    div#ObjectClassifications_edit label {
        width: 87px;
        display: block;
    }
    div#ObjectClassifications_edit textarea#emergencyContact {
        width: 210px;
        height: 75px;
    }
</style>

<!--  -->
<div class="popup" id="ObjectClassifications_edit">
    <div class="popupHeader">
        <h2><span id="popupTitle">Edit ObjectClassification</span></h2>
    </div>

    <div class="popupBody">

        <form id="ObjectClassificationForm" action="" name="ObjectClassificationForm">
            <a:ajax  id="parentObjectClassificationFilter" type="iwm.filter" name="iwm.filter"/>
            <table >
                <tr>
                    <td><label><bean:message key="description"/>:<span class="required">*</span></label></td>
                    <td><input id="description" name="description" type="text"/></td>
                </tr>
                
                
                <tr>
                    <td><label><bean:message key="abbr"/>:<span class="required">*</span></label></td>
                    <td><input id="abbr" name="abbr" type="text"/></td>
                </tr>
                
				<tr>
                    <td><label><bean:message key="code"/>:<span class="required">*</span></label></td>
                    <td><input id="code" name="code" type="text"/></td>
                </tr>
                
            </table>
            <input type="button" class="button" value="Save" onclick="theObjectClassificationEditPopup.save()"/>
            <input type="button" class="button" value="Cancel" onclick="theObjectClassificationEditPopup.close();"/>
        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="ObjectClassificationForm" dynamicJavascript="true" staticJavascript="false"/>