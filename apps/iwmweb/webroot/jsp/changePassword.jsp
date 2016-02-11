<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>

<script src="scripts/prototype.js" type="text/javascript"></script>
<script type='text/javascript' src="scripts/common.js"></script>
<script type='text/javascript' src="dwr/engine.js"></script>
<script type='text/javascript' src="dwr/util.js"></script>
<script type='text/javascript' src="scripts/dwrsetup.jsp"></script>
<script type='text/javascript' src="dwr/interface/Users.js"></script>
<script type='text/javascript' src="scripts/validatorstatic.jsp"></script>



<html>
<head>
    <link type="text/css" href="styles/IWMMain.css" rel="stylesheet">


    <style type="text/css">

        img#loginHeader {
            position: absolute;
            margin-left: 50%;
            left: -150px;
            width: 300px;
            top: 50px;

        }

        div#loginBox {
            background-color: #EEE;
            position: absolute;
            margin-left: 50%;
            left: -150px;
            width: 280px;
            border: 1px solid #CCC;
            top: 100px;
            padding: 10px;
            font-size:10pt;
        }

        div#loginBox form {
            margin: 0px;
        }

        div#loginBox td{
            font-size:12px;
        }
    </style>

</head>


<body>

<img src="images/login.gif" id="loginHeader">
<div id="loginBox">

    <div style="color:red;">Password update is required at this time.</div>

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
        <input type="button" class="button" value="Update" onclick="save();">

    </form>
</div>
</body>
</html>



<script type='text/javascript'>
    var theUser = new Object();
    function callback(item){
        theUser = item;
        FormValuesUtil.setFormValues($("PasswordForm"),theUser);
    }
    function finish (message){
        if(message && message.length > 0)
            alert(message);
        else{
            alert("Password has been updated");
            document.location.href='Home.do'
        }
    }
    function save(){
        if(validatePasswordForm($("PasswordForm")) && validateConfirmPassword()){
            FormValuesUtil.getFormValues($("PasswordForm"),theUser);
            Users.saveItem(finish, theUser);
            return true;
        }

    }
    function init(){
        Users.getItem(callback);
    }

    function validateConfirmPassword(){
        if($F('confirmPassword')==$F('password'))
            return true;
        else{
            alert('New Password and Retype New do not match');
            return false;
        }
    }
callOnLoad(init);
</script>

<html:javascript formName="PasswordForm" dynamicJavascript="true" staticJavascript="false"/>
