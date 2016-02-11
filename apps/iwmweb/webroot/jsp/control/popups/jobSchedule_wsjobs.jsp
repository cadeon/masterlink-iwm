<%@ taglib uri="struts-bean" prefix="bean" %>
<script type='text/javascript' src="dwr/interface/JobSchedules.js"></script>

<script type="text/javascript" >
function showJobsForWSchedule(wsId) {
    var fillWSJobsTable = function (item) {
        DWRUtil.removeAllRows("WSJobsTableBody");
        DWRUtil.addRows("WSJobsTableBody", item.jobs, [ function(item){return item.id;}, function(item){return item.createdDate;},
                function(item){return item.description},
                function(item){
                    var hmc= HourMinuteConveter.splitMinutes(item.estTime);
                    return hmc.hours + ':' + hmc.minutes;
                },
                function(item){return item.skillType},
                function(item){return item.status},
                function(item){return item.fullLocator}]);
        thePopupManager.showPopup('jobSchedule_wsjobs');
    }
    JobSchedules.getItem(wsId,{callback:fillWSJobsTable,async:false});
}
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#jobSchedule_wsjobs{
        width: 570px;
    }

    div#jobSchedule_wsjobs table.dataTable td{
       font-size:10px;
    }

</style>

<div class="popup" id="jobSchedule_wsjobs">
    <div class="popupHeader">
        <h2>Jobs</h2>
    </div>

    <div class="popupBody">
        <div style="height: 200px; width: 540px;overflow: auto; border: 2px inset; margin-top: 5px;">

            <table id="WSJobsTable" class="dataTable">
                <colgroup>
                    <col class="jobIdCol" />
                    <col class="createdDateCol" />
                    <col class="descriptionCol" />
                    <col class="estTime" />
                    <col class="skillType" />
                    <col class="status" />
                    <col class="locatorCol" />
                </colgroup>
                <tr>
                    <th ><bean:message key="jobId"/></th>
                    <th >Created</th>
                    <th ><bean:message key="description"/></th>
                    <th ><bean:message key="estTime"/></th>
                    <th ><bean:message key="skillType"/></th>
                    <th ><bean:message key="status"/></th>
                    <th ><bean:message key="location"/></th>
                </tr>
                <tbody id="WSJobsTableBody">
                </tbody>
            </table>
        </div>
        <input type="button" value="OK" class="button" onClick="thePopupManager.hidePopup();">
    </div>
</div>

<script type="text/javascript">
    new Draggable("jobSchedule_wsjobs",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
</script>