<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>



<body id="trackPage">
	<div id="theBody">
		
		<div id="header">
			<h1>Maintenance Work Request</h1>
		</div>

		<div id="content">

			
			<%-- error message --%>
			<nested:root name="ExternalWorkRequestForm">
				<nested:notEmpty  property="message" >

<%-- move to css --%>
<div style="margin-bottom: 10px;">
<span style="font-weight: bold;background-color:#FEFAE1;font-size:15px;padding: 8px 3px;"><nested:write property="message"/></span>
</div>


				</nested:notEmpty>
			</nested:root>



			<h2>Track Request</h2>

			<h3>Track by Work Request Number</h3>


			<html:form  action="ExternalWorkRequest.do?forward=track" >
				<nested:text property="jobId"/>
				<html:submit  styleClass="button" value="Search" onclick="return validateSearchForm()"  style="margin: 0px;padding: 0px;" />
			</html:form>

			<nested:root name="ExternalWorkRequestForm">
				<nested:notEmpty  property="tenantRequestId" >


					<h3>Status Report</h3>

					<div class="formField">
					<label>Request #:</label>
					<span><nested:write property="jobId"/>&nbsp;</span>
					</div>

					<div class="formField">
						<label>Status:</label>
						<span><nested:write property="status"/>&nbsp;</span>
					</div>

					<div class="formField">
						<label>Created Date:</label>
						<span><nested:write property="createdDate"/>&nbsp;</span>
					</div>

					<div class="formField">
						<label>Dispatched Date:</label>
						<span><nested:write property="dispatchedDate"/>&nbsp;</span>
					</div>
               
					<div class="formField">
						<label>Completed Date:</label>
						<span><nested:write property="completedDate"/>&nbsp;</span>
					</div>

					<div class="formField">
						<label>Submitted By:</label>
						<span><nested:write property="tenantName"/>&nbsp;</span> 
					</div>


					<div class="formField">
						<label>Location:</label>
						<span><nested:write property="locatorName"/> </span>
					</div>

					<div class="formField">
						<label>Category:</label>
						<span><nested:write property="problemDesc"/>&nbsp;</span> 
					</div>

					<div class="formField">
						<label>Description:</label>
						<span><nested:write property="note"/>&nbsp;</span> 
					</div>

					<div class="formField">
						<label>Urgent:</label>
						<span>
							<nested:equal value="1" property="urgent">Yes</nested:equal>
							<nested:notEqual value="1" property="urgent">No</nested:notEqual>
							&nbsp;
						</span> 
					</div>

					<div class="formField">
						<label>Assigned to:</label>
						<span>

							<nested:notEmpty property="workers">
								<nested:iterate id="item"  property="workers" indexId="nIndex" >

									<div>
										<nested:write  property="name" />
										<nested:write  property="phone" />
									</div>

								</nested:iterate>

							</nested:notEmpty>

							<nested:empty property="workers">
								Not currently assigned
							</nested:empty>      
							&nbsp;
						</span> 
					</div>

					<div class="formField">
						<label>Comments:</label>
						<span>

							<table>
							<nested:iterate id="item"  property="actions" indexId="nIndex" >
								<tr>
									<td nowrap="true" valign="top">
										<nested:write  property="verb" />
										<nested:write  property="name" />
										<nested:write  property="modifier" />
									</td>
									<td valign="top">
									<nested:notEmpty property="fieldCondition">
										<nested:write  property="fieldCondition" />
									</nested:notEmpty>

									<nested:empty property="fieldCondition">
										N/A
									</nested:empty>
									</td>
								</tr>
							</nested:iterate>
							</table>
							&nbsp;

						</span> 
					</div>

					<div style="clear:both">
						<input type="button" class="button" onclick="window.print();" value="Print">
						<input type="button" class="button" onclick="window.location.href='<c:url value='/ExternalWorkRequest.do?forward=welcome'/>';" value="Done">
						<input type="button" class="button"  onclick="window.location.href='<%=request.getContextPath()%>/ExternalWorkRequest.do?forward=new';" value="Submit Another">
					</div>


				</nested:notEmpty>
				
				
				<nested:empty  property="tenantRequestId" >

					<input type="button" class="button" onclick="window.location.href='<c:url value='/ExternalWorkRequest.do?forward=welcome'/>';" value="Done">
					<input type="button" class="button"  onclick="window.location.href='<%=request.getContextPath()%>/ExternalWorkRequest.do?forward=new';" value="Submit New Request">
					
				</nested:empty>


			</nested:root>
		</div>
		


		<div id="footer">&copy; 2006 MasterLink Corporation, All Rights Reserved </div>
	</div>


<script type="text/javascript">
	function validateSearchForm(form) {
		formValidationResult = validateRequired(document.forms['ExternalWorkRequestForm']);
		return (formValidationResult == 1);
	}

	function ExternalWorkRequestForm_required () {
		this.a0 = new Array("jobId", "Request Number is required.", new Function ("varName", " return this[varName];"));
	}
</script>



</body>




