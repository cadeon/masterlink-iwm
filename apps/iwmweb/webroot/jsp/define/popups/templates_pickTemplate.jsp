<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%-- --%>
<script type="text/javascript" >
    var objectClassFilter;
    var PickTemplatePopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theTargetObjectTemplate = new Object();
        },
        show: function(theId){
            this.theTargetObjectTemplate.id = theId;
            SessionUtil.getCurrentClass({
                callback:function(id){
                    objectClassFilter.populateChain(id);
                },
                async:false});
            refreshTemplatesList();
            thePopupManager.showPopup('templates_pickTemplate');
        },
        populate: function(item){
        },

        callback: function(copiedTasksCount){
            alert(" Copied " + copiedTasksCount + " tasks");
            this.close();
            dataTable.update();
        },
        save: function(){
            var theSourceId = DWRUtil.getValue('theTemplateId');
            if(!theSourceId){
                alert("Template must be elected first!");
            }else{
                var _this = this;
                if(confirm("All tasks from chosen templated will be copied to the current template. Proceed?")){
                    TemplateTasks.copyTaskTemplates(function(count){_this.callback(count)}, theSourceId, this.theTargetObjectTemplate.id);
                }
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
        TemplateTasks.getTemplates(fillTemplatesTable, searchCriteria);
    }
    callOnLoad(function(){
        objectClassFilter = new ObjectClassChain("objectClassFilter", refreshTemplatesList,false, false);
        pickTemplatePopup = new PickTemplatePopup("templates_pickTemplate","SelectTemplateForm", dataTable);
    });
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#templates_pickTemplate div#selectTemplate {
        width: 92%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;
    }
    div#templates_pickTemplate {
        width: 350px;
    }
    div#templates_pickTemplate div#selectTemplate div{
        border-bottom: 1px solid #EEE;
        padding-top: 1px;
        padding-bottom: 2px;
    }

    div#templates_pickTemplate table.dataTable td{
       font-size:10px;
    }
</style>

<div class="popup" id="templates_pickTemplate">
    <div class="popupHeader"><h2><span id="popupTitle">Choose Template to Copy Tasks</span></h2></div>

    <div class="popupBody">
        <form id="SelectTemplateForm" action="" name="SelectTemplateForm">
            <h3>Template Filter</h3>
            <a:ajax id="objectClassFilter" type="iwm.filter" name="iwm.filter"/>
            <h3>Select Template<span class="required">*</span></h3>
            <input type="radio" name="theTemplateId" style="display:none">  <%/*trick to make radio waork with DWRUtil.getValue. Must be at least two radios to return value, overwise returns checked/uncheked*/%>
            <div id="selectTemplate">
                <table class="dataTable" >
                    <tbody id="TemplatesListTable">
                    </tbody>
                </table>
            </div>
        </form>
        <input type="button" class="button" value="Copy" onclick="pickTemplatePopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="pickTemplatePopup.close();"/>
    </div>
</div>
