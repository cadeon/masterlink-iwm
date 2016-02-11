<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- This is the first step of Job createion wizard--%>
<script type="text/javascript" >
    var Project;
    var SelectObjectPopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(projectId){      //projectid is used for ProjectJobs only, overwise not defined
            $("returnedObjectsCount").innerHTML="";
            if(projectId){
                Project = new Object(); Project.id=projectId;
            }
            thePopupManager.showPopup('jobs_selectObject');
            SessionUtil.getCurrentClass({callback:function(id){objectClassFilter.populateChain(id);},async:false});
            SessionUtil.getCurrentLocator({callback:function(id){objectLocatorFilter.populateChain(id);},async:false});
            refreshObjectList();
        },

        populate: function(item){
        },

        save: function(){
            var theId = DWRUtil.getValue('theObjectId');
            if(!theId){
                alert("Object must be selected first!");
            }else{
                var _this = this;
                JobCreateWizard.selectObject(function(message){_this.wizardCallback()}, theId);
            }
        },

        wizardCallback: function(message){
            if(message && message.length > 0)
                alert(message);
            else{
                this.close();
                createJobWizardStep2.show(null, true);
            }
        }
    });

    var createJobWizardStep1;
    var objectClassFilter;
    var objectLocatorFilter;
            var numberOfShownObjects=100;
    function refreshObjectList() {
        var fillObjectList = function (response) {
            DWRUtil.removeAllRows("ObjectListTable");
            DWRUtil.addRows("ObjectListTable", response.items, [
                    function(item){return item.active==1?'<input type="radio" name="theObjectId" value='+item.objectId+'>':'';},
                    function(item){return  item.objectRef;},
                    function(item){return  item.classDesc;},
                    function(item){return  item.fullLocator;},
                    function(item){return  item.active==1?'Y':'N';}
                    ]);
            if(response.totalCount > numberOfShownObjects){
                $("returnedObjectsCount").innerHTML= " shown " + numberOfShownObjects + " out of total " + response.totalCount;
            }else{
                $("returnedObjectsCount").innerHTML= " shown " + response.totalCount  + " out of total " + response.totalCount;
            }
        }
        var searchCriteria = new Object();
        searchCriteria.locatorId=objectLocatorFilter.currentSelectedId;
        searchCriteria.classId=objectClassFilter.currentSelectedId;
        JobCreateWizard.getObjects(fillObjectList, searchCriteria,0,numberOfShownObjects,"objectRef","ASC");
    }
    callOnLoad(function(){
        createJobWizardStep1 = new SelectObjectPopup("jobs_selectObject", "SelectObjectForm", theJobDataTable);
        objectClassFilter = new ObjectClassChain("objectClassFilter",refreshObjectList,true, false);
        objectLocatorFilter = new LocatorChain("objectLocatorFilter", refreshObjectList, true,false);
    });
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
div#jobs_selectObject div#selectObject {
    width: 400px;
    height: 180px;
    overflow:auto;
    border: 1px solid #666;
}

div#jobs_selectObject th {
    font-size: 10px;
}

div#jobs_selectObject div#selectObject td{
    border-bottom: 1px solid #EEE;
    padding-top: 1px;
    padding-bottom: 2px;
}
div#jobs_selectObject div#selectObject table {
    width: 100%;
    border-collapse: collapse;
}

    div#jobs_selectObject table.dataTable td{
       font-size:10px;
    }


div#jobs_selectObject select {
    width: 200px;
}

div#jobs_selectObject {
    width: 440px;
    left: -220px;
}

</style>

<div class="popup" id="jobs_selectObject">
    <div class="popupHeader"><h2><span id="popupTitle">Create Job: Select Object</span></h2></div>

    <div class="popupBody">
        <form id="SelectObjectForm" action="" name="SelectObjectForm">

            <h3>Object Class Filter</h3>
            <a:ajax  id="objectClassFilter" type="iwm.filter" name="iwm.filter"/>

            <h3>Object Location Filter</h3>
            <a:ajax  id="objectLocatorFilter" type="iwm.filter" name="iwm.filter"/>

            <h3>Select Object <span style="margin-left:100px;font-weight:normal;" id="returnedObjectsCount"></span></h3>
            <div id="selectObject">
                <input type="radio" name="theObjectId" style="display:none">  <%/*trick to make radio waork with DWRUtil.getValue. Must be at least two radios to return value, overwise returns checked/uncheked*/%>
                <table class="dataTable" >
                    <tr>
                        <th></th>
                        <th>Object</th>
                        <th>Class</th>
                        <th>Locator</th>
                        <th>Active</th>
                    </tr>
                    <tbody id="ObjectListTable">
                    </tbody>
                </table>
            </div>
        </form>
        <input type="button" class="button" value="Next" onclick="createJobWizardStep1.save()"/>
        <input type="button" class="button" value="Cancel" onclick="createJobWizardStep1.close();"/>
    </div>
</div>

