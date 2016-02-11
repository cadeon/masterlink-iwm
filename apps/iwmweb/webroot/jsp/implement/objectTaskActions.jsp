<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TaskActions.js"></script>

<script type="text/javascript">
var isTaskCustom = <%=String.valueOf(request.getAttribute("isCustomTask"))%>;
var customTaskMsg="Task is not custom. This operation is available to custom tasks only!"
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
    getCustom:function(item){return item.custom},
    getVerb: function(item){return item.verb;},
    getName: function(item){return item.name;},
    getModifier: function(item){return item.modifier;},
    getDelete: function(item) {
        if(isTaskCustom) return '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.verb + ' ' + item.name + ' ' + item.modifier + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';
        else return '<div class="icon" onclick="alert(\''+customTaskMsg+'\');"><img src="images/trash_16.gif"  alt="Delete" name="Delete" id=\''+item.verb + ' ' + item.name + ' ' + item.modifier + '\' /></div>';
    },
    getEdit: function(item){
        if(isTaskCustom) return '<div class="icon" onclick="theActionEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
        else return '<div class="icon" onclick="alert(\''+customTaskMsg+'\');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id=\''+item.verb + ' ' + item.name + ' ' + item.modifier + '\' /></div>';
    },

    getFormaters: function(){return [this.getSequence, this.getVerb, this.getName, this.getModifier,
             this.getEdit, this.getDelete];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new TADataTableConfig(TaskActions, ["sequence"]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();
}
callOnLoad(init);

function onOrderActions(){
    if(isTaskCustom) getOrderActions();
    else alert(customTaskMsg);
}
function onAddAction(){
    if(isTaskCustom) theActionEditPopup.show();
    else alert(customTaskMsg);
}

</script>
<!-- FILTER: none -->

<!-- Table showing locators that match the filter -->
<h3>Task Actions</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Order Actions" onclick="onOrderActions();"/>
<input type="button" class="button" value="Add Action" onclick="onAddAction();"/>
<input type="button" class="button" value="Back to Tasks List" onclick="document.location.href = 'ObjectTasks.do?id=<%=request.getParameter("id")%>'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="templateTaskActionsTable">

    <colgroup>
        <col class="orderCol" />
        <col class="verbCol" />
        <col class="nameCol" />
        <col class="modifierCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_sequence'><bean:message key="order"/></td>
            <td><bean:message key="verb"/></td>
            <td><bean:message key="name"/></td>
            <td><bean:message key="modifier"/></td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Order Actions" onclick="onOrderActions();"/>
<input type="button" class="button" value="Add Action" onclick="onAddAction();"/>
<input type="button" class="button" value="Back to Tasks List" onclick="document.location.href = 'ObjectTasks.do?id=<%=request.getParameter("id")%>'"/>
