<%@ page import="org.mlink.iwm.dao.SearchCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%
    SearchCriteria cr =    session.getAttribute("LocatorsCriteria")==null?new SearchCriteria():(SearchCriteria)session.getAttribute("LocatorsCriteria");
    request.setAttribute("LocatorsCriteria", cr);
%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Locators.js"></script>


<script type="text/javascript">

function fillPopup(itemId){
    var tooltip;
    var callback = function(locator){
        var name = "Name:" + locator.name + "<br>";
        var schema = "Schema:" + locator.schema + "<br>";
        tooltip = name + schema;
    }
    Locators.getItem(itemId,{callback:callback,async:false});
    return tooltip;
}
var LocatorsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=locatorFilter.currentSelectedId;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },
    getName:function(locator){
//        var name = "Name:" + locator.name + "<br>";
//        var schema = "Schema:" + locator.schema + "<br>";
//        var tooltip = name + schema;
//        return '<a  onmousemove="showToolTip(event,fillPopup('+ locator.locatorId + '));return false" onmouseout="hideToolTip()">' + locator.name + '</a>';
        return locator.name;
    },
    getSchema: function(locator){return locator.schema;},
    getFullLocator: function(locator){return locator.fullLocator;},
    getLocatorData: function(locator) {
        var theIcon = "";
        theIcon += '<div class="icon"   onclick="document.location.href=\'LocatorData.do?id=' + locator.locatorId +'\'">';
        theIcon += '<img src="images/db_16.gif" alt="Data" id='+escapeJSString(locator.fullLocator)+' name="Data" />';
        theIcon += '<span class="theNumber">' + locator.locatorDataCount + '</span>';
        theIcon += '</div>';
        return theIcon;
      //return '<a href="LocatorData.do?'+'locatorId='+locator.locatorId+'">'+locator.locatorDataCount+'</a>';
    },
    getDelete: function(locator) {
        return '<div class="icon" onclick="theLocatorDataTable.deleteItem('+locator.locatorId+', \''+locator.fullLocator + '\');"><img src="images/trash_16.gif"  alt="Delete" name="Delete" id='+escapeJSString(locator.fullLocator)+' /></div>';
    },

    getEdit: function(locator){
        return '<div class="icon"  onclick="theLocatorEditPopup.show('+locator.locatorId+')" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id='+escapeJSString(locator.fullLocator)+' /></div>';
    },

    getFormaters: function(){
        return [this.getFullLocator, this.getName, this.getSchema,  this.getLocatorData, this.getEdit, this.getDelete];
    }


});




var locatorFilter;
var theLocatorDataTable;

function init() {
    //setup the dataTable
    theConfig = new LocatorsDataTableConfig(Locators, ["fullLocator","name","schema","locatorDataCount"]);
    theLocatorDataTable = new IWMDataTable(theConfig);

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                theLocatorDataTable.pageNavigator.reset();
                theLocatorDataTable.update();
            },
            true, true);
    SessionUtil.getCurrentLocator({
        callback:function(curLocatorId){
            locatorFilter.populateChain(curLocatorId);
        },
        async:false});
    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theLocatorDataTable.update() } );
    $('filterText').value="<%=cr.getFilterText2()%>";    
    theLocatorDataTable.update();

}
callOnLoad(init);



</script>

<!--a:ajax  id="tooltip" type="dhtmlgoodies.ballontooltip" name="dhtmlgoodies.ballontooltip"/-->


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

<!-- Table showing locators that match the filter -->
<h3>Matching Locator</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Locator" onclick="theLocatorEditPopup.show();"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="locatorsTable">
    <colgroup>
        <col class="locationCol" />
        <col class="nameCol" />
        <col class="typeCol" />
        <col class="dataCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_fullLocator'>Location</td>
            <td id='sort_name'>Name</td>
            <td id='sort_schema'>Type</td>
            <td class="dataCol" id='sort_locatorDataCount'>Data</td>
            <td class="editCol">Edit</td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id locatorsbody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="6">Loading ...</td>
    </tbody>
</table>

<input type="button" class="button" value="Add Locator" onclick="theLocatorEditPopup.show();"/>

