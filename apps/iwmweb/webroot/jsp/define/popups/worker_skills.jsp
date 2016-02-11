<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="jstl-core" prefix="c" %>


<script type='text/javascript'>

var WorkerSkillsPopup = Class.extend( AbstractCrudPopup, {

    //abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

    //custom. you can use base class clean most of the time.
    clean: function(){
        $('worker_skills_body').innerHTML='';
        $("SKILLS_WORKER_NAME").innerHTML='';
    },

    //abstract in base class
    show: function(workerId, workerName, isWizard){
        this.theWorker = new Object();
        if(isWizard){
            this.isWizard = isWizard;
        } else {
            this.isWizard = false;
        }

        if(this.isWizard){
            $("skillsSaveButton").value = "Next";
            new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {onSuccess:handlerFunc, onFailure:errFunc});
        } else {
            this.theWorker.id = workerId;
            this.theWorker.name = workerName;

            $("SKILLS_WORKER_NAME").innerHTML=workerName;
            $("skillsSaveButton").value = "Save";
            new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'personId='+this.theWorker.id+'',onSuccess:handlerFunc, onFailure:errFunc});
        }
        thePopupManager.showPopup('worker_skills');


    },

    //abstract in base class
    populate: function(item){

    },

    addSkill: function(skillTypeId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=addSkill&personId='+this.theWorker.id+'&skillTypeId='+skillTypeId+'',onSuccess:handlerFunc, onFailure:errFunc});
    },
    removeSkill: function(skillTypeId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=removeSkill&personId='+this.theWorker.id+'&skillTypeId='+skillTypeId+'',onSuccess:handlerFunc, onFailure:errFunc});
    },
    updateSkillLevel: function (skillTypeId,skillLevelId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=updateSkillLevel&personId='+this.theWorker.id+'&skillTypeId='+skillTypeId+'&skillLevelId='+skillLevelId+'',onSuccess:handlerFunc, onFailure:errFunc});
    },

    save: function(){
        if(this.isWizard){
            this.close();
            theWorkerRightsPopup.show(null, null, true);
        }   else {
            new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=save&personId='+this.theWorker.id,onSuccess:thePopupManager.hidePopup, onFailure:errFunc});
            this.close();
        }
    }
});

var theWorkerSkillsPopup;
callOnLoad(function(){
    theWorkerSkillsPopup = new WorkerSkillsPopup("worker_skills", "SkillsForm", theWorkersTable);
});


//    var Person = new Object();
var handlerFunc = function(t) {
    //alert($('worker_skills_body').innerHTML);
    $('worker_skills_body').innerHTML=t.responseText;

    var theOptions = $('addSkill').options;
    for(var i = 0;i< theOptions.length;i++){
        //alert(theOptions[i].text);
        if("Must Assign Skill" == theOptions[i].text){
            $('addSkill').remove(i);
        }
    }
}

var errFunc = function(t) {
    alert('Error ' + t.status + ' -- ' + t.statusText);
}

        /*
    function getSkills(personId,workerName) {
        Person.id=personId;
        Person.name=workerName;
        $("SKILLS_WORKER_NAME").innerHTML=workerName;

        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'personId='+Person.id+'',onSuccess:handlerFunc, onFailure:errFunc});
    }
    function resetSkills() {
        $('worker_skills_body').innerHTML='';
        $("SKILLS_WORKER_NAME").innerHTML='';
    }
    function addSkill(skillTypeId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=addSkill&personId='+Person.id+'&skillTypeId='+skillTypeId+'',onSuccess:handlerFunc, onFailure:errFunc});
    }

    function saveSkills() {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=save&personId='+Person.id,onSuccess:thePopupManager.hidePopup, onFailure:errFunc});
    }
    function removeSkill(skillTypeId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=removeSkill&personId='+Person.id+'&skillTypeId='+skillTypeId+'',onSuccess:handlerFunc, onFailure:errFunc});
    }

    function updateSkillLevel(skillTypeId,skillLevelId) {
        new Ajax.Request('<c:url value='/WorkerSkillsMaint.do' />', {parameters:'forward=updateSkillLevel&personId='+Person.id+'&skillTypeId='+skillTypeId+'&skillLevelId='+skillLevelId+'',onSuccess:handlerFunc, onFailure:errFunc});
    }
*/
</script>


<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#worker_skills table.dataTable {
        width: 370px;
    }

    div#worker_skills_body {
        height: 400px;
        overflow: auto;
    }

    div#worker_skills {
        width: 400px;
        left: -215px;
    }
</style>


<!-- WORKER SKILLS -->
<div class="popup" id="worker_skills">
    <div class="popupHeader" id="workerSkillsPopupHeader">
        <h2><bean:message key="skills"/>: <span id="SKILLS_WORKER_NAME"></span></h2>
    </div>
    <div class="popupBody">
        <form action="" id="SkillsForm">
            <div id="worker_skills_body"></div>
            <input id="skillsSaveButton" type="button" class="button" value="Save" onclick="theWorkerSkillsPopup.save();"/>
            <input type="button" class="button" value="Cancel" onclick="theWorkerSkillsPopup.close();"/>
        </form>
    </div>
</div>