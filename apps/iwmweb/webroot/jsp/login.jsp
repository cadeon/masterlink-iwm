<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-logic" prefix="logic" %>



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
        }

        div#loginBox form {
            margin: 0px;
        }

        div#loginBox td {
            font-family:sans-serif;
        }

        span#rememberme {
            font-family:sans-serif;
            margin-left:5em;margin-right:0.5em;font-size:8pt;
        }
    </style>

</head>


<body>

<img src="images/login.gif" id="loginHeader">
<div id="loginBox">

    <logic:present name="CURRENT_FORM">
        <div style="color:red;"><bean:write name="CURRENT_FORM" property="message"/></div>
    </logic:present>

    <form name ="USERFORM" action="Login.do?forward=auth" method="post" >
        <table>
            <tr>
                <td>User name:</td>
                <td>
                    <input type="text" name="j_username" id="j_username"/>
                    <script type="text/javascript">
                        document.getElementById("j_username").focus();
                    </script>

                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password"  name="j_password"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="Login" class="button"></td>
                <td><span id="rememberme">Remember Me</span><input type="checkbox"  name="rememberMe" value="true"/></td>
            </tr>
        </table>

    </form>
</div>
</body>
</html>