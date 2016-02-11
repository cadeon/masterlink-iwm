<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%-- --%>
<script type="text/javascript" >
    var objectClassFilter;
    var parentObjectId;
    var parentLocatorId;
    
    var PickTemplatePopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable, isWizard){
            this.superInit(popupId, formName, dataTable, isWizard);
        },
        show: function(parentObjectId1, parentLocatorId1, theId, isWizard){
        	parentObjectId = parentObjectId1;
        	parentLocatorId=parentLocatorId1;
            if(isWizard){
                this.isWizard = isWizard;
            } else {
                this.isWizard = false;
            }

            SessionUtil.getCurrentClass({
                callback:function(id){
                    objectClassFilter.populateChain(id);
                },
                async:false});
            refreshTemplatesList();
            thePopupManager.showPopup('objects_pickTemplate');
        },
        populate: function(item){
        },
        wizardCallback: function(message){
            if(message && message.length > 0)
                alert(message);
            else{
                this.close();
                theObjectEditPopup.show(parentObjectId, parentLocatorId, null, true);
            }
        },
        save: function(){
            var theId = DWRUtil.getValue('theTemplateId');
            if(!theId){
                alert("Template must be elected first!");
            }else{
                var _this = this;
                ObjectCreateWizard.selectTemplate(function(message){_this.wizardCallback()}, theId);
            }
        }
    });

    function refreshTemplatesList() {
        var fillTemplatesTable = function (response) {
            DWRUtil.removeAllRows("TemplatesListTable");
            DWRUtil.addRows("TemplatesListTable", response.items, [
                    function(item){return '<input type="radio" name="theTemplateId" value='+item.id+'>';},
                    function(item){return  item.classDesc;} ]);
        }
        var searchCriteria = new Object();
        searchCriteria.id=objectClassFilter.currentSelectedId;
        ObjectCreateWizard.getData(fillTemplatesTable, searchCriteria);
    }
    callOnLoad(function(){
        objectClassFilter = new ObjectClassChain("objectClassFilter", refreshTemplatesList,false, false);
        objectCreateWizardStep1 = new PickTemplatePopup("objects_pickTemplate","SelectTemplateForm", theObjectsTable, true);
    });
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#objects_pickTemplate div#selectTemplate {
        width: 92%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;
    }
    div#objects_pickTemplate {
        width: 350px;
    }
    div#objects_pickTemplate div#selectTemplate div{
        border-bottom: 1px solid #EEE;
        padding-top: 1px;
        padding-bottom: 2px;
    }

    div#objects_pickTemplate table.dataTable td{
       font-size:10px;
    }
</style>

<div class="popup" id="objects_pickTemplate">
    <div class="popupHeader"><h2><span id="popupTitle">Create Object: Pick Template</span></h2></div>

    <div class="popupBody">
        <form id="SelectTemplateForm" action="" name="SelectTemplateForm">
            <h3>Template Filter</h3>
            <a:ajax id="objectClassFilter" type="iwm.filter" name="iwm.filter"/>
            <h3>Select Template<span class="required">*</span></h3>
            <input type="radio" name="theTemplateId" style="display:none">  <%/*trick to make radio waork with DWRUtil.getValue. Must be at least two radios to return value, overwise returns checked/uncheked*/%>
            <div id="selectTemplate">
                <table class="dataTable" id="templatesListTable">
                    <tbody id="TemplatesListTable">
                    </tbody>
                </table>
            </div>
        </form>
        <input type="button" class="button" value="Next" onclick="objectCreateWizardStep1.save()"/>
        <input type="button" class="button" value="Cancel" onclick="objectCreateWizardStep1.close();"/>
    </div>
</div>
