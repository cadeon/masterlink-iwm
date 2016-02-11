<%@ taglib uri="struts-bean" prefix="bean" %>
<%@ taglib uri="struts-html" prefix="html" %>

<script type="text/javascript" src="scripts/webtoolkit.aim.js"></script>
<script type="text/javascript">
    function startCallback() {
        // make something useful before submit (onStart)
        return true;
    }

    function completeCallback(response) {
        // make something useful after (onComplete)
        //document.getElementById('nr').innerHTML = parseInt(document.getElementById('nr').innerHTML) + 1;
        document.getElementById('r').innerHTML = response;
        $('response').style.display="";
        theattachmentstable.update();
    }

</script>

<style type="text/css">
    div#attachment_upload {
        width: 450px;
        left: -175px;
    }

    div#attachment_upload input{
        margin-left:30px;
        margin-top:5px
    }
</style>

<div class="popup" id="attachment_upload">
    <div class="popupHeader">
        <h2>Upload Attachment</h2>
    </div>


    <div class="popupBody">
        <form name="UploadForm" action="/IWM/Upload-submit.do?forward=attachment" method="post" enctype="multipart/form-data" onsubmit="return AIM.submit(this, {'onStart' : startCallback, 'onComplete' : completeCallback})">
            <p>Select the file that you would like to upload: <br />
                <div><label>File:</label> <input type="file" name="theFile" id="theFile" size="50"/></div> </p>
            <p>
                Enter a new name for the file (optional): <br/>
                 <input type="text" name="theText" size="20" maxlength="30" id="theText"/></p>
            <p>
                Enter a short description for the file (optional):
                <input type="text" name="fileDescription" size="50" maxlength="200" id="desc"/></p>
            <p>
                <input class="button" class="button" style="width:60px"  type="submit" value="Submit" />
                <input type="button" class="button"  style="width:60px" value="Close" onclick="theAttachmentUploadPopup.close();"/>
            </p>
        </form>

        <div id="response">
            <hr/>
            <div id="r"></div>
        </div>
    </div>

    <!--iframe style="display:block;" src="jsp/misc/attachment_upload_frame.jsp"></iframe-->

</div>



<script type="text/javascript" >
    var AttachmentUploadPopup= Class.create();
    AttachmentUploadPopup.prototype = {
        initialize: function(){
            var popupId="attachment_upload";
            new Draggable(popupId);
            this.popupId = popupId;
        },

        show: function() {
            $('theFile').value='';
            $('theText').value='';
            $('desc').value='';
            $('r').innerHTML='';
            $('response').style.display="none";
            thePopupManager.showPopup('attachment_upload');
        },
        close: function() {
            thePopupManager.hidePopup();
        }
    };

    var theAttachmentUploadPopup;
    callOnLoad(function(){
        theAttachmentUploadPopup = new AttachmentUploadPopup();
    });

</script>





