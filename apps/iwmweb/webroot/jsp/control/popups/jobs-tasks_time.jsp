<script type='text/javascript' src="dwr/interface/JobTaskTimes.js"></script>

<script type="text/javascript" >
function showJobTaskTimes(taskId) {
    var fillJobTaskTimesTable = function (response) {
        DWRUtil.removeAllRows("jobTaskTimesTableBody");
        DWRUtil.addRows("jobTaskTimesTableBody", response.items, [function(item){return item.name;}, function(item){return item.totalTime;},function(item){return item.dayDisplay;} ]);
    }
    searchCriteria = new Object();
    searchCriteria.id=taskId;
    JobTaskTimes.getData(fillJobTaskTimesTable, searchCriteria);
    thePopupManager.showPopup('jobs-tasks_time');
}
    function closeJobTaskTimes(){
        thePopupManager.hidePopup();
        DWRUtil.removeAllRows("jobTaskTimesTableBody");
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
<div class="popup" id="jobs-tasks_time">
<div class="popupHeader">
    <h2>Task time</h2>
</div>

<div class="popupBody">
        <table class="dataTable">
            <tr>
                <th >Worker</th>
                <th >Time</th>
                <th >Date</th>
            </tr>
            <tbody id="jobTaskTimesTableBody"></tbody>
        </table>
        <input type="button" value="Cancel" class="button" onClick="closeJobTaskTimes();">
</div>
</div>

