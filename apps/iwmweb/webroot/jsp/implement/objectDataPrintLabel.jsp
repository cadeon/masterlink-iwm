<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="jstl-core" prefix="c" %>
<%@ taglib uri="struts-nested" prefix="nested" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%@ page import="org.mlink.iwm.util.Config"%>

<script type="text/javascript">

</script>
<!-- FILTER -->
<h2><span>
    <nested:root name="CURRENT_FORM" >
        <nested:nest property="pageContext">
            <nested:notEmpty property="title">
                Object : <nested:write property="title" />
            </nested:notEmpty>
        </nested:nest>
    </nested:root>
</span></h2><br/>
<img src="/images/out.png" alt="Label" /><br/>
<input type="button" class="button" value="Close" onclick="window.close()"/>
