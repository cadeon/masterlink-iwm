<%@ page import="org.mlink.iwm.util.Constants"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >

var JobEditPopup = Class.extend( AbstractCrudPopup, {
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
        this.theJobData = new Object();
        this.jobOrganizationFilter =
            new OrganizationChain("jobOrganizationFilter", function(id){return}, false,false);
    },

    show: function(theId, projectId){
        _this = this;
        if(projectId != null && projectId != -1){
        	this.theJobData.projectId=projectId;
        }
            
        if(theId != null && theId != -1){
            $("jobPopupTitle").innerHTML= "Edit Job ";
            Jobs.getItem(function(response){_this.populate(response);}, theId);
            $('bn_save').value="Save";
            this.theJobData.id=theId;
            this.displayExtraFields('');
        } else {
            //if no locator id is passed in that means it is an add. make a blank form.
            $("jobPopupTitle").innerHTML= "Create Job: Set Details";
            this.theJobData.id=-1;
            JobCreateWizard.getPrototype(function(response){_this.populate(response);});
            $('bn_save').value="Finish";
            this.displayExtraFields("none");
        }

    },


    disableFields: function(isDisabled){

        //$('id').disabled = "true";
        //$('createdBy').disabled = "true";
        //$('createdDate').disabled = "true";

        $('description').disabled = isDisabled;
        $('note').disabled = isDisabled;
        //$('numberOfWorkers').disabled = isDisabled;
        //$('sticky').disabled = isDisabled;
        $('estTimeHours').disabled = isDisabled;
        $('estTimeMinutes').disabled = isDisabled;
        $('priorityId').disabled = isDisabled;
        $('skillLevelId').disabled = isDisabled;
        

        //alert(document.getElementById(('skillTypeTd')));
        $('skillTypeId').disabled = isDisabled;  //TODO: problem because of more than one element with this id
        $('jobTypeId').disabled = isDisabled;
        //$('organizationId').disabled = isDisabled;
        //$('scheduleResponsibilityId').disabled = isDisabled;
        $('statusId').disabled = isDisabled;
        $('earliestStart').disabled = isDisabled;
        $('latestStart').disabled = isDisabled;

        //$('startedDate').disabled = "true";
        //$('dispatchedDate').disabled = "true";
        //$('completedDate').disabled = "true";

        //$('tenantNote').disabled = isDisabled;
        //$('tenantName').disabled = isDisabled;
        //$('tenantPhone').disabled = isDisabled;
        //$('tenantEmail').disabled = isDisabled;
        if(isDisabled){
            $('bn_save').style.display = 'none';
        } else {
            $('bn_save').style.display = 'inline';
        }
    },

    displayExtraFields: function(show){
        $('requestor').style.display=show;
        $('milestones').style.display=show;
        $('headers').style.display=show;
        if(show != "none"){
            $('jobs_edit').style.width = '650px';
            $('jobs_edit').style.left = '-325px';
        } else {
            $('jobs_edit').style.width = '366px';
            $('jobs_edit').style.left = '-183px';
        }

    },

    populate: function(item){
        var projectId = this.theJobData.projectId;
        FormValuesUtil.setFormValues($("JobForm"),item);
        this.theJobData=item;
        this.theJobData.projectId = projectId;
        var hourMinute=HourMinuteConveter.splitMinutes(item.estTime);
        DWRUtil.setValue("estTimeHours",hourMinute.hours);
        DWRUtil.setValue("estTimeMinutes",hourMinute.minutes);
        //if(item.sticky=="Y") $("sticky").checked=true;
        //else $("sticky").checked=false;
        this.jobOrganizationFilter.populateChain(this.theJobData.organizationId);

        thePopupManager.showPopup('jobs_edit');
        $('bn_save').focus();        

      //Should this popup be disabled?
		this.disableFields(false); //assume not
        if	(this.theJobData.completedDate != null) 
            { //Has a completed date. Disable
            this.disableFields(true);
	        } 

		if	(this.theJobData.latestStart != null) 
	        {
			var expDate = new Date(this.theJobData.latestStart);
			var today = new Date();
			if (expDate < today){this.disableFields(true);}
        	} 
    	if (this.theJobData.status == "CIA"){
			//TODO: I hate keying off status.
			this.disableFields(true);
        	}
    },


    save: function(){
        if(this.validate()){
            FormValuesUtil.getSubmittableFormValues($("JobForm"),this.theJobData);
            this.theJobData.estTime=HourMinuteConveter.toMinutes(DWRUtil.getValue("estTimeHours"),DWRUtil.getValue("estTimeMinutes"));
            this.theJobData.organizationId=this.jobOrganizationFilter.currentSelectedId==null?'':this.jobOrganizationFilter.currentSelectedId;
            this.persist(this.theJobData);
            return true;
        }
        else {
            return false;
        }
    }
});


  function maxLength(len){
    if ($('note').value.length > len) {
        $('note').value =  $('note').value.slice(0, len-2)
        alert("Text too long. Must be "+len+" characters or less");
        return false;
    }
    return true;
  }



