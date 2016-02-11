<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript" >
var ParentLocatorFilter;
var LocatorEditPopup = Class.extend( AbstractCrudPopup, {
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },
    show: function(locatorId){
        this.theLocator = new Object();
        if(locatorId) {
            $("popupTitle").innerHTML= "Edit Locator";
            _this = this;
            Locators.getItem(function(response){_this.populate(response);}, locatorId);
        } else {
            //if no locator id is passed in that means it is an add. make a blank form.
            $("popupTitle").innerHTML= "Add Locator";
            this.theLocator.locatorId=-1;
            ParentLocatorFilter.reset();
            ParentLocatorFilter.isModified=true; // the parent filter will be refreshed on save
            Form.reset("LocatorForm");
            SessionUtil.getCurrentLocator({
                callback:function(curLocatorId){
                    ParentLocatorFilter.populateChain(curLocatorId);
                    theLocatorEditPopup.theLocator.parentId=curLocatorId;
                },
                async:false});
            $("fullLocator").innerHTML="";
            thePopupManager.showPopup('locators_edit');
            $('name').focus();
        }
    },

    populate: function(item){
        this.theLocator = item;
        DWRUtil.setValues(this.theLocator);
        ParentLocatorFilter.populateChain(this.theLocator.parentId);
        thePopupManager.showPopup('locators_edit');
        $('name').focus();
    },

        /* an overide to enable optional parent filter update*/
    showCallBackMessage: function(message){
        if(message && message.length > 0)
            alert(message);
        else{
            this.close();
            this.dataTable.update();
            if(ParentLocatorFilter.isModified) locatorFilter.refresh();
       }
    },

    save: function(){
        if(this.validate() && validateEmail()){
            FormValuesUtil.getFormValues($("LocatorForm"),this.theLocator);
            this.theLocator.fullLocator=$("fullLocator").innerHTML;
            if(this.theLocator.parentId==null) this.theLocator.parentId=''; //must be empty not null, nulls will be ignored by Middle tier conversions, see CopyUtils.NullAliasValues
            this.persist(this.theLocator);
            return true;
        }
        else {
            return false;
        }
    }
});


callOnLoad(function(){
    ParentLocatorFilter = new LocatorChain("parentLocatorFilter", onChange, false,false,4);
    theLocatorEditPopup = new LocatorEditPopup("locators_edit", "LocatorForm", theLocatorDataTable);
});

function onChange(parentId){
    theLocatorEditPopup.theLocator.parentId=parentId;
    updateFullLocator(parentId);
}

function onAbbrChange(){
    updateFullLocator(theLocatorEditPopup.theLocator.parentId);
}

function updateFullLocator(parentId){
    if(parentId==null){
        $("fullLocator").innerHTML= $("abbr").value;
    }else{
        Locators.getItem(parentId,{
            callback:
                    function(alocator){
                        $("fullLocator").innerHTML= alocator.fullLocator + "." + $("abbr").value;
                    }
        });
    }
}

function validateEmail(){
    element  = $('emergencyContact');
    if(element.value.length==0) return true; //empty is ok

    tmp = element.value.replace(/,/g," ");   // global replace of commmas with single space
    emails = tmp.split(/(\s)+/);     //split using  one or more spaces as separator

    //alert(tmp+"\n" + emails);
    var fmtStr='';
    for (i=0; i<emails.length; i++){
        email = trim(emails[i]);
        if(i!=0)
            fmtStr=fmtStr+","+email;
        else
            fmtStr=email;

        if(email.length!=0 && !email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)){
            alert (email + " is not valid email. \nUse comma or white space to separate multiple addresses.");
            return false;
        }
    }
    //element.value=fmtStr;
    return true;
}
function validateForm(){
    return validateEmail() && validateLocatorForm($("LocatorForm"));
}
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#locators_edit {
        width: 350px;
        left: -175px;
    }
    div#locators_edit label {
        width: 87px;
        display: block;
    }
    div#locators_edit textarea#emergencyContact {
        width: 210px;
        height: 75px;
    }
</style>

<!--  -->
<div class="popup" id="locators_edit">
    <div class="popupHeader">
        <h2><span id="popupTitle">Edit Locator</span></h2>
    </div>

    <div class="popupBody">

        <form id="LocatorForm" action="" name="LocatorForm">
            <a:ajax  id="parentLocatorFilter" type="iwm.filter" name="iwm.filter"/>
            <table >
                <tr>
                    <td><label><bean:message key="name"/>:<span class="required">*</span></label></td>
                    <td><input id="name" name="name" type="text"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="abbr"/>:<span class="required">*</span></label></td>
                    <td><input id="abbr" name="abbr" type="text" onchange="onAbbrChange();"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="fullLocator"/>:</label></td>
                    <td><span id="fullLocator"/></td>
                </tr>
                <tr>
                    <td><label><bean:message key="address"/>:<span class="required">*</span></label></td>
                    <td><input id="address" name="address" type="text"/></td>
                </tr>

                <%/*tr>
                    <td><label><bean:message key="securityLevel"/>:</label></td>
                    <td >
                        <html:select property="securityLevel" styleId="securityLevel"  value="">
                            <html:optionsCollection property="options"  name="SecurityTypeRef"/>
                        </html:select>
                    </td>
                </tr*/%>
                <tr>
                    <td><label><bean:message key="inService"/>:<span class="required">*</span></label></td>
                    <td><!--input type="text" id="inServiceDate"/-->
                        <a:ajax id="inServiceDate"  type="iwm.calendar" name="iwm.calendar" />
                    </td>
                </tr>
                <tr>
                    <td valign="top"><label><bean:message key="emergencyContact"/>:</label></td>
                    <td>
                        <textarea id="emergencyContact"> </textarea>
                        <span class="comment">Multiple emails should be comma seperated.</span>
                    </td>
                </tr>
            </table>
            <input type="button" class="button" value="Save" onclick="theLocatorEditPopup.save()"/>
            <input type="button" class="button" value="Cancel" onclick="theLocatorEditPopup.close();"/>
        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="LocatorForm" dynamicJavascript="true" staticJavascript="false"/>