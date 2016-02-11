<%@ page import="org.mlink.iwm.struts.action.BaseAction"%>
<%@ page import="org.mlink.iwm.util.Constants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>

<%String userException = Constants.USER_EXCEPTION;%>


<html>
<head>
    <title>Defect Report</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/styles/defect-report.css'/>">
    <script  type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
    <script  type="text/javascript" src="<c:url value='/scripts/validatorstatic.jsp'/>"></script>

    <script type="text/javascript">
        function toggleException(){
            el = document.getElementById('exception-text');
            if(el.style.display==''){
                el.style.display='block';
                document["ic_exc"].src="<c:url value='/images/minus.gif'/>";
            }else{
                el.style.display='';
                document["ic_exc"].src="<c:url value='/images/plus.gif'/>";
            }
        }
    </script>
</head>

<body>
<div id="theBody">
    <div id="header">
        Defect Report
    </div>


    <div id="content">
        <c:if test="${sessionScope.user_exception!=null}">    <!-- see BaseAction.USER_EXCEPTION-->
            <div id="exception"><img src="<c:url value='/images/plus.gif'/>" name="ic_exc" border="0" onclick="toggleException();" alt="Exception details">
                Application generated an error
            </div>
            <div id="exception-text">
                <c:out value="${sessionScope.user_exception}"/>
            </div>
        </c:if>

        <html:form styleId="DefectReportForm" action="DefectReport.do?forward=submit">
            <div id="defectinfo">
                <fieldset>
                    <legend>Submitter Contact Information</legend>
                    <div class="formField">
                        <label for="tenantName">Name*:</label>
                        <input type="text" id="tenantName" name="tenantName" />
                    </div>
                    <div class="formField">
                        <label for="tenantEmail"> Email:</label>
                        <input type="text" id="tenantEmail" name="tenantEmail"/>
                    </div>
                    <div class="formField">
                        <label for="tenantPhone" >Phone:</label>
                        <input type="text" id="tenantPhone" name="tenantPhone" />
                    </div>
                </fieldset>
                <p>
                <fieldset>
                    <legend>Defect Details</legend>
                    <div class="formField">
                        <label>Report Type</label>
                        <select  name="reportType">
                            <option value="Defect">Defect</option>
                            <option value="Enhancement">Enhancement</option>
                            <option value="Question">Question</option>
                        </select>
                    </div>
                    <div class="formField">
                        <label for="issueName" onclick="" title="Short Description">Issue Name*:</label>
                        <input name="issueName" id="issueName" title=" Name your issue here"/>
                    </div>
                    <div class="formField">
                        <label>Description*:</label>
                        <textarea name="note" rows=10 cols=55></textarea>
                    </div>
                </fieldset>
           </div>
        </html:form>

        <div id="greeting">


            MasterLink team is dedicated to deliver a well-tuned and reliable product to our customers.
            Please specify  if you have experienced an application error while performing an operation or have a suggestion  that can improve application usability.
            Each defect or improvement report will be given  our utmost attention.

            <ul> When describing an issue, please follow these
                recommendations:
                <li> Precision -    Please state  what is the exact
                    problem?  </li>
                <li> Compression -  Please describe the issue  clearly
                    and  briefly   </li>
                <li> Source -       Does it appear to be an
                    application error or  could it be a user
                    misunderstanding that needs further clarification?
                </li>
                <li> Impact -  How does this issue  affect your
                    work ( critical, high, medium, low)? </li>
                <li> User Actions - Please state what exact user actions preceded this problem or may have triggered it?<li>
                <li> Work-Around -  Is there a known work-around  that helps you overcome this problem in the mean time ?<li>
                <li> Occurrence -   Can you re-produce this
                    application behaviour at will or it seems to be random or intermittent? </li> </ul>
            <div> Your feedback  is invaluable to us and would help us deliver an outstanding product.
            Thank you for helping us make IWM application even better! </div>

            <button class="button"  onclick="if(validateDefectReportForm(document.DefectReportForm)) submitForm(document.DefectReportForm);"> Submit </button>
            <button class="button"  onclick="if(confirm('Your feedback is very important. Do you still want to close the window?')){ window.close()};"> Close </button>
             
        </div>

    </div>
    <div id="footer">&copy; 2006 MasterLink Corporation, All Rights Reserved </div>

</div>
</body>
<html:javascript formName="DefectReportForm" dynamicJavascript="true" staticJavascript="false" />


</html>