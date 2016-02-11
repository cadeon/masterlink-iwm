<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- --%>
<script type="text/javascript" >

    var ActionEditPopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
            this.theData = new Object();
        },
        show: function(theId){
            if(theId != null && theId != -1){
                $("popupTitle").innerHTML= "Edit Action Data";
                _this = this;
                TemplateTaskActions.getItem(function(response){_this.populate(response);}, theId);
            } else {
                //if no locator id is passed in that means it is an add. make a blank form.
                $("popupTitle").innerHTML= "Add Action Data";
                this.theData.id=-1;
                this.theData.taskDefId=<%= request.getParameter("taskId") %>;
            }
            thePopupManager.showPopup('templates-tasks-actions_edit');
            $('verb').focus();
        },
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("ActionForm"),this.theData);
        },
        save: function(){
            if(this.validate()){
            FormValuesUtil.getFormValues($("ActionForm"),this.theData);
            var _this = this;
           //TemplateTaskActions.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
            this.persist(this.theData);
        }}
    });

callOnLoad(function(){
    theActionEditPopup = new ActionEditPopup("templates-tasks-actions_edit", "ActionForm", dataTable);
});
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
    div#templates-tasks-actions_edit input.textInput {
        width: 175px;
    }

    div#templates-tasks-actions_edit {
        width: 270px;
        left: -135px;
    }

</style>

<!--  -->
<div class="popup" id="templates-tasks-actions_edit">
    <div class="popupHeader">
        <h2><span id="popupTitle"></span></h2>
    </div>

    <div class="popupBody">
        <form id="ActionForm" name="ActionForm" action="">
            <table>
                <tr>
                    <td><label>Verb:</label></td>
                    <td><input id="verb" name="verb" type="text" class="textInput"></td>
                </tr>

                <tr>
                    <td><label>Name:</label></td>
                    <td><input id="name" name="name"  type="text" class="textInput"></td>
                </tr>

                <tr>
                    <td><label>Modifier:</label></td>
                    <td><input id="modifier" name="modifier"  type="text" class="textInput"></td>
                </tr>

            </table>
            <input type="button" value="Save" class="button" onClick="theActionEditPopup.save();">
            <input type="button" value="Cancel" class="button" onClick="theActionEditPopup.close();">

        </form>

    </div>
</div>

<html:javascript formName="ActionForm" dynamicJavascript="true" staticJavascript="false"/>

