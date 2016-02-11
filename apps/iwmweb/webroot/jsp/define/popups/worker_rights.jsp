
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/UserRoles.js"></script>

<script type="text/javascript">


var WorkerRightsPopup = Class.extend( AbstractCrudPopup, {

//abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

//custom. you can use base class clean most of the time.
    clean: function(){
        $("ROLES_WORKER_NAME").innerHTML='';
        DWRUtil.removeAllRows("workerRightsTableBody");
    },

//abstract in base class
    show: function(workerId, workerName, isWizard){
        this.theWorker = new Object();
        if(isWizard){
            this.isWizard = isWizard;
        } else {
            this.isWizard = false;
        }

        //clear out the old table
        DWRUtil.removeAllRows("workerRightsTableBody");

        //anonymous function to help create the rights table
        var getSelected = function(item){
            var onClick =   "onRoleClick(" + item.roleId + ", this.checked );";
            var rtn = '<input disabled="disabled" id="'+escapeJSString(item.desc)+'" onclick="return ' + onClick + ';" type="checkbox"'+(item.isAssigned==1?'checked':'') +'>';

            <%if(request.isUserInRole("AssgnRol")){%>
            var rtn = '<input id="'+escapeJSString(item.desc)+'" onclick="return ' + onClick + ';" type="checkbox"'+(item.isAssigned==1?'checked':'') +'>'
            <%}%>
            return rtn;
        }
        //anonymous function to help create the rights table
        var getDesc = function(item){
            return item.desc;
        }

        //anonymous functiono to help create the rights table
        var fillWorkerRightsTable = function (response) {
            DWRUtil.addRows("workerRightsTableBody", response.items, [ getSelected, getDesc ]);
            thePopupManager.showPopup('workerRights');
        }
        var rolesSearchCriteria = new Object();


        if(this.isWizard){
            rolesSearchCriteria.id=-1;
            $("workerRightsSaveButton").value = "Finish";
            UserRoles.getNewUserData(fillWorkerRightsTable);

        } else {
            this.theWorker.id = workerId;
            this.theWorker.name = workerName;
            $("ROLES_WORKER_NAME").innerHTML=workerName;
            rolesSearchCriteria.id=workerId;
            UserRoles.getData(fillWorkerRightsTable, rolesSearchCriteria);
        }

    },

//abstract in base class
    populate: function(item){

    },

    wizardCallback: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            theWorkersTable.update();            
        }
    },

    save: function(){
        var _this=this;
        if(this.isWizard){
            WorkerCreateWizard.finish(function(message){_this.wizardCallback(message)});
        }   else {
            UserRoles.saveItem(this.theWorker.id);
            this.close();
        }
    }
});

var theWorkerRightsPopup;
callOnLoad(function(){
    theWorkerRightsPopup = new WorkerRightsPopup("workerRights", "RightsForm", theWorkersTable);
    <%if(request.isUserInRole("RstPass")){%>
    
    <%}%>
});

//var rolesSearchCriteria = new Object();

function onRoleClick(roleId,isChecked){
    if(isChecked)
        UserRoles.addItem(roleId);
    else
        UserRoles.deleteItem(roleId);
}



</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

</style>

<!-- WORKER RIGHTS -->
<div class="popup" id="workerRights">
    <div class="popupHeader">
        <h2>Rights: <span id="ROLES_WORKER_NAME"></span></h2>
    </div>

    <div class="popupBody">
        <form id="RightsForm" action="">
            <table id="workerRightsTable">
                <tbody id="workerRightsTableBody">
                </tbody>
            </table>
			<%if(request.isUserInRole("AssgnRol")){%>
            <input type="button" class="button" value="Save" id="workerRightsSaveButton" onclick="theWorkerRightsPopup.save();">
            <%}%>
            <input type="button" class="button" value="Cancel" onclick="theWorkerRightsPopup.close()">

        </form>
    </div>
</div>
