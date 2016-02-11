

//
// Function: load()
// Called by HTML body element's onload event when the web application is ready to start
//

function resetApp()
{
if(document.getElementById('username')){document.getElementById('username').value="";}
if(document.getElementById('password')){document.getElementById('password').value="";}
Worker = JSON.parse("{\"workScheduleOps\":[]}");
scheduleTouched = 0;
jobTouched=0;
taskTouched=0;
//grab a new loc
navigator.geolocation.getCurrentPosition(geoWorker);
}

loginpress=false;
url="/MWSync.do";
resetApp();

function load()
{
    dashcode.setupParts();
    resetApp();
}

function geoWorker(location)
{
	//update location if the accuracy is good, otherwise null
	if(location.coords.accuracy <= 7000){
	Worker.latitude=location.coords.latitude;
	Worker.longitude=location.coords.longitude;
	Worker.accuracy=location.coords.accuracy;
	} else {
	//This data's no good- accuracy=0 tells the server to not log it
	Worker.accuracy=0;
	}
}
	
function Talk()
{ //does the actual syncing
    // Build worker object and submit via JSON
    params="worker="+JSON.stringify(Worker);
	params=params.replace(/&/g,'\\u0026');
	
    http= new XMLHttpRequest();
    http.open("POST", url, true);
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

http.onreadystatechange = function() {//Call a function when the state changes.
	if(http.readyState == 4 && http.status == 200) {
        parseDownload(http.responseText);
    }
}
    http.send(params);
}
	

function DoSync()
{
	//We're syncing, so send some geo info.
	navigator.geolocation.getCurrentPosition(geoWorker,{maximumAge: 300000, timeout:5000});
	//Wait a bit and then do the talking
	setTimeout("Talk()",5000);	
}

function parseDownload(responseText)
{
Worker=JSON.parse(responseText);
if (Worker.id==0) {
	alert("Username or password incorrect");	
}else{
    //Reload the anything of interest
	tlist= document.getElementById('ScheduleList');		
	tlist.object.reloadData();
	tlist= document.getElementById('JobsList');		
	tlist.object.reloadData();
	//Reset the Action Page (we may be on it)
	//TODO
    //if we're on the login screen, go forward. Otherwise do nothing.
    if (loginpress){
	goForward("Schedule",Worker.name);
	loginpress=false;
    }
}

}


function updateUsername(event)
{
    Worker.username=event.target.value;
}


function updatePassword(event)
{
    Worker.password=event.target.value
}

function login(event)
{	
	loginpress=true;
	//Give core location some time to do it's thing
	Talk();
}

function goForward(page, title)
{		
			view=document.getElementById(page+"View");
			list=document.getElementById(page+"List");
			list.object.reloadData();
			var browser = document.getElementById('browser').object;
			browser.goForward(view, title);
}

//List data sources

var ScheduleListDataSource = {
	// The List calls this method to find out how many rows should be in the list.
	numberOfRows: function() {
		return Worker.workScheduleOps.length;
	},
	
	// The List calls this method once for every row.
	prepareRow: function(rowElement, rowIndex, templateElements) {
		// templateElements contains references to all elements that have an id in the template row.
		// Ex: set the value of an element with id="label".
		if (templateElements.label) {
			templateElements.label.innerText = Worker.workScheduleOps[rowIndex].location;
			templateElements.time.innerText = Worker.workScheduleOps[rowIndex].stime + " to " +Worker.workScheduleOps[rowIndex].etime;
			templateElements.numberOfJobs.innerText = Worker.workScheduleOps[rowIndex].jobOps.length;
		}

		// Assign a click event handler for the row.
		rowElement.onclick = function(event) {
			// Do something interesting
			if (Worker.workScheduleOps[rowIndex].jobOps.length > 0){
			scheduleTouched=rowIndex;
			goForward("Jobs",Worker.workScheduleOps[rowIndex].location);
			}
		};
	}
};

var JobsListDataSource = {
	// The List calls this method to find out how many rows should be in the list.
	numberOfRows: function() {
	if(Worker.workScheduleOps.length>0){
		//count the non-DUN jobs
		rows=0;
		for (i=0; i<Worker.workScheduleOps[scheduleTouched].jobOps.length; i++){
			if (Worker.workScheduleOps[scheduleTouched].jobOps[i].status !="DUN"){
			rows++;
			}
		}
		return rows;
		}
	},
	
	// The List calls this method once for every row.
	prepareRow: function(rowElement, rowIndex, templateElements) {
		// templateElements contains references to all elements that have an id in the template row.
		// Ex: set the value of an element with id="label".
	if (Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].status !="DUN"){
		if (templateElements.desc) {
			templateElements.desc.innerText = Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].desc;
			templateElements.jobid.innerText = Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].jobId;
			templateElements.locator.innerText = Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].loc;
			if (Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].typ != "Routine" && Worker.workScheduleOps[scheduleTouched].jobOps[rowIndex].typ != "Combined")
			{templateElements.desc.style.color="red";}
			}
		}

		// Assign a click event handler for the row.
		rowElement.onclick = function(event) {
			jobTouched=rowIndex;
			setTaskPage();
			goForward('Tasks',"Job ID: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId);
		};
	}
};


