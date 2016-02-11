<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >
    var StencilEditPopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable, isWizard){
            this.superInit(popupId, formName, dataTable, isWizard);
            this.theData = new Object();
        },

        show: function(theId){
            if(!theId){
                $('stencilEditTitle').innerHTML = "Add Project Stencil";
                thePopupManager.showPopup('stencils_edit');
                theStencilLocatorFilter.populateChain(null);
                theStencilOrganizationFilter.populateChain(null);
                $('active').checked = false;
                this.theData.id = -1;
                showFrequency(false);                
            } else {
                $('stencilEditTitle').innerHTML = "Edit Project Stencil";
                var _this = this;
                ProjectStencils.getItem(function(response){_this.populate(response);}, theId);
            }
        },

        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("ProjectStencilForm"),item);
            if(item.active==1)
                $("active").checked=true;
            else
                $("active").checked=false;

            showFrequency(item.autoplanning==1);

            theStencilLocatorFilter.populateChain(item.locatorId);
            theStencilOrganizationFilter.populateChain(item.organizationId);
            thePopupManager.showPopup('stencils_edit');
        },

        save: function(){
            if(this.validate()){
                FormValuesUtil.getSubmittableFormValues($("ProjectStencilForm"),this.theData);
                var _this = this;
                if($("active").checked)
                    this.theData.active=1; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
                else
                    this.theData.active=0

                if($F("frequencyId")==-1) this.theData.frequencyId="";
                this.theData.organizationId=theStencilOrganizationFilter.currentSelectedId==null?'':theStencilOrganizationFilter.currentSelectedId;

                this.persist(this.theData);
                return true;
            }
            else {
                return false;
            }
        }
    });


    var theStencilLocatorFilter;
    var theStencilOrganizationFilter;
    callOnLoad(function(){
        theStencilEditPopup = new StencilEditPopup("stencils_edit", "ProjectStencilForm", theStencilsTable);
        theStencilLocatorFilter = new LocatorChain("theStencilLocatorFilter", onLocatorChange, false,false,1);
        theStencilOrganizationFilter = new OrganizationChain("theStencilOrganizationFilter", onOrgChange, false,false);
    });

    function onLocatorChange(locatorId){
        theStencilEditPopup.theData.locatorId=locatorId;
    }
    function onOrgChange(organizationId){
        theStencilEditPopup.theData.organizationId=organizationId;
    }

    function updateActive(){
        if($('active').checked){
            $('startDate').value=convertDateToString(new Date());
        }else
            $('startDate').value="";
    }

    function showFrequency(truefalse){
        truefalse=false; //always false till autoplanning is supported by agents
        if(truefalse)
            $("frequencyTR").style.display = "";
        else
            $("frequencyTR").style.display = "none";
    }

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#stencils_edit {
        width: 350px;
    }
    div#stencils_edit select {
        width: 165px;
    }
    div#stencils_edit label {
        width: 122px;
        display: block;
    }
    div#stencils_edit textarea#description {
        width: 165px;
    }
    div#stencils_edit input#name {
        width: 165px;
    }

    /*override for iwm.filter styles*/
div#stencils_edit .filterSelect{
    width:165;
    margin-left:2px;
    margin-top:2px;
    margin-bottom:2px;
}



</style>

<!--  -->
<div class="popup" id="stencils_edit">
    <div class="popupHeader">
        <div class="popupHeader"><h2><span id="stencilEditTitle">Edit Object</span></h2></div>
    </div>

    <div class="popupBody">

        <form action="" name="ProjectStencilForm" id="ProjectStencilForm">
            <table>
                <tr>
                    <td><label><bean:message key="name"/>:<span class="required">*</span></label></td>
                    <td><input id="name" name="name" type="text" maxlength="50"></td>
                </tr>
                <tr>
                    <td><label><bean:message key="description"/>:<span class="required">*</span></label></td>
                    <td><textarea id="description" name="description" type="text" cols=""></textarea></td>
                </tr>

                <tr>
                    <td><label>Project Type:</label></td>
                    <td>
                        <html:select property="projectTypeId" styleId="projectTypeId"  value="">
                            <html:option value="">- Select Type-</html:option>
                            <html:optionsCollection property="options"  name="ProjectTypeRef"/>
                        </html:select>
                    </td>
                </tr>                

                <tr>
                    <td><label><bean:message key="planning"/>:</label></td>
                    <td>
                        <html:select property="autoplanning" styleId="autoplanning" value="" onchange="showFrequency($(this).value==1);" >
                            <%//disabled per disc btw Mike,Chrus and Andrei. Autoplanning is not yet implemented by agent portion. Manual is what we have for now*/%>
                            <%/*html:option value="1">Auto</html:option*/%>
                            <html:option value="0">Manual</html:option>
                        </html:select>
                    </td>
                </tr>

                <tr id="frequencyTR">
                    <td><bean:message key="frequencyId"/></td>
                    <td >
                        <html:select styleClass="frequency" property="frequencyId" styleId="frequencyId" value="" >
                            <option value="-1"> --Select Frequency -- </option>
                            <html:optionsCollection property="options"  name="TaskFrequencyRef"/>
                        </html:select>
                    </td>
                </tr>

                <%/*tr>
                    <td><label><bean:message key="frequencyValue"/>:</label></td>
                    <td><input id="frequencyValue" name="frequencyValue" type="text"></td>
                </tr*/%>
                <tr>
                    <td><bean:message key="startDate"/>:</td>
                    <td>
                        <a:ajax id="startDate"  type="iwm.calendar" name="iwm.calendar" />
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="active"/>:</label></td>
                    <td><input type="checkbox" id="active" onclick="updateActive()" >
                    </td>
                </tr>
                <tr>
                    <td><bean:message key="lastPlannedDate"/>:</td>
                    <td>
                        <input id="lastPlannedDate" type="text" size="8" disabled="true">
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <h3><bean:message key="location"/></h3>
                        <a:ajax  id="theStencilLocatorFilter" type="iwm.filter" name="iwm.filter"/>
                        <h3><bean:message key="organization"/></h3>
                        <a:ajax  id="theStencilOrganizationFilter" type="iwm.filter" name="iwm.filter"/>
                    </td>
                </tr>
            </table>


        </form>
        <input type="button" value="Save" class="button" onClick="theStencilEditPopup.save();">
        <input type="button" value="Cancel" class="button" onClick="theStencilEditPopup.close();">
    </div>
</div>

<html:javascript formName="ProjectStencilForm" dynamicJavascript="true" staticJavascript="false"/>


