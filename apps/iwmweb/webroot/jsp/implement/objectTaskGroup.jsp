<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%--
  Created by IntelliJ IDEA.
  User: jmirick
  Date: Oct 17, 2006
--%>
<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TaskGroups.js"></script>

<script type="text/javascript">

var TaskGroupsConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getDesc:function(item){return item.description},
    getSkillType: function(item){return item.skillType;},
    getTaskCount: function(item){return item.taskCount;},
    getCustom:function(item){return item.custom==1?'Y':'N';},
    getDelete: function(item) {
        return '<div class="icon" onclick="theTaskGroupsTable.deleteItem('+item.id+', \''+item.description + '\');"><img src="images/trash_16.gif"  alt="Delete" name="Delete" id=\''+item.description+'\' /></div>';
    },
    getEdit: function(item){
        return '<div class="icon" onclick="theGroupEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id=\''+item.description+'\' /></div>';
    },
    getFormaters: function(){return [this.getDesc, this.getSkillType,  this.getTaskCount,
           this.getEdit, this.getDelete];}
});


var theTaskGroupsTable;
function init() {
    //setup the dataTable
    theConfig = new TaskGroupsConfig(TaskGroups, ["description","skillType","taskCount"]);
    theTaskGroupsTable = new IWMDataTable(theConfig);

    theTaskGroupsTable.update();
    theGroupEditPopup = new GroupEditPopup("objects-taskgroups_edit", "TaskGroupForm", theTaskGroupsTable);    
}
callOnLoad(init);

</script>

<!-- FILTER: none -->

<!-- Table showing locators that match the filter -->
<h3>Task Groups</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Task Group" onclick="theGroupEditPopup.show();"/>
<input type="button" class="button" value="Back to Object List" onclick="document.location.href = 'ObjectList.do'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="objectTaskGroupsTable">

    <colgroup>
        <col class="descriptionCol" />
        <col class="skillCol" />
        <col class="tasksCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_description'>Descripton</td>
            <td id='sort_skillType'>Skill</td>
            <td id='sort_taskCount'>Tasks</td>
            <td class="editCol">Edit</td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">


    </tbody>
</table>

<input type="button" class="button" value="Add Task Group"  onclick="theGroupEditPopup.show();"/>
<input type="button" class="button" value="Back to Object List" onclick="document.location.href = 'ObjectList.do'"/>
