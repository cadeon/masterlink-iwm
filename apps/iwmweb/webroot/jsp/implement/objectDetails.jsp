<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%@ page import="org.mlink.iwm.util.Config"%>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/ObjectsData.js"></script>
<script type='text/javascript' src="dwr/interface/Jobs.js"></script>
<script type='text/javascript' src="dwr/interface/Objects.js"></script>
<script type='text/javascript' src="dwr/interface/ObjectCreateWizard.js"></script>
<script type='text/javascript' src="dwr/interface/JobCreateWizard.js"></script>



<%@ page import="org.mlink.iwm.dao.JobsCriteria"%>
<script type="text/javascript">
var objectId=<%=request.getParameter("id")%>;

var ODDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.id=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getLabel:function(item){return item.dataLabel;},
    getType: function(item){return item.dataType;},
    getValue: function(item){return item.dataValue;},
    getUnit: function(item){return '<div class="centerText">' + item.uom + '</div>';},
    getIsDisplay: function(item){
        var returnText = item.isDisplay==1?'Y':'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;
    },
    getIsEditInField: function(item){
        var returnText = item.isEditInField==1?'Y':'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;
    },
    getCustom: function(item){
        var returnText = item.custom==1?'Y':'N';
        var centeredText = '<div class="centerText">' + returnText  + '</div>';
        return centeredText;
    },
    getDelete: function(item) {
        var theIcon = "";
        theIcon += '<div class="icon" onclick="dataTable.deleteItem('+item.id+', \''+item.dataLabel + '\');">';
        theIcon += '<img src="images/trash_16.gif" alt="Delete" name="Delete" id="'+item.dataLabel+'"/>';
        theIcon += '</div>';
        return theIcon;
    },
    getEdit: function(item){
        return '<div class="icon" onclick="theDataEditPopup.show('+item.id+');" ><img src="images/edit_16.gif"  alt="Edit" name="Edit" id="'+item.dataLabel+'" /></div>';
    },

    getFormaters: function(){return [this.getType, this.getLabel, this.getValue, this.getUnit,
            this.getIsDisplay, this.getIsEditInField, this.getCustom, this.getEdit, this.getDelete];}

});

var JobsDataTableConfig = Class.extend( DataTableConfig, {
    initialize: function(theDWRObject, theSortCols, theToolTipCols, thePageMeter, theDataTableBody) {
        this.superInit(theDWRObject,theSortCols, theToolTipCols, thePageMeter, theDataTableBody);
    },
    getSearchCriteria: function(){
        var searchCriteria = new Object();
        searchCriteria.objectId=<%=request.getParameter("id")%>;
        return searchCriteria;
    },
    getJobId:function(item){
       
        return "<div style='text-align:center'><a href='/JobDetails.do?id=" + item.id + "'\'>"+item.id+"</a></div>";
    },
    getAge:function(item){
        //TODO: Boy, this is ugly. It should be done server side.
        //item.earliestStart is in 06/10/2012 format
		var ONE_DAY = 86400000; // ms per day        
        var parts= item.earliestStart.split("/");
     	// new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
        var estart = new Date(parts[2], parts[0]-1, parts[1]); // months are 0-based
        var now = new Date();
		estart_ms = estart.getTime();
        now_ms = now.getTime();
                
		return "<div style='text-align:center'>"+parseInt((now_ms - estart_ms)/ONE_DAY)+"</div>"; // no rounding. 
    },
    getStatus:function(item){
        if("Action Req"==item.status){     //highlight NYA jobs
            return "<div  style='text-align:center;background-color:khaki;'>"+item.status+ "</div>"}
        else{
            return "<div style='text-align:center'>"+item.status+"</div>";
        }
    },
    getObjectRef2:function(item){return item.objectRef},
    getObjectRef:function(item){
        var rtn = "";
        rtn += '<span onclick="autoscrollToClass(' + item.objectClassId +','+ item.id +')"> ';
        //rtn += '<span class="icon" ondblclick="filterByClass(' + item.objectClassId + ')" onmousedown="navigateToClass(' + item.objectClassId + ')" onmouseup="resetNavigateToClass()">';
        rtn += item.objectRef;
        rtn += '</span>';
        return rtn;
    },
    getDesc: function(item){return item.description;},
    getJobType: function(item){
        return "<div style='text-align:center'>"+item.jobType+"</div";
        },
    getSkillType: function(item){
        return "<div style='text-align:center'>"+item.skillType+"</div>";
        },
    getCreatedDate: function(item){return item.createdDate;},
    
    getTotalTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.totalTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},

    getEstTime: function(item){
    	var hourMinute=HourMinuteConveter.splitMinutes(item.estTime);
    	var txt = "<div style='text-align:center'>";
		if (hourMinute.hours >0){
			txt += +hourMinute.hours+ "h ";}
		if (hourMinute.minutes >0){
			txt += +hourMinute.minutes+ "m";
			}
			
    	txt +="</div>";
    	return txt;
    	},

    
    getFormaters: function(){return [this.getJobId, this.getAge, this.getStatus, this.getJobType, this.getDesc,
            this.getSkillType, this.getEstTime, this.getTotalTime];}


});


var dataTable;

function setupObjectDataTable(){
	//setup the dataTable
    theDataConfig = new ODDataTableConfig(ObjectsData, ["dataTypeId","dataLabel","dataValue","uomId","custom"],[0,1,2]);
    dataTable = new IWMDataTable(theDataConfig);

    dataTable.update();
}

