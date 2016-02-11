<%@ page import="org.mlink.iwm.dao.JobsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Projects.js"></script>
<script type='text/javascript' src="dwr/interface/ProjectJobs.js"></script>
<script type='text/javascript' src="dwr/interface/JobCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>

<%
    JobsCriteria cr =    session.getAttribute("JobsCriteria")==null?new JobsCriteria():(JobsCriteria)session.getAttribute("JobsCriteria");
    request.setAttribute("JobsCriteria", cr);
%>

<script type="text/javascript">


var JobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.projectId=<%=request.getParameter("projectId")%>;
        searchCriteria.jobType=$('jobType').value;
        searchCriteria.jobStatus=$('jobStatus').value;
        searchCriteria.skillType=$('skillType').value;
        searchCriteria.filterText=$('filterText').value;
        searchCriteria.failure=$('failureFilter').value;
        return searchCriteria;
    },
    getJobId:function(item){
        return item.id;
    },
    getSequence:function(item){return item.sequenceLevel},
    getStatus:function(item){return item.status},
    getObjectRef:function(item){return item.objectRef},
    getDesc: function(item){return item.description;},
    getFullLocator: function(item){return item.fullLocator;},
    getJobType: function(item){return item.jobType;},
    getSkillType: function(item){return item.skillType;},
    getCreatedDate: function(item){return item.createdDate;},
    getEstTime: function(item){
        var returnText = item.estTimeDisp;
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;

    },

    getTaskCount: function(item) {
        var theIcon = "";

        if(document.body.id == 'projectJobs'){
            theIcon += '<div class="icon" onclick="document.location.href=\'ProjectJobTasks.do?projectId=<%=request.getParameter("projectId")%>&id=' + item.id + '\'">';
            theIcon += '<img src="images/db_16.gif" alt="Tasks" />';
            theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
            theIcon += '</div>';
        } else {
            theIcon += '<div class="icon" onclick="document.location.href=\'JobTasks.do?jobId=' + item.id + '\'">';
            theIcon += '<img src="images/db_16.gif" alt="Tasks" />';
            theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
            theIcon += '</div>';
        }
        return theIcon;
    },
    getSchedule: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ProjectJobSchedule.do?projectId=<%=request.getParameter("projectId")%>&id=' + item.id + '\'">';
        theIcon += '<img src="images/cal_16.gif" alt="Schedule" />';
        theIcon += '</div>';
        return theIcon;
    },

    getHistory: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="showJobHistory('+item.id+')">';
        theIcon += '<img src="images/hist_16.gif" alt="data" />';
        theIcon += '</div>';
        return theIcon;
    },
    getDelete: function(item) {
        return '<div class="icon" onclick="theJobDataTable.deleteItem('+item.id+', \''+item.id + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';

    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theJobEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
    },
    getFormaters: function(){return [this.getSequence, this.getJobId,this.getStatus,this.getObjectRef, this.getDesc, this.getFullLocator,
            this.getSkillType, this.getEstTime, this.getTaskCount,   this.getSchedule, this.getEdit];}


});

function startProject(){
    var callback = function(message){
        if(message && message.length > 0) alert(message);
        else  theJobDataTable.update();
    }
    var jobCount = <%=request.getParameter("jobCount")%>;
    if(jobCount<1) {
        alert("Project must have jobs before starting.");
    } else {
	    if(confirm("The project jobs will be released for scheduling. Proceed?")) {
	        Projects.start(callback,<%=request.getParameter("projectId")%>);
	    }
    }
}

function cancelProject(){
    var callback = function(message){
        if(message && message.length > 0) alert(message);
        else theJobDataTable.update();
    }
    if(confirm("The project and its jobs will be cancelled. This operation is not reversable. Proceed?")){
        Projects.cancel(callback,<%=request.getParameter("projectId")%>);
    }
}

function onFailureFilterChange(){
    theJobDataTable.update();
}


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


var theJobDataTable;
function init() {
    //setup the dataTable
    theConfig = new JobsDataTableConfig(ProjectJobs, [], [3,4,5,6]);
    theJobDataTable = new IWMDataTable(theConfig);
    theJobDataTable.tableSort.currentSortColumn="sequenceLevel";
    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theJobDataTable.update() } );
    theJobDataTable.setOnUpdateHook(setTableSeparators);
    $('filterText').value="<%=cr.getFilterText2()%>";    
    theJobDataTable.update();
}
callOnLoad(init);

