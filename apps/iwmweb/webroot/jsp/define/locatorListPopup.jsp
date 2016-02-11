<%@ page import="org.mlink.iwm.bean.Locator"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript">


    /*emptyLocator is the elementIds definition for functions  clear and SaveLocator to work properly
   need to investigate how to use json for automatic conversion of js object into json string*/
    function getEmptyLocator() {
        var emptyLocator = { locatorId:-1,
            name:null, abbr:null, fullLocator:null, address:null, schemaId:null,
            inServiceDate:null, parentId:null,emergencyContact:null };
        return emptyLocator;
    }


   var ParentLocatorFilter;

   function fillForm(alocator){
       var locator=alocator;
       DWRUtil.setValues(locator);
       //fire onchange event to trigger the filter update. It does not happen in either IE or FF by default
       //document.getElementById("parentId").click();
       ParentLocatorFilter.populateChain(locator.parentId);
   }

   function readLocator(locatorId){
       //locatorId=20000718391;
       //Element.show( $('locator_edit'));
       if(locatorId) {
           $("popupTitle").innerHTML= "Edit Locator";
           clearLocator();
           Locators.getItem(fillForm, locatorId);
           thePopupManager.showPopup('locator_edit');
       } else {
           //if no locator id is passed in that means it is an add. make a blank form.
           $("popupTitle").innerHTML= "Add Locator";
           clearLocator();
           ParentLocatorFilter.reset();
           thePopupManager.showPopup('locator_edit');
       }
   }

   function clearLocator() {
       DWRUtil.setValues(getEmptyLocator());
   }

       function updateLocatorList(message){
           if(message.length==0){
               theLocatorDataTable.update(); //function in Locators.jsp
               //Element.hide( $('locator_edit'));
               thePopupManager.hidePopup();
               clearLocator();
           }else{
               alert(message);
           }
       }
   function saveLocator() {
       //locator = { locatorId:-1, name:null, address:null, schemaId:null,inServiceDate:null };
       //DWRUtil.getValues(locator.toJSONString());
       if(validateForm()){
           var locator = getEmptyLocator();
           DWRUtil.getValues(locator);
           Locators.saveItem(updateLocatorList, locator);
       }

       //call Locators.jsp update in case locator was moved in the tree
       //theLocatorDataTable.update();
   }
    function onChange(parentId){
        $("parentId").value=parentId;
        updateFullLocator(parentId);
    }

    function onAbbrChange(){
        parentId = $("parentId").value;
        if(!parentId && parentId.length==0) parentId=null;
        updateFullLocator(parentId);
    }

    function updateFullLocator(parentId){
        $("parentId").value=parentId;
        Locators.getItem(parentId,{
            callback:
                    function(alocator){

                        if(alocator.fullLocator==null) {
                            $("fullLocator").innerHTML= $("abbr").value;
                        }else{
                            $("fullLocator").innerHTML= alocator.fullLocator + "." + $("abbr").value;}
                        }
        });
    }

   function init() {
       ParentLocatorFilter = new LocatorChain("parentLocatorFilter", onChange, false);
       //alert("executing Locator.init");
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

   callOnLoad(init);

</script>
<%-- *** START HTML ****************************************************** --%>

    <input id="locatorId" type="hidden"/>
    <input id="parentId" type="hidden"/>

<div class="popup" id="locator_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Edit Locator</span></h2></div>
    <div class="popupBody">

        <form id="LocatorForm" action="" name="LocatorForm">
            <a:ajax  id="parentLocatorFilter" type="iwm.filter" name="iwm.filter"/>
            <table >
                <tr>
                    <td ><bean:message key="name"/>:</td>
                    <td><input id="name" name="name" type="text"/></td>
                </tr>
                <tr>
                    <td><bean:message key="abbr"/>:</td>
                    <td><input id="abbr" name="abbr" type="text" onchange="onAbbrChange();"/></td>
                </tr>
                <tr>
                    <td><bean:message key="fullLocator"/>:</td>
                    <td><span id="fullLocator"/></td>
                </tr>
                <tr>
                    <td><bean:message key="address"/>:</td>
                    <td><input id="address" name="address" type="text"/></td>
                </tr>

                <%/*tr>
                    <td><bean:message key="securityLevel"/>:</td>
                    <td >
                        <html:select property="securityLevel" styleId="securityLevel"  value="">
                            <html:optionsCollection property="options"  name="SecurityTypeRef"/>
                        </html:select>
                    </td>
                </tr*/%>
                <tr>
                    <td><bean:message key="inServiceDate"/>:</td>
                    <td><!--input type="text" id="inServiceDate"/-->
                        <a:ajax id="inServiceDate"  type="iwm.calendar" name="iwm.calendar" />
                    </td>
                </tr>
                <tr>
                    <td><bean:message key="emergencyContact"/>:</td>
                    <td>
                        <textarea rows="3" cols="25"  id="emergencyContact"> </textarea>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <input type="button" class="button" value="Save" onclick="saveLocator()"/>
                        <input type="button" class="button" value="Clear" onclick="clearLocator()"/>
                        <input type="button" class="button" value="Cancel" onclick="thePopupManager.hidePopup();"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="LocatorForm" dynamicJavascript="true" staticJavascript="false"/>