<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/JobTaskActions.js"></script>

<script type="text/javascript">

var TADataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("taskId")%>;
        return searchCriteria;
    },
    getSequence:function(item){return item.sequence},
    getFieldCond:function(item){return item.fieldCondition},
    getVerb: function(item){return item.verb;},
    getName: function(item){return item.name;},
    getModifier: function(item){return item.modifier;},
    getEdit: function(item){
      return '<input type="button" class="button" style="border: 1px solid #999; padding: 0px; font-size: 10px;" value="Edit" onclick="readItem('+item.id+')"/>';
    },

    getFormaters: function(){return [this.getSequence, this.getVerb, this.getName, this.getModifier,
             this.getFieldCond];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new TADataTableConfig(JobTaskActions, ["sequence"]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();

    var breadcrumbLinks = $$('div.breadcrumbs a');
    $('buttonTop').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }
    $('buttonBottom').onclick = function(){
        document.location.href=breadcrumbLinks[breadcrumbLinks.length-1].href;
    }

}
callOnLoad(init);

</script>


<!-- Table showing locators that match the filter -->
<h3>Job Actions</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input id="buttonTop" type="button" class="button" value="Back to Job Tasks" />

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="actionsDataTable">

    <thead>
        <tr>
            <td class="orderCol" id='sort_sequence'><bean:message key="order"/></td>
            <td class="verbCol"><bean:message key="verb"/></td>
            <td class="nameCol"><bean:message key="name"/></td>
            <td class="modifierCol"><bean:message key="modifier"/></td>
            <td class="fieldConditionCol"><bean:message key="fieldCondition"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input id="buttonBottom" type="button" class="button" value="Back to Job Tasks" />
