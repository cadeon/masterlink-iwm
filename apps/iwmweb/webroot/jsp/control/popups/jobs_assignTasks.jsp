<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var AssignTasksPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(theId, isWizard){
            if(isWizard){
                this.isWizard = isWizard;
            } else {
                this.isWizard = false;
            }
            getTaskList();
            getActiveJobs();
            thePopupManager.showPopup('jobs_assignTasks');
        },

        populate: function(item){
        },

        save: function(){
            var _this = this;
            <%--
          	//TODO: Removed due to unused project feature
            if(Project && Project.id){  // if a project job being created
                JobCreateWizard.buildJobPrototype(function(message){_this.wizardCallback(message)},Project.id);
            }else{
                JobCreateWizard.buildJobPrototype(function(message){_this.wizardCallback(message)},null);
            } --%>
            JobCreateWizard.buildJobPrototype(function(message){_this.wizardCallback(message)},null);
        },

        wizardCallback: function(message){
            if(message && message.length > 0)
                alert(message);
            else{
                this.close();
                //createJobWizardStep3.show(null, true);
                
                <%--
              	//TODO: removed due to unused project feature
                if(Project && Project.id){  // if a project job being created
                	theJobEditPopup.show(null, Project.id);
                }else{
                	theJobEditPopup.show();
                }--%>
                theJobEditPopup.show();
            }
        }
    });

var createJobWizardStep2;
    function getTaskList() {
        var fillObjectList = function (response) {
            DWRUtil.removeAllRows("TaskListTable");
            DWRUtil.addRows("TaskListTable", response.items, [
                    function(item){return item.active==1?'<input type="checkbox" onclick="addTask(this,'+item.id+')">':'';},
                    function(item){return  item.description;},
                    function(item){return  item.skillType;},
                    function(item){return  item.active==1?'Y':'N';}
                    ]);
        }
        JobCreateWizard.getTasks(fillObjectList);
    }

    function getActiveJobs() {
        var fillActiveJobList = function (response) {
            DWRUtil.removeAllRows("JobListTable");
            DWRUtil.addRows("JobListTable", response.items, [
                    function(item){return  item.id;},
                    function(item){return  item.description;},
                    function(item){return  item.createdDate;},
                    function(item){return  item.status;}
                    ]);
        }
        JobCreateWizard.getActiveJobs(fillActiveJobList);
    }
    var theCheckbox;
    var selectTaskCallback = function(message){
        if(message && message.length > 0){
            alert(message);
            theCheckbox.checked=false;
        }
    }
    function addTask(checkbox, taskId){
        theCheckbox = checkbox;
        if(checkbox.checked){
            JobCreateWizard.selectTask(selectTaskCallback,taskId);
        }else{
            JobCreateWizard.unselectTask(taskId);
        }
    }
callOnLoad(function(){
    createJobWizardStep2 = new AssignTasksPopup("jobs_assignTasks","AssignTasksForm", theJobDataTable);
});
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#jobs_assignTasks {
        width: 420px;
        left: -200px;
    }
    div#jobs_assignTasks div#activeJobs{
        width: 95%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;

    }
    div#jobs_assignTasks div#assignTasks{
        width: 95%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;

    }

    div#jobs_assignTasks th {
        text-align:left;
        font-size: 10px;
        background-color: #EEE;
        padding: 2px;
    }
    div#jobs_assignTasks table {
        width: 100%;
        border-collapse: collapse;
    }

    div#jobs_assignTasks td {
        border-bottom: 1px solid #EEE;
        padding: 2px;
    }
</style>

<div class="popup" id="jobs_assignTasks">
    <div class="popupHeader"><h2><span id="popupTitle">Create Job: Assign Tasks</span></h2></div>

    <div class="popupBody">
        <form id="AssignTasksForm" action="" name="AssignTasksForm">

            <h3>Assign Tasks to Job</h3>
            <div id="assignTasks">
                <table>
                    <thead>
                        <tr>
                            <th>Assign</th>
                            <th><bean:message key="task"/></th>
                            <th><bean:message key="skill"/></th>
                            <th><bean:message key="active"/></th>
                        </tr>
                    </thead>
                    <tbody id=TaskListTable> </tbody>
                </table>
            </div>

             <br>
            <h3>Active Jobs for selected Object</h3>
            <div id="activeJobs">
                <table>
                    <thead>
                        <tr>
                            <th>Job Id</th>
                            <th>Description</th>
                            <th>Created</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody id="JobListTable"></tbody>
                </table>
            </div>
        </form>
        <input type="button" class="button" value="Next" onclick="createJobWizardStep2.save()"/>
        <input type="button" class="button" value="Cancel" onclick="createJobWizardStep2.close();"/>
    </div>
</div>
