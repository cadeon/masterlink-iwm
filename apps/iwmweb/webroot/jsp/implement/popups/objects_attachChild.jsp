<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%-- --%>
<script type="text/javascript" >
	var objectLocatorFilterAC;
    var objectClassFilterAC;
    var parentObjectId;
    var theCheckbox;
    var locatorIdAct, classIdAct;
    var PickObjectPopup = Class.extend( AbstractCrudPopup, {
    	initialize: function(popupId, formName, dataTable, isWizard){
            this.superInit(popupId, formName, dataTable, isWizard);
        },
        show: function(id, locatorId, classId){
        	locatorIdAct = locatorId;
        	classIdAct = classId;
        	
        	parentObjectId = id;
            objectClassFilterAC.populateChain(classId);
            objectLocatorFilterAC.populateChain(locatorId);
            refreshObjectsList();
            thePopupManager.showPopup('objects_attachChild');
        },
        selectChildCallback: function(message){
            if(message && message.length > 0){
                alert(message);
                theCheckbox.checked=!theCheckbox.checked;
            }
        },
        addChild: function(checkbox, childId, locatorIdAct, locatorId){
            theCheckbox = checkbox;
            if(checkbox.checked){
                if(locatorIdAct != locatorId){
                	if(confirm('Child must be moved to parent\'s locater. Do you want to continue?')){
                	   	Objects.attachChild(this.selectChildCallback, parentObjectId, childId, locatorIdAct, 1);
                	}else{
                		checkbox.checked=false;
                	}
                }else{
                	Objects.attachChild(this.selectChildCallback, parentObjectId, childId, locatorId, 1);
                }
            }else{
            	Objects.attachChild(this.selectChildCallback, parentObjectId, childId, locatorId, 0);
            }
        },
        closeAttachChild: function(){
        	theObjectsTable.update();
        	this.close();
        }
    });

    function refreshObjectsList() {
        var fillObjectsTable = function (response) {
            DWRUtil.removeAllRows("ObjectsListTable");
            DWRUtil.addRows("ObjectsListTable", response.items, [
                    function(item){return '<input type="checkbox" id="theObjectId" name="theObjectId" value='+item.objectId + ' onclick="objectAttachPopup.addChild(this,'+item.objectId+','+locatorIdAct+','+item.locatorId+')">';},
                    function(item){return item.objectRef;} ]);
        }
		var objectsCriteria = new Object();
		objectsCriteria.classId=objectClassFilterAC.currentSelectedId;
		objectsCriteria.locatorId=objectLocatorFilterAC.currentSelectedId;
        Objects.getUnattachedData(fillObjectsTable, objectsCriteria);
    }

    callOnLoad(function(){
        objectClassFilterAC = new ObjectClassChain("objectClassFilterAC", refreshObjectsList,false, false);
        objectLocatorFilterAC = new LocatorChain("objectLocatorFilterAC", refreshObjectsList,false, false);
        objectAttachPopup = new PickObjectPopup("objects_attachChild","ObjectAttachChildForm", theObjectsTable, true);
    });
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#objects_attachChild div#selectObject {
        width: 92%;
        height: 200px;
        overflow:auto;
        border: 1px solid #666;
    }
    div#objects_attachChild {
        width: 350px;
    }
    div#objects_attachChild div#selectObject div{
        border-bottom: 1px solid #EEE;
        padding-top: 1px;
        padding-bottom: 2px;
    }

    div#objects_attachChild table.dataTable td{
       font-size:10px;
    }
</style>

<div class="popup" id="objects_attachChild">
    <div class="popupHeader"><h2><span id="popupTitle">Attach Object</span></h2></div>

    <div class="popupBody">
        <form id="ObjectAttachChildForm" action="" name="ObjectAttachChildForm">
            <h3>Location Filter</h3>
            <a:ajax id="objectLocatorFilterAC" type="iwm.filter" name="iwm.filter"/>
            
            <h3>Template Filter</h3>
            <a:ajax id="objectClassFilterAC" type="iwm.filter" name="iwm.filter"/>
            <h3>Select Object<span class="required">*</span></h3>
            
            <input type="checkbox" id="theObjectId" name="theObjectId" style="display:none">  <%/*trick to make radio waork with DWRUtil.getValue. Must be at least two radios to return value, overwise returns checked/uncheked*/%>
            <div id="selectObject">
                <table class="dataTable" >
                    <tbody id="ObjectsListTable">
                    </tbody>
                </table>
            </div>
        </form>
        <input type="button" class="button" value="Close" onclick="objectAttachPopup.closeAttachChild();"/>
    </div>
</div>
