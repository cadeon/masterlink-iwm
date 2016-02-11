<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

<nested:root name="ExternalWorkRequestForm">
<body id="listPage">
<div id="theBody">

<div id="header">
<h1>Maintenance Work Request</h1>
</div>

<div id="content">

<h3>Please select from the choices below</h3> 

<input type="button" value="Create New Work Request" class="button" style="margin-top: 5px;width:200px;"  onclick="window.location.href='<%=request.getContextPath()%>/ExternalWorkRequest.do?forward=new';" /><br>
<input type="button" value="Track Existing Work Request" class="button" style="margin-top: 5px;width:200px;" onclick="window.location.href='<%=request.getContextPath()%>/ExternalWorkRequest.do?forward=track';"/>

<nested:notEmpty property="activeRequests">


<h3>Please review current jobs below to see if your request has already been submitted</h3> 

<table id="maintenanceRequests" class="dataTable">

<colgroup>
<col id="requestNumCol" />      
<col id="createdCol" />      
<col id="problemTypeCol" />     
<col id="locationCol" />
<col id="descriptionCol" />    
<col id="submittedCol" />   
</colgroup>

<thead>
<tr>
<th>Request #</th>
<th>Created</th>
<th>Problem Type</th>
<th>Location</th>
<th>Description</th>
<th>Submitted By</th>
</tr>
</thead>

<tbody>

<nested:iterate id="item"  property="activeRequests" indexId="nIndex">
	


   <c:choose>
        <c:when test='${nIndex%2 == 0}'>
	<tr class="shaded">

        </c:when>
        <c:otherwise>
	<tr class="unshaded">

        </c:otherwise>
    </c:choose>


	

		<td>
			<nested:link action="/ExternalWorkRequest.do?forward=track" paramId="jobId" paramProperty="jobId" >
				<nested:write  property="jobId" />
			</nested:link>
		</td>
		<td><nested:write  property="createdDate" /></td>
		<td><nested:write  property="problemDesc" /></td>
		<td><nested:write  property="locatorName" /><nested:write  property="locationComment" /></td>
		<td><nested:write  property="note" /></td>
		<td><nested:write  property="tenantName" /></td>
	</tr>

</nested:iterate>

</tbody>
</table>


</nested:notEmpty>


</div>

<div id="footer">&copy; 2006 MasterLink Corporation, All Rights Reserved </div>
</div>
</body>

</nested:root>