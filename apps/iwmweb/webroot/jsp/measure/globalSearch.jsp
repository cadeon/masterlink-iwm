<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/GlobalSearch.js"></script>

<script type="text/javascript">

var TADataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.filterText='Paint';
        return searchCriteria;
    },
    getText:function(item){return item},
    getFormaters: function(){return [this.getText];}

});


var dataTable;

function init() {
    theConfig = new TADataTableConfig(GlobalSearch, []);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();
}
callOnLoad(init);

</script>

<!-- FILTER: none -->

<!-- Table showing locators that match the filter -->
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<h3>Search Results</h3>
<table class="dataTable" id="globalSearch">

    <colgroup>
        <col class="textCol" />
    </colgroup>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>
