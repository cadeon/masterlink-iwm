<%@ page import="org.mlink.iwm.dao.SkillTypesCriteria"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>      

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/SkillTypes.js"></script>

 <%
    SkillTypesCriteria cr =    session.getAttribute("SkillTypesCriteria")==null?new SkillTypesCriteria():(SkillTypesCriteria)session.getAttribute("SkillTypesCriteria");
    request.setAttribute("SkillTypesCriteria", cr);
%>

<script type="text/javascript">
    var SkillTypesDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            return null;
        },
        getCode:function(item){return item.code;},
        getDescription:function(item){return item.description;},
        getWorkers: function(item){
            var theIcon = "";
            theIcon += '<div class="icon" onclick="showData(\'Workers\', '+item.id+')">';
            theIcon += '<img src="images/group_16.gif" alt="Workers" />';
            theIcon += '<span class="theNumber">' + item.workerCount + '</span>';
            theIcon += '</div>';
            return theIcon;
            //return item.workerCount;
        },
        getJobCount:function(item){
        	var theIcon = "";
            theIcon += '<div class="icon" onclick="showData(\'Jobs\', '+item.id+')">';
            theIcon += '<img src="images/group_16.gif" alt="Jobs" />';
            theIcon += '<span class="theNumber">' + item.jobCount + '</span>';
            theIcon += '</div>';
            return theIcon;
            //return item.jobCount;
        },
        getTemplateCount:function(item){
        	var theIcon = "";
            theIcon += '<div class="icon" onclick="showData(\'Templates\', '+item.id+')">';
            theIcon += '<img src="images/group_16.gif" alt="Templates" />';
            theIcon += '<span class="theNumber">' + item.templateCount + '</span>';
            theIcon += '</div>';
            return theIcon;
            //return item.templateCount;
        },
    	getDelete: function(item) {
        	if(item.jobCount==0 && item.templateCount==0){
        	    return '<div class="icon" onclick="theSkillTypesDataTable.deleteItem('+item.id+',\''+item.description+'\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';
        	}else{
        		return '<div class="icon"><img src="images/trash_16.gif"  alt="Delete" /></div>';
        	}
        },
    	getFormaters: function(){return [this.getCode, this.getDescription, this.getWorkers, this.getJobCount, this.getTemplateCount, this.getDelete];}
    });

    var theSkillTypesDataTable;
    
    function init() {
        //setup the dataTable
        theConfig = new SkillTypesDataTableConfig(SkillTypes, ["code","description"]);
        theSkillTypesDataTable = new IWMDataTable(theConfig);

        theSkillTypesDataTable.update();
    }
    callOnLoad(init);

</script>
<br/>

<!-- Table showing locators that match the filter -->
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />

<input type="button" class="button" value="Add Skill Type" onclick="theSkillTypeEditPopup.show(null, true);"/>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="skillTypeDataTable">

    <colgroup>
        <col class="codeCol" />
        <col class="descriptionCol" />
        <col class="workerCountCol" />
        <col class="jobCountCol" />
        <col class="templateCountCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table code for sorting --%>
            <td class="codeCol" id='sort_code'><bean:message key="typeCode"/></td>
            <td class="descriptionCol" id='sort_description'><bean:message key="description"/></td>
            <td class="countCol" id='sort_value'><bean:message key="workerCount"/></td>
            <td class="countCol" id='sort_value'><bean:message key="jobCount"/></td>
            <td class="countCol" id='sort_value'><bean:message key="templateCount"/></td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td colspan="8">Loading ...</td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add Skill Type" onclick="theSkillTypeEditPopup.show(null, true);"/>
