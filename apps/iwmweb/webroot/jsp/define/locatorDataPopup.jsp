<%@ page import="org.mlink.iwm.bean.LocatorData"%>
<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>


<script type="text/javascript">

    /*emptyLocator is the elementIds definition for functions  clear and SaveLocator to work properly
   need to investigate how to use json for automatic conversion of js object into json string*/
    //ABSTRACT IN BASE CLASS
    function getEmptyLocatorData() {
        var emptyLocatorData = { id:-1,
            dataTypeId:null, dataLabel:null, dataValue:null, uomId:null, custom:null, isDisplay:null, locatorId:null };
        return emptyLocatorData;
    }

//jpm var ParentLocatorFilter;

    //IN BASE CLASS
   function fillForm(alocatorData){
       var locatorData=alocatorData;
       DWRUtil.setValues(locatorData);
       //fire onchange event to trigger the filter update. It does not happen in either IE or FF by default
       //document.getElementById("parentId").click();
//jpm       ParentLocatorFilter.populateChain(locator.parentId);
   }

            //ASTRACT IN BASE CLASS
   function readLocatorData(locatorDataId){
       //locatorId=20000718391;
       //Element.show( $('locator_edit'));
       if(locatorDataId) {
           $("popupTitle").innerHTML= "Edit Locator Data";
           clearLocatorData();
           LocatorsData.getItem(fillForm, locatorDataId);
           thePopupManager.showPopup('locatorData_edit');
       } else {
           //if no locator id is passed in that means it is an add. make a blank form.
           $("popupTitle").innerHTML= "Add Locator Data";
           clearLocatorData();
//           ParentLocatorFilter.reset();
           thePopupManager.showPopup('locatorData_edit');
       }
   }

            //BASE CLASS
   function clearLocatorData() {
       DWRUtil.setValues(getEmptyLocatorData());
   }

        //BASE CLASS
   function updateLocatorDataList(message){
       if(message == null || message.length==0){
           dataTable.update(); //function in Locators.jsp
           thePopupManager.hidePopup();
           clearLocatorData();
       }else{
           alert(message);
       }
   }


            //BASE CLASS
   function saveLocatorData() {
       //locator = { locatorId:-1, name:null, address:null, schemaId:null,inServiceDate:null };
       //DWRUtil.getValues(locator.toJSONString());
       if(validateDataForm($("DataForm"))){
           var locatorData = getEmptyLocatorData();
           DWRUtil.getValues(locatorData);
           locatorData.locatorId = <%= request.getParameter("id") %>;
           LocatorsData.saveItem(updateLocatorDataList, locatorData);
       }

       //call Locators.jsp update in case locator was moved in the tree
       //theLocatorDataTable.update();
   }
/*
            //CUSTOM
    function onChange(parentId){
        $("parentId").value=parentId;
        updateFullLocator(parentId);
    }

            //CUSTOM
    function onAbbrChange(){
        parentId = $("parentId").value;
        if(!parentId && parentId.length==0) parentId=null;
        updateFullLocator(parentId);
    }

            //CUSTOM
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
  */
   function init() {
 //jpm      ParentLocatorFilter = new LocatorChain("parentLocatorFilter", onChange, false);
       //alert("executing Locator.init");
   }

   callOnLoad(init);

</script>

<%-- *** START HTML ****************************************************** --%>
<%-- THIS IS THE FORMAT OF A POPUP

<div class="popup" id="numberPopup">
    <div class="popupHeader">
        <h2>TITLE HERE</h2>
    </div>

    <div class="popupBody">
        <div>
            YOUR BODY HERE
        </div>
    </div>
</div>
--%>


<div class="popup" id="locatorData_edit">
    <div class="popupHeader"><h2><span id="popupTitle">Edit Locator</span></h2></div>

    <div class="popupBody">
        <form id="DataForm" action="" name="DataForm">

        <div style="display:none;">
            <input id="id" name="id" type="text"/>
            <input id="locatorId" name="locatorId" type="hidden" value="<%= request.getParameter("id") %>"/>
        </div>

        <table >
            <tr>
                <td><bean:message key="dataTypeId"/>:</td>
                <td>
                    <%//Create empty bean. Need this trick to take advantage of html:select tag
                    request.setAttribute("curentLocatorData",new LocatorData());%>
                    <html:select property="dataTypeId" styleId="dataTypeId" name="currentLocatorData" value="">
                    <html:optionsCollection property="options"  name="DataTypeRef"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td>Label:</td>
                <td><input id="dataLabel" name="dataLabel" type="text"/></td>
            </tr>
            <tr>
                <td>Value:</td>
                <td><input id="dataValue" name="dataValue" type="text"/></td>
            </tr>
            <tr>
                <td><bean:message key="uomId"/>:</td>
                <td>
                    <html:select property="uomId" styleId="uomId" name="currentLocatorData" value="">
                    <html:optionsCollection property="options"  name="UOMRef"/>
                    </html:select>
                </td>
            </tr>
            <!--tr>
                <td colspan="2"><input type="checkbox">Display In Field</td>           
            </tr-->
            <tr>
                <td colspan="2">
                    <input type="button" class="button" value="Save" onclick="saveLocatorData()"/>
                    <input type="button" class="button" value="Clear" onclick="clearLocatorData()"/>
                    <input type="button" class="button" value="Cancel" onclick="thePopupManager.hidePopup();"/>
                </td>
            </tr>
        </table>
        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="DataForm" dynamicJavascript="true" staticJavascript="false"/>