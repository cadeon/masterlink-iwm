<script type='text/javascript' src="dwr/interface/JobSchedules.js"></script>


<!-- this not yet completed. Whole idea is come up with a popup dialog (js confirm like) but with miltiple choices
here we need to ask user: Remove worker from job schedule? Would ypu also like to mark job as manula schedile from now on? -->
<script type="text/javascript" >
    function confirmJobSchedule() {
        if(workSchedule.isAssigned==1)
            $("questiontext").innerHTML = "Remove worker from job schedule?";
        else
            $("questiontext").innerHTML  = "Add worker to job schedule?";

        thePopupManager.showPopup('jobSchedule_confirm');
    }

    function updateJobSchedule(doSave) {
        var rtn = false;
        if(doSave) {
            alert(doSave);
            jobScheduleDataTable.saveItem(workSchedule);
        }
        thePopupManager.hidePopup();
        jobScheduleDataTable.update();  // if change in schedule update the list
    }

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#jobSchedule_confirm{
        width: 400px;
    }

    div#jobSchedule_confirm table.dataTable td{
        font-size:10px;
    }

</style>

<div class="popup" id="jobSchedule_confirm">
    <div class="popupHeader">
        <h2>Workers</h2>
    </div>

    <div class="popupBody">
        <div id="questiontext"></div>
        <input type="button" value="OK" class="button" onClick="updateJobSchedule(true);">
        <input type="button" value="Cancel" class="button" onClick="updateJobSchedule(false);">
    </div>
</div>

<script type="text/javascript">
    new Draggable("jobSchedule_confirm",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
</script>