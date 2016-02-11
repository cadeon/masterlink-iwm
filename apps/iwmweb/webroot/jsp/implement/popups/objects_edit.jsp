<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type="text/javascript" >
	var parentObjectId;
	var parentLocatorId;
    var ObjectEditPopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable, isWizard){
            this.superInit(popupId, formName, dataTable, isWizard);
            this.theData = new Object();
            this.orgOrganizationFilter =
                    new OrganizationChain("orgOrganizationFilter", function(id){return}, false,false);
        },

        show: function(parentObjectId1, parentLocatorId1, theId, isWizard){
            if(isWizard){
                this.isWizard = isWizard;
            } else {
                this.isWizard = false;
            }

            if(this.isWizard){
            	parentObjectId=parentObjectId1;
            	parentLocatorId=parentLocatorId1;
            	this.clean();
                $('objectEditTitle').innerHTML = "Create Object: Set Properties";
                $('createObjectFinishButton').style.display = 'inline';
                $('createObjectSaveButton').style.display = 'none';
                thePopupManager.showPopup('objects_edit');
                objectLocatorFilter.populateChain(parentLocatorId);
                this.theData.objectId = -1;
                $('active').checked = true;
                this.orgOrganizationFilter.populateChain();
            } else {
                $('objectEditTitle').innerHTML = "Edit Object";
                $('createObjectFinishButton').style.display = 'none';
                $('createObjectSaveButton').style.display = 'inline';
                _this = this;
                
                Objects.getItem(function(response){_this.populate(response);}, theId);
            }
        },

        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("ObjectForm"),this.theData);
            if(this.theData.active==1) $("active").checked=true; else $("active").checked=false;
            objectLocatorFilter.populateChain(item.locatorId);
            this.orgOrganizationFilter.populateChain(this.theData.organizationId);
            thePopupManager.showPopup('objects_edit');

        },

        save: function(){
            if(this.validate()){
                FormValuesUtil.getFormValues($("ObjectForm"),this.theData);
                var _this = this;
                if($("active").checked)
                    this.theData.active="1"; //DWR.getValues for checkboxes returns true/false ignoring actual value. Need to convert
                else
                    this.theData.active="0";

                this.theData.organizationId=this.orgOrganizationFilter.currentSelectedId==null?'':this.orgOrganizationFilter.currentSelectedId;

                if(this.isWizard){
                    if(parentLocatorId!=null){
                        if(this.theData.locatorId!=parentLocatorId){
                			if(confirm('Child must be moved to parent\'s locater. Else the object will be created as a stand-alone object. Do you want to continue?')){
                				this.theData.locatorId=parentLocatorId;
                				this.theData.parentObjectId=parentObjectId;
                			}
                        }
                    }
                    ObjectCreateWizard.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
                }else{
                    //Objects.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
                    this.persist(this.theData);
                }
                return true;
            }
            else {
                return false;
            }
        }
    });


    var objectLocatorFilter;
    callOnLoad(function(){
        theObjectEditPopup = new ObjectEditPopup("objects_edit", "ObjectForm", theObjectsTable);
        objectLocatorFilter = new LocatorChain("objectLocatorFilter", onChange, false,false);

    });

function onChange(locatorId){
    theObjectEditPopup.theData.locatorId=locatorId;
    //objectCreateWizard[1].theData.locatorId=locatorId;
}

function toggleDisabled(el) {
    try {
        el.disabled = el.disabled ? false : true;
    }
    catch(E){}
    
    if (el.childNodes && el.childNodes.length > 0) {
        for (var x = 0; x < el.childNodes.length; x++) {
            toggleDisabled(el.childNodes[x]);
        }
    }
}
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#objects_edit select {
        width: 165px;
    }
    div#objects_edit input#createObjectFinishButton {
        display:none;
    }

    div#objects_edit input#tag {
        width: 165px;
    }
    div#objects_edit input#runHours {
        width: 165px;
    }
</style>

<!--  -->
<div class="popup" id="objects_edit">
    <div class="popupHeader">
        <div class="popupHeader"><h2><span id="objectEditTitle">Edit Object</span></h2></div>
    </div>

    <div class="popupBody">

<form action="" name="ObjectForm" id="ObjectForm">
<h3><bean:message key="objectDetails"/></h3>
  <table>
    <tr>
      <td><label><bean:message key="name"/>:<span class="required">*</span></label></td>
      <td><input id="tag" name="tag" type="text" class="textInput">
      </td>
    </tr>

      <tr>
          <td><label><bean:message key="type"/>:<span class="required">*</span></label></td>
          <td>
              <html:select property="objectTypeId" styleId="objectTypeId" value="">
                  <html:option value="">-- Select Type --</html:option>
                  <html:optionsCollection property="options"  name="ObjectTypeRef"/>
              </html:select>
          </td>
      </tr>

    <tr>
      <td><label><bean:message key="meterReading"/>:</label></td>
      <td><input type="text"  id="runHours" name="runHours" class="textInput">
      </td>
    </tr>
    <tr>
      <td><label><bean:message key="active"/>:</label></td>
        <td><input type="checkbox" id="active" onclick="if(!this.checked) return(confirm('Deactivation prevents the creation of jobs for this object. All tasks will become inactive.\nYou will have to manually activate each task when object is reactivated. Proceed?'));"></td>
    </tr>
</table>

<span id="locMessage"></span>
<h3><bean:message key="objectLocation"/><span class="required">*</span></h3>
    <a:ajax  id="objectLocatorFilter" type="iwm.filter" name="iwm.filter"/>

<h3><bean:message key="organization"/></h3>
    <a:ajax  id="orgOrganizationFilter" type="iwm.filter" name="iwm.filter"/>

  <input type="button" id="createObjectFinishButton" value="Finish" class="button" onClick="theObjectEditPopup.save();">
  <input type="button" id="createObjectSaveButton" value="Save" class="button" onClick="theObjectEditPopup.save();">
  <input type="button" value="Cancel" class="button" onClick="thePopupManager.hidePopup();">
</form>

    </div>
</div>

<html:javascript formName="ObjectForm" dynamicJavascript="true" staticJavascript="false"/>


