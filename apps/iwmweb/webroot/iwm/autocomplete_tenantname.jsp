<script type='text/javascript' src="<%=request.getContextPath()%>/resources/scriptaculous/scriptaculous.js"></script>

<style type="text/css">
    div.autocomplete {
        position:absolute;
        width:250px;
        background-color:white;
        border:1px solid #888;
        margin:0px;
        padding:0px;
    }
    div.autocomplete ul {
        list-style-type:none;
        margin:0px;
        padding:0px;
    }
    div.autocomplete ul li div.selected { font-size:9pt; background-color: #ffb;}
    div.autocomplete ul li div.extrainfo { font-size:7pt; background-color: gray;}
    div.autocomplete ul li {
        list-style-type:none;
        display:block;
        margin:0;
        padding:2px;
        height:28px;
        cursor:pointer;
    }
</style>

<div id="tenantName_choices" class="autocomplete"></div>
<script type="text/javascript">
    var autocompleter = new Ajax.Autocompleter("tenantName", "tenantName_choices", "iwm/autocomplete_tenantname_service.jsp", {paramName: "tenantName", minChars: 2, select:'selected',afterUpdateElement:updatePhoneEmail,frequency: 0.4});
    function updatePhoneEmail(autocompleteElement, autocompleteChoiceElement){
        var nodes = document.getElementsByClassName('email', autocompleteChoiceElement) || [];
        if(nodes.length>0) $('tenantEmail').value = Element.collectTextNodes(nodes[0]);
        nodes = document.getElementsByClassName('phone', autocompleteChoiceElement) || [];
        if(nodes.length>0) $('tenantPhone').value = Element.collectTextNodes(nodes[0]);
    }
</script>