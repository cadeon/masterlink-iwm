<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/LocatorsData.js"></script>

<script type="text/javascript">

var LocatorsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getLabel:function(item){return item.dataLabel},
    getType: function(item){return item.dataType;},
    getValue: function(item){return item.dataValue;},
    getUnit: function(item){return item.uom;},
    getEdit: function(item){

       var theIcon = "";
        theIcon += '<div class="icon" onclick="theLocatorDataEditPopup.show('+item.id+')">';
        theIcon += '<img src="images/edit_16.gif" alt="Edit" name="Edit" id="'+escapeJSString(item.dataLabel)+'" />';
        theIcon += '</div>';
        return theIcon;
    },
    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.dataLabel + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Delete" name="Delete" id="'+escapeJSString(item.dataLabel)+'" />';
        theIcon += '</div>';
        return theIcon;
    },

    getFormaters: function(){return [this.getType, this.getLabel,  this.getValue, this.getUnit, this.getEdit, this.getDelete];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new LocatorsDataTableConfig(LocatorsData, ["dataTypeId","dataLabel","dataValue","uomId"]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();
}
callOnLoad(init);

</script>

<!-- FILTER -->
<!--h3>Locator Data Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text">
    </div>
</div-->

<!-- Table showing locators that match the filter -->
<h3>Matching Locator Data</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Locator Data" onclick="theLocatorDataEditPopup.show();"/>
<input type="button" class="button" value="Back to Locator List" onclick="document.location.href = 'LocatorList.do'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="locatorDataTable">


    <colgroup>
        <col class="typeCol" />
        <col class="labelCol" />
        <col class="valueCol" />
        <col class="unitCol" />
        <col class="editCol" />
        <col class="deleteCol" />

    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_dataTypeId'><bean:message key="type"/></td>
            <td id='sort_dataLabel'><bean:message key="label"/></td>
            <td id='sort_dataValue'><bean:message key="value"/></td>
            <td id='sort_uomId'><bean:message key="unit"/></td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id locatorsbody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Add Locator Data" onclick="theLocatorDataEditPopup.show();"/>
<input type="button" class="button" value="Back to Locator List" onclick="document.location.href = 'LocatorList.do'"/>
