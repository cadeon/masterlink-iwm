<%@ page import="org.mlink.iwm.bean.ObjectTask"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ObjectTasks.js"></script>
<script type='text/javascript' src="dwr/interface/Counters.js"></script>

<script type="text/javascript">

var TasksDataTableConfig = Class.extend( DataTableConfig, {
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
    getCustom:function(item){
        var returnText = item.custom==1?'Y':'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;

    },
    getActive:function(item){
        var returnText =  item.active==1?'Y':'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;
    },
    getStartDate:function(item){return item.startDate},
    getEstTime: function(item){
        var rtn = item.estTime;
        if(rtn.length>0){
            var hm=HourMinuteConveter.splitMinutes(rtn);
            rtn=hm.hours+":"+hm.minutes;
        }
        return rtn;
    },
    getSkillType: function(item){return item.skillType;},
    getTaskType: function(item){return item.taskType;},
    getSkillLevel: function(item){
        var returnText = item.skillLevel;
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;
    },
    getPriority: function(item){return item.priority;},
    getActionCount: function(item){
    	var actionCt = 0;
    	if( item.actionCount!=null){
    		actionCt = item.actionCount;
    	}
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ObjectTaskActions.do?taskId=' + item.id + '&id='+<%=request.getParameter("id")%>+'\'">';
        theIcon += '<img src="images/effects_16.gif" alt="Actions" name="Actions" id="'+item.description+'" />';
        theIcon += '<span id="'+item.id+'actionCount" class="theNumber">' +  actionCt + '</span>';
        //theIcon += '<span id="'+item.id+'actionCount" class="theNumber">0</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getCalendar: function(item){
        return '<div class="icon"  onclick="readCalendar('+item.id+')" ><img src="images/cal_16.gif"  alt="Calendar" name="Calendar" id="'+item.description+'" /></div>';
    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theTaskEditPopup.show('+item.id+')" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id="'+item.description+'" /></div>';
    },

    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.description + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Delete" name="Delete" id="'+item.description+'" />';
        theIcon += '</div>';
        return theIcon;
    },
    getTaskGroup: function(item){
        /*if(item.groupId) returnText = 'Y'; else returnText = 'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText; */
        var theIcon = "";
        if(item.groupId) {
            theIcon += '<div class="icon" onclick="theGroupEditPopup.show('+item.groupId+');">';
            theIcon += '<img src="images/edit_16.gif" alt="Task Group" name="Task Group" id="'+item.description+'" />';
            theIcon += '</div>';
        }
        return theIcon;

    },
    getFormaters: function(){return [this.getTaskDescription, this.getCustom, this.getEstTime, this.getSkillType,this.getSkillLevel, this.getTaskType,
            this.getActive, this.getTaskGroup,this.getStartDate,  this.getCalendar, this.getActionCount,  this.getEdit, this.getDelete];}

});


var dataTable;


function updateAdtlInfo(list){
    var callback = function(response){
        $(response.id+'actionCount').innerHTML=response.value;
    }
    for (var i=0; i < list.length; i++) {
        Counters.getNumberOfActionsForTask(list[i].id,{callback:callback,async:true});        //causes NS_ERROR_NOT_AVAILABLE on quick clicks.probably due to simultaneos calls to ObjectTasks, and Counters, see jobSchedule.jsp which does the similar functionality using a single script with no problem
    }
}

function init() {
    //setup the dataTable
    theConfig = new TasksDataTableConfig(ObjectTasks, ["description","custom","estTime","skillType", "skillLevel",
            "taskType","actionCount","active","startDate"], [0,3,4,5,6,8]);
    dataTable = new IWMDataTable(theConfig);
    //dataTable.setOnUpdateHook(updateAdtlInfo);
    dataTable.update();
    theGroupEditPopup = new GroupEditPopup("objects-taskgroups_edit", "TaskGroupForm", dataTable);
}
callOnLoad(init);

</script>
<!-- FILTER -->
<h3>Task Filter</h3>
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
<h3>Matching Object Tasks</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Task" onclick="theTaskEditPopup.show();"/>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1);"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="objectTasksTable">
    <colgroup>
        <col class="descriptionCol" />
        <col class="customCol" />
        <col class="estTimeCol" />
        <col class="skillCol" />
        <col class="skillLevelCol" />
        <col class="typeCol" />
        <!--col class="priorityCol" /-->
        <col class="activeCol"  />
        <col class="taskGroupCol"  />
        <col class="startDateCol"  />
        <col class="clndrCol" />
        <col class="actionsCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>
    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td  id='sort_description'><bean:message key="description"/></td>
            <td  id='sort_custom'>Custom</td>
            <td  id='sort_estTime'><bean:message key="estTime2"/></td>
            <td  class="skillCol" id='sort_skillType'><bean:message key="skillTypeId"/></td>
            <td  class="skillLevelCol" id='sort_skillLevel'><bean:message key="skillLevel2"/></td>
            <td  id='sort_taskType'><bean:message key="taskType2"/></td>
            <%/*td id='sort_priority'><bean:message key="priority"/></td*/%>
            <td  id='sort_active'><bean:message key="active"/></td>
            <td>Task<br>Group</td>
            <td id='sort_startDate'>Start<br>Date</td>
            <td  ><bean:message key="calendar"/></td>
            <td   id='sort_actionCount'><bean:message key="actionCount"/></td>
            <td  ><bean:message key="edit"/></td>
            <td  ><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="13">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Add Task" onclick="theTaskEditPopup.show();"/>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1);"/>