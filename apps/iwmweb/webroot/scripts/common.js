if(!myonloads){
    var myonloads = new Array();
}
function executeOnloads() {
    setTimeout(function(){},1000);
    for (var i = 0; i < myonloads.length; i++){
        //alert("executing "+myonloads[i] );
        myonloads[i]();
        }
}

function callOnLoad(load) {
    myonloads[myonloads.length]=load;
    for (var i = 0; i < myonloads.length; i++){
        //alert("onloads"+i +" "+myonloads[i] );
        }
}

function _callOnLoad(load) {
    //alert("adding to callOnLoad "+load)
    if (window.addEventListener){
        window.addEventListener("load", load, false);
    }
    else if (window.attachEvent) {
        window.attachEvent("onload", load);
    }
    else {
        window.onload = load;
    }
}

_callOnLoad(executeOnloads);

function stopEventPropagation(event){
    if(window.event){
        window.event.cancelBubble;
    }
    if(event && event.stopPropagation){
       event.stopPropagation(); 
    }
}

function isAlreadySubmitted(){
// doubleclick save submission
    if ( typeof document.__documentAlreadySubmitted == 'undefined' ) {
        document.__documentAlreadySubmitted = 0;
    } else {
        document.__documentAlreadySubmitted++;
    }


    if ( document.__documentAlreadySubmitted > 0 ){
        alert("Please wait! Request is already being processed");
        return true;
    }
    else{
        return false;
    }
}

function resetAlreadySubmitted(){
    document.__documentAlreadySubmitted = 'undefined';
}

log = function(msg){
    if(self.console){console.log(msg);}
    else {
        /*alert(msg);*/
    }
}

function submitForm(form){
    if(!isAlreadySubmitted()){
        form.submit();
    }
}

function popUp(URL,winName, h, w) {
    parentWin=top.window;
	var options = "toolbar=no";
	options    += ",location=no";
	options    += ",status=no";
	options    += ",menubar=no";
	options    += ",scrollbars=yes";
	options    += ",resizable=yes";
	options    += ",width=" + w ;
	options    += ",height=" + h;
	options    += ",screenX=50";
	options    += ",screenY=50";
	options    += ",top=150";
	options    += ",left=150";

    var newWin = window.open( URL , winName, options );
   parentWin.popup = newWin;
   newWin.focus();
   newWin.creator = parentWin;
   newWin.opener = parentWin;
}
function openWindow(URL,winName, h, w) {
	var options = "toolbar=no";
	options    += ",location=no";
	options    += ",status=no";
	options    += ",menubar=no";
	options    += ",scrollbars=yes";
	options    += ",resizable=yes";
	options    += ",width=" + w ;
	options    += ",height=" + h;
	options    += ",screenX=50";
	options    += ",screenY=50";
	options    += ",top=150";
	options    += ",left=150";

    window.open( URL , winName, options );
}

function convertDateToString(kou) {
    var converted=  (kou.getMonth()+101).toString().substring(1,3)+'/'+(kou.getDate()+100).toString().substring(1,3)+'/'+kou.getFullYear();
    return converted;
}

function convertStringToDate(dateStr) {
    var day = dateStr.substring(0,2);
    var month = dateStr.substring(3,5);
    var year = dateStr.substring(6,10);
    var date = new Date(); date.setDate(day); date.setMonth(month);date.setYear(year);
    return date;
}



HourMinuteConveter = new Object();
HourMinuteConveter.splitMinutes = function(durationInMinutes){
    var hm = new Object();
    hm.minutes = (durationInMinutes % 60 + 100).toString().substring(1,3);
    hm.hours = (durationInMinutes-hm.minutes)/60;
    return hm;
}
HourMinuteConveter.toMinutes = function(hours,minutes){
    return eval(hours)*60 + eval(minutes);
}

function FormValuesUtil() {
    // DWRUtil's getValue and setValue is overriden to have element as parameter insted of element id
    // this helps to localize element to particular section (form) and more suitable to our multi-popup design
}
FormValuesUtil.setFormValues= function(form,map){
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].id.length>0) {
            for (var property in map) {
                if (property==elements[i].id) {
                    DWRUtil.setValue(elements[i], map[property]);
                }
            }
        }
    }
}

FormValuesUtil.getFormValues= function(form, map){
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].id.length>0) {
                map[elements[i].id] = DWRUtil.getValue(elements[i]);
        }
    }
    return map;
}
FormValuesUtil.getSubmittableFormValues= function(form, map){
    var elements = Form.getElements(form);
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].id.length>0) {
            if(!elements[i].disabled) {
                map[elements[i].id] = DWRUtil.getValue(elements[i]);
            }
        }
    }
    return map;
}

function escapeJSString(arg) {
    arg = arg.replace(/\\/g, '\\\\'); //escape \
    arg = arg.replace(/\'/g, "\\'");  //escape '
    return arg;
}


