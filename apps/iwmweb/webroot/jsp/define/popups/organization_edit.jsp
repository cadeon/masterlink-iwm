<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

    var OrganizationEditPopup = Class.extend( AbstractCrudPopup, {

        //abstract in base class
        initialize: function(popupId, formName, dataTable){

            this.superInit(popupId, formName, dataTable);
            this.theOrganization = new Object();
            var _this = this;
            this.orgLocatorFilter =
                    new LocatorChain("orgLocatorFilter", function(id){return}, false);
            this.orgOrganizationFilter =
                    new OrganizationChain("orgOrganizationFilter", onChange, false,false,3);
        },


        //abstract in base class
        show: function(theId){
            //edit
            if(theId != null && theId != -1){
                $('EDIT_ORG_NAME').innerHTML = "Edit Organization";
                _this = this;
                Organizations.getItem(function(response){_this.populate(response);},theId);
            }
            //add
            else {
                $('EDIT_ORG_NAME').innerHTML = "Add Organization";
                this.theOrganization.id=-1;
                this.orgLocatorFilter.populateChain(null);
                this.orgOrganizationFilter.populateChain(organizationFilter.currentSelectedId);    // use value from OrganizationList page dropdown
                thePopupManager.showPopup('organization_edit');
                $('name').focus();
            }
        },


        //abstract in base class
        populate: function(item){
            //set DWR object
            this.theOrganization = item;
            //populate the form

            FormValuesUtil.setFormValues($("OrganizationForm"),this.theOrganization);
            //set the filters
            this.orgLocatorFilter.populateChain(this.theOrganization.locatorId);
            this.orgOrganizationFilter.populateChain(this.theOrganization.parentId);
            thePopupManager.showPopup('organization_edit');
            $('name').focus();

        },

        //base class. override if needed
        showCallBackMessage: function(message){
            if(message && message.length > 0) {
                alert(message);
            } else {
                this.close();
                //update Organization list and Org filter if needed
                this.dataTable.update();
                if(this.orgOrganizationFilter.isModified==true) {
                    organizationFilter.refresh();     //organizationFilter is in organizationList.jsp
                }
                
            	if(this.theOrganization.parentId=='') {
            		organizationFilter.refresh();
            	}
           }
        },

        //abstract in base class
        save: function(){
            if(this.validate()){
                FormValuesUtil.getFormValues($("OrganizationForm"),this.theOrganization);

                //use Org and Loc filter to set parentId and locatorId for Organization. If selected id is null, substitute if with empty string. null value will never get to database. Empty string serves as NulAlias
                this.theOrganization.parentId=this.orgOrganizationFilter.currentSelectedId==null?'':this.orgOrganizationFilter.currentSelectedId;
                this.theOrganization.locatorId=this.orgLocatorFilter.currentSelectedId==null?'':this.orgLocatorFilter.currentSelectedId;
                this.persist(this.theOrganization);
                return true;
            }
            else {
                return false;
            }
        }
    });

callOnLoad(function(){
    theOrganizationEditPopup = new OrganizationEditPopup("organization_edit", "OrganizationForm", theOrganizationTable);
});

function onChange(parentId){
    theOrganizationEditPopup.theOrganization.parentId=parentId;
    updateFullOrganization(parentId);
}

function onNameChange(){
	//nothing to do
}



</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#organization_edit input.textField {
        width: 185px;
    }
    div#organization_edit select {
        width: 185px;
    }

    div#organization_edit {
        width: 350px;
        left: - 175px;
    }
    label {
        width: 88px;
        display: block;
    }


</style>

<!--  -->
<div class="popup" id="organization_edit">
    <div class="popupHeader">
        <h2><span id="EDIT_ORG_NAME">Edit Organization </span></h2>
    </div>

    <div class="popupBody">
        <form  id="OrganizationForm" action="" name="OrganizationForm">
            <h3>Organization Properties</h3>
            <table>
                <!-- ORG PROPERTIES -->
                <tr>
                    <td><label>Name:<span class="required">*</span></label></td>
                    <td><input class="textField" id="name" name="name" type="text" onchange="onNameChange();"/></td>
                </tr>

                <tr>
                    <td><label>Org Type:<span class="required">*</span></label></td>
                    <td>
                        <html:select property="type" styleId="type"  value="">
                            <html:option value="">- Select Type-</html:option>
                            <html:optionsCollection property="options"  name="OrganizationTypeRef"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td><label>Web Page:</label></td>
                    <td><input type="text" id="url" name="url" class="textField"></td>
                </tr>
                <tr>
                    <td><label>Email:</label></td>
                    <td><input type="text" id="email" name="email" class="textField"></td>
                </tr>
                <tr>
                    <td><label>Phone #:</label></td>
                    <td><input type="text" id="phone" name="phone" class="textField"></td>
                </tr>
                <tr>
                    <td><label>Fax #:</label></td>
                    <td><input type="text" id="fax" name="fax" class="textField"></td>
                </tr>
            </table>
            <h3><bean:message key="organization"/> <bean:message key="hierarchy"/></h3>
            <a:ajax  id="orgOrganizationFilter" type="iwm.filter" name="iwm.filter"/>


            <h3><bean:message key="location"/></h3>
            <a:ajax  id="orgLocatorFilter" type="iwm.filter" name="iwm.filter"/>

        </form>
        <input type="button" class="button" value="Save" onclick="theOrganizationEditPopup.save();">
        <input type="button" class="button" value="Cancel" onclick="theOrganizationEditPopup.close();"/>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="OrganizationForm" dynamicJavascript="true" staticJavascript="false"/>

