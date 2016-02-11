<%@ page import="org.mlink.iwm.bean.TemplateData"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/TemplatesData.js"></script>

<script type="text/javascript">

var ODDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols) {
        this.superInit(theDWRObject,theSortCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        searchCriteria.dataTypeId=$('dataTypeId').value;
        return searchCriteria;
    },
    getLabel:function(item){return item.dataLabel},
    getType: function(item){return item.dataType;},
    getValue: function(item){return item.dataValue;},
    getUnit: function(item){return item.uom;},
    getIsDisplay: function(item){return item.isDisplay==1?'Y':'N';},
    getIsEditInField: function(item){return item.isEditInField==1?'Y':'N';},
    getEdit: function(item){

        var theIcon = "";

        theIcon += '<div class="icon" onclick="theDataEditPopup.show('+item.id+');" >';
        theIcon += '<img src="images/edit_16.gif" alt="Edit" name="Edit" id="'+item.dataLabel+'" />';
        theIcon += '</div>';
        return theIcon;

    },
    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.fullLocator + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Delete" name="Delete" id="'+item.dataLabel+'" />';
        theIcon += '</div>';
        return theIcon;
    },

    getFormaters: function(){return [ this.getType,this.getLabel, this.getValue, this.getUnit,
            this.getIsDisplay, this.getIsEditInField, this.getEdit, this.getDelete];}

});


var dataTable;

function init() {
    //setup the dataTable
    theConfig = new ODDataTableConfig(TemplatesData, ["dataTypeId","dataLabel","dataValue","uomId"]);
    dataTable = new IWMDataTable(theConfig);

    dataTable.update();
}
callOnLoad(init);

function showAdd() {
	theDataEditPopup.show('-1');
}
</script>

<!-- FILTER -->
<h3>Template Data Filter</h3>
<div class="filterGroup">
    <div class="filter"  >
        <h4><bean:message key="dataTypeId"/></h4>
        <%//Create empty bean. Need this trick to take advantage of html:select tag
            request.setAttribute("emptyItem",new TemplateData());%>
        <html:select property="dataTypeId" styleId="dataTypeId" name="emptyItem" onchange="dataTable.update();">
            <option value="">-- Show All Types --</option>
            <html:optionsCollection property="options"  name="DataTypeRef"/>
        </html:select>
    </div>

    <!--div class="filter"  >
        <h4><bean:message key="textFilter"/></h4>
        <input type="text">
    </div-->
</div>

<!-- Table showing locators that match the filter -->
<h3>Matching Template Data</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Template Data" onclick="theDataEditPopup.show();"/>
<input type="button" class="button" value="Back to Template List" onclick="document.location.href = 'TemplateList.do'"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="templateDataTable">
    <colgroup>
        <col class="typeCol" />
        <col class="labelCol" />
        <col class="valueCol" />
        <col class="unitCol" />
        <col class="displayCol" />
        <col class="fieldEditCol" />
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
            <td>Disp. in<br>Field</td>
            <td class="fieldEditCol" id='sort_fieldedit'>Edit in<br>Field</td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="Add Template Data" onclick="showAdd();"/>
<input type="button" class="button" value="Back to Template List" onclick="document.location.href = 'TemplateList.do'"/>