callOnLoad(function(){
    theJobEditPopup = new JobEditPopup("jobs_edit","JobForm", theJobDataTable);
});

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#jobs_edit {
        width: 366px;
        left: -183px;
    }
    div#jobs_edit select {
        width: 185px;
    }
    div#jobs_edit textarea {
        width: 225px;
        height: 40px;
        resize: vertical;
    }
    div#jobs_edit input.textField {
        width: 225px;
    }

    div#jobs_edit select.filterSelect {
        padding: 0px;
        margin: 0px;
    }


    div#jobs_edit table.formTable td{
        padding: 0px;
    }

    div.popup table td.fieldComment{
        padding: 0px;
        font-size:9px;
        color: #777;
    }
</style>

<!--  -->
<div class="popup" id="jobs_edit">
<div class="popupHeader">
    <h2><span id="jobPopupTitle">Edit Data</span></h2>
</div>
<div class="popupBody">
<form id="JobForm" name="JobForm" action="">

<table>

<tr id="headers">
    <td><h3>Job Details</h3></td>
    <td><h3>Requestor Info</h3></td>
</tr>
<tr>
<td valign="top">



<table>
<tr>
    <td><label>Job Id:</label></td>
    <td><input type="text" class="textField" readonly id="id"></td>
</tr>

<tr>
    <td><label>Created By:</label></td>
    <td><input type="text" class="textField" readonly  id="createdBy"></td>
</tr>

<tr>
    <td><label>Created On:</label></td>
    <td><input type="text" class="textField" readonly id="createdDate"></td>
</tr>

<tr>
    <td><label>Description:</label></td>
    <td><input type="text"  class="textField" id="description"></td>
</tr>

<tr>
    <td><label>Note:</label></td>
    <td><textarea type="text"  class="textField" id="note" onkeyup="maxLength(250);"></textarea></td>
</tr>

<!--   No more number of workers. It is what it is (hopefully always 1)
to avoid problems, we're leaving the input, but hiding it.
<tr>
    <td><label># of Workers:</label></td>
    <td>--><input type="hidden" size="3" id="numberOfWorkers"><!--   
    </td>
</tr>
-->

<tr>
    <td valign="top"><label>Est Time:</label></td>
    <td>

        <table cellpadding="0" cellspacing="0">
            <tr><td><a:ajax id="estTimeHours" name="iwm.spinner"/>&nbsp;</td>
                <td><a:ajax id="estTimeMinutes" name="iwm.spinner"/></td></tr>
            <tr><td class="fieldComment">Hours</td><td class="fieldComment">Mins</td></tr>
        </table>
    </td>
</tr>


<tr>
    <td><label><bean:message key="priority"/>:</label></td>
    <td>
        <html:select styleClass="filterSelect" property="priorityId" styleId="priorityId" value="">
            <option value="">--- Select Proirity --</option>
            <html:optionsCollection property="options"  name="PriorityRef"/>
        </html:select>
    </td>
</tr>

<tr>
    <td><label><bean:message key="skill"/>:</label></td>
    <td id="skillTypeTd">
        <html:select styleClass="filterSelect" property="skillTypeId" styleId="skillTypeId" value="">
            <option value="">-- Select Skill --</option>
            <html:optionsCollection property="options"  name="SkillTypeRef"/>
        </html:select>
    </td>
</tr>

