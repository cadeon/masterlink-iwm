<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>      

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/SystemProps.js"></script>

<script type="text/javascript">

    var SystemPropsDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            return null;
        },
        getProperty:function(item){return item.property;},
        getValue: function(item){return item.value;},
        getDescription:function(item){return item.description;},
        getEdit: function(item){
            return '<div class="icon" id="edit"  onclick="theSystemPropEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
        },
        getFormaters: function(){return [this.getProperty, this.getDescription, this.getValue,  this.getEdit];}
    });

    var theSystemPropsTable;
    
    function init() {
        //setup the dataTable

        theConfig = new SystemPropsDataTableConfig(SystemProps, ["property","description","value"]);
        theSystemPropsTable = new IWMDataTable(theConfig);

        theSystemPropsTable.update();
    }
    callOnLoad(init);

</script>

<!-- Table showing locators that match the filter -->
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />

<input type="button" class="button" value="Add System Prop" onclick="theSystemPropEditPopup.show(null, true);"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="systemPropDataTable">

    <colgroup>
        <col class="propertyCol" />
        <col class="descriptionCol" />
        <col class="valueCol" />
        <col class="editCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table property for sorting --%>
            <td class="propertyCol" id='sort_property'><bean:message key="property"/></td>
            <td class="descriptionCol" id='sort_description'><bean:message key="description"/></td>
            <td class="valueCol" id='sort_value'><bean:message key="value"/></td>
            <td class="editCol">Edit</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="8">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add System Prop" onclick="theSystemPropEditPopup.show(null, true);"/>
