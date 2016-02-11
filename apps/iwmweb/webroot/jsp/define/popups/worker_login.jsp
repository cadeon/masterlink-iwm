<%-- --%>
<script type="text/javascript" >

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
div#worker_login input.textField {
    width: 200px;
}
</style>

<!-- WORKER LOGIN -->
<div class="popup" id="worker_login">
    <div class="popupHeader">
        <h2>Login: WORKER_NAME</h2>
    </div>

    <div class="popupBody">
        <form action="">

            <table>
                <tr>
                    <td>User Name:</td>
                    <td><input type="text" name="textField"></td>
                </tr>

                <tr>
                    <td>Current Password:</td>
                    <td><input type="text" name="textfield3"></td>
                </tr>

                <tr>
                    <td>New Password:</td>
                    <td><input type="text" name="textfield3"></td>
                </tr>

                <tr>
                    <td>Retype Password:</td>
                    <td><input type="text" name="textfield4"></td>
                </tr>

            </table>

            <input type="button" class="button" value="Submit">
            <input type="button" class="button" value="Cancel" onclick="thePopupManager.hidePopup();"/>

        </form>
    </div>
</div>