<%@ taglib prefix="a" uri="http://java.sun.com/jmaki" %>
<%@ taglib uri="struts-html" prefix="html" %>
<%@ taglib uri="struts-bean" prefix="bean" %>

<%-- This is the first step of Job createion wizard--%>
<script type="text/javascript" >
    var Project;
    var UploadedContentPopup = Class.extend( AbstractCrudPopup, {

        initialize: function(popupId, formName, dataTable){
            this.superInit(popupId, formName, dataTable);
        },
        show: function(filename){
            $("popupTitle").innerHTML= filename;
            thePopupManager.showPopup('uploads_view');
            _this = this;
            Uploads.getItem(function(response){_this.populate(response);}, filename);
        },
        convertExcel: function(filename){
            $("popupTitle").innerHTML= filename;
            thePopupManager.showPopup('uploads_view');
            _this = this;
            Uploads.getExcelItem(function(response){_this.populate(response);}, filename);
        },

        populate: function(item){
            $('file_content').value=item;
        },

        save: function(){
        }
    });

    callOnLoad(function(){
        theUploadedContentPopup = new UploadedContentPopup("uploads_view", "UploadedContentForm", theUploadstable);
    });
</script>

<%--
STYLES FOR THIS SPECIFIC POPUP

MAKE SURE to use a unique identifier for your styles, or the styles might spill over
and have unwanted effects on other elements.  Each style should start with div#[popupId].

--%>
<style type="text/css">

    div#uploads_view {
        width: 720px;
        left: -360px;
    }

    div#uploads_view #file_content{
        left:-10pt;
        font-size:8pt;
    }

</style>

<div class="popup" id="uploads_view">
    <div class="popupHeader"><h2><span id="popupTitle"></span></h2></div>

    <div class="popupBody">
        <form id="UploadedContentForm" action="" name="UploadedContentForm">
          <textarea rows='32' wrap='off' cols="95" id="file_content"
                    style="overflow:auto;width:98%"></textarea>     <!-- wrap is deprecated but does the job in FF and Safari-->
        </form>
        <input type="button" class="button" value="Close" onclick="theUploadedContentPopup.close();"/>
    </div>
</div>

