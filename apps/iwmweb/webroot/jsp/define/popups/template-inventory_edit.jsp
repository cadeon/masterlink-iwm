<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var TemplateInventoryPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(theId){
            this.theData = new Object();
            _this = this;
            Templates.getItem(function(response){_this.populate(response);}, theId);
            thePopupManager.showPopup('template-inventory_edit');
            $('deltaInventory').focus();
        },
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("InventoryDataForm"),this.theData);
        },
        save: function(){
            this.theData.deltaInventory = $F("deltaInventory");
            Templates.persistInventory(this.theData);
            theTemplateInventoryPopup.close();
        }
    });

callOnLoad(function(){
	theTemplateInventoryPopup = new TemplateInventoryPopup("template-inventory_edit", "InventoryDataForm", theTemplateDataTable);
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

<div class="popup" id="template-inventory_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Add/Remove Inventory</span></h2></div>

    <div class="popupBody">
        <form id="InventoryDataForm" action="" name="InventoryDataForm">
        <table >
            <tr>
                <td>Present Inventory:</td>
                <td><input id="presentInventory" name="presentInventory" type="text" disabled="true"/></td>
            </tr>
            <tr>
                <td>Value:</td>
                <td><input id="deltaInventory" name="deltaInventory" type="text"/></td>
            </tr>
        </table>
        </form>
        <input type="button" class="button" value="Save" onclick="theTemplateInventoryPopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="theTemplateInventoryPopup.close();"/>
    </div>
</div>