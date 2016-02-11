<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >
    var MaintenanceRequestAddPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(){
            var _this = this;
            TenantRequests.getNewItem(function(response){_this.populate(response);});
            disableRequestor(true);
            thePopupManager.showPopup('maintenanceRequest_add');
            //$('tenantEmail').focus();
        },
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("MaintRequestForm"),this.theData);
        },
        showCallBackMessage: function(message){
            alert(message);
            this.close();
            this.dataTable.update();
        },
        save: function(){
            if(this.validate()){
                FormValuesUtil.getFormValues($("MaintRequestForm"),this.theData);
                var _this = this;
                //TenantRequests.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
                this.persist(this.theData);
                return true;
            }
            else {
                return false;
            }
        }
    });

    var newRequestLocatorFilter;
    callOnLoad(function(){
        theMaintenanceRequestAddPopup = new MaintenanceRequestAddPopup("maintenanceRequest_add", "MaintRequestForm", dataTable);

        var onChange = function(locatorId){
            $("locatorId").value=locatorId;
        }
        newRequestLocatorFilter = new LocatorChain("newRequestLocatorFilter", onChange, false,false);
        newRequestLocatorFilter.populateChain();
    });

    function disableRequestor(checked){
        $("tenantEmail").disabled=checked;  $("tenantName").disabled=checked; $("tenantPhone").disabled=checked;
    }

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#maintenanceRequest_add {
        width: 320px;
        left: -160px;
    }

    div#maintenanceRequest_add textarea#note {
        height: 60px;
        width: 185px;
    }

    div#maintenanceRequest_add input.textInput {
        width: 185px;
    }

    div#maintenanceRequest_add label {
        width: 65px;
        display: block;
    }

</style>

<!--  -->
<div class="popup" id="maintenanceRequest_add">
    <div class="popupHeader">
        <h2><span id="maintenanceRequestPopupTitle">Create Maintenance Request</span></h2>
    </div>
    <div class="popupBody">

        <form  id="MaintRequestForm" name="MaintRequestForm" action="">
            <input type="hidden" id="locatorId"/>

            <h3>Requestor Information</h3>
            <table id="maintReqContactForm" >
                <tr>
                    <td><label><bean:message key="email"/>:</label></td>
                    <td><input class="textInput" id="tenantEmail" name="tenantEmail" type="text" maxlength="100"></td>
                </tr>

                <tr>
                    <td><label><bean:message key="name"/>:</label></td>
                    <td><input class="textInput" id="tenantName" name="tenantName" type="text" maxlength="100"></td>
                </tr>

                <tr>
                    <td><label><bean:message key="phone"/>:</label></td>
                    <td><input class="textInput" id="tenantPhone" name="tenantPhone" type="text" maxlength="100"></td>
                </tr>

                <tr>
                    <td colspan="2"><input type="checkbox" checked="true" class="checkbox" onclick="disableRequestor(this.checked);">Use information on file for current user</td>
                </tr>

            </table>

            <h3>Request</h3>

            <table id="maintReqForm" >

                <tr>
                    <td><label><bean:message key="problem"/>:</label></td>
                    <td>
                        <html:select property="problemId"  styleId="problemId" value="">
                            <option value="">-- Select Problem --</option>
                            <html:optionsCollection property="options"  name="ProblemSelector"/>
                        </html:select>
                    </td>
                </tr>

                <tr>
                    <td><label><bean:message key="desc"/>:</label></td>
                    <td><input  class="textInput" id="jobDescription" name="jobDescription" type="text" maxlength="150"></td>
                </tr>

                <tr>
                    <td valign="top"><label><bean:message key="comment"/>:</label></td>
                    <td><textarea id="note" name="note"></textarea></td>
                </tr>


            </table>
            <a:ajax  id="newRequestLocatorFilter" type="iwm.filter" name="iwm.filter"/>

        </form>

        <input type="button" value="Save" class="button" onClick="theMaintenanceRequestAddPopup.save();">
        <input type="button" value="Cancel" class="button" onClick="theMaintenanceRequestAddPopup.close();">

    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="MaintRequestForm" dynamicJavascript="true" staticJavascript="false"/>


