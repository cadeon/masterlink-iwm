<script type="text/javascript" src="scripts/IWMCommon.js"></script>
<script type='text/javascript' src="dwr/interface/MWLocations.js"></script>
  
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>

<meta http-equiv="Cache-Control" content="no-cache">

<script type='text/javascript'>
var map;

var MWLocationPopup = Class.extend( AbstractCrudPopup, {

    //abstract in base class
    initialize: function(popupId, formName, dataTable){
        this.superInit(popupId, formName, dataTable);
    },

    //custom. you can use base class clean most of the time.
    clean: function(){
        //map.close();
        //thePopupManager.close();
    },

    //abstract in base class
    show: function(workerName, locInfo){
		$("HEADING").innerHTML='Work Location Information for: '+workerName;
           
   		var markersLst = locInfo.split(";");
           var markerInfoLst;
           if(markersLst != null && markersLst.length>0){
           	var i = 0;
            var label = '', lat, lng, mins, point, label, html, marker;
             //,10:24:06 AM,10:24:06 AM,0,1;28.6102422,-81.4244023,10:05:03 AM,10:11:06 AM,.1,4;
            for (i=0;i<markersLst.length;i++){
	            if(markersLst[i]!=''){
	            	markerInfoLst = markersLst[i].split(",");
	            	if(markerInfoLst != null && markerInfoLst.length>0){
	            		lat = parseFloat(markerInfoLst[0]);
	      		      	lng = parseFloat(markerInfoLst[1]);
	      		      	mins= parseFloat(markerInfoLst[4]);
	      		      	hits= parseFloat(markerInfoLst[5]);
	      		      	label = markerInfoLst[2]+' - '+markerInfoLst[3];
		      		    //html = workerName + ' is in this location from '+label+' for '+mins+' mins. <br/>Number of hits on application server are '+hits+'.';

		      		    var myLatlng = new google.maps.LatLng(lat,lng);
		      		    if(i == 0){
		      		      	var myOptions = {
		      	              zoom: 11,
		      	              center: myLatlng,
		      	              mapTypeId: google.maps.MapTypeId.HYBRID
		      	            };
		      	            map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	      		      	}
	      		      	
						var marker = new google.maps.Marker({
		      	        	position: myLatlng, 
		      	        	map: map,
		      	        	title:label
		      	    }); 
	            	}
	            }
            }
            thePopupManager.showPopup('mw_location');   
            google.maps.event.trigger(map, 'resize'); 
   		}
    },

    showOrgLocInfo: function (orgId, orgName) {
        var fillOrgLocInfo = function (response) {
            if(response.items!=null && response.items.length>0){
                var orgCnt = 0;

            	var locInfo, workerName;
            	var map;
            	for (orgCnt=0;orgCnt<response.items.length;orgCnt++){
                	locInfo = response.items[orgCnt].locInfo;
		        	var markersLst = locInfo.split(";");
		            var markerInfoLst;
		            if(markersLst != null && markersLst.length>0){
		            	var i = 0;
			            var label = '', lat, lng, mins, point, label, html, marker;
			             //theo,28.6102422,-81.4244023,11:01 AM,11:02 AM,2,3;
			            for (i=0;i<markersLst.length;i++){
				            if(markersLst[i]!=''){
				            	markerInfoLst = markersLst[i].split(",");
				            	if(markerInfoLst != null && markerInfoLst.length>0){
				            		workerName = markerInfoLst[0];
				            		lat = parseFloat(markerInfoLst[1]);
				      		      	lng = parseFloat(markerInfoLst[2]);
				      		      	mins= parseFloat(markerInfoLst[5]);
				      		      	hits= parseFloat(markerInfoLst[6]);
				      		      	label = workerName+' : '+markerInfoLst[3]+' - '+markerInfoLst[4];
					      		    //html = workerName + ' is in this location from '+label+' for '+mins+' mins. <br/>Number of hits on application server are '+hits+'.';
		
					      		    var myLatlng = new google.maps.LatLng(lat,lng);
					      		    if(i == 0){
					      		      	var myOptions = {
					      	              zoom: 11,
					      	              center: myLatlng,
					      	              mapTypeId: google.maps.MapTypeId.HYBRID
					      	            };
					      	            map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
				      		      	}
				      		      	
									var marker = new google.maps.Marker({
					      	        	position: myLatlng, 
					      	        	map: map,
					      	        	title:label
					      	    }); 
				            	}
				            }
			            }
		    		}
            	}
            	thePopupManager.showPopup('mw_location');
                google.maps.event.trigger(map, 'resize'); 
            }
        }
        $("HEADING").innerHTML='Work Location Information for Organization: '+orgName;

        var searchCriteria = new Object();
        searchCriteria.id=orgId;
        searchCriteria.type='org';
        MWLocations.getData(fillOrgLocInfo, searchCriteria, 0, 1000);
    },

    //abstract in base class
    populate: function(item){

    }
});

var theMWLocationPopup;
callOnLoad(function(){
    theMWLocationPopup = new MWLocationPopup("mw_location", "MWLocation", null);
});
</script>

<style type="text/css">

    div#map_canvas {
    	width: 670px;
        height: 450px;
        left: 0px;
        top: 0px;
        overflow: auto;
    }
    
    div#mw_location_body {
        width: 700px;
        height: 450px;
        overflow: auto;
    }

    div#mw_location {
        width: 700px;
        height: 500px;
        left: -350px;
        top:0px;
    }
</style>

<!-- WORKER SKILLS -->
<div class="popup" id="mw_location">
    <div class="popupHeader" id="MWLocationPopupHeader">
        <h2><span id="HEADING"></span>
        </h2>
    </div>
    <div class="popupBody">
        <form action="" id="MWLocation">
            <div id="mw_location_body">
               <div id="map_canvas"></div>
            </div>
            
            <input type="button" class="button" value="Close" onclick="thePopupManager.hidePopup();"/>
        </form>
    </div>
</div>