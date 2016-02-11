<%--
  Created by IntelliJ IDEA.
  User: jmirick
  Date: Oct 17, 2006
  Time: 11:29:49 AM
  To change this template use File | Settings | File Templates.
--%>

<!-- FILTER -->
<h3>Worker Type</h3>
<div><input type="radio" name="isManager" value="1" checked>Worker is a manager.</div>
<div><input type="radio" name="isManager" value="0">Worker is <u>not</u> a manager.</div>

<!-- Table showing locators that match the filter -->
<h3>Managing Organizations</h3>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="managerOrganizations">


    <colgroup>
        <col class="organizationCol" />
        <col class="deleteCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_organization'>Organization</td>
            <td class="deleteCol">Delete</td>
        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td>Buildings on Zone</td>
            <td><div class="icon"><img src="images/trash_16.gif" alt="Delete"></div></td>
        </tr>
        <tr>
            <td>Buildings on Zone</td>
            <td><div class="icon"><img src="images/trash_16.gif" alt="Delete"></div></td>
        </tr>
    </tbody>
</table>

<input type="button" class="button" value="Add Organization" onclick="thePopupManager.showPopup('worker-manager_organization');"/>
