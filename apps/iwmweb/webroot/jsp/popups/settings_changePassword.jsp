<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<script type='text/javascript' src="dwr/interface/Users.js"></script>

<script type="text/javascript" >

    var ChangePasswordPopup = Class.extend( AbstractCrudPopup, {

    //abstract in base class
        initialize: function(formName, dataTable){
            this.superInit(formName, dataTable);
        },


    //abstract in base class
        show: function(theId){
            this.theData = new Object();
            var _this=this;
            Users.getItem(function(response){_this.populate(response);}, theId);
        },


    //abstract in base class
        populate: function(item){
            this.theData = item;
            FormValuesUtil.setFormValues($("PasswordForm"),this.theData);
            thePopupManager.showPopup('settings_changePassword');
        },

    //base class. override if needed
        showCallBackMessage: function(message){
            if(message && message.length > 0)
                alert(message);
            else{
                this.close();
            }
        },

    //abstract in base class
        save: function(){
            var _this=this;
            if(this.validate() && this.validateConfirmPassword()){
                FormValuesUtil.getFormValues($("PasswordForm"),this.theData);
                Users.saveItem(function(message){_this.showCallBackMessage(message)}, this.theData);
                return true;
            }
        },
        validateConfirmPassword: function(){
            if($F('confirmPassword')==$F('password'))
                return true;
            else{
                alert('New Password and Retype New do not match');
                return false;
                }
        }
    });

    var theChangePasswordPopup;
    callOnLoad(function(){
        theChangePasswordPopup = new ChangePasswordPopup("settings_changePassword","PasswordForm", null);
    });


</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
</style>

<!--  -->
<div class="popup" id="settings_changePassword">
    <div class="popupHeader">
        <h2>Update Password</h2>
    </div>

    <div class="popupBody">
        <form  id="PasswordForm" action="" name="PasswordForm">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" disabled id="username" name="username"></td>
                </tr>

                <tr>
                    <td>Current Password:</td>
                    <td><input type="password" id="oldPassword" name="oldPassword"></td>
                </tr>

                <tr>
                    <td>New Password:</td>
                    <td><input type="password" id="password" name="password"></td>
                </tr>

                <tr>
                    <td>Retype New:</td>
                    <td><input type="password" id="confirmPassword" name="confirmPassword" ></td>
                </tr>
            </table>
            <input type="button" class="button" value="Save" onclick="theChangePasswordPopup.save();">
            <input type="button" class="button" value="Cancel" onclick="theChangePasswordPopup.close();"/>

        </form>
    </div>
</div>

<%/*
Using Validator comes with some rules:
1. As before all validated properties defined in validation.xml
2. You must have a form element in your page  (Validator works with the form)
3. You must have the name atrribute peroperly defined for each validated property (Validator works with names of the form elements)
*/%>
<html:javascript formName="PasswordForm" dynamicJavascript="true" staticJavascript="false"/>



