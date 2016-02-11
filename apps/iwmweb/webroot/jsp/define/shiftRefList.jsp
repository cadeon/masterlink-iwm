<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>      

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ShiftRefs.js"></script>

<script type="text/javascript">

    var ShiftRefDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            return null;
        },
        getCode:function(item){return item.code;},
        getDescription:function(item){return item.description;},
        getShiftStart: function(item){
        	var hourMinute = HourMinuteConveter.splitMinutes(item.shiftStart);
            return hourMinute.hours + ':' + hourMinute.minutes;
        },
        getShiftEnd: function(item){
        	var hourMinute = HourMinuteConveter.splitMinutes(item.shiftEnd);
            return hourMinute.hours + ':' + hourMinute.minutes;
        },
        getTime: function(item){
        	var hourMinute = HourMinuteConveter.splitMinutes(item.time);
            return hourMinute.hours + ':' + hourMinute.minutes;
        },
        getEdit: function(item){
            return '<div class="icon" id="edit"  onclick="theShiftRefEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" /></div>';
        },
        getDelete: function(item) {
            return '<div class="icon" onclick="theShiftRefsTable.deleteItem('+item.id+', \''+escapeJSString(item.code) + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';

        },
        getFormaters: function(){return [this.getCode, this.getDescription, this.getShiftStart,this.getShiftEnd, this.getTime, this.getEdit, this.getDelete];}
    });

    var theShiftRefsTable;
    
    function init() {
        //setup the dataTable

        theConfig = new ShiftRefDataTableConfig(ShiftRefs, ["code","description","shiftStart","shiftEnd", "time"]);
        theShiftRefsTable = new IWMDataTable(theConfig);

        theShiftRefsTable.update();
    }
    callOnLoad(init);

</script>

<!-- Table showing locators that match the filter -->
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />

<input type="button" class="button" value="Add Shift Ref" onclick="theShiftRefEditPopup.show(null, true);"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="shiftRefDataTable">

    <colgroup>
        <col class="codeCol" />
        <col class="descriptionCol" />
        <col class="shiftStartCol" />
        <col class="shiftEndCol" />
        <col class="timeCol" />
        <col class="editCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table property for sorting --%>
            <td class="codeCol" id='sort_code'><bean:message key="code"/></td>
            <td class="descriptionCol" id='sort_description'><bean:message key="description"/></td>
            <td class="shiftStartCol" id='sort_shiftStart'><bean:message key="shiftStart"/></td>
            <td class="shiftEndCol" id='sort_shiftEnd'><bean:message key="end"/></td>
            <td class="timeCol" id='sort_time'><bean:message key="time"/></td>
            <td class="editCol">Edit</td>
            <td class="deleteCol"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="8">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add Shift Ref" onclick="theShiftRefEditPopup.show(null, true);"/>
