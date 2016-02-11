
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TemplateTasks.js"></script>

<script type="text/javascript">

var TTDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipsCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipsCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        searchCriteria.taskTypeId=$('taskType').value;
        return searchCriteria;
    },
    getTaskDescription:function(item){return item.description},
    geEstTime: function(item){
        var rtn = item.estTime;
        if(rtn.length>0){
            var hm=HourMinuteConveter.splitMinutes(rtn);
            rtn=hm.hours+":"+hm.minutes;
        }
        var centeredText = '<div class="centerText">' + rtn  + '</div>';
        return centeredText;

    },
    getSkillType: function(item){return item.skillType;},
    getTaskType: function(item){return item.taskType;},
    getSkillLevel: function(item){
        var centeredText = '<div class="centerText">' + item.skillLevel  + '</div>';
        return centeredText;
    },

    getPriority: function(item){return item.priority;},
    getInstanceCount: function(item){
        var centeredText = '<div class="centerText">' + item.instanceCount  + '</div>';
        return centeredText;
    },

    getTaskGroup: function(item){
        //var returnValue = "";
        //if(item.groupId) returnValue = 'Y'; else returnValue = 'N';
        //var centeredText = '<div class="centerText">' + returnValue  + '</div>';
        //  return centeredText;
        var theIcon = "";
        if(item.groupId) {
            theIcon += '<div class="icon" onclick="theGroupEditPopup.show('+item.groupId+');">';
            theIcon += '<img src="images/edit_16.gif" alt="Edit" name="Task Group" id="'+item.description+'" />';
            theIcon += '</div>';
        }
        return theIcon;
    },
    getActionCount: function(item){
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'TemplateTaskActions.do?taskId=' + item.id + '&id='+<%=request.getParameter("id")%>+'\'">';
        theIcon += '<img src="images/effects_16.gif" alt="Actions" name="Actions" id="'+item.description+'" />';
        theIcon += '<span class="theNumber">' + item.actionCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },

    getEdit: function(item){
        var theIcon = "";
        theIcon += '<div class="icon" onclick="theTaskEditPopup.show('+item.id+')">';
        theIcon += '<img src="images/edit_16.gif" alt="Edit" name="Edit" id="'+item.description+'" />';
        theIcon += '</div>';
        return theIcon;
    },
    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.description + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Delete" name="Delete" id="'+item.description+'" />';
        theIcon += '</div>';
        return theIcon;
    },

    getFormaters: function(){return [this.getTaskDescription, this.geEstTime, this.getSkillType,this.getSkillLevel, this.getTaskType,
             this.getTaskGroup, this.getInstanceCount,  this.getActionCount, this.getEdit, this.getDelete];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new TTDataTableConfig(TemplateTasks, ["description","estTime","skillType", "skillLevel",
            "taskType","instanceCount", "actionCount"],[0,2,3,4]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();
    theGroupEditPopup = new GroupEditPopup("templates-taskgroups_edit", "TaskGroupForm", dataTable);
}
callOnLoad(init);

</script>

<!-- FILTER -->
<h3>Template Task Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4>Task Type</h4>

        <html:select property="taskType" styleId="taskType" value="" onchange="dataTable.update();">
            <option value="">-- Show All Types --</option>
            <html:optionsCollection property="options"  name="TaskTypeRef"/>
        </html:select>
    </div>

    <!--div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text">
    </div-->
</div>

<!-- Table showing locators that match the filter -->
<h3>Matching Template Tasks</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Task" onclick="theTaskEditPopup.show();"/>
<input type="button" class="button" value="Back to Template List" onclick="document.location.href = 'TemplateList.do'"/>

<input type="button" class="button" value="Copy Tasks From" onclick="pickTemplatePopup.show(<%=request.getParameter("id")%>);"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="templateTasksTable">

    <colgroup>
        <col class="descriptionCol" />
        <col class="estTimeCol" />
        <col class="skillCol" />
        <col class="skillLevelCol" />
        <col class="typeCol" />
        <%--col class="priorityCol" /> --%>
        <col class="taskGroupCol"  />
        <col class="instsCol" />
        <col class="actionsCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="descriptionCol" id='sort_description'><bean:message key="description"/></td>
            <td class="estTimeCol" id='sort_estTime'><bean:message key="estTime2"/></td>
            <td id='sort_skillType'><bean:message key="skillTypeId"/></td>
            <td class="skillLevelCol" id='sort_skillLevel'><bean:message key="skillLevel2"/></td>
            <td class="typeCol" id='sort_taskType'><bean:message key="taskType2"/></td>
            <%/*td id='sort_priority'><bean:message key="priority"/></td*/%>
            <td class="taskGroupCol">Task<br>Group</td>
            <td class="instsCol" id='sort_instanceCount'><bean:message key="instanceCount"/></td>
            <td class="actionsCol" id='sort_actionCount'><bean:message key="actionCount"/></td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="10">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Add Task" onclick="theTaskEditPopup.show();"/>
<input type="button" class="button" value="Back to Template List" onclick="document.location.href = 'TemplateList.do'"/>
