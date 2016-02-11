<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<script type="text/javascript" >
    var Template;
    var ServicePlan;
    function readCalendar(templateId){
        Template = new Object();
        Template.id=templateId;
        var fillTemplateForm = function(item){
            ServicePlan = item.servicePlan;
            DWRUtil.setValues(ServicePlan);
            $("CAL_DESC").innerHTML=item.classDesc;
        }

        Templates.getItem(fillTemplateForm,templateId);
        thePopupManager.showPopup('templates-tasks_calendar');
    }

    function cleanCalendar(){
        Form.reset($("CalendarForm"));
        $("CAL_DESC").innerHTML='';
    }

    // utility function
    function saveCalendarCallback(message){
        if(message && message.length > 0)
            alert(message);
        else{
            closeCalendar();
        }
    }

    function closeCalendar(){
        thePopupManager.hidePopup();
        cleanCalendar();
        $("templates-tasks_calendar").style.top = "18px";
        $("templates-tasks_calendar").style.marginLeft = "50%";
        $("templates-tasks_calendar").style.left = "-215px";
    }

    function saveCalendar() {
        //if(validateCalendarForm($("CalendarForm"))){
            DWRUtil.getValues(ServicePlan);
            ServicePlan.id=Template.id;
            Templates.saveItem(saveCalendarCallback, ServicePlan);
        //}
    }

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
div#templates-tasks_calendar {
    width: 420px;
}
</style>

<!--  -->
<div class="popup" id="templates-tasks_calendar">
<div class="popupHeader">
    <h2>Service Calendar <span id="CAL_DESC"></span></h2>
</div>

<div class="popupBody">
    <form action="" name="CalendarForm" id="CalendarForm">
        <div class="sectionComment">
            The system will enforce planning restrictions based on the service calendar.  Please,
            set the periods in which this task may be planned below.
        </div>

        <h3>Service Seasons</h3>
        <table class="formTable" >
            <tr>
                <td><input type="checkbox" checked id="spring">Spring <span class="season">(March, 22 - June, 21)</span></td>
            </tr>
            <tr>
                <td><input type="checkbox" checked id="summer">Summer <span class="season">(June, 22 - August, 21)</span></td>
            </tr>
            <tr>
                <td><input type="checkbox" checked id="fall">Fall <span class="season">(August, 22 - December, 21)</span></td>
            </tr>
            <tr>
                <td><input type="checkbox" checked id="winter">Winter <span class="season">(December, 22 - March, 21)</span></td>
            </tr>
        </table>

        <h3>Service Months</h3>
        <table class="formTable" >
            <tr>
                <td width="53px"><input type="checkbox" checked id="january">Jan</td>
                <td width="53px"><input type="checkbox" checked id="february">Feb</td>
                <td width="53px"><input type="checkbox" checked id="march">Mar</td>
                <td width="53px"><input type="checkbox" checked id="april">Apr</td>
                <td width="53px"><input type="checkbox" checked id="may">May</td>
                <td width="53px"><input type="checkbox" checked id="june">Jun</td>
            </tr>

            <tr>
                <td><input type="checkbox" checked id="july">Jul</td>
                <td><input type="checkbox" checked id="august">Aug</td>
                <td><input type="checkbox" checked id="september">Sept</td>
                <td><input type="checkbox" checked id="october">Oct</td>
                <td><input type="checkbox" checked id="november">Nov</td>
                <td><input type="checkbox" checked id="december">Dec</td>
            </tr>
        </table>

        <%/*
        <h3 style="display:inline;">Custom Service Date Range </h3>
        <table class="formTable">
            <tr>
                <td>From:<a:ajax id="rangeStartDate"  type="iwm.calendar" name="iwm.calendar" /></td>
                <td>To:<a:ajax id="rangeEndDate"  type="iwm.calendar" name="iwm.calendar" /> </td>
            </tr>
        </table*/%>
        <h3>Service Days</h3>
        <table class="formTable">
            <tr>
                <td width="53px"><input type="checkbox" id="sunday">Sun</td>
                <td width="53px"><input type="checkbox"  id="monday" >Mon</td>
                <td width="53px"><input type="checkbox"  id="tuesday" >Tue</td>
                <td width="53px"><input type="checkbox"  id="wednesday">Wed</td>
                <td width="53px"><input type="checkbox"  id="thursday" >Thr</td>
                <td width="53px"><input type="checkbox"  id="friday">Fri</td>
                <td width="53px"><input type="checkbox" id="saturday" >Sat</td>
            </tr>
        </table>
    </form>

        <input type="button" value="Save" class="button" onclick="saveCalendar()">
        <input type="button" value="Cancel" class="button" onClick="closeCalendar();">
</div>
</div>

<!--html:javascript formName="CalendarForm" dynamicJavascript="true" staticJavascript="false"/-->


<script type="text/javascript">
function validateDateRange(){
    var startDate,endDate;
    if($('rangeStartDate').value.length>0) startDate = convertStringToDate($('rangeStartDate').value);
    if($('rangeEndDate').value.length>0) endDate = convertStringToDate($('rangeEndDate').value);
    //todo: add date validation so startDate is <=endDate

}
new Draggable("templates-tasks_calendar",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});

</script>