<tr>
    <td><label><bean:message key="skillLevel"/>:</label></td>
    <td>
        <html:select styleClass="filterSelect" property="skillLevelId" styleId="skillLevelId" value="">
            <option value="">-- Select Level --</option>
            <html:optionsCollection property="options"  name="SkillLevelRef"/>
        </html:select>
    </td>
</tr>

<tr>
    <td><label>Job Type:</label></td>
    <td>
        <html:select styleClass="filterSelect" property="jobTypeId" styleId="jobTypeId" value="">
            <option value="">-- Select Job Type --</option>
            <html:optionsCollection property="options"  name="TaskTypeRef"/>
        </html:select>
    </td>
</tr>


<!-- No more schedule by. This is implicit when a job is manually scheduled. <tr>
    <td><label><bean:message key="scheduleBy"/>:</label></td>
    <td>
        <html:select styleClass="filterSelect" property="scheduleResponsibilityId" styleId="scheduleResponsibilityId" value="">
            <option value="">-- Select Scheduler --</option>
            <html:optionsCollection property="options"  name="ScheduleResponsibilityRef"/>
        </html:select>
    </td>
</tr>
-->

<tr>
    <td><label>Status:</label></td>
    <td><html:select styleClass="filterSelect" property="statusId" styleId="statusId" value="">
        <option value="">-- Select Status --</option>
        <!--html:optionsCollection property="options"  name="JobStatusRef"/-->
        <c:forEach items="${JobStatusRef.options}" var="item">
            <%-- <c:choose>
                     TODO / FIXME Chris is commenting this check at request of the college. 
               I'd prefer to just change some of the rules here, but I'm not sure how.
               <c:when test="${item.disabled}">
                    <option disabled value="<c:out value="${item.value}"/>"><c:out value="${item.label}"/></option>
                </c:when> 
                <c:otherwise> --%>
                    <option value="<c:out value="${item.value}"/>"><c:out value="${item.label}"/></option>
                <%--  </c:otherwise> 
            </c:choose>--%>
        </c:forEach>
    </html:select></td>
</tr>

<tr>
    <td valign="top"><label>Start Date:</label></td>
    <td>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td><a:ajax id="earliestStart" type="iwm.calendar" name="iwm.calendar"/>&nbsp;</td>
                <td><a:ajax id="latestStart" type="iwm.calendar" name="iwm.calendar"/></td>
            </tr>
            <tr><td class="fieldComment">Earliest</td><td class="fieldComment">Latest</td></tr>
        </table>
    </td>
</tr>

<tr id="milestones">
    <td valign="top"><label>Milestones:</label></td>
    <td>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td><input id="startedDate" type="text" size="10" disabled="true">&nbsp;</td>
                <td><input id="dispatchedDate" type="text" size="10" disabled="true">&nbsp;</td>
                <td><input id="completedDate" type="text" size="10" disabled="true">&nbsp;</td>
            </tr>
            <tr><td class="fieldComment">Started</td><td class="fieldComment">Dispatched</td><td class="fieldComment">End</td></tr>
        </table>
    </td>
</tr>
</table>

</td>
<td valign="top" id="requestor">
    <table>
        <tr>
            <td><label>Requestor's<br>Description:</label></td>
            <td><textarea  readonly id="tenantNote"></textarea></td>
        </tr>
        <tr>
            <td><label>Name:</label></td>
            <td><input type="text" readonly  size="28" id="tenantName"></td>
        </tr>

        <tr>
            <td><label>Phone:</label></td>
            <td><input type="text" size="28" readonly   id="tenantPhone"></td>
        </tr>

        <tr>
            <td><label>Email:</label></td>
            <td><input type="text" size="28" readonly  id="tenantEmail"></td>
        </tr>
        <tr><p></p></tr>
        <tr><td colspan="2"><h3><bean:message key="organization"/></h3></td></tr> 
        <tr>
        	<td colspan="2"><a:ajax  id="jobOrganizationFilter" type="iwm.filter" name="iwm.filter"/></td>
		</tr>
    </table>
</td>
</tr>
</table>
</form>
<input type="button" value="Save" class="button" id="bn_save" onClick="theJobEditPopup.save();">
<input type="button" value="Cancel" class="button" onClick="theJobEditPopup.close();">
</div>

</div>


<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute properly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="JobForm" dynamicJavascript="true" staticJavascript="false"/>


