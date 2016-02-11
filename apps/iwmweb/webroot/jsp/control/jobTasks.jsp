<%@ page import="org.mlink.iwm.bean.ObjectTask"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/JobTasks.js"></script>

<script type="text/javascript">

var TasksDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getTaskDescription:function(item){return item.description},
    getEstTime: function(item){
        var returnText = item.estTime;
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;

    },
    getTotalTime: function(item){return item.totalTime;},
    getSkillType: function(item){return item.skillType;},
    getTaskType: function(item){return item.taskType;},
    getActionCount: function(item){
        var theIcon = "";
        if(document.body.id == 'projectJobTasks'){
            theIcon += '<div class="icon" onclick="document.location.href=\'ProjectJobTaskActions.do?taskId=' + item.id + '&id='+<%=request.getParameter("id")%>+  '&projectId='+<%=request.getParameter("projectId")%>+'\'">';
        } else{
            theIcon += '<div class="icon" onclick="document.location.href=\'JobTaskActions.do?taskId=' + item.id + '&id='+<%=request.getParameter("id")%>+  '&projectId='+<%=request.getParameter("projectId")%>+'\'">';
        }
        theIcon += '<img src="images/db_16.gif" alt="Actions" />';
        theIcon += '<span class="theNumber">' + item.actionCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getTime: function(item) {
        return '<div class="icon"  onclick="showJobTaskTimes('+item.id+');" ><img src="images/edit_16.gif"  alt="Time" /></div>';
    },

    getFormaters: function(){return [this.getTaskDescription,  this.getEstTime, this.getTotalTime, this.getSkillType, this.getTaskType, this.getActionCount, this.getTime];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new TasksDataTableConfig(JobTasks, ["description","estTime","totalTime","skillType", "taskType","actionCount"]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();


    var breadcrumbLinks = $$('div.breadcrumbs a');
    $('buttonTop').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }
    $('buttonBottom').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }

}
callOnLoad(init);



</script>
<!-- Table showing locators that match the filter -->
<h3>Job Tasks</h3>
<a:ajax id="pagemeter"   type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input id="buttonTop" type="button" class="button" value="Back to Jobs List"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="jobTasksDataTable">

    <colgroup>
        <col class="descriptionCol" />
        <col class="estTimeCol" />
        <col class="totalTimeCol" />
        <col class="skillCol" />
        <col class="taskTypeCol" />
        <col class="actionsCol" />
        <col class="timeCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="descriptionCol" id='sort_description'>Description</td>
            <td class="estTimeCol" id='sort_estTime'>Est.<br>Time</td>
            <td class="totalTimeCol" id='sort_totalTime'>Total.<br>Time</td>
            <td class="skillCol" id='sort_skillType'>Skill</td>
            <td class="taskTypeCol" id='sort_taskType'>Task Type</td>
            <td class="actionsCol" id='sort_actionCount'>Actions</td>
            <td class="timeCol">Time<br>Record</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input id="buttonBottom" type="button" class="button" value="Back to Jobs List"/>
