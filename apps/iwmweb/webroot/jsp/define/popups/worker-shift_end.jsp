<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/Workers.js"></script>

<script type="text/javascript" >
    var TimeSpecsRequest;

    function showShift(personId, wsId){
    	TimeSpecsRequest = new Object();
        TimeSpecsRequest.personId=personId;
        TimeSpecsRequest.workScheduleId=wsId;
        
        var hourMinute=HourMinuteConveter.splitMinutes(0);
        DWRUtil.setValue("shiftHours2",hourMinute.hours);
        DWRUtil.setValue("shiftMinutes2",hourMinute.minutes);
        Workers.getItem(function(item){$("personName2").innerHTML = item.name;},personId);
        thePopupManager.showPopup('worker-shift_end');
    }
    
    function init(){
		new Draggable("worker-shift_end",{handle:"popupHeader",revert:false,starteffect:null,endeffect:null});
    }

    function cleanShift(){
        Form.reset($("WorkerShiftForm"));
    }

    // utility function
    function saveShiftCallback(message){
        if(message && message.length > 0)
            alert(message);
        else{
            closeShift();
            workerCalendarTable.update();
       }
    }

    function closeShift(){
        thePopupManager.hidePopup();
        cleanShift();
    }

    function saveShift() {
    	TimeSpecsRequest.shiftId=   DWRUtil.getValue("CalendarSpecsShiftId");
        TimeSpecsRequest.time=      HourMinuteConveter.toMinutes(DWRUtil.getValue("shiftHours2"),DWRUtil.getValue("shiftMinutes2"));
        TimeSpecs.endShift(saveShiftCallback, TimeSpecsRequest);
        return true;
    }

    callOnLoad(init);
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#worker-shift_end {
        width:320px;
        left: -160px;
    }
    div#worker-shift_end table.calendarDatesTable td {
        padding:0px;
    }
    div#worker-shift_end select {
        width:180px;
    }
    label {
        width: 55px;
    }
</style>

<!-- WORKER LOGIN -->
<div class="popup" id="worker-shift_end">
    <div class="popupHeader">
        <h2>End Shift for <span id="personName2"></span></h2>
    </div>

    <div class="popupBody">

        <form action="" id="WorkerShiftForm" name="WorkerShiftForm">
            <table>
                <!-- tr>
                    <td><label>Shift:</label></td>
                    <td>
                        <html:select property="" styleId="ShiftShiftId" value="" >
                            <html:optionsCollection property="options"  name="ShiftRef"/>
                        </html:select>
                    </td>
                </tr> -->

                <tr>
                    <td valign="top" style="padding-top:5px;"><label>Time:</label></td>
                    <td>
                        <table class="calendarDatesTable">
                            <tr>
                                <td>
                                    <a:ajax id="shiftHours2" name="iwm.spinner"/>
                                </td>
                                <td>
                                    <a:ajax id="shiftMinutes2" name="iwm.spinner"/>
                                </td>
                            </tr>
                            <tr><td style="font-size:10px;">Hours</td><td style="font-size:10px;">Minutes</td></tr>
                        </table>
                    </td>
                </tr>
			</table>
        </form>
            <input type="button" value="Save" class="button" onClick="saveShift();">
            <input type="button" value="Cancel" class="button" onClick="closeShift();">
    </div>
</div>

<html:javascript formName="WorkerShiftForm" dynamicJavascript="true" staticJavascript="false"/>