// This object implements the dataSource methods for the list.
var TasksListDataSource = {
	
	// The List calls this method to find out how many rows should be in the list.
	numberOfRows: function() {
	if(Worker.workScheduleOps.length>0){
		return Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps.length;
		}
	},
	
	// The List calls this method once for every row.
	prepareRow: function(rowElement, rowIndex, templateElements) {
		// templateElements contains references to all elements that have an id in the template row.
		// Ex: set the value of an element with id="label".
		if (templateElements.task_desc) {
			templateElements.task_desc.innerText = Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[rowIndex].desc;
		}

		// Assign a click event handler for the row.
		rowElement.onclick = function(event) {
		taskTouched=rowIndex;
		//Setup action page
		setActionPage();
		goForward('Actions',"Job ID: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId);

		};
	}
};



// This object implements the dataSource methods for the list.
var ActionsListDataSource = {
	
	// The List calls this method to find out how many rows should be in the list.
	numberOfRows: function() {
		if (Worker.workScheduleOps.length > 0)
		{
		return Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps.length;
		}
	},
	
	// The List calls this method once for every row.
	prepareRow: function(rowElement, rowIndex, templateElements) {
		// templateElements contains references to all elements that have an id in the template row.
		// Ex: set the value of an element with id="label".
		if (templateElements.ActionLabel) {
			templateElements.ActionLabel.innerText = 
			Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps[rowIndex].verb + " " +
			Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps[rowIndex].subject + " " +
			Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps[rowIndex].modifier;
		}
		if (templateElements.fc) {
			templateElements.fc.name=rowIndex;
			templateElements.fc.value = 
			Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps[rowIndex].fc;
		}

	}
};



function updateAction(event)
{
	Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].jobActionOps[event.target.name].fc=event.target.value;
	DoSync();
}


function updateTaskTime(event)
{
	var hrs=document.getElementById('hrs');
	var mins=document.getElementById('mins');

Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].hrs=hrs.value;
Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].mins=mins.value;
    DoSync();
}

function setActionPage()
{
	var hrs=document.getElementById('hrs');
	var mins=document.getElementById('mins');
hrs.value=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].hrs;
mins.value=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobTaskOps[taskTouched].mins;		
}

function setTaskPage()
{
	var jobDesc = document.getElementById('jobDesc');
	var jobLoc = document.getElementById('jobLoc');
	var infoButton = document.getElementById('infoButton');
	var jobTyp = document.getElementById('jobTyp');
	var jobNote = document.getElementById('jobNote');
	var objTag = document.getElementById('objTag');
	
	jobDesc.innerText=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].desc;
	jobLoc.innerText="Locator: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].loc;
	jobTyp.innerText="Type: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].typ;
	if (Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].note != ""){
	 jobNote.style.display="block";
	 jobNote.innerText="Note: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].note;
	}else{ jobNote.style.display="none";}
	objTag.innerText="Object: " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].objectRef;
	
	if (Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].tenantRequestId != null)
	{
		infoButton.style.display="block";
	} else {
		infoButton.style.display="none";
	}
	
}

function closeJob(event)
{
	var answer = confirm("Close Job " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId +"?");
	if (answer)
	{
	Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].status="DUN";
	DoSync();
	alert("Job " + Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId + " Closed.");
	} 
	tlist= document.getElementById('JobsList');		
	tlist.object.reloadData()
	var browser = document.getElementById('browser').object;
	browser.goBack();
}


function reqInfo(event)
{
	setReqPage();
	var browser = document.getElementById('browser').object;
    browser.goForward("RequestorView",Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId);
}

function setReqPage()
{
	var note=document.getElementById('reqNoteBox');
	var cx=document.getElementById('reqContactBox');
	var xt=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].xt;
	var ph=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].ph;
	//remove 'x'
	ph=ph.toUpperCase();
	ph=ph.replace("X","");
	
	if (ph.length<=4){ph="407-582-"+ph;} //VCC numbers are usually put in as 4 digits instead of ten
	
	ph="<a href=\"tel:"+ph+"\">"+ph+"</a>";
	xt="<a href=\"mailto:"+xt+"?subject=Job ID: "+ Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].jobId +"\">"+xt+"</a>";
	
note.innerText=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].cm;

cx.innerHTML=Worker.workScheduleOps[scheduleTouched].jobOps[jobTouched].ct + "<P>" + xt + "<P>" + ph;		
}

