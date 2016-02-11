<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ProjectStencilTasks.js"></script>
<script type='text/javascript' src="dwr/interface/Projects.js"></script>
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
        return searchCriteria;
    },
    getTaskDescription:function(item){return item.description},
    getCustom:function(item){return item.custom==1?'Y':'N';},
    getActive:function(item){return item.active==1?'Y':'N';},
    getStartDate:function(item){return item.startDate},
    getEstTime: function(item){
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
    getSkillLevel: function(item){return item.skillLevel;},
    getPriority: function(item){return item.priority;},
    getObjectRef: function(item){return item.objectRef;},
    getLocator: function(item){return item.fullLocator;},
    /*getActionCount: function(item){
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ObjectTaskActions.do?taskId=' + item.id + '&id='+<%=request.getParameter("id")%>+'\'">';
        theIcon += '<img src="images/effects_16.gif" alt="Actions" />';
        theIcon += '<span id="'+item.id+'actionCount" class="theNumber">' +  item.actionCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },*/
    getCalendar: function(item){
        return '<div class="icon"  onclick="readCalendar('+item.id+')" ><img src="images/cal_16.gif"  alt="Calendar" /></div>';
    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theTaskEditPopup.show('+item.id+')" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
    },

    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.taskSequenceId+', \''+item.description + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Remove" />';
        theIcon += '</div>';
        return theIcon;
    },
    getTaskGroup: function(item){
        if(item.groupId) return 'Y'; else return 'N';
    },
    getSequence:function(item){return item.sequenceLevel},
    getFormaters: function(){return [this.getSequence, this.getTaskDescription,  this.getObjectRef, this.getLocator, this.getEstTime, this.getSkillType,this.getTaskType,
             this.getActive,  this.getCalendar,  this.getEdit, this.getDelete];}

});



function setTableSeparators(list){
    var theRows = $('dataTableBody').rows;
    var prevOrder = -1;
    var backgroundColor = "#FFFFFF";
    for(var i=0;i<theRows.length;i++){
        //theRows[i].style.className = "break"

        if(prevOrder != -1 && prevOrder != theRows[i].cells[0].innerHTML){

            if("#FFFFFF" == backgroundColor){
                backgroundColor = "#FFFFCC";
            } else {
                backgroundColor = "#FFFFFF";
            }

            for(var j=0; j < theRows[i].cells.length; j++){
                //alert(theRows[i].cells[j].innerHTML);
                theRows[i].cells[j].className = "break";
            }
        }
        prevOrder = theRows[i].cells[0].innerHTML;
        theRows[i].style.backgroundColor = backgroundColor;
    }
}

var dataTable;
function init() {
    //setup the dataTable
    theConfig = new TasksDataTableConfig(ProjectStencilTasks, [], [1,2,3,5,6]);
    dataTable = new IWMDataTable(theConfig);
    dataTable.tableSort.currentSortColumn="sequenceLevel";
    dataTable.setOnUpdateHook(setTableSeparators);

    dataTable.update();
}
callOnLoad(init);

function createProjectNow(){
    var projectStencilId=<%=request.getParameter("id")%>;
    var callback =  function(message){
        if(message && message.length > 0) alert(message);
    }
    Projects.createProjectFromStencil(callback,projectStencilId);
}

</script>


<input type="button" class="button" value="Create Project Now" onclick="createProjectNow();"/>

<h3>Project Stencil Tasks</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Task" onclick="theSelectObjectPopup.show();"/>
<input type="button" class="button" value="Order Tasks" onclick="getOrderTasks();"/>
<input type="button" class="button" value="Back to Project Stencil List" onclick="document.location.href = 'StencilList.do'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="stencilTasksTable">
        <col class="orderCol" />
        <col class="descriptionCol" />
        <col class="objectIdCol" />
        <col class="locationCol" />
        <col class="estTimeCol" />
        <col class="skillCol" />
        <col class="typeCol" />
        <col class="activeCol" />
        <col class="clndrCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    <thead>
        <tr>
            <td ><bean:message key="order"/></td>
            <td ><bean:message key="description"/></td>
            <td ><bean:message key="objectRef"/></td>
            <td ><bean:message key="fullLocator"/></td>
            <td ><bean:message key="estTime2"/></td>
            <td ><bean:message key="skillType"/></td>
            <td ><bean:message key="taskType2"/></td>
            <td ><bean:message key="active"/></td>
            <td ><bean:message key="calendar"/></td>
            <td ><bean:message key="edit"/></td>
            <td ><bean:message key="remove"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="9">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Add Task" onclick="theSelectObjectPopup.show();"/>
<input type="button" class="button" value="Order Tasks" onclick="getOrderTasks();"/>
<input type="button" class="button" value="Back to Project Stencil List" onclick="document.location.href = 'StencilList.do'"/>
