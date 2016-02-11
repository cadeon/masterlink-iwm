<%@ page import="org.mlink.iwm.dao.JobsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Projects.js"></script>
<script type='text/javascript' src="dwr/interface/ProjectItems.js"></script>
<script type='text/javascript' src="dwr/interface/JobCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>

<%
    JobsCriteria cr =    session.getAttribute("JobsCriteria")==null?new JobsCriteria():(JobsCriteria)session.getAttribute("JobsCriteria");
    request.setAttribute("JobsCriteria", cr);
%>

<script type="text/javascript">

function getLocatorByNameTest(name){
    var sql = "select ID from locator where name='"+name +"'";
    var callback=function(response){
        //response is a type of array
        for (var i = 0; i < response.length; i++) {
            //each element is a map object{
            var map = response[i];
            //properties of element
            var tmp='';
              for (property in map) {
                    //alert(property + ' = ' + map[property])
                }
        }

        alert(response[0].ID);

    }
    CustomRequest.execute(callback,sql);
}



var JobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.projectId=<%=request.getParameter("projectId")%>;
        return searchCriteria;
    },
    getId:function(item){
        return item.id;
    },
    getSequence:function(item){
            var rtn = "<select id='"+item.referenceNumber + "' onchange='onSequenceChange(this);'>";
            var selected='';
            for (var i = 0; i < theJobDataTable.size; i++){
                if(item.sequenceNumber==(i+1)) selected='selected'; else selected='';
                rtn += "<option "+selected + ">"+(i+1)+"</option>";
            }
            rtn += '</select>'
            return rtn;
    },
    getPosition:function(item){
            return '<a onclick="moveUp('+item.referenceNumber+','+item.sequenceNumber+')">&uarr</a>'+
                    '<a onclick="moveDown('+item.referenceNumber+','+item.sequenceNumber+')">&darr</a>' +
                    '<a onclick="gotoPosition('+item.referenceNumber+')">'+item.sequenceNumber+'</a>';
    },

    getIndent:function(item){
         return '<a onclick="removeIndent('+item.referenceNumber+')">&larr</a>' + '<a onclick="addIndent('+item.referenceNumber+')">&rarr</a>';
    },

    getReference:function(item){return item.referenceNumber},
    getDesc: function(item){
        var desc = item.description;
        var cssClass='jobType';
        if(item.type == 'project') {
            cssClass='projectType';
            desc = desc + '<a style="margin-right:20px;" onclick="expand('+item.referenceNumber+')">&plusmn</a>';
        }
        return '<span '+'class="'+cssClass+'"'+ 'style="padding-left:'+10*parseInt(item.indent)+'pt">' + desc + '</span>';},
    getDelete: function(item) {
        return '<div class="icon" onclick="theJobDataTable.deleteItem('+item.referenceNumber+', \''+item.description + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';

    },
    /*getStatus:function(item){return item.status},
    getObjectRef:function(item){return item.objectRef},
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

    getEdit: function(item){
        return '<div class="icon"  onclick="theJobEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
    },*/
    getFormaters: function(){return [this.getPosition, this.getIndent, this.getDesc, this.getReference, this.getDelete];}


});

function startProject(){
    var callback = function(message){
        if(message && message.length > 0) alert(message);
        else  theJobDataTable.update();
    }
    if(confirm("The project jobs will be released for scheduling. Proceed?")) {
        Projects.start(callback,<%=request.getParameter("projectId")%>);
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


function addIndent(refNumber){
    ProjectItems.addIndent(refNumber);
    theJobDataTable.update();
    enableEditMode(true);
}

function removeIndent(refNumber){
    ProjectItems.removeIndent(refNumber);
    theJobDataTable.update();
    enableEditMode(true);
}

function onSequenceChange (select){
    var seqNumber = DWRUtil.getValue(select);
    ProjectItems.updateSequence(select.id,seqNumber,{callback:function(){theJobDataTable.update();}});
    enableEditMode(true);
}
function moveUp(refNumber,seqNumber){
        ProjectItems.updateSequence(refNumber,eval(seqNumber-1),{callback:function(){theJobDataTable.update();}});
        enableEditMode(true);
}
function gotoPosition(refNumber){
    var newPosition=prompt("Enter new position");
    if(newPosition){
        if(isNaN(newPosition)){
            alert("Enter a valid number!")
        }else{
            ProjectItems.updateSequence(refNumber,newPosition,{callback:function(){theJobDataTable.update();}});
            enableEditMode(true);
        }
    }
}

function expand(refNumber){
    ProjectItems.expand(refNumber,{callback:function(){theJobDataTable.update();}});
    enableEditMode(true);
}

function moveDown(refNumber,seqNumber){
    ProjectItems.updateSequence(refNumber,eval(seqNumber+1),{callback:function(){theJobDataTable.update();}});
    enableEditMode(true);
}
function enableEditMode(value){
    if(value){
        $('editModeButtons').style.display = 'inline';
    } else {
        $('editModeButtons').style.display = 'none';
    }
}
function cancelChanges(){
    ProjectItems.cancel(function(){enableEditMode(false)});
    theJobDataTable.update();
}

function applyChanges(){
    ProjectItems.apply(function(){enableEditMode(false)});
    theJobDataTable.update();
}



var theJobDataTable;
function init() {
    //setup the dataTable
    theConfig = new JobsDataTableConfig(ProjectItems,[],[]);
    theJobDataTable = new IWMDataTable(theConfig);
    theJobDataTable.update();
}
callOnLoad(init);

</script>


<style type="text/css" >

    table#projectItemsTable tbody#dataTableBody td.break {
        border-top: 1px solid black;
        border-collapse: separate
    }

</style>

<h3>Jobs</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Job" onclick="createJobWizardStep1.show(<%=request.getParameter("projectId")%>);"/>
<input type="button" class="button" value="Start Project" onclick="startProject();"/>
<input type="button" class="button" value="Cancel Project" onclick="cancelProject();"/>
<span id="editModeButtons"style="display:none">
    <input type="button" class="button" value="Cancel" onclick="cancelChanges();"/>
    <input type="button" class="button" value="Apply" onclick="applyChanges()"/>
</span>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="projectItemsTable">
    <colgroup>
        <col class="positionCol" />
        <col class="indentCol" />
        <col class="descCol" />
        <col class="referenceCol" />
        <col class="deleteCol" />
    </colgroup>
    <thead>
        <tr>
            <td class="positionCol"></td>
            <td class="indentCol"></td>
            <td class="descCol"><bean:message key="description"/></td>
            <td class="referenceCol">Ref#</td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Back to Project List" onclick="document.location.href = 'ProjectList.do'"/>
