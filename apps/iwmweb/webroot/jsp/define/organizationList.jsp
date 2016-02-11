<%@ page import="org.mlink.iwm.dao.SearchCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
                                                                    
<%
    SearchCriteria cr =    session.getAttribute("OrganizationCriteria")==null?new SearchCriteria():(SearchCriteria)session.getAttribute("OrganizationCriteria");
    request.setAttribute("OrganizationCriteria", cr);
%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Organizations.js"></script>

<script type="text/javascript">

var OrganizationsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=organizationFilter.currentSelectedId;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },
    /*getName:function(item){return item.name;},*/
    getHierarchy: function(item){return item.hierarchy;},
    getWorkers: function(item){
        var theIcon = "";
        //theIcon += '<div class="icon" onclick="document.location.href=\'WorkerList.do?id=' + item.id + '\'">';
        theIcon += '<div class="icon" onclick="showWorkers('+item.id+')">';
        theIcon += '<img src="images/group_16.gif" alt="Workers" name="Workers" id=\''+escapeJSString(item.name)+'\' />';
        theIcon += '<span class="theNumber">' + item.workerCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },
    getDelete: function(item) {
        return '<div class="icon" onclick="theOrganizationTable.deleteItem('+item.id+', \''+escapeJSString(item.name) + '\');"><img src="images/trash_16.gif"  name="Delete" id=\''+escapeJSString(item.name)+'\' alt="Delete" /></div>';

    },
    getCalendar: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon"   onclick="document.location.href=\'OrganizationCalendar.do?id=' + item.id +'\'">';
        theIcon += '<img src="images/cal_16.gif" alt="Calendar" name="Calendar" id=\''+escapeJSString(item.name)+'\' />';
        theIcon += '</div>';
        return theIcon;
    },
    getEdit: function(item){
        return '<div class="icon"  onclick="theOrganizationEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id=\''+escapeJSString(item.name)+'\' /></div>';
    },
    getLocInfo: function(item) {
    	var theIcon = "";
    	theIcon += '<div class="icon" id="LocInfo" onclick="theMWLocationPopup.showOrgLocInfo('+item.id+',\''+escapeJSString(item.name)+ '\');">';  	
        theIcon += '<img src="images/confg_16.gif" alt="Location Info" />';
        theIcon += '</div>';
        return theIcon;
    },
    getFormaters: function(){return [this.getHierarchy, this.getWorkers, /*this.getLocInfo,*/ this.getCalendar, this.getEdit, this.getDelete];}

});

//var locatorFilter;
var theOrganizationTable;
var organizationFilter;

function init() {
    //setup the dataTable
    theConfig = new OrganizationsDataTableConfig(Organizations, ["hierarchy","workerCount"]);
    theOrganizationTable = new IWMDataTable(theConfig);

    organizationFilter = new OrganizationChain("organizationFilter",
            function (){
                theOrganizationTable.pageNavigator.reset();
                theOrganizationTable.update();
            },
            true, true);
    SessionUtil.getCurrentOrganization({
        callback:function(curId){
            organizationFilter.populateChain(curId);
        },
        async:false});

    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theOrganizationTable.update() } );
    $('filterText').value="<%=cr.getFilterText2()%>";
    theOrganizationTable.update();
}

callOnLoad(init);

</script>

<%-- ************************************************************************ --%>
<%-- START HTML ************************************************************* --%>



<!-- FILTER -->
<h3><bean:message key="organizationFilter"/></h3>
<div class="filterGroup">

    <div class="filter">
        <h4><bean:message key="organizationFilter"/></h4>
        <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
     </div>


    <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing organizations that match the filter -->
<h3><bean:message key="matching"/> <bean:message key="organizations"/></h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Organization" onclick="theOrganizationEditPopup.show();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="organizationTable">


    <colgroup>
        <!-- col class="orgNameCol" / -->
        <col class="hierarchyCol" />
        <col class="workersCol" />
        <!-- col class="locInfoCol"  -->
        <col class="clndrCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <!-- td id='sort_name'><bean:message key="name"/></td -->
            <td id='sort_hierarchy'><bean:message key="organization"/></td>
            <td id='sort_workerCount'><bean:message key="workers"/></td>
            <!-- td class="locInfoCol"><bean:message key="locationInfo"/></td>  -->
            <td class="clndrCol" >Clndr</td>
            <td class="editCol">Edit</td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Add Organization" onclick="theOrganizationEditPopup.show();"/>
