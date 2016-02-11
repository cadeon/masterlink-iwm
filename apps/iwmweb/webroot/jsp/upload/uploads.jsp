<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Uploads.js"></script>

<script type="text/javascript">

    var UploadsDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols) {
            this.superInit(theDWRObject,theSortCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            return searchCriteria;
        },
        getName:function(item){return item.name;},
        getType:function(item){return item.type;},
        getAction:function(item){
            return '<div class="icon"   onclick="processFile(\''+item.name+'\');" ><img src="images0/execute.gif" height="15px" width="15px" alt="Process" /></div>';
        },
        getLog: function(item){
            if(item.log){
                return '<div class="icon"   onclick="theUploadedContentPopup.show(\''+item.log+'\');" ><img src="images/prtpv_16.gif"  alt="View" /></div>';
            }else{
                return '';
            }
        },
        getLastModified: function(item){return item.lastModified;},
        getDelete: function(item) {
            return '<div class="icon" onclick="theUploadstable.deleteItem(\''+ item.name+'\', \''+item.name + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';
        },
        getView: function(item){
            var rtn;
            if(item.type=='Excel'){
                rtn =  '<span class="icon"   onclick="viewFile(\''+item.name+'\');" ><img src="images/excel.gif"  alt="View" /></span>';
                rtn = rtn + '<span class="icon"   onclick="theUploadedContentPopup.convertExcel(\''+item.name+'\');" ><img src="images/prtpv_16.gif"  alt="View" /></span>';
            }else{
                rtn = '<div class="icon"   onclick="theUploadedContentPopup.show(\''+item.name+'\');" ><img src="images/prtpv_16.gif"  alt="View" /></div>';
            }
            return rtn;
        },
        getFormaters: function(){return [this.getName, this.getType,  this.getLastModified,  this.getAction, this.getView, this.getLog, this.getDelete];}
    });

    var theUploadstable;
    function init() {
        var theConfig = new UploadsDataTableConfig(Uploads, ["name","lastModified"]);
        theUploadstable = new IWMDataTable(theConfig);

        theUploadstable.update();
    }
    callOnLoad(init);

   function viewFile(filename){
        popUp('Uploads.do?forward=view&name='+filename, 'viewfile');
        //location.href='Attachments.do?forward=view&id='+attachId;
    }
    function processFile(filename){
        if(confirm("The file " +filename + " will be processed by the system. Proceed?")){
            var processFileCallback = function(responce){
                if(responce.length>0){
                    alert(responce);
                }
                theUploadstable.update();
            }
            Uploads.processFile(filename,{callback:processFileCallback,async:false});
        }
    }

</script>

<h3></h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<input type="button" class="button" value="New Upload" onclick="location.href ='Upload.do'"/>
<table class="dataTable" id="uploadsTable">
    <colgroup>
        <col class="nameCol" />
        <col class="typeCol" />
        <col class="lastModifiedCol" />
        <col class="processCol" />
        <col class="viewCol" />
        <col class="logCol" />
        <col class="deleteCol" />
    </colgroup>
    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_name'><bean:message key="name"/></td>
            <td >Type</td>
            <td id='sort_lastModified'>Date Loaded</td>
            <td>Process</td>
            <td>View</td>
            <td>Log</td>
            <td>Delete</td>
        </tr>
    </thead>
    <tbody id="dataTableBody">
    </tbody>
</table>

<input type="button" class="button" value="New Upload" onclick="location.href ='Upload.do'"/>
