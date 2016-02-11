<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var AssignTasksPopup = Class.extend( AbstractCrudPopup, {
        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(theId){
            getTaskList();
            thePopupManager.showPopup('projectStencilTasks_assignTasks');
        },

        populate: function(item){
        },

        save: function(){
            var _this = this;
            var projectStencilId=<%= request.getParameter("id") %>;
            ProjectStencilTasks.saveTasks(function(message){_this.wizardCallback(message)},projectStencilId);
        },

        wizardCallback: function(message){
            if(message && message.length > 0)
                alert(message);
            else{
                this.close();
                dataTable.update();
            }
        }
    });

var theAssignTasksPopup;
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
        ProjectStencilTasks.getTasks(fillObjectList);
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
            ProjectStencilTasks.selectTask(selectTaskCallback,taskId);
        }else{
            ProjectStencilTasks.unselectTask(taskId);
        }
    }
callOnLoad(function(){
    theAssignTasksPopup = new AssignTasksPopup("projectStencilTasks_assignTasks", "AssignTasksForm", dataTable);
});
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#projectStencilTasks_assignTasks {
        width: 400px;
        left: -200px;
    }
    div#projectStencilTasks_assignTasks div#activeJobs{
        width: 100%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;

    }
    div#projectStencilTasks_assignTasks div#assignTasks{
        width: 100%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;

    }

    div#projectStencilTasks_assignTasks th {
        text-align:left;
        font-size: 10px;
        background-color: #EEE;
        padding: 2px;
    }
    div#projectStencilTasks_assignTasks table {
        width: 100%;
        border-collapse: collapse;
    }

    div#projectStencilTasks_assignTasks td {
        border-bottom: 1px solid #EEE;
        padding: 2px;
    }
</style>

<div class="popup" id="projectStencilTasks_assignTasks">
    <div class="popupHeader"><h2><span id="popupTitle">Add Task: Select Tasks</span></h2></div>

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
        </form>
        <input type="button" class="button" value="Next" onclick="theAssignTasksPopup.save()"/>
        <input type="button" class="button" value="Cancel" onclick="theAssignTasksPopup.close();"/>
    </div>
</div>
