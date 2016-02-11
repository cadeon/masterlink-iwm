<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >
    var LocatorDataEditPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theLocatorData = new Object();
        },
        show: function(theId){
            if(theId != null && theId != -1){
                $("popupTitle").innerHTML= "Edit Locator Data";
                _this = this;
                LocatorsData.getItem(function(response){_this.populate(response);}, theId);
            } else {
                //if no locator id is passed in that means it is an add. make a blank form.
                $("popupTitle").innerHTML= "Add Locator Data";
                this.theLocatorData.id=-1;
            }
            thePopupManager.showPopup('locators-data_edit');
            $('dataTypeId').focus();
        },
        populate: function(item){
            //set DWR object
            this.theLocatorData = item;
            DWRUtil.setValues(this.theLocatorData);
        },
        save: function(){
            if(this.validate()){
                var item = new Object();
                item.uomId=DWRUtil.getValue("uomId");
                item.locatorId=DWRUtil.getValue("locatorId");
                item.dataTypeId=DWRUtil.getValue("dataTypeId");
                item.dataLabel=DWRUtil.getValue("dataLabel");
                item.dataValue=DWRUtil.getValue("dataValue");
                item.id=DWRUtil.getValue("id");

                var _this = this;
                //LocatorsData.saveItem(function(message){_this.showCallBackMessage(message)}, item);
                this.persist(item);
                return true;
            }
            else {
                return false;
            }
        }
    });

callOnLoad(function(){
    theLocatorDataEditPopup = new LocatorDataEditPopup("locators-data_edit", "DataForm", dataTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#locators-data_edit input.textInput,
    div#locators-data_edit select {
        width: 165px;

    }
</style>

<!--  -->
<div class="popup" id="locators-data_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Edit Locator Data</span></h2></div>

    <div class="popupBody">
        <form id="DataForm" action="" name="DataForm">

        <div style="display:none;">
            <input id="id" name="id" type="text"  class="textInput"/>
            <input id="locatorId" name="locatorId" type="hidden" value="<%= request.getParameter("id") %>"/>
        </div>

        <table >
            <tr>
                <td><label><bean:message key="dataTypeId"/>:</label></td>
                <td>
                    <html:select property="dataTypeId" styleId="dataTypeId"  value="">
                        <html:option value="">-- Select Type --</html:option>
                    <html:optionsCollection property="options"  name="DataTypeRef"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td><label>Label:</label></td>
                <td><input id="dataLabel" name="dataLabel" type="text" class="textInput"/></td>
            </tr>
            <tr>
                <td><label>Value:</label></td>
                <td><input id="dataValue" name="dataValue" type="text" class="textInput"/></td>
            </tr>
            <tr>
                <td><label><bean:message key="uomId"/>:</label></td>
                <td>
                    <html:select property="uomId" styleId="uomId" value="">
                    <html:option value="">-- Unit of Measure --</html:option>
                    <html:optionsCollection property="options"  name="UOMRef"/>
                    </html:select>
                </td>
            </tr>
            <!--tr>
                <td colspan="2"><input type="checkbox">Display In Field</td>
            </tr-->
        </table>
        <input type="button" class="button" value="Save" onclick="theLocatorDataEditPopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="theLocatorDataEditPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="DataForm" dynamicJavascript="true" staticJavascript="false"/>

