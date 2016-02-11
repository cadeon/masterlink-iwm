<%-- --%>
<script type="text/javascript" >

</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">
div#worker-manager_organization select {
    width: 200px;
}
</style>

<!--  -->
<div class="popup" id="worker-manager_organization">
<div class="popupHeader">
    <h2>Managing Organization</h2>
</div>

<div class="popupBody">
    <form>
        <table>
        <tr>
            <td><label>Department:</label></td>
            <td>
            <select>
                <option>-- Select Department --</option>
                <option>A Department</option>
            </select>
            </td>
        </tr>

        <tr>
            <td><label>Division:</label></td>
            <td>
            <select>
                <option>-- Select Division --</option>
                <option>A Division</option>
            </select>
            </td>
        </tr>

        <tr>
            <td><label>Group:</label></td>
            <td>
            <select>
                <option>-- Select Group --</option>
                <option>A Group</option>
            </select>
            </td>
        </tr>

        <tr>
            <td><label>Team:</label></td>
            <td>
            <select>
                <option>-- Select Team --</option>
                <option>A Team</option>
            </select>
            </td>
        </tr>

        </table>
      <input type="button" value="Add" class="button">
      <input type="button" value="Cancel" class="button" onClick="thePopupManager.hidePopup();">
    </form>
</div>
</div>

