<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/Attachments.js"></script>

<script type="text/javascript">
    function showAttachment(attachId){
        //'popUp('Attachments.do?forward=view&id='+attachId, 'attachment');
        location.href='Attachments.do?forward=view&id='+attachId;
    }

    var attachmentsDataTableConfig = Class.extend( DataTableConfig, {
        initialize: function(theDWRObject, theSortCols, theToolTipCols) {
            this.superInit(theDWRObject,theSortCols, theToolTipCols);
        },
        getSearchCriteria: function(){
            var searchCriteria = new Object();
            return searchCriteria;
        },
        getId:function(item){return item.id;},
        getName:function(item){return item.filename;},
        getDesc:function(item){return item.description;},
        getCreatedDate:function(item){return item.createdDate;},
        getSize:function(item){return item.size;},
        getLastModified: function(item){return item.lastModified;},
        getDelete: function(item) {
            return '<div class="icon" onclick="theattachmentstable.deleteItem(\''+ item.id+'\', \''+item.filename + '\');"><img src="images/trash_16.gif"  alt="Delete" /></div>';
        },
        getView: function(item){
            return '<div class="icon"   onclick="showAttachment(\''+item.id+'\');" ><img src="images/prtpv_16.gif"  alt="View" /></div>';
        },
        getFormaters: function(){return [this.getId,this.getName, this.getDesc, this.getCreatedDate, this.getSize, this.getView, this.getDelete];}
    });

    var theattachmentstable;
    function init() {
        var theConfig = new attachmentsDataTableConfig(Attachments, ["id","filename","description","createdDate","size"],[1,2]);
        theattachmentstable = new IWMDataTable(theConfig);

        theattachmentstable.update();
    }
    callOnLoad(init);

</script>

<h3></h3>
<a:ajax id="pagemeter"  type="iwm.pagenavigator"  name="iwm.pagenavigator" />
<!--input type="button" class="button" value="Upload File" onclick="location.href ='Upload.do'"/-->
<input type="button" class="button" value="Upload File" onclick="theAttachmentUploadPopup.show();"/>


<table class="dataTable" id="attachmentsTable">
    <colgroup>
        <col class="idCol" />
        <col class="nameCol" />
        <col class="descriptionCol" />
        <col class="createdDateCol" />
        <col class="sizeCol" />
        <col class="viewCol" />
        <col class="deleteCol" />
    </colgroup>
    <thead>
        <tr>
            <td class="idCol" id='sort_id'><bean:message key="id"/></td>
            <td class="nameCol" id='sort_filename'><bean:message key="name"/></td>
            <td class="descriptionCol" id='sort_description'><bean:message key="description"/></td>
            <td class="createdDateCol" id='sort_createdDate'><bean:message key="createdDate"/></td>
            <td class="sizeCol" id='sort_size'>Size</td>
            <td class="viewCol" >View</td>
            <td class="deleteCol" >Delete</td>
        </tr>
    </thead>
    <tbody id="dataTableBody">
    </tbody>
</table>

