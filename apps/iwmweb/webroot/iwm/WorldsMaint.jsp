<%@ taglib uri="struts-logic" prefix="logic" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-html-el" prefix="html-el" %>
<%@ taglib uri="struts-tiles" prefix="tiles" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/WebAppStyles.css" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/common.js"></script>

<html:form action="WorldsMaint.do">
    <div class="StaticInfo" width="80%">
        You may only select simulation schema or Base (*) schema. Otherwise you are on your own!
        <br>
        Simulation schema is that has same DDL structure as Base. Below shown all schemas to which base schema owner has access.
        When the simulation module is in place it should read schemas from Worlds table
        <br>
        ConnectionInterceptor must be registered in oracle_ds.xml for world change have any effect
    </div>


    <table class="TableControl" cellpadding="4" cellspacing="1" width="80%" >
        <thead>
            <tr class="BodyTableHeaderRowColor">
                <th class="BodyTableHeaderCellText" valign="top" width="35"></th>
                <th class="BodyTableHeaderCellText" valign="top">Schema Name</th>
                <th class="BodyTableHeaderCellText" valign="top">Schema Desc</th>
                <th class="BodyTableHeaderCellText" valign="top">Created Date</th>
                <th class="BodyTableHeaderCellText" valign="top"></th>
            </tr>
        </thead>
        <tbody class="BodyTableRowColor">
            <nested:iterate id="item" property="worlds" indexId="nIndex">
                <tr>
                    <nested:equal value="true" property="isActive">
                    <td>
                            <html:img page="/images/active.gif" alt="Active" border="0"/>
                    </td>
                    </nested:equal>
                    <nested:equal value="false" property="isActive">
                    <td>
                            <html:img page="/images/inactive.gif" alt="Inactive" border="0"/>
                            <a href="javascript:process('select', '<nested:write property='schemaName' />');">
                                Activate</a>
                    </td>
                    </nested:equal>

                    <td class="BodyTableCellText">
                        <nested:equal value="true" property="isSchemaOwner">(*)</nested:equal>
                        <nested:write property="schemaName"/></td>
                    <td class="BodyTableCellText"><nested:write property="schemaDesc"/></td>
                    <td class="BodyTableCellText"><nested:write property="createdDate"/></td>
                    <td>
                        <a href="javascript:process('delete', '<nested:write property='schemaName' />');">
                            <html:img page="/images/delete.gif" alt="Delete" border="0"/></a>
                    </td>
                </tr>

            </nested:iterate>
        </tbody>
    </table>

    <html:hidden property="selectedItemId"/>
    <html:hidden property="cancelPath"/>
    <html:hidden property="dispatch"/>
    <html:hidden property="forward"/>


</html:form>

<script language="javascript">
    function process(action, id) {
        aform = document.forms[0]
        element = aform.selectedItemId;
        element.value = id;
        setDispatch(action);

        if ('delete' == action) {
            if (isDeleteApproved())  submitForm(aform);
        } else {
            submitForm(aform);
        }
    }
    function cloneWorld(origin, target){
	    if(target){
	        <%/* call to DWRSupport-AJAX*/%>
	        Command.cloneWorld(addRow,origin,target);
	    }
    }
    function addRow(SimResponse sr){
       alert("Adding row"+ sr.name);
    }
</script>


