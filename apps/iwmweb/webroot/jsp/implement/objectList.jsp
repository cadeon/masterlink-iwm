<%@ page import="org.mlink.iwm.dao.ObjectsCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Objects.js"></script>
<script type='text/javascript' src="dwr/interface/ObjectCreateWizard.js"></script>

<%
    ObjectsCriteria cr =    session.getAttribute("ObjectsCriteria")==null?new ObjectsCriteria():(ObjectsCriteria)session.getAttribute("ObjectsCriteria");
    request.setAttribute("ObjectsCriteria", cr);
%>

<script type="text/javascript">
var parentObjectId=<%=request.getParameter("id")%>;
var objectRef=<%=request.getParameter("objectRef")%>;
var parentIdsChainStr=<%=request.getParameter("parentIdsChainStr")%>;
var parentORsChainStr=<%=request.getParameter("parentORsChainStr")%>;

var locatorId=<%=request.getAttribute("locatorId")%>;
var classId=<%=request.getAttribute("classId")%>;
var ObjectsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.organizationId=organizationFilter.currentSelectedId;
        searchCriteria.locatorId=locatorFilter.currentSelectedId;
        searchCriteria.classId=classFilter.currentSelectedId;
        searchCriteria.filterText=$('filterText').value;
        return searchCriteria;
    },
    getObjectRef:function(item){
        return "<a href='/ObjectDetails.do?id=" + item.objectId + "'\'>"+item.objectRef+"</a>";
        },
    getClassCode: function(item){return item.classDesc;},
    getFullLocator: function(item){return item.fullLocator;},
    
    getTaskCount: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="document.location.href=\'ObjectTasks.do?id=' + item.objectId + '\'">';
        theIcon += '<img src="images/tasks_16.gif" alt="Tasks" name="Tasks" id=\''+escapeJSString(item.objectRef)+'\' />';
        theIcon += '<span class="theNumber">' + item.taskCount + '</span>';
        theIcon += '</div>';
        return theIcon;
    },

    getCustomizationIndicators:function(item){
        return '<div class="centerText">' +(item.hasCustomData==1?'Y':'N') + '-' + (item.hasCustomTask==1?'Y':'N') + '</div>';
    },
    getActive:function(item){
        var theText = item.active==1?'Y':'N';
        var centeredText = '<div class="centerText">' + theText  + '</div>';
        return centeredText;
    },
    getDelete: function(item) {
        	return '<div class="icon" onclick="theObjectsTable.deleteItem('+item.objectId+', \''+escapeJSString(item.objectRef) + '\');"><img src="images/trash_16.gif"  alt="Delete" name="Delete" id=\''+escapeJSString(item.objectRef)+'\' /></div>';
    },
    getEdit: function(item){
        return '<div class="icon" onclick="theObjectEditPopup.show('+parentObjectId+','+locatorId+', '+item.objectId+');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id=\''+escapeJSString(item.objectRef)+'\' /></div>';
    },
    getFormaters: function(){return [this.getObjectRef, this.getClassCode, this.getFullLocator,
            this.getCustomizationIndicators, this.getActive, this.getTaskCount, this.getEdit, this.getDelete];}
});

var locatorFilter;
var classFilter;
var organizationFilter;

var theObjectsTable;


function init() {
    //setup the dataTable
    theConfig = new ObjectsDataTableConfig(Objects, ["objectRef","classCode",
            "fullLocator","taskCount"], [0,1,2]);
    theObjectsTable = new IWMDataTable(theConfig);

    locatorFilter = new LocatorChain("locatorFilter",
            function (){
                theObjectsTable.pageNavigator.reset();
                theObjectsTable.update();
            },
            true, true);
    SessionUtil.getCurrentLocator({
        callback:function(curLocatorId){
            locatorFilter.populateChain(curLocatorId);
        },
        async:false});

    classFilter = new ObjectClassChain("classFilter",
            function (){
                theObjectsTable.pageNavigator.reset();
                theObjectsTable.update();
            },
        true, true);

    SessionUtil.getCurrentClass({
            callback:function(id){
            classFilter.populateChain(id);
        },
        async:false});
    
    organizationFilter = new OrganizationChain("organizationFilter",
            function (){
                theObjectsTable.pageNavigator.reset();
                theObjectsTable.update();
            },
            true, true);
    SessionUtil.getCurrentOrganization({
        callback:function(curId){
            organizationFilter.populateChain(curId);
        },
        async:false});
    
    new EventThrottledTextInput(document.getElementById('filterText'), function(){ theObjectsTable.update() } );

    $('filterText').value="<%=cr.getFilterText2()%>";

    theObjectsTable.update();
}
callOnLoad(init);
objectCreateWizard = new Array();
</script>

<!-- FILTER -->
<h3>Object Filters</h3>
<div class="filterGroup">
    <div class="filter" id="locationFilter" >
        <h4>Location Filter</h4>
        <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
    </div>
        
    <div class="filter" id="templateFilter" >
        <h4>Template Filter</h4>
        <a:ajax  id="classFilter" type="iwm.filter" name="iwm.filter"/>
    </div>

    <div class="filter"  >
        <h4><bean:message key="organizationFilter"/></h4>
        <a:ajax  id="organizationFilter" type="iwm.filter" name="iwm.filter"/>
    </div>

    <div class="filter" >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text" id="filterText">
    </div>
</div>

<!-- Table showing objects that match the filter -->
<h3>Matching Objects</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Object" onclick="objectCreateWizardStep1.show(parentObjectId, locatorId, null,true)"/>
<%pageContext.setAttribute("pid", request.getParameter("id"));%>



<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="objectListTable">

    <colgroup>
        <col class="objectRefCol" />
        <col class="classCodeCol" />
        <col class="locatorCol" />
        <col class="customCol" />     
        <col class="activeCol" />     
        <col class="tasksCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_objectRef'><bean:message key="objectRef"/></td>
            <td id='sort_classCode'><bean:message key="class"/></td>
            <td id='sort_fullLocator'><bean:message key="fullLocator"/></td>
            <td class="customCol">Custom<br>D-T</td>
            <td class="activeCol"><bean:message key="active"/></td>
            <td class="tasksCol" id='sort_taskCount'><bean:message key="taskCount"/></td>
            <td class="editCol"><bean:message key="edit"/></td>
			<c:choose>
			  <c:when test="${!empty pid}">
	            <td class="deleteCol"><bean:message key="remove"/></td>
			  </c:when>
			  <c:otherwise>
	            <td class="deleteCol"><bean:message key="delete"/></td>
			  </c:otherwise>
			</c:choose>
		</tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <td colspan="10">Loading ...</td>
    </tbody>
</table>
