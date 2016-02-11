<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/Workers.js"></script>


<script type="text/javascript" >
    var calendarAddlocatorFilter;
    var TimeSpecsRequest;

    function showCalendarSpecs(personId, locatorId){
        TimeSpecsRequest = new Object();
        TimeSpecsRequest.personId=personId;
        if(!locatorId) locatorId=null;
        calendarAddlocatorFilter.populateChain(locatorId);

        var endDate = new Date();
        endDate.setMonth(endDate.getMonth()+1);

        DWRUtil.setValue("startDate",convertDateToString(new Date()));
        DWRUtil.setValue("endDate",convertDateToString(endDate));

        var hourMinute=HourMinuteConveter.splitMinutes(480);
        DWRUtil.setValue("shiftHours",hourMinute.hours);
        DWRUtil.setValue("shiftMinutes",hourMinute.minutes);
        Workers.getItem(function(item){$("personName").innerHTML = item.name;},personId)
        thePopupManager.showPopup('worker-calendar_add');
    }
    function init(){
        calendarAddlocatorFilter = new LocatorChain("calendarAddLocator",
                function (){
                    //define what to do on callback
                },
                false, true, 1);

        new Draggable("worker-calendar_add",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});

    }

    function cleanCalendarSpecs(){
        Form.reset($("CalendarSpecsForm"));
    }

    // utility function
    function saveCalendarSpecsCallback(message){
        if(message && message.length > 0)
            alert(message);
        else{
            closeCalendarSpecs();
            workerCalendarTable.update();
       }
    }

    function closeCalendarSpecs(){
        thePopupManager.hidePopup();
        cleanCalendarSpecs();
    }

    function saveCalendarSpecs() {
        if(validateCalendarSpecsForm($("CalendarSpecsForm"))){
            TimeSpecsRequest.monday =   DWRUtil.getValue("monday");
            TimeSpecsRequest.tuesday=   DWRUtil.getValue("tuesday");
            TimeSpecsRequest.wednesday= DWRUtil.getValue("wednesday");
            TimeSpecsRequest.thursday=  DWRUtil.getValue("thursday");
            TimeSpecsRequest.friday=    DWRUtil.getValue("friday");
            TimeSpecsRequest.saturday=  DWRUtil.getValue("saturday");
            TimeSpecsRequest.sunday=    DWRUtil.getValue("sunday");

            TimeSpecsRequest.startDate= DWRUtil.getValue("startDate");
            TimeSpecsRequest.endDate=   DWRUtil.getValue("endDate");
            TimeSpecsRequest.shiftId=   DWRUtil.getValue("CalendarSpecsShiftId");
            TimeSpecsRequest.time=      HourMinuteConveter.toMinutes(DWRUtil.getValue("shiftHours"),DWRUtil.getValue("shiftMinutes"));
            TimeSpecsRequest.locatorId=calendarAddlocatorFilter.currentSelectedId;
            TimeSpecs.saveItem(saveCalendarSpecsCallback, TimeSpecsRequest);
            return true;
        }
        else
            return false;
    }

    callOnLoad(init);
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#worker-calendar_add {
        width:320px;
        left: -160px;
    }
    div#worker-calendar_add table.calendarDatesTable td {
        padding:0px;
    }
    div#worker-calendar_add select {
        width:180px;
    }
    div#worker-calendar_add div.filterDiv select {
        margin: 0px;
        padding: 0px;
    }

    div.daySelect {
        width: 45px;
        display: inline;
    }
    label {
        width: 55px;
    }

</style>


<!-- WORKER LOGIN -->
<div class="popup" id="worker-calendar_add">
    <div class="popupHeader">
        <h2>Add to <span id="personName"></span> Calendar</h2>
    </div>

    <div class="popupBody">

        <form action="" id="CalendarSpecsForm" name="CalendarSpecsForm">
            <table>
                <tr>
                    <td><label>Location:</label></td>
                    <td>
                        <a:ajax  id="calendarAddLocator" type="iwm.filter" name="iwm.filter"/>
                    </td>
                </tr>
				<!-- 
                <tr>
                    <td><label>Shift:</label></td>
                    <td>
                        <html:select property="" styleId="CalendarSpecsShiftId" value="" >
                            <html:optionsCollection property="options"  name="ShiftRef"/>
                        </html:select>
                    </td>
                </tr>
				-->
				<input type="hidden" id="CalendarSpecsShiftId" value="1068">
                <tr>
                    <td valign="top" style="padding-top:5px;"><label>Time:</label></td>
                    <td>
                        <table class="calendarDatesTable">
                            <tr>
                                <td>
                                    <a:ajax id="shiftHours" name="iwm.spinner"/>
                                </td>
                                <td>
                                    <a:ajax id="shiftMinutes" name="iwm.spinner"/>
                                </td>
                            </tr>
                            <tr><td style="font-size:10px;">Hours</td><td style="font-size:10px;">Minutes</td></tr>
                        </table>



                        <!--
                        <input type="text" style="width:45px;" id="shiftHours">
                        Minutes:<input type="text" style="width:45px;" id="shiftMinutes">
                        -->
                    </td>
                </tr>

                <tr>
                    <td valign="top" style="padding-top:5px;"><label>Dates:</label></td>
                    <td>
                        <table class="calendarDatesTable">
                            <tr>
                                <td>
                                    <a:ajax id="startDate"  type="iwm.calendar" name="iwm.calendar" />
                                </td>
                                <td>
                                    <a:ajax id="endDate"  type="iwm.calendar" name="iwm.calendar" />
                                </td>
                            </tr>
                            <tr><td style="font-size:10px;">Start</td><td style="font-size:10px;">End</td></tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td valign="top" style="padding-top:5px;"><label>Days:</label></td>
                    <td>
                        <div>
                            <div class="daySelect"><input type="checkbox" id="monday" checked>Mon</div>
                            <div class="daySelect"><input type="checkbox" id="tuesday" checked>Tue</div>
                            <div class="daySelect"><input type="checkbox" id="wednesday" checked>Wed</div>
                            <div class="daySelect"><input type="checkbox" id="thursday" checked>Thu</div>
                            <div class="daySelect"><input type="checkbox" id="friday" checked>Fri</div>
                        </div>

                        <div>
                            <div class="daySelect"><input type="checkbox" id="saturday">Sat</div>
                            <div class="daySelect"><input type="checkbox" id="sunday">Sun</div>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
            <input type="button" value="Save" class="button" onClick="saveCalendarSpecs();">
            <input type="button" value="Cancel" class="button" onClick="closeCalendarSpecs();">
    </div>
</div>

<html:javascript formName="CalendarSpecsForm" dynamicJavascript="true" staticJavascript="false"/>
