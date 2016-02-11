<%@ page import="org.mlink.iwm.dao.ProjectsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Projects.js"></script>


<%
    ProjectsCriteria cr =    session.getAttribute("ProjectsCriteria")==null?new ProjectsCriteria():(ProjectsCriteria)session.getAttribute("ProjectsCriteria");
    request.setAttribute("ProjectsCriteria", cr);

%>
<script type="text/javascript">

    function launchMPReport(){
        var orgId = organizationFilter.currentSelectedId==null?"-1":organizationFilter.currentSelectedId;
        var projType = $('projectType').value==""?"-1":$('projectType').value;
        var status = $('activeStatusFilter').value;
        var rangeStartDate = $('rangeStartDate').value==""?"-1":$('rangeStartDate').value;
        var rangeEndDate = $('rangeEndDate').value==""?"-1":$('rangeEndDate').value;
        var url = 'ReadReport.do?report=manpower_projects&organizationID='+orgId+
                  '&projectTypeID='+projType+'&status='+status +
                  '&rangeStartDate='+rangeStartDate + '&rangeEndDate='+rangeEndDate;
        openWindow(url,'manpowerreport', 600, 860)
    }


var ProjectsTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipsCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipsCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.projectCategory=$('activeStatusFilter').value;
        searchCriteria.filterText=$('filterText').value;
        searchCriteria.projectType=$('projectType').value;
        searchCriteria.rangeStartDate=$('rangeStartDate').value==''?null:$('rangeStartDate').value;
        searchCriteria.rangeEndDate= $('rangeEndDate').value==''?null:$('rangeEndDate').value;
        searchCriteria.organization=organizationFilter.currentSelectedId;
        return searchCriteria;
    },

    getId: function(item){return item.id;},
    getCompleted: function(item){return item.percentCompleted + '%';},
    getState: function(item){return item.status;},
    getCreatedDate: function(item){return item.createdDate;},
    getType: function(item){return item.projectType;},
    getName: function(item){return item.name;},
    getCreatedBy: function(item){return item.createdBy;},
    getDelete: function(item) {
        return '<div class="icon" onclick="projectsTable.deleteItem('+item.id+', \''+ escapeJSString(item.name) + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';

    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theProjectEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
    },
    getJobCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ProjectJobs.do?projectId=' + item.id + '&jobCount='+item.jobCount+'\'">';
        theIcon += '<img src="images/db_16.gif" alt="Jobs" />';
        theIcon += '<span class="theNumber">' + item.jobCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getFormaters: function(){return [this.getId, this.getState, this.getCompleted, this.getCreatedDate,  this.getName, this.getType, this.getCreatedBy, this.getEdit, this.getJobCount]}
});




var projectsTable;
var organizationFilter;

function init() {
    //setup the dataTable
    var theConfig = new ProjectsTableConfig(Projects, ["id","createdDate","name","projectType","createdBy","jobCount"], [4,5]);
    projectsTable = new IWMDataTable(theConfig);

    new EventThrottledTextInput(document.getElementById('filterText'), function(){ projectsTable.update() } );
    $('filterText').value="<%=cr.getFilterText2()%>";
<%if(cr.getRangeStartDate()!=null){%>
    $('rangeStartDate').value="<%=cr.getRangeStartDate()%>";
<%}%>
<%if(cr.getRangeEndDate()!=null){%>
    $('rangeEndDate').value="<%=cr.getRangeEndDate()%>";
<%}%>

    organizationFilter = new OrganizationChain("organizationFilter",
            function (){
                projectsTable.pageNavigator.reset();
                projectsTable.update();
            },
            true, true);
    SessionUtil.getCurrentOrganization({
        callback:function(curId){
            organizationFilter.populateChain(curId);
        },
        async:false});

    projectsTable.update();
}
callOnLoad(init);

</script>

<%-- ************************************************************************ --%>
<%-- START HTML ************************************************************* --%>

<!-- FILTER -->
<h3>Projects Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <table class="filterTable">
            <tr>
                <td valign="top">
                    <h4><bean:message key="status"/></h4>
                        <html:select property="category" style="width: 160px" styleId="activeStatusFilter" name="ProjectsCriteria" onchange="projectsTable.update();">
                        <html:option value="Active">Active</html:option>
                        <html:option value="History">History</html:option>
                        <html:option value="All">All</html:option>
                    </html:select>
                </td>
                <td valign="top">
                    <h4><bean:message key="projectTypeId"/></h4>
                    <html:select  property="projectType" style="width: 160px" styleId="projectType"  name="ProjectsCriteria" onchange="projectsTable.update();">
                        <html:option value=""><bean:message key="allTypes"/></html:option>
                        <html:optionsCollection property="options"  name="ProjectTypeRef"/>
                    </html:select>
                </td>
                <td valign="top">
                    <h4>Date Range</h4>
                    <table>
                        <tr>
                            <td><a:ajax  id="rangeStartDate" type="iwm.calendar" name="iwm.calendar"/></td>
                            <td><a:ajax  id="rangeEndDate" type="iwm.calendar" name="iwm.calendar"/></td>
                        </tr>
                        <tr>
                            <td class="fieldComment">From</td>
                            <td class="fieldComment">To</td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
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


<!-- Table showing TenantRequests that match the filter -->
<h3>Recent Requests</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Project" onclick="theProjectEditPopup.show();"/>
<input type="button" class="button" value="Export" onclick="launchMPReport();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="projectsTable">

    <colgroup>
        <col class="projectIdCol" />
        <col class="stateCol" />
        <col class="percentCompleted" />
        <col class="createdDateCol" />
        <col class="nameCol" />
        <col class="typeCol" />
        <col class="createdByCol" />
        <col class="editCol" />
        <col class="jobsCol" />
        <!--col class="deleteCol" /-->
    </colgroup>

    <thead>
        <tr>
            <td id='sort_id'>Project Id</td>
            <td >State</td>
            <td >Complete</td>
            <td id='sort_createdDate'>Created</td>
            <td id='sort_name'>Name</td>
            <td id="sort_projectType">Type</td>
            <td id='sort_createdBy'>Created By</td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="jobsCol" id='sort_jobCount' ><bean:message key="jobCount"/></td>
            <!--td><bean:message key="delete"/></td-->
        </tr>
    </thead>

    <tbody id="dataTableBody">
        <tr>
        <td colspan="9">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add Project" onclick="theProjectEditPopup.show()"/>


<script type="text/javascript">
function onCalendarChange(){
        projectsTable.update();
}
</script>