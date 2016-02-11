<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >
    var MaintenanceRequestViewPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(theId){
            var _this = this;
            TenantRequests.getItem(function(response){_this.populate(response);},theId);
            thePopupManager.showPopup('maintenanceRequest_view');

            if(!$('tenantEmail').disabled){
                $('tenantEmail').focus();
            }
        },
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("MaintRequestView"),this.theData);
        }
    });

    callOnLoad(function(){
        theMaintenanceRequestViewPopup = new MaintenanceRequestViewPopup("maintenanceRequest_view", "MaintRequestView", dataTable);
    });


</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    form#MaintRequestView table {
        border-collapse: collapse;
        border-spacing: 0px;
    }
    div#maintenanceRequest_view {
        width: 350px;
        left: -175px;
    }

    form#MaintRequestView input {
        width: 225px;
    }
    form#MaintRequestView textarea {    <%/*why this style does not work*/%>
        height: 60px;
        width: 225px;
        font-family: arial;
        font-size: 11px;
    }
    div#maintenanceRequest_view label{
        width: 70px;
        display: block;
    }

</style>

<!--  -->
<div class="popup" id="maintenanceRequest_view">
    <div class="popupHeader">
        <h2><span id="maintenanceRequestPopupTitle">View Maintenance Request</span></h2>
    </div>
    <div class="popupBody">

        <form  id="MaintRequestView" name="MaintRequestView" action="">
            <input type="hidden" id="locatorId"/>

            <h3>Requestor Information</h3>
            <table id="maintReqContactForm" >
                <tr>
                    <td><label><bean:message key="email"/>:</label></td>
                    <td><input id="tenantEmail"  type="text" disabled></td>
                </tr>

                <tr>
                    <td><label><bean:message key="name"/>:</label></td>
                    <td><input id="tenantName"   type="text" disabled></td>
                </tr>

                <tr>
                    <td><label><bean:message key="phone"/>:</label></td>
                    <td><input id="tenantPhone"  type="text" disabled></td>
                </tr>

            </table>

            <h3>Request</h3>

            <table id="maintReqForm" >

                <tr>
                    <td><label><bean:message key="problem"/>:</label></td>
                    <td><input id="problemDesc"  type="text" disabled></td>
                </tr>

                <tr>
                    <td valign="top"><label><bean:message key="desc"/>:</label></td>
                    <td><textarea id="jobDescription" type="text" disabled ></textarea></td>
                </tr>

                <tr>
                    <td valign="top"><label><bean:message key="comment"/>:</label></td>
                    <td><textarea id="note" disabled ></textarea></td>
                </tr>

                <tr>
                    <td><label><bean:message key="locator"/>:</label></td>
                    <td><input id="fullLocator" type="text" disabled></td>
                </tr>

            </table>
        </form>

        <input type="button" value="Close" class="button" onClick="theMaintenanceRequestViewPopup.close();">

    </div>
</div>


