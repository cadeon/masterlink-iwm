<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

    function showStencilHistory(stencilId) {
        var fillStencilHistoryTable = function (response) {
            DWRUtil.removeAllRows("stencilHistoryTableBody");
            DWRUtil.addRows("stencilHistoryTableBody", response.items, [ function(item){return item.id;},
                    function(item){return item.duration;},
                    function(item){return item.createdDate;},
                    function(item){return item.startedDate;},
                    function(item){return item.completedDate;},
                    function(item){return item.status;}
                    ]);
            thePopupManager.showPopup('projectStencil_history');
        }
        var searchCriteria = new Object();
        searchCriteria.id=stencilId;
        ProjectStencils.getHistory(fillStencilHistoryTable, searchCriteria);
    }

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
#projectStencil_history {
    width: 450px;
    left: -225px;
}

table#stencilHistoryTable {
    width: 400px;
}
table#stencilHistoryTable th,
    table#stencilHistoryTable td {
    font-size: 10px;
}

table#stencilHistoryTable th.idCol {
    width: 60px;
}
table#stencilHistoryTable th.durationCol {
    width: 60px;
}
table#stencilHistoryTable th.startedCol {
}

table#stencilHistoryTable th.completedCol {
}
table#stencilHistoryTable th.statusCol {
    width: 100px;
}
</style>

<!--  -->
<div class="popup" id="projectStencil_history">
    <div class="popupHeader">
        <div class="popupHeader"><h2><span id="stencilHistoryTitle">Project Stencil History</span></h2></div>
    </div>

    <div class="popupBody">
            <form action="" id="StencilHistoryForm">

                <div style="height: 200px; width: 415px;overflow: auto; border: 2px inset; margin-top: 5px;">

                <table id="stencilHistoryTable" class="dataTable">
                    <thead>
                       <tr>
                           <th class="idCol">Stencil Id</th>
                           <th class="durationCol">Duration</th>
                           <th class="createdCol">Created</th>
                           <th class="startedCol">Started</th>
                           <th class="CompletedCol">Completed</th>
                           <th class="statusCol">Status</th>
                       </tr>
                    </thead>

                    <tbody id="stencilHistoryTableBody">
                    </tbody>

                </table>
                </div>
                <input type="button" value="Close" class="button" onClick="thePopupManager.hidePopup();">

            </form>

    </div>
</div>



<script type="text/javascript" >
    new Draggable("projectStencil_history",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
</script>