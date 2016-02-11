<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<script type='text/javascript' src="../../../resources/lib/prototype.js"></script>
<script type='text/javascript' src="../../../dwr/engine.js"></script>
<script type='text/javascript' src="../../../dwr/util.js"></script>

<a:ajax id="locatorFilter" type="iwm.filter" name="iwm.filter"/>
<p></p>
<a:ajax id="classFilter" type="iwm.filter" name="iwm.filter"/>

<script type='text/javascript'>
    function init(){
        locatorFilter = new LocatorChain("locatorFilter", function(){}, true);
        SessionUtil.getCurrentLocator({
            callback:function(curLocatorId){
                locatorFilter.populateChain(curLocatorId);
            },
            async:false});

        classFilter = new ObjectClassChain("classFilter", function(){}, true);
        SessionUtil.getCurrentClass({
            callback:function(classId){
                classFilter.populateChain(classId);
            },
            async:false});
    }
    setTimeout(init,100);

</script>