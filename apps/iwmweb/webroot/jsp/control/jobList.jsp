<%@ page import="org.mlink.iwm.dao.JobsCriteria"%>
<%@ page import="org.mlink.iwm.util.Constants"%>
<%@ page import="org.mlink.iwm.lookup.JobStatusRef"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>
<script type='text/javascript' src="dwr/interface/JobCreateWizard.js"></script>

<%
    JobsCriteria cr =    session.getAttribute("JobsCriteria")==null?new JobsCriteria():(JobsCriteria)session.getAttribute("JobsCriteria");
    request.setAttribute("JobsCriteria", cr);
%>

<script type="text/javascript">

function fillPopup(itemId){
    var tooltip;
    var callback = function(item){
        tooltip ="ObjectRef:"+item.objectRef + "<br>";
        tooltip +="Description:"+item.description + "<br>";
        tooltip +="Full Locator:"+item.fullLocator + "<br>";
        tooltip +="Job Type:"+item.jobType + "<br>";
        tooltip +="Skill Type:"+item.skillType + "<br>";
        tooltip +="Created Date:"+item.createdDate + "<br>";
        tooltip +="Latest Start Date:"+item.latestStart + "<br>";
    }
    Jobs.getItem(itemId,{callback:callback,async:false});
    return tooltip;
}



var JobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.locatorId=locatorFilter.currentSelectedId;
        searchCriteria.classId=classFilter.currentSelectedId;
        searchCriteria.organizationId=organizationFilter.currentSelectedId;
        searchCriteria.jobCategory=$('activeStatusFilter').value;
        searchCriteria.jobType=$('jobType').value;
        searchCriteria.jobStatus=$('jobStatus').value;
        searchCriteria.skillType=$('skillType').value;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },
    getJobId:function(item){
        /*
        var tooltip="ObjectRef:"+item.objectRef + "<br>";
        tooltip +="Description:"+item.description + "<br>";
        tooltip +="Full Locator:"+item.fullLocator + "<br>";
        tooltip +="Job Type:"+item.jobType + "<br>";
        tooltip +="Skill Type:"+item.skillType + "<br>";
        tooltip +="Created Date:"+item.createdDate + "<br>";
        //var rtn =  '<a  onmousemove="showToolTip(event,\''+ tooltip + '\');return false" onmouseout="hideToolTip()">' + item.id + '</a>';
        var rtn =  '<a  onmousemove="showToolTip(event,fillPopup('+ item.id + '));return false" onmouseout="hideToolTip()">' + item.id + '</a>';
        return rtn;
        */
        return "<div style='text-align:center'><a href='/JobDetails.do?id=" + item.id + "'\'>"+item.id+"</a></div>";
    },
    getAge:function(item){
        //TODO: Boy, this is ugly. It should be done server side.
        //item.earliestStart is in 06/10/2012 format
		var ONE_DAY = 86400000; // ms per day        
        var parts= item.earliestStart.split("/");
     	// new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
        var estart = new Date(parts[2], parts[0]-1, parts[1]); // months are 0-based
        var now = new Date();
		estart_ms = estart.getTime();
        now_ms = now.getTime();
                
		return "<div style='text-align:center'>"+parseInt((now_ms - estart_ms)/ONE_DAY)+"</div>"; // no rounding. 
    },
    getStatus:function(item){
        if("Action Req"==item.status){     //highlight NYA jobs
            return "<div  style='text-align:center;background-color:khaki;'>"+item.status+ "</div>"}
        else{
            return "<div style='text-align:center'>"+item.status+"</div>";
        }
    },
    getObjectRef2:function(item){return item.objectRef},
    getObjectRef:function(item){
        if (item.objectRef.startsWith("AreaTarget")){
			return item.objectRef;
            }
        //It's a real object, set up the details link
        var rtn = "";
        rtn += '<a href="/ObjectDetails.do?id=' +item.objectId + '">';
        rtn += item.objectRef;
        rtn += '</a>';
        return rtn;
    },
    getDesc: function(item){return item.description;},
    getFullLocator2: function(item){return item.fullLocator;},
    getFullLocator: function(item){
        var rtn = "";
        rtn += '<span onclick="autoscrollToLocator(' + item.locatorId +','+ item.id +')">';
        rtn += item.fullLocator;
        rtn += '</span>';
        return rtn;
    },
    getJobType: function(item){
        return "<div style='text-align:center'>"+item.jobType+"</div";
        },
    getSkillType: function(item){
        return "<div style='text-align:center'>"+item.skillType+"</div>";
        },
    getCreatedDate: function(item){return item.createdDate;},

    
    getSchedule: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'JobSchedule.do?id=' + item.id + '\'">';
        theIcon += '<img src="images/cal_16.gif" alt="Schedule" name="Schedule" id='+item.id+'/>';
        theIcon += '</div>';
        return theIcon;
    },

    getEdit: function(item){
    	var theIcon = "";
        theIcon += '<div class="icon" onclick="theJobEditPopup.show('+item.id+');" >';
        theIcon += '<img src="images/edit_16.gif"  alt="Edit" name="Edit" id='+item.id+'  />';
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
    getFormaters: function(){return [this.getJobId, this.getAge, this.getStatus, this.getJobType, this.getDesc, this.getObjectRef, this.getFullLocator,
            this.getSkillType, this.getSchedule,  this.getEdit];}


});

function onFailureFilterChange(){
    if(DWRUtil.getValue($('failureFilter'))=='<%=String.valueOf(JobsCriteria.FailureMode.NoWorker)%>' &&
       DWRUtil.getValue($('activeStatusFilter'))=='<%=String.valueOf(Constants.TERMINAL_JOBS_CATEGORY)%>'){
        //only active jobs need be tested for worker availablity
        DWRUtil.setValue($('activeStatusFilter'),'<%=String.valueOf(Constants.ACTIVE_JOBS_CATEGORY)%>')
    }
    theJobDataTable.update();
}