function launchGTReport(){
    var projectId=<%=request.getParameter("projectId")%>;
    var callback=function(filename){
        var url = 'jsp/control/projectGanttChart.jsp?file='+filename;
        openWindow(url,'ganttchartdemo', 500, 800);
    }
    ProjectJobs.generateGanttChart(callback,projectId)
}

</script>


<style type="text/css" >

    table#jobsTable tbody#dataTableBody td.break {
        border-top: 1px solid black;
        border-collapse: separate
    }

</style>



<!-- FILTER -->
<h3>Job Filters</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4>Job Filter</h4>
        <table class="filterTable">
            <tbody class="filterEdit" id="edit">
                <tr>
                    <td>
                        <html:select styleClass="filterSelect" property="skillType" styleId="skillType" name="JobsCriteria" onchange="theJobDataTable.update();">
                            <option value=""><bean:message key="allSkills"/></option>
                            <html:optionsCollection property="options"  name="SkillTypeRef"/>
                        </html:select>
                    </td>
                    <td>
                        <html:select styleClass="filterSelect" property="jobType" styleId="jobType" name="JobsCriteria" onchange="theJobDataTable.update();">
                            <option value=""><bean:message key="allTypes"/></option>
                            <html:optionsCollection property="options"  name="TaskTypeRef"/>
                        </html:select>
                    </td>
                    <td>
                        <html:select styleClass="filterSelect" property="jobStatus" styleId="jobStatus" name="JobsCriteria" onchange="theJobDataTable.update();">
                            <option value=""><bean:message key="allStatuses"/></option>
                            <html:optionsCollection property="options"  name="JobStatusRef"/>
                        </html:select>
                    </td>
                    <td>
                        <html:select styleClass="filterSelect" property="failure" styleId="failureFilter" name="JobsCriteria" onchange="onFailureFilterChange();">
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.None)%>">No Failure</html:option>
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.NoWorker)%>">No Eligible Worker</html:option>
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.WorkerAvailableNotAutoAssigned)%>">Potentially Assignable Jobs</html:option>                            
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.ManualSchedule)%>">Manual Schedule</html:option>
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.NYAJobs)%>">NYA Jobs</html:option>
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.SoonToExpire)%>">Soon to Expire</html:option>
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.Expired)%>">Expired</html:option>
                        </html:select>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>


    <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing locators that match the filter -->
<h3>Matching Jobs</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Job" onclick="createJobWizardStep1.show(<%=request.getParameter("projectId")%>);"/>
<input type="button" class="button" value="Order Jobs" onclick="getOrderJobs();"/>
<input type="button" class="button" value="Start Project" onclick="startProject();"/>
<input type="button" class="button" value="Cancel Project" onclick="cancelProject();"/>
<input type="button" class="button" value="Gantt Chart" onclick="launchGTReport();"/>


<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="jobsTable">


    <colgroup>
        <col class="orderCol" />
        <col class="jobIdCol" />
        <col class="statusCol" />
        <col class="objectIdCol" />
        <col class="descriptionCol" />
        <col class="locationCol" />
        <col class="skillCol" />
        <!--col class="typeCol" /-->
        <col class="estTimeCol" />
        <col class="tasksCol" />
        <col class="editCol" />
        <col class="editCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_sequenceLevel'><bean:message key="order"/></td>
            <td id='sort_id'><bean:message key="jobId"/></td>
            <td id='sort_status'><bean:message key="status"/></td>
            <td id='sort_objectRef'><bean:message key="object"/></td>
            <td id='sort_description'><bean:message key="description"/></td>
            <td id='sort_fullLocator'><bean:message key="location"/></td>
            <td id='sort_skillType'><bean:message key="skill"/></td>
            <!--td id='sort_jobType'><bean:message key="jobType"/></td-->
            <td id='sort_estTime'><bean:message key="estTime"/></td>
            <td class="tasksCol" id='sort_taskCount'><bean:message key="taskCount"/></td>
                <!--td id='sort_createdDate'>Crtd Date</td-->
            <td class="editCol">Schdl</td>
            <td class="editCol"><bean:message key="edit"/></td>
            <!--td class="deleteCol"><bean:message key="delete"/></td-->
            <!--td class="deleteCol">Hist</td-->
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">  <tr>
        <td colspan="11">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Back to Project List" onclick="document.location.href = 'ProjectList.do'"/>
