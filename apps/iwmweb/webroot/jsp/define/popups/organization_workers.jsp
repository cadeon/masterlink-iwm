<script type='text/javascript' src="dwr/interface/Workers.js"></script>

<script type="text/javascript" >
function showWorkers(orgId) {
    var fillWorkersTable = function (response) {
        DWRUtil.removeAllRows("WorkersTableBody");
        DWRUtil.addRows("WorkersTableBody", response.items, [ function(item){return item.name;}, function(item){return item.active==1?'Yes':'No';}]);
        thePopupManager.showPopup('organization_workers');
    }
    var searchCriteria = new Object();
    searchCriteria.id=orgId;
    Workers.getData(fillWorkersTable, searchCriteria, 0, 1000, 'name','ASC');
}
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#organization_workers {
        width: 300px;
    }
    div#organization_workers table#workersTable {
        width: 100%;
    }

</style>

<div class="popup" id="organization_workers">
    <div class="popupHeader">
        <h2>Workers</h2>
    </div>

    <div class="popupBody">
        <div style="height: 300px; width: 270px;overflow: auto; border: 2px inset; margin-top: 5px;">

            <table id="workersTable" class="dataTable">
                <tr>
                    <th >Name</th>
                    <th >Active</th>
                </tr>
                <tbody id="WorkersTableBody">
                </tbody>
            </table>
        </div>
        <input type="button" value="Cancel" class="button" onClick="thePopupManager.hidePopup();">
    </div>
</div>

<script type="text/javascript">
new Draggable("organization_workers",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});    
</script>