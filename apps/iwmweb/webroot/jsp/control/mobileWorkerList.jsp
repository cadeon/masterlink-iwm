<%@ page import="org.mlink.iwm.dao.TimeSpecsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TimeSpecs.js"></script>


<script type="text/javascript">

    var WorkersDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            searchCriteria.organizationId=organizationFilter.currentSelectedId;
            searchCriteria.locatorId=locatorFilter.currentSelectedId;
            searchCriteria.filterText=$('filterText').value;
            searchCriteria.date=$('workDate').value;
            searchCriteria.dateRange='<%=String.valueOf(TimeSpecsCriteria.DATERANGE.ExactDate)%>';
            return searchCriteria;
        },
        getName:function(item){return item.name;},
        getHierarchy: function(item){return item.hierarchy;},
        getStatus: function(item){return  item.statusDesc;},
        getFullLocator: function(item){return item.fullLocator;},
        getJobs: function(item) {
            if(item.jobCount==0) return "";

            var theIcon = "";
            theIcon += '<div class="icon" onclick="document.location.href=\'MWJobs.do?workerId=' + item.personId + '&scheduledDate=' + $('workDate').value + '&status=' + item.status + '\'">';
            theIcon += '<img src="images/tools_16.gif" alt="Jobs" />';
            theIcon += '<span class="theNumber">' + item.jobCount + '</span>';
            theIcon += '</div>';
            return theIcon;
        },

        getShift:function(item){
            var hourMinute = HourMinuteConveter.splitMinutes(item.shiftStart);
            var shiftStart = hourMinute.hours + ':' + hourMinute.minutes;
            hourMinute = HourMinuteConveter.splitMinutes(parseInt(item.shiftStart) + parseInt(item.shiftTime));
            var shiftEnd = hourMinute.hours + ':' + hourMinute.minutes;
            return shiftStart + '-' + shiftEnd;
        },
        getFormaters: function(){return [this.getName, this.getHierarchy, this.getFullLocator, this.getStatus, this.getShift, this.getJobs];}
    });

    var theWorkersTable;
    var organizationFilter;
    var locatorFilter;


    function init() {
        //setup the dataTable
        $('workDate').value=convertDateToString(new Date());

        theConfig = new WorkersDataTableConfig(TimeSpecs, ["name","hierarchy","fullLocator","statusDesc","jobCount"]);
        theWorkersTable = new IWMDataTable(theConfig);

        organizationFilter = new OrganizationChain("organizationFilter",
                function (){
                    theWorkersTable.pageNavigator.reset();
                    theWorkersTable.update();
                },
                true, true,1);
        SessionUtil.getCurrentOrganization({
            callback:function(curId){
                organizationFilter.populateChain(curId);
            },
            async:false});

        locatorFilter = new LocatorChain("locatorFilter",
                function (){
                    theWorkersTable.pageNavigator.reset();
                    theWorkersTable.update();
                },
                true, true,1);
        SessionUtil.getCurrentLocator({
            callback:function(curLocatorId){
                locatorFilter.populateChain(curLocatorId);
            },
            async:false});

        theWorkersTable.update();
        new EventThrottledTextInput(document.getElementById('filterText'), function(){ theWorkersTable.update() } );
        new EventThrottledTextInput(document.getElementById('workDate'), function(){ theWorkersTable.update() } );
    }
    callOnLoad(init);

</script>

<!--div class="todo" >
   <ul>
       <li>FYI: This page is for internal use only. Not production quality! Navigation of some links/button broken on lower pages</li>
   </ul>
</div-->

<!-- FILTER -->
<h3>Worker Filter</h3>

<div class="filterGroup">
    <table>
        <tr>
            <td>
                <div class="filter"  >
                    <h4>Organization Filter</h4>
                    <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
                </div>
            </td>
            <td>
                <div class="filter"  >
                    <h4>Location Filter</h4>
                    <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
                </div>
            </td>
            <td style="width:100px">
                <div class="filter">
                    <h4>Scheduled Date</h4>
                    <a:ajax id="workDate" type="iwm.calendar" name="iwm.calendar"/>
                </div>
            </td>
            <td>
                <div class="filter"  >
                    <h4><bean:message key="textFilter"/></h4>
                    <input type="text" id="filterText">
                </div>
            </td>
        </tr>
    </table>
</div>

<!-- Table showing locators that match the filter -->
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3>Matching Schedules</h3>


<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="schedulesDataTable">

    <colgroup>
        <col class="nameCol" />
        <col class="organizationCol" />
        <col class="fullLocatorCol" />
        <col class="status" />
        <col class="shiftCol" />
        <col class="jobCount" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="nameCol" id='sort_name'><bean:message key="name"/></td>
            <td class="organizationCol" id='sort_hierarchy'><bean:message key="organization"/></td>
            <td class="fullLocatorCol" id='sort_fullLocator'><bean:message key="fullLocator"/></td>
            <td class="statusCol" id='sort_statusDesc'><bean:message key="status"/></td>
            <td class="shiftCol" >Shift</td>
            <td class="jobCount" id='sort_jobCount'>Jobs</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="6">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add Worker" onclick="theWorkerEditPopup.show(null, true);"/>

<script type="text/javascript">
    function onDateUpdate(){
        theWorkersTable.update();
    }
</script>
