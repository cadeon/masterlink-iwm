<script type="text/javascript" >
    var onSequenceChange;
    function getOrderJobs() {
        var  numOfLevels = 0;
        var getJob=function(item){return item.description;}
        var getSequence= function(item){
            var rtn = "<select id='"+item.id + "' onchange='onSequenceChange(this);'>";
            var selected='';
            for (var i = 0; i < numOfLevels; i++){
                if(item.sequenceLevel==(i+1)) selected='selected'; else selected='';
                rtn += "<option "+selected + ">"+(i+1)+"</option>";
            }
            rtn += '</select>'
            return rtn;
        }
        var getObjectRef = function(item){return item.objectRef}

        onSequenceChange = function(select){
            var seqNumber = DWRUtil.getValue(select);
            ProjectJobs.updateJobSequence(select.id,seqNumber,{callback:fillOrderJobTable});

        }
        var fillOrderJobTable = function (response) {
            numOfLevels=0;
            if(response.items.length>0){
                var lastItem=response.items[response.items.length-1];
                numOfLevels=parseInt(lastItem.sequenceLevel)+1;
            }
            DWRUtil.removeAllRows("orderJobTableBody");
            DWRUtil.addRows("orderJobTableBody", response.items, [getSequence, getJob,getObjectRef]);
        }
        var searchCriteria = new Object();
        ProjectJobs.getOrderedJobs(<%=request.getParameter("projectId")%>,{callback:fillOrderJobTable});
        thePopupManager.showPopup('projectJobs_order');
    }

    function closeOrderJobs(){
        thePopupManager.hidePopup();
        DWRUtil.removeAllRows("orderJobTableBody");
    }

    function saveOrderJobs(){
        saveOrderJobsCallback = function(){
            theJobDataTable.update();
        }
        ProjectJobs.saveOrderedJobs(saveOrderJobsCallback);
        thePopupManager.hidePopup();
    }
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

div#projectJobs_order {
    width: 400px;
    left: -180px;
}
div#projectJobs_order div#orderTableDiv {
    width: 100%;
    height: 180px;
    overflow:auto;
    border: 1px solid #666;
}

</style>


<!--  -->
<div class="popup" id="projectJobs_order">
    <div class="popupHeader">
        <h2>Reorder Jobs</h2>
    </div>


    <div class="popupBody">
        <div id="orderTableDiv">
            <table id="orderTable">
                <tbody id="orderJobTableBody"></tbody>
            </table>
        </div>
        <input type="button" value="Save" class="button" onClick="saveOrderJobs();">
        <input type="button" value="Cancel" class="button" onClick="closeOrderJobs();">
    </div>
</div>