var locatorFilter;
var classFilter;
var organizationFilter;

var theJobDataTable;

function init() {
    //setup the dataTable
    theConfig = new JobsDataTableConfig(Jobs, ["id","earliestStart","status","objectRef","description",
            "fullLocator","skillType","jobType"], [4,5,6]);
    theJobDataTable = new IWMDataTable(theConfig);
    theJobDataTable.tableSort.currentSortColumn="id";
    theJobDataTable.tableSort.currentSortDirection="DESC";

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                theJobDataTable.pageNavigator.reset();
                theJobDataTable.update();
            },
            true, true);
    SessionUtil.getCurrentLocator({
        callback:function(curLocatorId){
            locatorFilter.populateChain(curLocatorId);
        },
        async:false});

    classFilter = new ObjectClassChain("classFilter",
            function (){
                theJobDataTable.pageNavigator.reset();
                theJobDataTable.update();
            },
        true, true);

    SessionUtil.getCurrentClass({
            callback:function(id){
            classFilter.populateChain(id);
        },
        async:false});

    organizationFilter = new OrganizationChain("organizationFilter",
            function (){
                theJobDataTable.pageNavigator.reset();
                theJobDataTable.update();
            },
            true, true);
    SessionUtil.getCurrentOrganization({
        callback:function(curId){
            organizationFilter.populateChain(curId);
        },
        async:false});

    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theJobDataTable.update() } );

    $('filterText').value="<%=cr.getFilterText2()%>";
    theJobDataTable.update();
}


function filterByClass(classId){
    if(classFilter.currentSelectedId!=classId) {
        classFilter.populateChain(classId);
        theJobDataTable.update();
    }
}


var previouslyClickedItem=-1;
function autoscrollToClass(classId,itemId){
    if(classFilter.currentSelectedId!=classId) {   //item clicked for 1st time
        classFilter.populateChain(classId);
        previouslyClickedItem=itemId;
    }else{
        if(previouslyClickedItem==itemId) {        //item clicked for 2nd time
            //theJobDataTable.update();
            previouslyClickedItem=-1;
        }
    }
}

function autoscrollToLocator(locatorId,itemId){
    if(locatorFilter.currentSelectedId!=locatorId) {   //item clicked for 1st time
        locatorFilter.populateChain(locatorId);
        previouslyClickedItem=itemId;
    }else{
        if(previouslyClickedItem==itemId) {        //item clicked for 2nd time
            //theJobDataTable.update();
            previouslyClickedItem=-1;
        }
    }
}

callOnLoad(init);

</script>

<!--a:ajax  id="tooltip" type="dhtmlgoodies.ballontooltip" name="dhtmlgoodies.ballontooltip"/-->

<!-- FILTER -->
<h3><bean:message key="jobFilters"/></h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4><bean:message key="jobFilter"/></h4>
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
                        <html:select styleClass="filterSelect" property="jobCategory" styleId="activeStatusFilter" name="JobsCriteria" onchange="theJobDataTable.update();">
                            <html:option value="<%=String.valueOf(Constants.ACTIVE_JOBS_CATEGORY)%>">Active</html:option>
                            <html:option value="<%=String.valueOf(Constants.PENDING_JOBS_CATEGORY)%>">Pending</html:option>
                            <html:option value="<%=String.valueOf(Constants.TERMINAL_JOBS_CATEGORY)%>">History</html:option>
                        </html:select>
                    </td>
                    <td>
                        <html:select styleClass="filterSelect" property="failure" styleId="failureFilter" name="JobsCriteria" onchange="onFailureFilterChange();">
                            <html:option value="<%=String.valueOf(JobsCriteria.FailureMode.None)%>">No Failure</html:option>
                           
                        </html:select>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="filter" id="locationFilter" >
        <h4><bean:message key="locationFilter"/></h4>
        <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
    </div>

    <div class="filter" id="templateFilter" >
        <h4><bean:message key="templateFilter"/></h4>
        <a:ajax  id="classFilter" type="iwm.filter" name="iwm.filter"/>
    </div>


    <div class="filter"  >
        <h4><bean:message key="organizationFilter"/></h4>
        <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
    </div>


    <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing locators that match the filter -->
<h3><bean:message key="matching"/> <bean:message key="jobs"/></h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Job" onclick="createJobWizardStep1.show();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="jobsTable">
    <colgroup>
        <col class="jobIdCol" />
        <col class="jobAgeCol" />
        <col class="statusCol" />
        <col class="typeCol" />
        <col class="descriptionCol" />
        <col class="objectIdCol" />
        <col class="locationCol" />
        <col class="skillCol" />
        <col class="scheduleCol" />
        <col class="editCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_id' style='text-align:center;'><bean:message key="jobId"/></td>
            <td id='sort_earliestStart' style='text-align:center;'>Age</td>
            <td id='sort_status' style='text-align:center;'><bean:message key="status"/></td>
            <td id='sort_jobType' style='text-align:center;'><bean:message key="jobType"/></td>
            <td id='sort_description'><bean:message key="description"/></td>
            <td id='sort_objectRef'><bean:message key="object"/></td>
            <td id='sort_fullLocator'><bean:message key="location"/></td>
            <td id='sort_skillType' style='text-align:center;'><bean:message key="skill"/></td>
            <td class="scheduleCol">Schdl</td>
            <td class="editCol"><bean:message key="edit"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Add Job" onclick="createJobWizardStep1.show();"/>