<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TenantRequests.js"></script>


<script type="text/javascript">

var TenantRequestsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipsCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipsCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=locatorFilter.currentSelectedId;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },

    getJobId: function(item){return item.jobId;},
    getCreatedDate: function(item){return item.createdDate;},
    getStatus: function(item){return item.status;},
    getFullLocator: function(item){return item.fullLocator;},
    getDesc: function(item){return item.jobDescription;},
    getNote: function(item){return item.note;},
    getRequestType: function(item){return item.requestType;},
    getView: function(item){
        return '<div class="icon"  onclick="theMaintenanceRequestViewPopup.show('+item.jobId+')" ><img src="images/prtpv_16.gif"  alt="View" /></div>';
    },
    getFormaters: function(){return [this.getJobId, this.getCreatedDate, this.getStatus, this.getDesc,this.getFullLocator,
             this.getNote, this.getRequestType, this.getView]}
});




var locatorFilter;
var dataTable;

function init() {
    //setup the dataTable
    var theConfig = new TenantRequestsDataTableConfig(TenantRequests, ["jobId","createdDate","status","description","fullLocator","requestType"], [3,4,5]);
    dataTable = new IWMDataTable(theConfig);
    dataTable.tableSort.currentSortDirection="DESC";

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                dataTable.pageNavigator.reset();
                dataTable.update();
            },
            false, true);
    locatorFilter.populateChain();
    new EventThrottledTextInput(document.getElementById('filterText'), function(){ dataTable.update() } );

    dataTable.update();
}
callOnLoad(init);

</script>


<%-- ************************************************************************ --%>
<%-- START HTML ************************************************************* --%>

<!-- FILTER -->
<h3>Locator Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4>Location Filter</h4>
        <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
    </div>
    <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing TenantRequests that match the filter -->
<h3>Recent Requests</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Submit New Request" onclick="theMaintenanceRequestAddPopup.show()"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="tenantRequestsTable">

    <colgroup>
        <col class="jobIdCol" />
        <col class="createdDateCol" />
        <col class="statusCol" />
        <col class="descCol"/>
        <col class="locationCol" />
        <col class="noteCol"/>
        <col class="requestTypeCol"/>
        <col class="viewCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_jobId'>Job Id</td>
            <td id='sort_createdDate'>Created</td>
            <td id='sort_status'>Status</td>
            <td id="sort_description">Description</td>
            <td id='sort_fullLocator'>Location</td>
            <td>Tenant Note</td>
            <td id="sort_requestType">Type</td>
            <td class="editCol">View</td>
        </tr>
    </thead>

    <tbody id="dataTableBody">
        <td colspan="8">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Submit New Request" onclick="theMaintenanceRequestAddPopup.show()"/>

