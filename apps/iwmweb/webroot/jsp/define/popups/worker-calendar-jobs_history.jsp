<script type='text/javascript' src="dwr/interface/JobHistory.js"></script>

<script type="text/javascript" >
function showJobHistory(jobId) {
    var fillJobHistoryTable = function (response) {
        DWRUtil.removeAllRows("jobHistoryTable");
        DWRUtil.addRows("jobHistoryTable", response.items, [ function(item){return item.dayDisplay;}, function(item){return item.name;}, function(item){return item.totalTime;}]);
    }
    var searchCriteria = new Object();
    searchCriteria.id=jobId;
    JobHistory.getData(fillJobHistoryTable, searchCriteria);
    thePopupManager.showPopup('worker-calendar-jobs_history');
}
    function closeJobHistory(){
        thePopupManager.hidePopup();
        DWRUtil.removeAllRows("jobHistoryTable");
    }
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#worker-calendar-jobs_history {
        width: 420px;
    }
    div#worker-calendar-jobs_history table#historyTable {
        width: 100%;
    }
</style>

<div class="popup" id="worker-calendar-jobs_history">
    <div class="popupHeader">
        <h2>Job History</h2>
    </div>

    <div class="popupBody">
        <div style="height: 300px; width: 380px;overflow: auto; border: 2px inset; margin-top: 5px;">

            <table id="historyTable" class="dataTable">
                <tr>
                    <th class="date">Date</th>
                    <th class="worker">Worker</th>                    
                    <th class="hours">Hours</th>
                    <!--th class="status">Status</th-->
                </tr>
                <tbody id="jobHistoryTable">
                </tbody>
            </table>
        </div>
        <input type="button" value="Cancel" class="button" onClick="closeJobHistory();">
    </div>
</div>