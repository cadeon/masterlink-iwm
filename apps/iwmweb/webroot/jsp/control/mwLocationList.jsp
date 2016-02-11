<%@ page import="org.mlink.iwm.dao.MWJobsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/MWLocations.js"></script>
<script type="text/javascript" src="scripts/EventThrottledTextInput.js"></script>

<script type="text/javascript">

    var MWLocationsDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols, theToolTipCols) {
            this.superInit(theDWRObject,theSortCols, theToolTipCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            searchCriteria.id=organizationFilter.currentSelectedId;
            searchCriteria.filterText=$('filterText').value;
            searchCriteria.scheduledDate=$('workDate').value;
            return searchCriteria;
        },
        getName:function(item){
        	/*var tooltip="Name:"+item.name + "<br>";
            tooltip +="Organization:"+item.organization + "<br>";
            tooltip +="First Punch:"+item.firstPunch + "<br>";
            tooltip +="Last Punch:"+item.lastPunch + "<br>";
            tooltip +="Hours:"+item.hours + "<br>";
            var rtn =  '<a  onmousemove="showToolTip(event,\''+ tooltip + '\');return false" onmouseout="hideToolTip()">' + item.name + '</a>';
            return rtn;},*/
        	return item.workerName;},
        getOrganization: function(item){return item.organizationName;},
        getFirstPunch: function(item){return item.firstPunch;},
        getLastPunch: function(item){return item.lastPunch;},
        getHours: function(item){return  item.hours;},
        getLocInfo: function(item) {
        	var theIcon = "";
        	if(item.locInfo!=null && item.locInfo!=''){
            	theIcon += '<div class="icon" id="LocInfo" onclick="theMWLocationPopup.show(\''+escapeJSString(item.workerName)+'\',\''+escapeJSString(item.locInfo)+'\');">';
            	theIcon += '<img src="images/confg_16.gif" alt="Location Info" />';
            	theIcon += '</div>';
        	}
            return theIcon;
        },
        getFormaters: function(){return [this.getName, this.getOrganization, this.getFirstPunch, this.getLastPunch, this.getHours, this.getLocInfo];}
    });

    var theLocationsTable;
    var organizationFilter;
    
    function init() {
        //setup the dataTable
        $('workDate').value=convertDateToString(new Date());

        theConfig = new MWLocationsDataTableConfig(MWLocations, ["name","organization","firstPunch","lastPunch","hours"], [0,1,2,3,4]);
        theLocationsTable = new IWMDataTable(theConfig);
        theLocationsTable.tableSort.currentSortColumn="workerName";
        theLocationsTable.tableSort.currentSortDirection="ASC";

        organizationFilter = new OrganizationChain("organizationFilter",
                function (){
                    theLocationsTable.pageNavigator.reset();
                    theLocationsTable.update();
                },
                true, true,1);
        SessionUtil.getCurrentOrganization({
            callback:function(curId){
                organizationFilter.populateChain(curId);
            },
            async:false});

        theLocationsTable.update();
       	new EventThrottledTextInput(
                document.getElementById('filterText'), 
                function(){ 
                    theLocationsTable.update() 
                } );
        new EventThrottledTextInput(
                document.getElementById('workDate'), 
                function(){ 
                    theLocationsTable.update() 
                    } );
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
<h3>Worker Location</h3>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="locationsDataTable">

    <colgroup>
        <col class="nameCol" />
        <col class="organizationCol" />
        <col class="firstPunchCol" />
        <col class="lastPunchCol" />
        <col class="hoursCol" />
        <col class="locInfoCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="nameCol" id='sort_name'><bean:message key="worker"/></td>
            <td class="organizationCol" id='sort_organization'><bean:message key="organization"/></td>
            <td class="firstPunchCol" id='sort_firstPunch'><bean:message key="firstPunch"/></td>
            <td class="lastPunchCol" id='sort_lastPunch'><bean:message key="lastPunch"/></td>
            <td class="hoursCol" id='sort_hours'><bean:message key="hours"/></td>
            <td class="locInfoCol"><bean:message key="locationInfo"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="6">Loading ...</td>
        </tr>
    </tbody>
</table>

<script type="text/javascript">
    function onDateUpdate(){
        theLocationsTable.update();
    }
</script>
