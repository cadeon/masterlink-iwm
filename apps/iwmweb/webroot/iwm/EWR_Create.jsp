<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>

<script type='text/javascript' src="<%=request.getContextPath()%>/dwr/interface/LocatorFilter.js"></script>


<body id="createPage">
<div id="theBody">
  <div id="header">
    <h1>Maintenance Work Request</h1>
  </div>

  <div id="content">

      <nested:root name="ExternalWorkRequestForm">
          <nested:notEmpty  property="message" >
              <div class="message">
                  <html:img page="/images/envelope.gif" border="0" />
                  <nested:write property="message"/>
              </div>
          </nested:notEmpty>
      </nested:root>

	<html:form styleId="ExternalWorkRequestForm" action="SubmitExternalWorkRequest.do?forward=submit" >
      <h2>Create Work Request</h2>
      <h3>Requestor's Contact Information</h3>
      <div class="formField">
        <label>Name*:</label>
        <nested:text property="tenantName" styleId="tenantName"/>
      </div>

        <jsp:include page="/iwm/autocomplete_tenantname.jsp"/>


        <!--a:ajax type="scriptaculous.autocomplete" name="scriptaculous.autocompleter" service="_autocomplete_result.html"/-->

      <div class="formField">
        <label>Email*:</label>
        <nested:text property="tenantEmail" styleId="tenantEmail"/>
      </div>
      <div class="formField">
        <label>Phone*:</label>
        <nested:text property="tenantPhone" styleId="tenantPhone"/>
      </div>



        <h3>Work Request Details</h3>
        <span class="sectionComment">NOTE: In case of <u>emergency dial 911</u></span>


        <a:ajax  id="locatorFilter" type="iwm.filter" name="iwm.filter"/>


        <div class="formField">
            <label>Request Category*</label>
            <nested:select property="problemId"  styleId="categoryInput">
                <option value="">  -- Select --  </option>
                            <html:optionsCollection property="options"  name="ProblemSelector"/>
            </nested:select>
        </div>


        <div class="formField">
            <label>Description*:</label>

            <nested:textarea property="note"   styleId="descInput"
                             onkeypress="verifylength(this);" onkeydown="verifylength(this);" />


		<div id="remainingChars">Characters Remaining:<span id="remainingCharCount">234</span></div>

      </div>
      <div class="formField">
        <span class="urgent">
        <html:checkbox value="1" property="urgent"/>
        Yes - My request requires immediate attention </span> </div>


	<input type="button" class="button"
		onclick="if(validateExternalWorkRequestForm(document.ExternalWorkRequestForm)) submitForm(document.getElementById('ExternalWorkRequestForm'));"
		value="Submit Request">


	<input type="button" class="button"
		onclick="window.location.href='<c:url value='/ExternalWorkRequest.do?forward=welcome'/>';"
		value="Cancel">



<html:hidden property="locatorId" styleId="locatorId"/>

</html:form>
  </div>
  <div id="footer">&copy; 2006 MasterLink Corporation, All Rights Reserved </div>
</div>




<script type="text/javascript">
    var maxlength=250;
    var counter = $("remainingCharCount");
    var locatorFilter;
    function init(){
        updateCounter(0);
        //updateLocators('campusInput',null);
        locatorFilter = new LocatorChain("locatorFilter",function(id){$("locatorId").value=id;}, false)
        locatorFilter.populateChain(null);

    }
    function verifylength(source){
        number = source.value.length;
        if(number > maxlength-1) {
            source.value=source.value.substring(0,(maxlength-1));
        }else{
            updateCounter(number);
        }
    }
    function updateCounter(number){
        counter.innerHTML=maxlength-number;
    }
    function updateLocators(dropdownId, parentLocatorId){
        $("locatorId").value=parentLocatorId;
        if(dropdownId==null) return;

        DWRUtil.removeAllOptions(dropdownId);

        if(dropdownId){
            var selectId = dropdownId;
            var populateDropdown = function(data) {
                if(data){
                    $(selectId).disabled = false;
                    DWRUtil.addOptions(selectId, [{ label:' -- Select -- ', value:'' }] , 'value', 'label');
                    DWRUtil.addOptions(selectId, eval(data), 'value', 'label');
                } else {
                    $(selectId).disabled = true;
                }
            }
            <%/* call to DWRSupport-AJAX*/%>
            if(parentLocatorId==null) LocatorFilter.getDescendants(populateDropdown, null);
            else if(parentLocatorId.length!=0) LocatorFilter.getDescendants(populateDropdown,parentLocatorId);
        }
    }



    window.onload=init;

</script>

<html:javascript formName="ExternalWorkRequestForm" dynamicJavascript="true" staticJavascript="false" />



</body>











