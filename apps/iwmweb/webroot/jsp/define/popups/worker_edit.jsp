<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" >

var WorkerEditPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
        this.workerLocatorFilter = new LocatorChain("workerLocatorFilter", function(id){return}, false);
        this.workerOrganizationFilter = new OrganizationChain("workerOrganizationFilter", function(id){return}, false);


    },

//custom. you can use base class clean most of the time.
    clean: function(){
        Form.reset($(this.formName));
        $("username").disabled=true;
        $("EDIT_WORKER_NAME").innerHTML='';
    },

//abstract in base class
    show: function(theId, isWizard){

        this.theWorker = new Object();
        if(isWizard){
            this.isWizard = isWizard;
            $("workerEditTitle").innerHTML =  "Add Worker: <span id='EDIT_WORKER_NAME'></span>"            
        } else {
            this.isWizard = false;
            $("workerEditTitle").innerHTML =  "Edit Worker: <span id='EDIT_WORKER_NAME'></span>"
        }

        //edit
        if(theId != null && theId != -1){
            this.theWorker.personId=theId;
            $("username").disabled=true;
            var _this = this;
            Workers.getItem(function(response){_this.populate(response);},theId);
            $("saveButton").value = "Save";
        }
        //add
        else {
            $('active').checked  = true;
            this.theWorker.personId=0;
            $("username").disabled=false;
            this.workerLocatorFilter.populateChain(null);
            this.workerOrganizationFilter.populateChain(organizationFilter.currentSelectedId);    // use value from WorkerList page dropdown
            $("saveButton").value = "Next";
            thePopupManager.showPopup('worker_edit');
            $('name').focus();

        }
    },

//abstract in base class
    populate: function(item){
        //set DWR object
        this.theWorker = item;
        FormValuesUtil.setFormValues($("PersonForm"),this.theWorker);
        if(this.theWorker.active==1) $("active").checked=true; else $("active").checked=false;

        //set the filters
        this.workerLocatorFilter.populateChain(this.theWorker.locatorId);
        this.workerOrganizationFilter.populateChain(this.theWorker.organizationId);
        $("EDIT_WORKER_NAME").innerHTML=this.theWorker.name;
        thePopupManager.showPopup('worker_edit');
        $('name').focus();

    },
    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theWorkerSkillsPopup.show(null, null, true);
        }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getFormValues($("PersonForm"),this.theWorker);
            if($("active").checked)
                this.theWorker.active="1"; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
            else
                this.theWorker.active="0";

            //this.theWorker.organizationId = this.workerOrganizationFilter.currentSelectedId;
            //Loc filter to set parentId and locatorId for Organization. If selected id is null, substitute if with empty string. null value will never get to database. Empty string serves as NulAlias
            this.theWorker.organizationId = this.workerOrganizationFilter.currentSelectedId;
            this.theWorker.locatorId = this.workerLocatorFilter.currentSelectedId==null?'':this.workerLocatorFilter.currentSelectedId;
            var _this = this;
            if(this.isWizard){
                WorkerCreateWizard.saveWorkerInfo(function(message){_this.wizardCallback(message)}, this.theWorker);
            }  else {
                this.persist(this.theWorker);
            }
            return true;
        }
        else {
            return false;
        }

    }
});

callOnLoad(function(){
    theWorkerEditPopup = new WorkerEditPopup("worker_edit", "PersonForm", theWorkersTable);
});

function resetPassword(){
    if(confirm('Are you sure you want to reset the user password?\n The user will have to update the password on next login')){
        Users.resetPassword(theWorkerEditPopup.theWorker.personId);
    }
}

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div.popup#worker_edit{
        width: 330px;
        left: -165px;
    }

    div.popup#worker_edit input.textField {
        width: 185px;
    }

    div.popup#worker_edit select {
        width: 185px;
    }

    label {
        width: 90px;
        display: block;
    }
</style>


<div class="popup" id="worker_edit">
    <div class="popupHeader">
        <h2 id="workerEditTitle">Edit Worker: <span id="EDIT_WORKER_NAME"></span></h2>
    </div>

    <div class="popupBody">
        <form action="" id="PersonForm" name="PersonForm">
            <h3><bean:message key="properties"/></h3>
            <table >
                <tr>
                    <td><label><bean:message key="name"/>:<span class="required">*</span></label></td>
                    <td><input class="textField" id="name" name="name" type="text"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="username"/>:<span class="required">*</span></label></td>
                    <td><input id="username" name="username" class="textField"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="email"/>:</label></td>
                    <td><input class="textField" id="email" name="email" type="text"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="phone"/>:</label></td>
                    <td><input class="textField" id="phone" name="phone" type="text"/></td>
                </tr>
                <!-- Hidden per #263. Hidden input below adds the default 'internal' - 1079. -->
               <!--   <tr>
                    <td><label><bean:message key="workerType"/>:<span class="required">*</span></label></td>
                    <td> 
                        <html:select property="type" styleId="type" value="">
                            <html:option value="">- Select Type-</html:option>
                            <html:optionsCollection property="options"  name="WorkerTypeRef"/>
                        </html:select>      
                    </td>
                </tr>
                -->
                <input class="select" id="type" name="type" type="hidden" value="1079">
                <!-- Also hiding Title / Rank  -->
                <!--  
                <tr>
                    <td><label><bean:message key="title"/>:</label></td>
                    <td><input class="textField" id="title" name="title" type="text"/></td>
                </tr>
				-->
				<input class="textField" id="title" name="title" type="hidden"/>
				
                <tr>
                    <td><label><bean:message key="billingRate"/>:</label></td>
                    <td><input type="text" class="textField" id="billingRate" name="billingRate" style="width: 50px;">/hr</td>
                </tr>
                <tr>
                    <td><label><bean:message key="active"/>:</label></td>
                    <td>
                        <input type="checkbox" id="active" >
                    </td>
                </tr>
                <%if(request.isUserInRole("RstPass")){%>
                <tr>
                    <td><label><bean:message key="resetPassword"/></label></td>
                    <td>
                        <input type="button" class="button" value="Reset" onclick="resetPassword();"/>
                    </td>
                </tr>
                <%}%>
            </table>

            <h3><bean:message key="locator"/></h3>
            <a:ajax  id="workerLocatorFilter" type="iwm.filter" name="iwm.filter"/>
            <h3>Organization Hierarchy</h3>
            <a:ajax  id="workerOrganizationFilter" type="iwm.filter" name="iwm.filter"/>

            <input id="saveButton" type="button" class="button" value="Save" onclick="theWorkerEditPopup.save()">
            <input type="button" class="button" value="Cancel" onclick="theWorkerEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="PersonForm" dynamicJavascript="true" staticJavascript="false"/>
