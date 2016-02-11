<%@ page import="org.mlink.iwm.dao.TimeSpecsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ScheduledJobs.js"></script>


<script type="text/javascript">

    var WorkersDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols, theToolTipCols) {
            this.superInit(theDWRObject,theSortCols, theToolTipCols);
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
        getJobId:function(item){return item.id;},
        getJobStatus:function(item){return item.jobStatus;},
        getPriority:function(item){return item.priority;},
        getSkill:function(item){return item.skill;},
        getSkillLevel:function(item){return item.skillLevel;},
        getEstTime: function(item){
            var rtn = item.estTime;
            if(rtn.length>0){
                var hm=HourMinuteConveter.splitMinutes(rtn);
                rtn=hm.hours+":"+hm.minutes;
            }
            var centeredText = '<div class="centerText">' + rtn  + '</div>';
            return centeredText;

        },
        getName:function(item){return item.name;},
        getHierarchy: function(item){return item.hierarchy;},
        getStatus: function(item){return  item.status;},
        getFullLocator: function(item){return item.fullLocator;},
        getShift:function(item){return item.shift;},
        getFormaters: function(){return [this.getJobId, this.getJobStatus, this.getPriority,this.getEstTime, this.getSkill, this.getSkillLevel, this.getName, this.getHierarchy, this.getFullLocator,
                this.getStatus, this.getShift];}
    });

    var theWorkersTable;
    var organizationFilter;
    var locatorFilter;


    function init() {
        //setup the dataTable
        $('workDate').value=convertDateToString(new Date());

        theConfig = new WorkersDataTableConfig(ScheduledJobs, ["id","jobStatus","priority","estTime","skill","skillLevel",
                "name","hierarchy","fullLocator","status"],[2,4,6,7,8]);
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
<h3>Jobs Filter</h3>

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
<h3>Matching Jobs</h3>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="scheduledJobsDataTable">

    <colgroup>
        <col class="jobIdCol" />
        <col class="jobStatusCol" />
        <col class="priorityCol" />
        <col class="estTimeCol" />
        <col class="skillCol" />
        <col class="skillLevelCol" />
        <col class="nameCol" />
        <col class="organizationCol" />
        <col class="fullLocatorCol" />
        <col class="status" />
        <col class="shiftCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="jobIdCol" id='sort_id'><bean:message key="jobId"/></td>
            <td class="jobStatusCol" id='sort_jobStatus'><bean:message key="status"/></td>
            <td class="priorityCol" id='sort_priority'><bean:message key="priority"/></td>
            <td class="estTimeCol" id='sort_estTime'><bean:message key="estTime2"/></td>
            <td class="skillCol" id='sort_skill'><bean:message key="skill"/></td>
            <td class="skillLevelCol" id='sort_skillLevel'>Level</td>
            <td class="nameCol" id='sort_name'><bean:message key="worker"/></td>
            <td class="organizationCol" id='sort_hierarchy'> <bean:message key="worker"/>'s <bean:message key="organization"/></td>
            <td class="fullLocatorCol" id='sort_fullLocator'>Shift <bean:message key="location"/></td>
            <td class="statusCol" id='sort_status'> Shift <bean:message key="status"/></td>
            <td class="shiftCol" >Shift</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="11">Loading ...</td>
        </tr>
    </tbody>
</table>


<script type="text/javascript">
    function onDateUpdate(){
        theWorkersTable.update();
    }
</script>
