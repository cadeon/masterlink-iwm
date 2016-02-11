<%@ taglib prefix="a" uri="http://java.sun.com/jmaki"%>
<%@ taglib uri="struts-bean" prefix="bean"%>
<%@ taglib uri="struts-html" prefix="html"%>
<%@ taglib uri="jstl-core" prefix="c"%>
<%@ taglib uri="struts-nested" prefix="nested"%>

<script type='text/javascript'
	src="<%=request.getContextPath()%>/dwr/interface/LocatorFilter.js"></script>


<body id="createPage">
<div id="theBody">
<div id="header">
<h1>Internal Work Request</h1>
</div>

<div id="content"><nested:root name="MaintRequestForm">
	<nested:notEmpty property="message">
		<div class="message"><html:img page="/images/envelope.gif"
			border="0" /> <nested:write property="message" /></div>
	</nested:notEmpty>
</nested:root> <html:form styleId="MaintRequestForm"
	action="TenantRequestMaint.do?forward=submit">
	<h2>Create Internal Work Request</h2>
      
      This request will use the information on file for <b id="theName"></b>. 
      If you are not <b id="theName2"></b>, please <a href="Logout.do">Logout</a>. 
	<p>
	<h3>Work Request Details</h3>
	<span class="sectionComment">NOTE: In case of <u>emergency
	dial 911</u></span>


	<a:ajax id="locatorFilter" type="iwm.filter" name="iwm.filter" />


	<div class="formField"><label>Request Category*</label> <nested:select
		property="problemId" styleId="categoryInput">
		<option value="">-- Select --</option>
		<html:optionsCollection property="options" name="ProblemSelector" />
	</nested:select></div>
	<div class="formField"><label>Description*:</label> <nested:text
		property="jobDescription" styleId="descInput"></nested:text></div>
	<div class="formField"><label>Note*:</label> <nested:textarea
		property="note" styleId="noteInput" onkeypress="verifylength(this);"
		onkeydown="verifylength(this);" />


	<div id="remainingChars">Characters Remaining:<span
		id="remainingCharCount">234</span></div>

	</div>

	<input type="button" class="button"
		onclick="submitForm(document.getElementById('MaintRequestForm')); alert('Request Submitted!');"
		value="Submit Request">


	<html:hidden property="locatorId" styleId="locatorId" />

</html:form></div>
<div id="footer">&copy; 2008 MasterLink Corporation, All Rights
Reserved</div>
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
		
		var theName='<%=session.getAttribute("personName")%>';
		document.getElementById('theName').innerHTML= theName;		
		document.getElementById('theName2').innerHTML= theName;
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

<html:javascript formName="MaintRequestForm" dynamicJavascript="true"
	staticJavascript="false" />



</body>











