<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var ObjectDataPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(theId){
            this.theData = new Object();
            _this = this;
            ObjectsData.getItem(function(response){_this.populate(response);}, theId);
            thePopupManager.showPopup('mobileWorker-object_edit');
            $('dataTypeId').focus();
        },
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("DataForm"),this.theData);
        },
        save: function(){
            this.theData.dataValue = $F("dataValue");
            this.persist(this.theData);
        }
    });

callOnLoad(function(){
    theObjectDataPopup = new ObjectDataPopup("mobileWorker-object_edit", "DataForm", MWObjectDataTable);
});
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>


<style type="text/css">

</style>

<!--  -->

<div class="popup" id="mobileWorker-object_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Edit Object Data</span></h2></div>

    <div class="popupBody">
        <form id="DataForm" action="" name="DataForm">
        <table >
            <tr>
                <td><bean:message key="dataTypeId"/>:</td>
                <td>
                    <html:select property="dataTypeId" styleId="dataTypeId" value="" disabled="true">
                        <html:option value="">-- Select Type --</html:option>
                        <html:optionsCollection property="options"  name="DataTypeRef"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td>Label:</td>
                <td><input id="dataLabel" name="dataLabel" type="text" disabled="true"/></td>
            </tr>
            <tr>
                <td>Value:</td>
                <td><input id="dataValue" name="dataValue" type="text"/></td>
            </tr>
            <tr>
                <td><bean:message key="uomId"/>:</td>
                <td>
                    <html:select property="uomId" styleId="uomId"  value="" disabled="true">
                        <html:option value="">-- Unit of Measure --</html:option>
                    <html:optionsCollection property="options"  name="UOMRef"/>
                    </html:select>
                </td>
            </tr>
        </table>
        </form>
        <input type="button" class="button" value="Save" onclick="theObjectDataPopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="theObjectDataPopup.close();"/>
    </div>
</div>


