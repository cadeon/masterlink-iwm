<script type="text/javascript" >
    var onSequenceChange;
    function getOrderActions() {
        //var  numberOfActions = 0;
        var  maxSequence = 0;
        var getAction=function(item){return item.verb + " "+ item.name + " "+ item.modifier;}
        var getSequence= function(item){
            var rtn = "<select id='"+item.id + "' onchange='onSequenceChange(this);'>";
            var selected='';
            for (var i = 0; i < maxSequence; i++){
                if(item.sequence==(i+1)) selected='selected'; else selected='';
                rtn += "<option "+selected + ">"+(i+1)+"</option>";
            }
            rtn += '</select>'
            return rtn;
        }
        onSequenceChange = function(select){
            var seqNumber = DWRUtil.getValue(select);
            TemplateTaskActions.updateActionSequence(select.id,seqNumber,{callback:fillOrderActionTable});

        }
        var fillOrderActionTable = function (response) {
            //numberOfActions=response.items.length;
            for (var i = 0; i < response.items.length; i++){
                var sequence = parseInt(response.items[i].sequence);
                if(sequence>maxSequence) maxSequence=sequence;
            }
            DWRUtil.removeAllRows("orderActionTableBody");
            DWRUtil.addRows("orderActionTableBody", response.items, [getSequence, getAction]);
        }
        var searchCriteria = new Object();
        TemplateTaskActions.getOrderedActions(<%=request.getParameter("taskId")%>,{callback:fillOrderActionTable});
        thePopupManager.showPopup('templates-tasks-actions_order');
    }

    function closeOrderActions(){
        thePopupManager.hidePopup();
        DWRUtil.removeAllRows("orderActionTableBody");

        $("templates-tasks-actions_order").style.top = "18px";
        $("templates-tasks-actions_order").style.marginLeft = "50%";
        $("templates-tasks-actions_order").style.left = "-125px";

    }

    function saveOrderActions(){
        saveOrderActionsCallback = function(){
            dataTable.update();
        }
        TemplateTaskActions.saveOrderedActions(saveOrderActionsCallback);
        thePopupManager.hidePopup();
    }
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

</style>


<!--  -->
<div class="popup" id="templates-tasks-actions_order">
    <div class="popupHeader">
        <h2>Reorder Actions</h2>
    </div>


    <div class="popupBody">
        <table id="orderTable">
            <tbody id="orderActionTableBody"></tbody>
        </table>
        <input type="button" value="Save" class="button" onClick="saveOrderActions();">
        <input type="button" value="Cancel" class="button" onClick="closeOrderActions();">
    </div>
</div>

<script type="text/javascript" >
    new Draggable("templates-tasks-actions_order",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
</script>