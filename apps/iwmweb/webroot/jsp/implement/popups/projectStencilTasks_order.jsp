<script type="text/javascript" >
    var onSequenceChange;
    function getOrderTasks() {
        var  numOfLevels = 0;
        var getJob=function(item){return item.description;}
        var getSequence= function(item){
            var rtn = "<select id='"+item.displayOrder + "' onchange='onSequenceChange(this);'>";
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
            ProjectStencilTasks.updateTaskSequence(select.id,seqNumber,{callback:fillOrderTaskTable});

        }
        var fillOrderTaskTable = function (response) {
            numOfLevels=0;
            if(response.items.length>0){
                var lastItem=response.items[response.items.length-1];
                numOfLevels=parseInt(lastItem.sequenceLevel)+1;
            }
            DWRUtil.removeAllRows("orderTaskTableBody");
            DWRUtil.addRows("orderTaskTableBody", response.items, [getSequence, getJob,getObjectRef]);
        }
        var searchCriteria = new Object();
        ProjectStencilTasks.getOrderedTasks(<%=request.getParameter("id")%>,{callback:fillOrderTaskTable});
        thePopupManager.showPopup('projectStencilTasks_order');
    }

    function closeOrderTasks(){
        thePopupManager.hidePopup();
        DWRUtil.removeAllRows("orderTaskTableBody");
        $("projectStencilTasks_order").style.top = "18px";
        $("projectStencilTasks_order").style.marginLeft = "50%";
        $("projectStencilTasks_order").style.left = "-180px";
    }

    function saveOrderTasks(){
        saveOrderTasksCallback = function(){
            dataTable.update();
        }
        ProjectStencilTasks.saveOrderedTasks(saveOrderTasksCallback);
        thePopupManager.hidePopup();
    }
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

div#projectStencilTasks_order {
    width: 400px;
    left: -180px;
}
div#projectStencilTasks_order div#orderTableDiv {
    width: 100%;
    height: 180px;
    overflow:auto;
    border: 1px solid #666;
}

</style>


<!--  -->
<div class="popup" id="projectStencilTasks_order">
    <div class="popupHeader">
        <h2>Reorder Tasks</h2>
    </div>


    <div class="popupBody">
        <div id="orderTableDiv">
            <table id="orderTable">
                <tbody id="orderTaskTableBody"></tbody>
            </table>
        </div>
        <input type="button" value="Save" class="button" onClick="saveOrderTasks();">
        <input type="button" value="Cancel" class="button" onClick="closeOrderTasks();">
    </div>
</div>

<script type="text/javascript" >
    new Draggable("projectStencilTasks_order",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
</script>