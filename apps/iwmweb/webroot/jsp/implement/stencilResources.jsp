<%--
  Created by IntelliJ IDEA.
  User: jmirick
  Date: Oct 17, 2006
  Time: 11:29:49 AM
  To change this template use File | Settings | File Templates.
--%>


<!-- Table showing locators that match the filter -->
<h3>Resources</h3>

<%-- JPM: each table should have a unique id.  We will use selectors likec
table#id col.col1 to style the tables. --%>
<table class="dataTable" id="stencilResourcesTable">


    <colgroup>
        <col class="tasksCol" />
        <col class="skillCol" />
        <col class="resourcesCol" />
        <col class="editCol" />
    </colgroup>

    <thead>
        <tr>
            <%-- JPM: IDs are needed in table name for sorting --%>
            <td id='sort_tasks'>Tasks</td>
            <td id='sort_skill'>Skill</td>
            <td id='sort_resources'>Resources</td>
            <td class="editCol" id='sort_edit'>Edit</td>

        </tr>
    </thead>

    <%-- JPM: id dataTableBody is used by js to find and populate the table --%>
    <tbody id="dataTableBody">
        <tr>
            <td>23</td>
            <td>Engineer</td>
            <td>
                this.is.an.org<br>
                another.org.is.here<br>
                this.is.an.org<br>
                another.org.is.here<br>
            </td>
            <td><div class="icon"><img src="images/edit_16.gif"></div></td>
        </tr>
        <tr>
            <td>3</td>
            <td>Painter</td>
            <td>
                this.is.an.org<br>
                another.org.is.here<br>
            </td>
            <td><div class="icon"><img src="images/edit_16.gif"></div></td>

        </tr>
        <tr>
            <td>11</td>
            <td>IT</td>
            <td>
                this.is.an.org<br>
            </td>
            <td><div class="icon"><img src="images/edit_16.gif"></div></td>

        </tr>
    </tbody>
</table>

