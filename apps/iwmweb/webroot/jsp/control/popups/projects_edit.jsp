<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

var ProjectEditPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

//custom. you can use base class clean most of the time.
    clean: function(){
        Form.reset($(this.formName));
        $("EDIT_PROJECT_NAME").innerHTML='';
    },

//abstract in base class
    show: function(theId){
        this.theProject = new Object();
        //edit
        if(theId != null && theId != -1){
            this.theProject.id=theId;
            var _this = this;
            Projects.getItem(function(response){_this.populate(response);},theId);

            //if history is selected
            if($('activeStatusFilter') && $('activeStatusFilter').value == 'History'){
                this.disableFields(true);
            } else {
                this.disableFields(false);
            }

        }
        //add
        else {
            this.theProject.id=-1;
            $("EDIT_PROJECT_NAME").innerHTML="Create Project";
            theProjectOrganizationFilter.populateChain(null);
            thePopupManager.showPopup('projects_edit');
            $('createdBy').value='<%=request.getUserPrincipal().getName()%>';
            $('createdDate').value=convertDateToString(new Date());
        }
    },

    disableFields: function(isDisabled){
        $('name').disabled = isDisabled;
        $('description').disabled = isDisabled;
        $('projectTypeId').disabled = isDisabled;
    },

//abstract in base class
    populate: function(item){
        //set DWR object
        this.theProject=item;
        FormValuesUtil.setFormValues($("ProjectForm"),item);
        $("EDIT_PROJECT_NAME").innerHTML="Edit " + item.name;
        theProjectOrganizationFilter.populateChain(item.organizationId);
        thePopupManager.showPopup('projects_edit');
    },
    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theProjectSkillsPopup.show(null, null, true);
        }
    },

    save: function(){
        if(this.validate()){
            FormValuesUtil.getSubmittableFormValues($("ProjectForm"),this.theProject);
            this.theProject.organizationId=theProjectOrganizationFilter.currentSelectedId==null?'':theProjectOrganizationFilter.currentSelectedId;
            if($F("parentId")=="-1") this.theProject.parentId=""; //parentId is not required. DWRUtils getValue returns label for options with empty values. We do need to iverride this manually. Other dropdowns are required thus this problem is not applicable for them.
            this.persist(this.theProject);
            return true;
        }
        else {
            return false;
        }

    }
});

function onOrgChange(organizationId){
    theProjectEditPopup.theProject.organizationId=organizationId;
}
var theProjectOrganizationFilter;
callOnLoad(function(){
    theProjectEditPopup = new ProjectEditPopup("projects_edit", "ProjectForm", projectsTable);
    theProjectOrganizationFilter = new OrganizationChain("theProjectOrganizationFilter", onOrgChange, false,false);
});


</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div.popup#projects_edit{
        width: 330px;
        left: -165px;
    }

    div.popup#projects_edit input.textField {
        width: 185px;
    }

    div.popup#projects_edit select {
        width: 185px;
    }

    label {
        width: 90px;
        display: block;
    }
    div#projects_edit textarea {
        width: 185px;
        height: 35px;
    }

</style>


<div class="popup" id="projects_edit">
    <div class="popupHeader">
        <h2><span id="EDIT_PROJECT_NAME"></span></h2>
    </div>

    <div class="popupBody">
        <form action="" id="ProjectForm" name="ProjectForm">
            <table >
                <tr>
                    <td><label><bean:message key="name"/>:</label></td>
                    <td><input class="textField" id="name" name="name" type="text"/></td>
                </tr>
                <tr>
                    <td><label>Description:</label></td>
                    <td><textarea id="description" name="description" class="textField"></textarea>
                </tr>

                <tr>
                    <td><label><bean:message key="projectTypeId"/>:</label></td>
                    <td>
                        <html:select property="projectTypeId" styleId="projectTypeId"  value="">
                            <html:option value="">- Select Type-</html:option>
                            <html:optionsCollection property="options"  name="ProjectTypeRef"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <h3><bean:message key="organization"/></h3>
                        <a:ajax  id="theProjectOrganizationFilter" type="iwm.filter" name="iwm.filter"/>
                    </td>
                </tr>
                <tr>
                    <td><label>Parent:</label></td>
                    <td>
                        <html:select property="parentId" styleId="parentId"  value="">
                            <html:option value="-1">- Select Parent-</html:option>
                            <html:optionsCollection property="options"  name="ProjectRef"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td><label><bean:message key="createdDate"/>:</label></td>
                    <td>
                        <input id="createdDate" name="createdDate" class="textField" disabled="true"/>
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="startedDate"/>:</label></td>
                    <td>
                        <input id="startedDate" name="startedDate" class="textField" disabled="true"/>
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="completedDate"/>:</label></td>
                    <td>
                        <input id="completedDate" name="completedDate" class="textField" disabled="true"/>
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="state"/>:</label></td>
                    <td>
                        <input id="status" name="status" class="textField" disabled="true"/>
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="createdBy"/>:</label></td>
                    <td>
                        <input id="createdBy" name="createdBy" class="textField" disabled="true"/>
                    </td>
                </tr>

            </table>

            <input id="saveButton" type="button" class="button" value="Save" onclick="theProjectEditPopup.save()">
            <input type="button" class="button" value="Cancel" onclick="theProjectEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="ProjectForm" dynamicJavascript="true" staticJavascript="false"/>