var theJobDataTable;

function setupObjectJobsTable() {
    //setup the dataTable
    theJobsConfig = new JobsDataTableConfig(Jobs, ["id","earliestStart","status","description",
           "skillType","jobType"], [5],"jobsPagemeter",jobsTableBody); 
    theJobDataTable = new IWMDataTable(theJobsConfig);
    theJobDataTable.tableSort.currentSortColumn="id";
    theJobDataTable.tableSort.currentSortDirection="DESC";

    theJobDataTable.update();
}

function editObjectClick(){
	theObjectEditPopup.dataTable.DWRObject=Objects; //Confuses the save() call into working. 
	theObjectEditPopup.show(null,null,objectId);
	//overide the existing save behavior to save and refresh the browser
	
	document.getElementById("createObjectSaveButton").onclick = function() { theObjectEditPopup.save(); document.location.reload(true); };
    }
    
    
function addJobClick(){
	theJobEditPopup.dataTable.DWRObject=Jobs; //Confuses the save() call into working.  
		//Jump straight to step two :-D
		createJobWizardStep2.show();
	}
	
	
    

function init() {
    setupObjectDataTable();
  
    setupObjectJobsTable();

    JobCreateWizard.selectObject(objectId); //Sets the object we're working with incase we add a job

  	/* TODO: We only want 10 in the jobs list, but it comes with 25 */
}
callOnLoad(init);

</script>
<nested:root name="ObjectDetailsForm">
<table width="945" style="font-size:8pt">
		<colgroup>
            <col style="width:85px;" />
            <col  />
            
            <col style="width:85px;" />
            <col style="width:100px;" />
        </colgroup>
<tr>
		<td>Location: </td>
		<td id="fullLocator"><nested:write property="fullLocator"/> </td>
		
		
		<td>Created: </td>
		<td id="createdDate"><nested:write property="createdDate"/></td>
</tr>

<tr>
	<td>Template: </td>
	<td id="className"><nested:write property="className"/></td>
	
	
	<td>Active: </td>
	<td id="active"><nested:write property="active"/></td>
</tr>

</table>

</nested:root>
<input type="button" class="button" value="Edit this Object" onclick="editObjectClick()"/>
<%if(request.isUserInRole("ObjTsks")){%>
                <input type="button" class="button" value="Object Tasks" onclick="window.location='/ObjectTasks.do?id=' + objectId"/>
                <%}%>
<input type="button" class="button" id="backButton" value="Back To Previous Page" onclick="history.go(-1)"/>

<div id="blarg" style="visibility: none;">
<!-- save() from the edit job popup needs a table to reference -->
<table id="theJobDataTable"></table>
</div>

<div id="blarg2" style="visibility: none;">
<!-- save() from the edit object popup needs a table to reference -->
<table id="theObjectsTable"></table>
</div>


<h3>Jobs for this Object</h3>
<a:ajax id="jobsPagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" id="followupButton" value="Add Job" onclick="addJobClick()"/>
<table class="dataTable" id="jobsTable">
    <colgroup>
        <col class="jobIdCol" />
        <col class="jobAgeCol" />
        <col class="statusCol" />
        <col class="typeCol" />
        <col class="descriptionCol" />
        <col class="skillCol" />
        <col class="tasksCol" />
        <col class="tasksCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_id' style='text-align:center;'><bean:message key="jobId"/></td>
            <td id='sort_earliestStart' style='text-align:center;'>Age</td>
            <td id='sort_status' style='text-align:center;'><bean:message key="status"/></td>
            <td id='sort_jobType' style='text-align:center;'><bean:message key="jobType"/></td>
            <td id='sort_description'><bean:message key="description"/></td>
            <td id='sort_skillType' style='text-align:center;'><bean:message key="skill"/></td>
            <td class="tasksCol">Est<br/>Time</td>
            <td class="tasksCol">Total<br/>Time</td>      
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="jobsTableBody">
    </tbody>
</table>
<p>

<h3>Object Data</h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="Add Data" onclick="theDataEditPopup.show();"/>
<table class="dataTable" id="objectDataTable">

    <colgroup>
        <col class="typeCol" />
        <col class="labelCol" />
        <col class="valueCol" />
        <col class="unitCol" />
        <col class="displayCol" />
        <col class="fieldEditCol" />
        <col class="fieldEditCol" />
        <col class="editCol" />
        <col class="deleteCol" />

    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td class="typeCol" id='sort_dataTypeId'><bean:message key="type"/></td>
            <td class="labelCol" id='sort_dataLabel'><bean:message key="label"/></td>
            <td class="valueCol" id='sort_dataValue'><bean:message key="value"/></td>
            <td class="unitCol" id='sort_uomId'><bean:message key="unit"/></td>
            <td class="displayCol">Disp. in<br>Field</td>
            <td class="fieldEditCol" id='sort_fieldedit'>Edit in<br>Field</td>
            <td class="fieldEditCol" id='sort_custom'><bean:message key="custom"/></td>
            <td class="editCol"><bean:message key="edit"/></td>
            <td class="delete"><bean:message key="delete"/></td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="9">Loading ...</td>
        </tr>
    </tbody>
</table>
<input type="button" class="button" value="Back to Previous Page" onclick="history.go(-1)"/>